package it.csi.iuffi.iuffiweb.util.pdf;

public class PDFText implements IValuableElement
{
  protected String text;

  public PDFText(String text)
  {
    this.text = text;
  }

  @Override
  public ValuableElementType getType()
  {
    return ValuableElementType.TEXT;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }
}
