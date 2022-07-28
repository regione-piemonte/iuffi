package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datafinelavori;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DataFineLavoriDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6865684393141163651L;

  private long              idFineLavori;
  private long              idProcedimentoOggetto;
  private String            descrProcedimentoOggetto;
  private Date              dataFineLavori;
  private Date              dataTermineRendicontazione;
  private Date              dataProroga;
  private String            note;
  private String            flagIstanza;

  public long getIdFineLavori()
  {
    return idFineLavori;
  }

  public void setIdFineLavori(long idFineLavori)
  {
    this.idFineLavori = idFineLavori;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public Date getDataFineLavori()
  {
    return dataFineLavori;
  }

  public String getDataFineLavoriStr()
  {
    return IuffiUtils.DATE.formatDate(dataFineLavori);
  }

  public String getDataTermineRendicontazioneStr()
  {
    return IuffiUtils.DATE.formatDate(dataTermineRendicontazione);
  }

  public String getDataProrogaStr()
  {
    return IuffiUtils.DATE.formatDate(dataProroga);
  }

  public void setDataFineLavori(Date dataFineLavori)
  {
    this.dataFineLavori = dataFineLavori;
  }

  public Date getDataTermineRendicontazione()
  {
    return dataTermineRendicontazione;
  }

  public void setDataTermineRendicontazione(Date dataTermineRendicontazione)
  {
    this.dataTermineRendicontazione = dataTermineRendicontazione;
  }

  public Date getDataProroga()
  {
    return dataProroga;
  }

  public void setDataProroga(Date dataProroga)
  {
    this.dataProroga = dataProroga;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getDescrProcedimentoOggetto()
  {
    return descrProcedimentoOggetto;
  }

  public void setDescrProcedimentoOggetto(String descrProcedimentoOggetto)
  {
    this.descrProcedimentoOggetto = descrProcedimentoOggetto;
  }

  public String getFlagIstanza()
  {
    return flagIstanza;
  }

  public void setFlagIstanza(String flagIstanza)
  {
    this.flagIstanza = flagIstanza;
  }

  public boolean isProcOggIstanza()
  {
    return IuffiConstants.FLAGS.SI.equals(flagIstanza);
  }

}
