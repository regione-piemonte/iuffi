package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.conticorrenti;

public class ContoCorrenteEstesoDTO extends ContoCorrenteDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -1700583242981106473L;
  private String            iban;
  private String            cin;
  private String            abi;
  private String            cab;
  private String            cifraControllo;
  private String            numeroContoCorrente;

  public String getIban()
  {
    return iban;
  }

  public void setIban(String iban)
  {
    this.iban = iban;
  }

  public String getCin()
  {
    return cin;
  }

  public void setCin(String cin)
  {
    this.cin = cin;
  }

  public String getAbi()
  {
    return abi;
  }

  public void setAbi(String abi)
  {
    this.abi = abi;
  }

  public String getCab()
  {
    return cab;
  }

  public void setCab(String cab)
  {
    this.cab = cab;
  }

  public String getCifraControllo()
  {
    return cifraControllo;
  }

  public void setCifraControllo(String cifraControllo)
  {
    this.cifraControllo = cifraControllo;
  }

  public String getNumeroContoCorrente()
  {
    return numeroContoCorrente;
  }

  public void setNumeroContoCorrente(String numeroContoCorrente)
  {
    this.numeroContoCorrente = numeroContoCorrente;
  }

}
