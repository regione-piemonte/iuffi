package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaModificaMultiplaInterventiDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                serialVersionUID = 8044787352387280348L;
  protected long                           idIntervento;
  protected long                           idProcedimentoOggetto;
  protected Integer                        progressivo;
  protected String                         descIntervento;
  protected String                         ulterioriInformazioni;
  protected String                         flagGestioneCostoUnitario;
  protected String                         flagTipoOperazione;
  protected long                           idDescrizioneIntervento;
  protected long                           idTipoClassificazione;
  protected long                           idTipoLocalizzazione;
  protected BigDecimal                     costoUnitarioMinimo;
  protected BigDecimal                     costoUnitarioMassimo;
  protected List<MisurazioneInterventoDTO> misurazioneIntervento;
  protected BigDecimal                     importoUnitario;
  protected BigDecimal                     importoAmmesso;
  protected BigDecimal                     importo;
  protected Long                           idAttivita;
  protected Long                           idPartecipante;
  protected String                         cuaaPartecipante;
  protected String                         flagBeneficiario;
  protected String						   codiceIdentificativo;
  
  

  // Ritorna l'id univoco che viene utilizzato nelle funzioni di modifica (viene
  // riscritto da RigaInserimentoMultiplaInterventiDTO)
  public long getId()
  {
    return idIntervento;
  }

  public void addMisurazioneIntervento(
      MisurazioneInterventoDTO misurazioneInterventoDTO)
  {
    if (misurazioneInterventoDTO == null)
    {
      misurazioneIntervento = new ArrayList<MisurazioneInterventoDTO>();
    }
    misurazioneIntervento.add(misurazioneInterventoDTO);
  }

  public Integer getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(Integer progressivo)
  {
    this.progressivo = progressivo;
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

  
  public String getCodiceIdentificativo() {
	return codiceIdentificativo;
}

public void setCodiceIdentificativo(String codiceIdentificativo) {
	this.codiceIdentificativo = codiceIdentificativo;
}

public String getFlagGestioneCostoUnitario()
  {
    return flagGestioneCostoUnitario;
  }

  public void setFlagGestioneCostoUnitario(String flagGestioneCostoUnitario)
  {
    this.flagGestioneCostoUnitario = flagGestioneCostoUnitario;
  }

  public long getIdDescrizioneIntervento()
  {
    return idDescrizioneIntervento;
  }

  public void setIdDescrizioneIntervento(long idDescrizioneIntervento)
  {
    this.idDescrizioneIntervento = idDescrizioneIntervento;
  }

  public long getIdTipoClassificazione()
  {
    return idTipoClassificazione;
  }

  public void setIdTipoClassificazione(long idTipoClassificazione)
  {
    this.idTipoClassificazione = idTipoClassificazione;
  }

  public long getIdTipoLocalizzazione()
  {
    return idTipoLocalizzazione;
  }

  public void setIdTipoLocalizzazione(long idTipoLocalizzazione)
  {
    this.idTipoLocalizzazione = idTipoLocalizzazione;
  }

  public List<MisurazioneInterventoDTO> getMisurazioneIntervento()
  {
    return misurazioneIntervento;
  }

  public void setMisurazioneIntervento(
      List<MisurazioneInterventoDTO> misurazioneIntervento)
  {
    this.misurazioneIntervento = misurazioneIntervento;
  }

  public BigDecimal getImportoUnitario()
  {
    return importoUnitario;
  }

  public void setImportoUnitario(BigDecimal importoUnitario)
  {
    this.importoUnitario = importoUnitario;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public BigDecimal getCostoUnitarioMinimo()
  {
    return costoUnitarioMinimo;
  }

  public void setCostoUnitarioMinimo(BigDecimal costoUnitarioMinimo)
  {
    this.costoUnitarioMinimo = costoUnitarioMinimo;
  }

  public BigDecimal getCostoUnitarioMassimo()
  {
    return costoUnitarioMassimo;
  }

  public void setCostoUnitarioMassimo(BigDecimal costoUnitarioMassimo)
  {
    this.costoUnitarioMassimo = costoUnitarioMassimo;
  }

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public String getFlagTipoOperazione()
  {
    return flagTipoOperazione;
  }

  public void setFlagTipoOperazione(String flagTipoOperazione)
  {
    this.flagTipoOperazione = flagTipoOperazione;
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

  public Long getIdAttivita()
  {
    return idAttivita;
  }

  public void setIdAttivita(Long idAttivita)
  {
    this.idAttivita = idAttivita;
  }

  public Long getIdPartecipante()
  {
    return idPartecipante;
  }

  public void setIdPartecipante(Long idPartecipante)
  {
    this.idPartecipante = idPartecipante;
  }

  public BigDecimal getImportoAmmesso()
  {
    return importoAmmesso;
  }

  public void setImportoAmmesso(BigDecimal importoAmmesso)
  {
    this.importoAmmesso = importoAmmesso;
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

}
