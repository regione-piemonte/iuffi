package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AccertamentoRendicontazioneDocumentiSpesaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                       serialVersionUID = 2147868281407954334L;
  private long                                    idIntervento;
  private String                                  descIntervento;
  private int                                     progressivo;
  private List<RigaAccertamentoDocumentiSpesaDTO> accertamento;

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public int getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(int progressivo)
  {
    this.progressivo = progressivo;
  }

  public List<RigaAccertamentoDocumentiSpesaDTO> getAccertamento()
  {
    return accertamento;
  }

  public void setAccertamento(
      List<RigaAccertamentoDocumentiSpesaDTO> accertamento)
  {
    this.accertamento = accertamento;
  }

}
