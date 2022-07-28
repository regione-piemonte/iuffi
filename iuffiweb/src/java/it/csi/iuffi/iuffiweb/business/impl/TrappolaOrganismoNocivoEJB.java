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
import it.csi.iuffi.iuffiweb.business.ITrappolaOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaEnteDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaOnDTO;
import it.csi.iuffi.iuffiweb.service.TipoTrappolaDAO;
import it.csi.iuffi.iuffiweb.service.TrappolaOnDAO;

@Stateless()
@EJB(name = "java:app/TrappolaOrganismoNocivo", beanInterface = ITrappolaOrganismoNocivoEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TrappolaOrganismoNocivoEJB extends IuffiAbstractEJB<TrappolaOnDAO> implements ITrappolaOrganismoNocivoEJB
{

  @Override
  public TrappolaOnDTO insert(TrappolaOnDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.insert(model);
  }

  @Override
  public void update(TrappolaOnDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.update(model);
  }

  @Override
  public List<TrappolaOnDTO> findAll() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findAll();
  }

  @Override
  public List<TrappolaOnDTO> findByFilter(TrappolaOnDTO model)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByFilter(model);
  }

  @Override
  public TrappolaOnDTO findById(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findById(id);
  }

  @Override
  public void remove(Integer idTrappolaOn) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.remove(idTrappolaOn);
  }

  @Override
  public List<TrappolaOnDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idTrappolaOn)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.updateDataFineValidita(idTrappolaOn); 
  }

  @Override
  public List<TrappolaOnDTO> findByIdOn(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdOn(id);
  }

  @Override
  public List<TrappolaOnDTO> findByIdTrappola(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdTrappola(id);
  }

}