
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Classe Java per decretoVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="decretoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="descSettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idDecreto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="numeroDecreto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "decretoVO", propOrder =
{
    "dataCreazione",
    "descSettore",
    "idDecreto",
    "numeroDecreto"
})
public class DecretoVO
{

  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataCreazione;
  protected String               descSettore;
  protected Long                 idDecreto;
  protected String               numeroDecreto;

  /**
   * Recupera il valore della proprietà dataCreazione.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataCreazione()
  {
    return dataCreazione;
  }

  /**
   * Imposta il valore della proprietà dataCreazione.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataCreazione(XMLGregorianCalendar value)
  {
    this.dataCreazione = value;
  }

  /**
   * Recupera il valore della proprietà descSettore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescSettore()
  {
    return descSettore;
  }

  /**
   * Imposta il valore della proprietà descSettore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescSettore(String value)
  {
    this.descSettore = value;
  }

  /**
   * Recupera il valore della proprietà idDecreto.
   * 
   * @return possible object is {@link Long }
   * 
   */
  public Long getIdDecreto()
  {
    return idDecreto;
  }

  /**
   * Imposta il valore della proprietà idDecreto.
   * 
   * @param value
   *          allowed object is {@link Long }
   * 
   */
  public void setIdDecreto(Long value)
  {
    this.idDecreto = value;
  }

  /**
   * Recupera il valore della proprietà numeroDecreto.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroDecreto()
  {
    return numeroDecreto;
  }

  /**
   * Imposta il valore della proprietà numeroDecreto.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroDecreto(String value)
  {
    this.numeroDecreto = value;
  }

}
