package it.csi.iuffi.iuffiweb.util.validator;

import org.apache.commons.validator.GenericValidator;

public class TextValidator extends BasicValidator
{
  /** serialVersionUID */
  private static final long  serialVersionUID               = 807054363415376393L;
  public static final String ERRORE_LUNGHEZZA_MINIMA_TESTO  = "Campo troppo corto, inserire almeno ${caratteri} caratteri";
  public static final String ERRORE_LUNGHEZZA_MASSIMA_TESTO = "Campo troppo lungo, inserire non più di ${caratteri} caratteri";
  public static final String ERRORE_LUNGHEZZA_ESATTA_TESTO  = "Il campo deve essere di ${caratteri} caratteri";
  public static final String ERRORE_LUNGHEZZA_NOME_FILE     = "Il nome del file è troppo lungo: non deve superare i 100 caratteri (estensione compresa)";
  public static final String ERRORE_ESTENSIONE_FILE         = "Estensione del file non valida";

  public boolean validateFieldLength(String fieldValue, String fieldName,
      int min, int max)
  {
    return validateFieldLength(fieldValue, fieldName, min, max, false);
  }

  public boolean validateFieldLength(String fieldValue, String fieldName,
      int min, int max, boolean trim)
  {
    int len = 0;
    if (fieldValue != null)
    {
      if (trim)
      {
        fieldValue = fieldValue.trim();
      }
      len = fieldValue.length();
    }

    if (min != max)
    {
      if (len < min)
      {
        addError(fieldName, ERRORE_LUNGHEZZA_MINIMA_TESTO
            .replace("${caratteri}", String.valueOf(min)));
        return false;
      }
      else
      {
        if (len > max)
        {
          addError(fieldName, ERRORE_LUNGHEZZA_MASSIMA_TESTO
              .replace("${caratteri}", String.valueOf(max)));
          return false;
        }
      }
    }
    else
    {
      if (len != max)
      {
        addError(fieldName, ERRORE_LUNGHEZZA_ESATTA_TESTO
            .replace("${caratteri}", String.valueOf(max)));
        return false;
      }
    }

    return true;
  }

  public boolean validateFieldMaxLength(String fieldValue, String fieldName,
      int max)
  {
    return validateFieldLength(fieldValue, fieldName, 0, max);
  }

  public boolean validateEmail(String fieldValue, String fieldName)
  {
    if (!GenericValidator.isEmail(fieldValue))
    {
      addError(fieldName, "Formato dell'email non valido");
      return false;
    }
    return true;
  }

  public boolean validateCAP(String fieldValue, String fieldName)
  {
    if (fieldValue.matches("\\d\\d\\d\\d\\d"))
    {
      return true;
    }
    else
    {
      addError(fieldName, "CAP non valido");
    }
    return false;
  }

}
