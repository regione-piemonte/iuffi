package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.PianoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.PrevisioneMonitoraggioDTO;

public interface IPrevisioneMonitoraggioEJB extends IIuffiAbstractEJB
{

  public List<PrevisioneMonitoraggioDTO> findAll() throws InternalUnexpectedException;
  
  public PrevisioneMonitoraggioDTO savePrevisione(PrevisioneMonitoraggioDTO previsione, Long idUtenteLogin) throws InternalUnexpectedException;

  public List<PianoMonitoraggioDTO> findPianiAll() throws InternalUnexpectedException;
  
  public List<PianoMonitoraggioDTO> findPianiByFilter(PianoMonitoraggioDTO piano) throws InternalUnexpectedException;
  
  public PianoMonitoraggioDTO findPianoById(Integer idPianoMonitoraggio) throws InternalUnexpectedException;
  
  public PianoMonitoraggioDTO insertPiano(PianoMonitoraggioDTO piano) throws InternalUnexpectedException;
  
  public PianoMonitoraggioDTO updatePiano(PianoMonitoraggioDTO piano) throws InternalUnexpectedException;
  
  public void removePiano(Integer idPianoMonitoraggio) throws InternalUnexpectedException;
  
  public void updateDataFineValidita(Integer idPianoMonitoraggio) throws InternalUnexpectedException;
  
  public List<PrevisioneMonitoraggioDTO> findPrevisioneByIdPiano(Integer idPianoMonitoraggio) throws InternalUnexpectedException;

  public long countPrevisioniByIdOrganismoNocivo(Integer idOrganismoNocivo) throws InternalUnexpectedException;

}
