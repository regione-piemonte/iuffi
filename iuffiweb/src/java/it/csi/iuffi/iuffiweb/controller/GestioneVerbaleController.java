package it.csi.iuffi.iuffiweb.controller;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneVerbaleEJB;
import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.VerbaleCampioni;
import it.csi.iuffi.iuffiweb.model.api.VerbaleJson;
import it.csi.iuffi.iuffiweb.model.api.VerbaleTrappole;
import it.csi.iuffi.iuffiweb.model.api.VerbaleVisual;
import it.csi.iuffi.iuffiweb.model.request.MissioneRequest;
import it.csi.iuffi.iuffiweb.model.request.VerbaleDati;
import it.csi.iuffi.iuffiweb.util.InputStreamDataSource;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smrcomms.commws.presentation.rest.client.CommwsJasperClient;
import it.csi.smrcomms.siapcommws.dto.smrcomm.EsitoDocumentoVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDocumentoInputVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsLeggiDocVO;
import it.csi.smrcomms.siapcommws.exception.smrcomm.InvalidParameterException;
import it.csi.smrcomms.siapcommws.exception.smrcomm.ProtocollaSystemException;
import it.csi.smrcomms.siapcommws.exception.smrcomm.UnrecoverableException;

@Controller

@IuffiSecurity(value = "GESTIONEVERBALE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GestioneVerbaleController extends TabelleController {

	// private static final String JASPER_REST_URL =
	// "http://<WEB_SERVER_HOST:PORT>/commws_ref/json/layer/";
	@Autowired
	private ISpecieVegetaleEJB specieEJB;

	@Autowired
	private IOrganismoNocivoEJB onEJB;

	@Autowired
	private ITipoAreaEJB areaEJB;

	@Autowired
	private IGestioneVerbaleEJB verbaleEJB;

	@Autowired
	private IMissioneEJB missioneEJB;

	@Autowired
	private IAnagraficaEJB anagraficaEJB;

	@Autowired
	private ISpecieVegetaleEJB specieVegetaleEJB;

	@Autowired
	private IOrganismoNocivoEJB organismoNocivoEJB;

	@Autowired
	private ITipoAreaEJB tipoAreaEJB;

	@Autowired
	protected SmartValidator validator;

	@Autowired
	private IRicercaEJB ricercaEJB;

	@Autowired
	private IQuadroEJB quadroEJB;

	ResourceBundle res = ResourceBundle.getBundle("config");

	private static final String THIS_CLASS = GestioneVerbaleController.class.getSimpleName();

	@InitBinder
	@Override
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
	}

	@RequestMapping(value = "/gestioneVerbale/showFilter")
	public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws InternalUnexpectedException {
		if (RequestContextUtils.getInputFlashMap(request) != null) {
			model.addAttribute("list", model.asMap().get("list"));
		}
		List<SpecieVegetaleDTO> listaSpecie = specieEJB.findAll();
		model.addAttribute("listaSpecie", listaSpecie);

		List<OrganismoNocivoDTO> listaOn = onEJB.findAll();
		model.addAttribute("listaOn", listaOn);

		List<TipoAreaDTO> listaArea = areaEJB.findAll();
		model.addAttribute("listaArea", listaArea);

		setBreadcrumbs(model, request);
		return "gestioneverbale/ricercaVerbale";
	}

	@RequestMapping(value = "/gestioneVerbale/produceMinutes")
	public String produceMinutes(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws InternalUnexpectedException {
		MissioneRequest mr = new MissioneRequest();
		if (session.getAttribute("missioneRequest") != null) {
			mr = (MissioneRequest) session.getAttribute("missioneRequest");
		}
		if (mr.getAnno() == null) {
			Calendar now = Calendar.getInstance();
			int currentYear = now.get(Calendar.YEAR);
			mr.setAnno(new Long(currentYear));
		}
		model.addAttribute("missioneRequest", mr);
		setBreadcrumbs(model, request);
		loadPopupCombo(model, session);
		session.setAttribute("currentPage", 1);

		List<String> tableNamesToRemove = new ArrayList<>();
		tableNamesToRemove.add("tableVerbali");
		cleanTableMapsInSession(session, tableNamesToRemove);
		return "gestioneverbale/produciVerbale";
	}

	@RequestMapping(value = "/gestioneVerbale/search")
	public String search(Model model, @ModelAttribute("missioneRequest") MissioneRequest missioneRequest,
			HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult)
			throws InternalUnexpectedException {
		try {
			if (missioneRequest.checkNull())
				missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
			if (missioneRequest == null)
				missioneRequest = new MissioneRequest();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// CARICO LE LISTE
		List<Integer> listaTipoArea = missioneRequest.getTipoArea();
		List<Integer> listaSpecie = missioneRequest.getSpecieVegetale();
		List<Integer> listaOrganismo = missioneRequest.getOrganismoNocivo();
		List<Integer> listaIspetAssegnato = missioneRequest.getIspettoreAssegnato();

		if (listaTipoArea != null) {
			String idArea = "";
			for (Integer i : listaTipoArea) {
				idArea += i + ",";
			}
			idArea = idArea.substring(0, idArea.length() - 1);
			List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
			model.addAttribute("listaAree", listaAree);
		}
		// specie
		if (listaSpecie != null) {
			String idSpecie = "";
			for (Integer i : listaSpecie) {
				idSpecie += i + ",";
			}
			idSpecie = idSpecie.substring(0, idSpecie.length() - 1);
			List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
			model.addAttribute("listaSpecie", listaSpecieVeg);
		}
		// organismi
		if (listaOrganismo != null) {
			String idOn = "";
			for (Integer i : listaOrganismo) {
				idOn += i + ",";
			}
			idOn = idOn.substring(0, idOn.length() - 1);
			List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
			model.addAttribute("listaOn", listaOn);
		}
		// ispettori assegnati
		if (listaIspetAssegnato != null) {
			String idIsAss = "";
			for (Integer i : listaIspetAssegnato) {
				idIsAss += i + ",";
			}
			idIsAss = idIsAss.substring(0, idIsAss.length() - 1);
			List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsAss);
			model.addAttribute("listaIspettAssegnati", listaIspettAssegnati);
		}
		model.addAttribute("missioneRequest", missioneRequest);
		session.setAttribute("missioneRequest", missioneRequest);
		model.addAttribute("from", "search");
		setBreadcrumbs(model, request);
		showFlashMessages(model, request);
		return "gestioneverbale/elencoMissioniPerVerbali";
	}

	/*
	 * @RequestMapping(value = "/pdfVerbale") public void
	 * pdfVerbale(HttpServletRequest request,HttpServletResponse response) throws
	 * InternalUnexpectedException, JRException, IOException {
	 * 
	 * InputStream jasperStream =
	 * this.getClass().getClassLoader().getResourceAsStream(
	 * "/jasper/reportMasterVerbali.jasper");
	 * //System.out.println(this.getClass().getClassLoader().getParent().toString())
	 * ; Map<String,Object> params = new HashMap<>(); JasperReport jasperReport =
	 * (JasperReport) JRLoader.loadObject(jasperStream);
	 * jasperReport.setProperty("net.sf.jasperreports.awt.ignore.missing.font",
	 * "true"); Connection conn = null; JasperPrint jasperPrint =
	 * JasperFillManager.fillReport(jasperReport, params, conn);//new
	 * JREmptyDataSource());
	 * 
	 * response.setContentType("application/x-pdf");
	 * response.setHeader("Content-disposition", "inline; filename=test.pdf");
	 * 
	 * final OutputStream outStream = response.getOutputStream();
	 * JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	 * 
	 * }
	 */
/*
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
*/
	@RequestMapping(value = "/gestioneVerbale/print", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<byte[]> produciVerbale(@RequestParam(value = "idMissione") Integer idMissione,
			HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException,
			JsonGenerationException, JsonMappingException, IOException, ParseException, Exception {

		ObjectMapper mapper = new ObjectMapper()
				.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
				.configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

		VerbaleDTO verbaleDTO = new VerbaleDTO();

		UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
		String cf = utente.getCodiceFiscale();
		boolean isIspettore = false;
		for (int h = 0; h < utente.getAbilitazioni().length; h++) {
			if (utente.getAbilitazioni()[h].getLivello() != null) {
				long livello = utente.getAbilitazioni()[h].getLivello().getIdLivello();
				if (livello == IuffiConstants.LivelliPapuaEnum.ISPETTORE_MONITORAGGIO.getIdLivello()) {
					isIspettore = true;
				}
			}
		}
		AnagraficaDTO anagrafica = new AnagraficaDTO();
		Integer idAnagrafica = 0;
		if (isIspettore) {
			anagrafica = anagraficaEJB.getIdAnagraficaFromCf(cf);
			if (anagrafica != null)
				idAnagrafica = anagrafica.getIdAnagrafica();
		}

		List<VerbaleVisual> resultVisual = new ArrayList<VerbaleVisual>();
		List<VerbaleCampioni> resultCampioni = new ArrayList<VerbaleCampioni>();
		List<VerbaleTrappole> resultTrappole = new ArrayList<VerbaleTrappole>();

		MissioneDTO missione = missioneEJB.findById(new Long(idMissione));
		if (!isIspettore) {
			resultVisual = verbaleEJB.getVisual(idMissione);
			resultCampioni = verbaleEJB.getCampioni(idMissione);
			resultTrappole = verbaleEJB.getTrappole(idMissione);
		} else {
			resultVisual = verbaleEJB.getVisualDiCompetenza(idMissione, idAnagrafica);
			resultCampioni = verbaleEJB.getCampioniDiCompetenza(idMissione, idAnagrafica);
			resultTrappole = verbaleEJB.getTrappoleDiCompetenza(idMissione, idAnagrafica);
		}
		VerbaleDati intestazione = verbaleEJB.getIntestazione(idMissione);
		VerbaleDTO verbalePresente = verbaleEJB.findByIdMissione(idMissione);

		List<VerbaleVisual> listVisual = new ArrayList<VerbaleVisual>();
		List<VerbaleCampioni> listCampioni = new ArrayList<VerbaleCampioni>();
		List<VerbaleTrappole> listTrappole = new ArrayList<VerbaleTrappole>();

		//Coordinate coordinate = new Coordinate();
		VerbaleJson verbale = new VerbaleJson();
		VerbaleDati verbaleDati = new VerbaleDati();

		if (verbalePresente != null) {
      /*
			String numVerb = verbalePresente.getNumVerbale();
			String[] arrOfStr = numVerb.split("_");
			String part1 = arrOfStr[0];
			String part2 = arrOfStr[1];
			String part3 = arrOfStr[2];
			int numStampaOdierna = Integer.parseInt(part3) + 1;
			String nuovoNumVerb = part1 + "_" + part2 + "_" + String.valueOf(numStampaOdierna);
			verbaleDati.setNumVerbale(nuovoNumVerb);
			*/
		  verbaleDati.setNumVerbale(verbalePresente.getNumVerbale());   // Modificato il 25/01/2022 (S.D.) - Richiesta dicembre 21 Luca Arculeo
		                                                                // Se si rigenera un verbale esistente deve avere lo stesso numero
		} else {

			//int year = Year.now().getValue();
			int year = Integer.decode(missione.getDataFineMissioneS().substring(6));
			String inizioAnno = "01/01/" + year;
			Date dataInizioAnno = new SimpleDateFormat("dd/MM/yyyy").parse(inizioAnno);
			// long millis=System.currentTimeMillis();
			// java.util.Date currentDate=new java.util.Date(millis);
			// long diffInMillies = Math.abs(currentDate.getTime() -
			// dataInizioAnno.getTime());
			long diffInMillies = Math.abs(missione.getDataOraFineMissione().getTime() - dataInizioAnno.getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
			//String numVerbale = "VERB_" + Long.toString(diff) + "_1";
			String pattern = "VERB_" + Long.toString(diff) + "\\_%";
			Integer progVerbale = verbaleEJB.getProgVerbale(pattern, "\\");
			String numVerbale = "VERB_" + Long.toString(diff) + "_" + progVerbale;
			verbaleDati.setNumVerbale(numVerbale);
		}

    verbaleDati.setData(intestazione.getData());
		verbaleDati.setRilevatore(intestazione.getRilevatore());
		if (intestazione.getOraInizio() != null) // -> da missione
			verbaleDati.setOraInizio(intestazione.getOraInizio());
		else
			verbaleDati.setOraInizio(" - ");
		if (intestazione.getOraFine() != null) // -> da missione
			verbaleDati.setOraFine(intestazione.getOraFine());
		else
			verbaleDati.setOraFine(" - ");

		if (resultVisual != null) {
			for (int i = 0; i < resultVisual.size(); i++) {
				VerbaleVisual visual = new VerbaleVisual();
				visual.setIdVisual(resultVisual.get(i).getIdVisual());
				if (resultVisual.get(i).getComuneVisual() != null)
					visual.setComuneVisual(resultVisual.get(i).getComuneVisual());
				else
					visual.setComuneVisual("");
				visual.setLatVisual(resultVisual.get(i).getLatVisual());
				visual.setLongVisual(resultVisual.get(i).getLongVisual());
				if (resultVisual.get(i).getSpecieVegetaleIndagata() != null)
					visual.setSpecieVegetaleIndagata(resultVisual.get(i).getSpecieVegetaleIndagata());
				else
					visual.setSpecieVegetaleIndagata("");
				if (resultVisual.get(i).getTipologiaArea() != null)
					visual.setTipologiaArea(resultVisual.get(i).getTipologiaArea());
				else
					visual.setTipologiaArea("");
				if (resultVisual.get(i).getOrganismiNociviVisual() != null)
					visual.setOrganismiNociviVisual(resultVisual.get(i).getOrganismiNociviVisual());
				else
					visual.setOrganismiNociviVisual("");
				if (resultVisual.get(i).getNoteVisual() != null)
					visual.setNoteVisual(resultVisual.get(i).getNoteVisual());
				else
					visual.setNoteVisual("");

				listVisual.add(visual);
			}

			verbaleDati.setVisual(listVisual);
		}
		if (resultCampioni != null) {
			for (VerbaleCampioni verbaleCampioni : resultCampioni) {
				VerbaleCampioni campioni = new VerbaleCampioni();
				if (verbaleCampioni.getOrganismiNociviCamp() != null)
					campioni.setOrganismiNociviCamp(verbaleCampioni.getOrganismiNociviCamp());
				else {

					campioni.setOrganismiNociviCamp("");
				}
				if (verbaleCampioni.getCodiceCampioni() != null)
					campioni.setCodiceCampioni(verbaleCampioni.getCodiceCampioni());
				else
					campioni.setCodiceCampioni("");
				
				campioni.setnCampioni(verbaleCampioni.getnCampioni());
				
				if (verbaleCampioni.getTipologiaCampione() != null)
					campioni.setTipologiaCampione(verbaleCampioni.getTipologiaCampione());
				else
					campioni.setTipologiaCampione("");

				campioni.setIdCampionamento(verbaleCampioni.getIdCampionamento());
				
				if (verbaleCampioni.getNote() != null)
				  campioni.setNote(verbaleCampioni.getNote());
				else
				  campioni.setNote("");
				
				listCampioni.add(campioni);
			}
			verbaleDati.setCampioni(listCampioni);
		}

		if (resultTrappole != null) {
			for (int k = 0; k < resultTrappole.size(); k++) {
				VerbaleTrappole trappole = new VerbaleTrappole();
				// trappole.setOrganismoNocivo(resultTrappole.get(k).); //passare gli organismi
				// nocivi
				if (resultTrappole.get(k).getTipologiaTrappola() != null)
					trappole.setTipologiaTrappola(resultTrappole.get(k).getTipologiaTrappola());
				else
					trappole.setTipologiaTrappola("");
				trappole.setLatitudine(resultTrappole.get(k).getLatitudine());
				trappole.setLongitudine(resultTrappole.get(k).getLongitudine());
				if (resultTrappole.get(k).getRaccolta() != null)
					trappole.setRaccolta(resultTrappole.get(k).getRaccolta());
				else
					trappole.setRaccolta("");
				if (resultTrappole.get(k).getCodiceTrappola() != null)
					trappole.setCodiceTrappola(resultTrappole.get(k).getCodiceTrappola());
				else
					trappole.setCodiceTrappola("");
				if (resultTrappole.get(k).getNote() != null)
					trappole.setNote(resultTrappole.get(k).getNote());
				else
					trappole.setNote("");

				listTrappole.add(trappole);
			}
			verbaleDati.setTrappole(listTrappole);
		}

		// passo al mapper
		verbale.setVerbale(verbaleDati);
		String obj = mapper.writeValueAsString(verbale);

		String codiceStampa = "IUFFI_VERBALE";

		it.csi.smrcomms.commws.dto.StampeJrWSEsitoOut output = generaStampaJasper(codiceStampa, obj, null);

		if (output.getEsito() == null || !"000".equalsIgnoreCase(output.getEsito())) {
			throw new InternalException(output.getDescrizioneEsito());
		}

		byte[] pdf = null;
		try {
			pdf = output.getFileReport();
			logger.debug("lunghezza pdf = " + pdf.length);
		} catch (Exception e1) {
			  e1.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		//String filename = verbaleDati.getNumVerbale() + ".pdf";
		//headers.add("Content-Disposition", "attachment; filename=" + filename);
		headers.add("Content-Type", "application/pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		verbaleDTO.setIdMissione(idMissione);
		verbaleDTO.setIdIspettore(missione.getIdIspettoreAssegnato().intValue());
		verbaleDTO.setPdfVerbale(pdf);
		verbaleDTO.setPdfJasper(pdf);
		
		try {
			verbaleDTO.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
		} catch (Exception e) {
			  verbaleDTO.setExtIdUtenteAggiornamento(0L);
		}
		
		verbaleDTO.setNumVerbale(verbaleDati.getNumVerbale());
		verbaleDTO.setIdStatoVerbale(1);

		if (verbalePresente != null) {
			verbaleDTO.setIdVerbale(verbalePresente.getIdVerbale());
			logger.info("GestioneVerbaleController - riga 510");
			verbaleEJB.update(verbaleDTO);
			logger.info("GestioneVerbaleController - riga 512");
		} else
		    verbaleEJB.insert(verbaleDTO);

    headers.add("Content-Disposition", "inline; filename=" + verbaleDati.getNumVerbale() + ".pdf");
    headers.add("Content-Type", "application/pdf");
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
  public <StampeJrWSEsitoOut> StampeJrWSEsitoOut generaStampaJasper(String codiceReport, String oggettoStampa,
			String rootName) throws Exception {
		String JASPER_REST_URL = null;
		StampeJrWSEsitoOut output = null;
		try {
			JASPER_REST_URL = res.getString("jasper.baseurl");
			logger.info("JASPER_URL: " + JASPER_REST_URL);
			CommwsJasperClient client = new CommwsJasperClient(JASPER_REST_URL);
			output = (StampeJrWSEsitoOut) client.generaStampa(IuffiConstants.TIPO_PROCEDIMENTO_AGRICOLO.IUFFI,
					codiceReport, oggettoStampa, null);

		} catch (Exception e) {
			  e.printStackTrace();
		}
		return output;
	}

	@RequestMapping(value = "/gestioneVerbale/getMissioneJson", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public String getMissioneJson(HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException {

		MissioneRequest missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
		List<MissioneDTO> lista = missioneEJB.findMissioneForVerbali(missioneRequest,dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
		
		if (lista == null) {
			lista = new ArrayList<>();
		}

		// session.removeAttribute("missioneRequest");
		ObjectMapper mapper = new ObjectMapper()
				.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
				.configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

		String obj = mapper.writeValueAsString(lista);
		return obj;
	}

	@RequestMapping(value = "/gestioneVerbale/searchVerbale")
	public String searchVerbale(Model model, @ModelAttribute("missioneRequest") MissioneRequest missioneRequest,
			HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult)
			throws InternalUnexpectedException {
		try {
			if (missioneRequest.checkNull())
				missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
//    String cf = utente.getCodiceFiscale();
//    boolean isIspettore = false;
//    for(int h=0; h<utente.getAbilitazioni().length; h++) {
//      long livello = utente.getAbilitazioni()[h].getLivello().getIdLivello();
//      if(livello == 6005)
//        isIspettore = true;
//    }

		List<VerbaleDTO> missioni = missioneEJB.findVerbaliByMissione(missioneRequest);
		setBreadcrumbs(model, request);
		model.addAttribute("missioni", missioni);
		// CARICO LE LISTE
		List<Integer> listaTipoArea = missioneRequest.getTipoArea();
		List<Integer> listaSpecie = missioneRequest.getSpecieVegetale();
		List<Integer> listaOrganismo = missioneRequest.getOrganismoNocivo();
		List<Integer> listaIspetAssegnato = missioneRequest.getIspettoreAssegnato();
		List<Integer> listaIspetSecondario = missioneRequest.getIspettoriSecondari();
		if (listaTipoArea != null) {
			String idArea = "";
			for (Integer i : listaTipoArea) {
				idArea += i + ",";
			}
			idArea = idArea.substring(0, idArea.length() - 1);
			List<TipoAreaDTO> listaAree = areaEJB.findByIdMultipli(idArea);
			model.addAttribute("listaAree", listaAree);
		}
		// specie
		if (listaSpecie != null) {
			String idSpecie = "";
			for (Integer i : listaSpecie) {
				idSpecie += i + ",";
			}
			idSpecie = idSpecie.substring(0, idSpecie.length() - 1);
			List<SpecieVegetaleDTO> listaSpecieVeg = specieEJB.findByIdMultipli(idSpecie);
			model.addAttribute("listaSpecie", listaSpecieVeg);

		}
		// organismi
		if (listaOrganismo != null) {
			String idOn = "";
			for (Integer i : listaOrganismo) {
				idOn += i + ",";
			}
			idOn = idOn.substring(0, idOn.length() - 1);
			List<OrganismoNocivoDTO> listaOn = onEJB.findByIdMultipli(idOn);
			model.addAttribute("listaOn", listaOn);
		}

		// ispettori assegnati
		if (listaIspetAssegnato != null) {
			String idIsAss = "";
			for (Integer i : listaIspetAssegnato) {
				idIsAss += i + ",";
			}
			idIsAss = idIsAss.substring(0, idIsAss.length() - 1);
			List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsAss);
			model.addAttribute("listaIspettAssegnati", listaIspettAssegnati);
		}
		// ispettori secondari
		if (listaIspetSecondario != null) {
			String idIsSec = "";
			for (Integer i : listaIspetSecondario) {
				idIsSec += i + ",";
			}
			idIsSec = idIsSec.substring(0, idIsSec.length() - 1);
			List<AnagraficaDTO> listaIspettSecondari = anagraficaEJB.findByIdMultipli(idIsSec);
			model.addAttribute("listaIspettSecondari", listaIspettSecondari);
		}
		model.addAttribute("missioneRequest", missioneRequest);
		session.setAttribute("missioneRequest", missioneRequest);
		return "gestioneverbale/elencoVerbali";
	}

	@RequestMapping(value = "/gestioneVerbale/getVerbaleJson", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public String getVerbaleJson(HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException {

		MissioneRequest missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
		List<VerbaleDTO> lista = missioneEJB.findVerbaliByMissione(missioneRequest);
		if (lista == null) {
			lista = new ArrayList<>();
		}

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				byte[] nullPdf = null;
				lista.get(i).setPdfVerbale(nullPdf);
			}
		}

		// session.removeAttribute("missioneRequest");
		ObjectMapper mapper = new ObjectMapper()
				.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
				.configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
		// mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		String obj;
		obj = mapper.writeValueAsString(lista);

		return obj;
	}

	@RequestMapping(value = "/rest/pdf-trasferta/{idVerbale}", method = RequestMethod.GET, headers = {
			"Accept=*/*" }, produces = "application/pdf")
	public ResponseEntity<?> getPdfVerbale(@PathVariable(value = "idVerbale") Integer idVerbale,
			HttpServletRequest request, HttpServletResponse response) {

		VerbaleDTO verbale = null;
		ResponseEntity<byte[]> resp = null;
		try {
			logger.debug("getPdfTrasferta");
			verbale = verbaleEJB.findById(idVerbale);
			if (verbale != null) {
				byte[] pdf = verbaleEJB.getPdfVerbale(idVerbale);
				// if (missione.getPdfTrasferta() == null ||
				// missione.getPdfTrasferta().isEmpty()) {
				if (pdf == null || pdf.length == 0) {
					ErrorResponse er = new ErrorResponse();
					er.addError("pdf trasferta", "non presente");
					er.setMessage("Pdf trasferta non presente");
					return new ResponseEntity<ErrorResponse>(er, HttpStatus.NOT_FOUND);
				}
				// byte[] contents = missione.getPdfTrasferta().getBytes();
				byte[] encoded = java.util.Base64.getEncoder().encode(pdf);
				resp = new ResponseEntity<byte[]>(encoded, HttpStatus.OK);
			} else {
				ErrorResponse er = new ErrorResponse();
				er.addError("idMissione", "id missione non valido");
				er.setMessage("Errore nella ricerca della missione");
				return new ResponseEntity<ErrorResponse>(er, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Errore nel metodo getPdfTrasferta: " + e.getMessage());
			ErrorResponse er = new ErrorResponse();
			er.addError("Errore", e.getMessage());
			er.setMessage("Errore interno nella ricerca della missione");
			return new ResponseEntity<ErrorResponse>(er, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@RequestMapping(value = "/gestioneVerbale/getpdf", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<byte[]> getPDF(@RequestParam(value = "idVerbale") Integer idVerbale,
			HttpServletResponse response) {

		// MissioneDTO missione = null;
		// ResponseEntity<byte[]> response = null;
		try {
			logger.debug("getPDF");
			// missione = missioneEJB.findById(idMissione);
			// byte[] contents = missione.getPdfTrasferta().getBytes();
			byte[] pdf = verbaleEJB.getPdfVerbale(idVerbale);
			VerbaleDTO verbale = verbaleEJB.findById(idVerbale);

			HttpHeaders headers = new HttpHeaders();
			// MediaType mediaType = MediaType.parseMediaType("application/pdf");
			// headers.setContentType(mediaType);
			// Here you have to set the actual filename of your pdf
			//String filename = "verbale.pdf";
			// headers.setContentDispositionFormData("inline", filename);

			headers.add("Content-Disposition", "inline; filename=" + verbale.getNumVerbale() + ".pdf");
			headers.add("Content-Type", "application/pdf");
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);
			
		} catch (Exception e) {
			  e.printStackTrace();
			  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	protected EsitoDocumentoVO archiviaProtocollaVerbale(VerbaleDTO verbaleDTO, MultipartFile stampaFirmata,
			long idUtente) throws InternalUnexpectedException, ApplicationException {
		final String THIS_METHOD = "archiviaProtocollaVerbale";
		Integer codice = null;
		final long idVerbale = verbaleDTO.getIdVerbale();

		try {
			SiapCommWsDocumentoInputVO inputVO = IuffiUtils.WS.getSiapCommDocumentoVOPerArchiviazioneVerbali(verbaleDTO,
					"verbale.pdf", idUtente);

			DataHandler handler = new DataHandler(
					new InputStreamDataSource(new ByteArrayInputStream(stampaFirmata.getBytes())));

			EsitoDocumentoVO esito = IuffiUtils.WS.getSiapComm().archiviaProtocollaDocumento(inputVO, handler);

			// esito.getIdDocumentoIndex() -> da salvare nel db e abbiamo l'id salvato su
			// siap per eventuali ricerche
			codice = esito == null ? null : esito.getEsitoVO().getCodice();
			if (codice != null && codice == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS) {
				logger.info("[" + THIS_CLASS + "." + THIS_METHOD + "] Il verbale " + idVerbale
						+ " è stata archiviata con idDocumentoIndex = " + esito.getIdDocumentoIndex());
				return esito;
			} else {
				String messaggioErrore = null;
				if (esito == null || esito.getEsitoVO() == null) {
					messaggioErrore = "esito==null";
				} else {
					messaggioErrore = esito.getEsitoVO().getMessaggio();
				}
				final String errorMessage = "Si è verificato un errore nel richiamo dels servizio archiviaProtocollaDocumento() di agriwell. Il messaggio di errore restituito è: "
						+ messaggioErrore;
				logger.error("[" + THIS_CLASS + "." + THIS_METHOD + " " + errorMessage);
				// Registro l'errore
				// E rilancio un'eccezione "pulita" (senza quindi informazioni tecniche,
				// già registrate su db) che verrà
				// visualizzata all'utente
				throw new ApplicationException(
						"Si è verificato un errore nella registrazione del verbale sul sistema documentale. Se il problema persistesse si prega di contattare l'assistenza tecnica");
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (InvalidParameterException | ProtocollaSystemException | MalformedURLException
				| UnrecoverableException e) {
			logger.error("[" + THIS_CLASS + "." + THIS_METHOD
					+ " Si è verificato un errore nel richiamo del servizio archiviaProtocollaDocumento() di agriwell. Il messaggio di errore restituito è: "
					+ e.getMessage());
			throw new ApplicationException(
					"Si è verificato un errore nella registrazione/protocollazione della lista di liquidazione sul sistema documentale. Se il problema persistesse si prega di contattare l'assistenza tecnica");
		} catch (Exception e) {
			logger.error("[" + THIS_CLASS + "." + THIS_METHOD
					+ " Si è verificato un errore generico nell'invio della lista di liquidazione sul documentale di agriwell. Il messaggio di errore restituito è: "
					+ e.getMessage());
			throw new ApplicationException(
					"Si è verificato un errore nella registrazione/protocollazione della lista di liquidazione sul sistema documentale. Se il problema persistesse si prega di contattare l'assistenza tecnica");
		}
	}

	@RequestMapping(value = "/gestioneVerbale/scaricaVerbale", method = RequestMethod.GET, produces = "application/pdf")
	protected ResponseEntity<byte[]> scaricaVerbaleArchiviato(@RequestParam(value = "idVerbale") Integer idVerbale,
			HttpServletRequest request, HttpServletResponse response) {
		
		UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
		long idUtente = utente.getIdUtenteLogin();
		byte[] pdf = new byte[8192];
		HttpHeaders headers = new HttpHeaders();
		String filename;
		
		try {
			VerbaleDTO verbale = verbaleEJB.findById(idVerbale);
			long idDocumentoIndex = verbale.getExtIdDocumentoIndex();

			SiapCommWsLeggiDocVO leggiDoc = new SiapCommWsLeggiDocVO();
			leggiDoc.setFlagSbustaDocFirmato("N");
			leggiDoc.setIdDocumentoIndex(idDocumentoIndex);
			leggiDoc.setIdUtente(idUtente);

			DataHandler dataHandler = IuffiUtils.WS.getSiapComm().leggiFileAuth(leggiDoc);
	
	    byte[] b = new byte[8192];
	    InputStream is = dataHandler.getInputStream();

        ByteArrayOutputStream dest = new ByteArrayOutputStream();

       	int count = 0;
        while ((count = is.read(b)) != -1) {
            dest.write(b, 0, count);
        }
        is.close();
        dest.flush();
        dest.close();

        pdf = dest.toByteArray();

        filename = verbale.getNumVerbale() + ".pdf";

        headers.add("Content-Disposition", "inline; filename=" + filename);
        headers.add("Content-Type", "application/pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	                
		} catch (Exception e) {
			  e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);
	}
	
    @RequestMapping(value = "/gestioneVerbale/caricaVerbale")
    public String caricaVerbale(Model model, @RequestParam(value = "idVerbale") String idVerbale,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
    {
       model.addAttribute("idVerbale",idVerbale);
       return "gestioneverbale/archiviaVerbale";
    }

    @RequestMapping(value = "/gestioneVerbale/archivia", method = RequestMethod.POST)
    public RedirectView archiviaVerbale(Model model, @ModelAttribute("verbale") VerbaleDTO verbale,
        HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session) {

      Errors errors = new Errors();

      UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
      long idUtente = utente.getIdUtenteLogin();

      if (StringUtils.isNotBlank(verbale.getNomeFile())) {
        String regexp = "^[A-Za-z0-9\\.\\-_]+$";
        if (!verbale.getNomeFile().toUpperCase().endsWith(".PDF") || !verbale.getNomeFile().matches(regexp) || verbale.getNomeFile().length() > 80) {
          errors.addError("nomeFile", "Nome file o estensione non valida. Sono ammessi solo file pdf con: lettere, numeri, .  -  _  (max 80 car.)");
        }
      }
      else
      {
        errors.addError("nomeFile", "Campo obbligatorio");
      }

      if(!errors.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "Nome file o estensione non valida. Sono ammessi solo file pdf con: lettere, numeri, .  -  _  (max 80 car.)");
        model.addAttribute("success", null);
        model.addAttribute("from", "search");
/*
        try {
          setBreadcrumbs(model, request);
        } catch (InternalUnexpectedException e) {
          e.printStackTrace();
        }
*/
        return new RedirectView("search.do", true);
      }
      else
      {
        try {
          //VerbaleDTO verbaleDTO = verbaleEJB.findById(idVerbale);
          logger.debug("getPDF");
          // missione = missioneEJB.findById(idMissione);
          // byte[] contents = missione.getPdfTrasferta().getBytes();
          MultipartFile pdfMultipart = verbale.getPdfVerbale();

          //byte[] pdf = pdfMultipart.getBytes();

          HttpHeaders headers = new HttpHeaders();
          // MediaType mediaType = MediaType.parseMediaType("application/pdf");
          // headers.setContentType(mediaType);
          // Here you have to set the actual filename of your pdf
          String filename = "verbale.pdf";
          // headers.setContentDispositionFormData("inline", filename);
          headers.add("Content-Disposition", "inline; filename=" + filename);
          headers.add("Content-Type", "application/pdf");
          headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

          EsitoDocumentoVO esito = archiviaProtocollaVerbale(verbale, pdfMultipart, idUtente);
          verbale.setExtIdDocumentoIndex(esito.getIdDocumentoIndex());
          verbale.setIdStatoVerbale(IuffiConstants.IUFFI.VERBALE_ARCHIVIATO);
          try {
            verbale.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
          } catch (Exception e) {
            verbale.setExtIdUtenteAggiornamento(0L);
          }

          verbaleEJB.updateStatoVerbale(verbale);

          return new RedirectView("search.do", true);
          
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("search.do", true);
        }
      }
    }

	private void loadPopupCombo(Model model, HttpSession session) throws InternalUnexpectedException {
		MissioneRequest missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");

		// Tipo area
		List<TipoAreaDTO> all_tipoArea = null;
		if (session.getAttribute("checkboxAllTipoAree") != null
				&& ((String) session.getAttribute("checkboxAllTipoAree")).equals("true")) {
			all_tipoArea = tipoAreaEJB.findAll();
		} else {
			all_tipoArea = tipoAreaEJB.findValidi();
		}
		model.addAttribute("all_tipoArea", all_tipoArea);
		// Ispettore assegnato
		List<AnagraficaDTO> all_ispettoreAssegnato = null;
		if (session.getAttribute("checkboxAllIspettoriAssegnati") != null
				&& ((String) session.getAttribute("checkboxAllIspettoriAssegnati")).equals("true")) {
			all_ispettoreAssegnato = anagraficaEJB.findAll();
		} else {
			all_ispettoreAssegnato = anagraficaEJB.findValidi();
		}
		model.addAttribute("all_ispettoreAssegnato", all_ispettoreAssegnato);
		// Specie vegetale
		List<SpecieVegetaleDTO> all_specieVegetale = null;
		if (session.getAttribute("checkboxAllSpecieVegetali") != null
				&& ((String) session.getAttribute("checkboxAllSpecieVegetali")).equals("true")) {
			all_specieVegetale = specieVegetaleEJB.findAll();
		} else {
			all_specieVegetale = specieVegetaleEJB.findValidi();
		}
		model.addAttribute("all_specieVegetale", all_specieVegetale);
		// Organismo nocivo
		List<OrganismoNocivoDTO> all_organismoNocivo = null;
		if (session.getAttribute("checkboxAllOrganismiNocivi") != null
				&& ((String) session.getAttribute("checkboxAllOrganismiNocivi")).equals("true")) {
			all_organismoNocivo = organismoNocivoEJB.findAll();
		} else {
			all_organismoNocivo = organismoNocivoEJB.findValidi();
		}
		model.addAttribute("all_organismoNocivo", all_organismoNocivo);
		//

		List<TipoAreaDTO> tipoAree = new ArrayList<>();
		model.addAttribute("tipoAree", tipoAree);

		if (missioneRequest != null) {
			// Tipo aree
			if (missioneRequest.getTipoArea() != null) {
				String idArea = "";
				for (Integer i : missioneRequest.getTipoArea()) {
					idArea += i + ",";
				}
				idArea = idArea.substring(0, idArea.length() - 1);
				List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
				model.addAttribute("tipoAree", listaAree);
			}
			// Specie vegetali
			if (missioneRequest.getSpecieVegetale() != null) {
				String idSpecie = "";
				for (Integer i : missioneRequest.getSpecieVegetale()) {
					idSpecie += i + ",";
				}
				idSpecie = idSpecie.substring(0, idSpecie.length() - 1);
				List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
				model.addAttribute("listaSpecieVegetali", listaSpecieVeg);
			}
			// Organismi nocivi
			if (missioneRequest.getOrganismoNocivo() != null) {
				String idOn = "";
				for (Integer i : missioneRequest.getOrganismoNocivo()) {
					idOn += i + ",";
				}
				idOn = idOn.substring(0, idOn.length() - 1);
				List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
				model.addAttribute("listaON", listaOn);
			}
			// ispettori assegnati
			if (missioneRequest.getIspettoreAssegnato() != null && missioneRequest.getIspettoreAssegnato().size() > 0) {
				String idIsSec = "";
				for (Integer i : missioneRequest.getIspettoreAssegnato()) {
					idIsSec += i + ",";
				}
				idIsSec = idIsSec.substring(0, idIsSec.length() - 1);
				List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
				model.addAttribute("listaIspettori", listaIspettAssegnati);
			}
			// comune
			if (StringUtils.isNotBlank(missioneRequest.getIstatComune())) {
				ComuneDTO comuneDTO = quadroEJB.getComune(missioneRequest.getIstatComune());
				if (comuneDTO != null) {
					model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
					model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
				}
			}
		}

	}

	@RequestMapping(value = "/gestioneVerbale/searchComuni", produces = "application/json", method = RequestMethod.POST)
	public Map<String, Object> searchComuni(HttpSession session, HttpServletRequest request)
			throws InternalUnexpectedException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		String prov = request.getParameter("provSceltaComune");
		String comune = request.getParameter("descComune");
		List<ComuneDTO> comuni = ricercaEJB.searchComuni(prov, comune);
		if (comuni != null && comuni.size() == 1) {
			values.put("oneResult", "true");
			values.put("prov", comuni.get(0).getSiglaProvincia());
			values.put("comune", comuni.get(0).getDescrizioneComune());
		} else {
			values.put("comuniDTO", comuni);
		}
		values.put(IuffiConstants.PAGINATION.IS_VALID, "true"); // Serve al
		// javascript per
		// capire che è
		// non ci sono
		// stati errori
		// nella
		// creazione del
		// file json
		return values;
	}

	@RequestMapping(value = "/gestioneVerbale/clearFilter")
	public String clearFilter(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws InternalUnexpectedException {
		session.removeAttribute("missioneRequest");
		return "redirect:produceMinutes.do";
	}
	
   @RequestMapping(value = "/gestioneVerbale/confermaConsolida")
   public String confermaConsolida(Model model, @RequestParam(value = "idVerbale") String idVerbale,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
   {
       model.addAttribute("idVerbale",idVerbale);
       return "gestioneverbale/confermaConsolidaBozzaVerbale";
   }
	
	@RequestMapping(value = "/gestioneVerbale/consolida", method = RequestMethod.GET, produces = "application/json")
	public RedirectView consolidaVerbale(@RequestParam(value = "idVerbale") Integer idVerbale, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException,
			JsonGenerationException, JsonMappingException, IOException, ParseException, Exception {
		try {
			VerbaleDTO verbaleDTO = verbaleEJB.findById(idVerbale);
			verbaleDTO.setIdStatoVerbale(IuffiConstants.IUFFI.VERBALE_CONSOLIDATO);
			verbaleEJB.updateStatoVerbale(verbaleDTO);
			
			//model.addAttribute("missioneRequest", missioneRequest);
			//session.setAttribute("missioneRequest", missioneRequest);
			//model.addAttribute("from", "search");
			//setBreadcrumbs(model, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RedirectView("search.do", true);
	}

}
