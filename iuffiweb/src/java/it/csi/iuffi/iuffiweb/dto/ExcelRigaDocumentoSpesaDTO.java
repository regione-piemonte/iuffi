package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExcelRigaDocumentoSpesaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long             serialVersionUID = 4601339650061084885L;

  private String                        dataDocumentoStr;
  private String                        tipoDocumento;
  private BigDecimal                    importoNetto;
  private BigDecimal                    importoLordo;
  private BigDecimal                    importoIva;
  private BigDecimal                    importoAssociato;
  private BigDecimal                    importoDaRendicontare;
  private BigDecimal                    importoRendicontato;
  private String                        denominazioneDomanda;
  private String                        noteDocumentoSpesa;
  private String                        numeroDocumento;
  private Long                          idDocumento;
  private String                        fornitore;
  private Long                          idFornitore;

  private List<ExcelRigaRicevutaPagDTO> ricevute;

  public String getDataDocumentoStr()
  {
    return dataDocumentoStr;
  }

  public void setDataDocumentoStr(String dataDocumentoStr)
  {
    this.dataDocumentoStr = dataDocumentoStr;
  }

  public String getTipoDocumento()
  {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento)
  {
    this.tipoDocumento = tipoDocumento;
  }

  public BigDecimal getImportoDaRendicontare()
  {
    return importoDaRendicontare;
  }

  public void setImportoDaRendicontare(BigDecimal importoDaRendicontare)
  {
    this.importoDaRendicontare = importoDaRendicontare;
  }

  public BigDecimal getImportoRendicontato()
  {
    return importoRendicontato;
  }

  public void setImportoRendicontato(BigDecimal importoRendicontato)
  {
    this.importoRendicontato = importoRendicontato;
  }

  public String getDenominazioneDomanda()
  {
    return denominazioneDomanda;
  }

  public void setDenominazioneDomanda(String denominazioneDomanda)
  {
    this.denominazioneDomanda = denominazioneDomanda;
  }

  public String getNoteDocumentoSpesa()
  {
    return noteDocumentoSpesa;
  }

  public void setNoteDocumentoSpesa(String noteDocumentoSpesa)
  {
    this.noteDocumentoSpesa = noteDocumentoSpesa;
  }

  public List<ExcelRigaRicevutaPagDTO> getRicevute()
  {
    return ricevute;
  }

  public void setRicevute(List<ExcelRigaRicevutaPagDTO> ricevute)
  {
    this.ricevute = ricevute;
  }

  public String getFornitore()
  {
    return fornitore;
  }

  public void setFornitore(String fornitore)
  {
    this.fornitore = fornitore;
  }

  public BigDecimal getImportoNetto()
  {
    return importoNetto;
  }

  public void setImportoNetto(BigDecimal importoNetto)
  {
    this.importoNetto = importoNetto;
  }

  public BigDecimal getImportoLordo()
  {
    return importoLordo;
  }

  public void setImportoLordo(BigDecimal importoLordo)
  {
    this.importoLordo = importoLordo;
  }

  public BigDecimal getImportoIva()
  {
    return importoIva;
  }

  public void setImportoIva(BigDecimal importoIva)
  {
    this.importoIva = importoIva;
  }

  public BigDecimal getImportoAssociato()
  {
    return importoAssociato;
  }

  public void setImportoAssociato(BigDecimal importoAssociato)
  {
    this.importoAssociato = importoAssociato;
  }

  public String getNumeroDocumento()
  {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento)
  {
    this.numeroDocumento = numeroDocumento;
  }

  public Long getIdDocumento()
  {
    return idDocumento;
  }

  public void setIdDocumento(Long idDocumento)
  {
    this.idDocumento = idDocumento;
  }

  public Long getIdFornitore()
  {
    return idFornitore;
  }

  public void setIdFornitore(Long idFornitore)
  {
    this.idFornitore = idFornitore;
  }

}
