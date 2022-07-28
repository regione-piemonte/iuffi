package it.csi.iuffi.iuffiweb.dto.procedimento;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TipoDocumentoSpesaVO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idTipoDocumentoSpesa;
  private String            descrizione;
  private String            ordine;

  private String            flagIdFornitore;
  private String            flagDataDocumentoSpesa;
  private String            flagNumeroDocumentoSpesa;
  private String            flagDataPagamento;
  private String            flagIdModalitaPagamento;
  private String            flagPercentualeIva;
  private String            flagNote;
  private String            flagFileDocumentoSpesa;

  public long getIdTipoDocumentoSpesa()
  {
    return idTipoDocumentoSpesa;
  }

  public void setIdTipoDocumentoSpesa(long idTipoDocumentoSpesa)
  {
    this.idTipoDocumentoSpesa = idTipoDocumentoSpesa;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getOrdine()
  {
    return ordine;
  }

  public void setOrdine(String ordine)
  {
    this.ordine = ordine;
  }

  public String getFlagIdFornitore()
  {
    return flagIdFornitore;
  }

  public void setFlagIdFornitore(String flagIdFornitore)
  {
    this.flagIdFornitore = flagIdFornitore;
  }

  public String getFlagDataDocumentoSpesa()
  {
    return flagDataDocumentoSpesa;
  }

  public void setFlagDataDocumentoSpesa(String flagDataDocumentoSpesa)
  {
    this.flagDataDocumentoSpesa = flagDataDocumentoSpesa;
  }

  public String getFlagNumeroDocumentoSpesa()
  {
    return flagNumeroDocumentoSpesa;
  }

  public void setFlagNumeroDocumentoSpesa(String flagNumeroDocumentoSpesa)
  {
    this.flagNumeroDocumentoSpesa = flagNumeroDocumentoSpesa;
  }

  public String getFlagDataPagamento()
  {
    return flagDataPagamento;
  }

  public void setFlagDataPagamento(String flagDataPagamento)
  {
    this.flagDataPagamento = flagDataPagamento;
  }

  public String getFlagIdModalitaPagamento()
  {
    return flagIdModalitaPagamento;
  }

  public void setFlagIdModalitaPagamento(String flagIdModalitaPagamento)
  {
    this.flagIdModalitaPagamento = flagIdModalitaPagamento;
  }

  public String getFlagPercentualeIva()
  {
    return flagPercentualeIva;
  }

  public void setFlagPercentualeIva(String flagPercentualeIva)
  {
    this.flagPercentualeIva = flagPercentualeIva;
  }

  public String getFlagNote()
  {
    return flagNote;
  }

  public void setFlagNote(String flagNote)
  {
    this.flagNote = flagNote;
  }

  public String getFlagFileDocumentoSpesa()
  {
    return flagFileDocumentoSpesa;
  }

  public void setFlagFileDocumentoSpesa(String flagFileDocumentoSpesa)
  {
    this.flagFileDocumentoSpesa = flagFileDocumentoSpesa;
  }
}
