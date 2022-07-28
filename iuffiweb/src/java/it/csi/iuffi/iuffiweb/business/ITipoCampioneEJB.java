package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;

public interface ITipoCampioneEJB extends IIuffiAbstractEJB
{

  public TipoCampioneDTO insertTipoCampione(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException;
  
  public void updateTipoCampione(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException;
  
  public List<TipoCampioneDTO> findAll() throws InternalUnexpectedException;
  
  public List<TipoCampioneDTO> findByFilter(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException;
  
  public TipoCampioneDTO findById(Integer idTipoCampione) throws InternalUnexpectedException;
  
  public void remove(Integer idTipoCampione) throws InternalUnexpectedException;

  public List<TipoCampioneDTO> findAllAttivi() throws InternalUnexpectedException;

  public List<TipoCampioneDTO> findValidi() throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idTipoCampione) throws InternalUnexpectedException;

  public List<TipoCampioneDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

}
