package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.accertamentospese.saldo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datafinelavori.DataFineLavoriDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.TotaleContributoAccertamentoElencoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-212-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi212l")
public class CUIUFFI212LAccertamentoSpeseSaldoElenco
    extends CUIUFFI212AccertamentoSpeseSaldoAbstract
{
  @Autowired
  private IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    final List<TotaleContributoAccertamentoElencoDTO> contributi = rendicontazioneEAccertamentoSpeseEJB
        .getTotaleContributoErogabileNonErogabileESanzioniAcconto(
            getIdProcedimentoOggetto(session));
    final boolean isMisura81 = quadroEJB.isCodiceLivelloInvestimentoEsistente(
        getProcedimentoFromSession(session).getIdProcedimento(),
        IuffiConstants.LIVELLO.CODICE_LIVELLO_8_1_1);
    model.addAttribute("isMisura81", isMisura81);
    model.addAttribute("contributi", contributi);

    if (isMisura81)
    {
      long idProcedimentoOggettoCorrente = getIdProcedimentoOggetto(session);
      Date dataFineLavori = null;
      List<DataFineLavoriDTO> list = quadroEJB.getElencoDateFineLavori(
          getProcedimentoFromSession(session).getIdProcedimento(),
          idProcedimentoOggettoCorrente);
      if (list != null)
      {
        for (DataFineLavoriDTO dfl : list)
        {
          long idProcedimentoOggetto = dfl.getIdProcedimentoOggetto();
          if (idProcedimentoOggettoCorrente == idProcedimentoOggetto)
          {
            dataFineLavori = dfl.getDataFineLavori();
            break;
          }
        }
      }
      model.addAttribute("warningImportaGIS",
          Boolean.valueOf(dataFineLavori == null));
    }
    addInfoRendicontazioneIVA(model,
        getIdProcedimentoOggetto(request.getSession()));
    return JSP_BASE_PATH + "elenco";
  }

  @RequestMapping(value = "/json/elenco", produces = "application/json")
  @ResponseBody
  public List<RigaAccertamentoSpese> elenco_json(Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<RigaAccertamentoSpese> elenco = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(getIdProcedimentoOggetto(session), null);
    return elenco;
  }
}
