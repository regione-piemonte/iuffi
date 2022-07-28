package it.csi.iuffi.iuffiweb.integration.ws.regata;

import java.util.ArrayList;
import java.util.List;

public class EsitoRegataMassiveVO {
	
	protected List<EsitoRegataVO> lista_esiti;

	public List<EsitoRegataVO> getLista_esiti() {
		if(lista_esiti==null)
			lista_esiti = new ArrayList<EsitoRegataVO>();
		return lista_esiti;
	}

	public void setLista_esiti(List<EsitoRegataVO> lista_esiti) {
		this.lista_esiti = lista_esiti;
	}
}
