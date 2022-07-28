package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProcedimentoOggetto implements ILoggable
{
  /** serialVersionUID */
  public static final String     REQUEST_NAME     = "procedimentoOggetto";
  private static final long      serialVersionUID = -5824167342866260460L;
  private long                   idProcedimentoOggetto;
  private long                   idProcedimento;
  private long                   codiceRaggruppamento;
  private Long                   idEsito;
  private Long                   idStatoOggetto;
  private Long                   idAttoAmmi;
  private long                   idUtenteAggiornamento;
  private String                 codiceAttore;
  private Date                   dataInizio;
  private String                 descrizione;
  private String                 flagAmmissione;
  private String                 flagIstanza;
  private String                 identificativo;
  private String                 codOggetto;
  private Date                   dataFine;
  private Date                   dataInizioLastIter;
  private String                 descEsito;
  private String                 codiceEsito;
  private String                 flagPremioAccertato;
  private String                 specificita;
  private String                 tipoPagamentoSigop;
  private String                 descStato;
  private String                 flagValidazioneGrafica;
  private List<QuadroOggettoDTO> quadri;
  private Long                   extIdDichiarazioneConsistenza;
  private Long                   iDLegameGruppoOggetto;

  
  
  public Long getIdAttoAmmi()
  {
    return idAttoAmmi;
  }

  public void setIdAttoAmmi(Long idAttoAmmi)
  {
    this.idAttoAmmi = idAttoAmmi;
  }

  public String getTipoPagamentoSigop()
  {
    return tipoPagamentoSigop;
  }

  public void setTipoPagamentoSigop(String tipoPagamentoSigop)
  {
    this.tipoPagamentoSigop = tipoPagamentoSigop;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public List<QuadroOggettoDTO> getQuadri()
  {
    return quadri;
  }

  public void setQuadri(List<QuadroOggettoDTO> quadri)
  {
    this.quadri = quadri;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public String getCodiceAttore()
  {
    return codiceAttore;
  }

  public void setCodiceAttore(String codiceAttore)
  {
    this.codiceAttore = codiceAttore;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public long getIdBandoOggetto()
  {
    return quadri.get(0).getIdBandoOggetto();
  }

  public long getIdOggetto()
  {
    return quadri.get(0).getIdOggetto();
  }

  public QuadroOggettoDTO findQuadroByCU(String cu)
  {
    if (quadri != null)
    {
      for (QuadroOggettoDTO quadro : quadri)
      {
        if (quadro.hasAzioneConCU(cu))
        {
          return quadro;
        }
      }
    }
    return null;
  }

  public QuadroOggettoDTO findQuadroByCodiceQuadro(String codQuadro)
  {
    if (quadri != null)
    {
      for (QuadroOggettoDTO quadro : quadri)
      {
        if (quadro.getCodQuadro().equals(codQuadro))
        {
          return quadro;
        }
      }
    }
    return null;
  }

  public AzioneDTO findAzioneByCU(String cu)
  {
    if (quadri != null)
    {
      for (QuadroOggettoDTO quadro : quadri)
      {
        AzioneDTO azioneDTO = quadro.azioneConCU(cu);
        if (azioneDTO != null)
        {
          return azioneDTO;
        }
      }
    }
    return null;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public String getDescEsito()
  {
    return descEsito;
  }

  public void setDescEsito(String descEsito)
  {
    this.descEsito = descEsito;
  }

  public String getDescStato()
  {
    return descStato;
  }

  public void setDescStato(String descStato)
  {
    this.descStato = descStato;
  }

  public Date getDataInizioLastIter()
  {
    return dataInizioLastIter;
  }

  public void setDataInizioLastIter(Date dataInizioLastIter)
  {
    this.dataInizioLastIter = dataInizioLastIter;
  }

  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public String getSpecificita()
  {
    return specificita;
  }

  public void setSpecificita(String specificita)
  {
    this.specificita = specificita;
  }

  public Long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(Long idEsito)
  {
    this.idEsito = idEsito;
  }

  public Long getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(Long idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public String getFlagPremioAccertato()
  {
    return flagPremioAccertato;
  }

  public void setFlagPremioAccertato(String flagPremioAccertato)
  {
    this.flagPremioAccertato = flagPremioAccertato;
  }

  public String getFlagIstanza()
  {
    return flagIstanza;
  }

  public void setFlagIstanza(String flagIstanza)
  {
    this.flagIstanza = flagIstanza;
  }

  public String getCodOggetto()
  {
    return codOggetto;
  }

  public void setCodOggetto(String codOggetto)
  {
    this.codOggetto = codOggetto;
  }

  public long getCodiceRaggruppamento()
  {
    return codiceRaggruppamento;
  }

  public void setCodiceRaggruppamento(long codiceRaggruppamento)
  {
    this.codiceRaggruppamento = codiceRaggruppamento;
  }

  public String getFlagAmmissione()
  {
    return flagAmmissione;
  }

  public void setFlagAmmissione(String flagAmmissione)
  {
    this.flagAmmissione = flagAmmissione;
  }

  public String getCodiceEsito()
  {
    return codiceEsito;
  }

  public void setCodiceEsito(String codiceEsito)
  {
    this.codiceEsito = codiceEsito;
  }

  public String getFlagValidazioneGrafica()
  {
    return flagValidazioneGrafica;
  }

  public void setFlagValidazioneGrafica(String flagValidazioneGrafica)
  {
    this.flagValidazioneGrafica = flagValidazioneGrafica;
  }

  public Long getExtIdDichiarazioneConsistenza()
  {
    return extIdDichiarazioneConsistenza;
  }

  public void setExtIdDichiarazioneConsistenza(
      Long extIdDichiarazioneConsistenza)
  {
    this.extIdDichiarazioneConsistenza = extIdDichiarazioneConsistenza;
  }

  public Long getiDLegameGruppoOggetto()
  {
    return iDLegameGruppoOggetto;
  }

  public void setiDLegameGruppoOggetto(Long iDLegameGruppoOggetto)
  {
    this.iDLegameGruppoOggetto = iDLegameGruppoOggetto;
  }

}
