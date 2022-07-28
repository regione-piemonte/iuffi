package it.csi.iuffi.iuffiweb.exception;

public class InterceptorException extends Exception
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8496768436610256388L;

  public InterceptorException(String message)
  {
    super(message);
  }

  public InterceptorException(Throwable cause)
  {
    super(cause);
  }

  public InterceptorException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
