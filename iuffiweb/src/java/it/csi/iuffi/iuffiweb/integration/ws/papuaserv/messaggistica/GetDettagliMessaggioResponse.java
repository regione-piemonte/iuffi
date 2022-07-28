
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per getDettagliMessaggioResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getDettagliMessaggioResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dettagliMessaggio" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}dettagliMessaggio" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDettagliMessaggioResponse", propOrder =
{
    "dettagliMessaggio"
})
public class GetDettagliMessaggioResponse
{

  protected DettagliMessaggio dettagliMessaggio;

  /**
   * Recupera il valore della proprietà dettagliMessaggio.
   * 
   * @return possible object is {@link DettagliMessaggio }
   * 
   */
  public DettagliMessaggio getDettagliMessaggio()
  {
    return dettagliMessaggio;
  }

  /**
   * Imposta il valore della proprietà dettagliMessaggio.
   * 
   * @param value
   *          allowed object is {@link DettagliMessaggio }
   * 
   */
  public void setDettagliMessaggio(DettagliMessaggio value)
  {
    this.dettagliMessaggio = value;
  }

}
