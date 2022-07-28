
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Classe Java per disposizioniTrasgrediteVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="disposizioniTrasgrediteVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annoCampagna" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="dataDomanda" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="interventoTrasgredito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroDomanda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="regolamentoTrasgredito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "disposizioniTrasgrediteVO", propOrder =
{
    "annoCampagna",
    "dataDomanda",
    "interventoTrasgredito",
    "numeroDomanda",
    "regolamentoTrasgredito"
})
public class DisposizioniTrasgrediteVO
{

  protected Integer              annoCampagna;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataDomanda;
  protected String               interventoTrasgredito;
  protected String               numeroDomanda;
  protected String               regolamentoTrasgredito;

  /**
   * Recupera il valore della proprietà annoCampagna.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getAnnoCampagna()
  {
    return annoCampagna;
  }

  /**
   * Imposta il valore della proprietà annoCampagna.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setAnnoCampagna(Integer value)
  {
    this.annoCampagna = value;
  }

  /**
   * Recupera il valore della proprietà dataDomanda.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataDomanda()
  {
    return dataDomanda;
  }

  /**
   * Imposta il valore della proprietà dataDomanda.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataDomanda(XMLGregorianCalendar value)
  {
    this.dataDomanda = value;
  }

  /**
   * Recupera il valore della proprietà interventoTrasgredito.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getInterventoTrasgredito()
  {
    return interventoTrasgredito;
  }

  /**
   * Imposta il valore della proprietà interventoTrasgredito.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setInterventoTrasgredito(String value)
  {
    this.interventoTrasgredito = value;
  }

  /**
   * Recupera il valore della proprietà numeroDomanda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroDomanda()
  {
    return numeroDomanda;
  }

  /**
   * Imposta il valore della proprietà numeroDomanda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroDomanda(String value)
  {
    this.numeroDomanda = value;
  }

  /**
   * Recupera il valore della proprietà regolamentoTrasgredito.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getRegolamentoTrasgredito()
  {
    return regolamentoTrasgredito;
  }

  /**
   * Imposta il valore della proprietà regolamentoTrasgredito.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setRegolamentoTrasgredito(String value)
  {
    this.regolamentoTrasgredito = value;
  }

}
