package it.csi.iuffi.iuffiweb.exception;

public class ApplicationException extends LocalException
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1864038758131826585L;

  public ApplicationException(String message)
  {
    super(message);
  }

  public ApplicationException(String message, int errorCode)
  {
    super(message, errorCode);
  }

  public ApplicationException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public ApplicationException(String message, int errorCode, Throwable cause)
  {
    super(message, errorCode, cause);
  }
}
