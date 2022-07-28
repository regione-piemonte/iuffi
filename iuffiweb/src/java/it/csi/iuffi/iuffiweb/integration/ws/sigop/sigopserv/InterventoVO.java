
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per interventoVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="interventoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fondiCollegati" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idIntervento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="riferimentoIuffiWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interventoVO", propOrder =
{
    "codice",
    "denominazione",
    "fondiCollegati",
    "idIntervento",
    "riferimentoPSR"
})
public class InterventoVO
{

  protected String codice;
  protected String denominazione;
  protected String fondiCollegati;
  protected Long   idIntervento;
  protected String riferimentoPSR;

  /**
   * Recupera il valore della proprietà codice.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodice()
  {
    return codice;
  }

  /**
   * Imposta il valore della proprietà codice.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodice(String value)
  {
    this.codice = value;
  }

  /**
   * Recupera il valore della proprietà denominazione.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDenominazione()
  {
    return denominazione;
  }

  /**
   * Imposta il valore della proprietà denominazione.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDenominazione(String value)
  {
    this.denominazione = value;
  }

  /**
   * Recupera il valore della proprietà fondiCollegati.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getFondiCollegati()
  {
    return fondiCollegati;
  }

  /**
   * Imposta il valore della proprietà fondiCollegati.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setFondiCollegati(String value)
  {
    this.fondiCollegati = value;
  }

  /**
   * Recupera il valore della proprietà idIntervento.
   * 
   * @return possible object is {@link Long }
   * 
   */
  public Long getIdIntervento()
  {
    return idIntervento;
  }

  /**
   * Imposta il valore della proprietà idIntervento.
   * 
   * @param value
   *          allowed object is {@link Long }
   * 
   */
  public void setIdIntervento(Long value)
  {
    this.idIntervento = value;
  }

  /**
   * Recupera il valore della proprietà riferimentoPSR.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getRiferimentoPSR()
  {
    return riferimentoPSR;
  }

  /**
   * Imposta il valore della proprietà riferimentoPSR.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setRiferimentoPSR(String value)
  {
    this.riferimentoPSR = value;
  }

}
