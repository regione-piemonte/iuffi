package it.csi.iuffi.iuffiweb.dto.nuovoprocedimento;

import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.EsitoOggettoDTO;

public class OggettoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long     serialVersionUID = 4601339650061084885L;

  private long                  idOggetto;
  private long                  idProcedimentoOggetto;
  private long                  idBandoOggetto;
  private long                  idLegameGruppoOggetto;
  private String                codice;
  private String                descrizione;
  private String                stato;
  private String                codiceDomanda;
  private String                motivoEstrazione = "";
  private String                lastEsitoDescr;
  private String                flagIstanza;
  private Date                  dataInizio;
  private Date                  dataFine;
  private Date                  dataTrasmissione;
  private String                dataTrasmissioneStr;
  private boolean               selected         = false;
  private List<QuadroDTO>       quadri;
  private List<EsitoOggettoDTO> esitiOggetto;
  private String                descrizioneGruppoOggetto;
  private String                extCodAttore;
  private String                flagAttivo;
  private Long                  idStato;
  private String                numeroProtocollo;
  private String                oggettoRicevutaDefault;
  private String                corpoRicevutaDefault;
  private String                note;
  private String                funzionarioIstruttore;

  public String getOggettoRicevutaDefault()
  {
    return oggettoRicevutaDefault;
  }

  public void setOggettoRicevutaDefault(String oggettoRicevutaDefault)
  {
    this.oggettoRicevutaDefault = oggettoRicevutaDefault;
  }

  public String getCorpoRicevutaDefault()
  {
    return corpoRicevutaDefault;
  }

  public void setCorpoRicevutaDefault(String corpoRicevutaDefault)
  {
    this.corpoRicevutaDefault = corpoRicevutaDefault;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public String getDescrizioneElencoEsiti()
  {

    String stati = "";
    int c = 0;
    for (EsitoOggettoDTO esito : esitiOggetto)
    {
      if (c > 0 && esito.isSelected())
        stati = stati + ", ";

      if (esito.isSelected())
      {
        stati = stati + esito.getDescrizione();
        c++;
      }
    }

    if (stati.trim().length() > 0)
      return descrizione + " (" + stati + ")";
    else
      return descrizione;
  }

  public String getDescrizioneElencoEsitiSenzaNonPresente()
  {

    String stati = "";
    int c = 0;
    for (EsitoOggettoDTO esito : esitiOggetto)
    {
      if (esito.getIdEsito() == 0)
        continue;

      if (c > 0)
        stati = stati + ", ";
      stati = stati + esito.getDescrizione();
      c++;
    }

    if (stati.trim().length() > 0)
      return descrizione + " (" + stati + ")";
    else
      return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public List<QuadroDTO> getQuadri()
  {
    return quadri;
  }

  public void setQuadri(List<QuadroDTO> quadri)
  {
    this.quadri = quadri;
  }

  public String getStato()
  {
    return stato;
  }

  public void setStato(String stato)
  {
    this.stato = stato;
  }

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public List<EsitoOggettoDTO> getEsitiOggetto()
  {
    return esitiOggetto;
  }

  public void setEsitiOggetto(List<EsitoOggettoDTO> esitiOggetto)
  {
    this.esitiOggetto = esitiOggetto;
  }

  public String getCodiceDomanda()
  {
    return codiceDomanda;
  }

  public void setCodiceDomanda(String codiceDomanda)
  {
    this.codiceDomanda = codiceDomanda;
  }

  public String getLastEsitoDescr()
  {
    return lastEsitoDescr;
  }

  public void setLastEsitoDescr(String lastEsitoDescr)
  {
    this.lastEsitoDescr = lastEsitoDescr;
  }

  public String getFlagIstanza()
  {
    return flagIstanza;
  }

  public void setFlagIstanza(String flagIstanza)
  {
    this.flagIstanza = flagIstanza;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getIdLegameGruppoOggetto()
  {
    return idLegameGruppoOggetto;
  }

  public void setIdLegameGruppoOggetto(long idLegameGruppoOggetto)
  {
    this.idLegameGruppoOggetto = idLegameGruppoOggetto;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected(boolean selected)
  {
    this.selected = selected;
  }

  public Date getDataTrasmissione()
  {
    return dataTrasmissione;
  }

  public void setDataTrasmissione(Date dataTrasmissione)
  {
    this.dataTrasmissione = dataTrasmissione;
  }

  public String getDescrizioneGruppoOggetto()
  {
    return descrizioneGruppoOggetto;
  }

  public void setDescrizioneGruppoOggetto(String descrizioneGruppoOggetto)
  {
    this.descrizioneGruppoOggetto = descrizioneGruppoOggetto;
  }

  public String getExtCodAttore()
  {
    return extCodAttore;
  }

  public void setExtCodAttore(String extCodAttore)
  {
    this.extCodAttore = extCodAttore;
  }

  public String getMotivoEstrazione()
  {
    return motivoEstrazione;
  }

  public void setMotivoEstrazione(String motivoEstrazione)
  {
    this.motivoEstrazione = motivoEstrazione;
  }

  public String getFlagAttivo()
  {
    return flagAttivo;
  }

  public void setFlagAttivo(String flagAttivo)
  {
    this.flagAttivo = flagAttivo;
  }

  public Long getIdStato()
  {
    return idStato;
  }

  public void setIdStato(Long idStato)
  {
    this.idStato = idStato;
  }

  public String getDataTrasmissioneStr()
  {
    return dataTrasmissioneStr;
  }

  public void setDataTrasmissioneStr(String dataTrasmissioneStr)
  {
    this.dataTrasmissioneStr = dataTrasmissioneStr;
  }

  public String getNumeroProtocollo()
  {
    return numeroProtocollo;
  }

  public void setNumeroProtocollo(String numeroProtocollo)
  {
    this.numeroProtocollo = numeroProtocollo;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getFunzionarioIstruttore()
  {
    return funzionarioIstruttore;
  }

  public void setFunzionarioIstruttore(String funzionarioIstruttore)
  {
    this.funzionarioIstruttore = funzionarioIstruttore;
  }

}
