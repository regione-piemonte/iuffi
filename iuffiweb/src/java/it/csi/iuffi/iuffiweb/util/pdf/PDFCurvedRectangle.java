package it.csi.iuffi.iuffiweb.util.pdf;

public class PDFCurvedRectangle implements IValuableElement
{

  protected int   page;
  protected float x1;
  protected float y1;
  protected float x2;
  protected float y2;
  protected float x3;
  protected float y3;

  public PDFCurvedRectangle(float x1, float y1, float x2, float y2, float x3,
      float y3, int page)
  {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.x3 = x3;
    this.y3 = y3;

    this.page = page;
  }

  public float getX1()
  {
    return x1;
  }

  public void setX1(float x1)
  {
    this.x1 = x1;
  }

  public float getY1()
  {
    return y1;
  }

  public void setY1(float y1)
  {
    this.y1 = y1;
  }

  public float getX2()
  {
    return x2;
  }

  public void setX2(float x2)
  {
    this.x2 = x2;
  }

  public float getY2()
  {
    return y2;
  }

  public void setY2(float y2)
  {
    this.y2 = y2;
  }

  public float getX3()
  {
    return x3;
  }

  public void setX3(float x3)
  {
    this.x3 = x3;
  }

  public float getY3()
  {
    return y3;
  }

  public void setY3(float y3)
  {
    this.y3 = y3;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage(int page)
  {
    this.page = page;
  }

  @Override
  public ValuableElementType getType()
  {
    return ValuableElementType.CURVED_RECTANGLE;
  }
}
