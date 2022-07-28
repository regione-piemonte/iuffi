package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldCampDTO;

public interface IExtraFieldCampEJB extends IIuffiAbstractEJB
{

  public ExtraFieldCampDTO insert(ExtraFieldCampDTO extraField) throws InternalUnexpectedException;
  
  public void update(ExtraFieldCampDTO extraField) throws InternalUnexpectedException;
  
  public List<ExtraFieldCampDTO> findAll() throws InternalUnexpectedException;
  
  public List<ExtraFieldCampDTO> findByFilter(ExtraFieldCampDTO extraField) throws InternalUnexpectedException;
  
  public ExtraFieldCampDTO findById(Integer idExtraField) throws InternalUnexpectedException;
  
  public void remove(Integer idExtraField) throws InternalUnexpectedException;

}
