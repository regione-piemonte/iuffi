package it.csi.iuffi.iuffiweb.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IExtraFieldTrapEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldTrapDTO;
import it.csi.iuffi.iuffiweb.service.ExtraFieldTrapDAO;

@Stateless()
@EJB(name = "java:app/ExtraFieldTrap", beanInterface = IExtraFieldTrapEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ExtraFieldTrapEJB extends IuffiAbstractEJB<ExtraFieldTrapDAO> implements IExtraFieldTrapEJB
{

  @Override
  public ExtraFieldTrapDTO insert(ExtraFieldTrapDTO extraField)
      throws InternalUnexpectedException
  {
    return dao.insert(extraField);
  }

  @Override
  public void update(ExtraFieldTrapDTO extraField)
      throws InternalUnexpectedException
  {
    dao.update(extraField);
  }

  @Override
  public List<ExtraFieldTrapDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<ExtraFieldTrapDTO> findByFilter(ExtraFieldTrapDTO extraField)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(extraField);
  }

  @Override
  public ExtraFieldTrapDTO findById(Integer idExtraField)
      throws InternalUnexpectedException
  {
    return dao.findById(idExtraField);
  }

  @Override
  public void remove(Integer idExtraField) throws InternalUnexpectedException
  {
    dao.remove(idExtraField);
  }


}