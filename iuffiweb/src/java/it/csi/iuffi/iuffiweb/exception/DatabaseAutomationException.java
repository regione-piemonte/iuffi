package it.csi.iuffi.iuffiweb.exception;

public class DatabaseAutomationException extends InternalException
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3608694561455495807L;

  public DatabaseAutomationException(String message)
  {
    super(message);
  }

  public DatabaseAutomationException(String message, int errorCode)
  {
    super(message, errorCode);
  }

  public DatabaseAutomationException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public DatabaseAutomationException(String message, int errorCode,
      Throwable cause)
  {
    super(message, errorCode, cause);
  }

}
