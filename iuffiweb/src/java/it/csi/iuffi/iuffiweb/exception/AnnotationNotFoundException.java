package it.csi.iuffi.iuffiweb.exception;

public class AnnotationNotFoundException extends InternalException
{
  /** serialVersionUID */
  private static final long serialVersionUID = 3876194643443964517L;

  public AnnotationNotFoundException(String message)
  {
    super(message);
  }

  public AnnotationNotFoundException(String message, int errorCode)
  {
    super(message, errorCode);
  }

  public AnnotationNotFoundException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public AnnotationNotFoundException(String message, int errorCode,
      Throwable cause)
  {
    super(message, errorCode, cause);
  }

}
