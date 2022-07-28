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

import it.csi.iuffi.iuffiweb.business.IMenuItemEJB;
import it.csi.iuffi.iuffiweb.dto.MenuItemDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.service.MenuItemDAO;


@Stateless()
@EJB(name = "java:app/MenuItem", beanInterface = IMenuItemEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
//@Asynchronous()
//@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
public class MenuItemEJB extends IuffiAbstractEJB<MenuItemDAO> implements IMenuItemEJB
{
  
  //private static final String THIS_CLASS = MenuItemEJB.class.getSimpleName();


  @Override
  public List<MenuItemDTO> getMainMenu(int idParent, long[] idLivello, boolean readOnly) throws InternalUnexpectedException
  {
    return dao.getMainMenu(idParent, idLivello, readOnly);
  }

  @Override
  public List<MenuItemDTO> getBreadcrumbs(int idMenuItem)
      throws InternalUnexpectedException
  {
    return dao.getBreadcrumbs(idMenuItem);
  }

  @Override
  public List<MenuItemDTO> getBreadcrumbs(String path)
      throws InternalUnexpectedException
  {
    return dao.getBreadcrumbs(path);
  }

}
