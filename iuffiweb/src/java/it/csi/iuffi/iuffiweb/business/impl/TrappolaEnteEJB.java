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

import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.ITrappolaEnteEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaEnteDTO;
import it.csi.iuffi.iuffiweb.service.TipoTrappolaDAO;
import it.csi.iuffi.iuffiweb.service.TrappolaEnteDAO;

@Stateless()
@EJB(name = "java:app/TrappolaEnte", beanInterface = ITrappolaEnteEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TrappolaEnteEJB extends IuffiAbstractEJB<TrappolaEnteDAO> implements ITrappolaEnteEJB
{

  @Override
  public TrappolaEnteDTO insert(TrappolaEnteDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.insert(model);
  }

  @Override
  public void update(TrappolaEnteDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.update(model);
  }

  @Override
  public List<TrappolaEnteDTO> findAll() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findAll();
  }

  @Override
  public List<TrappolaEnteDTO> findByFilter(TrappolaEnteDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByFilter(model);
  }

  @Override
  public TrappolaEnteDTO findById(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findById(id);
  }

  @Override
  public void remove(Integer idTrappolaEnte) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.remove(idTrappolaEnte);
  }

  @Override
  public List<TrappolaEnteDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idTrappolaEnte)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.updateDataFineValidita(idTrappolaEnte); 
  }

  @Override
  public List<TrappolaEnteDTO> findByIdEnte(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdEnte(id);
  }

  @Override
  public List<TrappolaEnteDTO> findByIdTrappola(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdTrappola(id);
  }
}