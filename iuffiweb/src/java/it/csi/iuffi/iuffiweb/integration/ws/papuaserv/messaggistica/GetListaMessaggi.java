
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per getListaMessaggi complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getListaMessaggi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idProcedimento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codiceRuolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoMessaggio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="letto" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="obbligatorio" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="visibile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getListaMessaggi", propOrder =
{
    "idProcedimento",
    "codiceRuolo",
    "codiceFiscale",
    "tipoMessaggio",
    "letto",
    "obbligatorio",
    "visibile"
})
public class GetListaMessaggi
{

  protected int     idProcedimento;
  protected String  codiceRuolo;
  protected String  codiceFiscale;
  protected int     tipoMessaggio;
  protected Boolean letto;
  protected Boolean obbligatorio;
  protected Boolean visibile;

  /**
   * Recupera il valore della proprietà idProcedimento.
   * 
   */
  public int getIdProcedimento()
  {
    return idProcedimento;
  }

  /**
   * Imposta il valore della proprietà idProcedimento.
   * 
   */
  public void setIdProcedimento(int value)
  {
    this.idProcedimento = value;
  }

  /**
   * Recupera il valore della proprietà codiceRuolo.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodiceRuolo()
  {
    return codiceRuolo;
  }

  /**
   * Imposta il valore della proprietà codiceRuolo.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodiceRuolo(String value)
  {
    this.codiceRuolo = value;
  }

  /**
   * Recupera il valore della proprietà codiceFiscale.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }

  /**
   * Imposta il valore della proprietà codiceFiscale.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCodiceFiscale(String value)
  {
    this.codiceFiscale = value;
  }

  /**
   * Recupera il valore della proprietà tipoMessaggio.
   * 
   */
  public int getTipoMessaggio()
  {
    return tipoMessaggio;
  }

  /**
   * Imposta il valore della proprietà tipoMessaggio.
   * 
   */
  public void setTipoMessaggio(int value)
  {
    this.tipoMessaggio = value;
  }

  /**
   * Recupera il valore della proprietà letto.
   * 
   * @return possible object is {@link Boolean }
   * 
   */
  public Boolean isLetto()
  {
    return letto;
  }

  /**
   * Imposta il valore della proprietà letto.
   * 
   * @param value
   *          allowed object is {@link Boolean }
   * 
   */
  public void setLetto(Boolean value)
  {
    this.letto = value;
  }

  /**
   * Recupera il valore della proprietà obbligatorio.
   * 
   * @return possible object is {@link Boolean }
   * 
   */
  public Boolean isObbligatorio()
  {
    return obbligatorio;
  }

  /**
   * Imposta il valore della proprietà obbligatorio.
   * 
   * @param value
   *          allowed object is {@link Boolean }
   * 
   */
  public void setObbligatorio(Boolean value)
  {
    this.obbligatorio = value;
  }

  /**
   * Recupera il valore della proprietà visibile.
   * 
   * @return possible object is {@link Boolean }
   * 
   */
  public Boolean isVisibile()
  {
    return visibile;
  }

  /**
   * Imposta il valore della proprietà visibile.
   * 
   * @param value
   *          allowed object is {@link Boolean }
   * 
   */
  public void setVisibile(Boolean value)
  {
    this.visibile = value;
  }

}
