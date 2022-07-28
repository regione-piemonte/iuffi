package it.csi.iuffi.iuffiweb.exception;

public class IuffiAssertionException extends InternalUnexpectedException
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1864038758131826585L;

  public IuffiAssertionException(String message)
  {
    super(message, (Throwable) null);
  }

  public IuffiAssertionException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
