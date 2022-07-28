
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
 * Classe Java per recuperoPregressoVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="recuperoPregressoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annoCampagna" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="annoDisposizionePagamento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="dataDecreto" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="descSettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="enteCreditore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idDecreto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idSchedaCredito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="importoRecupero" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="normeTrasgredite" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="numeriDomanda" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="numeroDecreto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroScheda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperoPregressoVO", propOrder =
{
    "annoCampagna",
    "annoDisposizionePagamento",
    "dataDecreto",
    "descSettore",
    "enteCreditore",
    "idDecreto",
    "idSchedaCredito",
    "importoRecupero",
    "normeTrasgredite",
    "numeriDomanda",
    "numeroDecreto",
    "numeroScheda"
})
public class RecuperoPregressoVO
{

  protected Integer              annoCampagna;
  protected Integer              annoDisposizionePagamento;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataDecreto;
  protected String               descSettore;
  protected String               enteCreditore;
  protected String               idDecreto;
  protected String               idSchedaCredito;
  protected BigDecimal           importoRecupero;
  @XmlElement(nillable = true)
  protected List<String>         normeTrasgredite;
  @XmlElement(nillable = true)
  protected List<String>         numeriDomanda;
  protected String               numeroDecreto;
  protected String               numeroScheda;

  /**
   * Recupera il valore della proprietà annoCampagna.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getAnnoCampagna()
  {
    return annoCampagna;
  }

  /**
   * Imposta il valore della proprietà annoCampagna.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setAnnoCampagna(Integer value)
  {
    this.annoCampagna = value;
  }

  /**
   * Recupera il valore della proprietà annoDisposizionePagamento.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getAnnoDisposizionePagamento()
  {
    return annoDisposizionePagamento;
  }

  /**
   * Imposta il valore della proprietà annoDisposizionePagamento.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setAnnoDisposizionePagamento(Integer value)
  {
    this.annoDisposizionePagamento = value;
  }

  /**
   * Recupera il valore della proprietà dataDecreto.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataDecreto()
  {
    return dataDecreto;
  }

  /**
   * Imposta il valore della proprietà dataDecreto.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataDecreto(XMLGregorianCalendar value)
  {
    this.dataDecreto = value;
  }

  /**
   * Recupera il valore della proprietà descSettore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescSettore()
  {
    return descSettore;
  }

  /**
   * Imposta il valore della proprietà descSettore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescSettore(String value)
  {
    this.descSettore = value;
  }

  /**
   * Recupera il valore della proprietà enteCreditore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getEnteCreditore()
  {
    return enteCreditore;
  }

  /**
   * Imposta il valore della proprietà enteCreditore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setEnteCreditore(String value)
  {
    this.enteCreditore = value;
  }

  /**
   * Recupera il valore della proprietà idDecreto.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getIdDecreto()
  {
    return idDecreto;
  }

  /**
   * Imposta il valore della proprietà idDecreto.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setIdDecreto(String value)
  {
    this.idDecreto = value;
  }

  /**
   * Recupera il valore della proprietà idSchedaCredito.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getIdSchedaCredito()
  {
    return idSchedaCredito;
  }

  /**
   * Imposta il valore della proprietà idSchedaCredito.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setIdSchedaCredito(String value)
  {
    this.idSchedaCredito = value;
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
   * Gets the value of the normeTrasgredite property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the normeTrasgredite property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getNormeTrasgredite().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  public List<String> getNormeTrasgredite()
  {
    if (normeTrasgredite == null)
    {
      normeTrasgredite = new ArrayList<String>();
    }
    return this.normeTrasgredite;
  }

  /**
   * Gets the value of the numeriDomanda property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the numeriDomanda property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getNumeriDomanda().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  public List<String> getNumeriDomanda()
  {
    if (numeriDomanda == null)
    {
      numeriDomanda = new ArrayList<String>();
    }
    return this.numeriDomanda;
  }

  /**
   * Recupera il valore della proprietà numeroDecreto.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroDecreto()
  {
    return numeroDecreto;
  }

  /**
   * Imposta il valore della proprietà numeroDecreto.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroDecreto(String value)
  {
    this.numeroDecreto = value;
  }

  /**
   * Recupera il valore della proprietà numeroScheda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroScheda()
  {
    return numeroScheda;
  }

  /**
   * Imposta il valore della proprietà numeroScheda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroScheda(String value)
  {
    this.numeroScheda = value;
  }

}
