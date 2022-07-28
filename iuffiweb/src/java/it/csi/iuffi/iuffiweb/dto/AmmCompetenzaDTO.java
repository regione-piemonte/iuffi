package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AmmCompetenzaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              extIdUtenteAggiornamento;
  private long              idAmmCompetenza;
  private String            descrizione;
  private String            descEstesaTipoAmministraz;
  private String            note;
  private String            descrAggiuntiva;
  private String            uffIndirizzo;
  private String            uffDenom;
  private String            uffComune;
  private Date              dataInizio;
  private Long              idBando;
  private String            descBreveTipoAmministraz;
  private String            descrizioneTecnico;

  public Long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(Long idBando)
  {
    this.idBando = idBando;
  }

  public String getDescBreveTipoAmministraz()
  {
    return descBreveTipoAmministraz;
  }

  public void setDescBreveTipoAmministraz(String descBreveTipoAmministraz)
  {
    this.descBreveTipoAmministraz = descBreveTipoAmministraz;
  }

  public AmmCompetenzaDTO()
  {
    super();
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public long getIdAmmCompetenza()
  {
    return idAmmCompetenza;
  }

  public void setIdAmmCompetenza(long idAmmCompetenza)
  {
    this.idAmmCompetenza = idAmmCompetenza;
  }

  public String getDescEstesaTipoAmministraz()
  {
    return descEstesaTipoAmministraz;
  }

  public void setDescEstesaTipoAmministraz(String descEstesaTipoAmministraz)
  {
    this.descEstesaTipoAmministraz = descEstesaTipoAmministraz;
  }

  public String getUffIndirizzo()
  {
    return uffIndirizzo;
  }

  public void setUffIndirizzo(String uffIndirizzo)
  {
    this.uffIndirizzo = uffIndirizzo;
  }

  public String getUffDenom()
  {
    return uffDenom;
  }

  public void setUffDenom(String uffDenom)
  {
    this.uffDenom = uffDenom;
  }

  public String getUffComune()
  {
    return uffComune;
  }

  public void setUffComune(String uffComune)
  {
    this.uffComune = uffComune;
  }

  public String getDescrizioneUfficioZona()
  {
    if (this.uffDenom != null)
    {
      if (this.uffIndirizzo != null)
        if (uffComune != null && uffComune.trim().length() > 0)
          return this.uffDenom + ", " + this.uffIndirizzo + " "
              + this.uffComune;
        else
          return this.uffDenom + ", " + this.uffIndirizzo;
      else
        return this.uffDenom;
    }
    else
    {
      return "";
    }
  }

  public String getDescrAggiuntiva()
  {
    return descrAggiuntiva;
  }

  public void setDescrAggiuntiva(String descrAggiuntiva)
  {
    this.descrAggiuntiva = descrAggiuntiva;
  }

  public String getDescrizioneTecnico()
  {
    return descrizioneTecnico;
  }

  public void setDescrizioneTecnico(String descrizioneTecnico)
  {
    this.descrizioneTecnico = descrizioneTecnico;
  }

}
