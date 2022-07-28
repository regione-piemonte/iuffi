package it.csi.iuffi.iuffiweb.util;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.DatoElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.ElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.VoceElementoDTO;

public class QuadroDinamicoUtils
{
  public String formatValoreElementoQuadro(DatoElementoQuadroDTO dato,
      ElementoQuadroDTO elemento)
  {
    return formatValoreElementoQuadro(dato, elemento, false);
  }

  public String formatValoreElementoQuadro(DatoElementoQuadroDTO dato,
      ElementoQuadroDTO elemento, boolean formatForEdit)
  {
    String valore = dato.getValoreElemento();
    if (valore == null)
    {
      return "";
    }
    if (GenericValidator.isBlankOrNull(valore) || elemento.isTipoNumerico())
    {
      if (!elemento.isTipoANN())
      {
        valore = formatNumeric(valore, elemento, formatForEdit);
      }
    }
    if (elemento.isTipoHTM())
    {
      /*
       * Non devo farne l'escape ==> viene visualizzato così com'è ==> non ci
       * dovrebbero essere problemi di sicurezza in quanto il codice html NON E'
       * inserito dall'utente
       */
      return valore;
    }
    if (elemento.isTipoCBT() || elemento.isTipoCMB() || elemento.isTipoLST()
        || elemento.isTipoRBT())
    {
      List<VoceElementoDTO> voci = elemento.getVociElemento();
      if (voci == null || voci.isEmpty())
      {
        // Non dovrebbe mai capitare però se c'è un errore di configurazione e
        // mancano le voci dell'elemento visualizzo un opportuno messaggio di
        // errore
        return "<div class=\"alert alert-danger\" role=\"alert\"><span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\">"
            + "</span> <span class=\"sr-only\">Error:</span>Elemento non configurato correttamente! Contattare l'assistenza tecnica</div>";
      }
      // Nel caso di elementi da selezionare da un elenco il campo valore
      // contiene il codice dell'elemento, per visualizzarne la descrizione
      // sovrascrivo la
      // variabile con la descrizione
      for (VoceElementoDTO voce : voci)
      {
        if (valore.equals(voce.getCodice()))
        {
          valore = voce.getValore();
          break;
        }
      }
    }
    valore = StringEscapeUtils.escapeHtml4(valore);
    if (!formatForEdit)
    {
      if (elemento.isTipoEUR())
      {
        valore += "&euro;";
      }
      else
      {
        if (elemento.isTipoPCT())
        {
          valore += "&#37;";
        }
      }
    }
    return valore;
  }

  private String formatNumeric(String valore, ElementoQuadroDTO elemento,
      boolean formatForEdit)
  {
    BigDecimal bdValore = new BigDecimal(valore);
    int precisione = IuffiUtils.NUMBERS.nvl(elemento.getPrecisione(), 0);
    return IuffiUtils.FORMAT.formatGenericNumber(bdValore, precisione,
        precisione > 0, !formatForEdit);
  }
}
