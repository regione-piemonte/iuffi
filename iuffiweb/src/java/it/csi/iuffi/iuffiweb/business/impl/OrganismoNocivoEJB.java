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

import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.service.OrganismoNocivoDAO;

@Stateless()
@EJB(name = "java:app/OrganismoNocivo", beanInterface = IOrganismoNocivoEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class OrganismoNocivoEJB extends IuffiAbstractEJB<OrganismoNocivoDAO> implements IOrganismoNocivoEJB
{
  @Override
  public OrganismoNocivoDTO insertOrganismoNocivo(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException
  {
    return dao.insertOrganismoNocivo(organismoNocivo);
  }

  @Override
  public List<OrganismoNocivoDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<OrganismoNocivoDTO> findByFilter(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException
  {
    return dao.findByFilter(organismoNocivo);
  }

  @Override
  public OrganismoNocivoDTO findById(Integer idOrganismoNocivo)
      throws InternalUnexpectedException
  {
    return dao.findById(idOrganismoNocivo);
  }

  @Override
  public void remove(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    dao.remove(idOrganismoNocivo);
  }

  @Override
  public void updateOrganismoNocivo(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException
  {
    dao.update(organismoNocivo);
  }

  @Override
  public List<OrganismoNocivoDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    return dao.findByIdMultipli(idConcatenati);
  }

  @Override
  public void updateDataFineValidita(Integer idOrganismoNocivo)
      throws InternalUnexpectedException
  {
    dao.updateDataFineValidita(idOrganismoNocivo);
  }

  @Override
  public List<OrganismoNocivoDTO> findValidi()
      throws InternalUnexpectedException
  {
    return dao.findValidi();
  }

  @Override
  public List<OrganismoNocivoDTO> findByPianificazione()
      throws InternalUnexpectedException
  {
    return dao.findByPianificazione();
  }

}
