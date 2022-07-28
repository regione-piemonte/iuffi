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

import it.csi.iuffi.iuffiweb.business.IGestioneVerbaleEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.VerbaleCampioni;
import it.csi.iuffi.iuffiweb.model.api.VerbaleTrappole;
import it.csi.iuffi.iuffiweb.model.api.VerbaleVisual;
import it.csi.iuffi.iuffiweb.model.request.VerbaleDati;
import it.csi.iuffi.iuffiweb.service.GestioneVerbaleDAO;


@Stateless()
@EJB(name = "java:app/Verbale", beanInterface = IGestioneVerbaleEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GestioneVerbaleEJB extends IuffiAbstractEJB<GestioneVerbaleDAO> implements IGestioneVerbaleEJB
{
  //private static final String THIS_CLASS = GestioneVerbaleEJB.class.getSimpleName();

  @Override
  public void update(VerbaleDTO verbale) throws InternalUnexpectedException
  {
    dao.update(verbale);
  }

  @Override
  public VerbaleDTO insert(VerbaleDTO verbale)
      throws InternalUnexpectedException
  {
    return dao.insert(verbale);
  }

  @Override
  public List<VerbaleDTO> findAll() throws InternalUnexpectedException
  {
    return dao.findAll();
  }

  @Override
  public List<VerbaleDTO> findByFilter(VerbaleDTO verbale)
      throws InternalUnexpectedException
  {
    return dao.findByFilter(verbale);
  }

  @Override
  public VerbaleDTO findById(Integer idVerbale) throws InternalUnexpectedException
  {
    return dao.findById(idVerbale);
  }

  @Override
  public void remove(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<VerbaleVisual> getVisual(Integer id)
      throws InternalUnexpectedException
  {
    return dao.getVisual(id);
  }

  @Override
  public List<VerbaleCampioni> getCampioni(Integer id)
      throws InternalUnexpectedException
  {
    return dao.getCampioni(id);
  }

  @Override
  public List<VerbaleTrappole> getTrappole(Integer id)
      throws InternalUnexpectedException
  {
    return dao.getTrappole(id);
  }
  
  @Override
  public VerbaleDati getIntestazione(Integer id)
      throws InternalUnexpectedException
  {
    return dao.getIntestazione(id);
  }
  
  @Override
  public VerbaleDTO findByIdMissione(Integer idMissione) throws InternalUnexpectedException
  {
    return dao.findByIdMissione(idMissione);
  }

  @Override
  public byte[] getPdfVerbale(Integer idVerbale)
      throws InternalUnexpectedException
  {
    return dao.getPdfVerbale(idVerbale);
  }

  @Override
  public List<VerbaleVisual> getVisualDiCompetenza(Integer idMissione, Integer idAnagrafica)
      throws InternalUnexpectedException
  {
    return dao.getVisualDiCompetenza(idMissione, idAnagrafica);
  }

  @Override
  public List<VerbaleCampioni> getCampioniDiCompetenza(Integer idMissione, Integer idAnagrafica)
      throws InternalUnexpectedException
  {
    return dao.getCampioniDiCompetenza(idMissione, idAnagrafica);
  }

  @Override
  public List<VerbaleTrappole> getTrappoleDiCompetenza(Integer idMissione, Integer idAnagrafica)
      throws InternalUnexpectedException
  {
    return dao.getTrappoleDiCompetenza(idMissione, idAnagrafica);
  }

  @Override
  public void updateStatoVerbale(VerbaleDTO verbale) throws InternalUnexpectedException
  {
    dao.updateStatoVerbale(verbale);
  }

  @Override
  public Integer getProgVerbale(String pattern, String escape) throws InternalUnexpectedException
  {
    return dao.getProgVerbale(pattern, escape);
  }

}
