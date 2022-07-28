package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianografico;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ParticellaGraficaVO implements ILoggable
{

  private static final long serialVersionUID = -8064889027606525236L;

  private String            descComune;
  private String            sezione;
  private String            foglio;
  private String            particella;
  private String            subalterno;
  private String            istatComune;
  private String            zonaAltimetricaPart;
  private String            idAnomalieHtml;
  private String            codiceUtilizzo;
  private String            descrizioneUtilizzo;
  private String            codiceDestinazione;
  private String            descrizioneDestinazione;
  private String            codiceDettaglioUso;
  private String            codiceIntervento;
  private String            descrizioneIntervento;
  private String            descrizioneDettaglioUso;
  private String            codiceQualitaUso;
  private String            descrizioneQualitaUso;
  private String            codiceVarieta;
  private String            descrizioneVarieta;
  private String            idIsol;
  private String            idParcAgri;
  private String            idAppe;
  private BigDecimal        supePrsu;
  private BigDecimal        supePrsuLorda;
  private BigDecimal        superficiePremio;
  private String            codicePremio;
  private String            descrPremio;
  private Long              idInterventoGrafico;
  private Long              idParticelleGrafico;
  private Long              idClassePremio;
  private Long              numAnomalie;

  public BigDecimal getSuperficiePremio()
  {
    return superficiePremio;
  }

  public void setSuperficiePremio(BigDecimal superficiePremio)
  {
    this.superficiePremio = superficiePremio;
  }

  public String getIdAnomalieHtml()
  {
    return idAnomalieHtml;
  }

  public void setIdAnomalieHtml(String idAnomalieHtml)
  {
    this.idAnomalieHtml = idAnomalieHtml;
  }

  public BigDecimal getSupePrsuLorda()
  {
    return supePrsuLorda;
  }

  public void setSupePrsuLorda(BigDecimal supePrsuLorda)
  {
    this.supePrsuLorda = supePrsuLorda;
  }

  public Long getIdParticelleGrafico()
  {
    return idParticelleGrafico;
  }

  public void setIdParticelleGrafico(Long idParticelleGrafico)
  {
    this.idParticelleGrafico = idParticelleGrafico;
  }

  public Long getNumAnomalie()
  {
    return numAnomalie;
  }

  public void setNumAnomalie(Long numAnomalie)
  {
    this.numAnomalie = numAnomalie;
  }

  public Long getIdInterventoGrafico()
  {
    return idInterventoGrafico;
  }

  public void setIdInterventoGrafico(Long idInterventoGrafico)
  {
    this.idInterventoGrafico = idInterventoGrafico;
  }

  public Long getIdClassePremio()
  {
    return idClassePremio;
  }

  public void setIdClassePremio(Long idClassePremio)
  {
    this.idClassePremio = idClassePremio;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getCodiceIntervento()
  {
    return codiceIntervento;
  }

  public void setCodiceIntervento(String codiceIntervento)
  {
    this.codiceIntervento = codiceIntervento;
  }

  public String getDescrizioneIntervento()
  {
    return descrizioneIntervento;
  }

  public void setDescrizioneIntervento(String descrizioneIntervento)
  {
    this.descrizioneIntervento = descrizioneIntervento;
  }

  public String getSubalterno()
  {
    return subalterno;
  }

  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public String getSezione()
  {
    return sezione;
  }

  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }

  public String getFoglio()
  {
    return foglio;
  }

  public void setFoglio(String foglio)
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

  public String getZonaAltimetricaPart()
  {
    return zonaAltimetricaPart;
  }

  public void setZonaAltimetricaPart(String zonaAltimetricaPart)
  {
    this.zonaAltimetricaPart = zonaAltimetricaPart;
  }

  public String getCodiceUtilizzo()
  {
    return codiceUtilizzo;
  }

  public void setCodiceUtilizzo(String codiceUtilizzo)
  {
    this.codiceUtilizzo = codiceUtilizzo;
  }

  public String getDescrizioneUtilizzo()
  {
    return descrizioneUtilizzo;
  }

  public void setDescrizioneUtilizzo(String descrizioneUtilizzo)
  {
    this.descrizioneUtilizzo = descrizioneUtilizzo;
  }

  public String getCodiceDestinazione()
  {
    return codiceDestinazione;
  }

  public void setCodiceDestinazione(String codiceDestinazione)
  {
    this.codiceDestinazione = codiceDestinazione;
  }

  public String getDescrizioneDestinazione()
  {
    return descrizioneDestinazione;
  }

  public void setDescrizioneDestinazione(String descrizioneDestinazione)
  {
    this.descrizioneDestinazione = descrizioneDestinazione;
  }

  public String getCodiceDettaglioUso()
  {
    return codiceDettaglioUso;
  }

  public void setCodiceDettaglioUso(String codiceDettaglioUso)
  {
    this.codiceDettaglioUso = codiceDettaglioUso;
  }

  public String getDescrizioneDettaglioUso()
  {
    return descrizioneDettaglioUso;
  }

  public void setDescrizioneDettaglioUso(String descrizioneDettaglioUso)
  {
    this.descrizioneDettaglioUso = descrizioneDettaglioUso;
  }

  public String getCodiceQualitaUso()
  {
    return codiceQualitaUso;
  }

  public void setCodiceQualitaUso(String codiceQualitaUso)
  {
    this.codiceQualitaUso = codiceQualitaUso;
  }

  public String getDescrizioneQualitaUso()
  {
    return descrizioneQualitaUso;
  }

  public void setDescrizioneQualitaUso(String descrizioneQualitaUso)
  {
    this.descrizioneQualitaUso = descrizioneQualitaUso;
  }

  public String getCodiceVarieta()
  {
    return codiceVarieta;
  }

  public void setCodiceVarieta(String codiceVarieta)
  {
    this.codiceVarieta = codiceVarieta;
  }

  public String getDescrizioneVarieta()
  {
    return descrizioneVarieta;
  }

  public void setDescrizioneVarieta(String descrizioneVarieta)
  {
    this.descrizioneVarieta = descrizioneVarieta;
  }

  public String getIdIsol()
  {
    return idIsol;
  }

  public void setIdIsol(String idIsol)
  {
    this.idIsol = idIsol;
  }

  public String getIdParcAgri()
  {
    return idParcAgri;
  }

  public void setIdParcAgri(String idParcAgri)
  {
    this.idParcAgri = idParcAgri;
  }

  public String getIdAppe()
  {
    return idAppe;
  }

  public void setIdAppe(String idAppe)
  {
    this.idAppe = idAppe;
  }

  public BigDecimal getSupePrsu()
  {
    return supePrsu;
  }

  public String getSupePrsuStr()
  {
    return IuffiUtils.FORMAT.formatDecimal4(supePrsu);
  }

  public void setSupePrsu(BigDecimal supePrsu)
  {
    this.supePrsu = supePrsu;
  }

  public String getCodicePremio()
  {
    return codicePremio;
  }

  public void setCodicePremio(String codicePremio)
  {
    this.codicePremio = codicePremio;
  }

  public String getDescrPremio()
  {
    return descrPremio;
  }

  public void setDescrPremio(String descrPremio)
  {
    this.descrPremio = descrPremio;
  }

}
