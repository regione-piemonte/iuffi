package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Elimina;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-133-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi133e")
public class CUIUFFI133EEliminaIntervento extends Elimina
{
}