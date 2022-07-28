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

import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.FotoApiDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.GpsDTO;
import it.csi.iuffi.iuffiweb.service.GpsFotoDAO;


@Stateless()
@EJB(name = "java:app/GpsFoto", beanInterface = IGpsFotoEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
//@Asynchronous()
//@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
public class GpsFotoEJB extends IuffiAbstractEJB<GpsFotoDAO> implements IGpsFotoEJB
{
  private static final String THIS_CLASS = GpsFotoEJB.class.getSimpleName();

  @Override
  public List<FotoDTO> findAllFoto() throws InternalUnexpectedException
  {
    return dao.findAllFoto();
  }

  @Override
  public FotoDTO findFotoById(Long id) throws InternalUnexpectedException
  {
    return dao.findFotoById(id);
  }

  @Override
  public List<FotoDTO> findFotoByFilter(FotoDTO foto, Integer idAnagrafica, Integer idEnte, String orderBy) throws InternalUnexpectedException
  {
    return dao.findFotoByFilter(foto, idAnagrafica, idEnte, orderBy);
  }

  //gps

  @Override
  public List<GpsDTO> findAllGps() throws InternalUnexpectedException
  {
    return dao.findAllGps();
  }

  @Override
  public GpsDTO findGpsById(Long id) throws InternalUnexpectedException
  {
    return dao.findGpsById(id);
  }

  @Override
  public List<GpsDTO> findGpsByFilter(GpsDTO foto, Integer idAnagrafica, Integer idEnte, String orderBy) throws InternalUnexpectedException
  {
    return dao.findGpsByFilter(foto, idAnagrafica, idEnte, orderBy);
  }

  @Override
  public Long insertFotoCampione(Long idFoto, Long idCampione)
      throws InternalUnexpectedException
  {
    return dao.insertFotoCampione(idFoto, idCampione);
  }

  @Override
  public Long insertFotoVisual(Long idFoto, Long idVisual)
      throws InternalUnexpectedException
  {
    return dao.insertFotoVisual(idFoto, idVisual);
  }

  @Override
  public Long insertFotoTrappola(Long idFoto, Long idTrappola)
      throws InternalUnexpectedException
  {
    return dao.insertFotoTrappola(idFoto, idTrappola);
  }

  @Override
  public void removeFoto(Integer idFoto) throws InternalUnexpectedException
  {
    dao.removeFoto(idFoto);
  }

  @Override
  public void removeFotoCampionamento(Integer idFoto, Integer idCampionamento)
      throws InternalUnexpectedException
  {
    dao.removeFotoCampionamento(idFoto, idCampionamento);
  }

  @Override
  public void removeFotoTrappolaggio(Integer idFoto, Integer idTrappolaggio)
      throws InternalUnexpectedException
  {
    dao.removeFotoTrappolaggio(idFoto, idTrappolaggio);
  }

  @Override
  public void removeFotoIspezione(Integer idFoto, Integer idIspezione)
      throws InternalUnexpectedException
  {
    dao.removeFotoIspezione(idFoto, idIspezione);
  }

  @Override
  public FotoApiDTO selectFotoToRemoveById(Integer id)
      throws InternalUnexpectedException
  {
    return dao.selectFotoToRemoveById(id);
  }

  @Override
  public List<FotoDTO> selectFotoByIdAzione(FotoApiDTO fotoApi)
      throws InternalUnexpectedException
  {
    return dao.selectFotoByIdAzione(fotoApi);
  }

  @Override
  public Long insertFoto(FotoDTO foto) throws InternalUnexpectedException
  {
    return dao.insertFoto(foto);
  }

  @Override
  public Long selectFotoCampione(Long id) throws InternalUnexpectedException
  {
    return dao.countFotoCampionamento(id);
  }

  @Override
  public Long selectFotoVisual(Long id) throws InternalUnexpectedException
  {
    return dao.countFotoVisual(id);
  }

  @Override
  public Long selectFotoTrappola(Long id) throws InternalUnexpectedException
  {
    return dao.countFotoTrappolaggio(id);
  }

}
