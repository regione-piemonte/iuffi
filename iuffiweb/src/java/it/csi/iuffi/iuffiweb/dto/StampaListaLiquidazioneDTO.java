package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class StampaListaLiquidazioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3465913956254688886L;

  private Long              idFileListaLiquidazione;
  private Long              idListaLiquidazione;
  private String            nomeFile;
  private byte[]            contenutoFile;
  private Long              idStatoStampa;
  private String            descAnomalia;
  private Date              dataUltimoAggiornamento;

  public Long getIdFileListaLiquidazione()
  {
    return idFileListaLiquidazione;
  }

  public void setIdFileListaLiquidazione(Long idFileListaLiquidazione)
  {
    this.idFileListaLiquidazione = idFileListaLiquidazione;
  }

  public Long getIdListaLiquidazione()
  {
    return idListaLiquidazione;
  }

  public void setIdListaLiquidazione(Long idListaLiquidazione)
  {
    this.idListaLiquidazione = idListaLiquidazione;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }

  public byte[] getContenutoFile()
  {
    return contenutoFile;
  }

  public void setContenutoFile(byte[] contenutoFile)
  {
    this.contenutoFile = contenutoFile;
  }

  public Long getIdStatoStampa()
  {
    return idStatoStampa;
  }

  public void setIdStatoStampa(Long idStatoStampa)
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

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

}
