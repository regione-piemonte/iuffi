package it.csi.iuffi.iuffiweb.presentation.stampeoggetto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;

@Controller
@IuffiSecurity(value = "CU-IUFFI-128", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public abstract class StampaController extends BaseController
{
  protected ResponseEntity<byte[]> stampaByCUName(long idProcedimentoOggetto,
      String cuName) throws Exception
  {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/pdf");
    Stampa stampa = IuffiUtils.STAMPA.getStampaFromCdU(cuName);
    byte[] contenuto = stampa.findStampaFinale(idProcedimentoOggetto, cuName)
        .genera(idProcedimentoOggetto, cuName);
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + stampa.getDefaultFileName(idProcedimentoOggetto) + "\"");

    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contenuto,
        httpHeaders, HttpStatus.OK);
    return response;
  }
}
