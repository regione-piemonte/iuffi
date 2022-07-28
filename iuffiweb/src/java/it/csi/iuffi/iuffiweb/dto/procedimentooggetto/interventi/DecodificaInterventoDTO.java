package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;

public class DecodificaInterventoDTO extends DecodificaDTO<Long>
{
  /** serialVersionUID */
  private static final long serialVersionUID = 2183267880347952375L;
  private String            descrizioneAggregazionePrimoLivello;

  public String getDescrizioneAggregazionePrimoLivello()
  {
    return descrizioneAggregazionePrimoLivello;
  }

  public void setDescrizioneAggregazionePrimoLivello(
      String descrizioneAggregazionePrimoLivello)
  {
    this.descrizioneAggregazionePrimoLivello = descrizioneAggregazionePrimoLivello;
  }
}
