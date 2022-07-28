package it.csi.iuffi.iuffiweb.exception;

public class InternalServiceException extends InternalException
{
  /** serialVersionUID */
  private static final long serialVersionUID        = 1532546188855624809L;
  public static final int   ERRORE_ACCESSO_SERVIZIO = 0x01000000;

  public InternalServiceException(String message)
  {
    super(message, ERRORE_ACCESSO_SERVIZIO);
  }

  public InternalServiceException(String message, int errorCode)
  {
    super(message, errorCode);
  }

  public InternalServiceException(String message, Throwable cause)
  {
    super(message, ERRORE_ACCESSO_SERVIZIO, cause);
  }

  public InternalServiceException(String message, int errorCode,
      Throwable cause)
  {
    super(message, errorCode, cause);
  }
}
