
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per esitoServizioVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="esitoServizioVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codErrore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="esito" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="msgErrore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "esitoServizioVO", propOrder =
{
    "codErrore",
    "esito",
    "msgErrore"
})
public class EsitoServizioVO
{

  protected String codErrore;
  protected int    esito;
  protected String msgErrore;

  /**
   * Recupera il valore della proprietà codErrore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodErrore()
  {
    return codErrore;
  }

  /**
   * Imposta il valore della proprietà codErrore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodErrore(String value)
  {
    this.codErrore = value;
  }

  /**
   * Recupera il valore della proprietà esito.
   * 
   */
  public int getEsito()
  {
    return esito;
  }

  /**
   * Imposta il valore della proprietà esito.
   * 
   */
  public void setEsito(int value)
  {
    this.esito = value;
  }

  /**
   * Recupera il valore della proprietà msgErrore.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getMsgErrore()
  {
    return msgErrore;
  }

  /**
   * Imposta il valore della proprietà msgErrore.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setMsgErrore(String value)
  {
    this.msgErrore = value;
  }

}
