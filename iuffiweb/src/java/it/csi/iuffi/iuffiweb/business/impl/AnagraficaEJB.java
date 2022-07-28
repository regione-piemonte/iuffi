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

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.EnteDTO;
import it.csi.iuffi.iuffiweb.service.AnagraficaDAO;


@Stateless()
@EJB(name = "java:app/Anagrafica", beanInterface = IAnagraficaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AnagraficaEJB extends IuffiAbstractEJB<AnagraficaDAO> implements IAnagraficaEJB
{
  //private static final String THIS_CLASS = AnagraficaEJB.class.getSimpleName();

  @Override
  public void updateAnagrafica(AnagraficaDTO anagrafica)
      throws InternalUnexpectedException
  {
    dao.update(anagrafica);
  }

  @Override
  public AnagraficaDTO insertAnagrafica(AnagraficaDTO anagrafica)
      throws InternalUnexpectedException
  {
    return dao.insertAnagrafica(anagrafica);
  }

  @Override
  public List<AnagraficaDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<AnagraficaDTO> findByFilter(AnagraficaDTO anagrafica)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(anagrafica);
  }

  @Override
  public AnagraficaDTO findById(Integer idAnagrafica)
      throws InternalUnexpectedException
  {
    return dao.findById(idAnagrafica);
  }

  @Override
  public void remove(Integer idAnagrafica) throws InternalUnexpectedException
  {
    dao.remove(idAnagrafica);
  }

  @Override
  public List<EnteDTO> getEnti() throws InternalUnexpectedException
  {
    return dao.getEnti();
  }

  @Override
  public void updateDataFineValidita(Integer idAnagrafica)
      throws InternalUnexpectedException
  {
    dao.updateDataFineValidita(idAnagrafica);    
  }

  @Override
  public List<AnagraficaDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    return dao.findByIdMultipli(idConcatenati);
  }

  @Override
  public List<AnagraficaDTO> findValidi() throws InternalUnexpectedException
  {
    return dao.findValidi();
  }

  @Override
  public AnagraficaDTO getIdAnagraficaFromCf(String cf) throws InternalUnexpectedException
  {
    return dao.getIdAnagraficaFromCf(cf);
  }

  @Override
  public EnteDTO findEnteById(Integer idEnte) throws InternalUnexpectedException
  {
    return dao.findEnteById(idEnte);
  }

  @Override
  public EnteDTO findEnteByIdPapua(Integer idEntePapua) throws InternalUnexpectedException
  {
    return dao.findEnteByIdPapua(idEntePapua);
  }

  @Override
  public EnteDTO insertEnte(EnteDTO ente) throws InternalUnexpectedException
  {
    return dao.insertEnte(ente);
  }

@Override
public List<AnagraficaDTO> findValidiMissione(Integer idMissione) throws InternalUnexpectedException {
	// TODO Auto-generated method stub
	return dao.findValidiMissione(idMissione);
}

}
