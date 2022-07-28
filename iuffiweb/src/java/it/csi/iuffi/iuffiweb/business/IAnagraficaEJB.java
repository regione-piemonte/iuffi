package it.csi.iuffi.iuffiweb.business;


import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.EnteDTO;


@Local
public interface IAnagraficaEJB extends IIuffiAbstractEJB
{
  
  public void updateAnagrafica(AnagraficaDTO anagrafica) throws InternalUnexpectedException;
  
  public AnagraficaDTO insertAnagrafica(AnagraficaDTO anagrafica) throws InternalUnexpectedException;
  
  public List<AnagraficaDTO> findAll() throws InternalUnexpectedException;
  
  public List<AnagraficaDTO> findByFilter(AnagraficaDTO anagrafica) throws InternalUnexpectedException;

  public AnagraficaDTO findById(Integer idAnagrafica) throws InternalUnexpectedException;
  
  public void remove(Integer idAnagrafica) throws InternalUnexpectedException;

  public List<EnteDTO> getEnti() throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idAnagrafica) throws InternalUnexpectedException;

  public List<AnagraficaDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

  public List<AnagraficaDTO> findValidi() throws InternalUnexpectedException;
  
  public AnagraficaDTO getIdAnagraficaFromCf(String cf) throws InternalUnexpectedException;

  public EnteDTO findEnteById(Integer idEnte) throws InternalUnexpectedException;
  
  public EnteDTO findEnteByIdPapua(Integer idEntePapua) throws InternalUnexpectedException;
  
  public EnteDTO insertEnte(EnteDTO ente) throws InternalUnexpectedException;
  
  public List<AnagraficaDTO> findValidiMissione(Integer idMissione) throws InternalUnexpectedException;

}
