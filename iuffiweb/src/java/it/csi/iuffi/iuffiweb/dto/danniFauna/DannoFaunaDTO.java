package it.csi.iuffi.iuffiweb.dto.danniFauna;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DannoFaunaDTO implements ILoggable
{

  // IUF_T_DANNO_FAUNA
  // IUF_T_SPECIE_FAUNA
  // IUF_D_TIPO_DANNO_FAUNA
  private Long       idDannoFauna;
  private long       idProcedimentoOggetto;
  private int        progressivo;
  private long       idDannoSpecie;
  private String     ulterioriInformazioni;
  private BigDecimal quantita;
  private Long       idUnitaMisura;
  private String     descUnitaMisura;
  private Date       dataAggiornamento;
  private long       extIdUtenteAggiornamento;

  // date IUF_D_SPECIE_FAUNA
  private long       idSpecieFauna;
  private String     descSpecieFauna;

  // IUF_R_DANNO_SPECIE
  private long       idTipoDannoFauna;
  private String     descTipoDannoFauna;

  // IUF_T_PARTICELLE_FAUNA
  private long       idParticelleFauna;
  private long       idUtilizzoDichiarato;
  private BigDecimal superficieDanneggiata;
  private BigDecimal superficieAccertata;

  // SMRGAA_V_CONDUZIONE_UTILIZZO
  private String     istatComune;
  private String     descComune;
  private String     descProvincia;
  private String     siglaProvincia;
  private String     sezione;
  private long       foglio;
  private String     particella;
  private String     subalterno;
  private long       idUtilizzo;
  private long       codTipoUtilizzo;
  private String     descTipoUtilizzo;
  private String     utilizzo;
  private String     utilizzoSecondario;
  private BigDecimal supCatastale;
  private BigDecimal superficieUtilizzata;
  private BigDecimal supUtilizzataSecondaria;
  private String     descZonaAltimetrica;
  private long       idZonaAltimetrica;
  
  private String flagUtilizzoSec;

  // PAPUA_V_UTENTE_LOGIN
  private String     descUtenteAggiornamento;
  
  //SMRGAA_V_MATRICE
  private Long idUtilizzoRiscontrato;
  private String descrizioneUtilizzoRisc;
  
  public Long getIdDannoFauna()
  {
    return idDannoFauna;
  }

  public void setIdDannoFauna(Long idDannoFauna)
  {
    this.idDannoFauna = idDannoFauna;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public int getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(int progressivo)
  {
    this.progressivo = progressivo;
  }

  public long getIdDannoSpecie()
  {
    return idDannoSpecie;
  }

  public void setIdDannoSpecie(long idDannoSpecie)
  {
    this.idDannoSpecie = idDannoSpecie;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public BigDecimal getQuantita()
  {
    return quantita;
  }

  public void setQuantita(BigDecimal quantita)
  {
    this.quantita = quantita;
  }

  public String getQuantitaStr()
  {
    return IuffiUtils.FORMAT.formatDecimal4(quantita) + " " + descUnitaMisura;
  }
  
  public String getQuantitaFrt()
  {
    return IuffiUtils.FORMAT.formatDecimal4(quantita);
  }

  public Long getIdUnitaMisura()
  {
    return idUnitaMisura;
  }

  public void setIdUnitaMisura(Long idUnitaMisura)
  {
    this.idUnitaMisura = idUnitaMisura;
  }

  public String getDescUnitaMisura()
  {
    return descUnitaMisura;
  }

  public void setDescUnitaMisura(String descUnitaMisura)
  {
    this.descUnitaMisura = descUnitaMisura;
  }

  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public long getIdSpecieFauna()
  {
    return idSpecieFauna;
  }

  public void setIdSpecieFauna(long idSpecieFauna)
  {
    this.idSpecieFauna = idSpecieFauna;
  }

  public String getDescSpecieFauna()
  {
    return descSpecieFauna;
  }

  public void setDescSpecieFauna(String descSpecieFauna)
  {
    this.descSpecieFauna = descSpecieFauna;
  }

  public long getIdTipoDannoFauna()
  {
    return idTipoDannoFauna;
  }

  public void setIdTipoDannoFauna(long idTipoDannoFauna)
  {
    this.idTipoDannoFauna = idTipoDannoFauna;
  }

  public String getDescTipoDannoFauna()
  {
    return descTipoDannoFauna;
  }

  public void setDescTipoDannoFauna(String descTipoDannoFauna)
  {
    this.descTipoDannoFauna = descTipoDannoFauna;
  }

  public long getIdParticelleFauna()
  {
    return idParticelleFauna;
  }

  public void setIdParticelleFauna(long idParticelleFauna)
  {
    this.idParticelleFauna = idParticelleFauna;
  }

  public long getIdUtilizzoDichiarato()
  {
    return idUtilizzoDichiarato;
  }

  public void setIdUtilizzoDichiarato(long idUtilizzoDichiarato)
  {
    this.idUtilizzoDichiarato = idUtilizzoDichiarato;
  }

  public BigDecimal getSuperficieDanneggiata()
  {
    return superficieDanneggiata;
  }

  public void setSuperficieDanneggiata(BigDecimal superficieDanneggiata)
  {
    this.superficieDanneggiata = superficieDanneggiata;
  }

  public BigDecimal getSuperficieAccertata()
  {
    return superficieAccertata;
  }

  public void setSuperficieAccertata(BigDecimal superficieAccertata)
  {
    this.superficieAccertata = superficieAccertata;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public String getComune()
  {
    return descComune + " (" + siglaProvincia + ") ";
  }

  public String getDescProvincia()
  {
    return descProvincia;
  }

  public void setDescProvincia(String descProvincia)
  {
    this.descProvincia = descProvincia;
  }

  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }

  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }

  public String getSezione()
  {
    return sezione;
  }

  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }

  public long getFoglio()
  {
    return foglio;
  }

  public void setFoglio(long foglio)
  {
    this.foglio = foglio;
  }

  public String getParticella()
  {
    return particella;
  }

  public void setParticella(String particella)
  {
    this.particella = particella;
  }

  public String getSubalterno()
  {
    return subalterno;
  }

  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }

  public long getIdUtilizzo()
  {
    return idUtilizzo;
  }

  public void setIdUtilizzo(long idUtilizzo)
  {
    this.idUtilizzo = idUtilizzo;
  }

  public String getDescTipoUtilizzo()
  {
    return descTipoUtilizzo;
  }

  public void setDescTipoUtilizzo(String descTipoUtilizzo)
  {
    this.descTipoUtilizzo = descTipoUtilizzo;
  }

  public long getCodTipoUtilizzo()
  {
    return codTipoUtilizzo;
  }

  public void setCodTipoUtilizzo(long codTipoUtilizzo)
  {
    this.codTipoUtilizzo = codTipoUtilizzo;
  }

  public String getUtilizzo()
  {
    return utilizzo;
  }

  public void setUtilizzo(String utilizzo)
  {
    this.utilizzo = utilizzo;
  }

  public BigDecimal getSupCatastale()
  {
    return supCatastale;
  }

  public void setSupCatastale(BigDecimal supCatastale)
  {
    this.supCatastale = supCatastale;
  }

  public BigDecimal getSuperficieUtilizzata()
  {
    return superficieUtilizzata;
  }

  public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
  {
    this.superficieUtilizzata = superficieUtilizzata;
  }

  public String getDescZonaAltimetrica()
  {
    return descZonaAltimetrica;
  }

  public void setDescZonaAltimetrica(String descZonaAltimetrica)
  {
    this.descZonaAltimetrica = descZonaAltimetrica;
  }

  public long getIdZonaAltimetrica()
  {
    return idZonaAltimetrica;
  }

  public void setIdZonaAltimetrica(long idZonaAltimetrica)
  {
    this.idZonaAltimetrica = idZonaAltimetrica;
  }

  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }

  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }

  public String getFlagUtilizzoSec()
  {
    return flagUtilizzoSec;
  }

  public void setFlagUtilizzoSec(String flagUtilizzoSec)
  {
    this.flagUtilizzoSec = flagUtilizzoSec;
  }
  
  public String getDecodeFlagUtilizzoSec() {
    if("S".equalsIgnoreCase(this.flagUtilizzoSec)) {
      return "SI";
    }else {
      return "NO";
    }
  }

  public String getUtilizzoSecondario()
  {
    return utilizzoSecondario;
  }

  public void setUtilizzoSecondario(String utilizzoSecondario)
  {
    this.utilizzoSecondario = utilizzoSecondario;
  }

  public BigDecimal getSupUtilizzataSecondaria()
  {
    return supUtilizzataSecondaria;
  }

  public void setSupUtilizzataSecondaria(BigDecimal supUtilizzataSecondaria)
  {
    this.supUtilizzataSecondaria = supUtilizzataSecondaria;
  }

  public Long getIdUtilizzoRiscontrato()
  {
    return idUtilizzoRiscontrato;
  }

  public void setIdUtilizzoRiscontrato(Long idUtilizzoRiscontrato)
  {
    this.idUtilizzoRiscontrato = idUtilizzoRiscontrato;
  }

  public String getDescrizioneUtilizzoRisc()
  {
    return descrizioneUtilizzoRisc;
  }

  public void setDescrizioneUtilizzoRisc(String descrizioneUtilizzoRisc)
  {
    this.descrizioneUtilizzoRisc = descrizioneUtilizzoRisc;
  }
}
