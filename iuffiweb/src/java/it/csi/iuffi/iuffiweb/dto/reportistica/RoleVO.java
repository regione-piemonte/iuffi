package it.csi.iuffi.iuffiweb.dto.reportistica;

import java.io.Serializable;

public class RoleVO implements Serializable
{
  private static final long  serialVersionUID = 9104726695231817976L;

  public static final String TYPE_INTERNAL    = "interval";
  public static final String TYPE_ANNOTATION  = "annotation";

  private String             role;

  public String getRole()
  {
    return role;
  }

  public void setRole(String role)
  {
    this.role = role;
  }

}
