package it.csi.iuffi.iuffiweb.model;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RiepilogoMonitoraggioDTO implements ILoggable
{

  private static final long serialVersionUID = 1L;
  
  private Integer idSpecieVegetale;
  private String nomeComuneSpecie;
  private String nomeLatinoSpecie;
  private String codiceEppo;
  private Integer idTipoCampione;
  private String descTipoCampione;
  private int gennaio;
  private int febbraio;
  private int marzo;
  private int aprile;
  private int maggio;
  private int giugno;
  private int luglio;
  private int agosto;
  private int settembre;
  private int ottobre;
  private int novembre;
  private int dicembre;
  
  private String organismiNociviStr;
  private List<OrganismoNocivoDTO> organismiNocivi;
  
  private String mesiStr;
  private Integer idOrganismoNocivo;
  private Integer oldIdTipoCampione;
  private Integer oldIdSpecieVegetale;
  private List<OrganismoNocivoDTO> oldOrganismiNocivi;
  private int oldGennaio;
  private int oldFebbraio;
  private int oldMarzo;
  private int oldAprile;
  private int oldMaggio;
  private int oldGiugno;
  private int oldLuglio;
  private int oldAgosto;
  private int oldSettembre;
  private int oldOttobre;
  private int oldNovembre;
  private int oldDicembre;
  
  
  public RiepilogoMonitoraggioDTO()
  {
    super();
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }

  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public String getNomeComuneSpecie()
  {
    return nomeComuneSpecie;
  }

  public void setNomeComuneSpecie(String nomeComuneSpecie)
  {
    this.nomeComuneSpecie = nomeComuneSpecie;
  }

  public String getNomeLatinoSpecie()
  {
    return nomeLatinoSpecie;
  }

  public void setNomeLatinoSpecie(String nomeLatinoSpecie)
  {
    this.nomeLatinoSpecie = nomeLatinoSpecie;
  }

  public Integer getIdTipoCampione()
  {
    return idTipoCampione;
  }

  public void setIdTipoCampione(Integer idTipoCampione)
  {
    this.idTipoCampione = idTipoCampione;
  }

  public String getDescTipoCampione()
  {
    return descTipoCampione;
  }

  public void setDescTipoCampione(String descTipoCampione)
  {
    this.descTipoCampione = descTipoCampione;
  }

  public int getGennaio()
  {
    return gennaio;
  }

  public void setGennaio(int gennaio)
  {
    this.gennaio = gennaio;
  }

  public int getFebbraio()
  {
    return febbraio;
  }

  public void setFebbraio(int febbraio)
  {
    this.febbraio = febbraio;
  }

  public int getMarzo()
  {
    return marzo;
  }

  public void setMarzo(int marzo)
  {
    this.marzo = marzo;
  }

  public int getAprile()
  {
    return aprile;
  }

  public void setAprile(int aprile)
  {
    this.aprile = aprile;
  }

  public int getMaggio()
  {
    return maggio;
  }

  public void setMaggio(int maggio)
  {
    this.maggio = maggio;
  }

  public int getGiugno()
  {
    return giugno;
  }

  public void setGiugno(int giugno)
  {
    this.giugno = giugno;
  }

  public int getLuglio()
  {
    return luglio;
  }

  public void setLuglio(int luglio)
  {
    this.luglio = luglio;
  }

  public int getAgosto()
  {
    return agosto;
  }

  public void setAgosto(int agosto)
  {
    this.agosto = agosto;
  }

  public int getSettembre()
  {
    return settembre;
  }

  public void setSettembre(int settembre)
  {
    this.settembre = settembre;
  }

  public int getOttobre()
  {
    return ottobre;
  }

  public void setOttobre(int ottobre)
  {
    this.ottobre = ottobre;
  }

  public int getNovembre()
  {
    return novembre;
  }

  public void setNovembre(int novembre)
  {
    this.novembre = novembre;
  }

  public int getDicembre()
  {
    return dicembre;
  }

  public void setDicembre(int dicembre)
  {
    this.dicembre = dicembre;
  }

  public List<OrganismoNocivoDTO> getOrganismiNocivi()
  {
    return organismiNocivi;
  }

  public void setOrganismiNocivi(List<OrganismoNocivoDTO> organismiNocivi)
  {
    this.organismiNocivi = organismiNocivi;
  }

  public String getOrganismiNociviStr()
  {
    return organismiNociviStr;
  }

  public void setOrganismiNociviStr(String organismiNociviStr)
  {
    this.organismiNociviStr = organismiNociviStr;
  }

  public String getMesiStr()
  {
    return mesiStr;
  }

  public void setMesiStr(String mesiStr)
  {
    this.mesiStr = mesiStr;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public Integer getOldIdTipoCampione()
  {
    return oldIdTipoCampione;
  }

  public void setOldIdTipoCampione(Integer oldIdTipoCampione)
  {
    this.oldIdTipoCampione = oldIdTipoCampione;
  }

  public Integer getOldIdSpecieVegetale()
  {
    return oldIdSpecieVegetale;
  }

  public void setOldIdSpecieVegetale(Integer oldIdSpecieVegetale)
  {
    this.oldIdSpecieVegetale = oldIdSpecieVegetale;
  }

  public List<OrganismoNocivoDTO> getOldOrganismiNocivi()
  {
    return oldOrganismiNocivi;
  }

  public void setOldOrganismiNocivi(List<OrganismoNocivoDTO> oldOrganismiNocivi)
  {
    this.oldOrganismiNocivi = oldOrganismiNocivi;
  }

  public int getOldGennaio()
  {
    return oldGennaio;
  }

  public void setOldGennaio(int oldGennaio)
  {
    this.oldGennaio = oldGennaio;
  }

  public int getOldFebbraio()
  {
    return oldFebbraio;
  }

  public void setOldFebbraio(int oldFebbraio)
  {
    this.oldFebbraio = oldFebbraio;
  }

  public int getOldMarzo()
  {
    return oldMarzo;
  }

  public void setOldMarzo(int oldMarzo)
  {
    this.oldMarzo = oldMarzo;
  }

  public int getOldAprile()
  {
    return oldAprile;
  }

  public void setOldAprile(int oldAprile)
  {
    this.oldAprile = oldAprile;
  }

  public int getOldMaggio()
  {
    return oldMaggio;
  }

  public void setOldMaggio(int oldMaggio)
  {
    this.oldMaggio = oldMaggio;
  }

  public int getOldGiugno()
  {
    return oldGiugno;
  }

  public void setOldGiugno(int oldGiugno)
  {
    this.oldGiugno = oldGiugno;
  }

  public int getOldLuglio()
  {
    return oldLuglio;
  }

  public void setOldLuglio(int oldLuglio)
  {
    this.oldLuglio = oldLuglio;
  }

  public int getOldAgosto()
  {
    return oldAgosto;
  }

  public void setOldAgosto(int oldAgosto)
  {
    this.oldAgosto = oldAgosto;
  }

  public int getOldSettembre()
  {
    return oldSettembre;
  }

  public void setOldSettembre(int oldSettembre)
  {
    this.oldSettembre = oldSettembre;
  }

  public int getOldOttobre()
  {
    return oldOttobre;
  }

  public void setOldOttobre(int oldOttobre)
  {
    this.oldOttobre = oldOttobre;
  }

  public int getOldNovembre()
  {
    return oldNovembre;
  }

  public void setOldNovembre(int oldNovembre)
  {
    this.oldNovembre = oldNovembre;
  }

  public int getOldDicembre()
  {
    return oldDicembre;
  }

  public void setOldDicembre(int oldDicembre)
  {
    this.oldDicembre = oldDicembre;
  }

  public String getCodiceEppo()
  {
    return codiceEppo;
  }

  public void setCodiceEppo(String codiceEppo)
  {
    this.codiceEppo = codiceEppo;
  }

}
