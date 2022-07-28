
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceEstraiPagamentiErogatiResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceEstraiPagamentiErogatiResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="debiti" type="{http://ws.business.sigop.csi.it/}pagamentiErogatiVO" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEstraiPagamentiErogatiResponse", propOrder =
{
    "debiti"
})
public class ServiceEstraiPagamentiErogatiResponse
{

  protected PagamentiErogatiVO debiti;

  /**
   * Recupera il valore della proprietà debiti.
   * 
   * @return possible object is {@link PagamentiErogatiVO }
   * 
   */
  public PagamentiErogatiVO getDebiti()
  {
    return debiti;
  }

  /**
   * Imposta il valore della proprietà debiti.
   * 
   * @param value
   *          allowed object is {@link PagamentiErogatiVO }
   * 
   */
  public void setDebiti(PagamentiErogatiVO value)
  {
    this.debiti = value;
  }

}
