package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.MisurazioneInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-133-MS", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi133ms")
public class CUIUFFI133MSModificaASaldo extends BaseController
{
  @Autowired
  protected IInterventiEJB interventiEJB;

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
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi"
        + IuffiUtils.APPLICATION.getCUNumber(request) + "ms/modifica.do");
    verificaNecessitaColonne(list, model);
    return "interventi/saldo/modificaMultipla";
  }

  protected void verificaNecessitaColonne(
      List<RigaModificaMultiplaInterventiDTO> list,
      Model model)
  {
    boolean importoAmmesso = false;

    for (RigaModificaMultiplaInterventiDTO riga : list)
    {
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
    model.addAttribute("importoAmmesso", Boolean.valueOf(importoAmmesso));
  }

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
    if (checkIsValid(list, model))
    {
      return "errore/utenteNonAutorizzato";
    }
    model.addAttribute("interventi", list);
    verificaNecessitaColonne(list, model);
    return "interventi/saldo/modificaMultipla";
  }

  public boolean checkIsValid(List<RigaModificaMultiplaInterventiDTO> list,
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
        if (IuffiConstants.FLAGS.SI
            .equals(intervento.getFlagGestioneCostoUnitario()))
        {
          model.addAttribute("errore",
              "L'intervento \"" + intervento.getDescIntervento()
                  + "\" viene gestito con costo unitario, impossibile modificarlo");
          return true;
        }
        if (intervento
            .getIdTipoLocalizzazione() == IuffiConstants.INTERVENTI.LOCALIZZAZIONE.PARTICELLE_AZIENDALI_IMPIANTI_BOSCHIVI)
        {
          model.addAttribute("errore",
              "L'intervento \"" + intervento.getDescIntervento()
                  + "\" ha un tipo di localizzazione non compatibile con la modifica");
          return true;
        }
        if (!intervento.getMisurazioneIntervento().get(0).isMisuraVisibile())
        {
          model.addAttribute("errore",
              "L'intervento \"" + intervento.getDescIntervento()
                  + "\" non è soggetto a misurazione, impossibile modificarlo");
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
    if (checkIsValid(list, model))
    {
      return "errore/utenteNonAutorizzato";
    }
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi"
        + IuffiUtils.APPLICATION.getCUNumber(request) + "ms/modifica.do");
    verificaNecessitaColonne(list, model);
    return "interventi/saldo/modificaMultipla";
  }

  private boolean validateAndUpdate(Model model, HttpServletRequest request,
      List<RigaModificaMultiplaInterventiDTO> elenco)
      throws InternalUnexpectedException, ApplicationException
  {
    HttpSession session = request.getSession();
    Errors errors = new Errors();
    List<List<MisurazioneInterventoDTO>> list = new ArrayList<>();
    for (RigaModificaMultiplaInterventiDTO riga : elenco)
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
      List<MisurazioneInterventoDTO> misurazioni = riga
          .getMisurazioneIntervento();
      int idx = 0;
      for (MisurazioneInterventoDTO misurazione : misurazioni)
      {
        final String nameValore = "valore_" + idIntervento + "_" + (idx++);
        BigDecimal bd = errors.validateMandatoryBigDecimalInRange(
            request.getParameter(nameValore), nameValore, 4,
            BigDecimal.ZERO,
            IuffiConstants.MAX.VALORE_INTERVENTO);
        if (bd != null)
        {
          misurazione.setValore(bd);
        }
      }
      list.add(misurazioni);
    }
    if (!errors.addToModelIfNotEmpty(model))
    {
      interventiEJB.updateMisurazioniInterventoSaldo(elenco,
          getLogOperationOggettoQuadroDTO(session));
      return true;
    }
    else
    {
      return false;
    }
  }

  protected List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(
      List<Long> ids,
      final long idProcedimentoOggetto, final long idBando)
      throws InternalUnexpectedException
  {
    return interventiEJB
        .getInfoInterventiPerModifica(idProcedimentoOggetto, ids, idBando);
  }
}