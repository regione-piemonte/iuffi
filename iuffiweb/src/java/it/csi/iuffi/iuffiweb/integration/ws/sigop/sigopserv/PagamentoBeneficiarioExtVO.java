
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Classe Java per pagamentoBeneficiarioExtVO complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="pagamentoBeneficiarioExtVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anno" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCreazioneLista" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataDecreto" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataMandato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataPresentazioneDomanda" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="decreto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descAmmCompetenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descRegolamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extIdAmmCompetenza" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="extIdAmmCompetenzaPadre" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idStatoPagamento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="importo" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="importoInLiquidazione" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="importoRecuperato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="intervento" type="{http://ws.business.sigop.csi.it/}interventoVO" minOccurs="0"/&gt;
 *         &lt;element name="istatProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mandato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numDomanda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroDecreto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroMandato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settore" type="{http://ws.business.sigop.csi.it/}settoreExtVO" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "pagamentoBeneficiarioExtVO", propOrder =
{
    "anno",
    "cuaa",
    "dataCreazioneLista",
    "dataDecreto",
    "dataMandato",
    "dataPagamento",
    "dataPresentazioneDomanda",
    "decreto",
    "denominazione",
    "descAmmCompetenza",
    "descRegolamento",
    "descrizioneStato",
    "extIdAmmCompetenza",
    "extIdAmmCompetenzaPadre",
    "idStatoPagamento",
    "importo",
    "importoInLiquidazione",
    "importoRecuperato",
    "intervento",
    "istatProvincia",
    "mandato",
    "numDomanda",
    "numeroDecreto",
    "numeroMandato",
    "settore",
    "siglaProvincia",
    "tipoPagamento"
})
public class PagamentoBeneficiarioExtVO
{

  protected Integer              anno;
  protected String               cuaa;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataCreazioneLista;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataDecreto;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataMandato;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataPagamento;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar dataPresentazioneDomanda;
  protected String               decreto;
  protected String               denominazione;
  protected String               descAmmCompetenza;
  protected String               descRegolamento;
  protected String               descrizioneStato;
  protected Integer              extIdAmmCompetenza;
  protected Integer              extIdAmmCompetenzaPadre;
  protected Integer              idStatoPagamento;
  protected BigDecimal           importo;
  protected BigDecimal           importoInLiquidazione;
  protected BigDecimal           importoRecuperato;
  protected InterventoVO         intervento;
  protected String               istatProvincia;
  protected String               mandato;
  protected String               numDomanda;
  protected String               numeroDecreto;
  protected String               numeroMandato;
  protected SettoreExtVO         settore;
  protected String               siglaProvincia;
  protected String               tipoPagamento;

  /**
   * Recupera il valore della propriet� anno.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getAnno()
  {
    return anno;
  }

  /**
   * Imposta il valore della propriet� anno.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setAnno(Integer value)
  {
    this.anno = value;
  }

  /**
   * Recupera il valore della propriet� cuaa.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCuaa()
  {
    return cuaa;
  }

  /**
   * Imposta il valore della propriet� cuaa.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCuaa(String value)
  {
    this.cuaa = value;
  }

  /**
   * Recupera il valore della propriet� dataCreazioneLista.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataCreazioneLista()
  {
    return dataCreazioneLista;
  }

  /**
   * Imposta il valore della propriet� dataCreazioneLista.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataCreazioneLista(XMLGregorianCalendar value)
  {
    this.dataCreazioneLista = value;
  }

  /**
   * Recupera il valore della propriet� dataDecreto.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataDecreto()
  {
    return dataDecreto;
  }

  /**
   * Imposta il valore della propriet� dataDecreto.
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
   * Recupera il valore della propriet� dataMandato.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataMandato()
  {
    return dataMandato;
  }

  /**
   * Imposta il valore della propriet� dataMandato.
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
   * Recupera il valore della propriet� dataPagamento.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataPagamento()
  {
    return dataPagamento;
  }

  /**
   * Imposta il valore della propriet� dataPagamento.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataPagamento(XMLGregorianCalendar value)
  {
    this.dataPagamento = value;
  }

  /**
   * Recupera il valore della propriet� dataPresentazioneDomanda.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getDataPresentazioneDomanda()
  {
    return dataPresentazioneDomanda;
  }

  /**
   * Imposta il valore della propriet� dataPresentazioneDomanda.
   * 
   * @param value
   *          allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setDataPresentazioneDomanda(XMLGregorianCalendar value)
  {
    this.dataPresentazioneDomanda = value;
  }

  /**
   * Recupera il valore della propriet� decreto.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDecreto()
  {
    return decreto;
  }

  /**
   * Imposta il valore della propriet� decreto.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDecreto(String value)
  {
    this.decreto = value;
  }

  /**
   * Recupera il valore della propriet� denominazione.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDenominazione()
  {
    return denominazione;
  }

  /**
   * Imposta il valore della propriet� denominazione.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDenominazione(String value)
  {
    this.denominazione = value;
  }

  /**
   * Recupera il valore della propriet� descAmmCompetenza.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescAmmCompetenza()
  {
    return descAmmCompetenza;
  }

  /**
   * Imposta il valore della propriet� descAmmCompetenza.
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
   * Recupera il valore della propriet� descRegolamento.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescRegolamento()
  {
    return descRegolamento;
  }

  /**
   * Imposta il valore della propriet� descRegolamento.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescRegolamento(String value)
  {
    this.descRegolamento = value;
  }

  /**
   * Recupera il valore della propriet� descrizioneStato.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescrizioneStato()
  {
    return descrizioneStato;
  }

  /**
   * Imposta il valore della propriet� descrizioneStato.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setDescrizioneStato(String value)
  {
    this.descrizioneStato = value;
  }

  /**
   * Recupera il valore della propriet� extIdAmmCompetenza.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getExtIdAmmCompetenza()
  {
    return extIdAmmCompetenza;
  }

  /**
   * Imposta il valore della propriet� extIdAmmCompetenza.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setExtIdAmmCompetenza(Integer value)
  {
    this.extIdAmmCompetenza = value;
  }

  /**
   * Recupera il valore della propriet� extIdAmmCompetenzaPadre.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getExtIdAmmCompetenzaPadre()
  {
    return extIdAmmCompetenzaPadre;
  }

  /**
   * Imposta il valore della propriet� extIdAmmCompetenzaPadre.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setExtIdAmmCompetenzaPadre(Integer value)
  {
    this.extIdAmmCompetenzaPadre = value;
  }

  /**
   * Recupera il valore della propriet� idStatoPagamento.
   * 
   * @return possible object is {@link Integer }
   * 
   */
  public Integer getIdStatoPagamento()
  {
    return idStatoPagamento;
  }

