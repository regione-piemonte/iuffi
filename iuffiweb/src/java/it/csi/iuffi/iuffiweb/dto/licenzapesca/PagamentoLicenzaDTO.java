package it.csi.iuffi.iuffiweb.dto.licenzapesca;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PagamentoLicenzaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private String cf;
  private String ruolo;
  private String liv;
  private String idProcedimento_accesso;
  private String idProcedimento;
  private String idFruitore;
  private String codicePagamento;
  private String codiceTracciatura;
  private String importo;
  private String pagatore_idAnagraficaAzienda;
  private String pagatore_nome;
  private String pagatore_cognome;
  private String pagatore_codiceFiscale;
  private String pagatore_ragioneSociale;
  private String pagatore_idUnicoPagatore;
  private String pagatore_piva;
  private String pagatore_email;
  private String pagatore_pec;
  private String versante_nome;
  private String versante_cognome;
  private String versante_codiceFiscale;
  private String versante_ragioneSociale;
  private String versante_idUnicoPagatore;
  private String versante_piva;
  private String versante_email;
  private String versante_pec;
  private String tipoPagamento;
  private String iuv;
  
  //Getters&Setters
  public String getCf()  {
    return cf;
  }
  public void setCf(String cf)  {
    this.cf = cf;
  }
  public String getRuolo()  {
    return ruolo;
  }
  public void setRuolo(String ruolo)  {
    this.ruolo = ruolo;
  }
  public String getLiv()  {
    return liv;
  }
  public void setLiv(String liv)  {
    this.liv = liv;
  }
  public String getIdProcedimento_accesso()  {
    return idProcedimento_accesso;
  }
  public void setIdProcedimento_accesso(String idProcedimento_accesso)  {
    this.idProcedimento_accesso = idProcedimento_accesso;
  }
  public String getIdProcedimento() {
    return idProcedimento;
  }
  public void setIdProcedimento(String idProcedimento)  {
    this.idProcedimento = idProcedimento;
  }
  public String getIdFruitore()  {
    return idFruitore;
  }
  public void setIdFruitore(String idFruitore)  {
    this.idFruitore = idFruitore;
  }
  public String getCodicePagamento()  {
    return codicePagamento;
  }
  public void setCodicePagamento(String codicePagamento)  {
    this.codicePagamento = codicePagamento;
  }
  public String getCodiceTracciatura()  {
    return codiceTracciatura;
  }
  public void setCodiceTracciatura(String codiceTracciatura)  {
    this.codiceTracciatura = codiceTracciatura;
  }
  public String getImporto()  {
    return importo;
  }
  public void setImporto(String importo)  {
    this.importo = importo;
  }
  public String getPagatore_idAnagraficaAzienda() {
    return pagatore_idAnagraficaAzienda;
  }
  public void setPagatore_idAnagraficaAzienda(String pagatore_idAnagraficaAzienda)  {
    this.pagatore_idAnagraficaAzienda = pagatore_idAnagraficaAzienda;
  }
  public String getPagatore_nome()  {
    return pagatore_nome;
  }
  public void setPagatore_nome(String pagatore_nome)  {
    this.pagatore_nome = pagatore_nome;
  }
  public String getPagatore_cognome()  {
    return pagatore_cognome;
  }
  public void setPagatore_cognome(String pagatore_cognome)  {
    this.pagatore_cognome = pagatore_cognome;
  }
  public String getPagatore_codiceFiscale() {
    return pagatore_codiceFiscale;
  }
  public void setPagatore_codiceFiscale(String pagatore_codiceFiscale)  {
    this.pagatore_codiceFiscale = pagatore_codiceFiscale;
  }
  public String getPagatore_ragioneSociale() {
    return pagatore_ragioneSociale;
  }
  public void setPagatore_ragioneSociale(String pagatore_ragioneSociale)  {
    this.pagatore_ragioneSociale = pagatore_ragioneSociale;
  }
  public String getPagatore_idUnicoPagatore()  {
    return pagatore_idUnicoPagatore;
  }
  public void setPagatore_idUnicoPagatore(String pagatore_idUnicoPagatore)  {
    this.pagatore_idUnicoPagatore = pagatore_idUnicoPagatore;
  }
  public String getPagatore_piva()  {
    return pagatore_piva;
  }
  public void setPagatore_piva(String pagatore_piva)  {
    this.pagatore_piva = pagatore_piva;
  }
  public String getPagatore_email()  {
    return pagatore_email;
  }
  public void setPagatore_email(String pagatore_email)  {
    this.pagatore_email = pagatore_email;
  }
  public String getPagatore_pec()  {
    return pagatore_pec;
  }
  public void setPagatore_pec(String pagatore_pec)  {
    this.pagatore_pec = pagatore_pec;
  }
  public String getVersante_nome()  {
    return versante_nome;
  }
  public void setVersante_nome(String versante_nome)  {
    this.versante_nome = versante_nome;
  }
  public String getVersante_cognome()  {
    return versante_cognome;
  }
  public void setVersante_cognome(String versante_cognome)  {
    this.versante_cognome = versante_cognome;
  }
  public String getVersante_codiceFiscale()  {
    return versante_codiceFiscale;
  }
  public void setVersante_codiceFiscale(String versante_codiceFiscale)  {
    this.versante_codiceFiscale = versante_codiceFiscale;
  }
  public String getVersante_ragioneSociale()  {
    return versante_ragioneSociale;
  }
  public void setVersante_ragioneSociale(String versante_ragioneSociale)  {
    this.versante_ragioneSociale = versante_ragioneSociale;
  }
  public String getVersante_idUnicoPagatore()  {
    return versante_idUnicoPagatore;
  }
  public void setVersante_idUnicoPagatore(String versante_idUnicoPagatore)  {
    this.versante_idUnicoPagatore = versante_idUnicoPagatore;
  }
  public String getVersante_piva()  {
    return versante_piva;
  }
  public void setVersante_piva(String versante_piva)  {
    this.versante_piva = versante_piva;
  }
  public String getVersante_email()  {
    return versante_email;
  }
  public void setVersante_email(String versante_email)  {
    this.versante_email = versante_email;
  }
  public String getVersante_pec()  {
    return versante_pec;
  }
  public void setVersante_pec(String versante_pec)  {
    this.versante_pec = versante_pec;
  }
  public String getTipoPagamento()  {
    return tipoPagamento;
  }
  public void setTipoPagamento(String tipoPagamento)  {
    this.tipoPagamento = tipoPagamento;
  }
  public String getIuv()  {
    return iuv;
  }
  public void setIuv(String iuv)  {
    this.iuv = iuv;
  }
    
}