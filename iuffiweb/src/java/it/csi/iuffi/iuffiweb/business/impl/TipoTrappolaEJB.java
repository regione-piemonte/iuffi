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
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.service.TipoTrappolaDAO;

@Stateless()
@EJB(name = "java:app/TipoTrappola", beanInterface = ITipoTrappolaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TipoTrappolaEJB extends IuffiAbstractEJB<TipoTrappolaDAO> implements ITipoTrappolaEJB
{
  @Override
  public TipoTrappolaDTO insertTipoTrappola(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    return dao.insertTipoTrappola(tipoTrappola);
  }

  @Override
  public List<TipoTrappolaDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<TipoTrappolaDTO> findByFilter(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    return dao.findByFilter(tipoTrappola);
  }

  @Override
  public TipoTrappolaDTO findById(Integer idTipoTrappola)
      throws InternalUnexpectedException
  {
    return dao.findById(idTipoTrappola);
  }

  @Override
  public void remove(Integer idTipoTrappola) throws InternalUnexpectedException
  {
    dao.remove(idTipoTrappola);
  }

  @Override
  public void updateTipoTrappola(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    dao.update(tipoTrappola);
  }

  @Override
  public List<TipoTrappolaDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idTipoTrappola)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
   dao.updateDataFineValidita(idTipoTrappola); 
  }

  @Override
  public List<TipoTrappolaDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdMultipli(idConcatenati);
  }

}