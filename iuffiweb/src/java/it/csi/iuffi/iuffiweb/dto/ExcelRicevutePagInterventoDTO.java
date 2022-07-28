package it.csi.iuffi.iuffiweb.dto;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExcelRicevutePagInterventoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                serialVersionUID = 4601339650061084885L;

  private String                           descrIntervento;
  private String                           ulterioriInformazioni;
  private String                           progressivo;

  private String                           descrFornitore;
  private List<ExcelRigaDocumentoSpesaDTO> dettaglioDocumento;

  public String getDescrInterventoUF()
  {

    if (ulterioriInformazioni != null && ulterioriInformazioni != "")
      return descrIntervento + " - " + ulterioriInformazioni;
    else
      return descrIntervento;
  }

  public String getDescrIntervento()
  {
    return descrIntervento;
  }

  public void setDescrIntervento(String descrIntervento)
  {
    this.descrIntervento = descrIntervento;
  }

  public String getDescrFornitore()
  {
    return descrFornitore;
  }

  public void setDescrFornitore(String descrFornitore)
  {
    this.descrFornitore = descrFornitore;
  }

  public List<ExcelRigaDocumentoSpesaDTO> getDettaglioDocumento()
  {
    return dettaglioDocumento;
  }

  public void setDettaglioDocumento(
      List<ExcelRigaDocumentoSpesaDTO> dettaglioDocumento)
  {
    this.dettaglioDocumento = dettaglioDocumento;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public String getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(String progressivo)
  {
    this.progressivo = progressivo;
  }

}
