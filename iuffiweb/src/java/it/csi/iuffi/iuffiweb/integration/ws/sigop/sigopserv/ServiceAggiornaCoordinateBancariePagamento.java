
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per serviceAggiornaCoordinateBancariePagamento complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="serviceAggiornaCoordinateBancariePagamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codiceDomanda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extIdSportello" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="numContoCorrente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cifraControllo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iban" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extIdProcedimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="utenteUltimoAggiornamento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceAggiornaCoordinateBancariePagamento", propOrder =
{
    "codiceDomanda",
    "extIdSportello",
    "numContoCorrente",
    "cin",
    "cifraControllo",
    "iban",
    "extIdProcedimento",
    "utenteUltimoAggiornamento"
})
public class ServiceAggiornaCoordinateBancariePagamento
{

  protected String  codiceDomanda;
  protected Long    extIdSportello;
  protected String  numContoCorrente;
  protected String  cin;
  protected String  cifraControllo;
  protected String  iban;
  protected Integer extIdProcedimento;
  protected Integer utenteUltimoAggiornamento;

  /**
   * Recupera il valore della proprietà codiceDomanda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodiceDomanda()
  {
    return codiceDomanda;
  }

  /**
   * Imposta il valore della proprietà codiceDomanda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodiceDomanda(String value)
  {
    this.codiceDomanda = value;
  }

  /**
   * Recupera il valore della proprietà extIdSportello.
   * 
   * @return possible object is {@link Long }
   * 
   */
  public Long getExtIdSportello()
  {
    return extIdSportello;
  }

  /**
   * Imposta il valore della proprietà extIdSportello.
   * 
   * @param value
   *          allowed object is {@link Long }
   * 
   */
  public void setExtIdSportello(Long value)
  {
    this.extIdSportello = value;
  }

  /**
   * Recupera il valore della proprietà numContoCorrente.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumContoCorrente()
  {
    return numContoCorrente;
  }

  /**
   * Imposta il valore della proprietà numContoCorrente.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumContoCorrente(String value)
  {
    this.numContoCorrente = value;
  }

  /**
   * Recupera il valore della proprietà cin.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCin()
  {
    return cin;
  }

  /**
   * Imposta il valore della proprietà cin.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCin(String value)
  {
    this.cin = value;
  }

  /**
   * Recupera il valore della proprietà cifraControllo.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCifraControllo()
  {
    return cifraControllo;
  }

  /**
   * Imposta il valore della proprietà cifraControllo.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCifraControllo(String value)
  {
    this.cifraControllo = value;
  }

  /**
   * Recupera il valore della proprietà iban.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getIban()
  {
    return iban;
  }

  /**
   * Imposta il valore della proprietà iban.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setIban(String value)
  {
    this.iban = value;
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
   * Recupera il valore della proprietà utenteUltimoAggiornamento.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getUtenteUltimoAggiornamento()
  {
    return utenteUltimoAggiornamento;
  }

  /**
   * Imposta il valore della proprietà utenteUltimoAggiornamento.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setUtenteUltimoAggiornamento(Integer value)
  {
    this.utenteUltimoAggiornamento = value;
  }

}
