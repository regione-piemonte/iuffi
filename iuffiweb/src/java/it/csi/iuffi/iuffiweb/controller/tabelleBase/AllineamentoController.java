package it.csi.iuffi.iuffiweb.controller.tabelleBase;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IUtilServiceEJB;
import it.csi.iuffi.iuffiweb.model.Allineamento;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;

@Controller
@RequestMapping(value = "/rest")
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class AllineamentoController
{
 
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Autowired
  private IUtilServiceEJB utilServiceEJB;
  

  @RequestMapping(value = "/last-change-timestamp", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<?> getLastChangeTimestamp()
  {
    String lastChange = null;
    try
    {
      logger.info("getLastChangeTimestamp deploy eseguito correttamente");
      lastChange = utilServiceEJB.getLastChangeTimestamp();
    } catch (Exception e) {
        logger.debug("Errore nel metodo getLastChangeTimestamp: " + e.getMessage());
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", e.getMessage());
        err.setMessage("Errore nel metodo getLastChangeTimestamp: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Allineamento>(new Allineamento(lastChange), HttpStatus.OK);
  }


}
