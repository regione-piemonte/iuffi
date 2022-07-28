package it.csi.iuffi.iuffiweb.presentation.stampeoggetto;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-128", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI128StampaPratica extends StampaController
{
  @RequestMapping(value = "/cuiuffi128/stampa")
  public ResponseEntity<byte[]> stampa(HttpSession session) throws Exception
  {
    return stampaByCUName(getIdProcedimentoOggetto(session),
        CUIUFFI128StampaPratica.class.getAnnotation(IuffiSecurity.class)
            .value());
  }
}
