package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati;

public class ContenutoFileAllegatiDTO extends FileAllegatiDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -6426779054574957317L;
  protected byte[]          contenuto;

  public byte[] getContenuto()
  {
    return contenuto;
  }

  public void setContenuto(byte[] contenuto)
  {
    this.contenuto = contenuto;
  }

  public static ContenutoFileAllegatiDTO from(FileAllegatiDTO f)
  {
    if (f == null)
    {
      return null;
    }
    ContenutoFileAllegatiDTO c = new ContenutoFileAllegatiDTO();
    c.idDettaglioInfo = f.idDettaglioInfo;
    c.idSelezioneInfo = f.idSelezioneInfo;
    c.idProcedimentoOggetto = f.idProcedimentoOggetto;
    c.idFileAllegati = f.idFileAllegati;
    c.extIdDocumentoIndex = f.extIdDocumentoIndex;
    c.nomeLogico = f.nomeLogico;
    c.nomeFisico = f.nomeFisico;
    return c;
  }
}
