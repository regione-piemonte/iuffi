package it.csi.iuffi.iuffiweb.model;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;


public class FotoApiDTO extends TabelleDTO implements ILoggable
{

	private static final long serialVersionUID = 1L;
	
  private String tag;
  private String note;
  private String base64;
  private String nomeFile;
  private Double longitudine;
  private Double latitudine;
  private Integer idMissione;
  private Integer idVisual;
  private Integer idCampionamento;
  private Integer idTrappolaggio;
  

  public Integer getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }

  public Integer getIdVisual()
  {
    return idVisual;
  }

  public void setIdVisual(Integer idVisual)
  {
    this.idVisual = idVisual;
  }

  public Integer getIdCampionamento()
  {
    return idCampionamento;
  }

  public void setIdCampionamento(Integer idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }

  public Integer getIdTrappolaggio()
  {
    return idTrappolaggio;
  }

  public void setIdTrappolaggio(Integer idTrappolaggio)
  {
    this.idTrappolaggio = idTrappolaggio;
  }

   
  public FotoApiDTO() {
    super();
  }

  public String getTag()
  {
    return tag;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Double getLongitudine()
  {
    return longitudine;
  }

  public void setLongitudine(Double longitudine)
  {
    this.longitudine = longitudine;
  }

  public Double getLatitudine()
  {
    return latitudine;
  }

  public void setLatitudine(Double latitudine)
  {
    this.latitudine = latitudine;
  }

  public String getBase64()
  {
    return base64;
  }

  public void setBase64(String base64)
  {
    this.base64 = base64;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }

  
}
