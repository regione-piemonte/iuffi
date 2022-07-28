package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.investimenti;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Elenco;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.AbilitazioneAzioneTag;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI20-167-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuIUFFI20167l")
public class CUIUFFI20167lElencoInvestimenti extends Elenco
{
	  @Autowired
	  protected IQuadroEJB quadroEJB;
	
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
	ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromRequest(request);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU("CU-IUFFI20-167-L");
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));

	return super.elenco(model, request);
  }

  @Override
  public String getCodiceQuadro()
  {
    return IuffiConstants.QUADRO.CODICE.INVESTIMENTI;
  }

  @Override
  public String getFlagEscludiCatalogo()
  {
    return IuffiConstants.FLAGS.SI;
  }

  @Override
  protected boolean isBandoConPercentualeRiduzione(long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return false;
  }

  @Override
  protected void addExtraAttributeToModel(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    boolean isAbilita = AbilitazioneAzioneTag.validate(IuffiConstants.QUADRO.CODICE.INVESTIMENTI, IuffiConstants.AZIONE.CODICE.ABILITA,
        getProcedimentoOggettoFromSession(session), getUtenteAbilitazioni(session), request);    
    model.addAttribute("isAbilita",Boolean.valueOf(isAbilita));
    model.addAttribute("hasflagAssociatoAltraMisura", Boolean.TRUE);
  }

	@Override
	protected void isInterventoWithDanni(Model model)
	{
		model.addAttribute("withDanni", Boolean.FALSE);
	}
}