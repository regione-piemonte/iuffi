package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExcelRigaRicevutaPagDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private String            numeroPagamento;
  private String            dataPagamentoStr;
  private BigDecimal        importoPagamento;
  private BigDecimal        importoPagamentoAssociato;
  private String            modalitaPagamento;
  private String            note;

  public String getNumeroPagamento()
  {
    return numeroPagamento;
  }

  public void setNumeroPagamento(String numeroPagamento)
  {
    this.numeroPagamento = numeroPagamento;
  }

  public String getDataPagamentoStr()
  {
    return dataPagamentoStr;
  }

  public void setDataPagamentoStr(String dataPagamentoStr)
  {
    this.dataPagamentoStr = dataPagamentoStr;
  }

  public BigDecimal getImportoPagamento()
  {
    return importoPagamento;
  }

  public void setImportoPagamento(BigDecimal importoPagamento)
  {
    this.importoPagamento = importoPagamento;
  }

  public String getModalitaPagamento()
  {
    return modalitaPagamento;
  }

  public void setModalitaPagamento(String modalitaPagamento)
  {
    this.modalitaPagamento = modalitaPagamento;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public BigDecimal getImportoPagamentoAssociato()
  {
    return importoPagamentoAssociato;
  }

  public void setImportoPagamentoAssociato(BigDecimal importoPagamentoAssociato)
  {
    this.importoPagamentoAssociato = importoPagamentoAssociato;
  }

}
