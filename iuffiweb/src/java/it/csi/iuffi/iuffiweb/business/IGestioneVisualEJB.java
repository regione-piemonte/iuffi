package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaSpecieOnDTO;
import it.csi.iuffi.iuffiweb.model.api.OrganismiNocivi;
import it.csi.iuffi.iuffiweb.model.request.IspezioneVisivaRequest;

@Local
public interface IGestioneVisualEJB  extends IIuffiAbstractEJB
{
  
  public void update(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException;
  
  public Integer insert(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException;
  
  public List<IspezioneVisivaDTO> findAll() throws InternalUnexpectedException;
  
  public List<IspezioneVisivaDTO> findByFilter(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException;

  public IspezioneVisivaDTO findById(Integer id) throws InternalUnexpectedException;
  
  public List<FotoDTO> findListFotoByIdIspezioneVisiva(Integer id) throws InternalUnexpectedException;
  
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException;
  
  public void remove(Integer id) throws InternalUnexpectedException;
  
  public void insertSpecieOn(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException;
  
  public void updateSpecieOn(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException;
  
  public List<Integer> findOrganismiNociviByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException;
  
  public List<OrganismiNocivi> findOrganismiNociviFlagTrovatoByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException;
  
  public List<IspezioneVisivaDTO> findByFilterFromRequest(IspezioneVisivaRequest ispezioneVisivaRequest,Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException;

  public List<IspezioneVisivaSpecieOnDTO> findOrganismiNociviByVisualCompleto(Integer id) throws InternalUnexpectedException;

  public Integer findCampionamentiCountByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException;
  
  public Integer findTrappolaggiCountByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException;

  public void removeOn(Integer idIspezione, Integer idOrganismoNocivo) throws InternalUnexpectedException;

  public List<IspezioneVisivaSpecieOnDTO> findOnByIdIspezione(Integer id) throws InternalUnexpectedException;
  
  public void insertVisualSpecieOn(IspezioneVisivaSpecieOnDTO visual) throws InternalUnexpectedException;

}
