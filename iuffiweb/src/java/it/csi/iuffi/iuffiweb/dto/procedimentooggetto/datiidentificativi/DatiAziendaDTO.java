package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi;

import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DatiAziendaDTO extends AziendaDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -7866141986745760614L;
  private String            itestazionePartitaIva;
  private String            formaGiuridica;
  private String            telefono;
  private String            fax;
  private String            email;
  private String            pec;
  private String            attivitaAteco;
  private String            attivitaOte;
  private String            sottosettoreOte;
  private String            labelAttivitaOte;
  private String            cciaaNumeroRegistroImprese;
  private String            cciaaAnnoIscrizione;

  public String getItestazionePartitaIva()
  {
    return itestazionePartitaIva;
  }

  public void setItestazionePartitaIva(String itestazionePartitaIva)
  {
    this.itestazionePartitaIva = itestazionePartitaIva;
  }

  public String getFormaGiuridica()
  {
    return formaGiuridica;
  }

  public void setFormaGiuridica(String formaGiuridica)
  {
    this.formaGiuridica = formaGiuridica;
  }

  public String getTelefono()
  {
    return telefono;
  }

  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }

  public String getFax()
  {
    return fax;
  }

  public void setFax(String fax)
  {
    this.fax = fax;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPec()
  {
    return pec;
  }

  public void setPec(String pec)
  {
    this.pec = pec;
  }

  public String getAttivitaAteco()
  {
    return attivitaAteco;
  }

  public void setAttivitaAteco(String attivitaAteco)
  {
    this.attivitaAteco = attivitaAteco;
  }

  public String getAttivitaOte()
  {
    return attivitaOte;
  }

  public void setAttivitaOte(String attivitaOte)
  {
    this.attivitaOte = attivitaOte;
  }

  public String getCciaaNumeroRegistroImprese()
  {
    return cciaaNumeroRegistroImprese;
  }

  public void setCciaaNumeroRegistroImprese(String cciaaNumeroRegistroImprese)
  {
    this.cciaaNumeroRegistroImprese = cciaaNumeroRegistroImprese;
  }

  public String getCciaaAnnoIscrizione()
  {
    return cciaaAnnoIscrizione;
  }

  public void setCciaaAnnoIscrizione(String cciaaAnnoIscrizione)
  {
    this.cciaaAnnoIscrizione = cciaaAnnoIscrizione;
  }

  public String getLabelAttivitaOte()
  {
    return labelAttivitaOte;
  }

  public void setLabelAttivitaOte(String labelAttivitaOte)
  {
    this.labelAttivitaOte = labelAttivitaOte;
  }

  public String getSottosettoreOte()
  {
    return sottosettoreOte;
  }

  public String getSottosettoreOteHtml()
  {
    return IuffiUtils.STRING.safeHTMLText(sottosettoreOte);
  }

  public void setSottosettoreOte(String sottosettoreOte)
  {
    this.sottosettoreOte = sottosettoreOte;
  }

}
