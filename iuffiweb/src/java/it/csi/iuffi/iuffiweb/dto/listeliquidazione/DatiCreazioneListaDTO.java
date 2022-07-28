package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiCreazioneListaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6434992392154956909L;
  protected long            idListaLiquidazione;
  protected String          denominazioneBando;
  protected long            numeroLista;

  public long getIdListaLiquidazione()
  {
    return idListaLiquidazione;
  }

  public void setIdListaLiquidazione(long idListaLiquidazione)
  {
    this.idListaLiquidazione = idListaLiquidazione;
  }

  public long getNumeroLista()
  {
    return numeroLista;
  }

  public void setNumeroLista(long numeroLista)
  {
    this.numeroLista = numeroLista;
  }

  public String getDenominazioneBando()
  {
    return denominazioneBando;
  }

  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }
}
