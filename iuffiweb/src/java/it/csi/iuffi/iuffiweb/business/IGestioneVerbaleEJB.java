package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.VerbaleCampioni;
import it.csi.iuffi.iuffiweb.model.api.VerbaleTrappole;
import it.csi.iuffi.iuffiweb.model.api.VerbaleVisual;
import it.csi.iuffi.iuffiweb.model.request.VerbaleDati;


@Local
public interface IGestioneVerbaleEJB extends IIuffiAbstractEJB
{
  
  public void update(VerbaleDTO verbale) throws InternalUnexpectedException;
  
  public VerbaleDTO insert(VerbaleDTO verbale) throws InternalUnexpectedException;
  
  public List<VerbaleDTO> findAll() throws InternalUnexpectedException;
  
  public List<VerbaleDTO> findByFilter(VerbaleDTO verbale) throws InternalUnexpectedException;

  public VerbaleDTO findById(Integer idVerbale) throws InternalUnexpectedException;
  
  public void remove(Integer id) throws InternalUnexpectedException;
  
  public List<VerbaleVisual> getVisual(Integer id) throws InternalUnexpectedException;
  
  public List<VerbaleCampioni> getCampioni(Integer id) throws InternalUnexpectedException;
  
  public List<VerbaleTrappole> getTrappole(Integer id) throws InternalUnexpectedException;

  public VerbaleDati getIntestazione(Integer id) throws InternalUnexpectedException;
  
  public VerbaleDTO findByIdMissione(Integer idMissione) throws InternalUnexpectedException;
  
  public byte[] getPdfVerbale(Integer idVerbale) throws InternalUnexpectedException;
  
  public List<VerbaleVisual> getVisualDiCompetenza(Integer idMissione, Integer idAnagrafica) throws InternalUnexpectedException;
  
  public List<VerbaleCampioni> getCampioniDiCompetenza(Integer idMissione, Integer idAnagrafica) throws InternalUnexpectedException;
  
  public List<VerbaleTrappole> getTrappoleDiCompetenza(Integer idMissione, Integer idAnagrafica) throws InternalUnexpectedException;

  public void updateStatoVerbale(VerbaleDTO verbale) throws InternalUnexpectedException;
  
  public Integer getProgVerbale(String pattern, String escape) throws InternalUnexpectedException;

}
