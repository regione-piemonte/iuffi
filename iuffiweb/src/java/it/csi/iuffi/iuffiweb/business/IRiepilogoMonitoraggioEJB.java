package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.RiepilogoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;

public interface IRiepilogoMonitoraggioEJB extends IIuffiAbstractEJB
{

  public List<RiepilogoMonitoraggioDTO> findAll() throws InternalUnexpectedException;
  
  public List<RiepilogoMonitoraggioDTO> save(RiepilogoMonitoraggioDTO riepilogo) throws InternalUnexpectedException;
  
  public List<RiepilogoMonitoraggio> getRiepilogoMonitoraggio() throws InternalUnexpectedException;

  public RiepilogoMonitoraggioDTO findBySpecieVegetaleTipoCampioneAndMesi(Integer idSpecieVegetale, Integer idTipoCampione, String mesi) throws InternalUnexpectedException;

  public RiepilogoMonitoraggio findByUniqueKey(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, Integer mese) throws InternalUnexpectedException;

  public RiepilogoMonitoraggio insert(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException;

  public void update(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException;
  
  public void storicizza(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException;
  
  public void removeMesi(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, String mesi, Long idUtente) throws InternalUnexpectedException;
  
  public void removeOrganismoNocivo(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, Long idUtente) throws InternalUnexpectedException;

  public void remove(Integer idSpecieVegetale, Integer idTipoCampione, String mesi, String on, Long idUtente) throws InternalUnexpectedException;
  
  public Long getMaxCountON() throws InternalUnexpectedException;

  public List<RiepilogoMonitoraggio> findByIdOrganismoNocivo(Integer idOrganismoNocivo) throws InternalUnexpectedException;

  public List<RiepilogoMonitoraggio> findByIdSpecieVegetale(Integer idSpecieVegetale) throws InternalUnexpectedException;

  public List<RiepilogoMonitoraggio> findByIdTipoCampione(Integer idTipoCampione) throws InternalUnexpectedException;

}
