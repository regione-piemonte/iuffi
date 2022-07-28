package it.csi.iuffi.iuffiweb.exception;

public class InternalException extends LocalException
{
  /** serialVersionUID */
  private static final long  serialVersionUID  = 1864038758131826585L;
  public static final String MSG_GENERIC_ERROR = "Si è verificato un errore interno";

  public InternalException(String message)
  {
    super(message);
  }

  public InternalException(String message, int errorCode)
  {
    super(message, errorCode);
  }

  public InternalException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public InternalException(String message, int errorCode, Throwable cause)
  {
    super(message, errorCode, cause);
  }
}
