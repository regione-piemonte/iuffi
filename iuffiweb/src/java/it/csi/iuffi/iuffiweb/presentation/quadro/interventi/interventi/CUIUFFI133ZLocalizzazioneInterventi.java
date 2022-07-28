package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Localizza;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-133-Z", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi133z")
public class CUIUFFI133ZLocalizzazioneInterventi extends Localizza
{
  @Override
  public String getFlagEscludiCatalogo()
  {
    return IuffiConstants.FLAGS.NO;
  }

}
