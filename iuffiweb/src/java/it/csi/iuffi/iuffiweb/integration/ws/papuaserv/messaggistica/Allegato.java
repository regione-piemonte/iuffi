
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per allegato complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="allegato">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAllegato" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="nomeFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "allegato", propOrder =
{
    "descrizione",
    "idAllegato",
    "nomeFile"
})
public class Allegato
{

  protected String descrizione;
  protected long   idAllegato;
  protected String nomeFile;

  /**
   * Recupera il valore della proprietà descrizione.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescrizione()
  {
    return descrizione;
  }

  /**
   * Imposta il valore della proprietà descrizione.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescrizione(String value)
  {
    this.descrizione = value;
  }

  /**
   * Recupera il valore della proprietà idAllegato.
   * 
   */
  public long getIdAllegato()
  {
    return idAllegato;
  }

  /**
   * Imposta il valore della proprietà idAllegato.
   * 
   */
  public void setIdAllegato(long value)
  {
    this.idAllegato = value;
  }

  /**
   * Recupera il valore della proprietà nomeFile.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNomeFile()
  {
    return nomeFile;
  }

  /**
   * Imposta il valore della proprietà nomeFile.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNomeFile(String value)
  {
    this.nomeFile = value;
  }

}
