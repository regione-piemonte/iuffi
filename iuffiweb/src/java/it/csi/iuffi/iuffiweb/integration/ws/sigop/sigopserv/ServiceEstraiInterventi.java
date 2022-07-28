
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceEstraiInterventi complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceEstraiInterventi"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="settore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="anno" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEstraiInterventi", propOrder =
{
    "settore",
    "anno"
})
public class ServiceEstraiInterventi
{

  protected String  settore;
  protected Integer anno;

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

  /**
   * Recupera il valore della proprietà anno.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getAnno()
  {
    return anno;
  }

  /**
   * Imposta il valore della proprietà anno.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setAnno(Integer value)
  {
    this.anno = value;
  }

}
