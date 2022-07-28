package it.csi.iuffi.iuffiweb.util.validator;

import java.math.BigDecimal;

public class NumericValidator extends TextValidator
{
  public static final String ERRORE_MAX_CIFRE_DECIMALI     = "Valore non valido, sono ammesse al massimo ${cifre} cifre decimali";
  public static final String ERRORE_VALORE_MASSIMO_AMMESSO = "Valore non permesso, il valore massimo ammesso è ";
  public static final String ERRORE_VALORE_MINIMO_AMMESSO  = "Valore non permesso, il valore minimo ammesso è ";
  public static final String ERRORE_VALORE_NON_NUMERICO    = "Valore non numerico";
  /** serialVersionUID */
  private static final long  serialVersionUID              = 807054363415376393L;

  public Long validateLong(String fieldValue, String fieldName)
  {
    try
    {
      return Long.parseLong(fieldValue.trim());
    }
    catch (Exception e)
    {
      addError(fieldName, ERRORE_VALORE_NON_NUMERICO);
    }
    return null;
  }

  public Double validateDouble(String fieldValue, String fieldName)
  {
    try
    {
      return Double.parseDouble(fieldValue.trim());
    }
    catch (Exception e)
    {
      addError(fieldName, ERRORE_VALORE_NON_NUMERICO);
    }
    return null;
  }

  public boolean validateLongInRange(long fieldValue, String fieldName,
      Long min, Long max)
  {
    if (min != null && fieldValue < min)
    {
      addError(fieldName, ERRORE_VALORE_MINIMO_AMMESSO + min);
      return false;
    }
    else
    {
      if (max != null && fieldValue > max)
      {
        addError(fieldName, ERRORE_VALORE_MASSIMO_AMMESSO + max);
        return false;
      }
    }
    return true;
  }

  public Long validateLongInRange(String fieldValue, String fieldName, Long min,
      Long max)
  {
    Long value = validateLong(fieldValue, fieldName);
    if (value != null)
    {
      if (!validateLongInRange(value, fieldName, min, max))
      {
        value = null;
      }
    }
    return value;
  }

  public BigDecimal validateBigDecimal(String fieldValue, String fieldName,
      int numDecimalDigit)
  {
    try
    {
      if (fieldValue.trim().equals(""))
      {
        return null;
      }
      else
      {
        BigDecimal value = new BigDecimal(fieldValue.trim().replace(',', '.'));
        if (value.scale() > numDecimalDigit)
        {
          addError(fieldName, ERRORE_MAX_CIFRE_DECIMALI.replace("${cifre}",
              String.valueOf(numDecimalDigit)));
        }
        else
        {
          return value;
        }

      }
    }
    catch (Exception e)
    {
      addError(fieldName, ERRORE_VALORE_NON_NUMERICO);
    }
    return null;
  }

  public boolean validateBigDecimalInRange(BigDecimal fieldValue,
      String fieldName, BigDecimal min, BigDecimal max)
  {
    try
    {
      if (min != null && fieldValue.compareTo(min) < 0)
      {
        addError(fieldName,
            ERRORE_VALORE_MINIMO_AMMESSO + min.toString().replace(".", ","));
        return false;
      }
      else
      {
        if (max != null && fieldValue.compareTo(max) > 0)
        {
          addError(fieldName,
              ERRORE_VALORE_MASSIMO_AMMESSO + max.toString().replace(".", ","));
          return false;
        }
      }
      return true;
    }
    catch (Exception e)
    {
      addError(fieldName, ERRORE_VALORE_NON_NUMERICO);
      return false;
    }
  }

  public BigDecimal validateBigDecimalInRange(String fieldValue,
      String fieldName, int numDecimalDigit, BigDecimal min, BigDecimal max)
  {
    BigDecimal value = validateBigDecimal(fieldValue, fieldName,
        numDecimalDigit);
    if (value != null)
    {
      if (!validateBigDecimalInRange(value, fieldName, min, max))
      {
        value = null;
      }
    }
    return value;
  }
}
