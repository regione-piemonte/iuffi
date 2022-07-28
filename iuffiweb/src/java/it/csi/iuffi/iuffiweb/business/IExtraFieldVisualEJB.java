package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldVisualDTO;

public interface IExtraFieldVisualEJB extends IIuffiAbstractEJB
{

  public ExtraFieldVisualDTO insert(ExtraFieldVisualDTO extraField) throws InternalUnexpectedException;
  
  public void update(ExtraFieldVisualDTO extraField) throws InternalUnexpectedException;
  
  public List<ExtraFieldVisualDTO> findAll() throws InternalUnexpectedException;
  
  public List<ExtraFieldVisualDTO> findByFilter(ExtraFieldVisualDTO extraField) throws InternalUnexpectedException;
  
  public ExtraFieldVisualDTO findById(Integer idExtraField) throws InternalUnexpectedException;
  
  public void remove(Integer idExtraField) throws InternalUnexpectedException;

}
