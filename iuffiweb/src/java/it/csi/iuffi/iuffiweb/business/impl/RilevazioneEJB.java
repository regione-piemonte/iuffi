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

import it.csi.iuffi.iuffiweb.business.IRilevazioneEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.api.Rilevazione;
import it.csi.iuffi.iuffiweb.service.RilevazioneDAO;



@Stateless()
@EJB(name = "java:app/Rilevazione", beanInterface = IRilevazioneEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RilevazioneEJB extends IuffiAbstractEJB<RilevazioneDAO> implements IRilevazioneEJB
{
  private static final String THIS_CLASS = RilevazioneEJB.class.getSimpleName();

  @Override
  public void update(RilevazioneDTO rilevazione)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.update(rilevazione);
  }

//  @Override
//  public RilevazioneDTO insert(RilevazioneDTO rilevazione)
//      throws InternalUnexpectedException
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
  
  @Override
  public Integer insert(RilevazioneDTO rilevazione)
      throws InternalUnexpectedException
  {
    Integer idRilevazione = dao.insert(rilevazione);
    return idRilevazione;
  }

  @Override
  public List<RilevazioneDTO> findAll() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<RilevazioneDTO> findByFilter(RilevazioneDTO rilevazione)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(rilevazione);
  }

  @Override
  public List<Rilevazione> findByFilterForApi(RilevazioneDTO rilevazione)
      throws InternalUnexpectedException
  {
    return dao.findByFilterForApi(rilevazione);
  }

  @Override
  public RilevazioneDTO findById(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findById(id);
  }

  @Override
  public void remove(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.remove(id);
  }

  @Override
  public void updateFilter(RilevazioneDTO rilevazione)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.updateFilter(rilevazione);
  }

 
}
