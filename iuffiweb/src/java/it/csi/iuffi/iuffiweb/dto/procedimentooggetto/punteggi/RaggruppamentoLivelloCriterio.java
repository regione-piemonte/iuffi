package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.punteggi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RaggruppamentoLivelloCriterio implements ILoggable
{

  private static final long serialVersionUID = 2899981921857271758L;

  private String            descrizioneLivello;
  private String            raggruppamento;
  private int               idLivello;
  private BigDecimal        totalePunteggioCalcolato;
  private BigDecimal        totalePunteggioIstruttoria;
  private List<CriterioVO>  criteri;

  /**
   * @return the descrizioneLivello
   */
  public final String getDescrizioneLivello()
  {
    return descrizioneLivello;
  }

  public final String getDescrizioneLivelloCompletaHtml()
  {
    if (GenericValidator.isBlankOrNull(raggruppamento))
    {
      return descrizioneLivello;
    }
    else
    {
      return descrizioneLivello
          + "<br><span style='margin-left:1.2em;font-size:13px'>"
          + raggruppamento + "</span>";
    }
  }

  /**
   * @param descrizioneLivello
   *          the descrizioneLivello to set
   */
  public final void setDescrizioneLivello(String descrizioneLivello)
  {
    this.descrizioneLivello = descrizioneLivello;
  }

  /**
   * @return the raggruppamento
   */
  public final String getRaggruppamento()
  {
    return raggruppamento;
  }

  /**
   * @param raggruppamento
   *          the raggruppamento to set
   */
  public final void setRaggruppamento(String raggruppamento)
  {
    this.raggruppamento = raggruppamento;
  }

  /**
   * @return the idLivello
   */
  public final int getIdLivello()
  {
    return idLivello;
  }

  /**
   * @param idLivello
   *          the idLivello to set
   */
  public final void setIdLivello(int idLivello)
  {
    this.idLivello = idLivello;
  }

  /**
   * @return the criteri
   */
  public final List<CriterioVO> getCriteri()
  {
    return criteri;
  }

  /**
   * @param criteri
   *          the criteri to set
   */
  public final void setCriteri(List<CriterioVO> criteri)
  {
    this.criteri = criteri;
  }

  public BigDecimal getTotalePunteggioCalcolato()
  {
    return totalePunteggioCalcolato;
  }

  public void setTotalePunteggioCalcolato(BigDecimal totalePunteggioCalcolato)
  {
    this.totalePunteggioCalcolato = totalePunteggioCalcolato;
  }

  public BigDecimal getTotalePunteggioIstruttoria()
  {
    return totalePunteggioIstruttoria;
  }

  public void setTotalePunteggioIstruttoria(
      BigDecimal totalePunteggioIstruttoria)
  {
    this.totalePunteggioIstruttoria = totalePunteggioIstruttoria;
  }

}
