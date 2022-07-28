package it.csi.iuffi.iuffiweb.presentation.quadro.coltureaziendali;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.superficicolture.ControlloColturaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;


public abstract class CUIUFFI305BaseController extends BaseController
{
	  @Autowired
	  protected IQuadroIuffiEJB quadroIuffiEJB = null;  
	
	  protected Map<Long,StringBuilder> getMapIdSuperficieColturaAnomalia(long idProcedimentoOggetto, HttpServletRequest request) throws InternalUnexpectedException
	  {
		  Map<Long,StringBuilder> mapIdSuperficieColturaAnomalia = new HashMap<Long,StringBuilder>();
		  List<ControlloColturaDTO> elencoControlloColtura = quadroIuffiEJB.getListControlloColtura(idProcedimentoOggetto, getArrayIdSuperficieColtura(request));
		  for(ControlloColturaDTO cc : elencoControlloColtura)
		  {
			  if(mapIdSuperficieColturaAnomalia.containsKey(cc.getIdSuperficieColtura()))
			  {
				  mapIdSuperficieColturaAnomalia
				  		.get(cc.getIdSuperficieColtura())
				  		.append("; ")
				  		.append(cc.getDescrizioneAnomalia());
			  }
			  else
			  {
				  StringBuilder sb = new StringBuilder(cc.getDescrizioneAnomalia());
				  mapIdSuperficieColturaAnomalia.put(cc.getIdSuperficieColtura(), sb);
				  Map<String,Boolean> map = new HashMap<String,Boolean>();
				  if(cc.getBloccante() != null && !cc.getBloccante().equals(""))
				  {
					  map.put(cc.getBloccante(),Boolean.TRUE);
				  }
			  }
		  }
		  return mapIdSuperficieColturaAnomalia;
	  }
	  
	  protected abstract long[] getArrayIdSuperficieColtura( HttpServletRequest request);
}
