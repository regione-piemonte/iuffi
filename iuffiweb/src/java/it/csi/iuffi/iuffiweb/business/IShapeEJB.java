package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ShapeDTO;


@Local
public interface IShapeEJB extends IIuffiAbstractEJB
{

  public ShapeDTO insert(ShapeDTO shape) throws InternalUnexpectedException;

  public List<ShapeDTO> findAll() throws InternalUnexpectedException;

  public List<ShapeDTO> findValidi() throws InternalUnexpectedException;

  public List<ShapeDTO> findByType(List<Integer> organismi, String tipoAttività, String type) throws InternalUnexpectedException;

  public void deleteByOn(Integer idOrganismoNocivo) throws InternalUnexpectedException;

  public void deleteByOnAndTipoAttivita(Integer idOrganismoNocivo, Long idTipoAttivita) throws InternalUnexpectedException;

}
