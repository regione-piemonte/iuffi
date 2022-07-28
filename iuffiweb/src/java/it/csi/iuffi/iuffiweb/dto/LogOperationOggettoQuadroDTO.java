package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class LogOperationOggettoQuadroDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8725780315581568572L;
  protected long            idProcedimentoOggetto;
  protected long            idQuadroOggetto;
  protected long            idBandoOggetto;
  protected long            extIdUtenteAggiornamento;
  protected Date            dataUltimoAggiornamento;

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public long getIdQuadroOggetto()
  {
    return idQuadroOggetto;
  }

  public void setIdQuadroOggetto(long idQuadroOggetto)
  {
    this.idQuadroOggetto = idQuadroOggetto;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

}
