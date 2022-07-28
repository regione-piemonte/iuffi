
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per fondoVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="fondoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="competenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fondoVO", propOrder =
{
    "competenza",
    "settore"
})
public class FondoVO
{

  protected String competenza;
  protected String settore;

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

  /**
   * Recupera il valore della proprietà settore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getSettore()
  {
    return settore;
  }

  /**
   * Imposta il valore della proprietà settore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setSettore(String value)
  {
    this.settore = value;
  }

}
