package it.csi.iuffi.iuffiweb.dto.procedimento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DocumentoSpesaVO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idIntervento;
  private long              idDocumentoSpesa;
  private Long              idDocumentoSpesaFile;
  private long              idDettDocumentoSpesa;
  private long              idProcedimentoOggetto;
  private long              idProcedimento;
  private long              idTipoDocumentoSpesa;
  private Long              idModalitaPagamento;
  private Long              idFornitore;
  private String            descrIntervento;
  private String            numeroDocumentoSpesa;

  private Date              dataDocumentoSpesa;
  private Date              dataPagamento;
  private BigDecimal        importoSpesa;
  private BigDecimal        importoRendicontato;
  private BigDecimal        importoDaRendicontare;
  private BigDecimal        importoIva;
  private BigDecimal        importoAssociato;
  private BigDecimal        importoLordoPagamento;
  private BigDecimal        importoAssociatoRic;
  private Integer           numeroIstruttorieAppN;
  private boolean           poApprovatoNegativo;

  public String getImportoAssociatoRicStr()
  {
    return (importoAssociatoRic != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoAssociatoRic)
        : "";
  }

  public String getImportoLordoPagamentoStr()
  {
    return (importoLordoPagamento != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoLordoPagamento)
        : "";
  }

  public String getImportoAncoraDaRendicontareStr()
  {
    BigDecimal impDaRend = BigDecimal.ZERO;
    if (importoAssociato == null)
      importoAssociato = BigDecimal.ZERO;
    if (importoRendicontato == null)
      importoRendicontato = BigDecimal.ZERO;

    impDaRend = importoAssociato.subtract(importoRendicontato);

    return (impDaRend != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(impDaRend) : "";
  }

  public BigDecimal getImportoAncoraDaRendicontare()
  {
    BigDecimal impDaRend = BigDecimal.ZERO;
    if (importoAssociato == null)
      importoAssociato = BigDecimal.ZERO;
    if (importoRendicontato == null)
      importoRendicontato = BigDecimal.ZERO;

    impDaRend = importoAssociato.subtract(importoRendicontato);

    return impDaRend;
  }

  public BigDecimal getImportoLordo()
  {
    if (importoIva != null)
      return importoIva.add(importoSpesa);

    return importoSpesa;
  }

  public String getImportoLordoStr()
  {
    BigDecimal ret = importoSpesa;
    if (importoIva != null)
      ret = ret.add(importoIva);

    return (ret != null) ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(ret)
        : "";
  }

  public String getImportoIvaStr()
  {
    return (importoIva != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoIva) : "";
  }

  public BigDecimal getImportoIva()
  {
    return importoIva == null ? BigDecimal.ZERO : importoIva;
  }

  public void setImportoIva(BigDecimal importoIva)
  {
    this.importoIva = importoIva;
  }

  private String                       note;
  private String                       nomeFileLogicoDocumentoSpe;
  private String                       nomeFileFisicoDocumentoSpe;
  private String                       prefix;

  private String                       descrModPagamento;
  private String                       descrTipoDocumento;
  private String                       codiceFornitore;
  private String                       ragioneSociale;
  private String                       indirizzoSedeLegale;
  private String                       flagEliminabile;
  private String                       flagHasInterventiAssociati;
  private String                       flagShowIconDocGiaRendicontato;
  private String                       flagShowIconEuro;

  private byte[]                       fileDocumentoSpesa;
  private String                       tipoDomanda;
  private Integer                      progressivo;

  private String                       iconaFile;

  private List<RicevutaPagamentoVO>    elencoRicevutePagamento;
  private List<DocumentoSpesaVO>       allegati;
  private List<ProcedimentoOggettoDTO> oggettiDoc;

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public long getIdDocumentoSpesa()
  {
    return idDocumentoSpesa;
  }

  public void setIdDocumentoSpesa(long idDocumentoSpesa)
  {
    this.idDocumentoSpesa = idDocumentoSpesa;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public long getIdTipoDocumentoSpesa()
  {
    return idTipoDocumentoSpesa;
  }

  public void setIdTipoDocumentoSpesa(long idTipoDocumentoSpesa)
  {
    this.idTipoDocumentoSpesa = idTipoDocumentoSpesa;
  }

  public Long getIdModalitaPagamento()
  {
    return idModalitaPagamento;
  }

  public void setIdModalitaPagamento(Long idModalitaPagamento)
  {
    this.idModalitaPagamento = idModalitaPagamento;
  }

  public Long getIdFornitore()
  {
    return idFornitore;
  }

  public void setIdFornitore(Long idFornitore)
  {
    this.idFornitore = idFornitore;
  }

  public String getDescrIntervento()
  {
    return descrIntervento;
  }

  public void setDescrIntervento(String descrIntervento)
  {
    this.descrIntervento = descrIntervento;
  }

  public String getNumeroDocumentoSpesa()
  {
    return numeroDocumentoSpesa;
  }

  public void setNumeroDocumentoSpesa(String numeroDocumentoSpesa)
  {
    this.numeroDocumentoSpesa = numeroDocumentoSpesa;
  }

  public Date getDataDocumentoSpesa()
  {
    return dataDocumentoSpesa;
  }

  public String getDataDocumentoSpesaStr()
  {
    return IuffiUtils.DATE.formatDate(dataDocumentoSpesa);
  }

  public void setDataDocumentoSpesa(Date dataDocumentoSpesa)
  {
    this.dataDocumentoSpesa = dataDocumentoSpesa;
  }

  public Date getDataPagamento()
  {
    return dataPagamento;
  }

  public String getDataPagamentoStr()
  {
    return IuffiUtils.DATE.formatDate(dataPagamento);
  }

  public void setDataPagamento(Date dataPagamento)
  {
    this.dataPagamento = dataPagamento;
  }

  public BigDecimal getImportoSpesa()
  {
    return importoSpesa;
  }

  public String getImportoSpesaStr()
  {
    return (importoSpesa != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoSpesa) : "";
  }

  public void setImportoSpesa(BigDecimal importoSpesa)
  {
    this.importoSpesa = importoSpesa;
  }

  public String getImportoRendicontatoStr()
  {
    importoRendicontato = getImportoRendicontato();
    return (importoRendicontato != null)
        ? "&euro; " + IuffiUtils.FORMAT.formatCurrency(importoRendicontato)
        : "";
  }

  public String getImportoRendicontatoStr2()
  {
    importoRendicontato = getImportoRendicontato();
    return (importoRendicontato != null)
        ? IuffiUtils.FORMAT.formatCurrency(importoRendicontato) : "";
  }

  public BigDecimal getImportoRendicontato()
  {

    BigDecimal importo = BigDecimal.ZERO;
    if (oggettiDoc != null && !oggettiDoc.isEmpty())
      for (ProcedimentoOggettoDTO p : oggettiDoc)
        if (p.getImportoRendicontato() != null)
          importo = importo.add(p.getImportoRendicontato());

    return importoRendicontato;
  }

  public void setImportoRendicontato(BigDecimal importoRendicontato)
  {
    this.importoRendicontato = importoRendicontato;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getNomeFileLogicoDocumentoSpe()
  {

    if (nomeFileLogicoDocumentoSpe != null)
      return nomeFileLogicoDocumentoSpe;

    long idDocSpFile = -1l;
    String nomeLogico = "";
    String nomeFisico = "";
    if (allegati != null && !allegati.isEmpty())
    {
      for (DocumentoSpesaVO all : allegati)
      {
        if (all.getIdDocumentoSpesaFile() > idDocSpFile)
        {
          idDocSpFile = all.getIdDocumentoSpesaFile();
          nomeLogico = all.getNomeFileLogicoDocumentoSpe();
          nomeFisico = all.getNomeFileFisicoDocumentoSpe();
        }
      }
      this.nomeFileFisicoDocumentoSpe = nomeFisico;
      this.nomeFileLogicoDocumentoSpe = nomeLogico;
      return nomeLogico;
    }
    else
      return nomeFileLogicoDocumentoSpe;
  }

  public void setNomeFileLogicoDocumentoSpe(String nomeFileLogicoDocumentoSpe)
  {
    this.nomeFileLogicoDocumentoSpe = nomeFileLogicoDocumentoSpe;
  }

  public String getNomeFileFisicoDocumentoSpe()
  {

    if (nomeFileFisicoDocumentoSpe != null)
      return nomeFileFisicoDocumentoSpe;

    long idDocSpFile = -1l;
    String nomeLogico = "";
    String nomeFisico = "";
    if (allegati != null && !allegati.isEmpty())
    {
      for (DocumentoSpesaVO all : allegati)
      {
        if (all.getIdDocumentoSpesaFile() > idDocSpFile)
        {
          idDocSpFile = all.getIdDocumentoSpesaFile();
          nomeLogico = all.getNomeFileLogicoDocumentoSpe();
          nomeFisico = all.getNomeFileFisicoDocumentoSpe();
        }
      }
      this.nomeFileFisicoDocumentoSpe = nomeFisico;
      this.nomeFileLogicoDocumentoSpe = nomeLogico;
      return nomeFisico;
    }
    else
      return nomeFileFisicoDocumentoSpe;
  }

  public void setNomeFileFisicoDocumentoSpe(String nomeFileFisicoDocumentoSpe)
  {
    this.nomeFileFisicoDocumentoSpe = nomeFileFisicoDocumentoSpe;
  }

  public String getDescrModPagamento()
  {
    return descrModPagamento;
  }

  public void setDescrModPagamento(String descrModPagamento)
  {
    this.descrModPagamento = descrModPagamento;
  }

  public String getDescrTipoDocumento()
  {
    return descrTipoDocumento;
  }

  public void setDescrTipoDocumento(String descrTipoDocumento)
  {
    this.descrTipoDocumento = descrTipoDocumento;
  }

  public String getRagioneSociale()
  {
    return ragioneSociale;
  }

  public void setRagioneSociale(String ragioneSociale)
  {
    this.ragioneSociale = ragioneSociale;
  }

  public String getIndirizzoSedeLegale()
  {
    return indirizzoSedeLegale;
  }

  public void setIndirizzoSedeLegale(String indirizzoSedeLegale)
  {
    this.indirizzoSedeLegale = indirizzoSedeLegale;
  }

  public byte[] getFileDocumentoSpesa()
  {
    return fileDocumentoSpesa;
  }

  public void setFileDocumentoSpesa(byte[] fileDocumentoSpesa)
  {
    this.fileDocumentoSpesa = fileDocumentoSpesa;
  }

  public String getFlagEliminabile()
  {
    return flagEliminabile;
  }

  public void setFlagEliminabile(String flagEliminabile)
  {
    this.flagEliminabile = flagEliminabile;
  }

  public String getIconaFile()
  {
    return iconaFile;
  }

  public void setIconaFile(String iconaFile)
  {
    this.iconaFile = iconaFile;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public long getIdDettDocumentoSpesa()
  {
    return idDettDocumentoSpesa;
  }

  public void setIdDettDocumentoSpesa(long idDettDocumentoSpesa)
  {
    this.idDettDocumentoSpesa = idDettDocumentoSpesa;
  }

  public String getNomeFileLogicoDocumentoSpeLink()
  {

    long idDocSpFile = -1l;
    String nomeLogico = "";
    String nomeFisico = "";
    if (allegati != null && !allegati.isEmpty())
    {
      for (DocumentoSpesaVO all : allegati)
      {
        if (all.getIdDocumentoSpesaFile() > idDocSpFile)
        {
          idDocSpFile = all.getIdDocumentoSpesaFile();
          nomeLogico = all.getNomeFileLogicoDocumentoSpe();
          nomeFisico = all.getNomeFileFisicoDocumentoSpe();
        }
      }
      this.nomeFileFisicoDocumentoSpe = nomeFisico;
      this.nomeFileLogicoDocumentoSpe = nomeLogico;
    }

    return nomeFileLogicoDocumentoSpe + " <a href=\"../cuiuffi263m/download_"
        + idDettDocumentoSpesa + ".do\" class=\"ico24 " + iconaFile
        + "\" title = " + nomeFileFisicoDocumentoSpe + "></a>";
  }

  public String getCodiceFornitore()
  {
    return codiceFornitore;
  }

  public void setCodiceFornitore(String codiceFornitore)
  {
    this.codiceFornitore = codiceFornitore;
  }

  public String getTipoDomanda()
  {
    return tipoDomanda;
  }

  public void setTipoDomanda(String tipoDomanda)
  {
    this.tipoDomanda = tipoDomanda;
  }

  public Integer getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(Integer progressivo)
  {
    this.progressivo = progressivo;
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

  public void setElencoRicevutePagamento(
      List<RicevutaPagamentoVO> elencoRicevutePagamento)
  {
    this.elencoRicevutePagamento = elencoRicevutePagamento;
  }

  public List<RicevutaPagamentoVO> getElencoRicevutePagamento()
  {
    return elencoRicevutePagamento;
  }

  public List<DocumentoSpesaVO> getAllegati()
  {
    return allegati;
  }

  public void setAllegati(List<DocumentoSpesaVO> allegati)
  {
    this.allegati = allegati;
  }

  public Long getIdDocumentoSpesaFile()
  {
    if (idDocumentoSpesaFile != null)
      return idDocumentoSpesaFile;

    long idDocSpFile = -1l;
    String nomeLogico = "";
    String nomeFisico = "";
    if (allegati != null && !allegati.isEmpty())
    {
      for (DocumentoSpesaVO all : allegati)
      {
        if (all.getIdDocumentoSpesaFile() > idDocSpFile)
        {
          idDocSpFile = all.getIdDocumentoSpesaFile();
          nomeLogico = all.getNomeFileLogicoDocumentoSpe();
          nomeFisico = all.getNomeFileFisicoDocumentoSpe();
        }
      }
      this.nomeFileFisicoDocumentoSpe = nomeFisico;
      this.nomeFileLogicoDocumentoSpe = nomeLogico;
      return idDocSpFile;
    }
    else
      return idDocumentoSpesaFile;
  }

  public void setIdDocumentoSpesaFile(long idDocumentoSpesaFile)
  {
    this.idDocumentoSpesaFile = idDocumentoSpesaFile;
  }

  public BigDecimal getImportoAssociato()
  {
    return importoAssociato;
  }

  public void setImportoAssociato(BigDecimal importoAssociato)
  {
    this.importoAssociato = importoAssociato;
  }

  public List<ProcedimentoOggettoDTO> getOggettiDoc()
  {
    return oggettiDoc;
  }

  public String getCodOggettiDoc()
  {
    String ret = "&&&";

    if (oggettiDoc == null || oggettiDoc.isEmpty())
      ret += "000&&&";

    BigDecimal impR = getImportoRendicontato();
    if (importoAssociato != null && impR != null)
      if (importoAssociato.subtract(impR).compareTo(BigDecimal.ZERO) != 0
          || (importoAssociato.compareTo(BigDecimal.ZERO) == 0
              && impR.compareTo(BigDecimal.ZERO) == 0))
        ret += "000&&&";

    if (oggettiDoc != null && !oggettiDoc.isEmpty())
      for (ProcedimentoOggettoDTO p : oggettiDoc)
      {
        if (p.getPrefix() != null)
          ret += p.getDescrOggetto() + " - " + p.getPrefix() + "&&&";
        else
          ret += "000&&&";
      }

    return ret;
  }

  public void setOggettiDoc(List<ProcedimentoOggettoDTO> oggettiDoc)
  {
    this.oggettiDoc = oggettiDoc;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  public String getFlagHasInterventiAssociati()
  {
    return flagHasInterventiAssociati;
  }

  public void setFlagHasInterventiAssociati(String flagHasInterventiAssociati)
  {
    this.flagHasInterventiAssociati = flagHasInterventiAssociati;
  }

  public String getFlagShowIconDocGiaRendicontato()
  {
    return flagShowIconDocGiaRendicontato;
  }

  public void setFlagShowIconDocGiaRendicontato(
      String flagShowIconDocGiaRendicontato)
  {
    this.flagShowIconDocGiaRendicontato = flagShowIconDocGiaRendicontato;
  }

  public String getFlagShowIconEuro()
  {
    return flagShowIconEuro;
  }

  public void setFlagShowIconEuro(String flagShowIconEuro)
  {
    this.flagShowIconEuro = flagShowIconEuro;
  }

  public BigDecimal getImportoLordoPagamento()
  {
    return importoLordoPagamento;
  }

  public void setImportoLordoPagamento(BigDecimal importoLordoPagamento)
  {
    this.importoLordoPagamento = importoLordoPagamento;
  }

  public BigDecimal getImportoAssociatoRic()
  {
    return importoAssociatoRic;
  }

  public void setImportoAssociatoRic(BigDecimal importoAssociatoRic)
  {
    this.importoAssociatoRic = importoAssociatoRic;
  }

  public Integer getNumeroIstruttorieAppN()
  {
    return numeroIstruttorieAppN;
  }

  public void setNumeroIstruttorieAppN(Integer numeroIstruttorieAppN)
  {
    this.numeroIstruttorieAppN = numeroIstruttorieAppN;
  }

  public boolean isPoApprovatoNegativo()
  {
    return poApprovatoNegativo;
  }

  public void setPoApprovatoNegativo(boolean poApprovatoNegativo)
  {
    this.poApprovatoNegativo = poApprovatoNegativo;
  }

}
