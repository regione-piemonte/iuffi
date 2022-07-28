
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per settoreExtVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="settoreExtVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.business.sigop.csi.it/}settoreVO"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="competenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "settoreExtVO", propOrder =
{
    "competenza"
})
public class SettoreExtVO
    extends SettoreVO
{

  protected String competenza;

  /**
   * Recupera il valore della proprietà competenza.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCompetenza()
  {
    return competenza;
  }

  /**
   * Imposta il valore della proprietà competenza.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCompetenza(String value)
  {
    this.competenza = value;
  }

}
