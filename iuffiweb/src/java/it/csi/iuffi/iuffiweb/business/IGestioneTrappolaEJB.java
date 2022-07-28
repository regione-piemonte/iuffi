package it.csi.iuffi.iuffiweb.business;


import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.OperazioneTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.model.api.Trappola;
import it.csi.iuffi.iuffiweb.model.request.TrappolaggioRequest;


@Local
public interface IGestioneTrappolaEJB extends IIuffiAbstractEJB
{
  
  public void update(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException;
  
  public TrappolaggioDTO insert(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException;
  
  public List<TrappolaggioDTO> findAll() throws InternalUnexpectedException;
  
  public List<TipoTrappolaDTO> findTipoTrappolaByFilter(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException;

  public List<TrappolaggioDTO> findByFilter(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException;
  
  public List<TrappolaggioDTO> findByFilterByTrappolaggioRequest(TrappolaggioRequest trappolaggio, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException;

  public TrappolaggioDTO findById(Integer idTrappolaggio) throws InternalUnexpectedException;
  
  public void remove(Integer idTrappolaggio) throws InternalUnexpectedException;
 
  public List<FotoDTO> findListFotoByIdTrappolaggio(Integer idTrappolaggio) throws InternalUnexpectedException;
  
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException;

  public List<OrganismoNocivoDTO> findOnByIdTrappola(Integer idTrappola) throws InternalUnexpectedException;
  
  public TrappolaDTO insertTrappola(TrappolaDTO trappola) throws InternalUnexpectedException;

  public void updateTrappola(TrappolaDTO trappola) throws InternalUnexpectedException;

  public void removeTrappola(Integer idTrappola, Long idUtente) throws InternalUnexpectedException;   // Rimozione trappola
  
  public void eliminaTrappola(Integer idTrappola) throws InternalUnexpectedException;  // Elimina il record

  public TrappolaDTO findTrappolaById(Integer idTrappola) throws InternalUnexpectedException;
  
  public List<Trappola> findTrappolaByCoordinates(Double latitudine, Double longitudine, Integer raggio) throws InternalUnexpectedException;
  
  public Trappola findTrappolaByCodice(String codice) throws InternalUnexpectedException;

  public List<TrappolaggioDTO> findByIdIspezione(Integer idIspezioneVisiva) throws InternalUnexpectedException;
  
  public List<TrappolaggioDTO> findStoriaTrappolaByCodice(String codice) throws InternalUnexpectedException;

  public TrappolaDTO findTrappolaByCodiceSfr(String codice) throws InternalUnexpectedException;
  
  public List<OperazioneTrappolaDTO> findTipoOperazione() throws InternalUnexpectedException;
  
  public List<OperazioneTrappolaDTO> findByIdMultipli(String idConcatenati) throws InternalUnexpectedException;

}
