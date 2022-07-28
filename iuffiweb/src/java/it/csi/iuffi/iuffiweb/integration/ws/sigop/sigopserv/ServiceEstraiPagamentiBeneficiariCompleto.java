
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
 * Classe Java per serviceEstraiPagamentiBeneficiariCompleto complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceEstraiPagamentiBeneficiariCompleto"&gt;
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
 *         &lt;element name="dataPresDomandaDal" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataPresDomandaAl" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataCreazListaDal" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataCreazListaAl" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="importoConcessoDa" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="importoConcessoA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="gestoriFascicolo" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="extIdProcedimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="sottoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="organismiDelegati" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="decreto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="dataCreazDecretoDal" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataCreazDecretoAl" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="limiteNumRecord" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEstraiPagamentiBeneficiariCompleto", propOrder =
{
    "anno",
    "settore",
    "interventi",
    "provincia",
    "comune",
    "denominazione",
    "cuaa",
    "numDomanda",
    "dataPresDomandaDal",
    "dataPresDomandaAl",
    "dataCreazListaDal",
    "dataCreazListaAl",
    "importoConcessoDa",
    "importoConcessoA",
    "gestoriFascicolo",
    "extIdProcedimento",
    "sottoProcedimento",
    "organismiDelegati",
    "decreto",
    "dataCreazDecretoDal",
    "dataCreazDecretoAl",
    "limiteNumRecord"
})
public class ServiceEstraiPagamentiBeneficiariCompleto
{

  protected Integer              anno;
  protected String               settore;
  @XmlElement(type = Integer.class)
  protected List<Integer>        interventi;
  protected String               provincia;
  protected String               comune;
  protected String               denominazione;
  protected String               cuaa;
  protected String               numDomanda;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataPresDomandaDal;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataPresDomandaAl;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataCreazListaDal;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataCreazListaAl;
  protected BigDecimal           importoConcessoDa;
  protected BigDecimal           importoConcessoA;
  protected List<String>         gestoriFascicolo;
  protected Integer              extIdProcedimento;
  protected String               sottoProcedimento;
  @XmlElement(type = Integer.class)
  protected List<Integer>        organismiDelegati;
  protected Integer              decreto;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataCreazDecretoDal;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataCreazDecretoAl;
  protected Integer              limiteNumRecord;

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
   * Recupera il valore della proprietà dataPresDomandaDal.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataPresDomandaDal()
  {
    return dataPresDomandaDal;
  }

  /**
   * Imposta il valore della proprietà dataPresDomandaDal.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataPresDomandaDal(XMLGregorianCalendar value)
  {
    this.dataPresDomandaDal = value;
  }

  /**
   * Recupera il valore della proprietà dataPresDomandaAl.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataPresDomandaAl()
  {
    return dataPresDomandaAl;
  }

  /**
   * Imposta il valore della proprietà dataPresDomandaAl.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataPresDomandaAl(XMLGregorianCalendar value)
  {
    this.dataPresDomandaAl = value;
  }

  /**
   * Recupera il valore della proprietà dataCreazListaDal.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataCreazListaDal()
  {
    return dataCreazListaDal;
  }

  /**
   * Imposta il valore della proprietà dataCreazListaDal.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataCreazListaDal(XMLGregorianCalendar value)
  {
    this.dataCreazListaDal = value;
  }

  /**
   * Recupera il valore della proprietà dataCreazListaAl.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataCreazListaAl()
  {
    return dataCreazListaAl;
  }

  /**
   * Imposta il valore della proprietà dataCreazListaAl.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataCreazListaAl(XMLGregorianCalendar value)
  {
    this.dataCreazListaAl = value;
  }

  /**
   * Recupera il valore della proprietà importoConcessoDa.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoConcessoDa()
  {
    return importoConcessoDa;
  }

  /**
   * Imposta il valore della proprietà importoConcessoDa.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoConcessoDa(BigDecimal value)
  {
    this.importoConcessoDa = value;
  }

  /**
   * Recupera il valore della proprietà importoConcessoA.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoConcessoA()
  {
    return importoConcessoA;
  }

  /**
   * Imposta il valore della proprietà importoConcessoA.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoConcessoA(BigDecimal value)
  {
    this.importoConcessoA = value;
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

  /**
   * Recupera il valore della proprietà dataCreazDecretoDal.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataCreazDecretoDal()
  {
    return dataCreazDecretoDal;
  }

  /**
   * Imposta il valore della proprietà dataCreazDecretoDal.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataCreazDecretoDal(XMLGregorianCalendar value)
  {
    this.dataCreazDecretoDal = value;
  }

  /**
   * Recupera il valore della proprietà dataCreazDecretoAl.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataCreazDecretoAl()
  {
    return dataCreazDecretoAl;
  }

  /**
   * Imposta il valore della proprietà dataCreazDecretoAl.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataCreazDecretoAl(XMLGregorianCalendar value)
  {
    this.dataCreazDecretoAl = value;
  }

  /**
   * Recupera il valore della proprietà limiteNumRecord.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getLimiteNumRecord()
  {
    return limiteNumRecord;
  }

  /**
   * Imposta il valore della proprietà limiteNumRecord.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setLimiteNumRecord(Integer value)
  {
    this.limiteNumRecord = value;
  }

}
