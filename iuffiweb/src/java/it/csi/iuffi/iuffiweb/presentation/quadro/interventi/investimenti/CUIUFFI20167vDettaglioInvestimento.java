package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.investimenti;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Dettaglio;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI20-167-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuIUFFI20167v")
public class CUIUFFI20167vDettaglioInvestimento extends Dettaglio
{
  @Override
  public String getFlagEscludiCatalogo()
  {
    return IuffiConstants.FLAGS.SI;
  }


}
