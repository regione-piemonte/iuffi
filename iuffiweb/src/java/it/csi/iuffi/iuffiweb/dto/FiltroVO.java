package it.csi.iuffi.iuffiweb.dto;

import java.util.HashMap;
import java.util.List;

public class FiltroVO extends HashMap<String, HashMap<String, Boolean>>
{

  private static final long serialVersionUID = -8411030316408596793L;

  private String            ugualeA          = null;
  private String            diversoDa        = null;
  private String            contiene         = null;
  private String            nonContiene      = null;
  private String            maggioreDi       = null;
  private String            minoreDi         = null;
  private boolean           vuoto            = false;
  private boolean           nonVuoto         = false;

  private List<Long>        values;
  private List<String>      strValues;

  public String getUgualeA()
  {
    return ugualeA;
  }

  public void setUgualeA(String ugualeA)
  {
    this.ugualeA = ugualeA;
  }

  public String getDiversoDa()
  {
    return diversoDa;
  }

  public void setDiversoDa(String diversoDa)
  {
    this.diversoDa = diversoDa;
  }

  public String getContiene()
  {
    return contiene;
  }

  public void setContiene(String contiene)
  {
    this.contiene = contiene;
  }

  public String getNonContiene()
  {
    return nonContiene;
  }

  public void setNonContiene(String nonContiene)
  {
    this.nonContiene = nonContiene;
  }

  public boolean isVuoto()
  {
    return vuoto;
  }

  public void setVuoto(boolean vuoto)
  {
    this.vuoto = vuoto;
  }

  public boolean isNonVuoto()
  {
    return nonVuoto;
  }

  public void setNonVuoto(boolean nonVuoto)
  {
    this.nonVuoto = nonVuoto;
  }

  public List<Long> getValues()
  {
    return values;
  }

  public void setValues(List<Long> values)
  {
    this.values = values;
  }

  public List<String> getStrValues()
  {
    return strValues;
  }

  public void setStrValues(List<String> strValues)
  {
    this.strValues = strValues;
  }

  public String getMaggioreDi()
  {
    return maggioreDi;
  }

  public void setMaggioreDi(String maggioreDi)
  {
    this.maggioreDi = maggioreDi;
  }

  public String getMinoreDi()
  {
    return minoreDi;
  }

  public void setMinoreDi(String minoreDi)
  {
    this.minoreDi = minoreDi;
  }

}
