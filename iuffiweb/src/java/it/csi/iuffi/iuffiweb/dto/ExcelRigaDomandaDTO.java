package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExcelRigaDomandaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long                   serialVersionUID = 7257221562827481342L;

  private Long                                idProcedimentoOggetto;
  private String                              prefix;
  private String                              descrOggetto;
  private List<ExcelRicevutePagInterventoDTO> interventi;
  private BigDecimal                          importoRendicontato;

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  public String getDescrOggetto()
  {
    return descrOggetto;
  }

  public void setDescrOggetto(String descrOggetto)
  {
    this.descrOggetto = descrOggetto;
  }

  public List<ExcelRicevutePagInterventoDTO> getInterventi()
  {
    return interventi;
  }

  public void setInterventi(List<ExcelRicevutePagInterventoDTO> interventi)
  {
    this.interventi = interventi;
  }

  public BigDecimal getImportoRendicontato()
  {
    return importoRendicontato;
  }

  public void setImportoRendicontato(BigDecimal importoRendicontato)
  {
    this.importoRendicontato = importoRendicontato;
  }
}
