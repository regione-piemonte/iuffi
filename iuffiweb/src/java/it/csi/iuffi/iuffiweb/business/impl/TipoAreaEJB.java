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

import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.VelocitaDTO;
import it.csi.iuffi.iuffiweb.model.api.TipoArea;
import it.csi.iuffi.iuffiweb.service.TipoAreaDAO;

@Stateless()
@EJB(name = "java:app/TipoArea", beanInterface = ITipoAreaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TipoAreaEJB extends IuffiAbstractEJB<TipoAreaDAO> implements ITipoAreaEJB
{

  @Override
  public TipoAreaDTO insertTipoArea(TipoAreaDTO tipoArea) throws InternalUnexpectedException
  {
    return dao.insertTipoArea(tipoArea);
  }

  @Override
  public List<TipoAreaDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<TipoAreaDTO> findByFilter(TipoAreaDTO tipoArea) throws InternalUnexpectedException
  {
    return dao.findByFilter(tipoArea);
  }

  @Override
  public TipoAreaDTO findById(Integer idTipoArea)
      throws InternalUnexpectedException
  {
    return dao.findById(idTipoArea);
  }

  @Override
  public void remove(Integer idTipoArea) throws InternalUnexpectedException
  {
    dao.remove(idTipoArea);
  }

  @Override
  public void updateTipoArea(TipoAreaDTO tipoArea) throws InternalUnexpectedException
  {
    dao.update(tipoArea);
  }

  @Override
  public List<TipoArea> getTipoAree() throws InternalUnexpectedException
  {
    return dao.getTipoAree();
  }

  @Override
  public List<TipoAreaDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    return dao.findByIdMultipli(idConcatenati);
  }

  @Override
  public List<TipoAreaDTO> findValidi() throws InternalUnexpectedException
  {
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idTipoArea)
      throws InternalUnexpectedException
  {
    dao.updateDataFineValidita(idTipoArea);
  }

  @Override
  public List<VelocitaDTO> findVelocita() throws InternalUnexpectedException
  {
    return dao.findVelocita();
  }
  
}