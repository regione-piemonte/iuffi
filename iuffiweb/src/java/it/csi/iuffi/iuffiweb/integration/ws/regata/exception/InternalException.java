package it.csi.iuffi.iuffiweb.integration.ws.regata.exception;

import it.csi.csi.wrapper.UserException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class InternalException 
  extends UserException
{
  private static final long serialVersionUID = 368031714889615637L;
  public static final int ERROR_CODE_UNKNOWN = 1;
  public static final int ERROR_CODE_DATABASE = 2;
  public static final int ERROR_CODE_SYSTEM = 3;
  public static final int ERROR_CODE_EXTERNAL_SERVICE = 4;
  public static final int ERROR_CODE_QUERY_TIMEOUT = 4097;
  public static final int ERROR_CODE_INVALID_PARAMETER = 8193;
  public static final int ERROR_CODE_DATA_NOT_FOUND = 16385;
  public static final int ERROR_CODE_USER_NOT_ALLOWED = 12289;
  public static final int ERROR_CODE_CONFIGURATION = 16385;
  public static final int ERROR_CODE_REST_CLIENT_UNKNOWN = -1879048192;
  public static final int ERROR_CODE_REST_CLIENT_DESERIALIZATION = -1879048191;
  public static final int ERROR_CODE_REST_CLIENT_NET = -1879048190;
  public static final String ERROR_MESSAGE_GENERIC = "Si è verificata una eccezione interna. Codice di errore: ";
  private int errorCode;
  private String exceptionType;
  
  public InternalException()
  {
    super("Si è verificata una eccezione interna. Codice di errore: ");
    this.errorCode = 1;
    this.exceptionType = getClass().getSimpleName();
  }
  
  @JsonCreator
  public InternalException(@JsonProperty("message") String paramString, @JsonProperty("errorCode") int paramInt)
  {
    super(paramString);
    this.errorCode = paramInt;
    this.exceptionType = getClass().getName();
  }
  
  public InternalException(int paramInt)
  {
    super("Si è verificata una eccezione interna. Codice di errore: " + paramInt);
    this.errorCode = paramInt;
    this.exceptionType = getClass().getSimpleName();
  }
  
  public int getErrorCode()
  {
    return this.errorCode;
  }
  
  
  public String getExceptionType()
  {
    return this.exceptionType;
  }
  
 
  public void setExceptionType(String paramString)
  {
    this.exceptionType = paramString;
  }
}

