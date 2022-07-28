package it.csi.iuffi.iuffiweb.dto.danniFauna;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DannoDaFaunaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -5742804233093011948L;

  private Date              dataDanno;
  private String            provincia;
  private String            comune;
  private String            idIstatComune;
  private String            idIstatProvincia;
  private Long              idIstituto;
  private String            istituto;
  private Long              idNominativo;
  private String            nominativo;
  private Boolean           urgenzaPerizia;
  private Long              idMotivoUrgenza;
  private String            motivazione;
  private String            note;
  private String            noteUrgenza;
  private long              idProcedimentoOggetto;

  public String getIdIstatComune()
  {
    return idIstatComune;
  }

  public void setIdIstatComune(String idIstatComune)
  {
    this.idIstatComune = idIstatComune;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public Date getDataDanno()
  {
    return dataDanno;
  }

  public String getDataDannoFormatted()
  {
    return IuffiUtils.DATE.formatDate(dataDanno);
  }

  public void setDataDanno(Date dataDanno)
  {
    this.dataDanno = dataDanno;
  }

  public String getProvincia()
  {
    return provincia;
  }

  public void setProvincia(String provincia)
  {
    this.provincia = provincia;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public Long getIdIstituto()
  {
    return idIstituto;
  }

  public void setIdIstituto(Long idIstituto)
  {
    this.idIstituto = idIstituto;
  }

  public String getIstituto()
  {
    return istituto;
  }

  public void setIstituto(String istituto)
  {
    this.istituto = istituto;
  }

  public Long getIdNominativo()
  {
    return idNominativo;
  }

  public void setIdNominativo(Long idNominativo)
  {
    this.idNominativo = idNominativo;
  }

  public String getNominativo()
  {
    return nominativo;
  }

  public void setNominativo(String nominativo)
  {
    this.nominativo = nominativo;
  }

  public Boolean getUrgenzaPerizia()
  {
    return urgenzaPerizia;
  }

  public void setUrgenzaPerizia(Boolean urgenzaPerizia)
  {
    this.urgenzaPerizia = urgenzaPerizia;
  }

  public String getMotivazione()
  {
    return motivazione;
  }

  public void setMotivazione(String motivazione)
  {
    this.motivazione = motivazione;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getIdIstatProvincia()
  {
    return idIstatProvincia;
  }

  public void setIdIstatProvincia(String idIstatProvincia)
  {
    this.idIstatProvincia = idIstatProvincia;
  }

  public Long getIdMotivoUrgenza()
  {
    return idMotivoUrgenza;
  }

  public void setIdMotivoUrgenza(Long idMotivoUrgenza)
  {
    this.idMotivoUrgenza = idMotivoUrgenza;
  }

  public String getNoteUrgenza()
  {
    return noteUrgenza;
  }

  public void setNoteUrgenza(String noteUrgenza)
  {
    this.noteUrgenza = noteUrgenza;
  }

}
