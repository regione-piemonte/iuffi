package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.gis;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRendicontazioneEAccertamentoSpeseEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datafinelavori.DataFineLavoriDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-260", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi260")
public class CUIUFFI260ImportazioniSuperficiGis extends BaseController
{
  private static final String                   JSP_BASE_PATH = "interventi/accertamentospese/saldo/gis/";

  @Autowired
  private IRendicontazioneEAccertamentoSpeseEJB rendicontazioneEAccertamentoSpeseEJB;
  @Autowired
  private IQuadroEJB                            quadroEjb;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Model model)
  {
    return JSP_BASE_PATH + "importaGis";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String importaSuperficiGIS(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    try
    {
      final LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = this
          .getLogOperationOggettoQuadroDTO(session);
      long idProcedimentoOggettoCorrente = logOperationOggettoQuadroDTO
          .getIdProcedimentoOggetto();
      Date dataFineLavori = null;
      List<DataFineLavoriDTO> list = quadroEjb.getElencoDateFineLavori(
          getProcedimentoFromSession(session).getIdProcedimento(),
          getIdProcedimentoOggetto(session));
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
      if (dataFineLavori == null)
      {
        throw new ApplicationException(
            "Impossibile effettuare l'importazione in quanto non è stata indicata una data di fine lavori ");
      }

      int anno = IuffiUtils.DATE.getYearFromDate(dataFineLavori);
      Date dataLimite = IuffiUtils.DATE.parseDate("11/11/" + anno);
      if (dataFineLavori.compareTo(dataLimite) > 0)
      {
        anno++;
      }
      rendicontazioneEAccertamentoSpeseEJB
          .importaSuperficiGIS(logOperationOggettoQuadroDTO, anno);
    }
    catch (ApplicationException e)
    {
      model.addAttribute("errorMessage", e.getMessage());
    }
    return JSP_BASE_PATH + "risultatoImportazione";
  }
}