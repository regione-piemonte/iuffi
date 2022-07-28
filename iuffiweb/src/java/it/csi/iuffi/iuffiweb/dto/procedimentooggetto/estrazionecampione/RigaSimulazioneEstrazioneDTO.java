package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;

public class RigaSimulazioneEstrazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idProcedimentoOggetto;
  private long              idStatoEstrazione;
  private long              idTipoEstrazione;
  private String            descrEnteDelegato;
  private String            identificativo;
  private String            descrTipoDomanda;
  private String            descrStato;
  private String            cuaAzie;
  private String            denominazioneAzie;
  private List<LivelloDTO>  listLivelli;

  private BigDecimal        importoRichiesto;
  private BigDecimal        punteggio;
  private String            classe;
  private String            flagEstratta;
  private String            flagEstrattaDescr;

  private String            descrTipoEstrazione;
  private String            descrStatoEstrazione;

  public String getListLivelliHtml()
  {
    if (listLivelli == null)
    {
      return "";
    }
    String liv = "";
    for (LivelloDTO i : listLivelli)
    {
      liv = liv + " " + i.getCodice() + "<br>";
    }
    return liv;
  }

  public String getListLivelliText()
  {
    if (listLivelli == null)
    {
      return "";
    }
    String liv = "";
    for (LivelloDTO i : listLivelli)
    {
      liv = liv + " " + i.getCodice() + "\n";
    }
    return liv;
  }

  public List<LivelloDTO> getListLivelli()
  {
    return listLivelli;
  }

  public void setListLivelli(List<LivelloDTO> listLivelli)
  {
    this.listLivelli = listLivelli;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public String getDescrEnteDelegato()
  {
    return descrEnteDelegato;
  }

  public void setDescrEnteDelegato(String descrEnteDelegato)
  {
    this.descrEnteDelegato = descrEnteDelegato;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public String getDescrTipoDomanda()
  {
    return descrTipoDomanda;
  }

  public void setDescrTipoDomanda(String descrTipoDomanda)
  {
    this.descrTipoDomanda = descrTipoDomanda;
  }

  public String getDescrStato()
  {
    return descrStato;
  }

  public void setDescrStato(String descrStato)
  {
    this.descrStato = descrStato;
  }

  public String getCuaAzie()
  {
    return cuaAzie;
  }

  public void setCuaAzie(String cuaAzie)
  {
    this.cuaAzie = cuaAzie;
  }

  public String getDenominazioneAzie()
  {
    return denominazioneAzie;
  }

  public void setDenominazioneAzie(String denominazioneAzie)
  {
    this.denominazioneAzie = denominazioneAzie;
  }

  public BigDecimal getImportoRichiesto()
  {
    return importoRichiesto;
  }

  public void setImportoRichiesto(BigDecimal importoRichiesto)
  {
    this.importoRichiesto = importoRichiesto;
  }

  public BigDecimal getPunteggio()
  {
    return punteggio;
  }

  public void setPunteggio(BigDecimal punteggio)
  {
    this.punteggio = punteggio;
  }

  public String getClasse()
  {
    return classe;
  }

  public void setClasse(String classe)
  {
    this.classe = classe;
  }

  public String getFlagEstratta()
  {
    return flagEstratta;
  }

  public void setFlagEstratta(String flagEstratta)
  {
    this.flagEstratta = flagEstratta;
  }

  public String getFlagEstrattaDescr()
  {
    return flagEstrattaDescr;
  }

  public void setFlagEstrattaDescr(String flagEstrattaDescr)
  {
    this.flagEstrattaDescr = flagEstrattaDescr;
  }

  public long getIdStatoEstrazione()
  {
    return idStatoEstrazione;
  }

  public void setIdStatoEstrazione(long idStatoEstrazione)
  {
    this.idStatoEstrazione = idStatoEstrazione;
  }

  public long getIdTipoEstrazione()
  {
    return idTipoEstrazione;
  }

  public void setIdTipoEstrazione(long idTipoEstrazione)
  {
    this.idTipoEstrazione = idTipoEstrazione;
  }

  public String getDescrTipoEstrazione()
  {
    return descrTipoEstrazione;
  }

  public void setDescrTipoEstrazione(String descrTipoEstrazione)
  {
    this.descrTipoEstrazione = descrTipoEstrazione;
  }

  public String getDescrStatoEstrazione()
  {
    return descrStatoEstrazione;
  }

  public void setDescrStatoEstrazione(String descrStatoEstrazione)
  {
    this.descrStatoEstrazione = descrStatoEstrazione;
  }

  public Long getIdFlagEstratta()
  {
    if (flagEstratta != null)
    {
      if (flagEstratta.compareTo("S") == 0)
        return Long.valueOf(0);

      if (flagEstratta.compareTo("D") == 0)
        return Long.valueOf(1);

      return Long.valueOf(-1);
    }
    return null;
  }

}
