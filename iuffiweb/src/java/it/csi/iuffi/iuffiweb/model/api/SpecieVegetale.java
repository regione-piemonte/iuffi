package it.csi.iuffi.iuffiweb.model.api;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SpecieVegetale implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  private Integer idSpecieVegetale;
  private String nomeVolgare;
  private String dataInizioValidita;
  private String dataFineValidita;
  
  public SpecieVegetale()
  {
    super();
  }

  public SpecieVegetale(Integer idSpecieVegetale, String nomeVolgare, String dataInizioValidita)
  {
    this.idSpecieVegetale = idSpecieVegetale;
    this.nomeVolgare = nomeVolgare;
    this.dataInizioValidita = dataInizioValidita;
  }

  public SpecieVegetale(Integer idSpecieVegetale, String nomeVolgare,
      String dataInizioValidita, String dataFineValidita)
  {
    super();
    this.idSpecieVegetale = idSpecieVegetale;
    this.nomeVolgare = nomeVolgare;
    this.dataInizioValidita = dataInizioValidita;
    this.dataFineValidita = dataFineValidita;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public String getNomeVolgare()
  {
    return nomeVolgare;
  }

  public void setNomeVolgare(String nomeVolgare)
  {
    this.nomeVolgare = nomeVolgare;
  }

  public String getDataInizioValidita()
  {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(String dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }

  public String getDataFineValidita()
  {
    return dataFineValidita;
  }

  public void setDataFineValidita(String dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }

}
