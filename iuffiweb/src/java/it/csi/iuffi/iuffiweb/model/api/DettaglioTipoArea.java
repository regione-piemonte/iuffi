package it.csi.iuffi.iuffiweb.model.api;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DettaglioTipoArea implements ILoggable
{

  private static final long serialVersionUID = -5219985945721622886L;

  private Integer id;
  private String descrizione;
  private String dataInizioValidita;
  private String dataFineValidita;
  
  
  public DettaglioTipoArea()
  {

  }

  public DettaglioTipoArea(Integer id, String descrizione)
  {
    this.id = id;
    this.descrizione = descrizione;
  }

  public DettaglioTipoArea(Integer id, String descrizione,
      String dataInizioValidita)
  {
    super();
    this.id = id;
    this.descrizione = descrizione;
    this.dataInizioValidita = dataInizioValidita;
  }

  public DettaglioTipoArea(Integer id, String descrizione,
      String dataInizioValidita, String dataFineValidita)
  {
    super();
    this.id = id;
    this.descrizione = descrizione;
    this.dataInizioValidita = dataInizioValidita;
    this.dataFineValidita = dataFineValidita;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
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
