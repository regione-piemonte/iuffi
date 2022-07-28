package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaAccertamentoDocumentiSpesaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 351785307284150899L;

  private int               progressivo;
  private String            descIntervento;
  private String            ragioneSociale;
  private Date              dataDocumentoSpesa;
  private String            numeroDocumentoSpesa;
  private String            descTipoDocumentoSpesa;
  private Date              dataPagamento;
  private String            descModalitaPagamento;
  private BigDecimal        importoSpesa;
  private BigDecimal        importo;
  private BigDecimal        importoRendicontatoPrec;
  private String            importoRendicontatoString;
  private BigDecimal        importoRendicontato;
  private BigDecimal        importoDaRendicontare;
  private String            nomeFile;
  private String            errorMessage;
  private long              idDocumentoSpesaInterven;
  private long              idDocumentoSpesa;

  private String            importoAccertato;
  private String            importoNonRiconosciuto;
  private String            importoDisponibile;
  private String            importoCalcoloContributo;

  private String            errorMessageImportoAccertato;
  private String            errorMessageImportoNonRiconosciuto;
  private String            errorMessageImportoDisponibile;
  private String            errorMessageImportoCalcoloContributo;
  private String            flagInterventoCompletato;
  private String            note;
  private boolean           warningDocumento;

  public int getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(int progressivo)
  {
    this.progressivo = progressivo;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public String getRagioneSociale()
  {
    return ragioneSociale;
  }

  public void setRagioneSociale(String ragioneSociale)
  {
    this.ragioneSociale = ragioneSociale;
  }

  public String getDataDocumentoSpesa()
  {
    return IuffiUtils.DATE.formatDate(dataDocumentoSpesa);
  }

  public void setDataDocumentoSpesa(Date dataDocumentoSpesa)
  {
    this.dataDocumentoSpesa = dataDocumentoSpesa;
  }

  public String getNumeroDocumentoSpesa()
  {
    return numeroDocumentoSpesa;
  }

  public void setNumeroDocumentoSpesa(String numeroDocumentoSpesa)
  {
    this.numeroDocumentoSpesa = numeroDocumentoSpesa;
  }

  public String getDescTipoDocumentoSpesa()
  {
    return descTipoDocumentoSpesa;
  }

  public void setDescTipoDocumentoSpesa(String descTipoDocumentoSpesa)
  {
    this.descTipoDocumentoSpesa = descTipoDocumentoSpesa;
  }

  public String getDataPagamento()
  {
    return IuffiUtils.DATE.formatDate(dataPagamento);
  }

  public void setDataPagamento(Date dataPagamento)
  {
    this.dataPagamento = dataPagamento;
  }

  public String getDescModalitaPagamento()
  {
    return descModalitaPagamento;
  }

  public void setDescModalitaPagamento(String descModalitaPagamento)
  {
    this.descModalitaPagamento = descModalitaPagamento;
  }

  public BigDecimal getImportoSpesa()
  {
    return importoSpesa;
  }

  public void setImportoSpesa(BigDecimal importoSpesa)
  {
    this.importoSpesa = importoSpesa;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public BigDecimal getImportoRendicontatoPrec()
  {
    return importoRendicontatoPrec;
  }

  public void setImportoRendicontatoPrec(BigDecimal importoRendicontatoPrec)
  {
    this.importoRendicontatoPrec = importoRendicontatoPrec;
  }

  public BigDecimal getImportoRendicontato()
  {
    return importoRendicontato;
  }

  public void setImportoRendicontato(BigDecimal importoRendicontato)
  {
    this.importoRendicontato = importoRendicontato;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }

  public String getErrorMessage()
  {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  public long getIdDocumentoSpesaInterven()
  {
    return idDocumentoSpesaInterven;
  }

  public void setIdDocumentoSpesaInterven(long idDocumentoSpesaInterven)
  {
    this.idDocumentoSpesaInterven = idDocumentoSpesaInterven;
  }

  public String getImportoRendicontatoString()
  {
    return importoRendicontatoString;
  }

  public void setImportoRendicontatoString(String importoRendicontatoString)
  {
    this.importoRendicontatoString = importoRendicontatoString;
  }

  public String getImportoRendicontatoCorrente()
  {
    if (importoRendicontatoString != null)
    {
      return importoRendicontatoString;
    }
    return IuffiUtils.FORMAT.formatGenericNumber(importoRendicontato, 2,
        true, false);
  }

  public long getIdDocumentoSpesa()
  {
    return idDocumentoSpesa;
  }

  public void setIdDocumentoSpesa(long idDocumentoSpesa)
  {
    this.idDocumentoSpesa = idDocumentoSpesa;
  }

  public BigDecimal getImportoDaRendicontare()
  {
    return importoDaRendicontare;
  }

  public void setImportoDaRendicontare(BigDecimal importoDaRendicontare)
  {
    this.importoDaRendicontare = importoDaRendicontare;
  }

  public String getImportoDaRendicontareStr()
  {
    return (importoDaRendicontare != null
        && importoDaRendicontare.compareTo(BigDecimal.ZERO) != 0)
            ? "&euro; "
                + IuffiUtils.FORMAT.formatCurrency(importoDaRendicontare)
            : "";
  }

  public String getImportoRendicontatoStr()
  {
    return (importoRendicontato != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoRendicontato)
        : "";
  }

  public String getImportoAccertato()
  {
    return importoAccertato;
  }

  public BigDecimal getImportoAccertatoBD()
  {
    return IuffiUtils.NUMBERS.getBigDecimal(importoAccertato);
  }

  public void setImportoAccertato(String importoAccertato)
  {
    this.importoAccertato = importoAccertato;
  }

  public void setImportoAccertato(BigDecimal importoAccertato)
  {
    this.importoAccertato = IuffiUtils.FORMAT
        .formatGenericNumber(importoAccertato, 2, true, false);
  }

  public BigDecimal getImportoNonRiconosciutoBD()
  {
    return IuffiUtils.NUMBERS.getBigDecimal(importoNonRiconosciuto);
  }

  public String getImportoNonRiconosciuto()
  {
    return importoNonRiconosciuto;
  }

  public void setImportoNonRiconosciuto(String importoNonRiconosciuto)
  {
    this.importoNonRiconosciuto = importoNonRiconosciuto;
  }

  public void setImportoNonRiconosciuto(BigDecimal importoNonRiconosciuto)
  {
    this.importoNonRiconosciuto = IuffiUtils.FORMAT
        .formatGenericNumber(importoNonRiconosciuto, 2, true, false);
  }

  public BigDecimal getImportoDisponibileBD()
  {
    return IuffiUtils.NUMBERS.getBigDecimal(importoDisponibile);
  }

  public String getImportoDisponibile()
  {
    return importoDisponibile;
  }

  public void setImportoDisponibile(String importoDisponibile)
  {
    this.importoDisponibile = importoDisponibile;
  }

  public void setImportoDisponibile(BigDecimal importoDisponibile)
  {
    this.importoDisponibile = IuffiUtils.FORMAT
        .formatGenericNumber(importoDisponibile, 2, true, false);
  }

  public BigDecimal getImportoCalcoloContributoBD()
  {
    return IuffiUtils.NUMBERS.getBigDecimal(importoCalcoloContributo);
  }

  public String getImportoCalcoloContributo()
  {
    return importoCalcoloContributo;
  }

  public void setImportoCalcoloContributo(String importoCalcoloContributo)
  {
    this.importoCalcoloContributo = importoCalcoloContributo;
  }

  public void setImportoCalcoloContributo(BigDecimal importoCalcoloContributo)
  {
    this.importoCalcoloContributo = IuffiUtils.FORMAT
        .formatGenericNumber(importoCalcoloContributo, 2, true, false);
  }

  public String getErrorMessageImportoAccertato()
  {
    return errorMessageImportoAccertato;
  }

  public void setErrorMessageImportoAccertato(
      String errorMessageImportoAccertato)
  {
    this.errorMessageImportoAccertato = errorMessageImportoAccertato;
  }

  public String getErrorMessageImportoNonRiconosciuto()
  {
    return errorMessageImportoNonRiconosciuto;
  }

  public void setErrorMessageImportoNonRiconosciuto(
      String errorMessageImportoNonRiconosciuto)
  {
    this.errorMessageImportoNonRiconosciuto = errorMessageImportoNonRiconosciuto;
  }

  public String getErrorMessageImportoDisponibile()
  {
    return errorMessageImportoDisponibile;
  }

  public void setErrorMessageImportoDisponibile(
      String errorMessageImportoDisponibile)
  {
    this.errorMessageImportoDisponibile = errorMessageImportoDisponibile;
  }

  public String getErrorMessageImportoCalcoloContributo()
  {
    return errorMessageImportoCalcoloContributo;
  }

  public void setErrorMessageImportoCalcoloContributo(
      String errorMessageImportoCalcoloContributo)
  {
    this.errorMessageImportoCalcoloContributo = errorMessageImportoCalcoloContributo;
  }

  public String getFlagInterventoCompletato()
  {
    return flagInterventoCompletato;
  }

  public void setFlagInterventoCompletato(String flagInterventoCompletato)
  {
    this.flagInterventoCompletato = flagInterventoCompletato;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public boolean isWarningDocumento()
  {
    return warningDocumento;
  }

  public void setWarningDocumento(boolean warningDocumento)
  {
    this.warningDocumento = warningDocumento;
  }

}
