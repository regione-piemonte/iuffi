package it.csi.iuffi.iuffiweb.dto.plsql;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class MainControlloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private int               risultato;
  private String            messaggio;
  private BigDecimal        idProcedimento;
  private ControlloDTO      controlli[];

  public int getRisultato()
  {
    return risultato;
  }

  public void setRisultato(int risultato)
  {
    this.risultato = risultato;
  }

  public String getMessaggio()
  {
    return messaggio;
  }

  public void setMessaggio(String messaggio)
  {
    this.messaggio = messaggio;
  }

  public ControlloDTO[] getControlli()
  {
    return controlli;
  }

  public void setControlli(ControlloDTO[] controlli)
  {
    this.controlli = controlli;
  }

  public BigDecimal getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(BigDecimal idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }
}
