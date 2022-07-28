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

import it.csi.iuffi.iuffiweb.business.IExtraFieldCampEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldCampDTO;
import it.csi.iuffi.iuffiweb.service.ExtraFieldCampDAO;

@Stateless()
@EJB(name = "java:app/ExtraFieldCamp", beanInterface = IExtraFieldCampEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ExtraFieldCampEJB extends IuffiAbstractEJB<ExtraFieldCampDAO> implements IExtraFieldCampEJB
{

  @Override
  public ExtraFieldCampDTO insert(ExtraFieldCampDTO extraField)
      throws InternalUnexpectedException
  {
    return dao.insert(extraField);
  }

  @Override
  public void update(ExtraFieldCampDTO extraField)
      throws InternalUnexpectedException
  {
    dao.update(extraField);
  }

  @Override
  public List<ExtraFieldCampDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<ExtraFieldCampDTO> findByFilter(ExtraFieldCampDTO extraField)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(extraField);
  }

  @Override
  public ExtraFieldCampDTO findById(Integer idExtraField)
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