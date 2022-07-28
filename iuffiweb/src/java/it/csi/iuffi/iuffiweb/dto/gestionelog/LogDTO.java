package it.csi.iuffi.iuffiweb.dto.gestionelog;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class LogDTO implements ILoggable {
  
  private static final long serialVersionUID = -3843027512888421318L;
  
  private long idLog;
  private String descLog;
  private Date dataLog;
  
  public long getIdLog()
  {
    return idLog;
  }
  public void setIdLog(long idLog)
  {
    this.idLog = idLog;
  }
  public String getDescLog()
  {
    return descLog;
  }
  public void setDescLog(String descLog)
  {
    this.descLog = descLog;
  }
  public Date getDataLog()
  {
    return dataLog;
  }
  public void setDataLog(Date dataLog)
  {
    this.dataLog = dataLog;
  }
  
  public String getDataLogStr()
  {
    return dataLog==null ? "" : IuffiUtils.DATE.formatDate(dataLog);
  }


}
