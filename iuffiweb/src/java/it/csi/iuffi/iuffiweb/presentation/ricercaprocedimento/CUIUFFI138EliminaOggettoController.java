package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi138")
@IuffiSecurity(value = "CU-IUFFI-138", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI138EliminaOggettoController extends BaseController
{

  @Autowired
  private IQuadroEJB  quadroEJB  = null;
  @Autowired
  private IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "popupindex_{paginaPadre}_{idProcedimento}_{idProcedimentoOggetto}_{idBandoOggetto}", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session,
      @PathVariable("paginaPadre") String paginaPadre,
      @PathVariable("idProcedimento") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimento,
      @PathVariable("idProcedimentoOggetto") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimentoOggetto,
      @PathVariable("idBandoOggetto") @NumberFormat(style = NumberFormat.Style.NUMBER) long idBandoOggetto)
      throws InternalUnexpectedException
  {
    setModelDialogWarning(model,
        "Stai cercando di eliminare l'oggetto selezionata, vuoi continuare ?",
        "../cuiuffi138/popupindex_" + paginaPadre + "_" + idProcedimento + "_"
            + idProcedimentoOggetto + "_" + idBandoOggetto + ".do");
    return "dialog/conferma";
  }

  @RequestMapping(value = "popupindex_{paginaPadre}_{idProcedimento}_{idProcedimentoOggetto}_{idBandoOggetto}", method = RequestMethod.POST)
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request,
      @PathVariable("paginaPadre") String paginaPadre,
      @PathVariable("idProcedimento") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimento,
      @PathVariable("idProcedimentoOggetto") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimentoOggetto,
      @PathVariable("idBandoOggetto") @NumberFormat(style = NumberFormat.Style.NUMBER) long idBandoOggetto)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    MainControlloDTO controlliGravi = quadroEJB.callMainEliminazione(
        idProcedimentoOggetto, utenteAbilitazioni.getIdUtenteLogin());

    if (controlliGravi
        .getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO)
    {
      model.addAttribute("messaggio",
          IuffiConstants.SQL.MESSAGE.PLSQL_ERRORE_CRITICO.replace("&egrave;",
              "è") + " " + controlliGravi.getMessaggio());
      model.addAttribute("titolo", "Errore");
      return "errore/erroreInterno";
    }
    else
      if (controlliGravi
          .getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE)
      {
        model.addAttribute("messaggio",
            IuffiConstants.SQL.MESSAGE.PLSQL_ERRORE_GRAVE.replace("&egrave;",
                "è") + " " + controlliGravi.getMessaggio());
        model.addAttribute("titolo", "Errore");
        return "errore/erroreInterno";
      }
      else
      {
        if (paginaPadre.equals("E"))
        {
          // se arrivo dalla pagina di elenco oggetti ricarico la pagina se
          // esiste ancora il procedimento, altrimenti torno ai risultati di
          // ricerca
          List<GruppoOggettoDTO> listGruppiOggetto = ricercaEJB
              .getElencoOggetti(idProcedimento,
                  Arrays.asList(utenteAbilitazioni.getMacroCU()),
                  IuffiUtils.PAPUASERV
                      .isAttoreBeneficiarioOCAA(utenteAbilitazioni), utenteAbilitazioni.getIdProcedimento());
          if (listGruppiOggetto != null && listGruppiOggetto.size() > 0)
          {
            return "redirect:../cuiuffi129/index_" + idProcedimento + ".do";
          }
          else
          {
            return "redirect:../ricercaprocedimento/restoreElencoProcedimenti.do";
          }
        }
        else
        {
          // Se arrivo dalla pagina di elenco procedimenti ricarico la pagina
          return "redirect:../nuovoprocedimento/ricercaBandoMultipla.do";
        }
      }
  }

}
