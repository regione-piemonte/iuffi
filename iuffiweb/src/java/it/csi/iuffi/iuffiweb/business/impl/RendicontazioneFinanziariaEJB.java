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

import it.csi.iuffi.iuffiweb.business.IRendicontazioneFinanziariaEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RendicontazioneDTO;
import it.csi.iuffi.iuffiweb.service.RendicontazioneDAO;


@Stateless()
@EJB(name = "java:app/Rendicontazione", beanInterface = IRendicontazioneFinanziariaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RendicontazioneFinanziariaEJB extends IuffiAbstractEJB<RendicontazioneDAO> implements IRendicontazioneFinanziariaEJB
{
  //private static final String THIS_CLASS = RendicontazioneFinanziariaEJB.class.getSimpleName();

  @Override
  public List<RendicontazioneDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<RendicontazioneDTO> findByFilter(
      RendicontazioneDTO rendicontazione) throws InternalUnexpectedException
  {
    return dao.findByFilter(rendicontazione);
  }

  @Override
  public List<OrganismoNocivoDTO> findOrganismiNocivi(RendicontazioneDTO rendicontazione) throws InternalUnexpectedException
  {
    return dao.findOrganismiNocivi(rendicontazione);
  }

}
