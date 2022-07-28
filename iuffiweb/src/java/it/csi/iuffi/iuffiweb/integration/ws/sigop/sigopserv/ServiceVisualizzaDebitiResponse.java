
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceVisualizzaDebitiResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceVisualizzaDebitiResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="debiti" type="{http://ws.business.sigop.csi.it/}schedaCreditoVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceVisualizzaDebitiResponse", propOrder =
{
    "debiti"
})
public class ServiceVisualizzaDebitiResponse
{

  protected List<SchedaCreditoVO> debiti;

  /**
   * Gets the value of the debiti property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the debiti property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getDebiti().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link SchedaCreditoVO }
   * 
   * 
   */
  public List<SchedaCreditoVO> getDebiti()
  {
    if (debiti == null)
    {
      debiti = new ArrayList<SchedaCreditoVO>();
    }
    return this.debiti;
  }

}
