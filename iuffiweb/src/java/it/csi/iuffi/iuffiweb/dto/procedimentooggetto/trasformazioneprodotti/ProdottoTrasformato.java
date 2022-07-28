package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProdottoTrasformato implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private long idprodottotrasformato; 
  private long idprodotto;
  private long iddatiprocedimento;
  private String note; 
  private String descrizione;
  private int index; 
  
  public long getIdprodottotrasformato()
  {
    return idprodottotrasformato;
  }
  public void setIdprodottotrasformato(long idprodottotrasformato)
  {
    this.idprodottotrasformato = idprodottotrasformato;
  }
  public long getIdprodotto()
  {
    return idprodotto;
  }
  public void setIdprodotto(long idprodotto)
  {
    this.idprodotto = idprodotto;
  }
  public long getIddatiprocedimento()
  {
    return iddatiprocedimento;
  }
  public void setIddatiprocedimento(long iddatiprocedimento)
  {
    this.iddatiprocedimento = iddatiprocedimento;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public int getIndex()
  {
    return index;
  }
  public void setIndex(int index)
  {
    this.index = index;
  } 

}
