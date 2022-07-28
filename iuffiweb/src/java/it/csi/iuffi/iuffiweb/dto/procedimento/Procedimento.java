package it.csi.iuffi.iuffiweb.dto.procedimento;

import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class Procedimento implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID = -8234162717069476571L;
  public static final String REQUEST_NAME     = "procedimento";

  private long               idProcedimento;
  private long               idBando;
  private long               idStatoOggetto;
  private String             codiceTipoBando;
  private String             identificativo;
  private String             descrStato;
  private String             flagRendicontazioneConIva;
  private String             flagRendicontazioneDocSpesa;
  private Date               dataUltimoAggiornamento;
  private long               extIdUtenteAggiornamento;
  private List<String>       operazioni;
  private int				 idProcedimentoAgricolo;

  public String getElencoOperazioniHtml()
  {
    if (operazioni != null && !operazioni.isEmpty())
    {
      String html = "";
      for (String op : operazioni)
      {
        html += op + "<br/>";
      }
      return html;
    }
    return "";
  }

  public List<String> getOperazioni()
  {
    return operazioni;
  }

  public void setOperazioni(List<String> operazioni)
  {
    this.operazioni = operazioni;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(long idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public String getDescrStato()
  {
    return descrStato;
  }

  public void setDescrStato(String descrStato)
  {
    this.descrStato = descrStato;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public String getFlagRendicontazioneConIva()
  {
    return flagRendicontazioneConIva;
  }

  public void setFlagRendicontazioneConIva(String flagRendicontazioneConIva)
  {
    this.flagRendicontazioneConIva = flagRendicontazioneConIva;
  }

  public String getFlagRendicontazioneDocSpesa()
  {
    return flagRendicontazioneDocSpesa;
  }

  public void setFlagRendicontazioneDocSpesa(String flagRendicontazioneDocSpesa)
  {
    this.flagRendicontazioneDocSpesa = flagRendicontazioneDocSpesa;
  }

  public String getCodiceTipoBando()
  {
    return codiceTipoBando;
  }

  public void setCodiceTipoBando(String codiceTipoBando)
  {
    this.codiceTipoBando = codiceTipoBando;
  }

	public int getIdProcedimentoAgricolo()
	{
		return idProcedimentoAgricolo;
	}
	
	public void setIdProcedimentoAgricolo(int idProcedimentoAgricolo)
	{
		this.idProcedimentoAgricolo = idProcedimentoAgricolo;
	}
  
  

}
