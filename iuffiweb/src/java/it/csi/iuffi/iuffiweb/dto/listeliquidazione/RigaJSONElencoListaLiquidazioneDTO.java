package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RigaJSONElencoListaLiquidazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID = -2520284066170141950L;
  protected long             idListaLiquidazione;
  protected long             idBando;
  protected long             numeroLista;
  protected String           note;
  protected String           denominazioneBando;
  protected String           annoCampagnaBando;
  protected String           numeroProtocollo;
  protected Date             dataCreazione;
  protected Date             dataProtocollo;
  protected String           flagStatoLista;
  protected String           descStatoLista;
  protected int              numPagamenti;
  protected BigDecimal       importo;
  protected String           descTipoImporto;
  protected Date             dataApprovazione;
  protected String           nomeTecnicoLiquidatore;
  protected String           cognomeTecnicoLiquidatore;
  protected String           codFiscTecnicoLiquidatore;
  protected String           flagInvioSigop;
  protected Date             dataInvioSigop;
  protected int              extIdAmmCompetenza;
  protected String           organismoDelegato;
  protected String           descOrganismoDelegato;
  protected String           descUtenteAggiornamento;
  protected int              idStatoStampa;
  protected long             extIdTecnicoLiquidatore;
  protected long             idIdTipoImporto;
  protected List<LivelloDTO> livelli;

  public List<LivelloDTO> getLivelli()
  {
    return livelli;
  }

  public void setLivelli(List<LivelloDTO> livelli)
  {
    this.livelli = livelli;
  }

  public long getNumeroLista()
  {
    return numeroLista;
  }

  public void setNumeroLista(long numeroLista)
  {
    this.numeroLista = numeroLista;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getDenominazioneBando()
  {
    return denominazioneBando;
  }

  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }

  public String getAnnoCampagnaBando()
  {
    return annoCampagnaBando;
  }

  public void setAnnoCampagnaBando(String annoCampagnaBando)
  {
    this.annoCampagnaBando = annoCampagnaBando;
  }

  public String getDataCreazione()
  {
    return IuffiUtils.DATE.formatDateTime(dataCreazione);
  }

  public String getDataCreazioneDate()
  {
    return IuffiUtils.DATE.formatDate(dataCreazione);
  }

  @JsonIgnore
  public int getAnnoDataCreazione()
  {
    return IuffiUtils.DATE.getYearFromDate(dataCreazione);
  }

  public void setDataCreazione(Date dataCreazione)
  {
    this.dataCreazione = dataCreazione;
  }

  public String getFlagStatoLista()
  {
    return flagStatoLista;
  }

  public void setFlagStatoLista(String flagStatoLista)
  {
    this.flagStatoLista = flagStatoLista;
  }

  public String getDescStatoLista()
  {
    return descStatoLista;
  }

  public void setDescStatoLista(String descStatoLista)
  {
    this.descStatoLista = descStatoLista;
  }

  public int getNumPagamenti()
  {
    return numPagamenti;
  }

  public void setNumPagamenti(int numPagamenti)
  {
    this.numPagamenti = numPagamenti;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public String getDescTipoImporto()
  {
    return descTipoImporto;
  }

  public void setDescTipoImporto(String descTipoImporto)
  {
    this.descTipoImporto = descTipoImporto;
  }

  public String getDataApprovazione()
  {
    return IuffiUtils.DATE.formatDateTime(dataApprovazione);
  }

  public void setDataApprovazione(Date dataApprovazione)
  {
    this.dataApprovazione = dataApprovazione;
  }

  public String getFlagInvioSigop()
  {
    return flagInvioSigop;
  }

  public void setFlagInvioSigop(String flagInvioSigop)
  {
    this.flagInvioSigop = flagInvioSigop;
  }

  public String getOrganismoDelegato()
  {
    return organismoDelegato;
  }

  public void setOrganismoDelegato(String organismoDelegato)
  {
    this.organismoDelegato = organismoDelegato;
  }

  public String getDescOrganismoDelegato()
  {
    return descOrganismoDelegato;
  }

  public void setDescOrganismoDelegato(String descOrganismoDelegato)
  {
    this.descOrganismoDelegato = descOrganismoDelegato;
  }

  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }

  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }

  public String getDataInvioSigop()
  {
    return IuffiUtils.DATE.formatDateTime(dataInvioSigop);
  }

  public void setDataInvioSigop(Date dataInvioSigop)
  {
    this.dataInvioSigop = dataInvioSigop;
  }

  public long getIdListaLiquidazione()
  {
    return idListaLiquidazione;
  }

  public void setIdListaLiquidazione(long idListaLiquidazione)
  {
    this.idListaLiquidazione = idListaLiquidazione;
  }

  public int getExtIdAmmCompetenza()
  {
    return extIdAmmCompetenza;
  }

  public void setExtIdAmmCompetenza(int extIdAmmCompetenza)
  {
    this.extIdAmmCompetenza = extIdAmmCompetenza;
  }

  public int getIdStatoStampa()
  {
    return idStatoStampa;
  }

  public void setIdStatoStampa(int idStatoStampa)
  {
    this.idStatoStampa = idStatoStampa;
  }

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  @JsonIgnore
  public String getNomeTecnicoLiquidatore()
  {
    return nomeTecnicoLiquidatore;
  }

  @JsonIgnore
  public void setNomeTecnicoLiquidatore(String nomeTecnicoLiquidatore)
  {
    this.nomeTecnicoLiquidatore = nomeTecnicoLiquidatore;
  }

  @JsonIgnore
  public String getCognomeTecnicoLiquidatore()
  {
    return cognomeTecnicoLiquidatore;
  }

  @JsonIgnore
  public void setCognomeTecnicoLiquidatore(String cognomeTecnicoLiquidatore)
  {
    this.cognomeTecnicoLiquidatore = cognomeTecnicoLiquidatore;
  }

  @JsonIgnore
  public String getCodFiscTecnicoLiquidatore()
  {
    return codFiscTecnicoLiquidatore;
  }

  @JsonIgnore
  public void setCodFiscTecnicoLiquidatore(String codFiscTecnicoLiquidatore)
  {
    this.codFiscTecnicoLiquidatore = codFiscTecnicoLiquidatore;
  }

  public String getDescTecnicoLiquidatore()
  {
    return cognomeTecnicoLiquidatore + " " + nomeTecnicoLiquidatore;
  }

  @JsonIgnore
  public long getExtIdTecnicoLiquidatore()
  {
    return extIdTecnicoLiquidatore;
  }

  @JsonIgnore
  public void setExtIdTecnicoLiquidatore(long extIdTecnicoLiquidatore)
  {
    this.extIdTecnicoLiquidatore = extIdTecnicoLiquidatore;
  }

  @JsonIgnore
  public long getIdIdTipoImporto()
  {
    return idIdTipoImporto;
  }

  @JsonIgnore
  public void setIdIdTipoImporto(long idIdTipoImporto)
  {
    this.idIdTipoImporto = idIdTipoImporto;
  }

  public String getNumListaStr()
  {
    return String.valueOf(this.numeroLista);
  }

  public String getNumeroProtocollo()
  {
    return numeroProtocollo;
  }

  public void setNumeroProtocollo(String numeroProtocollo)
  {
    this.numeroProtocollo = numeroProtocollo;
  }

  public String getDataProtocollo()
  {
    return IuffiUtils.DATE.formatDate(dataProtocollo);
  }

  public void setDataProtocollo(Date dataProtocollo)
  {
    this.dataProtocollo = dataProtocollo;
  }

  public String getElencoCodiciLivelliMisure()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceMisura() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliMisureHtml()
  {
    // return elencoCodiciLivelliHtml;
    String htmlElenco = "";
    List<String> lCodici = new ArrayList<String>();
    List<LivelloDTO> liv = this.getLivelli();
    int i = 0;
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        if (!lCodici.contains(l.getCodiceLivello()))
        {
          htmlElenco = htmlElenco + l.getCodiceLivello();
          lCodici.add(l.getCodiceLivello());
          if (i < livelli.size() - 1)
          {
            htmlElenco = htmlElenco + "<br>";
          }
        }

        i++;
      }

    return htmlElenco;
  }

  public String getElencoMisureHtml()
  {
    // return elencoCodiciLivelliHtml;
    String htmlElenco = "";
    List<String> lCodici = new ArrayList<String>();
    List<LivelloDTO> liv = this.getLivelli();
    int i = 0;
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        if (!lCodici.contains(l.getCodiceMisura()))
        {
          htmlElenco = htmlElenco + l.getCodiceMisura();
          lCodici.add(l.getCodiceMisura());
          if (i < livelli.size() - 1)
          {
            htmlElenco = htmlElenco + "<br>";
          }
        }

        i++;
      }

    return htmlElenco;
  }

  public String getElencoCodiciLivelli()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceLivello() + "&&&";

      }
    return s;
  }

}
