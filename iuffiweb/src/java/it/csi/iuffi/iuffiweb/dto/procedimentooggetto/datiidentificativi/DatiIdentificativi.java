package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiIdentificativi implements ILoggable
{
  /** serialVersionUID */
  private static final long           serialVersionUID = -8952850635361198139L;
  private DatiAziendaDTO              azienda;
  private DatiRappresentanteLegaleDTO rappLegale;
  private DatiSoggettoFirmatarioDTO   soggFirmatario;
  private DatiProcedimento            datiProcedimento;
  private SettoriDiProduzioneDTO      settore;

  public DatiAziendaDTO getAzienda()
  {
    return azienda;
  }

  public void setAzienda(DatiAziendaDTO azienda)
  {
    this.azienda = azienda;
  }

  public DatiRappresentanteLegaleDTO getRappLegale()
  {
    return rappLegale;
  }

  public void setRappLegale(DatiRappresentanteLegaleDTO rappLegale)
  {
    this.rappLegale = rappLegale;
  }

  public DatiProcedimento getDatiProcedimento()
  {
    return datiProcedimento;
  }

  public void setDatiProcedimento(DatiProcedimento datiProcedimento)
  {
    this.datiProcedimento = datiProcedimento;
  }

  public DatiSoggettoFirmatarioDTO getSoggFirmatario()
  {
    return soggFirmatario;
  }

  public void setSoggFirmatario(DatiSoggettoFirmatarioDTO soggFirmatario)
  {
    this.soggFirmatario = soggFirmatario;
  }

  public SettoriDiProduzioneDTO getSettore()
  {
    return settore;
  }

  public void setSettore(SettoriDiProduzioneDTO settore)
  {
    this.settore = settore;
  }

}
