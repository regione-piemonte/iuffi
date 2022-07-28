package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiListaDaCreareDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                   serialVersionUID = 6651716824863377401L;
  protected Long                              idMaxListaLiquidazioneCorrente;
  protected List<RisorseImportiOperazioneDTO> risorse;

  public Long getIdMaxListaLiquidazioneCorrente()
  {
    return idMaxListaLiquidazioneCorrente;
  }

  public void setIdMaxListaLiquidazioneCorrente(
      Long idMaxListaLiquidazioneCorrente)
  {
    this.idMaxListaLiquidazioneCorrente = idMaxListaLiquidazioneCorrente;
  }

  public List<RisorseImportiOperazioneDTO> getRisorse()
  {
    return risorse;
  }

  public void setRisorse(List<RisorseImportiOperazioneDTO> risorse)
  {
    this.risorse = risorse;
  }
}
