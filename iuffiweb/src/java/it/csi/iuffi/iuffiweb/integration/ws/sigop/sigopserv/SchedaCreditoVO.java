
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Classe Java per schedaCreditoVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="schedaCreditoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codiceFondo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioDebito" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="elencoDisposizioniTrasgredite" type="{http://ws.business.sigop.csi.it/}disposizioniTrasgrediteVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="importoDebito" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="importoRecupero" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="numeroScheda" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="presenzaGaranzia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="statoScheda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoDebito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "schedaCreditoVO", propOrder =
{
    "codiceFondo",
    "dataInizioDebito",
    "elencoDisposizioniTrasgredite",
    "importoDebito",
    "importoRecupero",
    "numeroScheda",
    "presenzaGaranzia",
    "statoScheda",
    "tipoDebito"
})
public class SchedaCreditoVO
{

  protected String                          codiceFondo;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar            dataInizioDebito;
  @XmlElement(nillable = true)
  protected List<DisposizioniTrasgrediteVO> elencoDisposizioniTrasgredite;
  protected BigDecimal                      importoDebito;
  protected BigDecimal                      importoRecupero;
  protected Integer                         numeroScheda;
  protected String                          presenzaGaranzia;
  protected String                          statoScheda;
  protected String                          tipoDebito;

  /**
   * Recupera il valore della proprietà codiceFondo.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodiceFondo()
  {
    return codiceFondo;
  }

  /**
   * Imposta il valore della proprietà codiceFondo.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodiceFondo(String value)
  {
    this.codiceFondo = value;
  }

  /**
   * Recupera il valore della proprietà dataInizioDebito.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataInizioDebito()
  {
    return dataInizioDebito;
  }

  /**
   * Imposta il valore della proprietà dataInizioDebito.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataInizioDebito(XMLGregorianCalendar value)
  {
    this.dataInizioDebito = value;
  }

  /**
   * Gets the value of the elencoDisposizioniTrasgredite property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the elencoDisposizioniTrasgredite property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getElencoDisposizioniTrasgredite().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link DisposizioniTrasgrediteVO }
   * 
   * 
   */
  public List<DisposizioniTrasgrediteVO> getElencoDisposizioniTrasgredite()
  {
    if (elencoDisposizioniTrasgredite == null)
    {
      elencoDisposizioniTrasgredite = new ArrayList<DisposizioniTrasgrediteVO>();
    }
    return this.elencoDisposizioniTrasgredite;
  }

  /**
   * Recupera il valore della proprietà importoDebito.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoDebito()
  {
    return importoDebito;
  }

  /**
   * Imposta il valore della proprietà importoDebito.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoDebito(BigDecimal value)
  {
    this.importoDebito = value;
  }

  /**
   * Recupera il valore della proprietà importoRecupero.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoRecupero()
  {
    return importoRecupero;
  }

  /**
   * Imposta il valore della proprietà importoRecupero.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoRecupero(BigDecimal value)
  {
    this.importoRecupero = value;
  }

  /**
   * Recupera il valore della proprietà numeroScheda.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getNumeroScheda()
  {
    return numeroScheda;
  }

  /**
   * Imposta il valore della proprietà numeroScheda.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setNumeroScheda(Integer value)
  {
    this.numeroScheda = value;
  }

  /**
   * Recupera il valore della proprietà presenzaGaranzia.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getPresenzaGaranzia()
  {
    return presenzaGaranzia;
  }

  /**
   * Imposta il valore della proprietà presenzaGaranzia.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setPresenzaGaranzia(String value)
  {
    this.presenzaGaranzia = value;
  }

  /**
   * Recupera il valore della proprietà statoScheda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getStatoScheda()
  {
    return statoScheda;
  }

  /**
   * Imposta il valore della proprietà statoScheda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setStatoScheda(String value)
  {
    this.statoScheda = value;
  }

  /**
   * Recupera il valore della proprietà tipoDebito.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getTipoDebito()
  {
    return tipoDebito;
  }

  /**
   * Imposta il valore della proprietà tipoDebito.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setTipoDebito(String value)
  {
    this.tipoDebito = value;
  }

}
