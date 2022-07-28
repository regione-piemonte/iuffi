
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Classe Java per pagamentoErogatoVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="pagamentoErogatoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annoCampagna" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="annoDisposizionePagamento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="dataDecreto" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataDisposizionePagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataMandato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="descAmmCompetenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descIntervento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descSettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="importoLordo" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="importoNetto" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="importoRecupero" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="numeroDecreto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroDomanda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroMandato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoPagamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagamentoErogatoVO", propOrder =
{
    "annoCampagna",
    "annoDisposizionePagamento",
    "dataDecreto",
    "dataDisposizionePagamento",
    "dataMandato",
    "descAmmCompetenza",
    "descIntervento",
    "descSettore",
    "importoLordo",
    "importoNetto",
    "importoRecupero",
    "numeroDecreto",
    "numeroDomanda",
    "numeroMandato",
    "tipoPagamento"
})
public class PagamentoErogatoVO
{

  protected Integer              annoCampagna;
  protected Integer              annoDisposizionePagamento;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataDecreto;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataDisposizionePagamento;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataMandato;
  protected String               descAmmCompetenza;
  protected String               descIntervento;
  protected String               descSettore;
  protected BigDecimal           importoLordo;
  protected BigDecimal           importoNetto;
  protected BigDecimal           importoRecupero;
  protected String               numeroDecreto;
  protected String               numeroDomanda;
  protected String               numeroMandato;
  protected String               tipoPagamento;

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
   * Recupera il valore della proprietà dataDisposizionePagamento.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataDisposizionePagamento()
  {
    return dataDisposizionePagamento;
  }

  /**
   * Imposta il valore della proprietà dataDisposizionePagamento.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataDisposizionePagamento(XMLGregorianCalendar value)
  {
    this.dataDisposizionePagamento = value;
  }

  /**
   * Recupera il valore della proprietà dataMandato.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataMandato()
  {
    return dataMandato;
  }

  /**
   * Imposta il valore della proprietà dataMandato.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataMandato(XMLGregorianCalendar value)
  {
    this.dataMandato = value;
  }

  /**
   * Recupera il valore della proprietà descAmmCompetenza.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescAmmCompetenza()
  {
    return descAmmCompetenza;
  }

  /**
   * Imposta il valore della proprietà descAmmCompetenza.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescAmmCompetenza(String value)
  {
    this.descAmmCompetenza = value;
  }

  /**
   * Recupera il valore della proprietà descIntervento.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescIntervento()
  {
    return descIntervento;
  }

  /**
   * Imposta il valore della proprietà descIntervento.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescIntervento(String value)
  {
    this.descIntervento = value;
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
   * Recupera il valore della proprietà importoLordo.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoLordo()
  {
    return importoLordo;
  }

  /**
   * Imposta il valore della proprietà importoLordo.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoLordo(BigDecimal value)
  {
    this.importoLordo = value;
  }

  /**
   * Recupera il valore della proprietà importoNetto.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoNetto()
  {
    return importoNetto;
  }

  /**
   * Imposta il valore della proprietà importoNetto.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoNetto(BigDecimal value)
  {
    this.importoNetto = value;
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
   * Recupera il valore della proprietà numeroDomanda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroDomanda()
  {
    return numeroDomanda;
  }

  /**
   * Imposta il valore della proprietà numeroDomanda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroDomanda(String value)
  {
    this.numeroDomanda = value;
  }

  /**
   * Recupera il valore della proprietà numeroMandato.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroMandato()
  {
    return numeroMandato;
  }

  /**
   * Imposta il valore della proprietà numeroMandato.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroMandato(String value)
  {
    this.numeroMandato = value;
  }

  /**
   * Recupera il valore della proprietà tipoPagamento.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getTipoPagamento()
  {
    return tipoPagamento;
  }

  /**
   * Imposta il valore della proprietà tipoPagamento.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setTipoPagamento(String value)
  {
    this.tipoPagamento = value;
  }

}
