package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.DiametroDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaPiantaDTO;
import it.csi.iuffi.iuffiweb.model.PositivitaDTO;

@Local
public interface IIspezioneVisivaPiantaEJB  extends IIuffiAbstractEJB
{
  
  public void update(IspezioneVisivaPiantaDTO ispezioneVisivaPianta) throws InternalUnexpectedException;
  
  public Integer insert(IspezioneVisivaPiantaDTO ispezioneVisivaPianta) throws InternalUnexpectedException;
  
  public List<IspezioneVisivaPiantaDTO> findAll() throws InternalUnexpectedException;

  public IspezioneVisivaPiantaDTO findById(Integer id) throws InternalUnexpectedException;
  
  public List<IspezioneVisivaPiantaDTO> findByIdIspezioneVisiva(Integer id) throws InternalUnexpectedException;
  
//  public List<IspezioneVisivaPiantaDTO> findByFilter(IspezioneVisivaPiantaDTO ispezioneVisivaPianta) throws InternalUnexpectedException;
//  
//  public List<FotoDTO> findListFotoByIdIspezioneVisiva(Integer id) throws InternalUnexpectedException;
//  
//  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException;
//  
  public void remove(Integer id) throws InternalUnexpectedException;
  
  public void removePianta(Integer idIspezione, Integer idIspezioneVisivaPianta) throws InternalUnexpectedException;
  
  public IspezioneVisivaPiantaDTO findByIdIspVisPianta(Integer id) throws InternalUnexpectedException;

  public List<PositivitaDTO> findPositivita() throws InternalUnexpectedException;
  
  public List<DiametroDTO> findDiametro() throws InternalUnexpectedException;
  
  public PositivitaDTO findPositivitaById(Integer id) throws InternalUnexpectedException;
  
  public DiametroDTO findDiametroById(Integer id) throws InternalUnexpectedException;


}
