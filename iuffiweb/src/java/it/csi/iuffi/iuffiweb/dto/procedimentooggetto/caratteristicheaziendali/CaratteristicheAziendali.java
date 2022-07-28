package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CaratteristicheAziendali implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String                 descrizione;
  private String                 denominazione;
  private String                 altrotipoattivita;
  private String                 cododc;
  private String                 descodc;
  private String                 descmetodocolt;
  private String                 descfiliera;
  private String                 descmulti;
  private String                 altrafiliera;
  private String                 altrafunz;
  private String                 desctrasformazione;
  private String                 desccatattiva;
  private List<DecodificaDTO<Long>> listTipoAttivita;
  private List<DecodificaDTO<Long>> listMetodiColtivazione;
  private List<DecodificaDTO<Long>> listFiliereProduttive;
  private List<DecodificaDTO<Long>> listMultifunzionalita;
  private Long id;
  public List<DecodificaDTO<Long>> getListTipoAttivita()
  {
    return listTipoAttivita;
  }
  public void setListTipoAttivita(List<DecodificaDTO<Long>> listTipoAttivita)
  {
    this.listTipoAttivita = listTipoAttivita;
  }
  public List<DecodificaDTO<Long>> getListMetodiColtivazione()
  {
    return listMetodiColtivazione;
  }
  public void setListMetodiColtivazione(
      List<DecodificaDTO<Long>> listMetodiColtivazione)
  {
    this.listMetodiColtivazione = listMetodiColtivazione;
  }
  public List<DecodificaDTO<Long>> getListFiliereProduttive()
  {
    return listFiliereProduttive;
  }
  public void setListFiliereProduttive(
      List<DecodificaDTO<Long>> listFiliereProduttive)
  {
    this.listFiliereProduttive = listFiliereProduttive;
  }
  public List<DecodificaDTO<Long>> getListMultifunzionalita()
  {
    return listMultifunzionalita;
  }
  public void setListMultifunzionalita(
      List<DecodificaDTO<Long>> listMultifunzionalita)
  {
    this.listMultifunzionalita = listMultifunzionalita;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getAltrotipoattivita()
  {
    return altrotipoattivita;
  }
  public void setAltrotipoattivita(String altrotipoattivita)
  {
    this.altrotipoattivita = altrotipoattivita;
  }
  public String getDescodc()
  {
    return descodc;
  }
  public void setDescodc(String descodc)
  {
    this.descodc = descodc;
  }
  public String getDescmetodocolt()
  {
    return descmetodocolt;
  }
  public void setDescmetodocolt(String descmetodocolt)
  {
    this.descmetodocolt = descmetodocolt;
  }
  public String getDescfiliera()
  {
    return descfiliera;
  }
  public void setDescfiliera(String descfiliera)
  {
    this.descfiliera = descfiliera;
  }
  public String getDescmulti()
  {
    return descmulti;
  }
  public void setDescmulti(String descmulti)
  {
    this.descmulti = descmulti;
  }
  public String getAltrafiliera()
  {
    return altrafiliera;
  }
  public void setAltrafiliera(String altrafiliera)
  {
    this.altrafiliera = altrafiliera;
  }
  public String getAltrafunz()
  {
    return altrafunz;
  }
  public void setAltrafunz(String altrafunz)
  {
    this.altrafunz = altrafunz;
  }
  public String getDesctrasformazione()
  {
    return desctrasformazione;
  }
  public void setDesctrasformazione(String desctrasformazione)
  {
    this.desctrasformazione = desctrasformazione;
  }
  public String getDesccatattiva()
  {
    return desccatattiva;
  }
  public void setDesccatattiva(String desccatattiva)
  {
    this.desccatattiva = desccatattiva;
  }
  public String getCododc()
  {
    return cododc;
  }
  public void setCododc(String cododc)
  {
    this.cododc = cododc;
  }
  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
  {
    this.id = id;
  }
  
  
}
