
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceEstraiPagamentiBeneficiari complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceEstraiPagamentiBeneficiari"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anno" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="settore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="interventi" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numDomanda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="gestoriFascicolo" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="extIdProcedimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="sottoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="organismiDelegati" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="decreto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEstraiPagamentiBeneficiari", propOrder =
{
    "anno",
    "settore",
    "interventi",
    "provincia",
    "comune",
    "denominazione",
    "cuaa",
    "numDomanda",
    "gestoriFascicolo",
    "extIdProcedimento",
    "sottoProcedimento",
    "organismiDelegati",
    "decreto"
})
public class ServiceEstraiPagamentiBeneficiari
{

  protected Integer       anno;
  protected String        settore;
  @XmlElement(type = Integer.class)
  protected List<Integer> interventi;
  protected String        provincia;
  protected String        comune;
  protected String        denominazione;
  protected String        cuaa;
  protected String        numDomanda;
  protected List<String>  gestoriFascicolo;
  protected Integer       extIdProcedimento;
  protected String        sottoProcedimento;
  @XmlElement(type = Integer.class)
  protected List<Integer> organismiDelegati;
  protected Integer       decreto;

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
   * Gets the value of the interventi property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the interventi property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getInterventi().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Integer }
   * 
   * 
   */
  public List<Integer> getInterventi()
  {
    if (interventi == null)
    {
      interventi = new ArrayList<Integer>();
    }
    return this.interventi;
  }

  /**
   * Recupera il valore della proprietà provincia.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getProvincia()
  {
    return provincia;
  }

  /**
   * Imposta il valore della proprietà provincia.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setProvincia(String value)
  {
    this.provincia = value;
  }

  /**
   * Recupera il valore della proprietà comune.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getComune()
  {
    return comune;
  }

  /**
   * Imposta il valore della proprietà comune.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setComune(String value)
  {
    this.comune = value;
  }

  /**
   * Recupera il valore della proprietà denominazione.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDenominazione()
  {
    return denominazione;
  }

  /**
   * Imposta il valore della proprietà denominazione.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDenominazione(String value)
  {
    this.denominazione = value;
  }

  /**
   * Recupera il valore della proprietà cuaa.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCuaa()
  {
    return cuaa;
  }

  /**
   * Imposta il valore della proprietà cuaa.
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
   * Recupera il valore della proprietà numDomanda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumDomanda()
  {
    return numDomanda;
  }

  /**
   * Imposta il valore della proprietà numDomanda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumDomanda(String value)
  {
    this.numDomanda = value;
  }

  /**
   * Gets the value of the gestoriFascicolo property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the gestoriFascicolo property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getGestoriFascicolo().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  public List<String> getGestoriFascicolo()
  {
    if (gestoriFascicolo == null)
    {
      gestoriFascicolo = new ArrayList<String>();
    }
    return this.gestoriFascicolo;
  }

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

  /**
   * Gets the value of the organismiDelegati property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the organismiDelegati property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getOrganismiDelegati().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Integer }
   * 
   * 
   */
  public List<Integer> getOrganismiDelegati()
  {
    if (organismiDelegati == null)
    {
      organismiDelegati = new ArrayList<Integer>();
    }
    return this.organismiDelegati;
  }

  /**
   * Recupera il valore della proprietà decreto.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getDecreto()
  {
    return decreto;
  }

  /**
   * Imposta il valore della proprietà decreto.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setDecreto(Integer value)
  {
    this.decreto = value;
  }

}
