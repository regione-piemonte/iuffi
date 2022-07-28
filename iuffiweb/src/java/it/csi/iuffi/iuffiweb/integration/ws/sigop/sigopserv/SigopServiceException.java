
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per SigopServiceException complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="SigopServiceException"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codErrore" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="msgErrore" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nestedExcClassName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="stackTraceMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nestedExcMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SigopServiceException", propOrder =
{
    "codErrore",
    "msgErrore",
    "nestedExcClassName",
    "stackTraceMessage",
    "nestedExcMsg"
})
public class SigopServiceException
{

  @XmlElement(required = true, nillable = true)
  protected String codErrore;
  @XmlElement(required = true, nillable = true)
  protected String msgErrore;
  @XmlElement(required = true, nillable = true)
  protected String nestedExcClassName;
  @XmlElement(required = true, nillable = true)
  protected String stackTraceMessage;
  @XmlElement(required = true, nillable = true)
  protected String nestedExcMsg;

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

  /**
   * Recupera il valore della proprietà nestedExcClassName.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNestedExcClassName()
  {
    return nestedExcClassName;
  }

  /**
   * Imposta il valore della proprietà nestedExcClassName.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNestedExcClassName(String value)
  {
    this.nestedExcClassName = value;
  }

  /**
   * Recupera il valore della proprietà stackTraceMessage.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getStackTraceMessage()
  {
    return stackTraceMessage;
  }

  /**
   * Imposta il valore della proprietà stackTraceMessage.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setStackTraceMessage(String value)
  {
    this.stackTraceMessage = value;
  }

  /**
   * Recupera il valore della proprietà nestedExcMsg.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNestedExcMsg()
  {
    return nestedExcMsg;
  }

  /**
   * Imposta il valore della proprietà nestedExcMsg.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNestedExcMsg(String value)
  {
    this.nestedExcMsg = value;
  }

}
