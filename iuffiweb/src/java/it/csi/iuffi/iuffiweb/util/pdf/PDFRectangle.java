package it.csi.iuffi.iuffiweb.util.pdf;

import java.awt.geom.Point2D;

public class PDFRectangle implements IValuableElement
{
  protected Point2D p0;
  protected Point2D p1;
  protected Point2D p2;
  protected Point2D p3;
  protected int     page;

  public PDFRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3, int page)
  {
    this.p0 = p0;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
    this.page = page;
  }

  public Point2D getP0()
  {
    return p0;
  }

  public void setP0(Point2D p0)
  {
    this.p0 = p0;
  }

  public Point2D getP1()
  {
    return p1;
  }

  public void setP1(Point2D p1)
  {
    this.p1 = p1;
  }

  public Point2D getP2()
  {
    return p2;
  }

  public void setP2(Point2D p2)
  {
    this.p2 = p2;
  }

  public Point2D getP3()
  {
    return p3;
  }

  public void setP3(Point2D p3)
  {
    this.p3 = p3;
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
    return ValuableElementType.RECTANGLE;
  }
}
