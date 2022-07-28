
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per recuperiPregressiVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="recuperiPregressiVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="esitoServizio" type="{http://ws.business.sigop.csi.it/}esitoServizioVO" minOccurs="0"/&gt;
 *         &lt;element name="listaRecuperi" type="{http://ws.business.sigop.csi.it/}recuperoPregressoVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperiPregressiVO", propOrder =
{
    "cuaa",
    "esitoServizio",
    "listaRecuperi"
})
public class RecuperiPregressiVO
{

  protected String                    cuaa;
  protected EsitoServizioVO           esitoServizio;
  @XmlElement(nillable = true)
  protected List<RecuperoPregressoVO> listaRecuperi;

  /**
   * Recupera il valore della propriet� cuaa.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCuaa()
  {
    return cuaa;
  }

  /**
   * Imposta il valore della propriet� cuaa.
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
   * Recupera il valore della propriet� esitoServizio.
   * 
   * @return possible object is {@link EsitoServizioVO }
   * 
   */
  public EsitoServizioVO getEsitoServizio()
  {
    return esitoServizio;
  }

  /**
   * Imposta il valore della propriet� esitoServizio.
   * 
   * @param value
   *          allowed object is {@link EsitoServizioVO }
   * 
   */
  public void setEsitoServizio(EsitoServizioVO value)
  {
    this.esitoServizio = value;
  }

  /**
   * Gets the value of the listaRecuperi property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the listaRecuperi property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getListaRecuperi().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link RecuperoPregressoVO }
   * 
   * 
   */
  public List<RecuperoPregressoVO> getListaRecuperi()
  {
    if (listaRecuperi == null)
    {
      listaRecuperi = new ArrayList<RecuperoPregressoVO>();
    }
    return this.listaRecuperi;
  }

}
