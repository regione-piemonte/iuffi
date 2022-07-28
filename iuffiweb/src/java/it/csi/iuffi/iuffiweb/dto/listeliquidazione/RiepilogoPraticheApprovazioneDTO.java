package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RiepilogoPraticheApprovazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -821502113906713340L;
  protected String          cuaa;
  protected String          denominazione;
  protected String          identificativo;
  protected String          codiceLivello;
  protected String          causalePagamento;
  protected BigDecimal      importoLiquidato;
  protected String          anomalia         = "N";
  protected Long            idProcedimento;
  protected Long            idProcedimentoOggetto;
  protected BigDecimal      anticipoErogato;
  protected BigDecimal      importoPremio;
  protected boolean         checked;

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public BigDecimal getAnticipoErogato()
  {
    return anticipoErogato;
  }

  public void setAnticipoErogato(BigDecimal anticipoErogato)
  {
    this.anticipoErogato = anticipoErogato;
  }

  public BigDecimal getImportoPremio()
  {
    return importoPremio;
  }

  public void setImportoPremio(BigDecimal importoPremio)
  {
    this.importoPremio = importoPremio;
  }

  public Long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(Long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getCausalePagamento()
  {
    return causalePagamento;
  }

  public void setCausalePagamento(String causalePagamento)
  {
    this.causalePagamento = causalePagamento;
  }

  public BigDecimal getImportoLiquidato()
  {
    return importoLiquidato;
  }

  public void setImportoLiquidato(BigDecimal importoLiquidato)
  {
    this.importoLiquidato = importoLiquidato;
  }

  public String getAnomalia()
  {
    return anomalia;
  }

  public void setAnomalia(String anomalia)
  {
    this.anomalia = anomalia;
  }
}
