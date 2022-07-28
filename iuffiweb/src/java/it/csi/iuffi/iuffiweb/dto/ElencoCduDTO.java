package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ElencoCduDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID       = 3594435726694712597L;
  public static final String TIPO_AZIONE_READ_ONLY  = "R";
  public static final String TIPO_AZIONE_READ_WRITE = "W";
  private long               idElencoCdu;
  private String             codiceCdu;
  private String             nomeDocumentoCdu;
  private String             extCodMacroCdu;
  private String             tipoAzione;
  private String             flagHelp;

  public long getIdElencoCdu()
  {
    return idElencoCdu;
  }

  public void setIdElencoCdu(long idElencoCdu)
  {
    this.idElencoCdu = idElencoCdu;
  }

  public String getCodiceCdu()
  {
    return codiceCdu;
  }

  public void setCodiceCdu(String codiceCdu)
  {
    this.codiceCdu = codiceCdu;
  }

  public String getNomeDocumentoCdu()
  {
    return nomeDocumentoCdu;
  }

  public void setNomeDocumentoCdu(String nomeDocumentoCdu)
  {
    this.nomeDocumentoCdu = nomeDocumentoCdu;
  }

  public String getExtCodMacroCdu()
  {
    return extCodMacroCdu;
  }

  public void setExtCodMacroCdu(String extCodMacroCdu)
  {
    this.extCodMacroCdu = extCodMacroCdu;
  }

  public String getTipoAzione()
  {
    return tipoAzione;
  }

  public void setTipoAzione(String tipoAzione)
  {
    this.tipoAzione = tipoAzione;
  }

  public String getFlagHelp()
  {
    return flagHelp;
  }

  public void setFlagHelp(String flagHelp)
  {
    this.flagHelp = flagHelp;
  }

}
