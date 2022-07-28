package it.csi.iuffi.iuffiweb.integration.ws.regata;

import java.io.Serializable;

public class EsitoRegataVO implements Serializable{
	private static final long serialVersionUID = 1183154383888694885L;
	
	private Long idRichiesta;
	private Long idControlliInterni;
	private String msg;

	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Long getIdControlliInterni() {
		return idControlliInterni;
	}
	public void setIdControlliInterni(Long idControlliInterni) {
		this.idControlliInterni = idControlliInterni;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
