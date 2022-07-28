package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;

public interface IOrganismoNocivoEJB extends IIuffiAbstractEJB
{
  public OrganismoNocivoDTO insertOrganismoNocivo(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException;
  
  public void updateOrganismoNocivo(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException;
  
  public List<OrganismoNocivoDTO> findAll() throws InternalUnexpectedException;
  
  public List<OrganismoNocivoDTO> findByFilter(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException;
  
  public OrganismoNocivoDTO findById(Integer idOrganismoNocivo) throws InternalUnexpectedException;
  
  public void remove(Integer idOrganismoNocivo) throws InternalUnexpectedException;

  public List<OrganismoNocivoDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

  public void updateDataFineValidita(Integer idOrganismoNocivo) throws InternalUnexpectedException;

  public List<OrganismoNocivoDTO> findValidi() throws InternalUnexpectedException;
  
  public List<OrganismoNocivoDTO> findByPianificazione() throws InternalUnexpectedException;

}
