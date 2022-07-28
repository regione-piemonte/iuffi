package it.csi.iuffi.iuffiweb.dto.licenzapesca;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class VersamentoLicenzaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;
  
  private Date datapagamento;
  private Date dataemissione;
  private String dataannullamento;
  private String iuv;
  private String email;
  private String descrizionelicenza;
  private String descrizionepagamento;
  private String nome; 
  private String cognome;
  private String cf;
  private String provincia;
  private String estero;
  private String importo;
  private String descrizionezona;
  private String descrizionetipotassa;
  
  public Date getDatapagamento()
  {
    return datapagamento;
  }
  public void setDatapagamento(Date datapagamento)
  {
    this.datapagamento = datapagamento;
  }
  public String getIuv()
  {
    return iuv;
  }
  public void setIuv(String iuv)
  {
    this.iuv = iuv;
  }
  public String getDescrizionelicenza()
  {
    return descrizionelicenza;
  }
  public void setDescrizionelicenza(String descrizionelicenza)
  {
    this.descrizionelicenza = descrizionelicenza;
  }
  public String getDescrizionepagamento()
  {
    return descrizionepagamento;
  }
  public void setDescrizionepagamento(String descrizionepagamento)
  {
    this.descrizionepagamento = descrizionepagamento;
  }
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getCognome()
  {
    return cognome;
  }
  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }
  public String getCf()
  {
    return cf;
  }
  public void setCf(String cf)
  {
    this.cf = cf;
  }
  public String getProvincia()
  {
    return provincia;
  }
  public void setProvincia(String provincia)
  {
    this.provincia = provincia;
  }
  public String getEstero()
  {
    return estero;
  }
  public void setEstero(String estero)
  {
    this.estero = estero;
  }
  public String getImporto()
  {
    return importo;
  }
  public void setImporto(String importo)
  {
    this.importo = importo;
  }
  public String getDescrizionezona()
  {
    return descrizionezona;
  }
  public void setDescrizionezona(String descrizionezona)
  {
    this.descrizionezona = descrizionezona;
  }
  public String getDescrizionetipotassa()
  {
    return descrizionetipotassa;
  }
  public void setDescrizionetipotassa(String descrizionetipotassa)
  {
    this.descrizionetipotassa = descrizionetipotassa;
  }
  public Date getDataemissione()
  {
    return dataemissione;
  }
  public void setDataemissione(Date dataemissione)
  {
    this.dataemissione = dataemissione;
  }
  public String getDataannullamento()
  {
    return dataannullamento;
  }
  public void setDataannullamento(String dataannullamento)
  {
    this.dataannullamento = dataannullamento;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  
  
}
