package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.MenuItemDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IMenuItemEJB extends IIuffiAbstractEJB
{
  
  List<MenuItemDTO> getMainMenu(int idParent, long[] idLivelli, boolean readOnly) throws InternalUnexpectedException;

  List<MenuItemDTO> getBreadcrumbs(int idMenuItem) throws InternalUnexpectedException;
  
  List<MenuItemDTO> getBreadcrumbs(String path) throws InternalUnexpectedException;

}
