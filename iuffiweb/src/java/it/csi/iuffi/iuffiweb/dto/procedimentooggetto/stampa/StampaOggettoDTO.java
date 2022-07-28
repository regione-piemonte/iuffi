package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class StampaOggettoDTO extends StampaOggettoIconaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -8943303518439121263L;
  protected String          nomeIconaStampa;
  protected String          tooltipStampa;
  protected long            idIconaStato;
  protected String          nomeIconaStato;
  protected String          tooltipStato;
  protected String          codiceCdu;
  protected String          extCodMacroCdu;
  protected Long            extIdUtenteAggiornamento;
  protected Long            extIdDocumentoIndex;
  protected Date            dataUltimoAggiornamento;
  protected Date            dataFineStampa;
  protected String          descUltimoAggiornamento;
  protected Long            idProcedimOggettoStampa;
  protected Long            idStatoStampa;

  public String getNomeIconaStampa()
  {
    return nomeIconaStampa;
  }

  public void setNomeIconaStampa(String nomeIconaStampa)
  {
    this.nomeIconaStampa = nomeIconaStampa;
  }

  public String getNomeIconaStampaVisualizza()
  {
    return idProcedimOggettoStampa != null ? nomeIconaStampa : null;
  }
  
  public String getNomeIconaEliminaIstanza()
  {
    return null;
  }

  public String getNomeIconaStampaGenera()
  {
    return idProcedimOggettoStampa == null ? nomeIconaStampa : null;
  }

  public String getTooltipStampa()
  {
    return tooltipStampa;
  }

  public void setTooltipStampa(String tooltipStampa)
  {
    this.tooltipStampa = tooltipStampa;
  }

  public String getNomeIconaStato()
  {
    return nomeIconaStato;
  }

  public void setNomeIconaStato(String nomeIconaStato)
  {
    this.nomeIconaStato = nomeIconaStato;
  }

  public String getTooltipStato()
  {
    return tooltipStato;
  }

  public void setTooltipStato(String tooltipStato)
  {
    this.tooltipStato = tooltipStato;
  }

  public String getCodiceCdu()
  {
    return codiceCdu;
  }

  public void setCodiceCdu(String codiceCdu)
  {
    this.codiceCdu = codiceCdu;
  }

  public String getExtCodMacroCdu()
  {
    return extCodMacroCdu;
  }

  public void setExtCodMacroCdu(String extCodMacroCdu)
  {
    this.extCodMacroCdu = extCodMacroCdu;
  }

  public Long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(Long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public String getDescUltimoAggiornamento()
  {
    return descUltimoAggiornamento;
  }

  public void setDescUltimoAggiornamento(String descUltimoAggiornamento)
  {
    this.descUltimoAggiornamento = descUltimoAggiornamento;
  }

  public long getIdIconaStato()
  {
    return idIconaStato;
  }

  public void setIdIconaStato(long idIconaStato)
  {
    this.idIconaStato = idIconaStato;
  }

  public Long getIdProcedimOggettoStampa()
  {
    return idProcedimOggettoStampa;
  }

  public void setIdProcedimOggettoStampa(Long idProcedimOggettoStampa)
  {
    this.idProcedimOggettoStampa = idProcedimOggettoStampa;
  }

  public String getNomeIconaElimina()
  {
    // Posso eliminare se posso inserire
    if (IuffiConstants.USECASE.STAMPE_OGGETTO.INSERISCI_STAMPA_OGGETTO
        .equals(codiceCdu))
    {
      return "ico24 ico_trash";
    }
    else
    {
      return null;
    }
  }

  public Long getIdStatoStampa()
  {
    return idStatoStampa;
  }

  public void setIdStatoStampa(Long idStatoStampa)
  {
    this.idStatoStampa = idStatoStampa;
  }

  public Date getDataFineStampa()
  {
    return dataFineStampa;
  }

  public void setDataFineStampa(Date dataFineStampa)
  {
    this.dataFineStampa = dataFineStampa;
  }

  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }

  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }

}
