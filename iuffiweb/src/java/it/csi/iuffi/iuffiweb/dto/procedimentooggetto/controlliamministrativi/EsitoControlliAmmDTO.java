package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class EsitoControlliAmmDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8304402414510338227L;
  protected long            idProcedimentoOggetto;
  protected long            idQuadroOggControlloAmm;
  protected String          note;
  protected long            idEsito;

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public long getIdQuadroOggControlloAmm()
  {
    return idQuadroOggControlloAmm;
  }

  public void setIdQuadroOggControlloAmm(long idQuadroOggControlloAmm)
  {
    this.idQuadroOggControlloAmm = idQuadroOggControlloAmm;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }
}
