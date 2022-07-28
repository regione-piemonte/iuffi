package it.csi.iuffi.iuffiweb.model.api;

import java.io.Serializable;

public class Ubicazione implements Serializable
{
  private String nome;
  private String cognome;
  private String indirizzo;
  private String telefono;
  private String email;
  private String numero;

  public Ubicazione()
  {
    super();
  }
  
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getCognome()
  {
    return cognome;
  }
  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getTelefono()
  {
    return telefono;
  }
  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getNumero()
  {
    return numero;
  }
  public void setNumero(String numero)
  {
    this.numero = numero;
  }
}
