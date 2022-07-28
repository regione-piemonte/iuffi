package it.csi.iuffi.iuffiweb.dto.permission;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class UpdatePermissionProcedimentoOggetto implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -4581533567261374480L;
  private Date              dataFineProcedimentoOggetto;
  private Date              dataFineBandoOggetto;
  private String            flagBandoOggettoAttivo;
  private int               countNotificheBloccanti;
  private int               countNotificheGravi;
  private String            extCodAttore;
  private String            flagIstanza;

  public Date getDataFineProcedimentoOggetto()
  {
    return dataFineProcedimentoOggetto;
  }

  public void setDataFineProcedimentoOggetto(Date dataFineProcedimentoOggetto)
  {
    this.dataFineProcedimentoOggetto = dataFineProcedimentoOggetto;
  }

  public Date getDataFineBandoOggetto()
  {
    return dataFineBandoOggetto;
  }

  public void setDataFineBandoOggetto(Date dataFineBandoOggetto)
  {
    this.dataFineBandoOggetto = dataFineBandoOggetto;
  }

  public String getFlagBandoOggettoAttivo()
  {
    return flagBandoOggettoAttivo;
  }

  public void setFlagBandoOggettoAttivo(String flagBandoOggettoAttivo)
  {
    this.flagBandoOggettoAttivo = flagBandoOggettoAttivo;
  }

  public int getCountNotificheBloccanti()
  {
    return countNotificheBloccanti;
  }

  public void setCountNotificheBloccanti(int countNotificheBloccanti)
  {
    this.countNotificheBloccanti = countNotificheBloccanti;
  }

  public String getExtCodAttore()
  {
    return extCodAttore;
  }

  public void setExtCodAttore(String extCodAttore)
  {
    this.extCodAttore = extCodAttore;
  }

  public int getCountNotificheGravi()
  {
    return countNotificheGravi;
  }

  public void setCountNotificheGravi(int countNotificheGravi)
  {
    this.countNotificheGravi = countNotificheGravi;
  }

  public String getFlagIstanza()
  {
    return flagIstanza;
  }

  public void setFlagIstanza(String flagIstanza)
  {
    this.flagIstanza = flagIstanza;
  }

}
