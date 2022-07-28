package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.EsitoOggettoDTO;

public class GruppoOggettoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long     serialVersionUID = 4601339650061084885L;

  private long                  idProcedimento;
  private long                  idGruppoOggetto;
  private String                codice;
  private String                codRaggruppamento;
  private String                descrizione;
  private String                descrStatoGruppo;
  private Date                  dataStatoGruppo;
  private List<OggettoDTO>      oggetti;
  private List<EsitoOggettoDTO> stati;
  private boolean               selected         = false;
  private boolean               gruppoBloccato   = false;

  public boolean isGruppoBloccato()
  {
    return gruppoBloccato;
  }

  public void setGruppoBloccato(boolean gruppoBloccato)
  {
    this.gruppoBloccato = gruppoBloccato;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<OggettoDTO> getOggetti()
  {
    return oggetti;
  }

  public void setOggetti(List<OggettoDTO> oggetti)
  {
    this.oggetti = oggetti;
  }

  public long getIdGruppoOggetto()
  {
    return idGruppoOggetto;
  }

  public void setIdGruppoOggetto(long idGruppoOggetto)
  {
    this.idGruppoOggetto = idGruppoOggetto;
  }

  public String getCodRaggruppamento()
  {
    return codRaggruppamento;
  }

  public void setCodRaggruppamento(String codRaggruppamento)
  {
    this.codRaggruppamento = codRaggruppamento;
  }

  public String getDescrStatoGruppo()
  {
    return descrStatoGruppo;
  }

  public void setDescrStatoGruppo(String descrStatoGruppo)
  {
    this.descrStatoGruppo = descrStatoGruppo;
  }

  public Date getDataStatoGruppo()
  {
    return dataStatoGruppo;
  }

  public void setDataStatoGruppo(Date dataStatoGruppo)
  {
    this.dataStatoGruppo = dataStatoGruppo;
  }

  public boolean isIterPresente()
  {
    return !GenericValidator.isBlankOrNull(this.descrStatoGruppo);
  }

  public String getElencoDescrStati()
  {

    String stati = "";
    int c = 0;

    if (this.stati != null && !this.stati.isEmpty())
    {
      for (EsitoOggettoDTO e : this.stati)
      {
        if (c > 0 && e.getDescrizione() != null && e.isSelected())
          stati = stati + ", ";

        if (e.isSelected())
        {
          stati = stati + e.getDescrizione();
          c++;
        }

      }
    }
    if (stati.trim().length() > 0)
      return descrizione + " (" + stati + ")";
    else
      return descrizione;
  }

  public String getElencoDescrStatiSenzaNonPresente()
  {

    String stati = "";
    int c = 0;

    if (this.stati != null && !this.stati.isEmpty())
    {
      for (EsitoOggettoDTO o : this.stati)
      {
        if (o.getIdEsito() == 0)
          continue;
        if (c > 0)
          stati = stati + ", ";
        stati = stati + o.getDescrizione();
        c++;
      }
    }
    if (stati.trim().length() > 0)
      return descrizione + " (" + stati + ")";
    else
      return descrizione;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected(boolean selected)
  {
    this.selected = selected;
  }

  public List<EsitoOggettoDTO> getStati()
  {
    return stati;
  }

  public void setStati(List<EsitoOggettoDTO> stati)
  {
    this.stati = stati;
  }

  public String getChiaveOggetto()
  {
    return this.idGruppoOggetto + "_" + this.codRaggruppamento;
  }

}
