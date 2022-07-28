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

import it.csi.iuffi.iuffiweb.business.IAutorizzazioniEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AutorizzazioniDTO;
import it.csi.iuffi.iuffiweb.service.AutorizzazioniDAO;



@Stateless()
@EJB(name = "java:app/Autorizzazioni", beanInterface = IAutorizzazioniEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AutorizzazioniEJB extends IuffiAbstractEJB<AutorizzazioniDAO> implements IAutorizzazioniEJB
{
  private static final String THIS_CLASS = AutorizzazioniEJB.class.getSimpleName();

  @Override
  public  List<AutorizzazioniDTO> findById(long id)
      throws InternalUnexpectedException
  {
    return dao.findById(id);
  }

  @Override
  public List<AutorizzazioniDTO> findByIdLivelloAndReadOnly(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.findByIdLivelloAndReadOnly(idLivello);
  }

}
