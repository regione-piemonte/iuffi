package it.csi.iuffi.iuffiweb.dto.nuovoprocedimento;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class QuadroDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private String            codice;
  private String            descrizione;

  private List<AzioneDTO>   azioni;

  public QuadroDTO(String codice, String descrizione)
  {
    super();
    this.codice = codice;
    this.descrizione = descrizione;
  }

  public QuadroDTO()
  {
    super();
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

  public List<AzioneDTO> getAzioni()
  {
    return azioni;
  }

  public void setAzioni(List<AzioneDTO> azioni)
  {
    this.azioni = azioni;
  }

}
