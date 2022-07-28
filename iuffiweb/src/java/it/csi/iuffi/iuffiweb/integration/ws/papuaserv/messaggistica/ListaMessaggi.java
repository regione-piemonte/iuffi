
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per listaMessaggi complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="listaMessaggi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messaggi" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}messaggio" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numeroMessaggiGenerici" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroMessaggiLogout" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroMessaggiTestata" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroTotaleMessaggi" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaMessaggi", propOrder =
{
    "messaggi",
    "numeroMessaggiGenerici",
    "numeroMessaggiLogout",
    "numeroMessaggiTestata",
    "numeroTotaleMessaggi"
})
public class ListaMessaggi
{

  @XmlElement(nillable = true)
  protected List<Messaggio> messaggi;
  protected long            numeroMessaggiGenerici;
  protected long            numeroMessaggiLogout;
  protected long            numeroMessaggiTestata;
  protected long            numeroTotaleMessaggi;

  /**
   * Gets the value of the messaggi property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the messaggi property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getMessaggi().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Messaggio }
   * 
   * 
   */
  public List<Messaggio> getMessaggi()
  {
    if (messaggi == null)
    {
      messaggi = new ArrayList<Messaggio>();
    }
    return this.messaggi;
  }

  /**
   * Recupera il valore della proprietà numeroMessaggiGenerici.
   * 
   */
  public long getNumeroMessaggiGenerici()
  {
    return numeroMessaggiGenerici;
  }

  /**
   * Imposta il valore della proprietà numeroMessaggiGenerici.
   * 
   */
  public void setNumeroMessaggiGenerici(long value)
  {
    this.numeroMessaggiGenerici = value;
  }

  /**
   * Recupera il valore della proprietà numeroMessaggiLogout.
   * 
   */
  public long getNumeroMessaggiLogout()
  {
    return numeroMessaggiLogout;
  }

  /**
   * Imposta il valore della proprietà numeroMessaggiLogout.
   * 
   */
  public void setNumeroMessaggiLogout(long value)
  {
    this.numeroMessaggiLogout = value;
  }

  /**
   * Recupera il valore della proprietà numeroMessaggiTestata.
   * 
   */
  public long getNumeroMessaggiTestata()
  {
    return numeroMessaggiTestata;
  }

  /**
   * Imposta il valore della proprietà numeroMessaggiTestata.
   * 
   */
  public void setNumeroMessaggiTestata(long value)
  {
    this.numeroMessaggiTestata = value;
  }

  /**
   * Recupera il valore della proprietà numeroTotaleMessaggi.
   * 
   */
  public long getNumeroTotaleMessaggi()
  {
    return numeroTotaleMessaggi;
  }

  /**
   * Imposta il valore della proprietà numeroTotaleMessaggi.
   * 
   */
  public void setNumeroTotaleMessaggi(long value)
  {
    this.numeroTotaleMessaggi = value;
  }

}
