package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class EsitoFinaleDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idEsitoTecnico;
  private Long              idEsitoIstrut;
  private Long              extIdFunzionarioIstruttore;
  private Long              extIdFunzionarioGradoSup;

  private Long              idEsitoDefinitivo;
  private Long              idEsitoFinale;
  private String            descrEsitoDefinitivo;
  private long              idTecnico;
  private long              idEsito;
  private Long              idEsitoLong;
  private Long              idGradoSup;
  private String            prescrizioni;
  private String            note;
  private String            motivazione;

  private String            descrEsito;
  private String            descrTecnico;
  private String            descrGradoSup;
  private String            codiceEsito;
  private Date              dataAmmissione;

  private String            tipoAtto;
  private String            numeroAtto;
  private Date              dataAtto;
  private String            flagAltreInfoAtto;
  private Long              idTipoAtto;

  public String getTipoAtto()
  {
    return tipoAtto;
  }

  public void setTipoAtto(String tipoAtto)
  {
    this.tipoAtto = tipoAtto;
  }

  public String getNumeroAtto()
  {
    return numeroAtto;
  }

  public void setNumeroAtto(String numeroAtto)
  {
    this.numeroAtto = numeroAtto;
  }

  public Date getDataAtto()
  {
    return dataAtto;
  }

  public void setDataAtto(Date dataAtto)
  {
    this.dataAtto = dataAtto;
  }

  public long getIdEsitoTecnico()
  {
    return idEsitoTecnico;
  }

  public void setIdEsitoTecnico(long idEsitoTecnico)
  {
    this.idEsitoTecnico = idEsitoTecnico;
  }

  public long getIdTecnico()
  {
    return idTecnico;
  }

  public void setIdTecnico(long idTecnico)
  {
    this.idTecnico = idTecnico;
  }

  public Long getIdGradoSup()
  {
    return idGradoSup;
  }

  public void setIdGradoSup(Long idGradoSup)
  {
    this.idGradoSup = idGradoSup;
  }

  public String getPrescrizioni()
  {
    return prescrizioni;
  }

  public String getPrescrizioniHtml()
  {
    return (prescrizioni != null)
        ? IuffiUtils.STRING.safeHTMLText(prescrizioni) : null;
  }

  public void setPrescrizioni(String prescrizioni)
  {
    this.prescrizioni = prescrizioni;
  }

  public String getNote()
  {
    return note;
  }

  public String getNoteHtml()
  {
    return (note != null) ? IuffiUtils.STRING.safeHTMLText(note) : null;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public String getDescrEsito()
  {
    return descrEsito;
  }

  public void setDescrEsito(String descrEsito)
  {
    this.descrEsito = descrEsito;
  }

  public String getDescrTecnico()
  {
    return descrTecnico;
  }

  public void setDescrTecnico(String descrTecnico)
  {
    this.descrTecnico = descrTecnico;
  }

  public String getDescrGradoSup()
  {
    return descrGradoSup;
  }

  public void setDescrGradoSup(String descrGradoSup)
  {
    this.descrGradoSup = descrGradoSup;
  }

  public Long getIdEsitoIstrut()
  {
    return idEsitoIstrut;
  }

  public void setIdEsitoIstrut(Long idEsitoIstrut)
  {
    this.idEsitoIstrut = idEsitoIstrut;
  }

  public String getCodiceEsito()
  {
    return codiceEsito;
  }

  public void setCodiceEsito(String codiceEsito)
  {
    this.codiceEsito = codiceEsito;
  }

  public String getMotivazione()
  {
    return motivazione;
  }

  public void setMotivazione(String motivazione)
  {
    this.motivazione = motivazione;
  }

  public Long getExtIdFunzionarioIstruttore()
  {
    return extIdFunzionarioIstruttore;
  }

  public void setExtIdFunzionarioIstruttore(Long extIdFunzionarioIstruttore)
  {
    this.extIdFunzionarioIstruttore = extIdFunzionarioIstruttore;
  }

  public Long getIdEsitoLong()
  {
    return idEsitoLong;
  }

  public void setIdEsitoLong(Long idEsitoLong)
  {
    this.idEsitoLong = idEsitoLong;
  }

  public Date getDataAmmissione()
  {
    return dataAmmissione;
  }

  public void setDataAmmissione(Date dataAmmissione)
  {
    this.dataAmmissione = dataAmmissione;
  }

  public String getFlagAltreInfoAtto()
  {
    return flagAltreInfoAtto;
  }

  public void setFlagAltreInfoAtto(String flagAltreInfoAtto)
  {
    this.flagAltreInfoAtto = flagAltreInfoAtto;
  }

  public Long getIdTipoAtto()
  {
    return idTipoAtto;
  }

  public void setIdTipoAtto(Long idTipoAtto)
  {
    this.idTipoAtto = idTipoAtto;
  }

  public String getDataAttoStr()
  {
    return IuffiUtils.DATE.formatDate(dataAtto);
  }

  public Long getExtIdFunzionarioGradoSup()
  {
    return extIdFunzionarioGradoSup;
  }

  public void setExtIdFunzionarioGradoSup(Long extIdFunzionarioGradoSup)
  {
    this.extIdFunzionarioGradoSup = extIdFunzionarioGradoSup;
  }

  public Long getIdEsitoDefinitivo()
  {
    return idEsitoDefinitivo;
  }

  public void setIdEsitoDefinitivo(Long idEsitoDefinitivo)
  {
    this.idEsitoDefinitivo = idEsitoDefinitivo;
  }

  public Long getIdEsitoFinale()
  {
    return idEsitoFinale;
  }

  public void setIdEsitoFinale(Long idEsitoFinale)
  {
    this.idEsitoFinale = idEsitoFinale;
  }

  public String getDescrEsitoDefinitivo()
  {
    return descrEsitoDefinitivo;
  }

  public void setDescrEsitoDefinitivo(String descrEsitoDefinitivo)
  {
    this.descrEsitoDefinitivo = descrEsitoDefinitivo;
  }

}
