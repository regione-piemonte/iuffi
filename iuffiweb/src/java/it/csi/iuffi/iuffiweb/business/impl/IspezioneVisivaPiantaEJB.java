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
import it.csi.iuffi.iuffiweb.business.IIspezioneVisivaPiantaEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.DiametroDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaPiantaDTO;
import it.csi.iuffi.iuffiweb.model.PositivitaDTO;
import it.csi.iuffi.iuffiweb.service.IspezioneVisivaPiantaDAO;


@Stateless()
@EJB(name = "java:app/IspezioneVisivaPianta", beanInterface = IIspezioneVisivaPiantaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class IspezioneVisivaPiantaEJB extends IuffiAbstractEJB<IspezioneVisivaPiantaDAO> implements IIspezioneVisivaPiantaEJB
{
  private static final String THIS_CLASS = GestioneVisualEJB.class.getSimpleName();

  @Override
  public void update(IspezioneVisivaPiantaDTO ispezioneVisivaPianta)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.update(ispezioneVisivaPianta);
  }

  @Override
  public Integer insert(IspezioneVisivaPiantaDTO ispezioneVisivaPianta)
      throws InternalUnexpectedException
  {
    Integer idIspezioneVisivaPianta = dao.insert(ispezioneVisivaPianta);
    return idIspezioneVisivaPianta;
  }

  @Override
  public List<IspezioneVisivaPiantaDTO> findAll() throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
     return dao.findAll();
  }

  @Override
  public IspezioneVisivaPiantaDTO findById(Integer id)
      throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    return dao.findById(id);
  }

  @Override
  public List<IspezioneVisivaPiantaDTO> findByIdIspezioneVisiva(Integer id)
      throws InternalUnexpectedException
  {
    return dao.findByIdIspezioneVisiva(id);
  }
  
//  @Override
//  public List<IspezioneVisivaPiantaDTO> findByFilter(IspezioneVisivaPiantaDTO ispezioneVisivaPianta)
//      throws InternalUnexpectedException
//  {
//    // TODO Auto-generated method stub
//    return dao.findByFilter(ispezioneVisivaPianta);
//  }
//
//  @Override
//  public List<FotoDTO> findListFotoByIdIspezioneVisiva(Integer id)
//      throws InternalUnexpectedException
//  {
//    // TODO Auto-generated method stub
//    return dao.findFotoByIdIspezioneVisiva(id);
//  }
//
//  @Override
//  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
//  {
//    // TODO Auto-generated method stub
//    return dao.findFotoById(id);
//  }
//  
  @Override
  public void remove(Integer id) throws InternalUnexpectedException
  {
    // TODO Auto-generated method stub
    dao.remove(id);
  }

@Override
public void removePianta(Integer idIspezione, Integer idIspezioneVisivaPianta) throws InternalUnexpectedException {
	// TODO Auto-generated method stub
	
	dao.removePianta(idIspezione,idIspezioneVisivaPianta);
	
}

@Override
public IspezioneVisivaPiantaDTO findByIdIspVisPianta(Integer id) throws InternalUnexpectedException {
	// TODO Auto-generated method stub
	return dao.findByIdIspVisPianta(id);
}

@Override
public List<PositivitaDTO> findPositivita() throws InternalUnexpectedException {
	// TODO Auto-generated method stub
	return dao.findPositivita();
}

@Override
public List<DiametroDTO> findDiametro() throws InternalUnexpectedException {
	// TODO Auto-generated method stub
	return dao.findDiametro();
}

@Override
public PositivitaDTO findPositivitaById(Integer id)
    throws InternalUnexpectedException
{
  // TODO Auto-generated method stub
  return dao.findPositivitaById(id);
}

@Override
public DiametroDTO findDiametroById(Integer id)
    throws InternalUnexpectedException
{
  // TODO Auto-generated method stub
  return dao.findDiametroById(id);
}

}

