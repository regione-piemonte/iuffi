package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaEnteDTO;

public interface ITrappolaEnteEJB extends IIuffiAbstractEJB
{
  
  public TrappolaEnteDTO  insert(TrappolaEnteDTO model) throws InternalUnexpectedException;
  
  public void update(TrappolaEnteDTO model) throws InternalUnexpectedException;
  
  public List<TrappolaEnteDTO> findAll() throws InternalUnexpectedException;
  
  public List<TrappolaEnteDTO> findByFilter(TrappolaEnteDTO model) throws InternalUnexpectedException;
  
  public TrappolaEnteDTO findById(Integer id) throws InternalUnexpectedException;
  
  public void remove(Integer idTrappolaEnte) throws InternalUnexpectedException;

  public List<TrappolaEnteDTO> findValidi() throws InternalUnexpectedException;
  
  public void updateDataFineValidita(Integer idTrappolaEnte) throws InternalUnexpectedException;

  public List<TrappolaEnteDTO> findByIdEnte(Integer id) throws InternalUnexpectedException;

  public List<TrappolaEnteDTO> findByIdTrappola(Integer id) throws InternalUnexpectedException;
}
