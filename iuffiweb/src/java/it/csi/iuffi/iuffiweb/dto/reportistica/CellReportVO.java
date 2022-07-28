package it.csi.iuffi.iuffiweb.dto.reportistica;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class CellReportVO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private Object            v;
  private String            f;
  private Object            p;

  public Object getValueFormatted()
  {
    if (v instanceof BigDecimal)
    {
      BigDecimal tmp = (BigDecimal) v;
      if (v.toString().indexOf("106") >= 0)
      {
        return IuffiUtils.FORMAT.formatGenericNumber(tmp, tmp.scale(),
            tmp.scale() > 0) + " AAAA";
      }
      return IuffiUtils.FORMAT.formatGenericNumber(tmp, tmp.scale(),
          tmp.scale() > 0);
    }

    return v;
  }

  public Object getV()
  {
    return v;
  }

  public void setV(Object v)
  {
    this.v = v;
  }

  public String getF()
  {
    return f;
  }

  public void setF(String f)
  {
    this.f = f;
  }

  public Object getP()
  {
    return p;
  }

  public void setP(Object p)
  {
    this.p = p;
  }

}
