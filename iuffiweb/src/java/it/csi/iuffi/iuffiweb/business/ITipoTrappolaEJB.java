package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;

public interface ITipoTrappolaEJB extends IIuffiAbstractEJB
{
  
  public TipoTrappolaDTO insertTipoTrappola(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException;
  
  public void updateTipoTrappola(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException;
  
  public List<TipoTrappolaDTO> findAll() throws InternalUnexpectedException;
  
  public List<TipoTrappolaDTO> findByFilter(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException;
  
  public TipoTrappolaDTO findById(Integer idTipoTrappola) throws InternalUnexpectedException;
  
  public void remove(Integer idTipoTrappola) throws InternalUnexpectedException;

  public List<TipoTrappolaDTO> findValidi() throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idTipoTrappola) throws InternalUnexpectedException;

  public List<TipoTrappolaDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

}
