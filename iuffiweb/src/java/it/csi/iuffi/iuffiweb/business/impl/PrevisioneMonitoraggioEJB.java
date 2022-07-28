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

import it.csi.iuffi.iuffiweb.business.IPrevisioneMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.PianoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.PrevisioneMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.service.PrevisioneMonitoraggioDAO;

@Stateless()
@EJB(name = "java:app/PrevisioneMonitoraggio", beanInterface = IPrevisioneMonitoraggioEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PrevisioneMonitoraggioEJB extends IuffiAbstractEJB<PrevisioneMonitoraggioDAO> implements IPrevisioneMonitoraggioEJB
{

  @Override
  public List<PrevisioneMonitoraggioDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public PrevisioneMonitoraggioDTO savePrevisione(PrevisioneMonitoraggioDTO previsione, Long idUtenteLogin) throws InternalUnexpectedException
  {
    return dao.savePrevisione(previsione, idUtenteLogin);
  }

  @Override
  public List<PianoMonitoraggioDTO> findPianiAll()
      throws InternalUnexpectedException
  {
    return dao.findPianiAll();
  }

  @Override
  public List<PianoMonitoraggioDTO> findPianiByFilter(PianoMonitoraggioDTO piano)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PianoMonitoraggioDTO insertPiano(PianoMonitoraggioDTO piano)
      throws InternalUnexpectedException
  {
    return dao.insertPiano(piano);
  }

  @Override
  public PianoMonitoraggioDTO updatePiano(PianoMonitoraggioDTO piano)
      throws InternalUnexpectedException
  {
    // Non prevista attualmente una modifica al piano 09/12/2020 (S.D.)
    return null;
  }

  @Override
  public void removePiano(Integer idPianoMonitoraggio)
      throws InternalUnexpectedException
  {
    dao.remove(idPianoMonitoraggio);
  }

  @Override
  public PianoMonitoraggioDTO findPianoById(Integer idPianoMonitoraggio)
      throws InternalUnexpectedException
  {
    return dao.findPianoById(idPianoMonitoraggio);
  }

  @Override
  public void updateDataFineValidita(Integer idPianoMonitoraggio)
      throws InternalUnexpectedException
  {
    dao.updateDataFineValidita(idPianoMonitoraggio);
  }

  @Override
  public List<PrevisioneMonitoraggioDTO> findPrevisioneByIdPiano(
      Integer idPianoMonitoraggio) throws InternalUnexpectedException
  {
    return dao.findPrevisioneByIdPiano(idPianoMonitoraggio);
  }

  @Override
  public long countPrevisioniByIdOrganismoNocivo(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    return dao.countPrevisioniByIdOrganismoNocivo(idOrganismoNocivo);
  }
  
}
