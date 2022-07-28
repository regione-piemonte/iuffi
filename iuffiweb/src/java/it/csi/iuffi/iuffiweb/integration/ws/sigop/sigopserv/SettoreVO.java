
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per settoreVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="settoreVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codiceFondo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneSettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFondo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "settoreVO", propOrder =
{
    "codiceFondo",
    "denominazione",
    "descrizioneSettore",
    "idFondo"
})
@XmlSeeAlso(
{
    SettoreExtVO.class
})
public class SettoreVO
{

  protected String codiceFondo;
  protected String denominazione;
  protected String descrizioneSettore;
  protected Long   idFondo;

  /**
   * Recupera il valore della proprietà codiceFondo.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodiceFondo()
  {
    return codiceFondo;
  }

  /**
   * Imposta il valore della proprietà codiceFondo.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodiceFondo(String value)
  {
    this.codiceFondo = value;
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
   * Recupera il valore della proprietà descrizioneSettore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescrizioneSettore()
  {
    return descrizioneSettore;
  }

  /**
   * Imposta il valore della proprietà descrizioneSettore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescrizioneSettore(String value)
  {
    this.descrizioneSettore = value;
  }

  /**
   * Recupera il valore della proprietà idFondo.
   * 
   * @return possible object is {@link Long }
   * 
   */
  public Long getIdFondo()
  {
    return idFondo;
  }

  /**
   * Imposta il valore della proprietà idFondo.
   * 
   * @param value
   *          allowed object is {@link Long }
   * 
   */
  public void setIdFondo(Long value)
  {
    this.idFondo = value;
  }

}
