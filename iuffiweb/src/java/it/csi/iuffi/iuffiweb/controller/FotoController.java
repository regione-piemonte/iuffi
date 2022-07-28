package it.csi.iuffi.iuffiweb.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.FotoApiDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
@Controller
@IuffiSecurity(value = "FOTO", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class FotoController extends TabelleController
{
  
  @Autowired
  private IGpsFotoEJB gpsFotoEJB;
  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;
  
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;
  
  @Autowired
  private ITipoAreaEJB tipoAreaEJB;
  
  @Autowired
  private ITipoTrappolaEJB tipoTrappolaEJB;
  
  @Autowired
  private ITipoCampioneEJB tipocampioneEJB;
  
  @Autowired
  private IQuadroEJB quadroEJB;
 
  @Autowired
  private IRicercaEJB ricercaEJB;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }


  @RequestMapping(value = "/foto/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    FotoDTO foto = new FotoDTO();
    if (session.getAttribute("foto") != null) {
      foto = (FotoDTO) session.getAttribute("foto");
    }
    if (foto.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      foto.setAnno(currentYear);
    }
    model.addAttribute("foto", foto);
    loadPopupCombo(model, session);
    setBreadcrumbs(model, request);
    session.setAttribute("currentPage", 1);

    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tableFoto");
    cleanTableMapsInSession(session, tableNamesToRemove);
    
    return "gestioneGpsFoto/ricercaFoto";
  }

  @RequestMapping(value = "/foto/mappa")
  public String mappa(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      setBreadcrumbs(model, request);
      return "gestioneGpsFoto/gps";
  }

  @RequestMapping(value = "/foto/search")
  public String search(Model model, @ModelAttribute("foto") FotoDTO foto, HttpSession session, HttpServletResponse response,HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {       
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);

    try
    {
      if (foto.checkNull())
        foto = (FotoDTO) session.getAttribute("foto");
      if (foto == null)
        foto = new FotoDTO();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    //CARICO LE LISTE
    List<Integer> listaTipoArea = foto.getTipoArea();
    List<Integer> listaSpecie = foto.getSpecieVegetale();
    List<Integer> listaOrganismo = foto.getOrganismoNocivo();
    List<Integer> listaIspetAssegnato = foto.getIspettoreAssegnato();
    List<Integer> listaTrappole = foto.getTrappole();
    List<Integer> listaCampioni = foto.getCampioni();
    if(listaTipoArea!=null) {
      String idArea="";
      for(Integer i : listaTipoArea) {
        idArea+=i+",";
      }
      idArea=idArea.substring(0,idArea.length()-1);
      List<TipoAreaDTO> listaAree=tipoAreaEJB.findByIdMultipli(idArea);
      model.addAttribute("listaAree",listaAree);   
    } 
    //specie
    if(listaSpecie!=null) {
      String idSpecie="";
      for(Integer i : listaSpecie) {
        idSpecie+=i+",";
      }
      idSpecie=idSpecie.substring(0,idSpecie.length()-1);
      List<SpecieVegetaleDTO> listaSpecieVeg=specieVegetaleEJB.findByIdMultipli(idSpecie);
      model.addAttribute("listaSpecie",listaSpecieVeg);     

    } 
    //organismi
    if(listaOrganismo!=null) {
      String idOn="";
      for(Integer i : listaOrganismo) {
        idOn+=i+",";
      }
      idOn=idOn.substring(0,idOn.length()-1);
      List<OrganismoNocivoDTO> listaOn=organismoNocivoEJB.findByIdMultipli(idOn);
      model.addAttribute("listaOn",listaOn);          
    }

    //ispettori assegnati
    if(listaIspetAssegnato!=null) {
      String idIsAss="";
      for(Integer i : listaIspetAssegnato) {
        idIsAss+=i+",";
      }
      idIsAss=idIsAss.substring(0,idIsAss.length()-1);
      List<AnagraficaDTO> listaIspettAssegnati=anagraficaEJB.findByIdMultipli(idIsAss);
      model.addAttribute("listaIspettAssegnati",listaIspettAssegnati);          
    }
    //trappole
    if(listaTrappole!=null) {
      String idTrappole="";
      for(Integer i : listaTrappole) {
        idTrappole+=i+",";
      }
      idTrappole=idTrappole.substring(0,idTrappole.length()-1);
      List<TipoTrappolaDTO> listTrappole=tipoTrappolaEJB.findByIdMultipli(idTrappole);
      model.addAttribute("listaTrappole",listTrappole);
    }
    //campioni
    if(listaCampioni!=null) {
      String idCampioni="";
      for(Integer i : listaCampioni) {
        idCampioni+=i+",";
      }
      idCampioni=idCampioni.substring(0,idCampioni.length()-1);
      List<TipoCampioneDTO> listCampioni=tipocampioneEJB.findByIdMultipli(idCampioni);
      model.addAttribute("listaCampioni",listCampioni);          
    }

    //recupero il comune
    ComuneDTO comuneDTO = quadroEJB.getComune(foto.getIstatComune()!=null ? foto.getIstatComune() : "notfou");
    foto.setComune(comuneDTO!=null ? comuneDTO.getDescrizioneComune() : " ");

    setBreadcrumbs(model, request);
    session.setAttribute("foto", foto);
    return "gestioneGpsFoto/listaFoto";
  }
 
 
  @RequestMapping(value = "/foto/edit")
  public String showFoto(Model model, @RequestParam(value = "id") Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    FotoDTO foto = gpsFotoEJB.findFotoById(id);
    
    // comune
    String comune = null;
    if(foto != null) {
      if (StringUtils.isNotBlank(foto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(foto.getIstatComune());
        if (comuneDTO != null)
        {
          comune = comuneDTO.getDescrizioneComune();
          foto.setDescrComune(comune);
         // model.addAttribute("comune", comune);
        } else   {
          foto.setDescrComune("Non disponibile");
        }
      }
    }

    
    model.addAttribute("foto", foto);
    
    setBreadcrumbs(model, request);
    return "gestioneGpsFoto/dettaglioFoto";
  }

  //aggiunto da Barbara
  @RequestMapping(value = "/foto/getFotoJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getFotoJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    FotoDTO foto = (FotoDTO) session.getAttribute("foto");
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    List<FotoDTO> lista = gpsFotoEJB.findFotoByFilter(foto, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte(), null);
    
    if (lista == null) {
      lista = new ArrayList<>();
    }
    //session.removeAttribute("missioneRequest");
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }

  @RequestMapping(value = "/foto/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    session.removeAttribute("foto");
    return "redirect:showFilter.do";
  }
  
  @Lazy
  @RequestMapping(value = "/foto/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableFoto") == null || "{}".equals(filtroInSessione.get("tableFoto"))) {
        filtroInit = "";//"{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableFoto");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableFoto", filtroInit);
      return filtroInSessione;
  }

  /**
   * carico popup
   * @param model
   * @param session
   * @throws InternalUnexpectedException
   */
  private void loadPopupCombo(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    //UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    //int idProcedimentoAgricolo = utenteAbilitazioni.getIdProcedimento();

    FotoDTO foto = (FotoDTO) session.getAttribute("foto");
 // Tipo area
    List<TipoAreaDTO> all_tipoArea = null;
    if (session.getAttribute("checkboxAllTipoAree") != null && ((String) session.getAttribute("checkboxAllTipoAree")).equals("true")) {
      all_tipoArea = tipoAreaEJB.findAll();
    } else {
      all_tipoArea = tipoAreaEJB.findValidi();
    }
    model.addAttribute("all_tipoArea", all_tipoArea);
    // Ispettore assegnato
    List<AnagraficaDTO> all_ispettoreAssegnato = null;
    if (session.getAttribute("checkboxAllIspettoriAssegnati") != null && ((String) session.getAttribute("checkboxAllIspettoriAssegnati")).equals("true")) {
      all_ispettoreAssegnato = anagraficaEJB.findAll();
    } else {
      all_ispettoreAssegnato = anagraficaEJB.findValidi();
    }
    model.addAttribute("all_ispettoreAssegnato", all_ispettoreAssegnato);
    // Specie vegetale
    List<SpecieVegetaleDTO> all_specieVegetale = null;
    if (session.getAttribute("checkboxAllSpecieVegetali") != null && ((String) session.getAttribute("checkboxAllSpecieVegetali")).equals("true")) {
      all_specieVegetale = specieVegetaleEJB.findAll();
    } else {
      all_specieVegetale = specieVegetaleEJB.findValidi();
    }
    model.addAttribute("all_specieVegetale", all_specieVegetale);
    // Organismo nocivo
    List<OrganismoNocivoDTO> all_organismoNocivo = null;
    if (session.getAttribute("checkboxAllOrganismiNocivi") != null && ((String) session.getAttribute("checkboxAllOrganismiNocivi")).equals("true")) {
      all_organismoNocivo = organismoNocivoEJB.findAll();
    } else {
      all_organismoNocivo = organismoNocivoEJB.findValidi();
    }
    model.addAttribute("all_organismoNocivo", all_organismoNocivo);
    // Tipo campione
    List<TipoCampioneDTO> all_tipoCampione = null;
    if (session.getAttribute("checkboxAllTipoCampioni") != null && ((String) session.getAttribute("checkboxAllTipoCampioni")).equals("true")) {
      all_tipoCampione = tipocampioneEJB.findAll();
    } else {
      all_tipoCampione = tipocampioneEJB.findValidi();
    }
    model.addAttribute("all_campioni", all_tipoCampione);
    //
    // Tipo trappola
    List<TipoTrappolaDTO> all_tipoTrappola = null;
    if (session.getAttribute("checkboxAllTipoTrappole") != null && ((String) session.getAttribute("checkboxAllTipoTrappole")).equals("true")) {
      all_tipoTrappola = tipoTrappolaEJB.findAll();
    } else {
      all_tipoTrappola = tipoTrappolaEJB.findValidi();
    }
    model.addAttribute("all_trappole", all_tipoTrappola);
    //

    //List<TipoAreaDTO> tipoAree = new ArrayList<>();
    //model.addAttribute("tipoAree", tipoAree);
    
    if (foto != null) {
      // Tipo aree
      if (foto.getTipoArea() != null) {
        String idArea="";
        for(Integer i : foto.getTipoArea()) {
          idArea+=i+",";
        }
        idArea=idArea.substring(0,idArea.length()-1);
        List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
        model.addAttribute("tipoAree", listaAree);   
      }
      // Specie vegetali
      if (foto.getSpecieVegetale() != null) {
        String idSpecie="";
        for (Integer i : foto.getSpecieVegetale()) {
          idSpecie+=i+",";
        }
        idSpecie=idSpecie.substring(0,idSpecie.length()-1);
        List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecieVegetali", listaSpecieVeg);     
      }
      // Organismi nocivi
      if (foto.getOrganismoNocivo() != null) {
          String idOn="";
          for (Integer i : foto.getOrganismoNocivo()) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
          model.addAttribute("listaON", listaOn);
       }
      // ispettori assegnati
      if (foto.getIspettoreAssegnato() != null && foto.getIspettoreAssegnato().size() > 0)
      {
          String idIsSec="";
          for(Integer i : foto.getIspettoreAssegnato()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettori",listaIspettAssegnati);          
      }
      // tipo trappole
      if (foto.getTrappole() != null) {
        String idTipoTrappola="";
        for (Integer i : foto.getTrappole()) {
          idTipoTrappola+=i+",";
        }
        idTipoTrappola=idTipoTrappola.substring(0,idTipoTrappola.length()-1);
        List<TipoTrappolaDTO> listaTipoTrapp = tipoTrappolaEJB.findByIdMultipli(idTipoTrappola);
        model.addAttribute("listaTipoTrappole", listaTipoTrapp);
      }

      // tipo campione
      if (foto.getCampioni() != null) {
        String idTipoCampione="";
        for (Integer i : foto.getCampioni()) {
          idTipoCampione+=i+",";
        }
        idTipoCampione=idTipoCampione.substring(0,idTipoCampione.length()-1);
        List<TipoCampioneDTO> listaTipoCamp = tipocampioneEJB.findByIdMultipli(idTipoCampione);
        model.addAttribute("listaTipoCampioni", listaTipoCamp);     
      }

      // comune
      if (StringUtils.isNotBlank(foto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(foto.getIstatComune());
        if (comuneDTO != null)
        {
          model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
          model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
        }
      }
    }   
  }
  
  @RequestMapping(value = "/foto/searchComuni", produces = "application/json", method = RequestMethod.POST)
  public Map<String, Object> searchComuni(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HashMap<String, Object> values = new HashMap<String, Object>();
    String prov = request.getParameter("provSceltaComune");
    String comune = request.getParameter("descComune");
    List<ComuneDTO> comuni = ricercaEJB.searchComuni(prov, comune);
    if (comuni != null && comuni.size() == 1)
    {
      values.put("oneResult", "true");
      values.put("prov", comuni.get(0).getSiglaProvincia());
      values.put("comune", comuni.get(0).getDescrizioneComune());
    }
    else
    {
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

  //----------------------------SEZIONE API FOTO
  /**
   * 
   * @param body
   * @param file
   * @param request
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/rest/immagine", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = { "application/json" })
  @ResponseBody
  public ResponseEntity<?> saveFoto(@RequestPart("infoFoto") String body, @RequestParam("image") MultipartFile file,HttpServletRequest request) 
      throws IOException  {
    
    //System.out.println(body); 
    logger.debug("body:::::::"+body);
    byte[] mediaBytes = readBytes(file.getInputStream());
    logger.debug("input stream size: " +file.getInputStream().available());
    logger.debug("content type: " +file.getContentType());
    logger.debug("file.isEmpty():::::::" + file.isEmpty());    
    logger.debug("file.getSize():::::::" + file.getSize());    
    logger.debug("mediaBytes.length:::::::" + mediaBytes.length);
    //logger.debug("mediaBytes:::::::" + mediaBytes);
    FotoApiDTO fotoApi= new ObjectMapper().readValue(body, FotoApiDTO.class);
    fotoApi.setBase64(mediaBytes.toString());
    
    try {
      //String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      //String cf = verifyToken(jwt);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(fotoApi);
      logger.debug(json);
      
      FotoDTO foto = new FotoDTO();
      foto.setFoto(mediaBytes);
      foto.setFotoByte(mediaBytes);     // Prova S.D.
      foto.setNomeFile(fotoApi.getNomeFile());
      foto.setLongitudine(fotoApi.getLongitudine());
      foto.setLatitudine(fotoApi.getLatitudine());
      foto.setNote(fotoApi.getNote());
      logger.debug("getNomeFile:::::::"+foto.getNomeFile());
      logger.debug("getLongitudine:::::::"+foto.getLongitudine());
      logger.debug("getLatitudine:::::::"+foto.getLatitudine());
      foto.setExtIdUtenteAggiornamento(0L); 
      Long id = gpsFotoEJB.insertFoto(foto);
      System.out.println(id);
      logger.debug("id:::::::"+id);
      if(fotoApi.getIdCampionamento()!=null && fotoApi.getIdCampionamento()>0) {
        gpsFotoEJB.insertFotoCampione(id, fotoApi.getIdCampionamento().longValue());
        logger.debug("insertFotoCampione:::::::");
      }else if(fotoApi.getIdVisual()!=null && fotoApi.getIdVisual()>0) {
        gpsFotoEJB.insertFotoVisual(id, fotoApi.getIdVisual().longValue());
        logger.debug("insertFotoVisual:::::::");
      }else if(fotoApi.getIdTrappolaggio()!=null && fotoApi.getIdTrappolaggio()>0) {
        gpsFotoEJB.insertFotoTrappola(id, fotoApi.getIdTrappolaggio().longValue());
        logger.debug("insertFotoTrappola:::::::");
      }
   // final IImageMetadata metadata = Imaging.getMetadata(file);
   // ImageInputStream iis = ImageIO.createImageInputStream(file);
   /* ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(mediaBytes));
    Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
    
    if (readers.hasNext()) {

     // pick the first available ImageReader
     ImageReader reader = readers.next();
  
     // attach source to the reader
     reader.setInput(iis, true);
  
     // read metadata of first image
     IIOMetadata metadata = reader.getImageMetadata(0);
  
     String[] names = metadata.getMetadataFormatNames();
     int length = names.length;
     for (int i = 0; i < length; i++) {
         System.out.println( "Format name: " + names[ i ] );
         Node node = metadata.getAsTree(names[i]);
         NodeList nodeList = node.getChildNodes();
         for (int x = 0; x < nodeList.getLength(); x++)
         {
           System.out.println(nodeList.item(x).getNodeName());
           NodeList markerSequenceChildren = nodeList.item(x).getChildNodes();
           
           for (int j = 0; j < markerSequenceChildren.getLength(); j++)
           {
             IIOMetadataNode metadataNode = (IIOMetadataNode)(markerSequenceChildren.item(j));
             System.out.println(markerSequenceChildren.item(j).getNodeName());
             byte[] bytes = (byte[])metadataNode.getUserObject();
             if (bytes == null)
             {
               continue;
             }
             
             byte[] magicNumber = new byte[4];
             ByteBuffer.wrap(bytes).get(magicNumber);             
           }
         } 
     }     
   }*/
    }
    catch (Throwable e) {
      logger.debug("Errore nel metodo saveFoto durante la registrazione: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo saveFoto durante la registrazione: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }
  
  
  @RequestMapping(value = "/rest/immagine/{idFoto}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<?> deleteFoto(@PathVariable (value = "idFoto") Long idFoto, HttpServletRequest request)
  {
    try {
      //String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      //verifyToken(jwt);
      logger.debug("id_foto: " + idFoto);
      
      FotoApiDTO fotoToDelete = gpsFotoEJB.selectFotoToRemoveById(idFoto.intValue());
      if(fotoToDelete!=null) {
         if(fotoToDelete.getIdCampionamento()>0) {
            gpsFotoEJB.removeFotoCampionamento(idFoto.intValue(), fotoToDelete.getIdCampionamento());
         }else if(fotoToDelete.getIdTrappolaggio()>0) {
           gpsFotoEJB.removeFotoTrappolaggio(idFoto.intValue(), fotoToDelete.getIdTrappolaggio());
         }else {
           gpsFotoEJB.removeFotoIspezione(idFoto.intValue(), fotoToDelete.getIdVisual());
         }
         
         gpsFotoEJB.removeFoto(idFoto.intValue());
      }
     
      
    } catch (Exception e) {
        logger.debug("Errore nel metodo deleteFoto: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella cancellazione della foto: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }

  
  
  @RequestMapping(value = "/rest/immagini", method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
  @ResponseBody
  public ResponseEntity<?> getElencoFoto(@Valid @RequestBody FotoApiDTO fotoInput, HttpServletRequest request)
  {
    //String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
    List<FotoDTO> foto = null;
    logger.debug("getElencoFoto");
    try
    {
      //String cf = verifyToken(jwt);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(fotoInput);
      logger.debug(json);
      if(fotoInput!=null) {
          foto = gpsFotoEJB.selectFotoByIdAzione(fotoInput);
      }   
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      logger.debug("Errore nel metodo getElencoFoto: " + e.getMessage());
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", e.getMessage());
      er.setMessage("Errore interno nella ricerca delle foto: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<FotoDTO>>(foto, HttpStatus.OK);
  }

  
   public static byte[] readBytes(InputStream is) throws IOException {      
     //InputStream is = new ByteArrayInputStream(data); // not really unknown
     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
     int nRead;
     byte[] dataNew = new byte[1024];
     while ((nRead = is.read(dataNew, 0, dataNew.length)) != -1) {
         buffer.write(dataNew, 0, nRead);
     }
     buffer.flush();
     byte[] byteArray = buffer.toByteArray();
     return byteArray;
 }

}
