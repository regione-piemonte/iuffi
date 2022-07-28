
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per verificaLogout complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="verificaLogout">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="arg1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verificaLogout", propOrder =
{
    "arg0",
    "arg1"
})
public class VerificaLogout
{

  protected int    arg0;
  protected String arg1;

  /**
   * Recupera il valore della propriet� arg0.
   * 
   */
  public int getArg0()
  {
    return arg0;
  }

  /**
   * Imposta il valore della propriet� arg0.
   * 
   */
  public void setArg0(int value)
  {
    this.arg0 = value;
  }

  /**
   * Recupera il valore della propriet� arg1.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getArg1()
  {
    return arg1;
  }

  /**
   * Imposta il valore della propriet� arg1.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setArg1(String value)
  {
    this.arg1 = value;
  }

}
