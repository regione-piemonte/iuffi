package it.csi.iuffi.iuffiweb.business.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IUtilServiceEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.service.UtilServiceDAO;

@Stateless()
@EJB(name = "java:app/UtilService", beanInterface = IUtilServiceEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UtilServiceEJB extends IuffiAbstractEJB<UtilServiceDAO> implements IUtilServiceEJB
{

  @Override
  public String getLastChangeTimestamp() throws InternalUnexpectedException
  {
    return dao.getLastChange();
  }

  @Override
  public String getTableByConstraint(String constraintName) throws InternalUnexpectedException
  {
    return dao.getTableByConstraint(constraintName);
  }

  @Override
  public String getTableCommentsByTableName(String tableName) throws InternalUnexpectedException
  {
    return dao.getTableCommentsByTableName(tableName);
  }

}