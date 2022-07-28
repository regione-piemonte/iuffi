package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RuoloDTO  implements ILoggable
{

  private static final long serialVersionUID = 2030054132006915082L;
  
  private Boolean amministratore;
  private Boolean funzionarioBO;
  private Boolean funzionarioLaboratorio;
  private Boolean incaricatoMonitoraggio;  
  private Boolean ispettoreMonitoraggio;  
  private Boolean supervisore;  

  public RuoloDTO()
  {
    super();
  }

  public Boolean getAmministratore()
  {
    return (amministratore==null)?false:amministratore;
  }

  public void setAmministratore(Boolean amministratore)
  {
    this.amministratore = amministratore;
  }

  public Boolean getFunzionarioBO()
  {
    return (funzionarioBO==null)?false:funzionarioBO;
  }

  public void setFunzionarioBO(Boolean funzionarioBO)
  {
    this.funzionarioBO = funzionarioBO;
  }

  public Boolean getFunzionarioLaboratorio()
  {
    return (funzionarioLaboratorio==null)?false:funzionarioLaboratorio;
  }

  public void setFunzionarioLaboratorio(Boolean funzionarioLaboratorio)
  {
    this.funzionarioLaboratorio = funzionarioLaboratorio;
  }

  public Boolean getIncaricatoMonitoraggio()
  {
    return (incaricatoMonitoraggio==null)?false:incaricatoMonitoraggio;
  }

  public void setIncaricatoMonitoraggio(Boolean incaricatoMonitoraggio)
  {
    this.incaricatoMonitoraggio = incaricatoMonitoraggio;
  }

  public Boolean getIspettoreMonitoraggio()
  {
    return (ispettoreMonitoraggio==null)?false:ispettoreMonitoraggio;
  }

  public void setIspettoreMonitoraggio(Boolean ispettoreMonitoraggio)
  {
    this.ispettoreMonitoraggio = ispettoreMonitoraggio;
  }

  public Boolean getSupervisore()
  {
    return (supervisore==null)?false:supervisore;
  }

  public void setSupervisore(Boolean supervisore)
  {
    this.supervisore = supervisore;
  }

}
