package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento;

import java.util.Date;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class DatiSpecificiDTO extends ControlliInLocoInvestDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5640820658585672497L;
  protected String          flagEstratta;
  protected String          flagSottopostaEstrazione;
  protected Date            dataEstrazione;
  protected String          descTipoEstrazione;
  protected Long            numeroEstrazione;

  public String getDescTipoEstrazioneNumLotto()
  {
    String ret = "";
    if (descTipoEstrazione != null)
      ret += descTipoEstrazione;

    if (numeroEstrazione != null && numeroEstrazione.toString() != "")
      ret += " - n° " + numeroEstrazione;

    return ret;

  }

  public Long getNumeroEstrazione()
  {
    return numeroEstrazione;
  }

  public void setNumeroEstrazione(Long numeroEstrazione)
  {
    this.numeroEstrazione = numeroEstrazione;
  }

  public String getFlagEstratta()
  {
    return flagEstratta;
  }

  public void setFlagEstratta(String flagEstratta)
  {
    this.flagEstratta = flagEstratta;
  }

  public Date getDataEstrazione()
  {
    return dataEstrazione;
  }

  public void setDataEstrazione(Date dataEstrazione)
  {
    this.dataEstrazione = dataEstrazione;
  }

  public String getDescTipoEstrazione()
  {
    return descTipoEstrazione;
  }

  public void setDescTipoEstrazione(String descTipoEstrazione)
  {
    this.descTipoEstrazione = descTipoEstrazione;
  }

  public String getFlagSottopostaEstrazione()
  {
    return flagSottopostaEstrazione;
  }

  public void setFlagSottopostaEstrazione(String flagSottopostaEstrazione)
  {
    this.flagSottopostaEstrazione = flagSottopostaEstrazione;
  }

  public boolean isEstratta()
  {
    return !IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.NON_ESTRATTA
        .equals(flagEstratta)
        && !IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.DICHIARAZIONI_SOSTITUTIVE
            .equals(flagEstratta);
  }
}
