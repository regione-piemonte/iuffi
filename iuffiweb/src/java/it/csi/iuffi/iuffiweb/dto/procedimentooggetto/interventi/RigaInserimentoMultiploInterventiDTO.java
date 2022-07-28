package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

public class RigaInserimentoMultiploInterventiDTO
    extends RigaModificaMultiplaInterventiDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8142945189114771573L;

  // Ritorna l'id univoco che viene utilizzato nelle funzioni di inserimento
  public long getId()
  {
    return idDescrizioneIntervento;
  }

}
