package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ProcedimentoOggettoVO extends ProcedimentoOggettoDTO
{

  private static final long            serialVersionUID = -6613613945681780603L;

  private String                       identificativo;
  private String                       descrAmmCompetenza;
  private String                       descrAmmCompetenzaInfo;
  private String                       descrUfficioZona;
  private String                       annoCampagna;
  private String                       denominazioneBando;
  private String                       descrComune;
  private String                       descrProvincia;
  private String                       descrizione;
  private String                       statoAmm;
  private Date                         dataStatoAmm;
  private Date                         dataEsitoRna;
  private String                       numeroVercor;
  private BigDecimal                   importoConcessoTotale;
  private BigDecimal                   importoPerizia;
  private Long                         idPraticheConcessione;
  
  private String                       indirizzoSedeLegale;
  private String                       dataUltimoAggiornamento;
  private String                       cuaa;
  private String                       denominazioneAzienda;
  private String                       denominazioneIntestazione;
  private String                       denominzioneDelega;
  private String                       azione;
  private String                       azioneHref;
  private String                       titleHref;
  private long                         idAzienda;
  private long                         idProcedimento;
  private boolean                      PO;
  private List<String>                 elencoCodiciLivelli;
  private int                          codiceRaggruppamentoUltimoGruppo;
  private String                       descrUltimoGruppo;
  // dati relativi all'oggetto
  private String                       identificativoOggetto;
  private String                       tipoOggetto;
  private String                       statoOggetto;
  // Dato relativo all'estrazione
  private String                       flagEstratta;

  private BigDecimal                   importoLiquidato;
  // IUF_R_PROCEDIMENTO_LIVELLO
  private BigDecimal                   procedimImportoInvestimento;
  private BigDecimal                   procedimSpesaAmmessa;
  private BigDecimal                   procedimContributoConcesso;

  private List<LivelloDTO>             livelli;
  private List<SettoriDiProduzioneDTO> elencoSettori;
  private List<FocusAreaDTO>           elencoFocusArea;

  private String                       responsabileProcedimento;
  private String                       tecnicoIstruttore;

  private String                       descrUltimaIstanza;
  private Date                         dataUltimaIstanza;

  private int                          cntNotificheW;
  private int                          cntNotificheG;
  private int                          cntNotificheB;

  public String getIdentificativoOggetto()
  {
    return identificativoOggetto;
  }

  public void setIdentificativoOggetto(String identificativoOggetto)
  {
    this.identificativoOggetto = identificativoOggetto;
  }

  public String getTipoOggetto()
  {
    return tipoOggetto;
  }

  public void setTipoOggetto(String tipoOggetto)
  {
    this.tipoOggetto = tipoOggetto;
  }

  public String getStatoOggetto()
  {
    return statoOggetto;
  }

  public void setStatoOggetto(String statoOggetto)
  {
    this.statoOggetto = statoOggetto;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public String getDescrAmmCompetenza()
  {
    return descrAmmCompetenza;
  }

  public void setDescrAmmCompetenza(String descrAmmCompetenza)
  {
    this.descrAmmCompetenza = descrAmmCompetenza;
  }

  public String getAnnoCampagna()
  {
    return annoCampagna;
  }

  public void setAnnoCampagna(String annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public String getDenominazioneBando()
  {
    return denominazioneBando;
  }

  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(String dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public String getDenominazioneIntestazione()
  {
    return denominazioneIntestazione;
  }

  public void setDenominazioneIntestazione(String denominazioneIntestazione)
  {
    this.denominazioneIntestazione = denominazioneIntestazione;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazioneAzienda()
  {
    return denominazioneAzienda;
  }

  public void setDenominazioneAzienda(String denominazioneAzienda)
  {
    this.denominazioneAzienda = denominazioneAzienda;
  }

  public String getDenominzioneDelega()
  {
    return denominzioneDelega;
  }

  public void setDenominzioneDelega(String denominzioneDelega)
  {
    this.denominzioneDelega = denominzioneDelega;
  }

  public String getIndirizzoSedeLegale()
  {
    return indirizzoSedeLegale;
  }

  public void setIndirizzoSedeLegale(String indirizzoSedeLegale)
  {
    this.indirizzoSedeLegale = indirizzoSedeLegale;
  }

  public String getAzione()
  {
    return azione;
  }

  public void setAzione(String azione)
  {
    this.azione = azione;
  }

  public String getAzioneHref()
  {
    return azioneHref;
  }

  public void setAzioneHref(String azioneHref)
  {
    this.azioneHref = azioneHref;
  }

  public String getTitleHref()
  {
    return titleHref;
  }

  public void setTitleHref(String titleHref)
  {
    this.titleHref = titleHref;
  }

  public String getDescrComune()
  {
    return descrComune;
  }

  public void setDescrComune(String descrComune)
  {
    this.descrComune = descrComune;
  }

  public String getDescrProvincia()
  {
    return descrProvincia;
  }

  public void setDescrProvincia(String descrProvincia)
  {
    this.descrProvincia = descrProvincia;
  }

  public List<String> getElencoCodiciLivelli()
  {
    return elencoCodiciLivelli;
  }

  public String getElencoCodiciLivelliHtml()
  {
    String htmlElenco = "";
    Vector<String> tmpArray = new Vector<>();
    if (elencoCodiciLivelli != null)
    {
      for (int i = 0; i < elencoCodiciLivelli.size(); i++)
      {
        if (!tmpArray.contains(elencoCodiciLivelli.get(i)))
        {
          tmpArray.add(elencoCodiciLivelli.get(i));
          htmlElenco = htmlElenco + elencoCodiciLivelli.get(i);
          if (i < elencoCodiciLivelli.size() - 1)
          {
            htmlElenco = htmlElenco + "<br>";
          }
        }
      }
    }
    return htmlElenco;
  }

  public String getElencoCodiciLivelliMisure()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceMisura() + "&&&";

      }
    return s;
  }

  public String getElencoSettoriStr()
  {
    String s = "&&&";

    List<SettoriDiProduzioneDTO> liv = this.getElencoSettori();
    if (liv != null)
      for (SettoriDiProduzioneDTO l : liv)
      {
        s += l.getDescrizione() + "&&&";

      }
    return s;
  }

  public String getElencoFocusAreaStr()
  {
    String s = "&&&";

    List<FocusAreaDTO> liv = this.getElencoFocusArea();
    if (liv != null)
      for (FocusAreaDTO l : liv)
      {
        s += l.getCodice() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciOperazione()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceLivello() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliSottoMisure()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceSottoMisura() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliText()
  {
    String htmlElenco = "";
    Vector<String> tmpArray = new Vector<>();
    if (elencoCodiciLivelli != null)
    {
      for (int i = 0; i < elencoCodiciLivelli.size(); i++)
      {
        if (!tmpArray.contains(elencoCodiciLivelli.get(i)))
        {
          tmpArray.add(elencoCodiciLivelli.get(i));
          htmlElenco = htmlElenco + elencoCodiciLivelli.get(i);
          if (i < elencoCodiciLivelli.size() - 1)
          {
            htmlElenco = htmlElenco + "\n";
          }
        }
      }
    }
    return htmlElenco;
  }

  public void setElencoCodiciLivelli(List<String> elencoCodiciLivelli)
  {
    this.elencoCodiciLivelli = elencoCodiciLivelli;
  }

  public long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public boolean getPO()
  {
    return PO;
  }

  public void setPO(boolean pO)
  {
    PO = pO;
  }

  public String getFlagEstratta()
  {
    return flagEstratta;
  }

  public String getFlagEstrattaStr()
  {
    if (this.flagEstratta != null)
    {
      if (flagEstratta.compareTo("C") == 0)
        return "Casuale";
      if (flagEstratta.compareTo("R") == 0)
        return "Rischio";
      if (flagEstratta.compareTo("M") == 0)
        return "Manuale";
      if (flagEstratta.compareTo("D") == 0)
        return "Dichiarazioni";
    }
    return "";
  }

  public int getIdFlagEstratta()
  {
    if (this.flagEstratta != null)
    {
      if (flagEstratta.compareTo("C") == 0)
        return 0;
      if (flagEstratta.compareTo("R") == 0)
        return 1;
      if (flagEstratta.compareTo("M") == 0)
        return 2;
      if (flagEstratta.compareTo("D") == 0)
        return 3;
    }
    return -1;
  }

  public static String getFlagEstrattaFromId(long id)
  {

    if (id == 0)
      return "'C'";
    if (id == 1)
      return "'R'";
    if (id == 2)
      return "'M'";
    if (id == 3)
      return "'D'";

    return null;
  }

  public void setFlagEstratta(String flagEstratta)
  {
    this.flagEstratta = flagEstratta;
  }

  public BigDecimal getProcedimImportoInvestimento()
  {
    return procedimImportoInvestimento;
  }

  public void setProcedimImportoInvestimento(
      BigDecimal procedimImportoInvestimento)
  {
    this.procedimImportoInvestimento = procedimImportoInvestimento;
  }

  public BigDecimal getProcedimSpesaAmmessa()
  {
    return procedimSpesaAmmessa;
  }

  public void setProcedimSpesaAmmessa(BigDecimal procedimSpesaAmmessa)
  {
    this.procedimSpesaAmmessa = procedimSpesaAmmessa;
  }

  public BigDecimal getProcedimContributoConcesso()
  {
    return procedimContributoConcesso;
  }

  public void setProcedimContributoConcesso(
      BigDecimal procedimContributoConcesso)
  {
    this.procedimContributoConcesso = procedimContributoConcesso;
  }

  public BigDecimal getImportoLiquidato()
  {
    return importoLiquidato;
  }

  public void setImportoLiquidato(BigDecimal importoLiquidato)
  {
    this.importoLiquidato = importoLiquidato;
  }

  public List<LivelloDTO> getLivelli()
  {
    return livelli;
  }

  public void setLivelli(List<LivelloDTO> livelli)
  {
    this.livelli = livelli;
  }

  public List<SettoriDiProduzioneDTO> getElencoSettori()
  {
    return elencoSettori;
  }

  public void setElencoSettori(List<SettoriDiProduzioneDTO> elencoSettori)
  {
    this.elencoSettori = elencoSettori;
  }

  public List<FocusAreaDTO> getElencoFocusArea()
  {
    return elencoFocusArea;
  }

  public void setElencoFocusArea(List<FocusAreaDTO> elencoFocusArea)
  {
    this.elencoFocusArea = elencoFocusArea;
  }

  public String getResponsabileProcedimento()
  {
    return responsabileProcedimento;
  }

  public void setResponsabileProcedimento(String responsabileProcedimento)
  {
    this.responsabileProcedimento = responsabileProcedimento;
  }

  public String getTecnicoIstruttore()
  {
    return tecnicoIstruttore;
  }

  public void setTecnicoIstruttore(String tecnicoIstruttore)
  {
    this.tecnicoIstruttore = tecnicoIstruttore;
  }

  public String getDescrAmmCompetenzaInfo()
  {
    return descrAmmCompetenzaInfo;
  }

  public void setDescrAmmCompetenzaInfo(String descrAmmCompetenzaInfo)
  {
    this.descrAmmCompetenzaInfo = descrAmmCompetenzaInfo;
  }

  public String getDescrUltimaIstanza()
  {
    return descrUltimaIstanza;
  }

  public void setDescrUltimaIstanza(String descrUltimaIstanza)
  {
    this.descrUltimaIstanza = descrUltimaIstanza;
  }

  public Date getDataUltimaIstanza()
  {
    return dataUltimaIstanza;
  }

  public void setDataUltimaIstanza(Date dataUltimaIstanza)
  {
    this.dataUltimaIstanza = dataUltimaIstanza;
  }

  public String getDataUltimaIstanzaStr()
  {
    return IuffiUtils.DATE.formatDate(dataUltimaIstanza);
  }

  public String getDescrUfficioZona()
  {
    return descrUfficioZona;
  }

  public void setDescrUfficioZona(String descrUfficioZona)
  {
    this.descrUfficioZona = descrUfficioZona;
  }

  public int totNotifiche()
  {
    return cntNotificheB + cntNotificheG + cntNotificheW;
  }

  public int getCntNotificheW()
  {
    return cntNotificheW;
  }

  public void setCntNotificheW(int cntNotificheW)
  {
    this.cntNotificheW = cntNotificheW;
  }

  public int getCntNotificheG()
  {
    return cntNotificheG;
  }

  public void setCntNotificheG(int cntNotificheG)
  {
    this.cntNotificheG = cntNotificheG;
  }

  public int getCntNotificheB()
  {
    return cntNotificheB;
  }

  public void setCntNotificheB(int cntNotificheB)
  {
    this.cntNotificheB = cntNotificheB;
  }

  public String getNotificheForFilter()
  {
    String ret = "&&&";
    if (cntNotificheW > 0)
      ret += "1&&&";
    if (cntNotificheB > 0)
      ret += "3&&&";
    if (cntNotificheG > 0)
      ret += "2&&&";
    if (ret.compareTo("&&&") == 0)
      ret += "4&&&";
    return ret;
  }

  public String getNotificheHtml()
  {
    String ret = "";
    if (cntNotificheW > 0)
      ret += "<img title=\"Warning\" src=\"../img/24/warning.png\">: "
          + cntNotificheW + "<br>";
    if (cntNotificheB > 0)
      ret += "<img title=\"Bloccante\" src=\"../img/24/errorB.png\">: "
          + cntNotificheB + "<br>";
    if (cntNotificheG > 0)
      ret += "<img title=\"Grave\" src=\"../img/24/errorG.png\">: "
          + cntNotificheG + "<br>";
    return ret;
  }

  public String getStatoAmm()
  {
    return statoAmm;
  }

  public void setStatoAmm(String statoAmm)
  {
    this.statoAmm = statoAmm;
  }

  public int getCodiceRaggruppamentoUltimoGruppo()
  {
    return codiceRaggruppamentoUltimoGruppo;
  }

  public void setCodiceRaggruppamentoUltimoGruppo(
      int codiceRaggruppamentoUltimoGruppo)
  {
    this.codiceRaggruppamentoUltimoGruppo = codiceRaggruppamentoUltimoGruppo;
  }

  public String getDescrUltimoGruppo()
  {
    return descrUltimoGruppo;
  }

  public void setDescrUltimoGruppo(String descrUltimoGruppo)
  {
    this.descrUltimoGruppo = descrUltimoGruppo;
  }

  public Date getDataStatoAmm()
  {
    return dataStatoAmm;
  }

  public String getDataStatoAmmStr()
  {
    return IuffiUtils.DATE.formatDate(dataStatoAmm);
  }

  public void setDataStatoAmm(Date dataStatoAmm)
  {
    this.dataStatoAmm = dataStatoAmm;
  }

  public Date getDataEsitoRna()
  {
    return dataEsitoRna;
  }

  public String getNumeroVercor()
  {
    return numeroVercor;
  }

  public void setDataEsitoRna(Date dataEsitoRna)
  {
    this.dataEsitoRna = dataEsitoRna;
  }

  public void setNumeroVercor(String numeroVercor)
  {
    this.numeroVercor = numeroVercor;
  }

  public BigDecimal getImportoConcessoTotale()
  {
    return importoConcessoTotale;
  }

  public void setImportoConcessoTotale(BigDecimal importoConcessoTotale)
  {
    this.importoConcessoTotale = importoConcessoTotale;
  }

  public BigDecimal getImportoPerizia()
  {
    return importoPerizia;
  }

  public void setImportoPerizia(BigDecimal importoPerizia)
  {
    this.importoPerizia = importoPerizia;
  }

  public Long getIdPraticheConcessione()
  {
    return idPraticheConcessione;
  }

  public void setIdPraticheConcessione(Long idPraticheConcessione)
  {
    this.idPraticheConcessione = idPraticheConcessione;
  }

}
