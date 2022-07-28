package it.csi.iuffi.iuffiweb.dto.procedimento;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class OrganismoDelegatoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idProcedimento;
  private long              extIdUtenteAggiornamento;
  private long              idProcedimAmministrazione;
  private Long              idUfficioZona;
  private long              extIdAmmCompetenza;
  private Long              extIdTecnico;
  private String            descrizioneAmm;
  private String            dettaglioAmm;
  private String            descrUffZona;
  private String            descrTecnico;
  private String            responsabile;

  public long getIdProcedimAmministrazione()
  {
    return idProcedimAmministrazione;
  }

  public void setIdProcedimAmministrazione(long idProcedimAmministrazione)
  {
    this.idProcedimAmministrazione = idProcedimAmministrazione;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public Long getIdUfficioZona()
  {
    return idUfficioZona;
  }

  public void setIdUfficioZona(Long idUfficioZona)
  {
    this.idUfficioZona = idUfficioZona;
  }

  public long getExtIdAmmCompetenza()
  {
    return extIdAmmCompetenza;
  }

  public void setExtIdAmmCompetenza(long extIdAmmCompetenza)
  {
    this.extIdAmmCompetenza = extIdAmmCompetenza;
  }

  public String getDescrizioneAmm()
  {
    return descrizioneAmm;
  }

  public void setDescrizioneAmm(String descrizioneAmm)
  {
    this.descrizioneAmm = descrizioneAmm;
  }

  public String getDettaglioAmm()
  {
    return dettaglioAmm;
  }

  public void setDettaglioAmm(String dettaglioAmm)
  {
    this.dettaglioAmm = dettaglioAmm;
  }

  public String getDescrUffZona()
  {
    return descrUffZona;
  }

  public void setDescrUffZona(String descrUffZona)
  {
    this.descrUffZona = descrUffZona;
  }

  public Long getExtIdTecnico()
  {
    return extIdTecnico;
  }

  public void setExtIdTecnico(Long extIdTecnico)
  {
    this.extIdTecnico = extIdTecnico;
  }

  public String getDescrTecnico()
  {
    return descrTecnico;
  }

  public void setDescrTecnico(String descrTecnico)
  {
    this.descrTecnico = descrTecnico;
  }

  public String getResponsabile()
  {
    return responsabile;
  }

  public void setResponsabile(String responsabile)
  {
    this.responsabile = responsabile;
  }

}
