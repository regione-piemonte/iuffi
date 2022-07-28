package it.csi.iuffi.iuffiweb.integration.ws.regata;

import java.util.List;

public class PayloadMassiveDTO {
	private CredenzialiDTO credenziali;
	
	protected List<DatiAziendaDTO> datiAzienda;

	public CredenzialiDTO getCredenziali() {
		return credenziali;
	}

	public void setCredenziali(CredenzialiDTO credenziali) {
		this.credenziali = credenziali;
	}

	public List<DatiAziendaDTO> getDatiAzienda() {
		return datiAzienda;
	}

	public void setDatiAzienda(List<DatiAziendaDTO> datiAzienda) {
		this.datiAzienda = datiAzienda;
	}
}
