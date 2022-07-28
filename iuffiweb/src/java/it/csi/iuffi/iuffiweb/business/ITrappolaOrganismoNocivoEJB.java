package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TrappolaOnDTO;

public interface ITrappolaOrganismoNocivoEJB extends IIuffiAbstractEJB
{
  
  public TrappolaOnDTO  insert(TrappolaOnDTO model) throws InternalUnexpectedException;
  
  public void update(TrappolaOnDTO model) throws InternalUnexpectedException;
  
  public List<TrappolaOnDTO> findAll() throws InternalUnexpectedException;
  
  public List<TrappolaOnDTO> findByFilter(TrappolaOnDTO model) throws InternalUnexpectedException;
  
  public TrappolaOnDTO findById(Integer id) throws InternalUnexpectedException;
  
  public void remove(Integer idTrappolaOn) throws InternalUnexpectedException;

  public List<TrappolaOnDTO> findValidi() throws InternalUnexpectedException;
  
  public void updateDataFineValidita(Integer idTrappolaOn) throws InternalUnexpectedException;

  public List<TrappolaOnDTO> findByIdOn(Integer id) throws InternalUnexpectedException;

  public List<TrappolaOnDTO> findByIdTrappola(Integer id) throws InternalUnexpectedException;

}
