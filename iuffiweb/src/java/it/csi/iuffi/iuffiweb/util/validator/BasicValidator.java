package it.csi.iuffi.iuffiweb.util.validator;

import java.util.HashMap;

public class BasicValidator extends HashMap<String, String>
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3770166489356520835L;
  private String            lastError;

  public void addError(String fieldName, String errorMessage)
  {
    put(fieldName, errorMessage);
    lastError = errorMessage;
  }

  public String removeError(String fieldName)
  {
    return remove(fieldName);
  }

  public String getLastError()
  {
    return lastError;
  }

  public void setLastError(String lastError)
  {
    this.lastError = lastError;
  }
}
