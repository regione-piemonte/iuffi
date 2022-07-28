package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SegnapostoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 9210431096002755374L;
  private int               idSegnaposto;
  private String            nome;
  private String            descrizione;
  private String            istruzioneSql;

  public int getIdSegnaposto()
  {
    return idSegnaposto;
  }

  public void setIdSegnaposto(int idSegnaposto)
  {
    this.idSegnaposto = idSegnaposto;
  }

  public String getNome()
  {
    return nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getIstruzioneSql()
  {
    return istruzioneSql;
  }

  public void setIstruzioneSql(String istruzioneSql)
  {
    this.istruzioneSql = istruzioneSql;
  }
}
