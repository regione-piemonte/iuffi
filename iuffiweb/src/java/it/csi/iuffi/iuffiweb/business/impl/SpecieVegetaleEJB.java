package it.csi.iuffi.iuffiweb.business.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;


import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.api.GenereSpecieVegetale;
import it.csi.iuffi.iuffiweb.model.api.SpecieVegetale;
import it.csi.iuffi.iuffiweb.service.MissioneDAO;
import it.csi.iuffi.iuffiweb.service.SpecieVegetaleDAO;


@Stateless()
@EJB(name = "java:app/SpecieVegetale", beanInterface = ISpecieVegetaleEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class SpecieVegetaleEJB extends IuffiAbstractEJB<SpecieVegetaleDAO> implements ISpecieVegetaleEJB
{
  private static final String THIS_CLASS = SpecieVegetaleEJB.class.getSimpleName();



  @Override
  public SpecieVegetaleDTO insertSpecieVegetale(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException
  {
    return dao.insertSpecieVegetale(specieVegetale);
  }

  @Override
  public List<SpecieVegetaleDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<SpecieVegetaleDTO> findByFilter(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException
  {
    return dao.findByFilter(specieVegetale);
  }

  @Override
  public SpecieVegetaleDTO findById(Integer idSpecieAnimale)
      throws InternalUnexpectedException
  {
    return dao.findById(idSpecieAnimale);
  }

  @Override
  public void remove(Integer idSpecieAnimale) throws InternalUnexpectedException
  {
    dao.remove(idSpecieAnimale);
  }

  @Override
  public void updateSpecieVegetale(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException
  {
    dao.update(specieVegetale);
  }

  @Override
  public List<GenereSpecieVegetale> getSpecieVegetali() throws InternalUnexpectedException
  {
    return dao.getSpecieVegetali();
  }

  @Override
  public List<SpecieVegetaleDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    return dao.findByIdMultipli(idConcatenati);
  }

  @Override
  public void updateDataFineValidita(Integer idSpecieVegetale)
      throws InternalUnexpectedException
  {
    dao.updateDataFineValidita(idSpecieVegetale);
  }

  @Override
  public List<SpecieVegetaleDTO> findValidi() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidi();
  }

}
