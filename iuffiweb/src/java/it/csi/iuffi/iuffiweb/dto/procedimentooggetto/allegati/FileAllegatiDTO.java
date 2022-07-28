package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class FileAllegatiDTO implements ILoggable
{
  /** serialVersionUID */
  protected static final long serialVersionUID = -6426779054574957317L;
  protected long              idDettaglioInfo;
  protected long              idSelezioneInfo;
  protected long              idProcedimentoOggetto;
  protected long              idFileAllegati;
  protected Long              extIdDocumentoIndex;
  protected String            nomeLogico;
  protected String            nomeFisico;
  protected Long              extIdTipoDocumento;

  public long getIdDettaglioInfo()
  {
    return idDettaglioInfo;
  }

  public void setIdDettaglioInfo(long idDettaglioInfo)
  {
    this.idDettaglioInfo = idDettaglioInfo;
  }

  public long getIdSelezioneInfo()
  {
    return idSelezioneInfo;
  }

  public void setIdSelezioneInfo(long idSelezioneInfo)
  {
    this.idSelezioneInfo = idSelezioneInfo;
  }

  public long getIdFileAllegati()
  {
    return idFileAllegati;
  }

  public void setIdFileAllegati(long idFileAllegati)
  {
    this.idFileAllegati = idFileAllegati;
  }

  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }

  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }

  public String getNomeLogico()
  {
    return nomeLogico;
  }

  public void setNomeLogico(String nomeLogico)
  {
    this.nomeLogico = nomeLogico;
  }

  public String getNomeFisico()
  {
    return nomeFisico;
  }

  public void setNomeFisico(String nomeFisico)
  {
    this.nomeFisico = nomeFisico;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public Long getExtIdTipoDocumento()
  {
    return extIdTipoDocumento;
  }

  public void setExtIdTipoDocumento(Long extIdTipoDocumento)
  {
    this.extIdTipoDocumento = extIdTipoDocumento;
  }
}
