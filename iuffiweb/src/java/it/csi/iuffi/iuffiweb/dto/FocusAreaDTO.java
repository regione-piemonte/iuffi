package it.csi.iuffi.iuffiweb.dto;

import java.io.Serializable;

public class FocusAreaDTO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;
  private long              idLivello;
  private long              idFocusArea;
  private String            tipo;
  private String            codice;
  private String            descrizione;
  private boolean           isPrimaria       = false;
  private boolean           isSecondaria     = false;

  public FocusAreaDTO()
  {

  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public long getIdFocusArea()
  {
    return idFocusArea;
  }

  public void setIdFocusArea(long idFocusArea)
  {
    this.idFocusArea = idFocusArea;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getTipo()
  {
    return tipo;
  }

  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }

  public boolean isPrimaria()
  {
    return isPrimaria;
  }

  public void setPrimaria(boolean isPrimaria)
  {
    this.isPrimaria = isPrimaria;
  }

  public boolean isSecondaria()
  {
    return isSecondaria;
  }

  public void setSecondaria(boolean isSecondaria)
  {
    this.isSecondaria = isSecondaria;
  }

}
