package it.csi.iuffi.iuffiweb.dto.plsql;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ControlloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private String            codice;
  private String            descrizione;
  private String            messaggioErrore;

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getMessaggioErrore()
  {
    return messaggioErrore;
  }

  public void setMessaggioErrore(String messaggioErrore)
  {
    this.messaggioErrore = messaggioErrore;
  }

}
