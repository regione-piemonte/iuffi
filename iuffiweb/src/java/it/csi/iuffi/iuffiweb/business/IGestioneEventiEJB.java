package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IGestioneEventiEJB extends IIuffiAbstractEJB
{
	public List<EventiDTO> getListEventiDisponibili() throws InternalUnexpectedException;
	
	public List<DecodificaDTO<Long>> getListDecodificaCategorieEvento () throws InternalUnexpectedException;
	
	public List<DecodificaDTO<Long>> getListDecodificaCategorieEvento (long[] arrayIdCategoriaEvento) throws InternalUnexpectedException;

	public long inserisciEventoCalamitoso(EventiDTO evento) throws InternalUnexpectedException;

	public EventiDTO getEventoCalamitoso(long idEventoCalamitoso) throws InternalUnexpectedException;

	public int modificaEventoCalamitoso(EventiDTO evento) throws InternalUnexpectedException, ApplicationException;
	
	public long getNBandiAssociatiAdEvento(long idEventoCalamitoso) throws InternalUnexpectedException;
	
	public long eliminaEventoCalamitoso(long idEventoCalamitoso) throws InternalUnexpectedException, ApplicationException;

	public List<DecodificaDTO<Long>> getListEventiCalamitosi(long idCategoriaEvento) throws InternalUnexpectedException;
	
	public List<DecodificaDTO<Long>> getListEventoCalamitoso(long idCategoriaEvento, long[] arrayIdEventoCalamitoso) throws InternalUnexpectedException;
	
}
