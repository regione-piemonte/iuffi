package it.csi.iuffi.iuffiweb.business.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IShapeEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ShapeDTO;
import it.csi.iuffi.iuffiweb.service.ShapeDAO;


@Stateless()
@EJB(name = "java:app/Shape", beanInterface = IShapeEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ShapeEJB extends IuffiAbstractEJB<ShapeDAO> implements IShapeEJB
{

  @Override
  public ShapeDTO insert(ShapeDTO shape) throws InternalUnexpectedException
  {
    return dao.insert(shape);
  }

  public List<ShapeDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  public List<ShapeDTO> findValidi() throws InternalUnexpectedException
  {
    return dao.findValidi();
  }

  public List<ShapeDTO> findByType(List<Integer> organismi, String tipoAttività, String type) throws InternalUnexpectedException
  {
    return dao.findByType(organismi, tipoAttività, type);
  }

  public void deleteByOn(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    dao.deleteByOn(idOrganismoNocivo);
  }

  public void deleteByOnAndTipoAttivita(Integer idOrganismoNocivo, Long idTipoAttivita) throws InternalUnexpectedException
  {
    dao.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
  }

}
