package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaAmmCompentenza;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.OrganismoDelegatoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-251", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping(value = "/cuiuffi251")
public class CUIUFFI251ModificaOD extends BaseController
{
  @Autowired
  private IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index")
  @IsPopup
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    Procedimento procedimento = getProcedimentoFromSession(session);

    if (procedimento.getIdStatoOggetto() < (new Long("10")).longValue()
        || procedimento.getIdStatoOggetto() > (new Long("90")).longValue())
    {
      model.addAttribute("errore",
          "La modifica non può essere effettuata in base allo stato del procedimento.");
      return "dialog/soloErrore";
    }

    Map<String, Object> common = getCommonFromSession("CU-IUFFI-251", session,
        false);
    common.remove("idProcedimentiSelezionati");
    common.clear();
    saveCommonInSession(common, session);

    OrganismoDelegatoDTO delegatoDTO = quadroEJB
        .getOrganismoDelegato(procedimento.getIdProcedimento());
    BandoDTO bandoDTO = quadroEJB
        .getInformazioniBando(procedimento.getIdBando());
    List<DecodificaAmmCompentenza> ammcompetenzelist = quadroEJB
        .getListAmmCompetenzaAbilitateBando(bandoDTO.getIdBandoMaster());

    model.addAttribute("delegatoDTO", delegatoDTO);
    if (ammcompetenzelist != null)
    {
      model.addAttribute("ammcompetenzelist", ammcompetenzelist);
      model.addAttribute("ufficiZonalist",
          quadroEJB.getElencoUfficiZona(delegatoDTO.getExtIdAmmCompetenza()));
      model.addAttribute("tecniciList",
          quadroEJB.getTecniciByUfficioDiZona(delegatoDTO.getIdUfficioZona(), getUtenteAbilitazioni(session).getIdProcedimento()));
      model.addAttribute("idAmmSelezionato",
          delegatoDTO.getExtIdAmmCompetenza());
      model.addAttribute("idUfficioSelezionato",
          delegatoDTO.getIdUfficioZona());
      model.addAttribute("idTecnicoSelezionato", delegatoDTO.getExtIdTecnico());

    }
    return "operazioniprocedimento/popupModificaOD";
  }

  @RequestMapping(value = "/refreshUfficiZona", method = RequestMethod.POST)
  @ResponseBody
  public String refreshUfficiZona(Model model, HttpSession session,
      @RequestParam(value = "idAmministrazione", required = true) long idAmministrazione)
      throws InternalUnexpectedException
  {
    StringBuilder htmlOptions = new StringBuilder("");
    List<DecodificaDTO<String>> elencoUfficiZona = quadroEJB
        .getElencoUfficiZona(idAmministrazione);

    if (elencoUfficiZona != null && elencoUfficiZona.size() > 1)
      htmlOptions.append("<option value='-1'>-- seleziona --</option>");
    if (elencoUfficiZona != null)
    {
      for (DecodificaDTO<String> ufficio : elencoUfficiZona)
      {
        htmlOptions.append("<option title=\""
            + IuffiUtils.STRING.safeHTMLText(ufficio.getDescrizione())
            + "\" value=\"" + ufficio.getId()
            + "\">"
            + IuffiUtils.STRING.safeHTMLText(ufficio.getDescrizione())
            + "</option>");
      }
    }

    return htmlOptions.toString();
  }

  @RequestMapping(value = "/refreshTecnici", method = RequestMethod.POST)
  @ResponseBody
  public String refreshTecnici(Model model, HttpSession session,
      @RequestParam(value = "idUfficioZona", required = false) long idUfficioZona)
      throws InternalUnexpectedException
  {

    StringBuilder htmlOptions = new StringBuilder("");
    List<DecodificaDTO<Long>> elencoTecnici = quadroEJB
        .getTecniciByUfficioDiZona(idUfficioZona, getUtenteAbilitazioni(session).getIdProcedimento());
    if (idUfficioZona == -1)
      elencoTecnici = null;
    if (elencoTecnici != null && elencoTecnici.size() > 1)
      htmlOptions.append("<option value='-1'>-- seleziona --</option>");

    if (elencoTecnici != null)
    {
      for (DecodificaDTO<Long> tecnico : elencoTecnici)
      {
        htmlOptions.append("<option title=\""
            + IuffiUtils.STRING.safeHTMLText(tecnico.getDescrizione())
            + "\" value=\"" + tecnico.getId()
            + "\">"
            + IuffiUtils.STRING.safeHTMLText(tecnico.getDescrizione())
            + "</option>");
      }
    }

    return htmlOptions.toString();
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  @IsPopup
  @ResponseBody
  public String post(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "ammcompetenze", required = false) Long idAmministrazione,
      @RequestParam(value = "ufficiozona", required = false) Long idUfficiozona,
      @RequestParam(value = "tecnici", required = false) Long idTecnico,
      @RequestParam(value = "note", required = false) String note)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    model.addAttribute("prfvalues", Boolean.TRUE);
    errors.validateMandatory(idAmministrazione, "ammcompetenze");
    if (idUfficiozona != null && idUfficiozona == -1)
      idUfficiozona = null;
    errors.validateMandatory(idUfficiozona, "ufficiozona");

    if (!GenericValidator.isBlankOrNull(note))
    {
      if (!errors.validateFieldMaxLength(note, "note", 4000))
      {
        return "Il campo \"Note\" può contenere al massimo 4000 caratteri.";
      }
    }

    Map<String, Object> common = getCommonFromSession("CU-IUFFI-251", session,
        false);
    @SuppressWarnings("unchecked")
    Vector<Long> idProcedimentiSelezionati = (Vector<Long>) common
        .get("idProcedimentiSelezionati");

    boolean isMassivo = true;
    // se la modifica non è massiva la lista è vuota -> aggiunto il procedimento
    // corrente alla lista
    if (idProcedimentiSelezionati == null
        || idProcedimentiSelezionati.isEmpty())
    {
      idProcedimentiSelezionati = new Vector<>();
      idProcedimentiSelezionati.addElement(getIdProcedimento(session));
      isMassivo = false;
    }

    if (errors.isEmpty())
    {
      if (idTecnico != null && idTecnico.longValue() == -1)
        idTecnico = null;

      OrganismoDelegatoDTO delegatoDTO = quadroEJB
          .getOrganismoDelegato(idProcedimentiSelezionati.elementAt(0));
      if (delegatoDTO != null)
      {
        /*
         * controllo se i procedimenti selezionati hanno gli stessi uffici di
         * zona e tecnici
         */
        boolean allProcHaveSameTecnico = true, allProcHaveSameUff = true;
        allProcHaveSameUff = checkIfProcHaveSameUfficioZona(
            idProcedimentiSelezionati, delegatoDTO);
        allProcHaveSameTecnico = checkIfProcHaveSameTecnico(
            idProcedimentiSelezionati, delegatoDTO);

        if (allProcHaveSameUff)
        {
          /*
           * Se i procedimenti selezionati hanno lo stesso ufficio di zona, lo
           * confronto con quello selezionato. Se non cambia controllo il tecico
           */
          if (delegatoDTO.getExtIdAmmCompetenza() == idAmministrazione
              .longValue()
              && (delegatoDTO.getIdUfficioZona() == null
                  && idUfficiozona == null)
              || (delegatoDTO.getIdUfficioZona() != null
                  && idUfficiozona != null
                  && delegatoDTO.getIdUfficioZona().longValue() == idUfficiozona
                      .longValue()))
          {
            /*
             * se anche il tecnico è uguale e non cambia, allora segnalo
             * l'errore
             */
            if (allProcHaveSameTecnico)
              if ((delegatoDTO.getExtIdTecnico() == null && idTecnico == null)
                  || (delegatoDTO.getExtIdTecnico() != null
                      && idTecnico != null && delegatoDTO.getExtIdTecnico()
                          .longValue() == idTecnico.longValue()))
                return "<strong>Attenzione! Impossibile effettuare il trasferimento in quanto i dati inseriti coincidono con quelli precedenti.</strong>";
          }

        }
      }

    }

    // rieffettuo il controllo sui vincoli per la modifica, nel caso fossero
    // cambiati precedentemente.
    String s = checkInfo(idProcedimentiSelezionati);
    if (s.compareTo("SUCCESS") != 0)
    {
      return "<strong>Attenzione! Impossibile effettuare il trasferimento in quanto alcuni dati sono stati modificati mentre si stava eseguendo l'operazione.</strong>";
    }

    if (errors.isEmpty())
    {

      UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
      if (IuffiUtils.PAPUASERV
          .hasAmministrazioneCompetenza(utenteAbilitazioni, idAmministrazione)
          || IuffiUtils.PAPUASERV.hasAmministrazioneCompetenza(
              utenteAbilitazioni,
              quadroEJB.getIdAmmCompetenzaProcedimento(
                  idProcedimentiSelezionati.elementAt(0))))
      {
        quadroEJB.updateOrganismoDelegato(idProcedimentiSelezionati,
            idAmministrazione, idUfficiozona, getIdUtenteLogin(session), note,
            idTecnico);

        if (!isMassivo)
          return "redirect:../cuiuffi129/index_"
              + idProcedimentiSelezionati.elementAt(0) + ".do";
        else
          return "SUCCESS-MASSIVO";
      }
      else
      {
        return "<strong>Impossibile completare l'aggiornamento dell'Organismo Delegato in quanto non si dispongono delle autorizzazioni necessarie.</strong>";
      }
    }

    return "<strong>I campi \"Nuovo Organismo Delegato\" e \"Ufficio di zona\" sono obbligatori</strong>";
  }

  private boolean checkIfProcHaveSameTecnico(
      Vector<Long> idProcedimentiSelezionati, OrganismoDelegatoDTO delegatoDTO)
      throws InternalUnexpectedException
  {
    return quadroEJB.checkIfProcHaveSameTecnico(idProcedimentiSelezionati);
  }

  private boolean checkIfProcHaveSameUfficioZona(
      Vector<Long> idProcedimentiSelezionati, OrganismoDelegatoDTO delegatoDTO)
      throws InternalUnexpectedException
  {
    return quadroEJB.checkIfProcHaveSameUfficioZona(idProcedimentiSelezionati);
  }

  @RequestMapping(value = "checkInfoPerTrasferimentoMassivo", method = RequestMethod.POST)
  @IsPopup
  public @ResponseBody String checkInfoPerTrasferimentoMassivo(
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-251", session,
        false);
    common.remove("idProcedimentiSelezionati");
    common.clear();
    saveCommonInSession(common, session);

    String[] ids = request.getParameterValues("cBP");
    Vector<Long> vect = new Vector<Long>();
    for (String s : ids)
    {
      vect.addElement(Long.parseLong(s));
    }
    /*
     * if (ids == null || ids == "") return null; ids = ids.replaceAll("=", "");
     * String[] idProcSelezionati = ids.split("&");
     * 
     * Vector<Long> vect = new Vector<Long>(); for (String s :
     * idProcSelezionati) { vect.addElement(Long.parseLong(s)); }
     */

    return checkInfo(vect);

  }

  private String checkInfo(Vector<Long> vect) throws InternalUnexpectedException
  {
    // tutti i procedimenti selezionati devono avere la stessa
    // amministrazione di competenza
    boolean hannoTuttiLaStessaAmm = quadroEJB
        .checkSeIProcSelezHannoLaStessaAmm(vect);
    if (!hannoTuttiLaStessaAmm)
      return "Impossibile procedere: le pratiche selezionate non appartengono allo stesso Organismo Delegato.";

    boolean checkStatoOggetto = quadroEJB.checkStatiOggetti(vect);
    if (!checkStatoOggetto)
      return "La modifica non può essere effettuata in base allo stato del procedimento.";

    // tutti i procedimenti selezionati devono appartenere allo stesso bando
    boolean checkTipologiaBando = quadroEJB
        .checkSeIProcSelezAppartengonoAlloStessoBando(vect);
    if (!checkTipologiaBando)
      return "Impossibile effettuare la modifica su pratiche non appartenenti allo stesso bando.";

    return "SUCCESS";
  }

  @RequestMapping(value = "trasferimentoMassivo", method = RequestMethod.GET)
  @IsPopup
  public String trasferimentoMassivo(HttpSession session,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {

    String[] ids = request.getParameterValues("cBP");
    Vector<Long> vect = new Vector<Long>();
    for (String s : ids)
    {
      vect.addElement(Long.parseLong(s));
    }

    Map<String, Object> common = getCommonFromSession("CU-IUFFI-251", session,
        false);
    common.put("idProcedimentiSelezionati", vect);
    saveCommonInSession(common, session);

    boolean allProcHaveSameUff = true;
    boolean allProcHaveSameTecnico = true;

    OrganismoDelegatoDTO delegatoDTO = quadroEJB
        .getOrganismoDelegato(vect.get(0));

    allProcHaveSameUff = checkIfProcHaveSameUfficioZona(vect, delegatoDTO);
    allProcHaveSameTecnico = checkIfProcHaveSameTecnico(vect, delegatoDTO);

    BandoDTO bandoDTO = quadroEJB.getInformazioniBando(
        quadroEJB.getProcedimento(vect.get(0)).getIdBando());
    List<DecodificaAmmCompentenza> ammcompetenzelist = quadroEJB
        .getListAmmCompetenzaAbilitateBando(bandoDTO.getIdBandoMaster());

    if (!allProcHaveSameUff)
    {
      delegatoDTO.setIdUfficioZona(null);
      delegatoDTO.setExtIdTecnico(null);
    }
    if (!allProcHaveSameTecnico)
      delegatoDTO.setExtIdTecnico(null);

    model.addAttribute("delegatoDTO", delegatoDTO);
    if (ammcompetenzelist != null)
    {
      model.addAttribute("ammcompetenzelist", ammcompetenzelist);
      model.addAttribute("ufficiZonalist",
          quadroEJB.getElencoUfficiZona(delegatoDTO.getExtIdAmmCompetenza()));
      model.addAttribute("idUfficioSelezionato",
          delegatoDTO.getIdUfficioZona());

      if (allProcHaveSameUff)
      {
        model.addAttribute("tecniciList", quadroEJB
            .getTecniciByUfficioDiZona(delegatoDTO.getIdUfficioZona(), getUtenteAbilitazioni(session).getIdProcedimento()));

        if (allProcHaveSameTecnico)
        {
          model.addAttribute("idTecnicoSelezionato",
              delegatoDTO.getExtIdTecnico());
        }
      }
      model.addAttribute("idAmmSelezionato",
          delegatoDTO.getExtIdAmmCompetenza());
      model.addAttribute("isTrasferimentoMassivo", true);

    }
    return "operazioniprocedimento/popupModificaOD";

  }

  @RequestMapping(value = "/trasferimentoMassivo", method = RequestMethod.POST)
  @IsPopup
  @ResponseBody
  public String postMassivo(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "ammcompetenze", required = false) Long idAmministrazione,
      @RequestParam(value = "ufficiozona", required = false) Long idUfficiozona,
      @RequestParam(value = "tecnici", required = false) Long idTecnico,
      @RequestParam(value = "note", required = false) String note)
      throws InternalUnexpectedException
  {

    return post(model, session, request, idAmministrazione, idUfficiozona,
        idTecnico, note);
  }

  @RequestMapping(value = "/attendere")
  @IsPopup
  public String attendere(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    model.addAttribute("messaggio",
        "Attendere prego: Il sistema sta effettuando il controllo sui dati inseriti; l'operazione potrebbe richiedere alcuni secondi...");
    return "operazioniprocedimento/attenderePrego";
  }

}
