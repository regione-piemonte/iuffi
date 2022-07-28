package it.csi.iuffi.iuffiweb.presentation.login;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class ModelLogin
{

  public ModelLogin()
  {
  }

  @NotNull
  public String ruolo;

  public String getRuolo()
  {
    return ruolo;
  }

  public void setRuolo(String ruolo)
  {
    this.ruolo = ruolo;
  }
}