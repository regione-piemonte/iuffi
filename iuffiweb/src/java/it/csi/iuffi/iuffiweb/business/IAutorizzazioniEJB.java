package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AutorizzazioniDTO;

@Local
public interface IAutorizzazioniEJB extends IIuffiAbstractEJB
{
  public List<AutorizzazioniDTO> findById(long id) throws InternalUnexpectedException;
  public List<AutorizzazioniDTO> findByIdLivelloAndReadOnly(long idLivello) throws InternalUnexpectedException;
}
