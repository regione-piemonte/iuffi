package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class FonteControlloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID = 4601339650061084885L;

  private long               idFonteControllo;
  private String             descrizione;
  private List<ControlloDTO> controlli;

  public long getIdFonteControllo()
  {
    return idFonteControllo;
  }

  public void setIdFonteControllo(long idFonteControllo)
  {
    this.idFonteControllo = idFonteControllo;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<ControlloDTO> getControlli()
  {
    return controlli;
  }

  public void setControlli(List<ControlloDTO> controlli)
  {
    this.controlli = controlli;
  }

}
