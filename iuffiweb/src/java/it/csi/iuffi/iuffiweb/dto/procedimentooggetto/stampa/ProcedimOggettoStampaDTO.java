package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProcedimOggettoStampaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -8890282631115750258L;
  protected long            IdProcedimOggettoStampa;
  protected long            idProcedimentoOggetto;
  protected long            idProcedimento;
  protected long            idBandoOggetto;
  protected long            idOggettoIcona;
  protected Long            extIdDocumentoIndex;
  protected long            extIdUtenteAggiornamento;
  protected Date            dataUltimoAggiornamento;
  protected Date            dataInizio;
  protected Date            dataFine;
  protected long            idStatoStampa;
  protected String          descAnomalia;

  public long getIdProcedimOggettoStampa()
  {
    return IdProcedimOggettoStampa;
  }

  public void setIdProcedimOggettoStampa(long idProcedimOggettoStampa)
  {
    IdProcedimOggettoStampa = idProcedimOggettoStampa;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getIdOggettoIcona()
  {
    return idOggettoIcona;
  }

  public void setIdOggettoIcona(long idOggettoIcona)
  {
    this.idOggettoIcona = idOggettoIcona;
  }

  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }

  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
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

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public long getIdStatoStampa()
  {
    return idStatoStampa;
  }

  public void setIdStatoStampa(long idStatoStampa)
  {
    this.idStatoStampa = idStatoStampa;
  }

  public String getDescAnomalia()
  {
    return descAnomalia;
  }

  public void setDescAnomalia(String descAnomalia)
  {
    this.descAnomalia = descAnomalia;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

}
