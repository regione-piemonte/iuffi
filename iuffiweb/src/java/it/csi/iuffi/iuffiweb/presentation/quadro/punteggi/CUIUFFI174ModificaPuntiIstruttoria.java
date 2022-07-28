package it.csi.iuffi.iuffiweb.presentation.quadro.punteggi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.punteggi.CriterioVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.punteggi.RaggruppamentoLivelloCriterio;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-174", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi174")
public class CUIUFFI174ModificaPuntiIstruttoria extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;
    

  @RequestMapping("/index")
  public final String index(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    List<RaggruppamentoLivelloCriterio> listaRaggruppamento = null;
    final ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    final QuadroOggettoDTO quadroOgg = po.findQuadroByCodiceQuadro("PUNTI");
    final Long idDatiProcedimentoPunti = this.quadroEjb
        .getIdDatiProcedimentoPunti(po.getIdProcedimentoOggetto());
    if (idDatiProcedimentoPunti != null)
    {
      listaRaggruppamento = this.quadroEjb.getCriteriPunteggio(
          quadroOgg.getIdOggetto(), idDatiProcedimentoPunti,
          getProcedimentoFromSession(session).getIdBando());
      model.addAttribute("listaRaggruppamento", listaRaggruppamento);
    }
    return "punteggi/modificaPunteggiIstruttoria";
  }

	@RequestMapping("/modifica")
	public final String modifica(Model model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException, ApplicationException
	{
		model.addAttribute("prefReqValues", Boolean.TRUE);
		Errors errors = new Errors();

		List<RaggruppamentoLivelloCriterio> listaRaggruppamento = null;
		final long idProcedimento = getIdProcedimento(request.getSession());
		final ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
		final QuadroOggettoDTO quadroOgg = po.findQuadroByCodiceQuadro("PUNTI");
		final Long idDatiProcedimentoPunti = this.quadroEjb.getIdDatiProcedimentoPunti(po.getIdProcedimentoOggetto());
		Map<Long, BigDecimal> mappaValoriInput = new HashMap<Long, BigDecimal>();
		if (idDatiProcedimentoPunti != null)
		{
			listaRaggruppamento = this.quadroEjb.getCriteriPunteggio(quadroOgg.getIdOggetto(), idDatiProcedimentoPunti,
					getProcedimentoFromSession(session).getIdBando());
		}

		if (listaRaggruppamento != null)
		{
			String punteggioIstr = "";
			for (RaggruppamentoLivelloCriterio raggr : listaRaggruppamento)
			{
				for (CriterioVO criterio : raggr.getCriteri())
				{
					punteggioIstr = request
							.getParameter("puntPerIdBandoLivCrit_" + criterio.getIdBandoLivelloCriterio());
					if (!GenericValidator.isBlankOrNull(punteggioIstr))
					{
						BigDecimal bdPunteggioIstr = IuffiUtils.NUMBERS.getBigDecimal(punteggioIstr);

						if (bdPunteggioIstr != null && BigDecimal.ZERO.compareTo(bdPunteggioIstr) == 0)
						{
							mappaValoriInput.put(criterio.getIdBandoLivelloCriterio(), bdPunteggioIstr);
						} else
						{
							mappaValoriInput.put(criterio.getIdBandoLivelloCriterio(),
									errors.validateMandatoryBigDecimalInRange(punteggioIstr,
											"puntPerIdBandoLivCrit_" + criterio.getIdBandoLivelloCriterio(), 2,
											criterio.getPunteggioMin(), criterio.getPunteggioMax()));
						}
					}
				}
			}
		}

		if (!errors.isEmpty())
		{
			model.addAttribute("errors", errors);
			return index(model, request, session);
		} else
		{
			final LogOperationOggettoQuadroDTO logOperationOggettoQuadro = getLogOperationOggettoQuadroDTO(
					request.getSession());
			long idProcedimentoOggetto = getIdProcedimentoOggetto(session); 
			
			
			this.quadroEjb.updateCriteriIstruttoria(quadroOgg.getIdOggetto(), logOperationOggettoQuadro,
					mappaValoriInput, idProcedimento, 
					getProcedimentoFromSession(session).getIdBando(),
					idDatiProcedimentoPunti, idProcedimentoOggetto
					);
		}
		return "redirect:../cuiuffi160/index.do";
	}
}
