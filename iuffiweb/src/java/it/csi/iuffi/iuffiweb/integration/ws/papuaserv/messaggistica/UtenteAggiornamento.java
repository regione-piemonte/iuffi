
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per utenteAggiornamento complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="utenteAggiornamento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiceEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="denominazioneEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUtente" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "utenteAggiornamento", propOrder =
{
    "codiceEnte",
    "cognome",
    "denominazioneEnte",
    "idUtente",
    "nome"
})
public class UtenteAggiornamento
{

  protected String codiceEnte;
  protected String cognome;
  protected String denominazioneEnte;
  protected long   idUtente;
  protected String nome;

  /**
   * Recupera il valore della proprietà codiceEnte.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodiceEnte()
  {
    return codiceEnte;
  }

  /**
   * Imposta il valore della proprietà codiceEnte.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodiceEnte(String value)
  {
    this.codiceEnte = value;
  }

  /**
   * Recupera il valore della proprietà cognome.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCognome()
  {
    return cognome;
  }

  /**
   * Imposta il valore della proprietà cognome.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCognome(String value)
  {
    this.cognome = value;
  }

  /**
   * Recupera il valore della proprietà denominazioneEnte.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDenominazioneEnte()
  {
    return denominazioneEnte;
  }

  /**
   * Imposta il valore della proprietà denominazioneEnte.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDenominazioneEnte(String value)
  {
    this.denominazioneEnte = value;
  }

  /**
   * Recupera il valore della proprietà idUtente.
   * 
   */
  public long getIdUtente()
  {
    return idUtente;
  }

  /**
   * Imposta il valore della proprietà idUtente.
   * 
   */
  public void setIdUtente(long value)
  {
    this.idUtente = value;
  }

  /**
   * Recupera il valore della proprietà nome.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNome()
  {
    return nome;
  }

  /**
   * Imposta il valore della proprietà nome.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNome(String value)
  {
    this.nome = value;
  }

}
