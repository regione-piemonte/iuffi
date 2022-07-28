
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceEstraiRecuperiPregressiResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceEstraiRecuperiPregressiResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="recuperi" type="{http://ws.business.sigop.csi.it/}recuperiPregressiVO" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEstraiRecuperiPregressiResponse", propOrder =
{
    "recuperi"
})
public class ServiceEstraiRecuperiPregressiResponse
{

  protected RecuperiPregressiVO recuperi;

  /**
   * Recupera il valore della proprietà recuperi.
   * 
   * @return possible object is {@link RecuperiPregressiVO }
   * 
   */
  public RecuperiPregressiVO getRecuperi()
  {
    return recuperi;
  }

  /**
   * Imposta il valore della proprietà recuperi.
   * 
   * @param value
   *          allowed object is {@link RecuperiPregressiVO }
   * 
   */
  public void setRecuperi(RecuperiPregressiVO value)
  {
    this.recuperi = value;
  }

}
