package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaJSONBandiNuovaListaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -9065564924011851868L;
  protected long            idBando;
  protected String          denominazioneBando;
  protected String          referenteBando;
  protected Integer         annoCampagna;
  protected Date            dataInizioBando;
  protected String          codiciLivello;
  protected String          descTipoBando;

  public String getDenominazioneBando()
  {
    return denominazioneBando;
  }

  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }

  public String getReferenteBando()
  {
    return referenteBando;
  }

  public void setReferenteBando(String referenteBando)
  {
    this.referenteBando = referenteBando;
  }

  public Integer getAnnoCampagna()
  {
    return annoCampagna;
  }

  public void setAnnoCampagna(Integer annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public String getDataInizioBando()
  {
    return IuffiUtils.DATE.formatDate(dataInizioBando);
  }

  public void setDataInizioBando(Date dataInizioBando)
  {
    this.dataInizioBando = dataInizioBando;
  }

  public String getCodiciLivello()
  {
    return codiciLivello;
  }

  public void setCodiciLivello(String codiciLivello)
  {
    this.codiciLivello = codiciLivello;
  }

  public String getDescTipoBando()
  {
    return descTipoBando;
  }

  public void setDescTipoBando(String descTipoBando)
  {
    this.descTipoBando = descTipoBando;
  }

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public String getCodiciLivelloHtml()
  {
    String s = "";
    String[] livelli = codiciLivello.split(",");

    for (int i = 0; i < livelli.length; i++)
    {
      s += livelli[i] + "<br>";

    }
    return s;
  }

  public String getCodiciLivelloForFilter()
  {
    String s = "&&&";
    String[] livelli = codiciLivello.split(",");

    for (int i = 0; i < livelli.length; i++)
    {
      s += livelli[i].trim() + "&&&";

    }
    return s;
  }

  public String getAnnoCampagnaStr()
  {
    if (annoCampagna != null)
      return annoCampagna.toString();
    return "";
  }
}
