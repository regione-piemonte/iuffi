package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.AnfiDTO;

@Local
public interface IAnfiEJB extends IIuffiAbstractEJB
{

  public AnfiDTO insertAnfi(AnfiDTO anfi) throws InternalUnexpectedException;
  
  public void updateAnfi(AnfiDTO anfi) throws InternalUnexpectedException;
  
  public List<AnfiDTO> findAll() throws InternalUnexpectedException;
  
  public List<AnfiDTO> findByFilter(AnfiDTO anfi) throws InternalUnexpectedException;
  
  public AnfiDTO findById(Integer idAnfi) throws InternalUnexpectedException;
  
  public void remove(Integer idAnfi) throws InternalUnexpectedException;

  public List<AnfiDTO> findValidi() throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idAnfi) throws InternalUnexpectedException;

  public List<AnfiDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

}
