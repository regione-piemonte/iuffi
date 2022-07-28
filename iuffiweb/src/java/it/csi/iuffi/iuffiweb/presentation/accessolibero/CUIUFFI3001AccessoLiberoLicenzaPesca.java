package it.csi.iuffi.iuffiweb.presentation.accessolibero;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.SoggettoDTO;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.ImportoLicenzaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.WsUtils;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.smranags.gaaserv.dto.anagraficatributaria.AnagraficaTributariaVO;
import weblogic.management.ApplicationException;

@Controller
@NoLoginRequired
@RequestMapping("/cuiuffi3001")
public class CUIUFFI3001AccessoLiberoLicenzaPesca
    extends BaseController
{
  @Autowired
  IQuadroIuffiEJB              quadroIuffiEJB                            = null;
  @Autowired
  IInterventiEJB interventiEJB;
  private String this_class = "[CUIUFFI3001::";
  private String common_name = "DATI_ANAG_PESCA";
  private String common_errori_validazione = "errori_validazione";
  private String common_errori_nome = "common_errori_nome";
  private String common_errori_cognome = "common_errori_cognome";
  private String common_errori_mail = "common_errori_mail";
  private String common_errori_provincia = "common_errori_provincia";
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String step01(Model model, HttpSession session) throws InternalUnexpectedException
  {
//    List<DecodificaDTO<String>> listaCittadinanza = new ArrayList<DecodificaDTO<String>>();
//    listaCittadinanza.add(new DecodificaDTO<String>(IuffiConstants.ACCESSO_LIBERO.LICENZA_PESCA.CITTADINO_ITALIANO, "Italiana"));
//    listaCittadinanza.add(new DecodificaDTO<String>(IuffiConstants.ACCESSO_LIBERO.LICENZA_PESCA.CITTADINO_STRANIERO, "Estero"));
//    model.addAttribute("listaCittadinanza", listaCittadinanza);
    
    return "accessolibero/pescaStep1";
  }
  @RequestMapping(value = "/confermacittadinanza_{cittadinanza}", method = RequestMethod.GET)
  public String step01Post(Model model, HttpSession session,@PathVariable(value = "cittadinanza") String cittadinanza) throws InternalUnexpectedException, ApplicationException, JsonGenerationException, JsonMappingException, IOException
  {
    String this_method = this_class+"step01Post]";
    logger.debug(this_method+" BEGIN ");
    model.addAttribute("preferRequestValues", Boolean.TRUE);
    Errors errors = new Errors();
    if(!errors.validateMandatory(cittadinanza, "cittadinanza"))  {
      model.addAttribute("errors", errors);
      logger.error(this_method+" error ");
      return step01(model, session);
    }
    
    //Se cittadino straniero prendo un cf fittizio e passo allo step03
    if(IuffiConstants.ACCESSO_LIBERO.LICENZA_PESCA.CITTADINO_STRANIERO.equals(cittadinanza)) {
      logger.debug(this_method+" cittadino straniero ");
      model.addAttribute("preferRequestValues", Boolean.FALSE);
      String codiceFiscale = quadroIuffiEJB.getNextCodiceFiscaleEstero(); 
      logger.debug(this_method+" CF : "+codiceFiscale);
      logger.debug(this_method+" END ");
      return step03_datianagrafici(model, session, codiceFiscale, false);
    }
    
    //Se cittadino italiano vado a step02 di richiesta cf
    logger.debug(this_method+" cittadino italiano ");
    logger.debug(this_method+" END ");
    return step02_codiceFiscale(model, session);
  }
  
  @RequestMapping(value = "/codicefiscale", method = RequestMethod.GET)
  public String step02_codiceFiscale(Model model, HttpSession sessio) throws InternalUnexpectedException
  {
    
    return "accessolibero/pescaStep2";
  }
  @RequestMapping(value = "/codicefiscale", method = RequestMethod.POST)
  public String step02_codiceFiscalePost(Model model, HttpSession session,@RequestParam(value="codicefiscale", required = false) String codicefiscale) throws InternalUnexpectedException, ApplicationException, JsonGenerationException, JsonMappingException, IOException
  {
    String this_method = this_class+"step02_codiceFiscalePost]";
    logger.debug(this_method+" BEGIN ");
    model.addAttribute("preferRequestValues", Boolean.TRUE);
    Errors errors = new Errors();
    if(errors.validateMandatory(codicefiscale, "codicefiscale")) {
      if(!IuffiUtils.VALIDATION.controlloCf(codicefiscale)) {
        logger.error(this_method+" error Verificare il formato del codice fiscale inserito!");
        errors.addError("codicefiscale", "Verificare il formato del codice fiscale inserito!");
      }
    }else
      logger.debug(this_method+" CF : "+codicefiscale);
    if(!errors.isEmpty()){
      model.addAttribute("errors", errors);
      return step02_codiceFiscale(model, session);
    }
    logger.debug(this_method+" END ");
    model.addAttribute("preferRequestValues", Boolean.FALSE);
    return step03_datianagrafici(model, session, codicefiscale, true);
  }
  
  @RequestMapping(value = "/datianagrafici", method = RequestMethod.GET)
  public String step03_datianagrafici(Model model, HttpSession session, String codiceFiscale, boolean isItaliano) throws InternalUnexpectedException, ApplicationException, JsonGenerationException, JsonMappingException, IOException
  {
    String this_method = this_class+"step03_datianagrafici_GET]";
    logger.debug(this_method+" BEGIN ");
    
   
    
    SoggettoDTO soggettoDTO = quadroIuffiEJB.getSoggettoAnagrafePesca(codiceFiscale.toUpperCase());
    try {
      if(soggettoDTO==null){
        logger.debug(this_method+" Soggetto null ");
        soggettoDTO = new SoggettoDTO();
        soggettoDTO.setCodiceFiscale(codiceFiscale.toUpperCase());
        soggettoDTO.setFlagCittadinoEstero(isItaliano?"N":"S");
        String[] elencoCuaa = new String[1];
        elencoCuaa[0] = codiceFiscale.toUpperCase().trim();
        AnagraficaTributariaVO[] tributariaVOArray = IuffiUtils.PORTADELEGATA
            .getGaaServiceHlCSIInterface()
            .gaaservGetAnagraficaTributariaByCUAARange(elencoCuaa,
                null,//IuffiUtils.PORTADELEGATA.getSianUtenteVO(utenteAbilitazioni),
                AnagraficaTributariaVO.FLAG_DATI_AZIENDA_TRIBUTARIA);
        
        if (tributariaVOArray != null && tributariaVOArray[0] != null
            && tributariaVOArray[0].getAziendaTributariaVO() != null) {
          logger.debug(this_method+" tributaria not null ");
          soggettoDTO.setCognome(tributariaVOArray[0].getAziendaTributariaVO().getCognome());
          soggettoDTO.setNome(tributariaVOArray[0].getAziendaTributariaVO().getNome());
          soggettoDTO.setSiglaProvinciaResidenza(tributariaVOArray[0].getAziendaTributariaVO().getProvincia());
        }
      }
    }catch (Exception e){
      logger.error(this_method+" Errore durante la ricerca del cittadino con codice fiscale: "+ codiceFiscale);
      throw new ApplicationException("Errore durante la ricerca del cittadino con codice fiscale: "+ codiceFiscale);
    }
    
    //scremo le tariffe e le aggrego
    List<ImportoLicenzaDTO> listaTariffe = quadroIuffiEJB.getImportiLicenzePesca();
    List<ImportoLicenzaDTO> listaAllTariffe = quadroIuffiEJB.getAllImportiLicenzePesca();
    List<ImportoLicenzaDTO> listaTariffeDef = new ArrayList<ImportoLicenzaDTO>();
    
    for(ImportoLicenzaDTO item:listaTariffe) {
      if(!isItaliano) {
        if(item.getIdTipoLicenza()==3) 
          listaTariffeDef.add(item);
      }else 
        if(item.getIdTipoLicenza()!=3) 
          listaTariffeDef.add(item);
    }
    
    ObjectMapper mapper = new ObjectMapper();
    //Converting the Object to JSONString
    String jsonString = mapper.writeValueAsString(listaAllTariffe);
    
    
    Map<String, Object> common = getCommonFromSession(common_name, session, true);
    Errors errors = (Errors) common.get(common_errori_validazione);
    if(errors!=null)
    {
      soggettoDTO.setCognome((String) common.get(common_errori_cognome));
      soggettoDTO.setNome((String) common.get(common_errori_nome));
      soggettoDTO.setEmail((String) common.get(common_errori_mail));
      soggettoDTO.setSiglaProvinciaResidenza((String) common.get(common_errori_provincia));
      model.addAttribute("errors", errors);
      clearCommonInSession(session);
    }
    
    model.addAttribute("listaTariffe", listaTariffeDef);
    model.addAttribute("listaAllTariffe", jsonString);
    model.addAttribute("pagelink", WsUtils.SIAPPAGOPA_PAGE_LINK);
    model.addAttribute("pageUrlAnnulla", WsUtils.SIAPPAGOPA_PAGE_URL_ANNULLA);
    model.addAttribute("pageReferral", WsUtils.SIAPPAGOPA_PAGE_REFERRAL);
    model.addAttribute("soggettoDTO", soggettoDTO);
    model.addAttribute("province", interventiEJB.getSiglaProvince(IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));

    logger.debug(this_method+" END ");
    return "accessolibero/pescaStep3";
  }
  
  
  @ResponseBody
  @RequestMapping(value = "/aggiornadatianagrafici", method = RequestMethod.GET)
  public String aggiornadatianagrafici(Model model, HttpSession session
      ,@RequestParam(value="idAnagrafePesca", required = false) Long idAnagrafePesca
      ,@RequestParam(value="flagCittadinoEstero", required = true) String flagCittadinoEstero
      ,@RequestParam(value="codiceFiscale", required = true) String codiceFiscale
      ,@RequestParam(value="cognome", required = false) String cognome
      ,@RequestParam(value="nome", required = false) String nome
      ,@RequestParam(value="email", required = false) String email
      ,@RequestParam(value="provresidenza", required = false) String provresidenza
      ,@RequestParam(value="tariffa", required = false) String tariffa
      ) throws InternalUnexpectedException, ApplicationException, JsonGenerationException, JsonMappingException, IOException
  {
    String this_method = this_class+"aggiornadatianagrafici_GET]";
    logger.debug(this_method+" BEGIN ");
    model.addAttribute("preferRequestValues", Boolean.TRUE);
    Errors errors = new Errors();
    errors.validateMandatoryFieldMaxLength(cognome, "cognome",100);
    errors.validateMandatoryFieldMaxLength(nome, "nome",100);
    errors.validateMandatory(tariffa, "tariffa");
    if(email!=null) 
      errors.validateFieldMaxLength(email, "email", 100);
    if(flagCittadinoEstero.equals("N")) 
      errors.validateMandatoryFieldMaxLength(provresidenza, "provresidenza", 2);
    else if(provresidenza!=null) 
      errors.validateFieldMaxLength(provresidenza, "provresidenza", 2);
    
    if(!errors.isEmpty()) {
      model.addAttribute("preferRequestValues", Boolean.TRUE);
      model.addAttribute("errors", errors);
      logger.debug(this_method+" ERROR "+errors.toString());
      
      Map<String, Object> common= getCommonFromSession(common_name, session, true);
      common.put(common_errori_validazione, errors);
      common.put(common_errori_cognome, cognome);
      common.put(common_errori_nome, nome);
      common.put(common_errori_mail, email);
      common.put(common_errori_provincia, provresidenza);
      saveCommonInSession(common, session);
      return "KO";
    }
    
    //salvo i dati e vado al pagamento
    SoggettoDTO soggettoDTO = new SoggettoDTO();
    soggettoDTO.setCodiceFiscale(codiceFiscale);
    soggettoDTO.setCognome(cognome);
    soggettoDTO.setEmail(email);
    soggettoDTO.setFlagCittadinoEstero(flagCittadinoEstero);
    soggettoDTO.setIdAnagrafePesca(idAnagrafePesca);
    soggettoDTO.setNome(nome);
    soggettoDTO.setSiglaProvinciaResidenza(provresidenza);
    Long idAnagrafica = quadroIuffiEJB.aggiornaAnagraficaPescatore(soggettoDTO);
    
    logger.debug(this_method+" setto idAnP in sessione "+idAnagrafica);
    session.setAttribute("idAnP", idAnagrafica);
    session.setAttribute("tariffa", tariffa);
    logger.debug(this_method+" END ");
    return "OK";
  }
  
  @RequestMapping(value = "/fine", method = RequestMethod.GET)
  public String step04_SiappagopaPost(Model model, HttpSession session,
      @RequestParam(value="iuv", required = false) String iuv,
      @RequestParam(value="esito", required = false) String esito
      ) throws InternalUnexpectedException, ApplicationException
  {
    String this_method = this_class+"step04_SiappagopaPost]";
    logger.debug(this_method+" BEGIN ");
    
    String tipo_pagamento;
    if(iuv.subSequence(0, 2).equals("RF"))
      tipo_pagamento="M1";
    else
      tipo_pagamento="M3";
    Object idAnagraficaPesca = session.getAttribute("idAnP");
    Object tariffa = session.getAttribute("tariffa");
    logger.debug(this_method+" iuv : "+iuv+" & idAnP : "+idAnagraficaPesca+" & esito "+esito);
    
    quadroIuffiEJB.inserisciPagamento(iuv,idAnagraficaPesca.toString(),esito,tariffa.toString(),tipo_pagamento);
    model.addAttribute("preferRequestValues", Boolean.FALSE);
    model.addAttribute("iuv", iuv);  
    model.addAttribute("esito", esito);
    model.addAttribute("idAnagraficaPesca", idAnagraficaPesca);
    model.addAttribute("tipo_pagamento", tipo_pagamento);
    List<ImportoLicenzaDTO> listaTariffe = quadroIuffiEJB.getImportiLicenzePesca();
    for(ImportoLicenzaDTO item:listaTariffe) {
      if(tariffa.toString().equals(""+item.getIdTipoLicenza()))
        model.addAttribute("importo", IuffiUtils.FORMAT.formatCurrency(item.getImporto()));
    }
    return "accessolibero/pescaStep4";
  }
}