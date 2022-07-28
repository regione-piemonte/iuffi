package it.csi.iuffi.iuffiweb.dto.procedimento;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RicevutaPagamentoVO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private Long              idDettRicevutaPagamento;
  private long              idRicevutaPagamento;
  private String            numero;
  private Date              dataPagamento;
  private Long              idModalitaPagamento;
  private String            descrModalitaPagamento;
  private BigDecimal        importoPagamento;
  private BigDecimal        importoDaAssociare;
  private String            note;
  private String            nomeLogicoFile;
  private byte[]            contenuto;
  private String            nomeFisicoFile;
  private String            visibleIcons;
  private Long              idProcedimentoOggetto;

  public String getIconaFile()
  {
    return IuffiUtils.FILE.getDocumentCSSIconClass(nomeFisicoFile);
  }

  public Long getIdDettRicevutaPagamento()
  {
    return idDettRicevutaPagamento;
  }

  public void setIdDettRicevutaPagamento(Long idDettRicevutaPagamento)
  {
    this.idDettRicevutaPagamento = idDettRicevutaPagamento;
  }

  public long getIdRicevutaPagamento()
  {
    return idRicevutaPagamento;
  }

  public void setIdRicevutaPagamento(long idRicevutaPagamento)
  {
    this.idRicevutaPagamento = idRicevutaPagamento;
  }

  public String getNumero()
  {
    return numero;
  }

  public void setNumero(String numero)
  {
    this.numero = numero;
  }

  public Date getDataPagamento()
  {
    return dataPagamento;
  }

  public String getDataPagamentoStr()
  {
    return IuffiUtils.DATE.formatDate(dataPagamento);
  }

  public void setDataPagamento(Date dataPagamento)
  {
    this.dataPagamento = dataPagamento;
  }

  public Long getIdModalitaPagamento()
  {
    return idModalitaPagamento;
  }

  public void setIdModalitaPagamento(Long idModalitaPagamento)
  {
    this.idModalitaPagamento = idModalitaPagamento;
  }

  public String getDescrModalitaPagamento()
  {
    return descrModalitaPagamento;
  }

  public void setDescrModalitaPagamento(String descrModalitaPagamento)
  {
    this.descrModalitaPagamento = descrModalitaPagamento;
  }

  public BigDecimal getImportoPagamento()
  {
    return importoPagamento;
  }

  public String getImportoPagamentoStr()
  {
    return IuffiUtils.FORMAT
        .formatDecimal2(IuffiUtils.NUMBERS.nvl(importoPagamento));
  }

  public void setImportoPagamento(BigDecimal importoPagamento)
  {
    this.importoPagamento = importoPagamento;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getNomeLogicoFile()
  {
    return nomeLogicoFile;
  }

  public void setNomeLogicoFile(String nomeLogicoFile)
  {
    this.nomeLogicoFile = nomeLogicoFile;
  }

  public byte[] getContenuto()
  {
    return contenuto;
  }

  public void setContenuto(byte[] contenuto)
  {
    this.contenuto = contenuto;
  }

  public String getNomeFisicoFile()
  {
    return nomeFisicoFile;
  }

  public void setNomeFisicoFile(String nomeFisicoFile)
  {
    this.nomeFisicoFile = nomeFisicoFile;
  }

  public BigDecimal getImportoDaAssociare()
  {
    return importoDaAssociare;
  }

  public void setImportoDaAssociare(BigDecimal importoDaAssociare)
  {
    this.importoDaAssociare = importoDaAssociare;
  }

  public String getVisibleIcons()
  {
    return visibleIcons;
  }

  public void setVisibleIcons(String visibleIcons)
  {
    this.visibleIcons = visibleIcons;
  }

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

}
