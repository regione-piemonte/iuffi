package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;

public class ProcedimentoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long            serialVersionUID = 4601339650061084885L;

  private long                         idProcedimento;
  private long                         idStatoOggetto;
  private String                       descrStatoOggetto;
  private long                         extIdUtenteAggiornamento;
  private String                       utenteAggiornamentoDescr;
  private String                       identificativo;
  private Date                         dataUltimoAggiornamento;
  private List<ProcedimentoOggettoDTO> procedimentoOggetto;
  private String                       denominazioneDelega;
  private List<DocumentoSpesaVO>       documentiSpesa;
  private List<LivelloDTO>             livelli;
  private String                       ammCompetenza;
  private String                       cuaaBeneficiario;
  private String                       denominazioneBeneficiario;
  private String                       denominazioneBando;

  private Long                         annoCampagna;

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public long getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(long idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public String getDescrStatoOggetto()
  {
    return descrStatoOggetto;
  }

  public void setDescrStatoOggetto(String descrStatoOggetto)
  {
    this.descrStatoOggetto = descrStatoOggetto;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public String getUtenteAggiornamentoDescr()
  {
    return utenteAggiornamentoDescr;
  }

  public void setUtenteAggiornamentoDescr(String utenteAggiornamentoDescr)
  {
    this.utenteAggiornamentoDescr = utenteAggiornamentoDescr;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public List<ProcedimentoOggettoDTO> getProcedimentoOggetto()
  {
    return procedimentoOggetto;
  }

  public void setProcedimentoOggetto(
      List<ProcedimentoOggettoDTO> procedimentoOggetto)
  {
    this.procedimentoOggetto = procedimentoOggetto;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = (identificativo != null) ? identificativo : "";
  }

  public String getDenominazioneDelega()
  {
    return denominazioneDelega;
  }

  public void setDenominazioneDelega(String denominzioneDelega)
  {
    this.denominazioneDelega = denominzioneDelega;
  }

  public List<DocumentoSpesaVO> getDocumentiSpesa()
  {
    return documentiSpesa;
  }

  public void setDocumentiSpesa(List<DocumentoSpesaVO> documentiSpesa)
  {
    this.documentiSpesa = documentiSpesa;
  }

  public String getDenominazioneBando()
  {
    return denominazioneBando;
  }

  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }

  public Long getAnnoCampagna()
  {
    return annoCampagna;
  }

  public String getAnnoCampagnaStr()
  {
    if (annoCampagna != null)
      return annoCampagna.toString();
    return "";
  }

  public void setAnnoCampagna(Long annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public List<LivelloDTO> getLivelli()
  {
    return livelli;
  }

  public String getLivelliForFilter()
  {
    String ret = "&&&";
    if (livelli != null)
      for (LivelloDTO l : livelli)
      {
        ret += l.getCodice() + "&&&";
      }
    return ret;
  }

  public void setLivelli(List<LivelloDTO> livelli)
  {
    this.livelli = livelli;
  }

  public String getLivelliHtml()
  {
    String ret = "";
    if (livelli != null && !livelli.isEmpty())
      for (LivelloDTO l : livelli)
        ret += l.getCodice() + " <br> ";
    return ret;
  }

  public String getAmmCompetenza()
  {
    return ammCompetenza;
  }

  public void setAmmCompetenza(String ammCompetenza)
  {
    this.ammCompetenza = ammCompetenza;
  }

  public String getCuaaBeneficiario()
  {
    return cuaaBeneficiario;
  }

  public void setCuaaBeneficiario(String cuaaBeneficiario)
  {
    this.cuaaBeneficiario = cuaaBeneficiario;
  }

  public String getDenominazioneBeneficiario()
  {
    return denominazioneBeneficiario;
  }

  public void setDenominazioneBeneficiario(String denominazioneBeneficiario)
  {
    this.denominazioneBeneficiario = denominazioneBeneficiario;
  }

}
