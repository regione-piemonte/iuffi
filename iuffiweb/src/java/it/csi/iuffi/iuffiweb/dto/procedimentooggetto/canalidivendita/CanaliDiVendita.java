package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CanaliDiVendita implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private Long id;
  private String amazon;
  private String canale;
  private String altrocanale;
  private String comearrivate;
  private String dettaglimercati;
  private String email;
  private String facebook;
  private byte[] immagine;
  private String descimmagine;
  private String indirizzo;
  private String instagram; 
  private String note;
  private String orari;
  private String sitoweb;
  private String telefono;
  private String idimmagine;
  private List<DecodificaDTO<Long>> listCanali;
  
  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
  {
    this.id = id;
  }
  public String getAmazon()
  {
    return amazon;
  }
  public void setAmazon(String amazon)
  {
    this.amazon = amazon;
  }
  public String getAltrocanale()
  {
    return altrocanale;
  }
  public void setAltrocanale(String altrocanale)
  {
    this.altrocanale = altrocanale;
  }
  public String getComearrivate()
  {
    return comearrivate;
  }
  public void setComearrivate(String comearrivate)
  {
    this.comearrivate = comearrivate;
  }
  public String getDettaglimercati()
  {
    return dettaglimercati;
  }
  public void setDettaglimercati(String dettaglimercati)
  {
    this.dettaglimercati = dettaglimercati;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getFacebook()
  {
    return facebook;
  }
  public void setFacebook(String facebook)
  {
    this.facebook = facebook;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getInstagram()
  {
    return instagram;
  }
  public void setInstagram(String instagram)
  {
    this.instagram = instagram;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getOrari()
  {
    return orari;
  }
  public void setOrari(String orari)
  {
    this.orari = orari;
  }
  public String getSitoweb()
  {
    return sitoweb;
  }
  public void setSitoweb(String sitoweb)
  {
    this.sitoweb = sitoweb;
  }
  public String getTelefono()
  {
    return telefono;
  }
  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }
  public String getCanale()
  {
    return canale;
  }
  public void setCanale(String canale)
  {
    this.canale = canale;
  }
  public byte[] getImmagine()
  {
    return immagine;
  }
  public void setImmagine(byte[] immagine)
  {
    this.immagine = immagine;
  }
  public String getDescimmagine()
  {
    return descimmagine;
  }
  public void setDescimmagine(String descimmagine)
  {
    this.descimmagine = descimmagine;
  }
  public List<DecodificaDTO<Long>> getListCanali()
  {
    return listCanali;
  }
  public void setListCanali(List<DecodificaDTO<Long>> listCanali)
  {
    this.listCanali = listCanali;
  }
  public String getIdimmagine()
  {
    return idimmagine;
  }
  public void setIdimmagine(String idimmagine)
  {
    this.idimmagine = idimmagine;
  }
  
  
}
