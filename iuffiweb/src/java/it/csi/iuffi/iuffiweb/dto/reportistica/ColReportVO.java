package it.csi.iuffi.iuffiweb.dto.reportistica;

import java.io.Serializable;
import java.util.HashMap;

public class ColReportVO implements Serializable
{
  private static final long  serialVersionUID = 9104726695231817976L;

  public static final String TYPE_BOOLEAN     = "boolean";
  public static final String TYPE_NUMBER      = "number";
  public static final String TYPE_STRING      = "string";
  public static final String TYPE_DATE        = "date";
  public static final String TYPE_DATETIME    = "datetime";
  public static final String TYPE_TIMEOFDAY   = "timeofday";

  private String             type;
  private String             id;
  private String             label;
  private String             pattern;
  private Object             p;

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }

  public Object getP()
  {
    return p;
  }

  public void setP(HashMap<String, Object> p)
  {
    this.p = p;
  }

  public void setP(Object p, boolean isAnnotationType)
  {
    if (!isAnnotationType)
    {
      this.p = p;
    }
    else
    {
      RoleVO role = new RoleVO();
      role.setRole(RoleVO.TYPE_ANNOTATION);
      this.p = role;
    }
  }

}
