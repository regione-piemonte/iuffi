package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DecodificaInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.MisurazioneInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class Inserisci extends BaseController
{
  public static final BigDecimal NESSUN_VALORE = new BigDecimal(-1);;
  @Autowired
  protected IInterventiEJB       interventiEJB;
  @Autowired
  protected IQuadroEJB                     quadroEJB;
  
  @Autowired
  protected IQuadroIuffiEJB				 quadroIuffiEJB;
  
  @RequestMapping(value = "/popup_seleziona_interventi", method = RequestMethod.GET)
  public String popupSelezionaInterventi(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
	  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	  getListDanniInterventi(model, idProcedimentoOggetto);
	  return "interventi/popupSelezionaInterventi";
  }

  
  protected abstract void getListDanniInterventi(Model model, long idProcedimentoOggetto) throws InternalUnexpectedException;


  public abstract String getFlagEscludiCatalogo();

  protected void verificaNecessitaColonnaImportoUnitario(
      List<RigaModificaMultiplaInterventiDTO> list, Model model)
  {
    for (RigaModificaMultiplaInterventiDTO riga : list)
    {
      if (IuffiConstants.FLAGS.SI
          .equals(riga.getFlagGestioneCostoUnitario()))
      {
        model.addAttribute("importoUnitario", Boolean.TRUE);
        return;
      }
    }
    model.addAttribute("importoUnitario", Boolean.FALSE);
  }

  @RequestMapping(value = "/popup_seleziona_interventi", method = RequestMethod.POST)
  public String postPopupSelezionaInterventi(HttpSession session, Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    if (request.getParameter("prosegui") != null) // Non mi interessa il valore,
                                                  // basta che esista
    {
      Errors errors = new Errors();
      String ids[] = request.getParameterValues("idDescrizioneIntervento");
      aggiungiDannoAtmAlModel(model, request);
      errors.validateMandatory(ids, "error","Selezionare almeno un intervento tra quelli disponibili per procedimento");
      if (!errors.addToModelIfNotEmpty(model))
      {
        model.addAttribute("idDescrizioneIntervento", ids);
        return "interventi/successSelezionaInterventi";
      }
    }

    return popupSelezionaInterventi(session,model);
  }
  
  public abstract void aggiungiDannoAtmAlModel(Model model, HttpServletRequest request);

  @RequestMapping(value = "/json/load_elenco_interventi", produces = "application/json")
  @ResponseBody
  public Map<String, Map<String, String>> loadElencoInterventi(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    return loadElencoInterventiGenerico(session,request);
  }
 
protected Map<String, Map<String, String>> loadElencoInterventiGenerico(HttpSession session, HttpServletRequest request)
		throws InternalUnexpectedException
{
	long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	List<DecodificaInterventoDTO> interventi = null;
	
	interventi = getListInterventiForInsert(idProcedimentoOggetto,request);
    Map<String, Map<String, String>> mapElenco = new HashMap<String, Map<String, String>>();
    for (DecodificaInterventoDTO intervento : interventi)
    {
      Map<String, String> element = new HashMap<String, String>();
      String id = String.valueOf(intervento.getId());
      element.put("idDescrizioneIntervento", id);
      element.put("descrizioneIntervento", intervento.getDescrizione());
      element.put("gruppo",
          intervento.getDescrizioneAggregazionePrimoLivello());
      mapElenco.put(id, element);
    }
    return mapElenco;
}
  

  protected abstract List<DecodificaInterventoDTO> getListInterventiForInsert(
		  long idProcedimentoOggetto,
		  HttpServletRequest request
		  ) throws InternalUnexpectedException;
  
  @RequestMapping(value = "/inserisci", method = RequestMethod.POST)
  public String inserisci(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    String[] idDescrizioneIntervento = request.getParameterValues("id");
    aggiungiDannoAtmAlModel(model, request);
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idDescrizioneIntervento);

    Procedimento procedimento = getProcedimentoFromSession(request);
    List<RigaModificaMultiplaInterventiDTO> list = getInfoInterventiPerModifica(
        ids, procedimento.getIdBando());
    boolean attivaGestioneBeneficiario = false;

    if (request.getParameter("confermaModificaInterventi") != null)
    {
      if (validateAndInsert(model, request, list))
      {
        return "redirect:../cuiuffi"
            + IuffiUtils.APPLICATION.getCUNumber(request) + "l/index.do";
      }
    }
    List<Long> listIdMinMaxCostiUguali = new ArrayList<>();
	if(list!=null){
    	for(RigaModificaMultiplaInterventiDTO i:list){
    		if(i.getCostoUnitarioMinimo()!=null && i.getCostoUnitarioMassimo()!=null &&
    				i.getCostoUnitarioMinimo().equals(i.getCostoUnitarioMassimo())){
    			listIdMinMaxCostiUguali.add(i.getId());
    	    }
    	}
    }
    model.addAttribute("unicoCostoPossibile", listIdMinMaxCostiUguali);
    model.addAttribute("attivaGestioneBeneficiario", attivaGestioneBeneficiario);
    model.addAttribute("preferRequest", Boolean.TRUE);
    model.addAttribute("interventi", list);
    verificaNecessitaColonnaImportoUnitario(list, model);
    preloadDataForInserimento(model, request, list);
    return getJSPBaseFolder() + "/modificaMultipla";
  }

  @RequestMapping(value = "/ricercaPartecipantePopup", method = RequestMethod.GET)
  @IsPopup
  public String ricercaPartecipantePopup(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "interventi/popupRicercaPartecipante";
  }

  protected void preloadDataForInserimento(Model model,
      HttpServletRequest request, List<RigaModificaMultiplaInterventiDTO> list)
      throws InternalUnexpectedException
  {
  }

  protected abstract List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(
      List<Long> ids, long idBando) throws InternalUnexpectedException;

  /*
   * Nota: Il metodo è riscritto nella classe CUIUFFI266MModificaInterventi,
   * mantenendo al contempo la logica "standard" e aggiungendo la validazione
   * delle attività/partecipanti, eventuali modifiche alla logica di base
   * dovrebbero essere riportate anche nel metodo
   * CUIUFFI266MModificaInterventi.validateAndUpdate
   * 
   */
  protected boolean validateAndInsert(Model model, 
	  HttpServletRequest request,
      List<RigaModificaMultiplaInterventiDTO> list)
      throws InternalUnexpectedException, ApplicationException
  {
    HttpSession session = request.getSession();
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    Errors errors = new Errors();
    for (RigaModificaMultiplaInterventiDTO riga : list)
    {
      final long idDescrizioneIntervento = riga.getIdDescrizioneIntervento();
      final String nameUlterioriInformazioni = "ulteriori_informazioni_"
          + idDescrizioneIntervento;
      String ulterioriInformazioni = request
          .getParameter(nameUlterioriInformazioni);
      if (errors.validateFieldMaxLength(ulterioriInformazioni,
          nameUlterioriInformazioni, 500))
      {
        riga.setUlterioriInformazioni(ulterioriInformazioni);
      }
      BigDecimal importoUnitario = null;
      BigDecimal importo = null;
      List<MisurazioneInterventoDTO> misurazioni = riga
          .getMisurazioneIntervento();
      int idx = 0;
      for (MisurazioneInterventoDTO misurazione : misurazioni)
      {
        if (misurazione.isMisuraVisibile())
        {
          final String nameValore = "valore_" + idDescrizioneIntervento + "_"
              + (idx++);
          BigDecimal bd = errors.validateMandatoryBigDecimalInRange(
              request.getParameter(nameValore), nameValore, 4, BigDecimal.ZERO,
              IuffiConstants.MAX.VALORE_INTERVENTO);
          misurazione.setValore(bd);
        }
        else
        {
          misurazione.setValore(NESSUN_VALORE); // L'unità di misura è una unità
                                                // fittizia, non verrà mai usata
                                                // direttamente, il valore 0
                                                // serve solo per poter
                                                // mettere il record su db dato
                                                // che il campo è NOT NULL
        }
      }
      boolean flagCostoUnitario = IuffiConstants.FLAGS.SI
          .equals(riga.getFlagGestioneCostoUnitario());
      if (flagCostoUnitario)
      {
        final String nameImportoUnitario = "importo_unitario_"
            + idDescrizioneIntervento;
        BigDecimal costoUnitarioMinimo = riga.getCostoUnitarioMinimo();
        BigDecimal costoUnitarioMassimo = riga.getCostoUnitarioMassimo();
        if (costoUnitarioMinimo == null)
        {
          // Errore di configurazione DB!
          throw new ApplicationException(
              "Si &egrave; verificato un errore di sistema, si prega di contattare l'assistenza tecnica comunicando il seguente messaggio. Costo unitario minimo dell'intervento/investimento non configurato per id_descrizione = #"
                  + riga.getIdDescrizioneIntervento());
        }
        if (costoUnitarioMassimo == null)
        {
          // Errore di configurazione DB!
          throw new ApplicationException(
              "Si &egrave; verificato un errore di sistema, si prega di contattare l'assistenza tecnica comunicando il seguente messaggio. Costo unitario massimo dell'intervento/investimento non configurato per id_descrizione = #"
                  + riga.getIdDescrizioneIntervento());
        }
        if(riga.getCostoUnitarioMinimo()!=null && riga.getCostoUnitarioMassimo()!= null &&
        		riga.getCostoUnitarioMassimo().equals(riga.getCostoUnitarioMinimo())){
        			 importoUnitario = riga.getCostoUnitarioMassimo();
        }else{
        	importoUnitario = errors.validateMandatoryBigDecimalInRange(
          request.getParameter(nameImportoUnitario), nameImportoUnitario, 2,
            costoUnitarioMinimo,
            costoUnitarioMassimo);
        }
        
        // trovato neppure il record)
        if (importoUnitario != null)
        {
          BigDecimal valore = misurazioni.get(0).getValore(); // C'è sempre
                                                              // almeno una
                                                              // misurazione per
                                                              // intervento
                                                              // (altrimenti la
                                                              // query andando
                                                              // in join "secca"
                                                              // non avrebbe
          if (valore != null)
          {
            importo = importoUnitario.multiply(valore, MathContext.UNLIMITED);
            if (importo.compareTo(IuffiConstants.MAX.IMPORTO_INTERVENTO) > 0)
            {
              final String errorMessage = "Il prodotto dell'importo unitario per la quantità è superiore al massimo consentito (pari a "
                  + IuffiUtils.FORMAT.formatDecimal2(IuffiConstants.MAX.IMPORTO_INTERVENTO) + " €)";
              final String nameValore = "valore_" + idDescrizioneIntervento
                  + "_0";
              errors.addError(nameValore, errorMessage);
              if(riga.getCostoUnitarioMinimo()!=null && riga.getCostoUnitarioMassimo()!= null){
	        	  if(riga.getCostoUnitarioMassimo().equals(riga.getCostoUnitarioMinimo())){
	        		  List<Long> listIdMinMaxCostiUguali = new ArrayList<>();
	        		    if(list!=null){
	        		    	for(RigaModificaMultiplaInterventiDTO i:list){
	        		    		if(i.getCostoUnitarioMinimo()!=null && i.getCostoUnitarioMassimo()!= null &&
	        		    				i.getCostoUnitarioMassimo().equals(i.getCostoUnitarioMinimo())){
	        		    			listIdMinMaxCostiUguali.add(i.getId());
	        		    	    }
	        		    	}
	        		    }            		    
	        		    model.addAttribute("unicoCostoPossibile", listIdMinMaxCostiUguali); 
	        	  }else{
	        		  errors.addError(nameImportoUnitario, errorMessage);
	        	  }
              }             
            }
          }
        }
      }
      else
      {
        final String nameImporto = "importo_" + idDescrizioneIntervento;
        importo = errors.validateMandatoryBigDecimalInRange(
            request.getParameter(nameImporto), nameImporto, 2, BigDecimal.ZERO,
            IuffiConstants.MAX.IMPORTO_INTERVENTO);
      }

      if ("S".equals(riga.getFlagBeneficiario()))
      {
        String cuaa = request
            .getParameter("hbeneficiario_" + idDescrizioneIntervento);
        errors.validateMandatory(cuaa,
            "beneficiario_" + idDescrizioneIntervento);
        riga.setCuaaPartecipante(cuaa);
      }

      riga.setImporto(importo);
      riga.setImportoUnitario(importoUnitario);
      riga.setIdProcedimentoOggetto(idProcedimentoOggetto);
      additionalValidations(request, idProcedimentoOggetto, riga, errors);
    }
    if (!errors.addToModelIfNotEmpty(model))
    {
    	insertInterventi(list, request, getLogOperationOggettoQuadroDTO(session));
      return true;
    }
    else
    {
      return false;
    }
  }
  
  protected abstract void insertInterventi(
		  	List<RigaModificaMultiplaInterventiDTO>  list,
		  	HttpServletRequest request,
		  	LogOperationOggettoQuadroDTO logOperationOggettoQuadroDto) throws InternalUnexpectedException, ApplicationException;

  protected void additionalValidations(HttpServletRequest request,
      long idProcedimentoOggetto,
      RigaModificaMultiplaInterventiDTO riga, Errors errors)
  {
  }

  protected String getJSPBaseFolder()
  {
    return "interventi";
  }
}