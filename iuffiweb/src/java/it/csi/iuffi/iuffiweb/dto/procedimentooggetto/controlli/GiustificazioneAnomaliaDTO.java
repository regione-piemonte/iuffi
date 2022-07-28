package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.FileAllegatiDTO;

public class GiustificazioneAnomaliaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1782012850962101151L;

  private long              idSoluzioneAnomalia;
  private Long              idTipoRisoluzione;
  private String            tipoRisoluzione;
  private String            note;
  private String            flagNoteObbligatorie;
  private String            flagFileAllegatoObbligatorio;
  private Long              extIdUtenteAggiornamento;

  private String            provPrefettura;
  private String            numProtocollo;
  private Date              dataProtocollo;
  private Date              dataDocumento;
  private FileAllegatiDTO   allegato;

  public String getFlagNoteObbligatorie()
  {
    return flagNoteObbligatorie;
  }

  public void setFlagNoteObbligatorie(String flagNoteObbligatorie)
  {
    this.flagNoteObbligatorie = flagNoteObbligatorie;
  }

  public String getFlagFileAllegatoObbligatorio()
  {
    return flagFileAllegatoObbligatorio;
  }

  public void setFlagFileAllegatoObbligatorio(
      String flagFileAllegatoObbligatorio)
  {
    this.flagFileAllegatoObbligatorio = flagFileAllegatoObbligatorio;
  }

  public long getIdSoluzioneAnomalia()
  {
    return idSoluzioneAnomalia;
  }

  public void setIdSoluzioneAnomalia(long idSoluzioneAnomalia)
  {
    this.idSoluzioneAnomalia = idSoluzioneAnomalia;
  }

  public Long getIdTipoRisoluzione()
  {
    return idTipoRisoluzione;
  }

  public void setIdTipoRisoluzione(Long idTipoRisoluzione)
  {
    this.idTipoRisoluzione = idTipoRisoluzione;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public FileAllegatiDTO getAllegato()
  {
    return allegato;
  }

  public void setAllegato(FileAllegatiDTO allegato)
  {
    this.allegato = allegato;
  }

  public String getTipoRisoluzione()
  {
    return tipoRisoluzione;
  }

  public void setTipoRisoluzione(String tipoRisoluzione)
  {
    this.tipoRisoluzione = tipoRisoluzione;
  }

  public String getProvPrefettura()
  {
    return provPrefettura;
  }

  public void setProvPrefettura(String provPrefettura)
  {
    this.provPrefettura = provPrefettura;
  }

  public String getNumProtocollo()
  {
    return numProtocollo;
  }

  public void setNumProtocollo(String numProtocollo)
  {
    this.numProtocollo = numProtocollo;
  }

  public Date getDataProtocollo()
  {
    return dataProtocollo;
  }

  public void setDataProtocollo(Date dataProtocollo)
  {
    this.dataProtocollo = dataProtocollo;
  }

  public Date getDataDocumento()
  {
    return dataDocumento;
  }

  public void setDataDocumento(Date dataDocumento)
  {
    this.dataDocumento = dataDocumento;
  }

  public Long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(Long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

}
