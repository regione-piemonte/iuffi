package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaElencoInterventi implements ILoggable
{
  /** serialVersionUID */
  private static final long               serialVersionUID = -5577279588754852861L;
  private long                            idIntervento;
  private Long                            idDettaglioIntervento;
  private Long                            idDettIntervProcOgg;
  private Integer                         progressivo;
  private BigDecimal                      importoInvestimento;
  private BigDecimal                      importoUnitario;
  private String                          descIntervento;
  private String                          ulterioriInformazioni;
  private String                          descTipoClassificazione;
  private int                             idTipoLocalizzazione;
  private List<InfoMisurazioneIntervento> misurazioni;
  private String                          descComuni;
  private String                          flagTipoOperazione;
  private String                          cuaaPartecipante;
  private String                          cuaaPartecipanteDenominazione;
  private String                          icone;
  private String                          descTipoAggregazione;
  private String                          flagBeneficiario;
  private String                          flagAssociatoAltraMisura;
  private Long                            idDescInterventoAssociato;
  private Date                            dataAttivazioneCorso;
  private Long                            PoAttivazioneCorso;
  private Long                            idProcedimentoOggetto;
  private boolean                         dataAvvioChanged;
  private String                          operazione;
  private BigDecimal                      spesaAmmessa;
  private BigDecimal                      percentualeContributo;
  private BigDecimal                      contributoConcesso;
  private boolean                         interventoModificabileASaldo;
  protected String						   codiceIdentificativo;

  
  private Long idDannoAtm;
  private String descDanno;
  private Long idDanno;
  private String descTipoDanno;
  private Long progressivoDanno;
  
  private String flagCondotta;
  private String flagCanale;
  private String flagOpereDiPresa;


  
  public String getCodiceIdentificativo() {
	return codiceIdentificativo;
}

public void setCodiceIdentificativo(String codiceIdentificativo) {
	this.codiceIdentificativo = codiceIdentificativo;
}

public String getFlagCondotta() {
	return flagCondotta;
}

public void setFlagCondotta(String flagCondotta) {
	this.flagCondotta = flagCondotta;
}

public String getFlagCanale() {
	return flagCanale;
}

public void setFlagCanale(String flagCanale) {
	this.flagCanale = flagCanale;
}

public String getFlagOpereDiPresa() {
	return flagOpereDiPresa;
}

public void setFlagOpereDiPresa(String flagOpereDiPresa) {
	this.flagOpereDiPresa = flagOpereDiPresa;
}

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

  public BigDecimal getImportoUnitario()
  {
    return importoUnitario;
  }

  public void setImportoUnitario(BigDecimal importoUnitario)
  {
    this.importoUnitario = importoUnitario;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public List<InfoMisurazioneIntervento> getMisurazioni()
  {
    return misurazioni;
  }

  public void setMisurazioni(List<InfoMisurazioneIntervento> misurazioni)
  {
    this.misurazioni = misurazioni;
  }

  public String getMisurazioniToString()
  {
    String s = "";
    if (this.misurazioni != null)
    {
      for (InfoMisurazioneIntervento m : misurazioni)
      {
        s += m.getDescMisurazione() + " ";
        if (m.getCodiceUnitaMisura() == null
            || m.getCodiceUnitaMisura().compareTo("NO_MISURA") != 0)
          s += m.getValore().toString();
      }
    }
    return s;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public String getDescTipoClassificazione()
  {
    return descTipoClassificazione;
  }

  public void setDescTipoClassificazione(String descTipoClassificazione)
  {
    this.descTipoClassificazione = descTipoClassificazione;
  }

  public int getIdTipoLocalizzazione()
  {
    return idTipoLocalizzazione;
  }

  public void setIdTipoLocalizzazione(int idTipoLocalizzazione)
  {
    this.idTipoLocalizzazione = idTipoLocalizzazione;
  }

  public String getDescComuni()
  {
    return descComuni;
  }

  public void setDescComuni(String descComuni)
  {
    this.descComuni = descComuni;
  }

  public void setDescComuni(StringBuilder descComuni)
  {
    this.descComuni = descComuni == null ? null : descComuni.toString();
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

  public String getDescTipoAggregazione()
  {
    return descTipoAggregazione;
  }

  public void setDescTipoAggregazione(String descTipoAggregazione)
  {
    this.descTipoAggregazione = descTipoAggregazione;
  }

  public String getFlagAssociatoAltraMisura()
  {
    return flagAssociatoAltraMisura;
  }

  public void setFlagAssociatoAltraMisura(String flagAssociatoAltraMisura)
  {
    this.flagAssociatoAltraMisura = flagAssociatoAltraMisura;
  }

  public Long getIdDescInterventoAssociato()
  {
    return idDescInterventoAssociato;
  }

  public void setIdDescInterventoAssociato(Long idDescInterventoAssociato)
  {
    this.idDescInterventoAssociato = idDescInterventoAssociato;
  }

  public Date getDataAttivazioneCorso()
  {
    return dataAttivazioneCorso;
  }

  public String getDataAttivazioneCorsoStr()
  {
    return IuffiUtils.DATE.formatDate(dataAttivazioneCorso);
  }

  public void setDataAttivazioneCorso(Date dataAttivazioneCorso)
  {
    this.dataAttivazioneCorso = dataAttivazioneCorso;
  }

  public Long getPoAttivazioneCorso()
  {
    return PoAttivazioneCorso;
  }

  public void setPoAttivazioneCorso(Long poAttivazioneCorso)
  {
    PoAttivazioneCorso = poAttivazioneCorso;
  }

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public boolean isDataAvvioChanged()
  {
    return dataAvvioChanged;
  }

  public void setDataAvvioChanged(boolean dataAvvioChanged)
  {
    this.dataAvvioChanged = dataAvvioChanged;
  }

  public String getOperazione()
  {
    return operazione;
  }

  public void setOperazione(String operazione)
  {
    this.operazione = operazione;
  };

  public BigDecimal getSpesaAmmessa()
  {
    return spesaAmmessa;
  }

  public void setSpesaAmmessa(BigDecimal spesaAmmessa)
  {
    this.spesaAmmessa = spesaAmmessa;
  }

  public BigDecimal getPercentualeContributo()
  {
    return percentualeContributo;
  }

  public void setPercentualeContributo(BigDecimal percentualeContributo)
  {
    this.percentualeContributo = percentualeContributo;
  }

  public BigDecimal getContributoConcesso()
  {
    return contributoConcesso;
  }

  public void setContributoConcesso(BigDecimal contributoConcesso)
  {
    this.contributoConcesso = contributoConcesso;
  }

  public String getImportoUnitarioStr()
  {
    if (importoUnitario == null)
      return "";
    else
      return IuffiUtils.FORMAT.formatCurrency(importoUnitario);
  }

  public String getSpesaAmmessaStr()
  {
    if (spesaAmmessa == null)
      return "";
    else
      return IuffiUtils.FORMAT.formatCurrency(spesaAmmessa);
  }

  public String getPercentualeContributoStr()
  {
    if (percentualeContributo == null)
      return "";
    else
      return IuffiUtils.FORMAT.formatCurrency(percentualeContributo);
  }

  public String getContributoConcessoStr()
  {
    if (contributoConcesso == null)
      return "";
    else
      return IuffiUtils.FORMAT.formatCurrency(contributoConcesso);
  }

  public void enforceInterventoModificabileASaldo(boolean canModify)
  {
    interventoModificabileASaldo &= canModify;
  }

  public boolean isInterventoModificabileASaldo()
  {
    return interventoModificabileASaldo;
  }

  public void setInterventoModificabileASaldo(
      boolean interventoModificabileASaldo)
  {
    this.interventoModificabileASaldo = interventoModificabileASaldo;
  }

  private List<DocumentoSpesaVO> elencoDocumenti;

  public List<DocumentoSpesaVO> getElencoDocumenti()
  {
    return elencoDocumenti;
  }

  public void setElencoDocumenti(List<DocumentoSpesaVO> elencoDocumenti)
  {
    this.elencoDocumenti = elencoDocumenti;
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

  public String getCuaaPartecipanteCompletoHtml()
  {
    if (GenericValidator.isBlankOrNull(cuaaPartecipante))
      return "";
    return cuaaPartecipante + " -<br>" + cuaaPartecipanteDenominazione;
  }

  public String getCuaaPartecipanteCompletoStampa()
  {
    if (GenericValidator.isBlankOrNull(cuaaPartecipante))
      return "";
    return cuaaPartecipante + " -\n" + cuaaPartecipanteDenominazione;
  }

  public void setCuaaPartecipanteDenominazione(
      String cuaaPartecipanteDenominazione)
  {
    this.cuaaPartecipanteDenominazione = cuaaPartecipanteDenominazione;
  }

	public Long getIdDannoAtm()
	{
		return idDannoAtm;
	}
	
	public void setIdDannoAtm(Long idDannoAtm)
	{
		this.idDannoAtm = idDannoAtm;
	}
	
	public String getDescDanno()
	{
		return descDanno;
	}
	
	public void setDescDanno(String descDanno)
	{
		this.descDanno = descDanno;
	}

	public Long getIdDanno()
	{
		return idDanno;
	}

	public void setIdDanno(Long idDanno)
	{
		this.idDanno = idDanno;
	}

	public String getDescTipoDanno()
	{
		return descTipoDanno;
	}

	public void setDescTipoDanno(String descTipoDanno)
	{
		this.descTipoDanno = descTipoDanno;
	}

	public Long getProgressivoDanno()
	{
		return progressivoDanno;
	}

	public void setProgressivoDanno(Long progressivoDanno)
	{
		this.progressivoDanno = progressivoDanno;
	}
	
	
}
