package it.csi.iuffi.iuffiweb.controller;

import java.io.File;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;



@Controller
@IuffiSecurity(value = "N/A", controllo = IuffiSecurity.Controllo.NESSUNO)
@NoLoginRequired
public class ComuniApiController extends BaseController {


  ResourceBundle res = ResourceBundle.getBundle("config");


  @RequestMapping(value = "/rest/comuni", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getComuni(HttpServletRequest request) {

    try {
      
      String fileComuni = "comuni.json";
      
      ServletContext servletContext = request.getSession().getServletContext();
      String root = servletContext.getRealPath("/");
      
      String comuniFile = root + res.getString("conf.path") + File.separator + fileComuni;

      File file = new File(comuniFile);

      if (!file.exists()) {
        logger.info("File dei comuni non trovato. File: " + comuniFile);
        comuniFile = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + fileComuni;
        file = new File(comuniFile);
      
        logger.info("Cerco in " + comuniFile);
        
        if (!file.exists()) {
          logger.info("File dei comuni non trovato. File: " + comuniFile);
          ErrorResponse err = new ErrorResponse();
          err.addError("Errore", "File dei comuni non trovato. File: " + comuniFile);
          err.setMessage("Errore: file comuni.json non trovato");
          return new ResponseEntity<ErrorResponse>(err, HttpStatus.NOT_FOUND);
        }
      }

      String configFileAsString = getFileAsString(file);
      return new ResponseEntity<>(configFileAsString,HttpStatus.OK);
    }
    catch (Exception e) {
      logger.error("Errore nel metodo getComuni: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo getComuni: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MethodArgumentNotValidException exception) {

    ErrorResponse err = new ErrorResponse();
/*
    exception.getBindingResult().getFieldErrors().stream().forEach(x->{
      err.addError(x.getField(), x.getDefaultMessage());
    });
*/
    err.setMessage("Error validating "+exception.getBindingResult().getObjectName());
    return err;
  }

}
