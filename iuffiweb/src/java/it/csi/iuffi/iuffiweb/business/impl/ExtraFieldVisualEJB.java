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

import it.csi.iuffi.iuffiweb.business.IExtraFieldVisualEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldVisualDTO;
import it.csi.iuffi.iuffiweb.service.ExtraFieldVisualDAO;

@Stateless()
@EJB(name = "java:app/ExtraFieldVisual", beanInterface = IExtraFieldVisualEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ExtraFieldVisualEJB extends IuffiAbstractEJB<ExtraFieldVisualDAO> implements IExtraFieldVisualEJB
{

  @Override
  public ExtraFieldVisualDTO insert(ExtraFieldVisualDTO extraField)
      throws InternalUnexpectedException
  {
    return dao.insert(extraField);
  }

  @Override
  public void update(ExtraFieldVisualDTO extraField)
      throws InternalUnexpectedException
  {
    dao.update(extraField);
  }

  @Override
  public List<ExtraFieldVisualDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<ExtraFieldVisualDTO> findByFilter(ExtraFieldVisualDTO extraField)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(extraField);
  }

  @Override
  public ExtraFieldVisualDTO findById(Integer idExtraField)
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