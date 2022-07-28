package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldTrapDTO;

public interface IExtraFieldTrapEJB extends IIuffiAbstractEJB
{

  public ExtraFieldTrapDTO insert(ExtraFieldTrapDTO extraField) throws InternalUnexpectedException;
  
  public void update(ExtraFieldTrapDTO extraField) throws InternalUnexpectedException;
  
  public List<ExtraFieldTrapDTO> findAll() throws InternalUnexpectedException;
  
  public List<ExtraFieldTrapDTO> findByFilter(ExtraFieldTrapDTO extraField) throws InternalUnexpectedException;
  
  public ExtraFieldTrapDTO findById(Integer idExtraField) throws InternalUnexpectedException;
  
  public void remove(Integer idExtraField) throws InternalUnexpectedException;

}
