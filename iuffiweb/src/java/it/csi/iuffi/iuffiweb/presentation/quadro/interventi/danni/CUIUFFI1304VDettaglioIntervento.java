package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.danni;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Dettaglio;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-1304-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi1304v")
public class CUIUFFI1304VDettaglioIntervento extends Dettaglio
{
	@Override
	public String getFlagEscludiCatalogo()
	{
		return IuffiConstants.FLAGS.NO;
	}
}
