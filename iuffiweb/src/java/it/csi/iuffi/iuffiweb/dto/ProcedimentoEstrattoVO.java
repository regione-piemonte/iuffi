package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ProcedimentoEstrattoVO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7048395105781721238L;

  private long              idProcedimentoEstratto;
  private String            flagEstrazione;
  private Date              dataEstrazione;

  public String getFlagEstrazione()
  {
    return flagEstrazione;
  }

  public void setFlagEstrazione(String flagEstrazione)
  {
    this.flagEstrazione = flagEstrazione;
  }

  public Date getDataEstrazione()
  {
    return dataEstrazione;
  }

  public void setDataEstrazione(Date dataEstrazione)
  {
    this.dataEstrazione = dataEstrazione;
  }

  public String getStatoEstrazione()
  {
    if (flagEstrazione.toUpperCase().compareTo("C") == 0)
      return "Casuale";
    if (flagEstrazione.toUpperCase().compareTo("R") == 0)
      return "Rischio";
    if (flagEstrazione.toUpperCase().compareTo("N") == 0)
      return "Non estratta";
    if (flagEstrazione.toUpperCase().compareTo("M") == 0)
      return "Manuale per verifica impegni OD";
    return "";
  }

  public String getDataEstrazioneStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataEstrazione);
  }

  public long getIdProcedimentoEstratto()
  {
    return idProcedimentoEstratto;
  }

  public void setIdProcedimentoEstratto(long idProcedimentoEstratto)
  {
    this.idProcedimentoEstratto = idProcedimentoEstratto;
  }

}
