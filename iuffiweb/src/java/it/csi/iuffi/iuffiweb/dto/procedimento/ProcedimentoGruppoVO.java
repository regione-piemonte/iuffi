package it.csi.iuffi.iuffiweb.dto.procedimento;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ProcedimentoGruppoVO implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID = -8234162717069476571L;
  public static final String REQUEST_NAME     = "procedimento";

  private long               idProcedimento;
  private long               idProcedimentoGruppo;
  private long               codiceRaggruppamento;
  private long               extIdUtente;
  private String             utenteAggiornamento;
  private Date               dataInizio;
  private Date               dataFine;
  private String             motivazioni;
  private String             flagGruppoChiuso;
  private String             flagGruppoChiusoDecod;

  public String getDataInizioStr()
  {
    return IuffiUtils.DATE.formatDate(dataInizio);
  }

  public String getDataFineStr()
  {
    return IuffiUtils.DATE.formatDate(dataFine);
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public long getIdProcedimentoGruppo()
  {
    return idProcedimentoGruppo;
  }

  public void setIdProcedimentoGruppo(long idProcedimentoGruppo)
  {
    this.idProcedimentoGruppo = idProcedimentoGruppo;
  }

  public long getCodiceRaggruppamento()
  {
    return codiceRaggruppamento;
  }

  public void setCodiceRaggruppamento(long codiceRaggruppamento)
  {
    this.codiceRaggruppamento = codiceRaggruppamento;
  }

  public long getExtIdUtente()
  {
    return extIdUtente;
  }

  public void setExtIdUtente(long extIdUtente)
  {
    this.extIdUtente = extIdUtente;
  }

  public String getUtenteAggiornamento()
  {
    return utenteAggiornamento;
  }

  public void setUtenteAggiornamento(String utenteAggiornamento)
  {
    this.utenteAggiornamento = utenteAggiornamento;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public String getMotivazioni()
  {
    return motivazioni;
  }

  public void setMotivazioni(String motivazioni)
  {
    this.motivazioni = motivazioni;
  }

  public String getFlagGruppoChiuso()
  {
    return flagGruppoChiuso;
  }

  public void setFlagGruppoChiuso(String flagGruppoChiuso)
  {
    this.flagGruppoChiuso = flagGruppoChiuso;
  }

  public String getFlagGruppoChiusoDecod()
  {
    return flagGruppoChiusoDecod;
  }

  public void setFlagGruppoChiusoDecod(String flagGruppoChiusoDecod)
  {
    this.flagGruppoChiusoDecod = flagGruppoChiusoDecod;
  }

}
