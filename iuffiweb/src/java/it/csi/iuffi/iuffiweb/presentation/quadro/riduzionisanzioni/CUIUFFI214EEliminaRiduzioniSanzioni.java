package it.csi.iuffi.iuffiweb.presentation.quadro.riduzionisanzioni;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.RiduzioniSanzioniDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-214-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi214e")
public class CUIUFFI214EEliminaRiduzioniSanzioni extends BaseController
{

  @Autowired
  IQuadroEJB quadroEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idProcOggSanzione}", method = RequestMethod.GET)
  public String confermaElilmina(
      @PathVariable("idProcOggSanzione") long idProcOggSanzione, Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<Long> ids = new LinkedList<>();
    ids.add(idProcOggSanzione);
    model.addAttribute("ids", ids);

    return "riduzionisanzioni/confermaEliminaRiduzioneSanzione";
  }

  @IsPopup
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String confermaElimina(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<Long> ids = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idProcOggSanzione"));
    model.addAttribute("ids", ids);
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);

    // controllo: se sono stati selezionate sanzioni automatiche, queste non
    // saranno eliminabili e lo segnalo.
    List<RiduzioniSanzioniDTO> riduzioni = quadroEJB.getElencoRiduzioniSanzioni(
        procedimentoOggetto.getIdProcedimentoOggetto());
    for (RiduzioniSanzioniDTO r : riduzioni)
    {
      if (r.getIdTipologia() == 3)
        if (ids.contains(r.getIdProcOggSanzione()))
          model.addAttribute("msgErrore",
              "Sono state selezionate delle sanzioni automatiche. Queste non verranno eliminate, neanche nel caso in cui sia già stato effettuato lo split!");
    }
    return "riduzionisanzioni/confermaEliminaRiduzioneSanzione";
  }

  @IsPopup
  @RequestMapping(value = "/elimina_riduzioni_sanzioni", method = RequestMethod.POST)
  public String elimina(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<Long> ids = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idProcOggSanzione"));

    List<Long> idsEliminabili = new ArrayList<>(); // aggiungo solo gli id di
                                                   // quelli selezionati ma con
                                                   // idTipologia!=3 (non
                                                   // automatici)
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    List<RiduzioniSanzioniDTO> riduzioni = quadroEJB.getElencoRiduzioniSanzioni(
        procedimentoOggetto.getIdProcedimentoOggetto());
    for (RiduzioniSanzioniDTO r : riduzioni)
    {
      if (r.getIdTipologia() != 3)
        if (ids.contains(r.getIdProcOggSanzione()))
          idsEliminabili.add(r.getIdProcOggSanzione());
    }

    // Se IUF_D_OGGETTO.TIPO_PAGAMENTO_SIGOP not in (‘ACCON’, ‘SALDO’) --
    // Errore
    String tipoPagamentoSigop = quadroEJB.getTipoPagamentoSigopOggetto(
        procedimentoOggetto.getIdProcedimentoOggetto());
    if (tipoPagamentoSigop == null
        || (tipoPagamentoSigop.compareTo("ACCON") != 0
            && tipoPagamentoSigop.compareTo("SALDO") != 0))
    {
      return "dialog/error";
    }
    else
      quadroEJB.eliminaRiduzioneSanzione(idsEliminabili,
          getLogOperationOggettoQuadroDTO(request.getSession()),
          procedimentoOggetto.getIdProcedimentoOggetto(), tipoPagamentoSigop);

    return "dialog/success";
  }

}
