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

import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.Missione;
import it.csi.iuffi.iuffiweb.model.request.MissioneRequest;
import it.csi.iuffi.iuffiweb.service.MissioneDAO;


@Stateless()
@EJB(name = "java:app/Missione", beanInterface = IMissioneEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
//@Asynchronous()
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
public class MissioneEJB extends IuffiAbstractEJB<MissioneDAO> implements IMissioneEJB
{

  @Override
  public Long insertMissione(MissioneDTO missione) throws InternalUnexpectedException
  {
    Long idMissione = dao.insertMissione(missione);
    return idMissione;
  }

  @Override
  public void updateMissione(MissioneDTO missione)
      throws InternalUnexpectedException
  {
    dao.updateMissione(missione);
  }

  @Override
  public List<MissioneDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public MissioneDTO findById(Long idMissione)
      throws InternalUnexpectedException
  {
    return dao.findById(idMissione);
  }

  @Override
  public List<AnagraficaDTO> getIspettoriAggiunti(Long idMissione)
      throws InternalUnexpectedException
  {
    return dao.getIspettoriAggiunti(idMissione);
  }


  @Override
  public List<MissioneDTO> findByFilter(MissioneRequest mr, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    return dao.findByFilter(mr, idAnagrafica, idEnte);
  }

  @Override
  public List<Missione> getMissioni(MissioneRequest mr)
      throws InternalUnexpectedException
  {
    return dao.getMissioni(mr);
  }

  @Override
  public void deleteMissione(Long idMissione) throws InternalUnexpectedException
  {
    dao.remove(idMissione.intValue());
  }

  @Override
  public byte[] getPdfTrasferta(Long idMissione)
      throws InternalUnexpectedException
  {
    return dao.getPdfTrasferta(idMissione);
  }

  @Override
  public List<MissioneDTO> findByDateAndIspettore(String dataOraInizio, Integer idIspettore, Long idMissione) throws InternalUnexpectedException
  {
    return dao.findByDateAndIspettore(dataOraInizio, idIspettore, idMissione);
  }

  @Override
  public List<MissioneDTO> findMissioneForVerbali(MissioneRequest mr, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    return dao.findMissioneForVerbali(mr, idAnagrafica, idEnte);
  }

  @Override
  public List<VerbaleDTO> findVerbaliByMissione(MissioneRequest mr)
      throws InternalUnexpectedException
  {
    return dao.findVerbaliByMissione(mr);
  }

  @Override
  public int getIdStatoVerbaleByIdMissione(long idMissione)
      throws InternalUnexpectedException
  {
    return dao.getIdStatoVerbaleByIdMissione(idMissione);
  }

}
