package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class GraduatoriaDTO implements ILoggable
{

  private static final long     serialVersionUID = -4204688476201096156L;

  private long                  idGraduatoria;
  private String                descrizione;
  private List<FileAllegatoDTO> elencoAllegati;
  private Date                  dataApprovazione;
  private String                ordinamento1, ordinamento2, ordinamento3,
      ordinamento4, ordinamento5;
  private BigDecimal            ord1Val, ord2Val, ord3Val, ord4Val, ord5Val;
  private String                flagIstruttoria;
  private long                  posizione;
  private String                raggruppamento;
  private String                note;

  public String getDescrizioneRaggruppamento()
  {
    if (raggruppamento != null)
      return descrizione + " " + raggruppamento;
    else
      return descrizione;
  }

  public String getOrdinamento1()
  {
    return ordinamento1;
  }

  public void setOrdinamento1(String ordinamento1)
  {
    this.ordinamento1 = ordinamento1;
  }

  public String getOrdinamento2()
  {
    return ordinamento2;
  }

  public void setOrdinamento2(String ordinamento2)
  {
    this.ordinamento2 = ordinamento2;
  }

  public String getOrdinamento3()
  {
    return ordinamento3;
  }

  public void setOrdinamento3(String ordinamento3)
  {
    this.ordinamento3 = ordinamento3;
  }

  public String getOrdinamento4()
  {
    return ordinamento4;
  }

  public void setOrdinamento4(String ordinamento4)
  {
    this.ordinamento4 = ordinamento4;
  }

  public String getOrdinamento5()
  {
    return ordinamento5;
  }

  public void setOrdinamento5(String ordinamento5)
  {
    this.ordinamento5 = ordinamento5;
  }

  public BigDecimal getOrd1Val()
  {
    return ord1Val;
  }

  public void setOrd1Val(BigDecimal ord1Val)
  {
    this.ord1Val = ord1Val;
  }

  public BigDecimal getOrd2Val()
  {
    return ord2Val;
  }

  public void setOrd2Val(BigDecimal ord2Val)
  {
    this.ord2Val = ord2Val;
  }

  public BigDecimal getOrd3Val()
  {
    return ord3Val;
  }

  public void setOrd3Val(BigDecimal ord3Val)
  {
    this.ord3Val = ord3Val;
  }

  public BigDecimal getOrd4Val()
  {
    return ord4Val;
  }

  public void setOrd4Val(BigDecimal ord4Val)
  {
    this.ord4Val = ord4Val;
  }

  public BigDecimal getOrd5Val()
  {
    return ord5Val;
  }

  public void setOrd5Val(BigDecimal ord5Val)
  {
    this.ord5Val = ord5Val;
  }

  public String getFlagIstruttoria()
  {
    return flagIstruttoria;
  }

  public void setFlagIstruttoria(String flagIstruttoria)
  {
    this.flagIstruttoria = flagIstruttoria;
  }

  public long getPosizione()
  {
    return posizione;
  }

  public void setPosizione(long posizione)
  {
    this.posizione = posizione;
  }

  public String getRaggruppamento()
  {
    return raggruppamento;
  }

  public void setRaggruppamento(String raggruppamento)
  {
    this.raggruppamento = raggruppamento;
  }

  public long getIdGraduatoria()
  {
    return idGraduatoria;
  }

  public void setIdGraduatoria(long idGraduatoria)
  {
    this.idGraduatoria = idGraduatoria;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<FileAllegatoDTO> getElencoAllegati()
  {
    return elencoAllegati;
  }

  public void setElencoAllegati(List<FileAllegatoDTO> elencoAllegati)
  {
    this.elencoAllegati = elencoAllegati;
  }

  public Date getDataApprovazione()
  {
    return dataApprovazione;
  }

  public void setDataApprovazione(Date dataApprovazione)
  {
    this.dataApprovazione = dataApprovazione;
  }

  public String getDataApprovazioneStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataApprovazione);
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

}
