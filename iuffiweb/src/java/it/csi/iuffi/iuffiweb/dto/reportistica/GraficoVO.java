package it.csi.iuffi.iuffiweb.dto.reportistica;

import java.io.Serializable;

public class GraficoVO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private String            descrBreve;
  private String            descrEstesa;
  private String            istruzioneSQL;
  private long              idTipoVisualizzazione;
  private String            descrTipoVisualizzazione;
  private String            tipoTema;
  private Object            jsonData;
  private long              idElencoQuery;
  private boolean           excelTemplate;
  private ReportVO          reportVO;

  public String getDescrBreve()
  {
    return descrBreve;
  }

  public void setDescrBreve(String descrBreve)
  {
    this.descrBreve = descrBreve;
  }

  public String getDescrEstesa()
  {
    return descrEstesa;
  }

  public void setDescrEstesa(String descrEstesa)
  {
    this.descrEstesa = descrEstesa;
  }

  public String getDescrCompleta()
  {
    return descrBreve + "\n" + descrEstesa;
  }

  public String getIstruzioneSQL()
  {
    return istruzioneSQL;
  }

  public void setIstruzioneSQL(String istruzioneSQL)
  {
    this.istruzioneSQL = istruzioneSQL;
  }

  public long getIdTipoVisualizzazione()
  {
    return idTipoVisualizzazione;
  }

  public void setIdTipoVisualizzazione(long idTipoVisualizzazione)
  {
    this.idTipoVisualizzazione = idTipoVisualizzazione;
  }

  public String getDescrTipoVisualizzazione()
  {
    return descrTipoVisualizzazione;
  }

  public void setDescrTipoVisualizzazione(String descrTipoVisualizzazione)
  {
    this.descrTipoVisualizzazione = descrTipoVisualizzazione;
  }

  public String getTipoTema()
  {
    return tipoTema;
  }

  public void setTipoTema(String tipoTema)
  {
    this.tipoTema = tipoTema;
  }

  public Object getJsonData()
  {
    return jsonData;
  }

  public void setJsonData(Object jsonData)
  {
    this.jsonData = jsonData;
  }

  public ReportVO getReportVO()
  {
    return reportVO;
  }

  public void setReportVO(ReportVO reportVO)
  {
    this.reportVO = reportVO;
  }

  public boolean isExcelTemplate()
  {
    return excelTemplate;
  }

  public void setExcelTemplate(boolean excelTemplate)
  {
    this.excelTemplate = excelTemplate;
  }

  public long getIdElencoQuery()
  {
    return idElencoQuery;
  }

  public void setIdElencoQuery(long idElencoQuery)
  {
    this.idElencoQuery = idElencoQuery;
  }

}
