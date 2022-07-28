package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaRendicontazioneDocumentiSpesaDTO implements ILoggable
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
  private BigDecimal        importoDisponibile;
  private String            importoRendicontatoString;
  private BigDecimal        importoRendicontato;
  private BigDecimal        importoDaRendicontare;
  private String            nomeFile;
  private String            errorMessage;
  private long              idDocumentoSpesaInterven;
  private long              idDocumentoSpesa;
  private BigDecimal        importoRicevutePagamento;
  private long              idIntervento;
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
    return (importoDaRendicontare != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoDaRendicontare)
        : "";
  }

  public String getImportoRendicontatoStr()
  {
    return (importoRendicontato != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoRendicontato)
        : "";
  }

  public BigDecimal getImportoDisponibile()
  {
    return importoDisponibile;
  }

  public void setImportoDisponibile(BigDecimal importoDisponibile)
  {
    this.importoDisponibile = importoDisponibile;
  }

  public BigDecimal getImportoRicevutePagamento()
  {
    return importoRicevutePagamento;
  }

  public void setImportoRicevutePagamento(BigDecimal importoRicevutePagamento)
  {
    this.importoRicevutePagamento = importoRicevutePagamento;
  }

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
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
