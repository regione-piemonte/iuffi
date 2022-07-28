
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceVisualizzaDebiti complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceVisualizzaDebiti"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoOrdinamento1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="tipoOrdinamento2" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="tipoOrdinamento3" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="tipoOrdinamento4" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceVisualizzaDebiti", propOrder =
{
    "cuaa",
    "tipoOrdinamento1",
    "tipoOrdinamento2",
    "tipoOrdinamento3",
    "tipoOrdinamento4"
})
public class ServiceVisualizzaDebiti
{

  protected String  cuaa;
  protected Integer tipoOrdinamento1;
  protected Integer tipoOrdinamento2;
  protected Integer tipoOrdinamento3;
  protected Integer tipoOrdinamento4;

  /**
   * Recupera il valore della proprietà cuaa.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCuaa()
  {
    return cuaa;
  }

  /**
   * Imposta il valore della proprietà cuaa.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCuaa(String value)
  {
    this.cuaa = value;
  }

  /**
   * Recupera il valore della proprietà tipoOrdinamento1.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getTipoOrdinamento1()
  {
    return tipoOrdinamento1;
  }

  /**
   * Imposta il valore della proprietà tipoOrdinamento1.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setTipoOrdinamento1(Integer value)
  {
    this.tipoOrdinamento1 = value;
  }

  /**
   * Recupera il valore della proprietà tipoOrdinamento2.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getTipoOrdinamento2()
  {
    return tipoOrdinamento2;
  }

  /**
   * Imposta il valore della proprietà tipoOrdinamento2.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setTipoOrdinamento2(Integer value)
  {
    this.tipoOrdinamento2 = value;
  }

  /**
   * Recupera il valore della proprietà tipoOrdinamento3.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getTipoOrdinamento3()
  {
    return tipoOrdinamento3;
  }

  /**
   * Imposta il valore della proprietà tipoOrdinamento3.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setTipoOrdinamento3(Integer value)
  {
    this.tipoOrdinamento3 = value;
  }

  /**
   * Recupera il valore della proprietà tipoOrdinamento4.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getTipoOrdinamento4()
  {
    return tipoOrdinamento4;
  }

  /**
   * Imposta il valore della proprietà tipoOrdinamento4.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setTipoOrdinamento4(Integer value)
  {
    this.tipoOrdinamento4 = value;
  }

}
