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

import it.csi.iuffi.iuffiweb.business.ICampioneEnteEJB;

import it.csi.iuffi.iuffiweb.business.ITrappolaEnteEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.CampioneEnteDTO;

import it.csi.iuffi.iuffiweb.service.CampioneEnteDAO;


@Stateless()
@EJB(name = "java:app/CampioneEnte", beanInterface = ICampioneEnteEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class CampioneEnteEJB extends IuffiAbstractEJB<CampioneEnteDAO> implements ICampioneEnteEJB
{

  @Override
  public CampioneEnteDTO insert(CampioneEnteDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.insert(model);
  }

  @Override
  public void update(CampioneEnteDTO model) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.update(model);
  }

  @Override
  public List<CampioneEnteDTO> findAll() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findAll();
  }

  @Override
  public List<CampioneEnteDTO> findByFilter(CampioneEnteDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByFilter(model);
  }

  @Override
  public CampioneEnteDTO findById(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findById(id);
  }

  @Override
  public void remove(Integer idCampioneEnte) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.remove(idCampioneEnte);
  }

  @Override
  public List<CampioneEnteDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idCampioneEnte)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.updateDataFineValidita(idCampioneEnte);

  }

  
}