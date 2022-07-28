package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Dettaglio;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-133-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi133v")
public class CUIUFFI133vDettaglioIntervento extends Dettaglio
{
	@Override
	public String getFlagEscludiCatalogo()
	{
		return IuffiConstants.FLAGS.NO;
	}

}
