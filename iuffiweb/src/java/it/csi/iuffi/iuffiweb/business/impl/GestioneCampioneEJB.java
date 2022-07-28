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

import it.csi.iuffi.iuffiweb.business.IGestioneCampioneEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoSpecOnDTO;
import it.csi.iuffi.iuffiweb.model.CodiceEsitoDTO;
import it.csi.iuffi.iuffiweb.model.EsitoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.request.CampionamentoRequest;
import it.csi.iuffi.iuffiweb.service.GestioneCampioneDAO;


@Stateless()
@EJB(name = "java:app/GestioneCampione", beanInterface = IGestioneCampioneEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GestioneCampioneEJB extends IuffiAbstractEJB<GestioneCampioneDAO> implements IGestioneCampioneEJB
{

  //private static final String THIS_CLASS = GestioneCampioneEJB.class.getSimpleName();

  @Override
  public List<CampionamentoDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<CampionamentoDTO> findByFilter(CampionamentoRequest campionamentoRequest, Integer idAnagrafica, Integer idEnte)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(campionamentoRequest, idAnagrafica, idEnte);
  }

  @Override
  public CampionamentoDTO findById(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findById(id);
  }

  @Override
  public List<FotoDTO> findListFotoByIdCampione(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findFotoByIdCampione(id);
  }

  @Override
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
  {
    return dao.findFotoById(id);
  }

  @Override
  public List<CampionamentoDTO> findByIdRilevazione(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findByIdRilevazione(id);
  }

  @Override
  public List<CampionamentoSpecOnDTO> findOnByIdCampionamento(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findOnByIdCampionamento(id);
  }

  @Override
  public List<EsitoCampioneDTO> findEsitiByIdCampionamento(Integer idCampionamento) throws InternalUnexpectedException
  {
    return dao.findEsitiByIdCampionamento(idCampionamento);
  }

  @Override
  public void removeEsito(Integer idEsitoCampione) throws InternalUnexpectedException
  {
    dao.removeEsito(idEsitoCampione);
  }

  @Override
  public EsitoCampioneDTO findEsitoById(Integer idEsitoCampione)
      throws InternalUnexpectedException
  {
    return dao.findEsitoById(idEsitoCampione);
  }

  @Override
  public EsitoCampioneDTO insertEsito(EsitoCampioneDTO esitoCampione)
      throws InternalUnexpectedException
  {
    return dao.insertEsitoCampione(esitoCampione);
  }

  @Override
  public void updateEsito(EsitoCampioneDTO esitoCampione) throws InternalUnexpectedException
  {
    dao.updateEsitoCampione(esitoCampione);
  }

  @Override
  public void update(CampionamentoDTO campione) throws InternalUnexpectedException
  {
    dao.update(campione);
  }
  
  @Override
  public Integer insert(CampionamentoDTO campione)
      throws InternalUnexpectedException
  {
    Integer idCampionamento = dao.insert(campione);
    return idCampionamento;
  }
  
  @Override
  public void remove(Integer id) throws InternalUnexpectedException
  {
    dao.remove(id);
  }

  @Override
  public void insertCampioneSpecieOn(CampionamentoSpecOnDTO campione) throws InternalUnexpectedException
  {
    dao.insertCampioneSpecieOn(campione);
  }
  
  @Override
  public void updateCampioneSpecieOn(CampionamentoDTO campione) throws InternalUnexpectedException
  {
    dao.updateCampioneSpecieOn(campione);
  }

  @Override
  public void removeCampioneSpecieOn(Integer id) throws InternalUnexpectedException
  {
    dao.removeCampioneSpecieOn(id);
  }

  @Override
  public void removeOn(Integer idCampionamento, Integer idOrganismoNocivo)
      throws InternalUnexpectedException
  {
    dao.removeOrganismoNocivo(idCampionamento, idOrganismoNocivo);
  }

  @Override
  public List<CampionamentoDTO> findByIdIspezione(Integer idIspezioneVisiva)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdIspezione(idIspezioneVisiva);
  }

  @Override
  public List<CodiceEsitoDTO> findValidiCodiciEsito() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findValidiCodiciEsito();
  }

  @Override
  public List<CodiceEsitoDTO> findAllCodiciEsito()
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findAllCodiciEsito();
  }

  @Override
  public List<CodiceEsitoDTO> findByIdMultipliCodiciEsito(String id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdMultipliCodiciEsito(id);
  }
  
  
  @Override
  public byte[] getPdf(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.getPdf(id);
  }

  @Override
  public CodiceEsitoDTO findCodiceEsitoById(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findCodiceEsitoById(id);
  }
  
}
