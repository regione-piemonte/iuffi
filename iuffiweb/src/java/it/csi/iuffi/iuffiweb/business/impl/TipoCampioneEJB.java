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

import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.service.TipoCampioneDAO;

@Stateless()
@EJB(name = "java:app/TipoCampione", beanInterface = ITipoCampioneEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
//@Asynchronous()
//@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
public class TipoCampioneEJB extends IuffiAbstractEJB<TipoCampioneDAO> implements ITipoCampioneEJB
{

  @Override
  public TipoCampioneDTO insertTipoCampione(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException
  {
    return dao.insertTipoCampione(tipoCampione);
  }

  @Override
  public List<TipoCampioneDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<TipoCampioneDTO> findByFilter(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException
  {
    return dao.findByFilter(tipoCampione);
  }

  @Override
  public TipoCampioneDTO findById(Integer idTipoCampione)
      throws InternalUnexpectedException
  {
    return dao.findById(idTipoCampione);
  }

  @Override
  public void remove(Integer idTipoCampione) throws InternalUnexpectedException
  {
    dao.remove(idTipoCampione);
  }

  @Override
  public void updateTipoCampione(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException
  {
    dao.update(tipoCampione);
  }

  @Override
  public List<TipoCampioneDTO> findAllAttivi()
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findAllAttivi();
  }

  @Override
  public List<TipoCampioneDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idTipoCampione)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.updateDataFineValidita(idTipoCampione);
  }

  @Override
  public List<TipoCampioneDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdMultipli(idConcatenati);
  }

}