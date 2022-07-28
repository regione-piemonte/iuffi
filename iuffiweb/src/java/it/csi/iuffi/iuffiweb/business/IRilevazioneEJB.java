package it.csi.iuffi.iuffiweb.business;

import java.util.List;


import javax.ejb.Local;


import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.api.Rilevazione;



@Local
public interface IRilevazioneEJB extends IIuffiAbstractEJB
{
  
  public void updateFilter(RilevazioneDTO rilevazione) throws InternalUnexpectedException;
  
  public void update(RilevazioneDTO rilevazione) throws InternalUnexpectedException;
  
  public Integer insert(RilevazioneDTO rilevazione) throws InternalUnexpectedException;
  
  //public RilevazioneDTO insert(RilevazioneDTO rilevazione) throws InternalUnexpectedException;
  
  public List<RilevazioneDTO> findAll() throws InternalUnexpectedException;
  
  public List<RilevazioneDTO> findByFilter(RilevazioneDTO rilevazione) throws InternalUnexpectedException;
  
  public List<Rilevazione> findByFilterForApi(RilevazioneDTO rilevazione) throws InternalUnexpectedException;

  public RilevazioneDTO findById(Integer id) throws InternalUnexpectedException;
  
  public void remove(Integer id) throws InternalUnexpectedException;

}
