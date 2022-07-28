package it.csi.iuffi.iuffiweb.model.api;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.TabelleStoricizzateDTO;

public class RiepilogoMonitoraggio extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -2462590351383899976L;
  
  @JsonIgnore
  private Integer idSpecieOnPeriodo;
  private Integer idSpecieVegetale;
  private Integer idTipoCampione;
  private Integer mese;
  private Integer idOrganismoNocivo;

  
  public RiepilogoMonitoraggio()
  {
    super();
  }
  
  public RiepilogoMonitoraggio(Integer idSpecieOnPeriodo)
  {
    super();
    this.idSpecieOnPeriodo = idSpecieOnPeriodo;
  }

  public RiepilogoMonitoraggio(Integer idSpecieVegetale, Integer idTipoCampione,
      Integer mese, Integer idOrganismoNocivo)
  {
    super();
    this.idSpecieVegetale = idSpecieVegetale;
    this.idTipoCampione = idTipoCampione;
    this.mese = mese;
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public Integer getIdSpecieOnPeriodo()
  {
    return idSpecieOnPeriodo;
  }

  public void setIdSpecieOnPeriodo(Integer idSpecieOnPeriodo)
  {
    this.idSpecieOnPeriodo = idSpecieOnPeriodo;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }
  public Integer getIdTipoCampione()
  {
    return idTipoCampione;
  }
  public void setIdTipoCampione(Integer idTipoCampione)
  {
    this.idTipoCampione = idTipoCampione;
  }
  public Integer getMese()
  {
    return mese;
  }
  public void setMese(Integer mese)
  {
    this.mese = mese;
  }
  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }
  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((idOrganismoNocivo == null) ? 0 : idOrganismoNocivo.hashCode());
    result = prime * result
        + ((idSpecieVegetale == null) ? 0 : idSpecieVegetale.hashCode());
    result = prime * result
        + ((idTipoCampione == null) ? 0 : idTipoCampione.hashCode());
    result = prime * result + ((mese == null) ? 0 : mese.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RiepilogoMonitoraggio other = (RiepilogoMonitoraggio) obj;
    if (idOrganismoNocivo == null)
    {
      if (other.idOrganismoNocivo != null)
        return false;
    }
    else
      if (!idOrganismoNocivo.equals(other.idOrganismoNocivo))
        return false;
    if (idSpecieVegetale == null)
    {
      if (other.idSpecieVegetale != null)
        return false;
    }
    else
      if (!idSpecieVegetale.equals(other.idSpecieVegetale))
        return false;
    if (idTipoCampione == null)
    {
      if (other.idTipoCampione != null)
        return false;
    }
    else
      if (!idTipoCampione.equals(other.idTipoCampione))
        return false;
    if (mese == null)
    {
      if (other.mese != null)
        return false;
    }
    else
      if (!mese.equals(other.mese))
        return false;
    return true;
  }

}
