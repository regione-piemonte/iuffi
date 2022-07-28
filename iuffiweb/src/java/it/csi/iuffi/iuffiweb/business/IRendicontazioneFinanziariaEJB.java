package it.csi.iuffi.iuffiweb.business;


import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RendicontazioneDTO;


@Local
public interface IRendicontazioneFinanziariaEJB extends IIuffiAbstractEJB
{

  public List<RendicontazioneDTO> findAll() throws InternalUnexpectedException;
  public List<RendicontazioneDTO> findByFilter(RendicontazioneDTO rendicontazione) throws InternalUnexpectedException;
  public List<OrganismoNocivoDTO> findOrganismiNocivi(RendicontazioneDTO rendicontazione) throws InternalUnexpectedException;

}
