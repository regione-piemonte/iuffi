package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProduzioneAnimale implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String codiceaziendazootecnica;
  private String specieanimale;
  private String categoriaanimale;
  private String quantita;
  private String produzionebiologica;
  private String pubblica;
  private long idallevamento;
  private long idspecieanimale;
  private long idcategoria;
  private long id;
  
  
  
  public long getIdallevamento()
  {
    return idallevamento;
  }
  public void setIdallevamento(long idallevamento)
  {
    this.idallevamento = idallevamento;
  }
  public long getIdspecieanimale()
  {
    return idspecieanimale;
  }
  public void setIdspecieanimale(long idspecieanimale)
  {
    this.idspecieanimale = idspecieanimale;
  }
  public long getIdcategoria()
  {
    return idcategoria;
  }
  public void setIdcategoria(long idcategoria)
  {
    this.idcategoria = idcategoria;
  }
  public String getCodiceaziendazootecnica()
  {
    return codiceaziendazootecnica;
  }
  public void setCodiceaziendazootecnica(String codiceaziendazootecnica)
  {
    this.codiceaziendazootecnica = codiceaziendazootecnica;
  }
  public String getSpecieanimale()
  {
    return specieanimale;
  }
  public void setSpecieanimale(String specieanimale)
  {
    this.specieanimale = specieanimale;
  }
  public String getCategoriaanimale()
  {
    return categoriaanimale;
  }
  public void setCategoriaanimale(String categoriaanimale)
  {
    this.categoriaanimale = categoriaanimale;
  }
  public String getQuantita()
  {
    return quantita;
  }
  public void setQuantita(String quantita)
  {
    this.quantita = quantita;
  }
  public String getProduzionebiologica()
  {
    return produzionebiologica;
  }
  public void setProduzionebiologica(String produzionebiologica)
  {
    this.produzionebiologica = produzionebiologica;
  }
  public String getPubblica()
  {
    return pubblica;
  }
  public void setPubblica(String pubblica)
  {
    this.pubblica = pubblica;
  }
  public long getId()
  {
    return id;
  }
  public void setId(long id)
  {
    this.id = id;
  }
  
  

  
  
}
