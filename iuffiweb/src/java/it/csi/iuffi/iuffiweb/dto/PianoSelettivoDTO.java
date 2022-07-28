package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class PianoSelettivoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 8008954636322165556L;

  private Long idProcedimentoOggetto; 
  private Long idInfoCinghiali; 
  private Long censitiAdultiM;
  private Long censitiAdultiF;
  private Long censitiPiccoliRossi;
  private Long censitiPiccoliStriati;
  private Long prelievoAdultiM;
  private Long prelievoAdultiF;
  private Long prelievoPiccoli;
  private String descrAltraMetod;
  private BigDecimal valoreMetodoCensimento;
  private Long idMetodoCensimento;
  private String descMetodologia;
  private Long idUnitaMisura;
  private String codUnitaMisura;
  private String descUnitaMisura;
  private List<Date> elencoDate;
  
  public List<Date> getElencoDate()
  {
    return elencoDate;
  }
  public void setElencoDate(List<Date> elencoDate)
  {
    this.elencoDate = elencoDate;
  }
  public Long getIdInfoCinghiali()
  {
    return idInfoCinghiali;
  }
  public Long getCensitiAdultiM()
  {
    return censitiAdultiM;
  }
  public Long getCensitiAdultiF()
  {
    return censitiAdultiF;
  }
  public Long getCensitiPiccoliRossi()
  {
    return censitiPiccoliRossi;
  }
  public Long getCensitiPiccoliStriati()
  {
    return censitiPiccoliStriati;
  }
  public String getDescrAltraMetod()
  {
    return descrAltraMetod;
  }
  public BigDecimal getValoreMetodoCensimento()
  {
    return valoreMetodoCensimento;
  }
  public Long getIdMetodoCensimento()
  {
    return idMetodoCensimento;
  }
  public String getDescMetodologia()
  {
    return descMetodologia;
  }
  public void setIdInfoCinghiali(Long idInfoCinghiali)
  {
    this.idInfoCinghiali = idInfoCinghiali;
  }
  public void setCensitiAdultiM(Long censitiAdultiM)
  {
    this.censitiAdultiM = censitiAdultiM;
  }
  public void setCensitiAdultiF(Long censitiAdultiF)
  {
    this.censitiAdultiF = censitiAdultiF;
  }
  public void setCensitiPiccoliRossi(Long censitiPiccoliRossi)
  {
    this.censitiPiccoliRossi = censitiPiccoliRossi;
  }
  public void setCensitiPiccoliStriati(Long censitiPiccoliStriati)
  {
    this.censitiPiccoliStriati = censitiPiccoliStriati;
  }
  public void setDescrAltraMetod(String descrAltraMetod)
  {
    this.descrAltraMetod = descrAltraMetod;
  }
  public void setValoreMetodoCensimento(BigDecimal valoreMetodoCensimento)
  {
    this.valoreMetodoCensimento = valoreMetodoCensimento;
  }
  public void setIdMetodoCensimento(Long idMetodoCensimento)
  {
    this.idMetodoCensimento = idMetodoCensimento;
  }
  public void setDescMetodologia(String descMetodologia)
  {
    this.descMetodologia = descMetodologia;
  }
  public Long getIdUnitaMisura()
  {
    return idUnitaMisura;
  }
  public String getCodUnitaMisura()
  {
    return codUnitaMisura;
  }
  public String getDescUnitaMisura()
  {
    return descUnitaMisura;
  }
  public void setIdUnitaMisura(Long idUnitaMisura)
  {
    this.idUnitaMisura = idUnitaMisura;
  }
  public void setCodUnitaMisura(String codUnitaMisura)
  {
    this.codUnitaMisura = codUnitaMisura;
  }
  public void setDescUnitaMisura(String descUnitaMisura)
  {
    this.descUnitaMisura = descUnitaMisura;
  }
  public Long getTotaleCensiti()
  {
    return censitiAdultiM+censitiAdultiF+censitiPiccoliRossi+censitiPiccoliStriati;
  }
  public Long getTotalePrelievo()
  {
    return prelievoAdultiM+prelievoAdultiF+prelievoPiccoli;
  }
  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }
  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }
  public Long getPrelievoAdultiM()
  {
    return prelievoAdultiM;
  }
  public Long getPrelievoAdultiF()
  {
    return prelievoAdultiF;
  }
  public Long getPrelievoPiccoli()
  {
    return prelievoPiccoli;
  }
  public void setPrelievoAdultiM(Long prelievoAdultiM)
  {
    this.prelievoAdultiM = prelievoAdultiM;
  }
  public void setPrelievoAdultiF(Long prelievoAdultiF)
  {
    this.prelievoAdultiF = prelievoAdultiF;
  }
  public void setPrelievoPiccoli(Long prelievoPiccoli)
  {
    this.prelievoPiccoli = prelievoPiccoli;
  }
  public List<String> getElencoDateStr(){
    if(elencoDate==null)
      return null;
    List<String> ret = new ArrayList<String>();
    for(Date d : elencoDate)
      ret.add(IuffiUtils.DATE.formatDate(d));
    return ret;
  }
 
}
