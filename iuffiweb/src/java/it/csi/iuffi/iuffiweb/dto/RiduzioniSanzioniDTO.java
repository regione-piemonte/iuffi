package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RiduzioniSanzioniDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -6030784934941693005L;

  private Long              idProcOggSanzione;
  private Long              idProcOggSanzioneSecondRecordAfterSplit;

  private String            note;
  private String            noteB;

  private String            operazione;
  private Long              idOperazione;
  private BigDecimal        importo;
  private String            tipologia;
  private Long              idTipologia;
  private String            descrizione;
  private String            descrizioneSecondRecordAfterSplit;

  private Long              idDescrizione;
  private Long              idDescrizioneSecondRecordAfterSplit;

  private boolean           splitted         = false;
  private BigDecimal        importoFirstRecord;
  private BigDecimal        importoSecondRecordAfterSplit;
  private BigDecimal        importoCalcolato;

  public Long getIdOperazione()
  {
    return idOperazione;
  }

  public void setIdOperazione(Long idOperazione)
  {
    this.idOperazione = idOperazione;
  }

  public Long getIdTipologia()
  {
    return idTipologia;
  }

  public void setIdTipologia(Long idTipologia)
  {
    this.idTipologia = idTipologia;
  }

  public Long getIdDescrizione()
  {
    return idDescrizione;
  }

  public void setIdDescrizione(Long idDescrizione)
  {
    this.idDescrizione = idDescrizione;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getOperazione()
  {
    return operazione;
  }

  public void setOperazione(String operazione)
  {
    this.operazione = operazione;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public String getImportoStr()
  {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);
    return df.format(importo);
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public String getTipologia()
  {
    return tipologia;
  }

  public void setTipologia(String tipologia)
  {
    this.tipologia = tipologia;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Long getIdProcOggSanzione()
  {
    return idProcOggSanzione;
  }

  public void setIdProcOggSanzione(Long idProcOggSanzione)
  {
    this.idProcOggSanzione = idProcOggSanzione;
  }

  public String getIdTipologiaSanzioneInvestimento()
  {
    return idTipologia + "-" + idDescrizione;
  }

  public boolean isSplitted()
  {
    return splitted;
  }

  public void setSplitted(boolean splitted)
  {
    this.splitted = splitted;
  }

  public String getDescrizioneSecondRecordAfterSplit()
  {
    return descrizioneSecondRecordAfterSplit;
  }

  public void setDescrizioneSecondRecordAfterSplit(
      String descrizioneSecondRecordAfterSplit)
  {
    this.descrizioneSecondRecordAfterSplit = descrizioneSecondRecordAfterSplit;
  }

  public BigDecimal getImportoFirstRecord()
  {
    return importoFirstRecord;
  }

  public void setImportoFirstRecord(BigDecimal importoFirstRecord)
  {
    this.importoFirstRecord = importoFirstRecord;
  }

  public BigDecimal getImportoSecondRecordAfterSplit()
  {
    return importoSecondRecordAfterSplit;
  }

  public void setImportoSecondRecordAfterSplit(
      BigDecimal importoSecondRecordAfterSplit)
  {
    this.importoSecondRecordAfterSplit = importoSecondRecordAfterSplit;
  }

  public String getNoteB()
  {
    return noteB;
  }

  public void setNoteB(String noteB)
  {
    this.noteB = noteB;
  }

  public Long getIdProcOggSanzioneSecondRecordAfterSplit()
  {
    return idProcOggSanzioneSecondRecordAfterSplit;
  }

  public void setIdProcOggSanzioneSecondRecordAfterSplit(
      Long idProcOggSanzioneSecondRecordAfterSplit)
  {
    this.idProcOggSanzioneSecondRecordAfterSplit = idProcOggSanzioneSecondRecordAfterSplit;
  }

  public Long getIdDescrizioneSecondRecordAfterSplit()
  {
    return idDescrizioneSecondRecordAfterSplit;
  }

  public void setIdDescrizioneSecondRecordAfterSplit(
      Long idDescrizioneSecondRecordAfterSplit)
  {
    this.idDescrizioneSecondRecordAfterSplit = idDescrizioneSecondRecordAfterSplit;
  }

  public BigDecimal getImportoCalcolato()
  {
    return importoCalcolato;
  }

  public void setImportoCalcolato(BigDecimal importoCalcolato)
  {
    this.importoCalcolato = importoCalcolato;
  }

}
