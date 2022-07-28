package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.punteggi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class CriterioVO implements ILoggable
{

  private static final long serialVersionUID = -2570711883739622615L;

  private int               idCriterio;
  private int               idPrincipioSelezione;
  private long              idBandoLivelloCriterio;
  private String            descPrincipioSelezione;
  private String            codice;
  private String            criterioSelezione;
  private String            specifiche;
  private String            punteggio;
  private BigDecimal        punteggioMin;
  private BigDecimal        punteggioMax;
  private String            flagElaborazione;
  private BigDecimal        punteggioCalcolato;
  private BigDecimal        punteggioIstruttoria;

  /**
   * @return the idCriterio
   */
  public final int getIdCriterio()
  {
    return idCriterio;
  }

  /**
   * @param idCriterio
   *          the idCriterio to set
   */
  public final void setIdCriterio(int idCriterio)
  {
    this.idCriterio = idCriterio;
  }

  /**
   * @return the idPrincipioSelezione
   */
  public final int getIdPrincipioSelezione()
  {
    return idPrincipioSelezione;
  }

  /**
   * @param idPrincipioSelezione
   *          the idPrincipioSelezione to set
   */
  public final void setIdPrincipioSelezione(int idPrincipioSelezione)
  {
    this.idPrincipioSelezione = idPrincipioSelezione;
  }

  /**
   * @return the idBandoLivelloCriterio
   */
  public final long getIdBandoLivelloCriterio()
  {
    return idBandoLivelloCriterio;
  }

  /**
   * @param idBandoLivelloCriterio
   *          the idBandoLivelloCriterio to set
   */
  public final void setIdBandoLivelloCriterio(long idBandoLivelloCriterio)
  {
    this.idBandoLivelloCriterio = idBandoLivelloCriterio;
  }

  /**
   * @return the descPrincipioSelezione
   */
  public final String getDescPrincipioSelezione()
  {
    return descPrincipioSelezione;
  }

  public String getDescPrincipioSelezioneHtml()
  {
    return (descPrincipioSelezione != null)
        ? IuffiUtils.STRING.safeHTMLText(descPrincipioSelezione) : null;
  }

  /**
   * @param descPrincipioSelezione
   *          the descPrincipioSelezione to set
   */
  public final void setDescPrincipioSelezione(String descPrincipioSelezione)
  {
    this.descPrincipioSelezione = descPrincipioSelezione;
  }

  /**
   * @return the codice
   */
  public final String getCodice()
  {
    return codice;
  }

  public String getCodiceHtml()
  {
    return (codice != null) ? IuffiUtils.STRING.safeHTMLText(codice) : null;
  }

  /**
   * @param codice
   *          the codice to set
   */
  public final void setCodice(String codice)
  {
    this.codice = codice;
  }

  /**
   * @return the criterioSelezione
   */
  public final String getCriterioSelezione()
  {
    return criterioSelezione;
  }

  public String getCriterioSelezioneHtml()
  {
    return (criterioSelezione != null)
        ? IuffiUtils.STRING.safeHTMLText(criterioSelezione) : null;
  }

  /**
   * @param criterioSelezione
   *          the criterioSelezione to set
   */
  public final void setCriterioSelezione(String criterioSelezione)
  {
    this.criterioSelezione = criterioSelezione;
  }

  /**
   * @return the specifiche
   */
  public final String getSpecifiche()
  {
    return specifiche;
  }

  public final String getSpecificheHtml()
  {
    return (specifiche != null) ? IuffiUtils.STRING.safeHTMLText(specifiche)
        : null;
  }

  /**
   * @param specifiche
   *          the specifiche to set
   */
  public final void setSpecifiche(String specifiche)
  {
    this.specifiche = specifiche;
  }

  /**
   * @return the flagElaborazione
   */
  public final String getFlagElaborazione()
  {
    return flagElaborazione;
  }

  public String getFlagElaborazioneDecod()
  {

    if ("A".equals(flagElaborazione))
      return "Automatica";
    if ("M".equals(flagElaborazione))
      return "Manuale";

    return "";
  }

  /**
   * @param flagElaborazione
   *          the flagElaborazione to set
   */
  public final void setFlagElaborazione(String flagElaborazione)
  {
    this.flagElaborazione = flagElaborazione;
  }

  /**
   * @return the punteggioCalcolato
   */
  public final BigDecimal getPunteggioCalcolato()
  {
    return punteggioCalcolato;
  }

  /**
   * @param punteggioCalcolato
   *          the punteggioCalcolato to set
   */
  public final void setPunteggioCalcolato(BigDecimal punteggioCalcolato)
  {
    this.punteggioCalcolato = punteggioCalcolato;
  }

  /**
   * @return the serialversionuid
   */
  public static final long getSerialversionuid()
  {
    return serialVersionUID;
  }

  /**
   * @return the punteggio
   */
  public final String getPunteggio()
  {
    return punteggio;
  }

  /**
   * @param punteggio
   *          the punteggio to set
   */
  public final void setPunteggio(String punteggio)
  {
    this.punteggio = punteggio;
  }

  /**
   * @return the punteggioMin
   */
  public final BigDecimal getPunteggioMin()
  {
    return punteggioMin;
  }

  /**
   * @param punteggioMin
   *          the punteggioMin to set
   */
  public final void setPunteggioMin(BigDecimal punteggioMin)
  {
    this.punteggioMin = punteggioMin;
  }

  /**
   * @return the punteggioMax
   */
  public final BigDecimal getPunteggioMax()
  {
    return punteggioMax;
  }

  /**
   * @param punteggioMax
   *          the punteggioMax to set
   */
  public final void setPunteggioMax(BigDecimal punteggioMax)
  {
    this.punteggioMax = punteggioMax;
  }

  public BigDecimal getPunteggioIstruttoria()
  {
    return punteggioIstruttoria;
  }

  public void setPunteggioIstruttoria(BigDecimal punteggioIstruttoria)
  {
    this.punteggioIstruttoria = punteggioIstruttoria;
  }

}
