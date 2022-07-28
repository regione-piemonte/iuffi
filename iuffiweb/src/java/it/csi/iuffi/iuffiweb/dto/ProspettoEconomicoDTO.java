package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProspettoEconomicoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long                           serialVersionUID = -8875893299820009025L;

  private Long                                        idProcedimento;
  private Long                                        idLivello;
  private String                                      operazione;

  private List<DomandaPagamentoProspettoEconomicoDTO> records;

  public Long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(Long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public List<DomandaPagamentoProspettoEconomicoDTO> getRecords()
  {
    return records;
  }

  public void setRecords(List<DomandaPagamentoProspettoEconomicoDTO> operazioni)
  {
    this.records = operazioni;
  }

  public Long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(Long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getOperazione()
  {
    return operazione;
  }

  public void setOperazione(String operazione)
  {
    this.operazione = operazione;
  }

  public BigDecimal getContributoNonRichiesto()
  {
    BigDecimal totImportiLiquidati = BigDecimal.ZERO;
    BigDecimal totEconomie = BigDecimal.ZERO;
    BigDecimal contrConcesso = BigDecimal.ZERO;

    if (records != null)
      for (DomandaPagamentoProspettoEconomicoDTO d : records)
      {
        if (d.getEconomia() != null)
          totEconomie = totEconomie.add(d.getEconomia());
        if (d.getImportoLiquidato() != null)
          totImportiLiquidati = totImportiLiquidati
              .add(d.getImportoLiquidato());
        if (d.getContributoConcesso() != null)
          contrConcesso = d.getContributoConcesso();
      }
    return contrConcesso.subtract(totImportiLiquidati.add(totEconomie));
  }

}
