package it.csi.iuffi.iuffiweb.business;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public interface IUtilServiceEJB extends IIuffiAbstractEJB
{
 
  public String getLastChangeTimestamp() throws InternalUnexpectedException;

  public String getTableByConstraint(String constraintName) throws InternalUnexpectedException;

  public String getTableCommentsByTableName(String tableName) throws InternalUnexpectedException;

}
