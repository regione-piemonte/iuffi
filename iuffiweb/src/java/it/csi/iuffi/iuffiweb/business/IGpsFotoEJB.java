package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.FotoApiDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.GpsDTO;


@Local
public interface IGpsFotoEJB extends IIuffiAbstractEJB
{

  public List<FotoDTO> findAllFoto() throws InternalUnexpectedException;
  
  public FotoDTO findFotoById(Long id) throws InternalUnexpectedException;

  public List<FotoDTO> findFotoByFilter(FotoDTO foto, Integer idAnagrafica, Integer idEnte, String orderBy) throws InternalUnexpectedException;
 
  public List<GpsDTO> findAllGps() throws InternalUnexpectedException;
  
  public GpsDTO findGpsById(Long id) throws InternalUnexpectedException;

  public List<GpsDTO> findGpsByFilter(GpsDTO gps, Integer idAnagrafica, Integer idEnte, String orderBy) throws InternalUnexpectedException;

  public Long insertFoto(FotoDTO foto) throws InternalUnexpectedException;

  public Long insertFotoCampione(Long idFoto,Long idCampione) throws InternalUnexpectedException;
  
  public Long insertFotoVisual(Long idFoto,Long idVisual) throws InternalUnexpectedException;
  
  public Long insertFotoTrappola(Long idFoto,Long idTrappola) throws InternalUnexpectedException;

  public void removeFoto(Integer idFoto) throws InternalUnexpectedException;

  public void removeFotoCampionamento(Integer idFoto,Integer idCampionamento) throws InternalUnexpectedException;
  
  public void removeFotoTrappolaggio(Integer idFoto, Integer idTrappolaggio) throws InternalUnexpectedException;
 
  public void removeFotoIspezione(Integer idFoto, Integer idIspezione) throws InternalUnexpectedException;

  public FotoApiDTO selectFotoToRemoveById(Integer id) throws InternalUnexpectedException;
  
  public List<FotoDTO> selectFotoByIdAzione(FotoApiDTO fotoApi) throws InternalUnexpectedException;
  
  public Long selectFotoCampione(Long id) throws InternalUnexpectedException;
  
  public Long selectFotoVisual(Long id) throws InternalUnexpectedException;
  
  public Long selectFotoTrappola(Long id) throws InternalUnexpectedException;

}
