package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ControlliInLocoInvestDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -7461374428223825657L;
  protected long            idProcedimentoOggetto;
  protected String          flagPreavviso;
  protected Date            dataPreavviso;
  protected Long            idTipologiaPreavviso;
  protected String          descTipologiaPreavviso;
  protected String          flagControllo;

  protected Long            idQuadroOggettoControlliLoco;
  protected Long            idQuadroOggettoControlliTecnicoAmm;

  public final long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public final void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public final String getFlagPreavviso()
  {
    return flagPreavviso;
  }

  public final void setFlagPreavviso(String flagPreavviso)
  {
    this.flagPreavviso = flagPreavviso;
  }

  public final Date getDataPreavviso()
  {
    return dataPreavviso;
  }

  public final void setDataPreavviso(Date dataPreavviso)
  {
    this.dataPreavviso = dataPreavviso;
  }

  public final Long getIdTipologiaPreavviso()
  {
    return idTipologiaPreavviso;
  }

  public final void setIdTipologiaPreavviso(Long idTipologiaPreavviso)
  {
    this.idTipologiaPreavviso = idTipologiaPreavviso;
  }

  public final String getDescTipologiaPreavviso()
  {
    return descTipologiaPreavviso;
  }

  public final void setDescTipologiaPreavviso(String descTipologiaPreavviso)
  {
    this.descTipologiaPreavviso = descTipologiaPreavviso;
  }

  public final String getFlagControllo()
  {
    return flagControllo;
  }

  public final void setFlagControllo(String flagControllo)
  {
    this.flagControllo = flagControllo;
  }

  public Long getIdQuadroOggettoControlliLoco()
  {
    return idQuadroOggettoControlliLoco;
  }

  public void setIdQuadroOggettoControlliLoco(Long idQuadroOggettoControlliLoco)
  {
    this.idQuadroOggettoControlliLoco = idQuadroOggettoControlliLoco;
  }

  public Long getIdQuadroOggettoControlliTecnicoAmm()
  {
    return idQuadroOggettoControlliTecnicoAmm;
  }

  public void setIdQuadroOggettoControlliTecnicoAmm(
      Long idQuadroOggettoControlliTecnicoAmm)
  {
    this.idQuadroOggettoControlliTecnicoAmm = idQuadroOggettoControlliTecnicoAmm;
  }

}
