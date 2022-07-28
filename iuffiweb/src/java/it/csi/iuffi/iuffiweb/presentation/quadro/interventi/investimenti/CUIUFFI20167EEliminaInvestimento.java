package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.investimenti;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Elimina;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI20-167-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuIUFFI20167e")
public class CUIUFFI20167EEliminaInvestimento extends Elimina
{
}