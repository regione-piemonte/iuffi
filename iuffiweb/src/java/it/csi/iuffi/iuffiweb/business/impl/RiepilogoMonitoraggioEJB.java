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

import it.csi.iuffi.iuffiweb.business.IRiepilogoMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.RiepilogoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;
import it.csi.iuffi.iuffiweb.service.RiepilogoMonitoraggioDAO;

@Stateless()
@EJB(name = "java:app/RiepilogoMonitoraggio", beanInterface = IRiepilogoMonitoraggioEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RiepilogoMonitoraggioEJB extends IuffiAbstractEJB<RiepilogoMonitoraggioDAO> implements IRiepilogoMonitoraggioEJB
{

  @Override
  public List<RiepilogoMonitoraggioDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<RiepilogoMonitoraggioDTO> save(RiepilogoMonitoraggioDTO riepilogo) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public List<RiepilogoMonitoraggio> getRiepilogoMonitoraggio() throws InternalUnexpectedException
  {
    return dao.getRiepilogoMonitoraggio();
  }

  @Override
  public RiepilogoMonitoraggio findByUniqueKey(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, Integer mese) throws InternalUnexpectedException
  {
    return dao.findByUniqueKey(idSpecieVegetale, idTipoCampione, idOrganismoNocivo, mese);
  }

  @Override
  public RiepilogoMonitoraggio insert(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException
  {
    return dao.insert(riepilogo);
  }

  @Override
  public void update(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException
  {
    dao.update(riepilogo);
  }

  @Override
  public void removeMesi(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, String mesi, Long idUtente) throws InternalUnexpectedException
  {
    dao.removeMesi(idSpecieVegetale, idTipoCampione, idOrganismoNocivo, mesi, idUtente);
  }

  @Override
  public RiepilogoMonitoraggioDTO findBySpecieVegetaleTipoCampioneAndMesi(Integer idSpecieVegetale, Integer idTipoCampione, String mesi) throws InternalUnexpectedException
  {
    return dao.findBySpecieVegetaleTipoCampioneAndMesi(idSpecieVegetale, idTipoCampione, mesi);
  }

  @Override
  public void storicizza(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException
  {
    dao.storicizza(riepilogo);
  }

  @Override
  public void removeOrganismoNocivo(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, Long idUtente) throws InternalUnexpectedException
  {
    dao.removeOrganismoNocivo(idSpecieVegetale, idTipoCampione, idOrganismoNocivo, idUtente);
  }

  @Override
  public void remove(Integer idSpecieVegetale, Integer idTipoCampione, String mesi, String on, Long idUtente) throws InternalUnexpectedException
  {
    dao.remove(idSpecieVegetale, idTipoCampione, mesi, on, idUtente);
  }

  @Override
  public Long getMaxCountON() throws InternalUnexpectedException
  {
    return dao.getMaxCountON();
  }

  @Override
  public List<RiepilogoMonitoraggio> findByIdOrganismoNocivo(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    return dao.findByIdOrganismoNocivo(idOrganismoNocivo);
  }

  @Override
  public List<RiepilogoMonitoraggio> findByIdSpecieVegetale(Integer idSpecieVegetale) throws InternalUnexpectedException
  {
    return dao.findByIdSpecieVegetale(idSpecieVegetale);
  }

  @Override
  public List<RiepilogoMonitoraggio> findByIdTipoCampione(Integer idTipoCampione) throws InternalUnexpectedException
  {
    return dao.findByIdTipoCampione(idTipoCampione);
  }
  
}
