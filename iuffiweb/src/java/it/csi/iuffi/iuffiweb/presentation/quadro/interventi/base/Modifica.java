package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoRiduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.MisurazioneInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class Modifica extends BaseController
{
  protected static final BigDecimal NESSUN_VALORE = Inserisci.NESSUN_VALORE;
  @Autowired
  protected IInterventiEJB          interventiEJB;
  @Autowired
  IQuadroEJB                        quadroEJB;

  @RequestMapping(value = "/modifica", method = RequestMethod.POST)
  public String controllerModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    String[] idIntervento = request.getParameterValues("id");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    Procedimento procedimento = getProcedimentoFromSession(session);
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    final long idBando = procedimento.getIdBando();
    List<RigaModificaMultiplaInterventiDTO> list = getInfoInterventiPerModifica(
        ids, idProcedimentoOggetto, idBando);
    boolean attivaGestioneBeneficiario = false;

    if (request.getParameter("confermaModificaInterventi") != null)
    {
      if (validateAndUpdate(model, request, list))
      {
        return "redirect:../cuiuffi"
            + IuffiUtils.APPLICATION.getCUNumber(request) + "l/index.do";
      }
      else
      {
        model.addAttribute("preferRequest", Boolean.TRUE);
      }
    }

    model.addAttribute("attivaGestioneBeneficiario",
        attivaGestioneBeneficiario);
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi"
        + IuffiUtils.APPLICATION.getCUNumber(request) + "m/modifica.do");
    verificaNecessitaColonne(list, model);
    preloadData(model, list, request, idProcedimentoOggetto);
    return getJSPBaseFolder() + "/modificaMultipla";
  }

  @RequestMapping(value = "/ricercaPartecipantePopup", method = RequestMethod.GET)
  @IsPopup
  public String ricercaPartecipantePopup(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "interventi/popupRicercaPartecipante";
  }

  protected abstract List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(
      List<Long> ids,
      final long idProcedimentoOggetto, final long idBando)
      throws InternalUnexpectedException;

  @RequestMapping(value = "/modifica_multipla", method = RequestMethod.POST)
  public String modificaMultipla(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    String[] idIntervento = request.getParameterValues("idIntervento");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    Procedimento procedimento = getProcedimentoFromSession(session);
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaModificaMultiplaInterventiDTO> list = getInfoInterventiPerModifica(
        ids, idProcedimentoOggetto,
        procedimento.getIdBando());
    for(RigaModificaMultiplaInterventiDTO i : list){
    	if(i!=null && IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE.equalsIgnoreCase(i.getCodiceIdentificativo())){
    		model.addAttribute("errore", "Intervento '"+i.getDescIntervento()+"' non modificabile");
    		return "errore/utenteNonAutorizzato";
    	}
    }
    if (checkForDeleted(list, model))
    {
      return "errore/utenteNonAutorizzato";
    }
    List<Long> listIdMinMaxCostiUguali = new ArrayList<>();
    boolean attivaGestioneBeneficiario = false;
    if (list != null)
    {
      for (RigaModificaMultiplaInterventiDTO i : list)
      {
        if (IuffiConstants.FLAGS.SI.equals(i.getFlagBeneficiario()))
        {
          attivaGestioneBeneficiario = true;
          break;
        }
        if(i.getCostoUnitarioMinimo()!=null && i.getCostoUnitarioMassimo()!= null &&
				i.getCostoUnitarioMassimo().equals(i.getCostoUnitarioMinimo())){
			listIdMinMaxCostiUguali.add(i.getId());
	    }
      }
    }    
    model.addAttribute("unicoCostoPossibile", listIdMinMaxCostiUguali); 

    model.addAttribute("attivaGestioneBeneficiario",
        attivaGestioneBeneficiario);

    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi"
        + IuffiUtils.APPLICATION.getCUNumber(request) + "m/modifica.do");
    verificaNecessitaColonne(list, model);
    preloadData(model, list, request, idProcedimentoOggetto);
    return getJSPBaseFolder() + "/modificaMultipla";
  }

  protected void verificaNecessitaColonne(
      List<RigaModificaMultiplaInterventiDTO> list,
      Model model)
  {
    boolean progressivo = false;
    boolean importoUnitario = false;
    boolean importoAmmesso = false;

    for (RigaModificaMultiplaInterventiDTO riga : list)
    {
      if (IuffiConstants.FLAGS.SI
          .equals(riga.getFlagGestioneCostoUnitario()))
      {
        importoUnitario = true;
      }
      if (riga.getProgressivo() != null)
      {
        progressivo = true;
      }
      if (!importoAmmesso)
      {
        final BigDecimal bdImportoAmmesso = riga.getImportoAmmesso();
        if (bdImportoAmmesso != null
            && BigDecimal.ZERO.compareTo(bdImportoAmmesso) != 0)
        {
          importoAmmesso = true;
        }
      }
    }
    model.addAttribute("progressivo", Boolean.valueOf(progressivo));
    model.addAttribute("importoUnitario", Boolean.valueOf(importoUnitario));
    model.addAttribute("importoAmmesso", Boolean.valueOf(importoAmmesso));
  }

  public boolean checkForDeleted(List<RigaModificaMultiplaInterventiDTO> list,
      Model model)
  {
    if (list != null)
    {
      for (RigaModificaMultiplaInterventiDTO intervento : list)
      {
        if (IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE
            .equals(intervento.getFlagTipoOperazione()))
        {
          model.addAttribute("errore",
              "L'intervento \"" + intervento.getDescIntervento()
                  + "\" è cessato, impossibile modificarlo");
          return true;
        }
      }
    }
    return false;
  }

  @RequestMapping(value = "/modifica_singola_{idIntervento}", method = RequestMethod.GET)
  public String modificaSingola(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<Long> ids = new ArrayList<Long>();
    ids.add(idIntervento);
    Procedimento procedimento = getProcedimentoFromSession(session);
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaModificaMultiplaInterventiDTO> list = getInfoInterventiPerModifica(
        ids, idProcedimentoOggetto,
        procedimento.getIdBando());
    for(RigaModificaMultiplaInterventiDTO i : list){
    	if(i!=null && IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE.equalsIgnoreCase(i.getCodiceIdentificativo())){
    		model.addAttribute("errore", "Intervento '"+i.getDescIntervento()+"' non modificabile");
    		return "errore/utenteNonAutorizzato";
    	}
    }
    if (checkForDeleted(list, model))
    {
      return "errore/utenteNonAutorizzato";
    }
    List<Long> listIdMinMaxCostiUguali = new ArrayList<>();
    boolean attivaGestioneBeneficiario = false;
    if (list != null)
    {
      for (RigaModificaMultiplaInterventiDTO i : list)
      {
        if (IuffiConstants.FLAGS.SI.equals(i.getFlagBeneficiario()))
        {
          attivaGestioneBeneficiario = true;
          break;
        }
        if(i.getCostoUnitarioMinimo()!=null && i.getCostoUnitarioMassimo()!= null &&
				i.getCostoUnitarioMassimo().equals(i.getCostoUnitarioMinimo())){
			listIdMinMaxCostiUguali.add(i.getId());
	    }
      }
    }
    model.addAttribute("unicoCostoPossibile", listIdMinMaxCostiUguali);
    model.addAttribute("attivaGestioneBeneficiario",
        attivaGestioneBeneficiario);
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi"
        + IuffiUtils.APPLICATION.getCUNumber(request) + "m/modifica.do");
    verificaNecessitaColonne(list, model);
    preloadData(model, list, request, idProcedimentoOggetto);
    return getJSPBaseFolder() + "/modificaMultipla";
  }

  /*
   * Nota: Il metodo è riscritto nella classe CUIUFFI266MModificaInterventi,
   * mantenendo al contempo la logica "standard" e aggiungendo la validazione
   * delle attività/partecipanti, eventuali modifiche alla logica di base
   * dovrebbero essere riportate anche nel metodo
   * CUIUFFI266MModificaInterventi.validateAndUpdate
   */
  protected boolean validateAndUpdate(Model model, HttpServletRequest request,
      List<RigaModificaMultiplaInterventiDTO> list)
      throws InternalUnexpectedException, ApplicationException
  {
    HttpSession session = request.getSession();
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    Errors errors = new Errors();
    for (RigaModificaMultiplaInterventiDTO riga : list)
    {
      final long idIntervento = riga.getId();
      final String nameUlterioriInformazioni = "ulteriori_informazioni_"
          + idIntervento;
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
          final String nameValore = "valore_" + idIntervento + "_" + (idx++);
          BigDecimal bd = errors.validateMandatoryBigDecimalInRange(
              request.getParameter(nameValore), nameValore, 4,
              BigDecimal.ZERO,
              IuffiConstants.MAX.VALORE_INTERVENTO);
          misurazione.setValore(bd);
        }
        else
        {
          misurazione.setValore(NESSUN_VALORE);
        }
      }
      boolean flagCostoUnitario = IuffiConstants.FLAGS.SI
          .equals(riga.getFlagGestioneCostoUnitario());
      if (flagCostoUnitario)
      {
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

        final String nameImportoUnitario = "importo_unitario_" + idIntervento;
        if(riga.getCostoUnitarioMinimo()!=null && riga.getCostoUnitarioMassimo()!= null &&
        		riga.getCostoUnitarioMassimo().equals(riga.getCostoUnitarioMinimo())){
        			 importoUnitario = riga.getCostoUnitarioMassimo();
        }else{
        	importoUnitario = errors.validateMandatoryBigDecimalInRange(
                    request.getParameter(nameImportoUnitario),
                    nameImportoUnitario, 2, costoUnitarioMinimo,
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
              final String nameValore = "valore_" + idIntervento + "_0";
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
        final String nameImporto = "importo_" + idIntervento;
        importo = errors.validateMandatoryBigDecimalInRange(
            request.getParameter(nameImporto), nameImporto, 2,
            BigDecimal.ZERO,
            IuffiConstants.MAX.IMPORTO_INTERVENTO);
      }

      if ("S".equals(riga.getFlagBeneficiario()))
      {
        String cuaa = request.getParameter("hbeneficiario_" + idIntervento);
        errors.validateMandatory(cuaa, "beneficiario_" + idIntervento);
        riga.setCuaaPartecipante(cuaa);
      }

      riga.setImporto(importo);
      riga.setImportoUnitario(importoUnitario);
      riga.setIdProcedimentoOggetto(idProcedimentoOggetto);
      additionalValidations(request, idProcedimentoOggetto, riga, errors);
    }
    if (!errors.addToModelIfNotEmpty(model))
    {
      interventiEJB.updateInterventi(list,
          getLogOperationOggettoQuadroDTO(session));
      return true;
    }
    else
    {
      return false;
    }
  }

  @RequestMapping(value = "/modificaPercentuale", method = RequestMethod.POST)
  public String modificaPercentuale(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    String percRiduzione = request.getParameter("percRiduzione");
    Errors errors = new Errors();
    BigDecimal bdPercentuale = errors.validateMandatoryBigDecimalInRange(
        percRiduzione, "percRiduzione", 2,
        BigDecimal.ZERO, IuffiConstants.MAX.PERCENTUALE);
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(
        request.getSession());
    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("inModifica", Boolean.TRUE);
      model.addAttribute("percRiduzione", percRiduzione);
      model.addAttribute("totaleRichiesto", "Non calcolabile");
      final InfoRiduzione infoRiduzione = interventiEJB
          .getInfoRiduzione(idProcedimentoOggetto);
      model.addAttribute("salvaPercentuale",
          IuffiUtils.FORMAT.formatDecimal2(infoRiduzione.getPercentuale()));
      model.addAttribute("salvaRichiesto", infoRiduzione.getTotaleRichiesto());
      model.addAttribute("totaleInterventi", IuffiUtils.FORMAT
          .formatDecimal2(infoRiduzione.getTotaleImporto()));
    }
    else
    {
      interventiEJB.updatePercentualeRibassoInterventi(idProcedimentoOggetto,
          bdPercentuale);
      final InfoRiduzione infoRiduzione = interventiEJB
          .getInfoRiduzione(idProcedimentoOggetto);
      model.addAttribute("percRiduzione",
          IuffiUtils.FORMAT.formatDecimal2(infoRiduzione.getPercentuale()));
      model.addAttribute("totaleRichiesto",
          infoRiduzione.getTotaleRichiestoEuro());
    }
    return getJSPBaseFolder() + "/include/percentualeRiduzione";
  }

  protected String getJSPBaseFolder()
  {
    return "interventi";
  }

  protected void preloadData(Model model,
      List<RigaModificaMultiplaInterventiDTO> list, HttpServletRequest request,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
  }

  protected void additionalValidations(HttpServletRequest request,
      long idProcedimentoOggetto,
      RigaModificaMultiplaInterventiDTO riga, Errors errors)
  {
    // TODO Auto-generated method stub

  }
}