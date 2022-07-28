package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoSpecOnDTO;
import it.csi.iuffi.iuffiweb.model.CodiceEsitoDTO;
import it.csi.iuffi.iuffiweb.model.EsitoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.request.CampionamentoRequest;


@Local
public interface IGestioneCampioneEJB extends IIuffiAbstractEJB
{
  
  public void update(CampionamentoDTO campione) throws InternalUnexpectedException;
  
  public Integer insert(CampionamentoDTO campione) throws InternalUnexpectedException;
  
  public List<CampionamentoDTO> findAll() throws InternalUnexpectedException;
  
  public List<CampionamentoDTO> findByFilter(CampionamentoRequest campionamentoRequest, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException;

  public List<CampionamentoDTO> findByIdRilevazione(Integer idRilevazione) throws InternalUnexpectedException;

  public CampionamentoDTO findById(Integer idCampionamento) throws InternalUnexpectedException;
  
  public void removeEsito(Integer idEsitoCampione) throws InternalUnexpectedException;

  public List<FotoDTO> findListFotoByIdCampione(Integer idCampionamento) throws InternalUnexpectedException;
  
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException;

  public List<CampionamentoSpecOnDTO> findOnByIdCampionamento(Integer idCampionamento) throws InternalUnexpectedException;

  public List<EsitoCampioneDTO> findEsitiByIdCampionamento(Integer idCampionamento) throws InternalUnexpectedException;

  public EsitoCampioneDTO findEsitoById(Integer idEsitoCampione) throws InternalUnexpectedException;
  
  public EsitoCampioneDTO insertEsito(EsitoCampioneDTO esitoCampione) throws InternalUnexpectedException;
  
  public void updateEsito(EsitoCampioneDTO esitoCampione) throws InternalUnexpectedException;
  
  public void remove(Integer id) throws InternalUnexpectedException;
  
  public void insertCampioneSpecieOn(CampionamentoSpecOnDTO campione) throws InternalUnexpectedException;
  
  public void updateCampioneSpecieOn(CampionamentoDTO campione) throws InternalUnexpectedException;
  
  public void removeCampioneSpecieOn(Integer idCampionamento) throws InternalUnexpectedException;
  
  public void removeOn(Integer idCampionamento, Integer idOrganismoNocivo) throws InternalUnexpectedException;
  
  public List<CampionamentoDTO> findByIdIspezione(Integer idIspezioneVisiva) throws InternalUnexpectedException;
  
  public List<CodiceEsitoDTO> findValidiCodiciEsito() throws InternalUnexpectedException;
  
  public List<CodiceEsitoDTO> findAllCodiciEsito() throws InternalUnexpectedException;
  
  public CodiceEsitoDTO findCodiceEsitoById(Integer id) throws InternalUnexpectedException;
  
  public List<CodiceEsitoDTO> findByIdMultipliCodiciEsito(String id) throws InternalUnexpectedException;
  
  public byte[] getPdf(Integer id) throws InternalUnexpectedException;
}
