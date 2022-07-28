package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ControlloAmministEsitoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8048050932779352119L;
  protected long            idControlloAmministrativo;
  protected long            idEsito;
  protected String          codice;
  protected String          descrizione;

  public long getIdControlloAmministrativo()
  {
    return idControlloAmministrativo;
  }

  public void setIdControlloAmministrativo(long idControlloAmministrativo)
  {
    this.idControlloAmministrativo = idControlloAmministrativo;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
}
