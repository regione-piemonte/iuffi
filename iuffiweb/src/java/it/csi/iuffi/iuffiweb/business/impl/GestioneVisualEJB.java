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

import it.csi.iuffi.iuffiweb.business.IGestioneVisualEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaSpecieOnDTO;
import it.csi.iuffi.iuffiweb.model.api.OrganismiNocivi;
import it.csi.iuffi.iuffiweb.model.request.IspezioneVisivaRequest;
import it.csi.iuffi.iuffiweb.service.GestioneVisualDAO;

@Stateless()
@EJB(name = "java:app/GestioneVisual", beanInterface = IGestioneVisualEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GestioneVisualEJB extends IuffiAbstractEJB<GestioneVisualDAO> implements IGestioneVisualEJB
{
  private static final String THIS_CLASS = GestioneVisualEJB.class.getSimpleName();

  @Override
  public void update(IspezioneVisivaDTO ispezioneVisiva)
      throws InternalUnexpectedException
  {
    dao.update(ispezioneVisiva);
  }

  @Override
  public Integer insert(IspezioneVisivaDTO ispezioneVisiva)
      throws InternalUnexpectedException
  {
    Integer idIspezione = dao.insert(ispezioneVisiva);
    return idIspezione;
  }

  @Override
  public List<IspezioneVisivaDTO> findAll() throws InternalUnexpectedException
  {
     return dao.findAll();
  }

  @Override
  public List<IspezioneVisivaDTO> findByFilter(IspezioneVisivaDTO ispezioneVisiva)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(ispezioneVisiva);
  }

  @Override
  public IspezioneVisivaDTO findById(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findById(id);
  }

  @Override
  public List<FotoDTO> findListFotoByIdIspezioneVisiva(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findFotoByIdIspezioneVisiva(id);
  }

  @Override
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
  {
    return dao.findFotoById(id);
  }
  
  @Override
  public void remove(Integer id) throws InternalUnexpectedException
  {
    dao.remove(id);
  }
  
  @Override
  public void insertSpecieOn(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
  {
    dao.insertSpecieOn(ispezioneVisiva);

  }
  
  @Override
  public void updateSpecieOn(IspezioneVisivaDTO ispezioneVisiva)
      throws InternalUnexpectedException
  {
    dao.updateSpecieOn(ispezioneVisiva);
  }

  @Override
  public List<Integer> findOrganismiNociviByVisual(Integer idIspezioneVisiva)
      throws InternalUnexpectedException
  {
    return dao.findOrganismiNociviByVisual(idIspezioneVisiva);
  }
  
  @Override
  public List<IspezioneVisivaDTO> findByFilterFromRequest(IspezioneVisivaRequest ispezioneVisivaRequest,Integer idAnagrafica, Integer idEnte)
      throws InternalUnexpectedException
  {
    return dao.findByFilterFromRequest(ispezioneVisivaRequest,idAnagrafica,idEnte);
  }

  @Override
  public List<IspezioneVisivaSpecieOnDTO> findOrganismiNociviByVisualCompleto(Integer id) throws InternalUnexpectedException
  {
    return dao.findOrganismiNociviByVisualCompleto(id);
  }

  @Override
  public List<OrganismiNocivi> findOrganismiNociviFlagTrovatoByVisual(
      Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findOrganismiNociviFlagTrovatoByVisual(idIspezioneVisiva);
  }
  
  @Override
  public Integer findCampionamentiCountByVisual(Integer idIspezioneVisiva)
      throws InternalUnexpectedException
  {
    return dao.findCampionamentiCountByVisual(idIspezioneVisiva);
  }

  @Override
  public Integer findTrappolaggiCountByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException 
  {
	  return dao.findTrappolaggiCountByVisual(idIspezioneVisiva);
  }
  
  @Override
  public void removeOn(Integer idCampionamento, Integer idOrganismoNocivo)
      throws InternalUnexpectedException
  {
    dao.removeOrganismoNocivo(idCampionamento, idOrganismoNocivo);
  }

  @Override
  public List<IspezioneVisivaSpecieOnDTO> findOnByIdIspezione(Integer id) throws InternalUnexpectedException
  {
    return dao.findOnByIdIspezione(id);
  }

  @Override
  public void insertVisualSpecieOn(IspezioneVisivaSpecieOnDTO visual)
      throws InternalUnexpectedException
  {
    dao.insertVisualSpecieOn(visual);
    
  }
  
}
