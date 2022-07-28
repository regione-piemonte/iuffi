package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnfiDTO;
import it.csi.iuffi.iuffiweb.model.CampioneEnteDTO;

public interface ICampioneEnteEJB extends IIuffiAbstractEJB
{
  
  public CampioneEnteDTO  insert(CampioneEnteDTO model) throws InternalUnexpectedException;
  
  public void update(CampioneEnteDTO model) throws InternalUnexpectedException;
  
  public List<CampioneEnteDTO> findAll() throws InternalUnexpectedException;
  
  public List<CampioneEnteDTO> findByFilter(CampioneEnteDTO model) throws InternalUnexpectedException;
  
  public CampioneEnteDTO findById(Integer id) throws InternalUnexpectedException;
  
  public void remove(Integer idCampioneEnte) throws InternalUnexpectedException;

  public List<CampioneEnteDTO> findValidi() throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idCampioneEnte) throws InternalUnexpectedException;

}
