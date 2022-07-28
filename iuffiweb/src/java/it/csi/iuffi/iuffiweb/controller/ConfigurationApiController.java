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
import org.springframework.web.bind.annotation.PathVariable;
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
public class ConfigurationApiController extends BaseController {

  //	private static final Logger log = LoggerFactory.getLogger(ConfigurationApiController.class);

/*
  private final ObjectMapper objectMapper;

  private final HttpServletRequest request;
*/

  ResourceBundle res = ResourceBundle.getBundle("config");

/*
  @Autowired
  public ConfigurationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
    this.objectMapper = objectMapper;
    this.request = request;
  }
*/

  @RequestMapping(value = "/config/{configFileName}.json", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<String> list(@PathVariable("configFileName") String configFileName, HttpServletRequest request) {

    try {
      ServletContext servletContext = request.getSession().getServletContext();
      String root = servletContext.getRealPath("/");
      
      if (!configFileName.endsWith(".json")) {
          configFileName+=".json";
      }
      String configFile = root + res.getString("conf.path") + File.separator + res.getString("application.env") + File.separator + configFileName;

      File file = new File(configFile);

      if(!file.exists()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      String configFileAsString = getFileAsString(file);
      return new ResponseEntity<>(configFileAsString,HttpStatus.OK);
    }
    catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
