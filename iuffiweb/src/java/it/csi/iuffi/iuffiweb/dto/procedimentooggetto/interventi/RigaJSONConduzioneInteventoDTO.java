package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaJSONConduzioneInteventoDTO
    extends AbstractRigaJSONCatastoInteventoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -1235011575768213104L;
  protected long            idConduzioneDichiarata;
  protected long            idUtilizzoDichiarato;
  protected String          descTipoUtilizzo;
  protected BigDecimal      superficieUtilizzata;
  protected long            idDichiarazioneConsistenza;
  protected String          superficieImpegno;
  protected String          error;
  protected String          descrizioneDestinazione;
  protected String          descTipoDettaglioUso;
  protected String          descrizioneQualitaUso;
  protected String          descTipoVarieta;

  public String getId()
  {
    return idDichiarazioneConsistenza + "_" + idConduzioneDichiarata + "_"
        + idUtilizzoDichiarato;
  }

  public void setIdConduzioneDichiarata(long idConduzioneDichiarata)
  {
    this.idConduzioneDichiarata = idConduzioneDichiarata;
  }

  public void setIdUtilizzoDichiarato(long idUtilizzoDichiarato)
  {
    this.idUtilizzoDichiarato = idUtilizzoDichiarato;
  }

  public String getDescTipoUtilizzo()
  {
    return descTipoUtilizzo;
  }

  public void setDescTipoUtilizzo(String descTipoUtilizzo)
  {
    this.descTipoUtilizzo = descTipoUtilizzo;
  }

  public String getSuperficieUtilizzata()
  {
    return IuffiUtils.FORMAT.formatDecimal4(superficieUtilizzata);
  }

  public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
  {
    this.superficieUtilizzata = superficieUtilizzata;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }

  public void setIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }

  public String getSuperficieImpegno()
  {
    return superficieImpegno;
  }

  public void setSuperficieImpegno(String superficieImpegno)
  {
    this.superficieImpegno = superficieImpegno;
  }

  public String getError()
  {
    return error;
  }

  public void setError(String error)
  {
    this.error = error;
  }

  public SuperficieConduzione toSuperficieConduzione()
  {
    SuperficieConduzione s = new SuperficieConduzione();
    s.setIdConduzioneDichiarata(idConduzioneDichiarata);
    s.setIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
    s.setIdUtilizzoDichiarato(idUtilizzoDichiarato);
    s.setSuperficieImpegno(
        IuffiUtils.NUMBERS.getBigDecimal(superficieImpegno));
    return s;
  }

  public String getDescrizioneDestinazione()
  {
    return descrizioneDestinazione;
  }

  public void setDescrizioneDestinazione(String descrizioneDestinazione)
  {
    this.descrizioneDestinazione = descrizioneDestinazione;
  }

  public String getDescTipoDettaglioUso()
  {
    return descTipoDettaglioUso;
  }

  public void setDescTipoDettaglioUso(String descTipoDettaglioUso)
  {
    this.descTipoDettaglioUso = descTipoDettaglioUso;
  }

  public String getDescTipoVarieta()
  {
    return descTipoVarieta;
  }

  public void setDescTipoVarieta(String descTipoVarieta)
  {
    this.descTipoVarieta = descTipoVarieta;
  }

  public long getIdConduzioneDichiarata()
  {
    return idConduzioneDichiarata;
  }

  public long getIdUtilizzoDichiarato()
  {
    return idUtilizzoDichiarato;
  }

  public String getDescrizioneQualitaUso()
  {
    return descrizioneQualitaUso;
  }

  public void setDescrizioneQualitaUso(String descrizioneQualitaUso)
  {
    this.descrizioneQualitaUso = descrizioneQualitaUso;
  }
}
