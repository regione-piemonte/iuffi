package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProcedimentoOggettoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                serialVersionUID = 4601339650061084885L;

  private long                             idProcedimentoOggetto;
  private String                           identificativo;
  private String                           descrGruppoOggetto;
  private Long                             idOggetto;
  private String                           descrOggetto;
  private String                           codOggetto;
  private String                           flagIstanza;
  private String                           prefix;

  private int                              codiceRaggruppamento;
  private List<IterProcedimentoOggettoDTO> iterProcedimentoggetto;
  private BigDecimal                       importoRendicontato;

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public List<IterProcedimentoOggettoDTO> getIterProcedimentoggetto()
  {
    return iterProcedimentoggetto;
  }

  public void setIterProcedimentoggetto(
      List<IterProcedimentoOggettoDTO> iterProcedimentoggetto)
  {
    this.iterProcedimentoggetto = iterProcedimentoggetto;
  }

  public String getDescrGruppoOggetto()
  {
    return descrGruppoOggetto;
  }

  public void setDescrGruppoOggetto(String descrGruppoOggetto)
  {
    this.descrGruppoOggetto = descrGruppoOggetto;
  }

  public String getDescrOggetto()
  {
    return descrOggetto;
  }

  public void setDescrOggetto(String descrOggetto)
  {
    this.descrOggetto = descrOggetto;
  }

  public Long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(Long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public String getCodOggetto()
  {
    return codOggetto;
  }

  public void setCodOggetto(String codOggetto)
  {
    this.codOggetto = codOggetto;
  }

  public int getCodiceRaggruppamento()
  {
    return codiceRaggruppamento;
  }

  public void setCodiceRaggruppamento(int codiceRaggruppamento)
  {
    this.codiceRaggruppamento = codiceRaggruppamento;
  }

  public String getFlagIstanza()
  {
    return flagIstanza;
  }

  public void setFlagIstanza(String flagIstanza)
  {
    this.flagIstanza = flagIstanza;
  }

  public BigDecimal getImportoRendicontato()
  {
    return importoRendicontato;
  }

  public void setImportoRendicontato(BigDecimal importoRendicontato)
  {
    this.importoRendicontato = importoRendicontato;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  private Integer numIstrNeg;

  public Integer getNumIstrNeg()
  {
    return numIstrNeg;
  }

  public void setNumIstrNeg(Integer numIstrNeg)
  {
    this.numIstrNeg = numIstrNeg;
  }

}
