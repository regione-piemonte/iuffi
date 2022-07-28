package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.integration.RigaAnticipoLivello;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DatiAnticipo extends DatiAnticipoModificaDTO
{
  /** serialVersionUID */
  private static final long           serialVersionUID = -6463376304597658909L;
  protected String                    abi;
  protected String                    denominazioneBanca;
  protected String                    cab;
  protected String                    denominazioneSportello;
  protected String                    indirizzoSportello;
  protected String                    capSportello;
  protected String                    descrizioneComuneSportello;
  protected String                    siglaProvinciaSportello;
  protected String                    capAltroIstituto;
  protected String                    descComuneAltroIstituto;
  protected String                    siglaProvinciaAltroIstituto;
  protected int                       idStatoOggetto;
  protected long                      idProcedimento;
  protected List<RigaAnticipoLivello> ripartizioneAnticipo;

  public String getAbi()
  {
    return abi;
  }

  public void setAbi(String abi)
  {
    this.abi = abi;
  }

  public String getDenominazioneBanca()
  {
    return denominazioneBanca;
  }

  public void setDenominazioneBanca(String denominazioneBanca)
  {
    this.denominazioneBanca = denominazioneBanca;
  }

  public String getCab()
  {
    return cab;
  }

  public void setCab(String cab)
  {
    this.cab = cab;
  }

  public String getDenominazioneSportello()
  {
    return denominazioneSportello;
  }

  public void setDenominazioneSportello(String denominazioneSportello)
  {
    this.denominazioneSportello = denominazioneSportello;
  }

  public String getIndirizzoSportello()
  {
    return indirizzoSportello;
  }

  public void setIndirizzoSportello(String indirizzoSportello)
  {
    this.indirizzoSportello = indirizzoSportello;
  }

  public String getCapSportello()
  {
    return capSportello;
  }

  public void setCapSportello(String capSportello)
  {
    this.capSportello = capSportello;
  }

  public String getDescrizioneComuneSportello()
  {
    return descrizioneComuneSportello;
  }

  public void setDescrizioneComuneSportello(String descrizioneComuneSportello)
  {
    this.descrizioneComuneSportello = descrizioneComuneSportello;
  }

  public String getSiglaProvinciaSportello()
  {
    return siglaProvinciaSportello;
  }

  public void setSiglaProvinciaSportello(String siglaProvinciaSportello)
  {
    this.siglaProvinciaSportello = siglaProvinciaSportello;
  }

  public String getCapAltroIstituto()
  {
    return capAltroIstituto;
  }

  public void setCapAltroIstituto(String capAltroIstituto)
  {
    this.capAltroIstituto = capAltroIstituto;
  }

  public String getDescComuneAltroIstituto()
  {
    return descComuneAltroIstituto;
  }

  public String getDescCompletaComuneAltroIstituto()
  {
    return extIstatComune == null ? null
        : capAltroIstituto + " - " + descComuneAltroIstituto + " ("
            + siglaProvinciaAltroIstituto + ")";
  }

  public String getDescCompletaComuneSportello()
  {
    return extIdSportello == null ? null
        : capSportello + " - " + descrizioneComuneSportello + " ("
            + siglaProvinciaSportello + ")";
  }

  public void setDescComuneAltroIstituto(String descComuneAltroIstituto)
  {
    this.descComuneAltroIstituto = descComuneAltroIstituto;
  }

  public String getSiglaProvinciaAltroIstituto()
  {
    return siglaProvinciaAltroIstituto;
  }

  public void setSiglaProvinciaAltroIstituto(String siglaProvinciaAltroIstituto)
  {
    this.siglaProvinciaAltroIstituto = siglaProvinciaAltroIstituto;
  }

  public int getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(int idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public void addRigaRiepilogo(String operazione,
      BigDecimal importoInvestimento,
      BigDecimal importoAmmesso,
      BigDecimal importoContributo,
      BigDecimal importoAnticipo,
      String flagAnticipo)
  {
    if (ripartizioneAnticipo == null)
    {
      ripartizioneAnticipo = new ArrayList<>();
    }
    RigaAnticipoLivello r = new RigaAnticipoLivello();
    r.setImportoAmmesso(importoAmmesso);
    r.setImportoAnticipo(importoAnticipo);
    r.setImportoContributo(importoContributo);
    r.setImportoInvestimento(importoInvestimento);
    r.setCodiceLivello(operazione);
    r.setFlagAnticipo(flagAnticipo);
    ripartizioneAnticipo.add(r);
  }

  public BigDecimal getImportoContributoAnticipabile()
  {
    BigDecimal importoContributoAnticipabile = BigDecimal.ZERO;
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello r : ripartizioneAnticipo)
      {
        if (IuffiConstants.FLAGS.SI.equals(r.getFlagAnticipo()))
        {
          importoContributoAnticipabile = IuffiUtils.NUMBERS
              .add(importoContributoAnticipabile, r.getImportoContributo());
        }
      }
    }
    return importoContributoAnticipabile;
  }

  public BigDecimal getTotaleImportoInvestimento()
  {
    BigDecimal totale = BigDecimal.ZERO;
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello r : ripartizioneAnticipo)
      {
        totale = IuffiUtils.NUMBERS.add(totale, r.getImportoInvestimento());
      }
    }
    return totale;
  }

  public BigDecimal getTotaleImportoAmmesso()
  {
    BigDecimal totale = BigDecimal.ZERO;
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello r : ripartizioneAnticipo)
      {
        totale = IuffiUtils.NUMBERS.add(totale, r.getImportoAmmesso());
      }
    }
    return totale;
  }

  public BigDecimal getTotaleContributoConcesso()
  {
    BigDecimal totale = BigDecimal.ZERO;
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello r : ripartizioneAnticipo)
      {
        totale = IuffiUtils.NUMBERS.add(totale, r.getImportoContributo());
      }
    }
    return totale;
  }

  public BigDecimal getTotaleImportoAnticipo()
  {
    BigDecimal totale = BigDecimal.ZERO;
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello r : ripartizioneAnticipo)
      {
        totale = IuffiUtils.NUMBERS.add(totale, r.getImportoAnticipo());
      }
    }
    return totale;
  }

  public List<RigaAnticipoLivello> getRipartizioneAnticipo()
  {
    return ripartizioneAnticipo;
  }

  public void setRipartizioneAnticipo(
      List<RigaAnticipoLivello> ripartizioneAnticipo)
  {
    this.ripartizioneAnticipo = ripartizioneAnticipo;
  }

}
