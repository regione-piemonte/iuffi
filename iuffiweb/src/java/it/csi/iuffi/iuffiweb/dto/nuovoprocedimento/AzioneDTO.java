package it.csi.iuffi.iuffiweb.dto.nuovoprocedimento;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AzioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idAzione;
  private String            label;
  private String            codiceCU;
  private String            documentoCU;

  private List<AzioneDTO>   azioni;

  public long getIdAzione()
  {
    return idAzione;
  }

  public void setIdAzione(long idAzione)
  {
    this.idAzione = idAzione;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getCodiceCU()
  {
    return codiceCU;
  }

  public void setCodiceCU(String codiceCU)
  {
    this.codiceCU = codiceCU;
  }

  public String getDocumentoCU()
  {
    return documentoCU;
  }

  public void setDocumentoCU(String documentoCU)
  {
    this.documentoCU = documentoCU;
  }

  public List<AzioneDTO> getAzioni()
  {
    return azioni;
  }

  public void setAzioni(List<AzioneDTO> azioni)
  {
    this.azioni = azioni;
  }

}
