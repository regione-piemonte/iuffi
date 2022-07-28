package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class RangePercentuale implements ILoggable
{
  /** serialVersionUID */
  private static final long              serialVersionUID = 2067962328202034792L;
  protected String                       codiceLivello;
  protected List<DecodificaDTO<Integer>> livelli          = new ArrayList<DecodificaDTO<Integer>>();
  protected List<Long>                   idInterventi     = new ArrayList<Long>();
  protected BigDecimal                   percentualeContributoMinima;
  protected BigDecimal                   percentualeContributoMassima;

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public BigDecimal getPercentualeContributoMinima()
  {
    return percentualeContributoMinima;
  }

  public void setPercentualeContributoMinima(
      BigDecimal percentualeContributoMinima)
  {
    this.percentualeContributoMinima = percentualeContributoMinima;
  }

  public BigDecimal getPercentualeContributoMassima()
  {
    return percentualeContributoMassima;
  }

  public void setPercentualeContributoMassima(
      BigDecimal percentualeContributoMassima)
  {
    this.percentualeContributoMassima = percentualeContributoMassima;
  }

  public int getNumeroInterventi()
  {
    return livelli.size();
  }

  public List<DecodificaDTO<Integer>> getLivelli()
  {
    return livelli;
  }

  public boolean isFixed()
  {
    // Nessun controllo sul null, le percentuali sono lette da DB con nvl. Si
    // declina ogni responsabilità per usi impropri
    return percentualeContributoMassima
        .compareTo(percentualeContributoMinima) == 0;
  }

  public String getLabel()
  {
    int size = livelli.size();
    if (size == 1)
    {
      return "Percentuale da applicare all'intervento selezionato per il tipo operazione "
          + codiceLivello;
    }
    else
    {
      return "Percentuale da applicare ai " + size
          + " interventi selezionati per il tipo operazione " + codiceLivello;
    }
  }

  public String getKey()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(codiceLivello).append("_").append(percentualeContributoMinima
        .multiply(IuffiConstants.MAX.PERCENTUALE).intValue())
        .append("_").append(percentualeContributoMassima
            .multiply(IuffiConstants.MAX.PERCENTUALE).intValue());
    return sb.toString();
  }

  public String getTooltip()
  {
    StringBuilder sb = new StringBuilder();
    TreeMap<String, String> codici = new TreeMap<String, String>();

    for (DecodificaDTO<Integer> livello : livelli)
    {
      final String codice = livello.getCodice();
      codici.put(codice, codice);
    }
    for (String codice : codici.keySet())
    {
      if (sb.length() != 0)
      {
        sb.append(" - ");
      }
      sb.append(codice);
    }
    return sb.toString();
  }

  public List<Long> getIdInterventi()
  {
    return idInterventi;
  }
}
