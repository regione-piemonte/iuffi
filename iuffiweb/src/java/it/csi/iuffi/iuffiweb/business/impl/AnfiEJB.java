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

import it.csi.iuffi.iuffiweb.business.IAnfiEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnfiDTO;
import it.csi.iuffi.iuffiweb.service.AnfiDAO;



@Stateless()
@EJB(name = "java:app/Anfi", beanInterface = IAnfiEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AnfiEJB extends IuffiAbstractEJB<AnfiDAO> implements IAnfiEJB
{


  @Override
  public AnfiDTO insertAnfi(AnfiDTO anfi) throws InternalUnexpectedException
  {
    return dao.insertAnfi(anfi);
  }

  @Override
  public List<AnfiDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<AnfiDTO> findByFilter(AnfiDTO anfi) throws InternalUnexpectedException
  {
    return dao.findByFilter(anfi);
  }

  @Override
  public AnfiDTO findById(Integer idAnfi)
      throws InternalUnexpectedException
  {
    return dao.findById(idAnfi);
  }

  @Override
  public void remove(Integer idAnfi) throws InternalUnexpectedException
  {
    dao.remove(idAnfi);
  }

  @Override
  public void updateAnfi(AnfiDTO anfi) throws InternalUnexpectedException
  {
    dao.update(anfi);
  }

  @Override
  public List<AnfiDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

  @Override
  public void updateDataFineValidita(Integer idAnfi)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.updateDataFineValidita(idAnfi);
  }

  @Override
  public List<AnfiDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdMultipli(idConcatenati);
  }

}
