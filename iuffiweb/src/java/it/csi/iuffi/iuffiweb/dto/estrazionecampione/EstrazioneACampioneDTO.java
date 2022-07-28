package it.csi.iuffi.iuffiweb.dto.estrazionecampione;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class EstrazioneACampioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 5918223009553272325L;

  private Long              idEstrazioneCampione;
  private Date              dataEstrazione;
  private String            descrizione;
  private Long              idTipoEstrazione;
  private String            descrizioneTipologia;
  private String            flagEstratta;
  private Long              numeroEstrazione;
  private Date              dataAnnullamento;

  public String getIdentificativo()
  {
    if (dataAnnullamento == null)
      return idEstrazioneCampione.toString();
    else
      return idEstrazioneCampione.toString() + " - ANNULLATA";
  }

  public Long getIdEstrazioneCampione()
  {
    return idEstrazioneCampione;
  }

  public void setIdEstrazioneCampione(Long idEstrazioneCampione)
  {
    this.idEstrazioneCampione = idEstrazioneCampione;
  }

  public Date getDataEstrazione()
  {
    return dataEstrazione;
  }

  public void setDataEstrazione(Date dataEstrazione)
  {
    this.dataEstrazione = dataEstrazione;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Long getIdTipoEstrazione()
  {
    return idTipoEstrazione;
  }

  public void setIdTipoEstrazione(Long idTipoEstrazione)
  {
    this.idTipoEstrazione = idTipoEstrazione;
  }

  public String getDescrizioneTipologia()
  {
    return descrizioneTipologia;
  }

  public void setDescrizioneTipologia(String descrizioneTipologia)
  {
    this.descrizioneTipologia = descrizioneTipologia;
  }

  public String getDataEstrazioneStr()
  {
    return IuffiUtils.DATE.formatDate(dataEstrazione);
  }

  public String getFlagEstratta()
  {
    return flagEstratta;
  }

  public void setFlagEstratta(String flagEstratta)
  {
    this.flagEstratta = flagEstratta;
  }

  public Long getIdFlagEstratta()
  {
    if (flagEstratta != null)
    {
      if (flagEstratta.compareTo("S") == 0)
        return Long.valueOf(0);

      if (flagEstratta.compareTo("D") == 0)
        return Long.valueOf(1);

      return Long.valueOf(-1);
    }
    return null;
  }

  public Long getNumeroEstrazione()
  {
    return numeroEstrazione;
  }

  public void setNumeroEstrazione(Long numeroEstrazione)
  {
    this.numeroEstrazione = numeroEstrazione;
  }

  public Date getDataAnnullamento()
  {
    return dataAnnullamento;
  }

  public void setDataAnnullamento(Date dataAnnullamento)
  {
    this.dataAnnullamento = dataAnnullamento;
  }

}
