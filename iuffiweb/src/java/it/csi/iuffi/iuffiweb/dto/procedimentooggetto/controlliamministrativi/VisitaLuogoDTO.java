package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class VisitaLuogoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -4065008766920158339L;
  private long              idVisitaLuogo;
  private Date              dataVisita;
  private long              extIdTecnico;
  private long              idEsito;
  private long              idProcedimentoOggetto;
  private String            note;
  private Date              dataVerbale;
  private String            numeroVerbale;
  protected long            idQuadroOggetto;

  public VisitaLuogoDTO()
  {
    super();
  }

  public long getIdVisitaLuogo()
  {
    return idVisitaLuogo;
  }

  public void setIdVisitaLuogo(long idVisitaLuogo)
  {
    this.idVisitaLuogo = idVisitaLuogo;
  }

  public Date getDataVisita()
  {
    return dataVisita;
  }

  public void setDataVisita(Date dataVisita)
  {
    this.dataVisita = dataVisita;
  }

  public long getExtIdTecnico()
  {
    return extIdTecnico;
  }

  public void setExtIdTecnico(long extIdTecnico)
  {
    this.extIdTecnico = extIdTecnico;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Date getDataVerbale()
  {
    return dataVerbale;
  }

  public void setDataVerbale(Date dataVerbale)
  {
    this.dataVerbale = dataVerbale;
  }

  public String getNumeroVerbale()
  {
    return numeroVerbale;
  }

  public void setNumeroVerbale(String numeroVerbale)
  {
    this.numeroVerbale = numeroVerbale;
  }

  public long getIdQuadroOggetto()
  {
    return idQuadroOggetto;
  }

  public void setIdQuadroOggetto(long idQuadroOggetto)
  {
    this.idQuadroOggetto = idQuadroOggetto;
  }
}