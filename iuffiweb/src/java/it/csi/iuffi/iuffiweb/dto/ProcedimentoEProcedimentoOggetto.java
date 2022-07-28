package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;

public class ProcedimentoEProcedimentoOggetto implements ILoggable
{
  /** serialVersionUID */
  private static final long     serialVersionUID = -1624333979225343408L;
  public static final String    REQUEST_NAME     = ProcedimentoEProcedimentoOggetto.class
      .getName();
  protected Procedimento        procedimento;
  protected ProcedimentoOggetto procedimentoOggetto;

  public Procedimento getProcedimento()
  {
    return procedimento;
  }

  public void setProcedimento(Procedimento procedimento)
  {
    this.procedimento = procedimento;
  }

  public ProcedimentoOggetto getProcedimentoOggetto()
  {
    return procedimentoOggetto;
  }

  public void setProcedimentoOggetto(ProcedimentoOggetto procedimentoOggetto)
  {
    this.procedimentoOggetto = procedimentoOggetto;
  }
}
