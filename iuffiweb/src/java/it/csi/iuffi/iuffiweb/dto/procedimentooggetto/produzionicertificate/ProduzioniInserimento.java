package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProduzioniInserimento implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String id;
  private String tipo;
  private String descrizioneProduzioneCertificata;
  private String descrizioneProduzione;
  private String descrizioneQualita;
  private String bio;
  private String nomecampo;
  private List<DecodificaDTO<Integer>> produzioniSecondarie;

  
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getTipo()
  {
    return tipo;
  }
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  public String getDescrizioneProduzioneCertificata()
  {
    return descrizioneProduzioneCertificata;
  }
  public void setDescrizioneProduzioneCertificata(
      String descrizioneProduzioneCertificata)
  {
    this.descrizioneProduzioneCertificata = descrizioneProduzioneCertificata;
  }
  public String getDescrizioneProduzione()
  {
    return descrizioneProduzione;
  }
  public void setDescrizioneProduzione(String descrizioneProduzione)
  {
    this.descrizioneProduzione = descrizioneProduzione;
  }
  public String getDescrizioneQualita()
  {
    return descrizioneQualita;
  }
  public void setDescrizioneQualita(String descrizioneQualita)
  {
    this.descrizioneQualita = descrizioneQualita;
  }
  public String getNomecampo()
  {
    return nomecampo;
  }
  public void setNomecampo(String nomecampo)
  {
    this.nomecampo = nomecampo;
  }
  public String getBio()
  {
    return bio;
  }
  public void setBio(String bio)
  {
    this.bio = bio;
  }
  public List<DecodificaDTO<Integer>> getProduzioniSecondarie()
  {
    return produzioniSecondarie;
  }
  public void setProduzioniSecondarie(List<DecodificaDTO<Integer>> produzioniSecondarie)
  {
    this.produzioniSecondarie = produzioniSecondarie;
  }
  
    
}
