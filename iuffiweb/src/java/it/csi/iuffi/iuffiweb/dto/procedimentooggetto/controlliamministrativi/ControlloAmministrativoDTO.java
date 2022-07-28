package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ControlloAmministrativoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 2822230875628560921L;
  protected long            idControlloAmministrativo;
  protected Long            idControlloAmministratPadre;
  protected long            idQuadroOggControlloAmm;
  protected long            idEsitoControlliAmm;
  protected String          codice;
  protected long            idEsito;
  protected String          descrizione;
  protected String          codiceEsito;
  protected String          descEsito;
  protected String          note;

  public long getIdControlloAmministrativo()
  {
    return idControlloAmministrativo;
  }

  public void setIdControlloAmministrativo(long idControlloAmministrativo)
  {
    this.idControlloAmministrativo = idControlloAmministrativo;
  }

  public long getIdQuadroOggControlloAmm()
  {
    return idQuadroOggControlloAmm;
  }

  public void setIdQuadroOggControlloAmm(long idQuadroOggeControlloAmm)
  {
    this.idQuadroOggControlloAmm = idQuadroOggeControlloAmm;
  }

  public long getIdEsitoControlliAmm()
  {
    return idEsitoControlliAmm;
  }

  public void setIdEsitoControlliAmm(long idEsitoControlliAmm)
  {
    this.idEsitoControlliAmm = idEsitoControlliAmm;
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

  public String getCodiceEsito()
  {
    return codiceEsito;
  }

  public void setCodiceEsito(String codiceEsito)
  {
    this.codiceEsito = codiceEsito;
  }

  public String getDescEsito()
  {
    return descEsito;
  }

  public void setDescEsito(String descEsito)
  {
    this.descEsito = descEsito;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public Long getIdControlloAmministratPadre()
  {
    return idControlloAmministratPadre;
  }

  public void setIdControlloAmministratPadre(Long idControlloAmministratPadre)
  {
    this.idControlloAmministratPadre = idControlloAmministratPadre;
  }
}
