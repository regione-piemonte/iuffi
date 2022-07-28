package it.csi.iuffi.iuffiweb.dto.login;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProcedimentoAgricoloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 2882046216335004689L;
  private long idProcedimentoAgricolo;
  private String codice;
  private String descrizione;
  private String descrizioneEstesa;
  
  public long getIdProcedimentoAgricolo()
  {
    return idProcedimentoAgricolo;
  }
  public void setIdProcedimentoAgricolo(long idProcedimentoAgricolo)
  {
    this.idProcedimentoAgricolo = idProcedimentoAgricolo;
  }
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
  public String getDescrizioneEstesa()
  {
    return descrizioneEstesa;
  }
  public void setDescrizioneEstesa(String descrizioneEstesa)
  {
    this.descrizioneEstesa = descrizioneEstesa;
  }
}