  /**
   * Imposta il valore della propriet� idStatoPagamento.
   * 
   * @param value
   *          allowed object is {@link Integer }
   * 
   */
  public void setIdStatoPagamento(Integer value)
  {
    this.idStatoPagamento = value;
  }

  /**
   * Recupera il valore della propriet� importo.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImporto()
  {
    return importo;
  }

  /**
   * Imposta il valore della propriet� importo.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImporto(BigDecimal value)
  {
    this.importo = value;
  }

  /**
   * Recupera il valore della propriet� importoInLiquidazione.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoInLiquidazione()
  {
    return importoInLiquidazione;
  }

  /**
   * Imposta il valore della propriet� importoInLiquidazione.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoInLiquidazione(BigDecimal value)
  {
    this.importoInLiquidazione = value;
  }

  /**
   * Recupera il valore della propriet� importoRecuperato.
   * 
   * @return possible object is {@link BigDecimal }
   * 
   */
  public BigDecimal getImportoRecuperato()
  {
    return importoRecuperato;
  }

  /**
   * Imposta il valore della propriet� importoRecuperato.
   * 
   * @param value
   *          allowed object is {@link BigDecimal }
   * 
   */
  public void setImportoRecuperato(BigDecimal value)
  {
    this.importoRecuperato = value;
  }

  /**
   * Recupera il valore della propriet� intervento.
   * 
   * @return possible object is {@link InterventoVO }
   * 
   */
  public InterventoVO getIntervento()
  {
    return intervento;
  }

  /**
   * Imposta il valore della propriet� intervento.
   * 
   * @param value
   *          allowed object is {@link InterventoVO }
   * 
   */
  public void setIntervento(InterventoVO value)
  {
    this.intervento = value;
  }

  /**
   * Recupera il valore della propriet� istatProvincia.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getIstatProvincia()
  {
    return istatProvincia;
  }

  /**
   * Imposta il valore della propriet� istatProvincia.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setIstatProvincia(String value)
  {
    this.istatProvincia = value;
  }

  /**
   * Recupera il valore della propriet� mandato.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getMandato()
  {
    return mandato;
  }

  /**
   * Imposta il valore della propriet� mandato.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setMandato(String value)
  {
    this.mandato = value;
  }

  /**
   * Recupera il valore della propriet� numDomanda.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumDomanda()
  {
    return numDomanda;
  }

  /**
   * Imposta il valore della propriet� numDomanda.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNumDomanda(String value)
  {
    this.numDomanda = value;
  }

  /**
   * Recupera il valore della propriet� numeroDecreto.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroDecreto()
  {
    return numeroDecreto;
  }

  /**
   * Imposta il valore della propriet� numeroDecreto.
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
   * Recupera il valore della propriet� numeroMandato.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNumeroMandato()
  {
    return numeroMandato;
  }

  /**
   * Imposta il valore della propriet� numeroMandato.
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
   * Recupera il valore della propriet� settore.
   * 
   * @return possible object is {@link SettoreExtVO }
   * 
   */
  public SettoreExtVO getSettore()
  {
    return settore;
  }

  /**
   * Imposta il valore della propriet� settore.
   * 
   * @param value
   *          allowed object is {@link SettoreExtVO }
   * 
   */
  public void setSettore(SettoreExtVO value)
  {
    this.settore = value;
  }

  /**
   * Recupera il valore della propriet� siglaProvincia.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }

  /**
   * Imposta il valore della propriet� siglaProvincia.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setSiglaProvincia(String value)
  {
    this.siglaProvincia = value;
  }

  /**
   * Recupera il valore della propriet� tipoPagamento.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getTipoPagamento()
  {
    return tipoPagamento;
  }

  /**
   * Imposta il valore della propriet� tipoPagamento.
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
