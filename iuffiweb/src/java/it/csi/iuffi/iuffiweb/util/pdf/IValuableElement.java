package it.csi.iuffi.iuffiweb.util.pdf;

public interface IValuableElement
{
  public enum ValuableElementType
  {
    TEXT, RECTANGLE, CURVED_RECTANGLE;
  }

  public ValuableElementType getType();
}
