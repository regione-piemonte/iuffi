package it.csi.iuffi.iuffiweb.dto.nuovoprocedimento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DocumentoDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class LivelloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID  = 4601339650061084885L;

  private long               idLivello;
  private String             codice;
  private String             codiceLivello;
  private String             descrizione;
  private String             descrEstesa;
  private List<DocumentoDTO> documenti;

  private Long               idParticellaPremio;
  private String             codiceParticellaPremio;

  // campi per la gestione del controllo in loco
  private String             flagControllo;
  private Date               dataInizioControllo;
  private Date               dataSopralluogo;
  private String             numeroVerbale;
  private Long               extIdTecnico;
  private String             decodificaTecnico;
  private String             flagInadempVincolata;
  private String             flagInadempCondizionata;
  private String             noteInadempVincolata;
  private String             noteInadempCondizionata;
  private String             motivazione;
  private String             codiceMisura;
  private String             codiceSottoMisura;
  private String             codiceTipoLivello = "";

  private Date               dataAmmissione;
  private BigDecimal         spesaAmmessa;
  private BigDecimal         contributoConcesso;

  // private List<DecodificaDTO<Long>> livelliPossibiliCombo;

  public String getFlagInadempVincolata()
  {
    return flagInadempVincolata;
  }

  public void setFlagInadempVincolata(String flagInadempVincolata)
  {
    this.flagInadempVincolata = flagInadempVincolata;
  }

  public String getFlagInadempCondizionata()
  {
    return flagInadempCondizionata;
  }

  public void setFlagInadempCondizionata(String flagInadempCondizionata)
  {
    this.flagInadempCondizionata = flagInadempCondizionata;
  }

  public String getNoteInadempVincolata()
  {
    return noteInadempVincolata;
  }

  public void setNoteInadempVincolata(String noteInadempVincolata)
  {
    this.noteInadempVincolata = noteInadempVincolata;
  }

  public String getNoteInadempCondizionata()
  {
    return noteInadempCondizionata;
  }

  public void setNoteInadempCondizionata(String noteInadempCondizionata)
  {
    this.noteInadempCondizionata = noteInadempCondizionata;
  }

  public Date getDataInizioControllo()
  {
    return dataInizioControllo;
  }

  public void setDataInizioControllo(Date dataInizioControllo)
  {
    this.dataInizioControllo = dataInizioControllo;
  }

  public Date getDataSopralluogo()
  {
    return dataSopralluogo;
  }

  public void setDataSopralluogo(Date dataSopralluogo)
  {
    this.dataSopralluogo = dataSopralluogo;
  }

  public String getNumeroVerbale()
  {
    return numeroVerbale;
  }

  public void setNumeroVerbale(String numeroVerbale)
  {
    this.numeroVerbale = numeroVerbale;
  }

  public Long getExtIdTecnico()
  {
    return extIdTecnico;
  }

  public void setExtIdTecnico(Long extIdTecnico)
  {
    this.extIdTecnico = extIdTecnico;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
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

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getDescrEstesa()
  {
    return descrEstesa;
  }

  public void setDescrEstesa(String descrEstesa)
  {
    this.descrEstesa = descrEstesa;
  }

  public List<DocumentoDTO> getDocumenti()
  {
    return documenti;
  }

  public void setDocumenti(List<DocumentoDTO> documenti)
  {
    if (documenti != null)
      this.documenti = documenti;
  }

  public String getFlagControllo()
  {
    return flagControllo;
  }

  public String getFlagControlloStr()
  {
    if (flagControllo == null)
      return null;
    if (flagControllo.compareTo("N") == 0)
      return "No";
    else
      return "Sì";
  }

  public void setFlagControllo(String flagControllo)
  {
    this.flagControllo = flagControllo;
  }

  public String getDecodificaTecnico()
  {
    return decodificaTecnico;
  }

  public void setDecodificaTecnico(String decodificaTecnico)
  {
    this.decodificaTecnico = decodificaTecnico;
  }

  /*
   * public List<DecodificaDTO<Long>> getLivelliPossibiliCombo() { return
   * livelliPossibiliCombo; }
   * 
   * public void setLivelliPossibiliCombo(List<DecodificaDTO<Long>>
   * livelliPossibiliCombo) { this.livelliPossibiliCombo =
   * livelliPossibiliCombo; }
   */

  public String getDataInizioControlloStr()
  {
    return IuffiUtils.DATE.formatDate(dataInizioControllo);
  }

  public String getDataSopralluogoStr()
  {
    return IuffiUtils.DATE.formatDate(dataSopralluogo);
  }

  public String getCodiceDescrizione()
  {
    return codice + " - " + descrizione;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getMotivazione()
  {
    return motivazione;
  }

  public void setMotivazione(String motivazione)
  {
    this.motivazione = motivazione;
  }

  public Long getIdParticellaPremio()
  {
    return idParticellaPremio;
  }

  public void setIdParticellaPremio(Long idParticellaPremio)
  {
    this.idParticellaPremio = idParticellaPremio;
  }

  public String getCodiceParticellaPremio()
  {
    return codiceParticellaPremio;
  }

  public void setCodiceParticellaPremio(String codiceParticellaPremio)
  {
    this.codiceParticellaPremio = codiceParticellaPremio;
  }

  public String getCodiceMisura()
  {
    return codiceMisura;
  }

  public void setCodiceMisura(String codiceMisura)
  {
    this.codiceMisura = codiceMisura;
  }

  public String getCodiceSottoMisura()
  {
    return codiceSottoMisura;
  }

  public void setCodiceSottoMisura(String codiceSottoMisura)
  {
    this.codiceSottoMisura = codiceSottoMisura;
  }

  public String getCodiceTipoLivello()
  {
    return codiceTipoLivello;
  }

  public void setCodiceTipoLivello(String codiceTipoLivello)
  {
    this.codiceTipoLivello = codiceTipoLivello;
  }

  public String getClassTipoBando()
  {
    String ret = "";
    if (this.codiceTipoLivello != null)
    {
      if (this.codiceTipoLivello.contains("I"))
      {
        if (ret != "")
          ret += " ";
        ret += "checkboxMisureInvestimento";
      }
      if (this.codiceTipoLivello.contains("G"))
      {
        if (ret != "")
          ret += " ";
        ret += "checkboxMisureGal";
      }
      if (this.codiceTipoLivello.contains("P"))
      {
        if (ret != "")
          ret += " ";
        ret += "checkboxMisurePremio";
      }
    }

    return ret;
  }

  public Date getDataAmmissione()
  {
    return dataAmmissione;
  }

  public void setDataAmmissione(Date dataAmmissione)
  {
    this.dataAmmissione = dataAmmissione;
  }

  public BigDecimal getSpesaAmmessa()
  {
    return spesaAmmessa;
  }

  public void setSpesaAmmessa(BigDecimal spesaAmmessa)
  {
    this.spesaAmmessa = spesaAmmessa;
  }

  public BigDecimal getContributoConcesso()
  {
    return contributoConcesso;
  }

  public void setContributoConcesso(BigDecimal contributoConcesso)
  {
    this.contributoConcesso = contributoConcesso;
  }

}
