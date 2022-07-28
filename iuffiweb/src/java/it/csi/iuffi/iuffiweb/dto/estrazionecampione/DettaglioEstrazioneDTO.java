package it.csi.iuffi.iuffiweb.dto.estrazionecampione;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;

public class DettaglioEstrazioneDTO implements ILoggable
{

  private static final long                  serialVersionUID = 5918223009553272325L;

  private ImportiTotaliDTO                   totali;
  private ImportiAttualiPrecedentiDTO        importiPA;
  private List<RigaSimulazioneEstrazioneDTO> elenco;

  public ImportiTotaliDTO getTotali()
  {
    return totali;
  }

  public void setTotali(ImportiTotaliDTO totali)
  {
    this.totali = totali;
  }

  public ImportiAttualiPrecedentiDTO getImportiPA()
  {
    return importiPA;
  }

  public void setImportiPA(ImportiAttualiPrecedentiDTO importiPA)
  {
    this.importiPA = importiPA;
  }

  public List<RigaSimulazioneEstrazioneDTO> getElenco()
  {
    return elenco;
  }

  public void setElenco(List<RigaSimulazioneEstrazioneDTO> elenco)
  {
    this.elenco = elenco;
  }

}
