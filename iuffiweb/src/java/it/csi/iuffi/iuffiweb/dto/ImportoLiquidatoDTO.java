package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ImportoLiquidatoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -2690992403448119930L;

  private Long              idProcedimentoOggetto;
  private Long              idImportoLiquidato;
  private Long              idListaLiquidazImpLiq;
  private String            raggruppamento;
  private BigDecimal        importoComplessivo;
  private BigDecimal        importoLiquidato;
  private Long              idLivello;
  private String            codiceLivello;
  private String            descrizioneLivello;
  private String            flagInvioSigop;
  private Date              dataInvioSigop;
  private Long              extIdTecnicoLiquidatore;
  private Long              extIdAmmCompetenza;
  private Long              decodificaAmmCompetenza;
  private Long              extIdDocumentoIndex;

  private String            note;
  private String            flagStatoLista;
  private Date              dataCreazione;

  private Long              idListaLiquidazione;
  private Long              idImportiLiquidati;
  private String            flagEsitoLiquidazione;
  private Date              dataUltimoAggiornamento;

  private Long              numeroLista;
  private Date              dataApprovazione;
  private Double            importo;
  private Double            importoRec;
  private Date              dataEffettuazione;
  private String            tecnico;
  private String            flagAmmCompetenza;
  private List<String>      ammCompetenza;
  // pagamento
  private String            codiceFondo;
  private Long              annoEsercizio;
  private Long              numeroMandato;
  private String            codicePagamento;
  private Long              numeroPagamento;
  private Date              dataCreazioneDecreto;
  private Long              numeroDecreto;
  private Date              dataCreazioneMandato;
  private Long              numeroProgressivo;
  private BigDecimal        importoPagato;
  private BigDecimal        importoRecupero;
  private Date              dataEffettuazionePagamento;
  private String            motivoResp;

  private String            numProtocollo;
  private Date              dataProtocollo;

  public String getCodicePagamento()
  {
    return codicePagamento;
  }

  public void setCodicePagamento(String codicePagamento)
  {
    this.codicePagamento = codicePagamento;
  }

  public Long getNumeroPagamento()
  {
    return numeroPagamento;
  }

  public void setNumeroPagamento(Long numeroPagamento)
  {
    this.numeroPagamento = numeroPagamento;
  }

  public Date getDataCreazioneDecreto()
  {
    return dataCreazioneDecreto;
  }

  public void setDataCreazioneDecreto(Date dataCreazioneDecreto)
  {
    this.dataCreazioneDecreto = dataCreazioneDecreto;
  }

  public String getDataCreazioneDecretoStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataCreazioneDecreto);
  }

  public Long getNumeroDecreto()
  {
    return numeroDecreto;
  }

  public void setNumeroDecreto(Long numeroDecreto)
  {
    this.numeroDecreto = numeroDecreto;
  }

  public Date getDataCreazioneMandato()
  {
    return dataCreazioneMandato;
  }

  public void setDataCreazioneMandato(Date dataCreazioneMandato)
  {
    this.dataCreazioneMandato = dataCreazioneMandato;
  }

  public String getDataCreazioneMandatoStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataCreazioneMandato);
  }

  public Long getNumeroProgressivo()
  {
    return numeroProgressivo;
  }

  public void setNumeroProgressivo(Long numeroProgressivo)
  {
    this.numeroProgressivo = numeroProgressivo;
  }

  public BigDecimal getImportoPagato()
  {
    return importoPagato;
  }

  public void setImportoPagato(BigDecimal importoPagato)
  {
    this.importoPagato = importoPagato;
  }

  public BigDecimal getImportoRecupero()
  {
    return importoRecupero;
  }

  public void setImportoRecupero(BigDecimal importoRecupero)
  {
    this.importoRecupero = importoRecupero;
  }

  public Date getDataEffettuazionePagamento()
  {
    return dataEffettuazionePagamento;
  }

  public void setDataEffettuazionePagamento(Date dataEffettuazionePagamento)
  {
    this.dataEffettuazionePagamento = dataEffettuazionePagamento;
  }

  public String getDataEffettuazionePagamentoStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataEffettuazionePagamento);
  }

  public String getDataCreazioneStr()
  {
    return IuffiUtils.DATE.formatDate(dataCreazione);
  }

  public String getDataUltimoAggiornamentoStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataUltimoAggiornamento);
  }

  public String getDataApprovazioneStr()
  {
    return IuffiUtils.DATE.formatDate(dataApprovazione);
  }

  public String getDataEffettuazioneStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataEffettuazione);
  }

  public String getDataInvioSigopStr()
  {
    return IuffiUtils.DATE.formatDate(dataInvioSigop);
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public void setIdImportoLiquidato(Long idImportoLiquidato)
  {
    this.idImportoLiquidato = idImportoLiquidato;
  }

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public Long getIdImportoLiquidato()
  {
    return idImportoLiquidato;
  }

  public void setIdImportoLiquidato(long idImportoLiquidato)
  {
    this.idImportoLiquidato = idImportoLiquidato;
  }

  public String getRaggruppamento()
  {
    return raggruppamento;
  }

  public void setRaggruppamento(String raggruppamento)
  {
    this.raggruppamento = raggruppamento;
  }

  public BigDecimal getImportoComplessivo()
  {
    return importoComplessivo;
  }

  public void setImportoComplessivo(BigDecimal importoComplessivo)
  {
    this.importoComplessivo = importoComplessivo;
  }

  public BigDecimal getImportoLiquidato()
  {
    return importoLiquidato;
  }

  public void setImportoLiquidato(BigDecimal importoLiquidato)
  {
    this.importoLiquidato = importoLiquidato;
  }

  public Long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(Long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getDescrizioneLivello()
  {
    return descrizioneLivello;
  }

  public void setDescrizioneLivello(String descrizioneLivello)
  {
    this.descrizioneLivello = descrizioneLivello;
  }

  public String getFlagInvioSigop()
  {
    return flagInvioSigop;
  }

  public void setFlagInvioSigop(String flagInvioSigop)
  {
    this.flagInvioSigop = flagInvioSigop;
  }

  public Date getDataInvioSigop()
  {
    return dataInvioSigop;
  }

  public void setDataInvioSigop(Date dataInvioSigop)
  {
    this.dataInvioSigop = dataInvioSigop;
  }

  public Long getExtIdTecnicoLiquidatore()
  {
    return extIdTecnicoLiquidatore;
  }

  public void setExtIdTecnicoLiquidatore(Long extIdTecnicoLiquidatore)
  {
    this.extIdTecnicoLiquidatore = extIdTecnicoLiquidatore;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getFlagStatoLista()
  {
    return flagStatoLista;
  }

  public void setFlagStatoLista(String flagStatoLista)
  {
    this.flagStatoLista = flagStatoLista;
  }

  public Date getDataCreazione()
  {
    return dataCreazione;
  }

  public void setDataCreazione(Date dataCreazione)
  {
    this.dataCreazione = dataCreazione;
  }

  public Double getImporto()
  {
    return importo;
  }

  public void setImporto(Double importo)
  {
    this.importo = importo;
  }

  public Double getImportoRec()
  {
    return importoRec;
  }

  public void setImportoRec(Double importoRec)
  {
    this.importoRec = importoRec;
  }

  public Date getDataEffettuazione()
  {
    return dataEffettuazione;
  }

  public void setDataEffettuazione(Date dataEffettuazione)
  {
    this.dataEffettuazione = dataEffettuazione;
  }

  public Long getIdListaLiquidazione()
  {
    return idListaLiquidazione;
  }

  public void setIdListaLiquidazione(Long idListaLiquidazione)
  {
    this.idListaLiquidazione = idListaLiquidazione;
  }

  public Long getIdImportiLiquidati()
  {
    return idImportiLiquidati;
  }

  public void setIdImportiLiquidati(Long idImportiLiquidati)
  {
    this.idImportiLiquidati = idImportiLiquidati;
  }

  public String getFlagEsitoLiquidazione()
  {
    return flagEsitoLiquidazione;
  }

  public void setFlagEsitoLiquidazione(String flagEsitoLiquidazione)
  {
    this.flagEsitoLiquidazione = flagEsitoLiquidazione;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public String getCodiceFondo()
  {
    return codiceFondo;
  }

  public void setCodiceFondo(String codiceFondo)
  {
    this.codiceFondo = codiceFondo;
  }

  public Long getNumeroMandato()
  {
    return numeroMandato;
  }

  public void setNumeroMandato(Long numeroMandato)
  {
    this.numeroMandato = numeroMandato;
  }

  public Long getAnnoEsercizio()
  {
    return annoEsercizio;
  }

  public void setAnnoEsercizio(Long annoEsercizio)
  {
    this.annoEsercizio = annoEsercizio;
  }

  public Long getNumeroLista()
  {
    if (numeroLista != null)
      if (numeroLista.longValue() == 0)
        return null;
    return numeroLista;
  }

  public void setNumeroLista(Long numeroLista)
  {
    this.numeroLista = numeroLista;
  }

  public Date getDataApprovazione()
  {
    return dataApprovazione;
  }

  public void setDataApprovazione(Date dataApprovazione)
  {
    this.dataApprovazione = dataApprovazione;
  }

  public String getStatoLista()
  {
    if (flagStatoLista != null)
    {
      if (flagStatoLista.compareTo("B") == 0)
        return "BOZZA";
      if (flagStatoLista.compareTo("A") == 0)
        return "APPROVATA";
      if (flagStatoLista.compareTo("T") == 0)
        return "TRASMESSA A OPR";
    }

    return "DA CREARE";
  }

  public String getEsitoLiquidazione()
  {
    if (flagEsitoLiquidazione != null)
    {
      if (flagEsitoLiquidazione.compareTo("S") == 0)
        return "LIQUIDATO";
      if (flagEsitoLiquidazione.compareTo("N") == 0)
        return "IN LIQUIDAZIONE";
      if (flagEsitoLiquidazione.compareTo("R") == 0)
        return "RESPINTO";
    }

    return "";
  }

  public String getInvioASigop()
  {
    if (flagInvioSigop != null)
    {
      if (flagInvioSigop.compareTo("S") == 0)
        return "Sì";
      if (flagInvioSigop.compareTo("N") == 0)
        return "No";
    }

    return "";
  }

  public String getTecnico()
  {
    return tecnico;
  }

  public void setTecnico(String tecnico)
  {
    this.tecnico = tecnico;
  }

  public String getFlagAmmCompetenza()
  {
    return flagAmmCompetenza;
  }

  public void setFlagAmmCompetenza(String flagAmmCompetenza)
  {
    this.flagAmmCompetenza = flagAmmCompetenza;
  }

  public String getAmmCompetenzaStr()
  {

    String s = "";
    if (flagAmmCompetenza != null)
      if (flagAmmCompetenza.compareTo("S") == 0)
        return s;
    if (ammCompetenza != null)
      for (String ss : ammCompetenza)
        s += ss + "<br/>";
    return s;
  }

  public void setAmmCompetenza(List<String> ammCompetenza)
  {
    this.ammCompetenza = ammCompetenza;
  }

  public Long getIdListaLiquidazImpLiq()
  {
    return idListaLiquidazImpLiq;
  }

  public void setIdListaLiquidazImpLiq(Long idListaLiquidazImpLiq)
  {
    this.idListaLiquidazImpLiq = idListaLiquidazImpLiq;
  }

  public String getMotivoResp()
  {
    return motivoResp;
  }

  public void setMotivoResp(String motivoResp)
  {
    this.motivoResp = motivoResp;
  }

  public Long getExtIdAmmCompetenza()
  {
    return extIdAmmCompetenza;
  }

  public void setExtIdAmmCompetenza(Long extIdAmmCompetenza)
  {
    this.extIdAmmCompetenza = extIdAmmCompetenza;
  }

  public Long getDecodificaAmmCompetenza()
  {
    return decodificaAmmCompetenza;
  }

  public void setDecodificaAmmCompetenza(Long decodificaAmmCompetenza)
  {
    this.decodificaAmmCompetenza = decodificaAmmCompetenza;
  }

  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }

  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }

  public String getNumProtocollo()
  {
    return numProtocollo;
  }

  public void setNumProtocollo(String numProtocollo)
  {
    this.numProtocollo = numProtocollo;
  }

  public Date getDataProtocollo()
  {
    return dataProtocollo;
  }

  public void setDataProtocollo(Date dataProtocollo)
  {
    this.dataProtocollo = dataProtocollo;
  }

  public String getDataProtocolloStr()
  {
    return IuffiUtils.DATE.formatDate(dataProtocollo);
  }
}
