package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi;


import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DataCensimento implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */
  
  private Date dataCensimento;
  private String descMetodologia;
  private String campo;
  private String descrizione;
  private String unita;
  private Long id;
  private Long metodo;
  public Date getDataCensimento()
  {
    return dataCensimento;
  }
  public void setDataCensimento(Date dataCensimento)
  {
    this.dataCensimento = dataCensimento;
  }
  public String getDescMetodologia()
  {
    return descMetodologia;
  }
  public void setDescMetodologia(String descMetodologia)
  {
    this.descMetodologia = descMetodologia;
  }
  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
  {
    this.id = id;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getCampo()
  {
    return campo;
  }
  public void setCampo(String campo)
  {
    this.campo = campo;
  }
  public String getUnita()
  {
    return unita;
  }
  public void setUnita(String unita)
  {
    this.unita = unita;
  }
  public Long getMetodo()
  {
    return metodo;
  }
  public void setMetodo(Long metodo)
  {
    this.metodo = metodo;
  }
  
 
}