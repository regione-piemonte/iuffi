package it.csi.iuffi.iuffiweb.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;


public class ErrorResponse {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private String message;

  List<Object> errors = new ArrayList<>();


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }



  public List<Object> getErrors() {
    return errors;
  }

  public void setErrors(List<Object> errors) {
    this.errors = errors;
  }

  public void addError(String field, String message) {
    Map<String,String> keyValue = new HashMap<>();
    keyValue.put(field, message);
    errors.add(keyValue);
  }

  @Override
  public String toString() {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "ErrorResponse []";
    }
    catch (IOException e)
    {
      return "ErrorResponse []";
    }


  }




}