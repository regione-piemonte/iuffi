package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DomandaPagamentoProspettoEconomicoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 175591287313756039L;

  private Long              idProcedimentoOggetto;
  private Long              idLivello;
  private String            operazione;
  private String            tipologiaDomandaDiPagamento;
  private String            tipoPagamentoSigop;
  private BigDecimal        contributoConcesso;
  private Date              dataPresentazione;
  private BigDecimal        contributoRichiesto;
  private BigDecimal        contributoRendicontato;
  private BigDecimal        importoRiduzioniSanzioni;
  private BigDecimal        importoLiquidato;
  private BigDecimal        economia;
  private BigDecimal        contributoRichiestoAnticipo;
  private BigDecimal        contrRichiestoAccontoSaldo;
  private BigDecimal        contributoNonRiconosciutoNonSanzionabile;
  private BigDecimal        contributoSanzionabile;

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
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

  public String getTipologiaDomandaDiPagamento()
  {
    return tipologiaDomandaDiPagamento;
  }

  public void setTipologiaDomandaDiPagamento(String tipologiaDomandaDiPagamento)
  {
    this.tipologiaDomandaDiPagamento = tipologiaDomandaDiPagamento;
  }

  public BigDecimal getContributoConcesso()
  {
    return contributoConcesso;
  }

  public void setContributoConcesso(BigDecimal contributoConcesso)
  {
    this.contributoConcesso = contributoConcesso;
  }

  public Date getDataPresentazione()
  {
    return dataPresentazione;
  }

  public void setDataPresentazione(Date dataPresentazione)
  {
    this.dataPresentazione = dataPresentazione;
  }

  public BigDecimal getContributoRichiesto()
  {
    return this.contributoRichiesto;
    /*
     * if (this.contributoRichiesto != null) return this.contributoRichiesto;
     * else if (this.getContributoRichiestoAnticipo() != null) return
     * this.getContributoRichiestoAnticipo(); else if
     * (this.getContrRichiestoAccontoSaldo() != null) return
     * this.getContrRichiestoAccontoSaldo();
     * 
     * return null;
     */
  }

  public void setContributoRichiesto(BigDecimal contributoRichiesto)
  {
    this.contributoRichiesto = contributoRichiesto;
  }

  public BigDecimal getImportoRiduzioniSanzioni()
  {
    return importoRiduzioniSanzioni;
  }

  public void setImportoRiduzioniSanzioni(BigDecimal importoRiduzioniSanzioni)
  {
    this.importoRiduzioniSanzioni = importoRiduzioniSanzioni;
  }

  public BigDecimal getImportoLiquidato()
  {
    return importoLiquidato;
  }

  public void setImportoLiquidato(BigDecimal importoLiquidato)
  {
    this.importoLiquidato = importoLiquidato;
  }

  public BigDecimal getEconomia()
  {
    return economia;
  }

  public void setEconomia(BigDecimal economia)
  {
    this.economia = economia;
  }

  public BigDecimal getContributoRichiestoAnticipo()
  {
    return contributoRichiestoAnticipo;
  }

  public void setContributoRichiestoAnticipo(
      BigDecimal contributoRichiestoAnticipo)
  {
    this.contributoRichiestoAnticipo = contributoRichiestoAnticipo;
  }

  public BigDecimal getContrRichiestoAccontoSaldo()
  {
    return contrRichiestoAccontoSaldo;
  }

  public void setContrRichiestoAccontoSaldo(
      BigDecimal contrRichiestoAccontoSaldo)
  {
    this.contrRichiestoAccontoSaldo = contrRichiestoAccontoSaldo;
  }

  public String getDataPresentazioneStr()
  {
    return IuffiUtils.DATE.formatDate(dataPresentazione);
  }

  public BigDecimal getContributoNonRiconosciutoNonSanzionabile()
  {
    return contributoNonRiconosciutoNonSanzionabile;
  }

  public void setContributoNonRiconosciutoNonSanzionabile(
      BigDecimal contributoNonRiconosciutoNonSanzionabile)
  {
    this.contributoNonRiconosciutoNonSanzionabile = contributoNonRiconosciutoNonSanzionabile;
  }

  public String getTipoPagamentoSigop()
  {
    return tipoPagamentoSigop;
  }

  public void setTipoPagamentoSigop(String tipoPagamentoSigop)
  {
    this.tipoPagamentoSigop = tipoPagamentoSigop;
  }

  public BigDecimal getContributoRendicontato()
  {
    return contributoRendicontato;
  }

  public void setContributoRendicontato(BigDecimal contributoRendicontato)
  {
    this.contributoRendicontato = contributoRendicontato;
  }

  public BigDecimal getContributoSanzionabile()
  {
    return contributoSanzionabile;
  }

  public void setContributoSanzionabile(BigDecimal contributoSanzionabile)
  {
    this.contributoSanzionabile = contributoSanzionabile;
  }
}
