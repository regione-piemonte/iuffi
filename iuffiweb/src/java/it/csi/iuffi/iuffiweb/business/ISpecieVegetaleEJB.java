package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.api.GenereSpecieVegetale;

@Local
public interface ISpecieVegetaleEJB extends IIuffiAbstractEJB
{
  
  public void updateSpecieVegetale(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException;
  
  public SpecieVegetaleDTO insertSpecieVegetale(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException;
  
  public List<SpecieVegetaleDTO> findAll() throws InternalUnexpectedException;
  
  public List<SpecieVegetaleDTO> findByFilter(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException;

  public SpecieVegetaleDTO findById(Integer idSpecieVegetale) throws InternalUnexpectedException;
  
  public void remove(Integer idSpecieVegetale) throws InternalUnexpectedException;

  public List<GenereSpecieVegetale> getSpecieVegetali() throws InternalUnexpectedException;

  public List<SpecieVegetaleDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idSpecieVegetale) throws InternalUnexpectedException;

  public List<SpecieVegetaleDTO> findValidi() throws InternalUnexpectedException;

}
