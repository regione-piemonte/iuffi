package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.VelocitaDTO;
import it.csi.iuffi.iuffiweb.model.api.TipoArea;

public interface ITipoAreaEJB extends IIuffiAbstractEJB
{

  public TipoAreaDTO insertTipoArea(TipoAreaDTO tipoArea) throws InternalUnexpectedException;
  
  public void updateTipoArea(TipoAreaDTO tipoArea) throws InternalUnexpectedException;
  
  public List<TipoAreaDTO> findAll() throws InternalUnexpectedException;
  
  public List<TipoAreaDTO> findByFilter(TipoAreaDTO tipoArea) throws InternalUnexpectedException;
  
  public TipoAreaDTO findById(Integer idTipoArea) throws InternalUnexpectedException;
  
  public void remove(Integer idTipoArea) throws InternalUnexpectedException;
  
  public List<TipoArea> getTipoAree() throws InternalUnexpectedException;

  public List<TipoAreaDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

  public List<TipoAreaDTO> findValidi() throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idTipoArea) throws InternalUnexpectedException;

  public List<VelocitaDTO> findVelocita() throws InternalUnexpectedException;

}
