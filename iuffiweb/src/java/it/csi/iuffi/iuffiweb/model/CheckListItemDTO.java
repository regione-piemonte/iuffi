package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CheckListItemDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1475593026374293938L;
  
  private long idCheckList;
  private String descrizione;
  
  public CheckListItemDTO(long idCheckList, String descrizione)
  {
    super();
    this.setIdCheckList(idCheckList);
    this.descrizione = descrizione;
  }

  public CheckListItemDTO()
  {
    super();
    // TODO Auto-generated constructor stub
  }


  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public long getIdCheckList()
  {
    return idCheckList;
  }

  public void setIdCheckList(long idCheckList)
  {
    this.idCheckList = idCheckList;
  }
  
 
}
