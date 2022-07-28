package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.Missione;
import it.csi.iuffi.iuffiweb.model.request.MissioneRequest;

@Local
public interface IMissioneEJB extends IIuffiAbstractEJB
{

  public Long insertMissione(MissioneDTO missione) throws InternalUnexpectedException;
  
  public void updateMissione(MissioneDTO missione) throws InternalUnexpectedException;
  
  public List<MissioneDTO> findAll() throws InternalUnexpectedException;
  
  public MissioneDTO findById(Long idMissione) throws InternalUnexpectedException;
  
  public List<AnagraficaDTO> getIspettoriAggiunti(Long idMissione) throws InternalUnexpectedException;
  
  public List<MissioneDTO> findByFilter(MissioneRequest mr, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException;
  
  public List<Missione> getMissioni(MissioneRequest mr) throws InternalUnexpectedException;
  
  public void deleteMissione(Long idMissione) throws InternalUnexpectedException;
  
  public byte[] getPdfTrasferta(Long idMissione) throws InternalUnexpectedException;
  
  public List<MissioneDTO> findByDateAndIspettore(String dataOraInizio, Integer idIspettore, Long idMissione) throws InternalUnexpectedException;
  
  public List<MissioneDTO> findMissioneForVerbali(MissioneRequest mr, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException;
  
  public List<VerbaleDTO> findVerbaliByMissione(MissioneRequest mr) throws InternalUnexpectedException;
  
  public int getIdStatoVerbaleByIdMissione(long idMissione) throws InternalUnexpectedException;

}
