
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceEstraiDecreti complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceEstraiDecreti"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="extIdProcedimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="sottoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEstraiDecreti", propOrder =
{
    "extIdProcedimento",
    "sottoProcedimento"
})
public class ServiceEstraiDecreti
{

  protected Integer extIdProcedimento;
  protected String  sottoProcedimento;

  /**
   * Recupera il valore della proprietà extIdProcedimento.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getExtIdProcedimento()
  {
    return extIdProcedimento;
  }

  /**
   * Imposta il valore della proprietà extIdProcedimento.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setExtIdProcedimento(Integer value)
  {
    this.extIdProcedimento = value;
  }

  /**
   * Recupera il valore della proprietà sottoProcedimento.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getSottoProcedimento()
  {
    return sottoProcedimento;
  }

  /**
   * Imposta il valore della proprietà sottoProcedimento.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setSottoProcedimento(String value)
  {
    this.sottoProcedimento = value;
  }

}
