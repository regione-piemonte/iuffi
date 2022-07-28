
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per SigopMaxRecordException complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="SigopMaxRecordException"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroLimite" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="numeroRecord" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "SigopMaxRecordException", propOrder =
{
    "numeroLimite",
    "numeroRecord",
    "nestedExcClassName",
    "stackTraceMessage",
    "nestedExcMsg"
})
public class SigopMaxRecordException
{

  @XmlElement(required = true, nillable = true)
  protected String numeroLimite;
  @XmlElement(required = true, nillable = true)
  protected String numeroRecord;
  @XmlElement(required = true, nillable = true)
  protected String nestedExcClassName;
  @XmlElement(required = true, nillable = true)
  protected String stackTraceMessage;
  @XmlElement(required = true, nillable = true)
  protected String nestedExcMsg;

  /**
   * Recupera il valore della proprietà numeroLimite.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroLimite()
  {
    return numeroLimite;
  }

  /**
   * Imposta il valore della proprietà numeroLimite.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroLimite(String value)
  {
    this.numeroLimite = value;
  }

  /**
   * Recupera il valore della proprietà numeroRecord.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroRecord()
  {
    return numeroRecord;
  }

  /**
   * Imposta il valore della proprietà numeroRecord.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumeroRecord(String value)
  {
    this.numeroRecord = value;
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
