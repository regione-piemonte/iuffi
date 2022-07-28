package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico;

import java.math.BigDecimal;

import org.apache.commons.validator.GenericValidator;
import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaJSONInterventoQuadroEconomicoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1799314396882724095L;
  private long              idIntervento;
  private Long              idDettaglioIntervento;
  private Long              idDettIntervProcOgg;
  private Integer           progressivo;
  private BigDecimal        importoInvestimento;
  private String            cuaaPartecipanteDenominazione;
  private BigDecimal        importoAmmesso;
  private BigDecimal        percentualeContributo;
  private BigDecimal        importoContributo;
  private String            descIntervento;
  private String            ulterioriInformazioni;
  private String            flagTipoOperazione;
  protected Long            idPartecipante;
  protected String          cuaaPartecipante;
  protected String          flagBeneficiario;
  private String            icone;
  @JsonIgnore
  private long              idLivello;
  @JsonIgnore
  private BigDecimal        percentualeContributoMassima;
  @JsonIgnore
  private BigDecimal        percentualeContributoMinima;

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public Long getIdDettaglioIntervento()
  {
    return idDettaglioIntervento;
  }

  public void setIdDettaglioIntervento(Long idDettaglioIntervento)
  {
    this.idDettaglioIntervento = idDettaglioIntervento;
  }

  public Long getIdDettIntervProcOgg()
  {
    return idDettIntervProcOgg;
  }

  public void setIdDettIntervProcOgg(Long idDettIntervProcOgg)
  {
    this.idDettIntervProcOgg = idDettIntervProcOgg;
  }

  public Integer getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(Integer progressivo)
  {
    this.progressivo = progressivo;
  }

  public BigDecimal getImportoInvestimento()
  {
    return importoInvestimento;
  }

  public void setImportoInvestimento(BigDecimal importoInvestimento)
  {
    this.importoInvestimento = importoInvestimento;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public String getFlagTipoOperazione()
  {
    return flagTipoOperazione;
  }

  public void setFlagTipoOperazione(String flagTipoOperazione)
  {
    this.flagTipoOperazione = flagTipoOperazione;
  }

  public String getIcone()
  {
    return icone;
  }

  public void setIcone(String icone)
  {
    this.icone = icone;
  }

  public BigDecimal getImportoAmmesso()
  {
    return importoAmmesso;
  }

  public BigDecimal getImportoAmmessoOInvestimento()
  {
    return IuffiUtils.NUMBERS.nvl(importoAmmesso, importoInvestimento);
  }

  public void setImportoAmmesso(BigDecimal importoAmmesso)
  {
    this.importoAmmesso = importoAmmesso;
  }

  public BigDecimal getPercentualeContributo()
  {
    return percentualeContributo;
  }

  public void setPercentualeContributo(BigDecimal percentualeContributo)
  {
    this.percentualeContributo = percentualeContributo;
  }

  public BigDecimal getImportoContributo()
  {
    return importoContributo;
  }

  public void setImportoContributo(BigDecimal importoContributo)
  {
    this.importoContributo = importoContributo;
  }

  public String htmlForProgressivo()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<div style=\"text-align:center\">");
    sb.append("<span class=\"badge\" style=\"");
    if (IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_INSERIMENTO
        .equals(flagTipoOperazione))
    {
      sb.append("background-color:green;");
    }
    else
    {
      if (IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_MODIFICA
          .equals(flagTipoOperazione))
      {
        sb.append("background-color:orange;");
      }
      else
      {
        if (IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE
            .equals(flagTipoOperazione))
        {
          // Non faccio nulla, va bene il colore di default
        }
        else
        {
          sb.append("background-color:white;color:black;");
        }
      }
    }
    sb.append("border:1px solid black\">");
    sb.append(IuffiUtils.STRING.nvl(progressivo, "&nbsp;"))
        .append("</span>");
    sb.append("</div>");
    return sb.toString();
  }

  @JsonIgnore
  public long getIdLivello()
  {
    return idLivello;
  }

  @JsonIgnore
  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  @JsonIgnore
  public BigDecimal getPercentualeContributoMassima()
  {
    return percentualeContributoMassima;
  }

  @JsonIgnore
  public void setPercentualeContributoMassima(
      BigDecimal percentualeContributoMassima)
  {
    this.percentualeContributoMassima = percentualeContributoMassima;
  }

  @JsonIgnore
  public BigDecimal getPercentualeContributoMinima()
  {
    return percentualeContributoMinima;
  }

  @JsonIgnore
  public void setPercentualeContributoMinima(
      BigDecimal percentualeContributoMinima)
  {
    this.percentualeContributoMinima = percentualeContributoMinima;
  }

  @JsonIgnore
  public boolean isPercentualeFissa()
  {
    // Nessun controllo sul null, le percentuali sono lette da DB con nvl. Si
    // declina ogni responsabilità per usi impropri
    return percentualeContributoMassima
        .compareTo(percentualeContributoMinima) == 0;
  }

  public Long getIdPartecipante()
  {
    return idPartecipante;
  }

  public void setIdPartecipante(Long idPartecipante)
  {
    this.idPartecipante = idPartecipante;
  }

  public String getCuaaPartecipante()
  {
    return cuaaPartecipante;
  }

  public void setCuaaPartecipante(String cuaaPartecipante)
  {
    this.cuaaPartecipante = cuaaPartecipante;
  }

  public String getFlagBeneficiario()
  {
    return flagBeneficiario;
  }

  public void setFlagBeneficiario(String flagBeneficiario)
  {
    this.flagBeneficiario = flagBeneficiario;
  }

  public String getCuaaPartecipanteDenominazione()
  {
    return cuaaPartecipanteDenominazione;
  }

  public void setCuaaPartecipanteDenominazione(
      String cuaaPartecipanteDenominazione)
  {
    this.cuaaPartecipanteDenominazione = cuaaPartecipanteDenominazione;
  }

  public String getCuaaPartecipanteCompletoStampa()
  {
    if (GenericValidator.isBlankOrNull(cuaaPartecipante))
      return "";
    return cuaaPartecipante + " -\n" + cuaaPartecipanteDenominazione;
  }

}
