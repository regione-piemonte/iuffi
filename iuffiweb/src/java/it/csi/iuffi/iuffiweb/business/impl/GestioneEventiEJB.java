package it.csi.iuffi.iuffiweb.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IGestioneEventiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.GestioneEventiDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

@Stateless()
@EJB(name = "java:app/GestioneEventi", beanInterface = IGestioneEventiEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GestioneEventiEJB extends IuffiAbstractEJB<GestioneEventiDAO> 
	implements IGestioneEventiEJB {
	private SessionContext sessionContext;

	  @Resource
	  private void setSessionContext(SessionContext sessionContext)
	  {
	    this.sessionContext = sessionContext;
	  }
	
	@Override
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<EventiDTO> getListEventiDisponibili() throws InternalUnexpectedException {
		List<EventiDTO> list = dao.getListEventoCalamitoso();
		if(list == null)
		{
			list = new ArrayList<EventiDTO>();
		}
		return list;
	}

	@Override
	public List<DecodificaDTO<Long>> getListDecodificaCategorieEvento() throws InternalUnexpectedException 
	{
		List<DecodificaDTO<Long>> list = dao.getListDecodificaCategorieEvento();
		if(list == null)
		{
			list = new ArrayList<DecodificaDTO<Long>>();
		}
		return list;
	}
	
	@Override
	public List<DecodificaDTO<Long>> getListDecodificaCategorieEvento(long[] arrayIdCategoriaEvento)
			throws InternalUnexpectedException {
		List<DecodificaDTO<Long>> list = dao.getListDecodificaCategorieEvento(arrayIdCategoriaEvento);
		if(list == null)
		{
			list = new ArrayList<DecodificaDTO<Long>>();
		}
		return list;
	}

	@Override
	public long inserisciEventoCalamitoso(EventiDTO evento) throws InternalUnexpectedException 
	{
		return dao.inserisciEvento(evento);
	}

	@Override
	public EventiDTO getEventoCalamitoso(long idEventoCalamitoso) throws InternalUnexpectedException {
		return dao.getListEventoCalamitoso(new long[]{idEventoCalamitoso}).get(0);
	}
	
	@Override	
	public int modificaEventoCalamitoso(EventiDTO evento) throws InternalUnexpectedException, ApplicationException
	{
		dao.lockEventoCalamitoso(evento.getIdEventoCalamitoso());
		long n = dao.getNBandiAssociatiAdEvento(evento.getIdEventoCalamitoso());
		if(n == 0L)
		{
			if(dao.updateEventoCalamitoso(evento) > 0L)
			{
				return 0;
			}
		}
		else
		{
			throw new ApplicationException("Impossibile modificare l'evento atmosferico. Esistono dei bandi associati all'evento.");
		}
		return -1;
	}
	
	@Override
	public long getNBandiAssociatiAdEvento(long idEventoCalamitoso) throws InternalUnexpectedException
	{
		return dao.getNBandiAssociatiAdEvento(idEventoCalamitoso);
	}
	
	@Override
	public long eliminaEventoCalamitoso(long idEventoCalamitoso) throws InternalUnexpectedException, ApplicationException
	{
		dao.lockEventoCalamitoso(idEventoCalamitoso);
		long nBandi = getNBandiAssociatiAdEvento(idEventoCalamitoso);
		if(nBandi > 0L)
		{
			return IuffiConstants.ERRORI.EVENTI_CALAMITOSI.BANDI_ESISTENTI;
		}
		else
		{
			return dao.eliminaEventoCalamitoso(idEventoCalamitoso);
		}
	}

	@Override
	public List<DecodificaDTO<Long>> getListEventiCalamitosi(long idCategoriaEvento)
			throws InternalUnexpectedException {
		List<DecodificaDTO<Long>> list = dao.getListEventoCalamitoso(idCategoriaEvento);
		if(list == null)
		{
			list = new ArrayList<DecodificaDTO<Long>>();
		}
		return list;
	}
	
	@Override
	public List<DecodificaDTO<Long>> getListEventoCalamitoso(long idCategoriaEvento, long[] arrayIdEventoCalamitoso) throws InternalUnexpectedException
	{
		List<DecodificaDTO<Long>> list = dao.getListEventoCalamitoso(idCategoriaEvento, arrayIdEventoCalamitoso);
		if(list == null)
		{
			list = new ArrayList<DecodificaDTO<Long>>();
		}
		return list;
	}



	

}
