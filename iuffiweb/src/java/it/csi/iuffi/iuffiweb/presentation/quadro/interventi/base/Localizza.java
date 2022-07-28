package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FileAllegatoInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaParticelle;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.SuperficieConduzione;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class Localizza extends LocalizzazioneJSON
{
  @Autowired
  IInterventiEJB interventiEJB;
  ObjectWriter   jacksonWriter = new ObjectMapper()
      .writer(new DefaultPrettyPrinter());

  public abstract String getFlagEscludiCatalogo();

  @RequestMapping(value = "/mappe_file_{idIntervento}", method = RequestMethod.GET)
  public String mappeFile(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    return "interventi/mappeFile";
  }

  @RequestMapping(value = "/allega_file_{idIntervento}", method = RequestMethod.GET)
  public String allegaFile(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    return "interventi/allegaFile";
  }

  @RequestMapping(value = "/visualizza_allegato_{idIntervento}_{idFileAllegatiIntervento}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> visualizzaAllegato(Model model,
      HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento,
      @ModelAttribute("idFileAllegatiIntervento") @PathVariable("idFileAllegatiIntervento") Long idFileAllegatiIntervento)
      throws InternalUnexpectedException
  {
    FileAllegatoInterventoDTO file = interventiEJB
        .getFileFisicoAllegato(getIdProcedimentoOggetto(request.getSession()),
            idIntervento, idFileAllegatiIntervento);
    HttpHeaders httpHeaders = new HttpHeaders();

    ResponseEntity<byte[]> response = null;
    if (file == null)
    {
      response = new ResponseEntity<byte[]>(null, httpHeaders,
          HttpStatus.NOT_FOUND);
    }
    else
    {
      httpHeaders.add("Content-type",
          IuffiUtils.FILE.getMimeType(file.getNomeFisico()));
      httpHeaders.add("Content-Disposition",
          "attachment; filename=\"" + file.getNomeFisico() + "\"");
      response = new ResponseEntity<byte[]>(file.getFileAllegato(), httpHeaders,
          HttpStatus.OK);
    }
    return response;
  }

  @RequestMapping(value = "/elimina_allegato_{idIntervento}_{idFileAllegatiIntervento}", method = RequestMethod.GET)
  public String eliminaAllegato(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento,
      @ModelAttribute("idFileAllegatiIntervento") @PathVariable("idFileAllegatiIntervento") Long idFileAllegatiIntervento)
      throws InternalUnexpectedException
  {
    return "interventi/confermaEliminaAllegato";
  }

  @RequestMapping(value = "/elimina_allegato_{idIntervento}_{idFileAllegatiIntervento}", method = RequestMethod.POST)
  public String eliminaAllegatoPost(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento,
      @ModelAttribute("idFileAllegatiIntervento") @PathVariable("idFileAllegatiIntervento") Long idFileAllegatiIntervento)
      throws InternalUnexpectedException
  {
    ArrayList<Long> ids = new ArrayList<Long>();
    ids.add(idFileAllegatiIntervento);
    HttpSession session = request.getSession();
    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
        session);
    interventiEJB.eliminaAllegati(
        logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), idIntervento,
        ids, logOperationOggettoQuadroDTO);
    return "dialog/success";
  }

  @RequestMapping(value = "/elimina_allegati_{idIntervento}", method = RequestMethod.GET)
  public String eliminaAllegati(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    List<Long> ids = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idFileAllegatiIntervento"));
    model.addAttribute("ids", ids);
    return "interventi/confermaEliminaAllegati";
  }

  @RequestMapping(value = "/elimina_allegati_{idIntervento}", method = RequestMethod.POST)
  public String eliminaAllegatiPost(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    List<Long> ids = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idFileAllegatiIntervento"));
    HttpSession session = request.getSession();
    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
        session);
    interventiEJB.eliminaAllegati(
        logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), idIntervento,
        ids, logOperationOggettoQuadroDTO);
    return "interventi/confermaEliminaAllegati";
  }

  @RequestMapping(value = "/allega_file_{idIntervento}", method = RequestMethod.POST)
  public String allegaFilePost(Model model, HttpServletRequest request,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") Long idIntervento,
      @ModelAttribute("errors") Errors errors,
      @RequestParam(value = "nomeAllegato", required = false) String nomeAllegato,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato,
      HttpSession session)
      throws InternalUnexpectedException, IOException
  {
    errors.validateMandatory(nomeAllegato, "nomeAllegato");
    if (fileAllegato == null || fileAllegato.isEmpty())
    {
      errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    if (errors.isEmpty())
    {
      ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
      FileAllegatoInterventoDTO file = new FileAllegatoInterventoDTO();
      file.setNomeFisico(IuffiUtils.FILE
          .getSafeSubmittedFileName(fileAllegato.getOriginalFilename()));
      file.setNomeLogico(nomeAllegato);
      file.setFileAllegato(fileAllegato.getBytes());
      interventiEJB.insertLocalizzazioneMappeFile(po.getIdProcedimentoOggetto(),
          idIntervento, file, getLogOperationOggettoQuadroDTO(session));
      return "interventi/allegaFileOk";
    }
    model.addAttribute("nomeAllegato", nomeAllegato);
    return "interventi/allegaFile";
  }

  @RequestMapping(value = "/elenco_conduzioni_{idIntervento}", method = RequestMethod.GET)
  public String elencoConduzioni(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    int idProcedimentoAgricolo = getUtenteAbilitazioni(request.getSession()).getIdProcedimento();
    model.addAttribute("provincie",
        interventiEJB.getProvincieConTerreniInConduzione(
            getIdProcedimentoOggetto(request.getSession()),
            IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
            idProcedimentoAgricolo));
    model.addAttribute("comuneDisabled", Boolean.TRUE);
    model.addAttribute("sezioneDisabled", Boolean.TRUE);
    return "interventi/elencoConduzioni";
  }

  @RequestMapping(value = "/elenco_particelle_{idIntervento}", method = RequestMethod.GET)
  public String elencoParticelle(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    model.addAttribute("provincie",
        interventiEJB
            .getProvincie(IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));
    model.addAttribute("comuneDisabled", Boolean.TRUE);
    model.addAttribute("sezioneDisabled", Boolean.TRUE);
    return "interventi/elencoParticelle";
  }

  @IsPopup
  @RequestMapping(value = "/elenco_particelle_{idIntervento}", method = RequestMethod.POST)
  public String elencoParticellePost(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    String[] idParticellaCertificata = request
        .getParameterValues("idParticellaCertificata");
    Errors errors = new Errors();
    errors.validateMandatory(idParticellaCertificata, "error",
        "Inserire almeno una particella");
    HttpSession session = request.getSession();
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      model.addAttribute("sezioneDisabled",
          Boolean.valueOf(request.getParameter("sezione") == null));

      model.addAttribute("provincie", interventiEJB
          .getProvincie(IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));
      String istatComune = request.getParameter("istatComune");
      if (!GenericValidator.isBlankOrNull(istatComune))
      {
        model.addAttribute("sezione",
            interventiEJB.getSezioniPerComune(istatComune));
      }
      final String istatProvincia = request.getParameter("istatProvincia");
      if (!GenericValidator.isBlankOrNull(istatProvincia))
      {
        List<DecodificaDTO<String>> comuni = interventiEJB.getDecodificheComuni(
            null, istatProvincia, IuffiConstants.FLAGS.NO);
        model.addAttribute("comuni", comuni);
      }
      else
      {
        model.addAttribute("comuneDisabled", Boolean.TRUE);
      }
    }
    else
    {
      interventiEJB.insertLocalizzazioneParticelle(idProcedimentoOggetto,
          idIntervento,
          IuffiUtils.LIST.toListOfLong(idParticellaCertificata),
          getLogOperationOggettoQuadroDTO(session));
      return "redirect:../cuiuffi"
          + IuffiUtils.APPLICATION.getCUNumber(request) + "l/index.do";
    }
    return "interventi/elencoParticelle";
  }

  @RequestMapping(value = "/elenco_conduzioni_{idIntervento}", method = RequestMethod.POST)
  public String elencoConduzioniPost(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    String[] idChiaveConduzione = request
        .getParameterValues("idChiaveConduzione");
    Errors errors = new Errors();
    errors.validateMandatory(idChiaveConduzione, "error",
        "Inserire almeno una conduzione");
    HttpSession session = request.getSession();
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      model.addAttribute("sezioneDisabled",
          Boolean.valueOf(request.getParameter("sezione") == null));

      int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
      model.addAttribute("provincie",
          interventiEJB.getProvincieConTerreniInConduzione(
              idProcedimentoOggetto,
              IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
              idProcedimentoAgricolo));
      String istatComune = request.getParameter("istatComune");
      if (!GenericValidator.isBlankOrNull(istatComune))
      {
        model.addAttribute("sezione",
            interventiEJB.getSezioniPerComuneConTerreniInConduzione(
                idProcedimentoOggetto, istatComune, idProcedimentoAgricolo));
      }
      final String istatProvincia = request.getParameter("istatProvincia");
      if (!GenericValidator.isBlankOrNull(istatProvincia))
      {
        List<DecodificaDTO<String>> comuni = interventiEJB
            .getComuniPerProvinciaConTerreniInConduzione(idProcedimentoOggetto,
                istatProvincia, idProcedimentoAgricolo);
        model.addAttribute("comuni", comuni);
      }
      else
      {
        model.addAttribute("comuneDisabled", Boolean.TRUE);
      }
    }
    else
    {
      interventiEJB.insertLocalizzazioneConduzioni(idProcedimentoOggetto,
          idIntervento, idChiaveConduzione,
          getLogOperationOggettoQuadroDTO(session),
          getUtenteAbilitazioni(session).getIdProcedimento());
      return "redirect:../cuiuffi"
          + IuffiUtils.APPLICATION.getCUNumber(request) + "l/index.do";
    }
    return "interventi/elencoConduzioni";
  }

  @IsPopup
  @RequestMapping(value = "/popup_ricerca_conduzioni", method = RequestMethod.GET)
  public String popupRicercaConduzioni(Model model, HttpServletRequest request,
		  
      @ModelAttribute("filtroParticelle") ParametriRicercaParticelle filtro)
      throws InternalUnexpectedException
  {
    FiltroRicercaConduzioni filtroRicercaConduzioni = new FiltroRicercaConduzioni();
    Errors errors = new Errors();
    String idDannoFauna = request.getParameter("idDannoFauna");
    model.addAttribute("idDannoFauna",idDannoFauna);
    addParametersToModel(model,request);
    boolean hasComune = errors.validateMandatory(filtro.getIstatComune(),
        "istatComune");
    errors.validateMandatory(filtro.getIstatProvincia(), "istatProvincia");
    errors.validateMandatory(filtro.getSezione(), "sezione");
    model.addAttribute("sezioneDisabled",
        Boolean.valueOf(filtro.getSezione() == null));
    Long foglio = errors.validateMandatoryLongInRange(filtro.getFoglio(),
        "foglio", 1l, 9999l);
    if (foglio != null)
    {
      filtroRicercaConduzioni.setFoglio(foglio);
    }
    filtroRicercaConduzioni.setParticella(errors.validateOptionalLongInRange(
        filtro.getParticella(), "particella", 1l, 99999l));
    errors.validateOptionalFieldLength(filtro.getSubalterno(), "subalterno", 1,
        3);
    int idProcedimentoAgricolo = getUtenteAbilitazioni(request.getSession()).getIdProcedimento();

    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      long idProcedimentoOggetto = getIdProcedimentoOggetto(
          request.getSession());
      model.addAttribute("provincie",
          interventiEJB.getProvincieConTerreniInConduzione(
              idProcedimentoOggetto,
              IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
              idProcedimentoAgricolo));
      if (hasComune)
      {
        model.addAttribute("sezione",
            interventiEJB.getSezioniPerComuneConTerreniInConduzione(
                idProcedimentoOggetto, filtro.getIstatComune(),
                idProcedimentoAgricolo));
      }
      final String istatProvincia = filtro.getIstatProvincia();
      if (!GenericValidator.isBlankOrNull(istatProvincia))
      {
        List<DecodificaDTO<String>> comuni = interventiEJB
            .getComuniPerProvinciaConTerreniInConduzione(idProcedimentoOggetto,
                istatProvincia, idProcedimentoAgricolo);
        model.addAttribute("comuni", comuni);
      }
      else
      {
        model.addAttribute("comuneDisabled", Boolean.TRUE);
      }
      return getFiltriInserimentoConduzione();
    }
    else
    {
      filtroRicercaConduzioni.setIstatComune(filtro.getIstatComune());
      String sezione = filtro.getSezione();
      if ("null".equalsIgnoreCase(sezione))
      {
        sezione = null;
      }
      filtroRicercaConduzioni.setSezione(sezione);
      filtroRicercaConduzioni.setSubalterno(filtro.getSubalterno());
      filtroRicercaConduzioni.setChiaviConduzioniInserite(
          request.getParameterValues("idChiaveConduzione"));
      HttpSession session = request.getSession();
      filtroRicercaConduzioni
          .setIdProcedimentoOggetto(getIdProcedimentoOggetto(session));
      filtroRicercaConduzioni.setChiaviConduzioniInserite(request.getParameterValues("idUtilizzoDichiarato"));
      session.setAttribute(FiltroRicercaConduzioni.class.getName(),
          filtroRicercaConduzioni);
      return getJSPBaseName() + "/popupRicercaConduzioni";
    }
  }
  
  public void addParametersToModel(Model model, HttpServletRequest request) {}
  
  public String getFiltriInserimentoConduzione()
  {
	  return "interventi/include/filtriInserimentoConduzione";
  }

  @IsPopup
  @RequestMapping(value = "/popup_ricerca_particelle", method = RequestMethod.GET)
  public String popupRicercaParticelle(Model model, HttpServletRequest request,
      @ModelAttribute("filtroParticelle") ParametriRicercaParticelle filtro)
      throws InternalUnexpectedException
  {
    FiltroRicercaParticelle filtroRicercaParticelle = new FiltroRicercaParticelle();
    filtroRicercaParticelle.setIdParticellaCertificata(IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idParticellaCertificata")));
    Errors errors = new Errors();
    boolean hasComune = errors.validateMandatory(filtro.getIstatComune(),
        "istatComune");
    errors.validateMandatory(filtro.getIstatProvincia(), "istatProvincia");
    errors.validateMandatory(filtro.getSezione(), "sezione");
    model.addAttribute("sezioneDisabled",
        Boolean.valueOf(filtro.getSezione() == null));
    Long foglio = errors.validateMandatoryLongInRange(filtro.getFoglio(),
        "foglio", 1l, 9999l);
    int idProcedimentoAgricolo = getUtenteAbilitazioni(request.getSession()).getIdProcedimento();
    if (foglio != null)
    {
      filtroRicercaParticelle.setFoglio(foglio);
    }
    filtroRicercaParticelle.setParticella(errors.validateOptionalLongInRange(
        filtro.getParticella(), "particella", 1l, 99999l));
    errors.validateOptionalFieldLength(filtro.getSubalterno(), "subalterno", 1,
        3);
    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      long idProcedimentoOggetto = getIdProcedimentoOggetto(
          request.getSession());
      model.addAttribute("provincie",
          interventiEJB.getProvincieConTerreniInConduzione(
              idProcedimentoOggetto,
              IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
              idProcedimentoAgricolo));
      if (hasComune)
      {
        model.addAttribute("sezione",
            interventiEJB.getSezioniPerComune(filtro.getIstatComune()));
      }
      final String istatProvincia = filtro.getIstatProvincia();
      if (!GenericValidator.isBlankOrNull(istatProvincia))
      {
        List<DecodificaDTO<String>> comuni = interventiEJB.getDecodificheComuni(
            null, istatProvincia, IuffiConstants.FLAGS.NO);
        model.addAttribute("comuni", comuni);
      }
      else
      {
        model.addAttribute("comuneDisabled", Boolean.TRUE);
      }
      return "interventi/include/filtriInserimentoParticelle";
    }
    else
    {
      filtroRicercaParticelle.setIstatComune(filtro.getIstatComune());
      String sezione = filtro.getSezione();
      filtroRicercaParticelle.setSezione(sezione);
      filtroRicercaParticelle.setSubalterno(filtro.getSubalterno());
      HttpSession session = request.getSession();
      filtroRicercaParticelle
          .setIdProcedimentoOggetto(getIdProcedimentoOggetto(session));
      session.setAttribute(FiltroRicercaParticelle.class.getName(),
          filtroRicercaParticelle);
      return "interventi/popupRicercaParticelle";
    }
  }

  @IsPopup
  @RequestMapping(value = "/popup_comuni_particelle", method = RequestMethod.POST)
  public String inserisciComuniParticelle(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    if (request.getParameter("conferma") != null) // Non mi interessa il valore,
                                                  // basta che esista
    {
      long idIntervento = new Long(request.getParameter("idIntervento"));
      String ids[] = request.getParameterValues("istatComune");
      if (ids == null || ids.length == 0)
      {
        model.addAttribute("error", "Selezionare almeno un comune");
        return "dialog/error";
      }
      else
      {
        LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
            session);
        interventiEJB.insertLocalizzazioneComuni(
            logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
            idIntervento, ids, logOperationOggettoQuadroDTO, IuffiConstants.FLAGS.NO, IuffiConstants.FLAGS.NO, IuffiConstants.FLAGS.NO);
        return "dialog/success";
      }
    }
    model.addAttribute("interventi",
        interventiEJB.getElencoInterventiProcedimentoOggetto(
            getIdProcedimentoOggetto(session), getFlagEscludiCatalogo(),
            getDataValiditaProcOggetto(session)));
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_particelle.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_particelle.do");
    return "interventi/popupComuni";
  }

  @IsPopup
  @RequestMapping(value = "/popup_comuni_particelle", method = RequestMethod.GET)
  public String popupComuniParticelle(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_particelle.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_particelle.do");
    return "interventi/popupComuni";
  }

  @IsPopup
  @RequestMapping(value = "/popup_comuni_piemonte", method = RequestMethod.GET)
  public String popupComuniPiemonte(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_piemonte.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_piemonte.do");
    return "interventi/popupComuni";
  }
  
  @IsPopup
  @RequestMapping(value = "/popup_comuni_opere_danneggiate", method = RequestMethod.GET)
  public String popupComuniOpereDanneggiate(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_piemonte.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_opere_danneggiate.do");
    List<String> valoriFlag = interventiEJB.getFlagCanaleOpereCondotta(getIdProcedimentoOggetto(request.getSession()), Long.parseLong(request.getParameter("idIntervento")));
    if(valoriFlag!=null){
    	model.addAttribute("canale", valoriFlag.get(0));
    	model.addAttribute("opere", valoriFlag.get(1));
    	model.addAttribute("condotta", valoriFlag.get(2));
    }else{
    	model.addAttribute("canale", "N");
    	model.addAttribute("opere", "N");
    	model.addAttribute("condotta", "N");
    }
    return "interventi/popupComuniOpereDanneggiate";
  }

  @IsPopup
  @RequestMapping(value = "/popup_comuni_piemonte", method = RequestMethod.POST)
  public String inserisciComuniPiemonte(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    if (request.getParameter("conferma") != null) // Non mi interessa il valore,
                                                  // basta che esista
    {
      long idIntervento = new Long(request.getParameter("idIntervento"));
      String ids[] = request.getParameterValues("istatComune");
      if (ids == null || ids.length == 0)
      {
        model.addAttribute("error", "Selezionare almeno un comune");
        return "dialog/error";
      }
      else
      {
        LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
            session);
        interventiEJB.insertLocalizzazioneComuni(
            logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
            idIntervento, ids, logOperationOggettoQuadroDTO, IuffiConstants.FLAGS.NO, IuffiConstants.FLAGS.NO, IuffiConstants.FLAGS.NO);
        return "dialog/success";
      }
    }
    model.addAttribute("interventi",
        interventiEJB.getElencoInterventiProcedimentoOggetto(
            getIdProcedimentoOggetto(session), getFlagEscludiCatalogo(),
            getDataValiditaProcOggetto(session)));
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_piemonte.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_piemonte.do");
    return "interventi/popupComuni";
  }
  
  
  @IsPopup
  @RequestMapping(value = "/popup_comuni_opere_danneggiate", method = RequestMethod.POST)
  public String inserisciComuniOpere(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    if (request.getParameter("conferma") != null) // Non mi interessa il valore,
                                                  // basta che esista
    {
      long idIntervento = new Long(request.getParameter("idIntervento"));
      String ids[] = request.getParameterValues("istatComune");
      if (ids == null || ids.length == 0)
      {
        model.addAttribute("error", "Selezionare almeno un comune");
        return "dialog/error";
      }
      else
      {
		if(request.getParameter("flagCanale")==null && 
		   request.getParameter("flagOpereDiPresa")==null && 
		   request.getParameter("flagCondotta")==null){
		     	 model.addAttribute("error", "Scegliere almeno una tipologia di Opera Danneggiata");
		         return "dialog/error";
		}
        LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
            session);
        String flagCanale = (request.getParameter("flagCanale"))!=null?IuffiConstants.FLAGS.SI:IuffiConstants.FLAGS.NO;
        String flagOpereDiPresa = (request.getParameter("flagOpereDiPresa"))!=null?IuffiConstants.FLAGS.SI:IuffiConstants.FLAGS.NO;
        String flagCondotta = (request.getParameter("flagCondotta"))!=null?IuffiConstants.FLAGS.SI:IuffiConstants.FLAGS.NO;
        
       
        interventiEJB.insertLocalizzazioneComuni(
            logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
            idIntervento, ids, logOperationOggettoQuadroDTO, flagCanale, flagOpereDiPresa, flagCondotta);
        return "dialog/success";
      }
    }
    model.addAttribute("interventi",
        interventiEJB.getElencoInterventiProcedimentoOggetto(
            getIdProcedimentoOggetto(session), getFlagEscludiCatalogo(),
            getDataValiditaProcOggetto(session)));
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_piemonte.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_piemonte.do");
    return "interventi/popupComuni";
  }
  

  @IsPopup
  @RequestMapping(value = "/popup_comuni_ute", method = RequestMethod.GET)
  public String popupComuniUte(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_ute.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_ute.do");
    return "interventi/popupComuni";
  }

  @IsPopup
  @RequestMapping(value = "/popup_comuni_ute", method = RequestMethod.POST)
  public String inserisciComuniUte(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    if (request.getParameter("conferma") != null) // Non mi interessa il valore,
                                                  // basta che esista
    {
      long idIntervento = new Long(request.getParameter("idIntervento"));
      String ids[] = request.getParameterValues("istatComune");
      if (ids == null || ids.length == 0)
      {
        model.addAttribute("error", "Selezionare almeno un comune");
        return "dialog/error";
      }
      else
      {
        LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
            session);
        interventiEJB.insertLocalizzazioneComuni(
            logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
            idIntervento, ids, logOperationOggettoQuadroDTO, IuffiConstants.FLAGS.NO, IuffiConstants.FLAGS.NO, IuffiConstants.FLAGS.NO);
        return "dialog/success";
      }
    }
    model.addAttribute("interventi",
        interventiEJB.getElencoInterventiProcedimentoOggetto(
            getIdProcedimentoOggetto(session), getFlagEscludiCatalogo(),
            getDataValiditaProcOggetto(session)));
    model.addAttribute("json_source",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/load_comuni_particelle.json?idIntervento="
            + request.getParameter("idIntervento"));
    model.addAttribute("submit_url",
        "../cuiuffi" + IuffiUtils.APPLICATION.getCUNumber(request)
            + "z/popup_comuni_particelle.do");
    return "interventi/popupComuni";
  }

  @RequestMapping(value = "/elenco_conduzioni_con_superficie_{idIntervento}", method = RequestMethod.GET)
  public String elencoConduzioniSuperfici(Model model,
      HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException, JsonGenerationException,
      JsonMappingException, IOException
  {
    int idProcedimentoAgricolo = getUtenteAbilitazioni(request.getSession()).getIdProcedimento();
    model.addAttribute("provincie",
        interventiEJB.getProvincieConTerreniInConduzione(
            getIdProcedimentoOggetto(request.getSession()),
            IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
            idProcedimentoAgricolo));
    model.addAttribute("comuneDisabled", Boolean.TRUE);
    model.addAttribute("sezioneDisabled", Boolean.TRUE);

    List<RigaJSONConduzioneInteventoDTO> conduzioni = interventiEJB
        .getElencoConduzioniJSON(getIdProcedimentoOggetto(request.getSession()),
            idIntervento);
    if (conduzioni != null)
    {
      StringWriter sw = new StringWriter();
      jacksonWriter.writeValue(sw, conduzioni);
      model.addAttribute("json", sw.toString());
    }

    return "interventi/elencoConduzioniSuperficie";
  }

  @RequestMapping(value = "/elenco_conduzioni_con_superficie_{idIntervento}", method = RequestMethod.POST)
  public String elencoConduzioniSuperficiPost(Model model,
      HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException, JsonGenerationException,
      JsonMappingException, IOException
  {
    String[] idChiaveConduzione = request
        .getParameterValues("idChiaveConduzione");
    Errors errors = new Errors();
    HttpSession session = request.getSession();
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaJSONConduzioneInteventoDTO> conduzioni = null;
    if (idChiaveConduzione != null && idChiaveConduzione.length > 0)
    {
      conduzioni = interventiEJB.getElencoConduzioniJSON(idProcedimentoOggetto,
          idChiaveConduzione,
          getUtenteAbilitazioni(session).getIdProcedimento());
      for (RigaJSONConduzioneInteventoDTO conduzione : conduzioni)
      {
        String key = conduzione.getId();
        final String fieldName = "superficieImpegno_" + key;
        String superficieImpegno = request
            .getParameter("superficieImpegno_" + key);
        BigDecimal superficieImpegnoBD = errors
            .validateMandatoryBigDecimalInRange(superficieImpegno, fieldName, 4,
                IuffiConstants.MIN.SUPERFICIE_IMPEGNO_INTERVENTO,
                IuffiConstants.MAX.SUPERFICIE_IMPEGNO_INTERVENTO);

        if (superficieImpegnoBD != null)
        {
          BigDecimal supUtilizzataBD = IuffiUtils.NUMBERS
              .getBigDecimal(conduzione.getSuperficieUtilizzata())
              .setScale(4, RoundingMode.HALF_DOWN);
          if (superficieImpegnoBD.compareTo(supUtilizzataBD) > 0)
          {
            errors.addError(fieldName,
                "La superficie oggetto dell'intervento non può superare la \"superficie utilizzata\"");
            conduzione.setError(errors.getLastError());
          }
        }
        else
        {
          conduzione.setError(errors.getLastError());
        }
        conduzione.setSuperficieImpegno(request.getParameter(fieldName));
      }
    }
    else
    {
      errors.validateMandatory(idChiaveConduzione, "error",
          "Inserire almeno una destinazione produttiva");
    }
    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      model.addAttribute("sezioneDisabled",
          Boolean.valueOf(request.getParameter("sezione") == null));
      int idProcedimentoAgricolo = getUtenteAbilitazioni(request.getSession()).getIdProcedimento();
      model.addAttribute("provincie",
          interventiEJB.getProvincieConTerreniInConduzione(
              idProcedimentoOggetto,
              IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
              idProcedimentoAgricolo));
      String istatComune = request.getParameter("istatComune");
      if (!GenericValidator.isBlankOrNull(istatComune))
      {
        model.addAttribute("sezione",
            interventiEJB.getSezioniPerComuneConTerreniInConduzione(
                idProcedimentoOggetto, istatComune, idProcedimentoAgricolo));
      }
      final String istatProvincia = request.getParameter("istatProvincia");
      if (!GenericValidator.isBlankOrNull(istatProvincia))
      {
        List<DecodificaDTO<String>> comuni = interventiEJB
            .getComuniPerProvinciaConTerreniInConduzione(idProcedimentoOggetto,
                istatProvincia, idProcedimentoAgricolo);
        model.addAttribute("comuni", comuni);
      }
      else
      {
        model.addAttribute("comuneDisabled", Boolean.TRUE);
      }
      if (conduzioni != null)
      {
        StringWriter sw = new StringWriter();
        jacksonWriter.writeValue(sw, conduzioni);
        model.addAttribute("json", sw.toString());
      }
    }
    else
    {
      List<SuperficieConduzione> superfici = new ArrayList<SuperficieConduzione>();
      if (conduzioni != null)
      {
        for (RigaJSONConduzioneInteventoDTO conduzione : conduzioni)
        {
          superfici.add(conduzione.toSuperficieConduzione());
        }
      }
      interventiEJB.insertLocalizzazioneConduzioni(idProcedimentoOggetto,
          idIntervento, superfici, getLogOperationOggettoQuadroDTO(session),
          getUtenteAbilitazioni(session).getIdProcedimento());
      return "redirect:../cuiuffi"
          + IuffiUtils.APPLICATION.getCUNumber(request) + "l/index.do";
    }
    return "interventi/elencoConduzioniSuperficie";
  }

  @IsPopup
  @RequestMapping(value = "/popup_ricerca_conduzioni_con_superficie", method = RequestMethod.GET)
  public String popupRicercaConduzioniSuperficie(Model model,
      HttpServletRequest request,
      @ModelAttribute("filtroParticelle") ParametriRicercaParticelle filtro)
      throws InternalUnexpectedException
  {
    FiltroRicercaConduzioni filtroRicercaConduzioni = new FiltroRicercaConduzioni();
    Errors errors = new Errors();
    boolean hasComune = errors.validateMandatory(filtro.getIstatComune(),
        "istatComune");
    errors.validateMandatory(filtro.getIstatProvincia(), "istatProvincia");
    errors.validateMandatory(filtro.getSezione(), "sezione");
    model.addAttribute("sezioneDisabled",
        Boolean.valueOf(filtro.getSezione() == null));
    Long foglio = errors.validateMandatoryLongInRange(filtro.getFoglio(),
        "foglio", 1l, 9999l);
    if (foglio != null)
    {
      filtroRicercaConduzioni.setFoglio(foglio);
    }
    filtroRicercaConduzioni.setParticella(errors.validateOptionalLongInRange(
        filtro.getParticella(), "particella", 1l, 99999l));
    errors.validateOptionalFieldLength(filtro.getSubalterno(), "subalterno", 1,
        3);
    if (errors.addToModelIfNotEmpty(model))
    {
      int idProcedimentoAgricolo = getUtenteAbilitazioni(request.getSession()).getIdProcedimento();
      model.addAttribute("preferRequest", Boolean.TRUE);
      long idProcedimentoOggetto = getIdProcedimentoOggetto(
          request.getSession());
      model.addAttribute("provincie",
          interventiEJB.getProvincieConTerreniInConduzione(
              idProcedimentoOggetto,
              IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
              idProcedimentoAgricolo));
      if (hasComune)
      {
        model.addAttribute("sezione",
            interventiEJB.getSezioniPerComuneConTerreniInConduzione(
                idProcedimentoOggetto, filtro.getIstatComune(),
                idProcedimentoAgricolo));
      }
      final String istatProvincia = filtro.getIstatProvincia();
      if (!GenericValidator.isBlankOrNull(istatProvincia))
      {
        List<DecodificaDTO<String>> comuni = interventiEJB
            .getComuniPerProvinciaConTerreniInConduzione(idProcedimentoOggetto,
                istatProvincia, idProcedimentoAgricolo);
        model.addAttribute("comuni", comuni);
      }
      else
      {
        model.addAttribute("comuneDisabled", Boolean.TRUE);
      }
      return getFiltriInserimentoConduzione();
    }
    else
    {
      filtroRicercaConduzioni.setIstatComune(filtro.getIstatComune());
      String sezione = filtro.getSezione();
      if ("null".equals(sezione))
      {
        sezione = null;
      }
      filtroRicercaConduzioni.setSezione(sezione);
      filtroRicercaConduzioni.setSubalterno(filtro.getSubalterno());
      filtroRicercaConduzioni.setChiaviConduzioniInserite(
          request.getParameterValues("idChiaveConduzione"));
      HttpSession session = request.getSession();
      filtroRicercaConduzioni
          .setIdProcedimentoOggetto(getIdProcedimentoOggetto(session));
      session.setAttribute(FiltroRicercaConduzioni.class.getName(),
          filtroRicercaConduzioni);
      model.addAttribute("superficie", "_con_superficie");
      return getJSPBaseName() + "/popupRicercaConduzioni";
    }
  }
  
  public String getJSPBaseName()
  {
	  return "interventi";
  }
}
