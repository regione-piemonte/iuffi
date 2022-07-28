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

import it.csi.iuffi.iuffiweb.business.IGestioneTrappolaEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.OperazioneTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.model.api.Trappola;
import it.csi.iuffi.iuffiweb.model.request.TrappolaggioRequest;
import it.csi.iuffi.iuffiweb.service.GestioneTrappolaDAO;


@Stateless()
@EJB(name = "java:app/GestioneTrappola", beanInterface = IGestioneTrappolaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GestioneTrappolaEJB extends IuffiAbstractEJB<GestioneTrappolaDAO> implements IGestioneTrappolaEJB
{
  
  @Override
  public void update(TrappolaggioDTO trappolaggio)
      throws InternalUnexpectedException
  {
    dao.update(trappolaggio);
  }

  @Override
  public TrappolaggioDTO insert(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException
  {
    return dao.insert(trappolaggio);
  }

  @Override
  public List<TrappolaggioDTO> findAll() throws InternalUnexpectedException
  {
     return dao.findAll();
  }

  @Override
  public TrappolaggioDTO findById(Integer idTrappolaggio) throws InternalUnexpectedException
  {
    return dao.findById(idTrappolaggio);
  }

  @Override
  public void remove(Integer idTrappolaggio) throws InternalUnexpectedException
  {
    dao.remove(idTrappolaggio);
  }

  @Override
  public List<FotoDTO> findListFotoByIdTrappolaggio(Integer idTrappolaggio) throws InternalUnexpectedException
  {
    return dao.findFotoByIdTrappolaggio(idTrappolaggio);
  }

  @Override
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
  {
    return dao.findFotoById(id);
  }

  @Override
  public List<OrganismoNocivoDTO> findOnByIdTrappola(Integer idTrappola) throws InternalUnexpectedException
  {
    return dao.findOnByIdTrappola(idTrappola);
  }

  @Override
  public List<TipoTrappolaDTO> findTipoTrappolaByFilter(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    return dao.findTipoTrappolaByFilter(tipoTrappola);
  }

  @Override
  public List<TrappolaggioDTO> findByFilter(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException
  {
    return dao.findByFilter(trappolaggio);
  }

  @Override
  public TrappolaDTO insertTrappola(TrappolaDTO trappola)
      throws InternalUnexpectedException
  {
    return dao.insertTrappola(trappola);
  }

  @Override
  public void removeTrappola(Integer idTrappola, Long idUtente) throws InternalUnexpectedException
  {
    dao.removeTrappola(idTrappola, idUtente);
  }

  @Override
  public TrappolaDTO findTrappolaById(Integer idTrappola) throws InternalUnexpectedException
  {
    return dao.findTrappolaById(idTrappola);
  }

  @Override
  public void updateTrappola(TrappolaDTO trappola) throws InternalUnexpectedException
  {
    dao.updateTrappola(trappola);
  }

  @Override
  public List<Trappola> findTrappolaByCoordinates(Double latitudine, Double longitudine, Integer raggio) throws InternalUnexpectedException
  {
    return dao.findTrappolaByCoordinates(latitudine, longitudine, raggio);
  }

  @Override
  public Trappola findTrappolaByCodice(String codice) throws InternalUnexpectedException
  {
    return dao.findTrappolaByCodice(codice);
  }

  @Override
  public List<TrappolaggioDTO> findByFilterByTrappolaggioRequest(TrappolaggioRequest trappolaggio, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    return dao.findByFilterByTrappolaggioRequest(trappolaggio, idAnagrafica, idEnte);
  }

  @Override
  public void eliminaTrappola(Integer idTrappola) throws InternalUnexpectedException
  {
    dao.eliminaTrappola(idTrappola);
  }

  @Override
  public List<TrappolaggioDTO> findByIdIspezione(Integer idIspezioneVisiva)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdIspezione(idIspezioneVisiva);
  }

  @Override
  public List<TrappolaggioDTO> findStoriaTrappolaByCodice(String codice)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findStoriaTrappolaByCodice(codice);
  }

  @Override
  public TrappolaDTO findTrappolaByCodiceSfr(String codice)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findTrappolaByCodiceSfr(codice);
  }
  
  @Override
  public List<OperazioneTrappolaDTO> findTipoOperazione()
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findTipoOperazione();
  }
  
  @Override
  public List<OperazioneTrappolaDTO> findByIdMultipli(String idConcatenati)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findByIdMultipli(idConcatenati);
  }

 
}
