package it.csi.iuffi.iuffiweb.dto.error;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class Errors extends HashMap<String, String>
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3770166489356520835L;

  public void addError(String errorName, String errorMessage)
  {
    put(errorName, errorMessage);
  }

  public String removeError(String errorName)
  {
    return remove(errorName);
  }

  public boolean validateMandatory(String fieldValue, String fieldName,
      String errorMessage)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateNotAllowed(String fieldValue, String fieldName,
      String errorMessage)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    addError(fieldName, errorMessage);
    return false;
  }

  public boolean validateNotAllowed(String fieldValue, String fieldName)
  {
    return validateNotAllowed(fieldValue, fieldName,
        VALIDATIONERRORS.VALIDAZIONE_CAMPO_NON_PERMESSO);
  }

  public boolean validateMandatory(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName,
        VALIDATIONERRORS.VALIDAZIONE_CAMPO_OBBLIGATORIO);
  }

  public boolean validateMandatory(Object fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName,
        VALIDATIONERRORS.VALIDAZIONE_CAMPO_OBBLIGATORIO);
  }

  public boolean validateMandatory(Object fieldValue, String fieldName,
      String errorMessage)
  {
    if (fieldValue == null)
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateFieldLength(String fieldValue, int min, int max,
      String fieldName)
  {
    return validateFieldLength(fieldValue, min, max, fieldName, false);
  }

  public boolean validateFieldLength(String fieldValue, int min, int max,
      String fieldName, boolean trim)
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
    if (len < min)
    {
      addError(fieldName, VALIDATIONERRORS.VALIDAZIONE_TESTO_LUNGHEZZA_MINIMA
          + min + " caratteri");
      return false;
    }
    else
    {
      if (len > max)
      {
        addError(fieldName, VALIDATIONERRORS.VALIDAZIONE_TESTO_LUNGHEZZA_MASSIMA
            + max + " caratteri");
        return false;
      }
    }
    return true;
  }

  public boolean validateFieldMaxLength(String fieldValue, int max,
      String fieldName)
  {
    return validateFieldLength(fieldValue, 0, max, fieldName);
  }

  public boolean validateMandatory(List<?> fieldValue, String fieldName,
      String errorMessage)
  {
    if (fieldValue == null || fieldValue.size() == 0)
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateMandatory(List<?> fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName,
        VALIDATIONERRORS.VALIDAZIONE_CAMPO_OBBLIGATORIO);
  }

  public long validateNumericID(String fieldValue, String fieldName)
  {
    try
    {
      return new Long(fieldValue.trim()).longValue();
    }
    catch (Exception e)
    {
      addError(fieldName, VALIDATIONERRORS.VALIDAZIONE_CAMPO_OBBLIGATORIO);
      return -1;
    }
  }

  public Date validateDate(String data, String filedName)
  {
    Date date = IuffiUtils.DATE.parseDate(data);
    if (date != null)
    {
      // Data formalmente valida
      return date;
    }
    else
    {
      // Data errata
      addError(filedName,
          "Data o Formato non valido (Formato accettato gg/mm/yyyy)");
      return null;
    }
  }

  public Date validateFutureDate(String dataRif, String data, String filedName,
      boolean equalAccepted)
  {
    return validateFutureDate(dataRif, data, filedName, equalAccepted, null);
  }

  public Date validateFutureDate(String dataRif, String data, String filedName,
      boolean equalAccepted, String msgErrore)
  {
    if (GenericValidator.isBlankOrNull(msgErrore))
      msgErrore = "La data deve essere posteriore o uguale alla data di riferimento";

    Date date = IuffiUtils.DATE.parseDate(data);
    if (date != null)
    {
      if (equalAccepted)
      {
        Date dataR = IuffiUtils.DATE.parseDate(dataRif);
        if (date.before(dataR))
        {
          // Data minima non rispettata
          addError(filedName, msgErrore);
          return null;
        }
      }
      else
      {
        if (!date.after(IuffiUtils.DATE.parseDate(dataRif)))
        {
          // Data minima non rispettata
          addError(filedName, msgErrore);
          return null;
        }
      }
      return date;
    }
    else
    {
      // Data errata
      addError(filedName,
          "Data o Formato non valido (Formato accettato gg/mm/yyyy)");
      return null;
    }
  }

  public Date validateDateTo(String dataDal, String dataAl, String filedName,
      boolean equalAccepted, String msgErrore)
  {
    if (GenericValidator.isBlankOrNull(msgErrore))
      msgErrore = "La data Al deve essere posteriore o uguale alla data Dal";

    Date date = IuffiUtils.DATE.parseDate(dataAl);
    if (date != null)
    {
      if (equalAccepted)
      {
        Date dataR = IuffiUtils.DATE.parseDate(dataDal);
        if (date.before(dataR))
        {
          // Data minima non rispettata
          addError(filedName, msgErrore);
          return null;
        }
      }
      else
      {
        if (!date.after(IuffiUtils.DATE.parseDate(dataDal)))
        {
          // Data minima non rispettata
          addError(filedName, msgErrore);
          return null;
        }
      }
      return date;
    }
    else
    {
      // Data errata
      addError(filedName,
          "Data o Formato non valido (Formato accettato gg/mm/yyyy)");
      return null;
    }
  }

  public Date validateFutureDate(String data, String filedName,
      boolean equalAccepted)
  {
    Date date = IuffiUtils.DATE.parseDate(data);
    if (date != null)
    {
      if (equalAccepted)
      {
        Date today = IuffiUtils.DATE.getCurrentDateNoTime();
        if (date.before(today))
        {
          // Data minima non rispettata
          addError(filedName,
              "La data deve essere posteriore o uguale ad oggi");
          return null;
        }
      }
      else
      {
        if (!date.after(IuffiUtils.DATE.getCurrentDateNoTime()))
        {
          // Data minima non rispettata
          addError(filedName, "La data deve essere posteriore ad oggi");
          return null;
        }
      }
      return date;
    }
    else
    {
      // Data errata
      addError(filedName,
          "Data o Formato non valido (Formato accettato gg/mm/yyyy)");
      return null;
    }
  }

  public Date validatePastDate(String data, String filedName,
      boolean equalAccepted)
  {
    Date date = IuffiUtils.DATE.parseDate(data);
    if (date != null)
    {
      if (equalAccepted)
      {
        if (date.after(IuffiUtils.DATE.getCurrentDateNoTime()))
        {
          // Data minima non rispettata
          addError(filedName, "La data deve essere minore o uguale ad oggi");
          return null;
        }
      }
      else
      {
        if (!date.before(IuffiUtils.DATE.getCurrentDateNoTime()))
        {
          // Data minima non rispettata
          addError(filedName, "La data deve essere minore ad oggi");
          return null;
        }
      }
      return date;
    }
    else
    {
      // Data errata
      addError(filedName,
          "Data o Formato non valido (Formato accettato gg/mm/yyyy)");
      return null;
    }
  }

  public boolean validateEmail(String fieldValue, String fieldName)
  {
    if (!GenericValidator.isEmail(fieldValue))
    {
      addError(fieldName, "formato dell'email non valido");
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

  public Long validateLongNumber(String fieldValue, String fieldName)
  {
    try
    {
      return Long.parseLong(fieldValue.trim());
    }
    catch (Exception e)
    {
      addError(fieldName, "Valore non numerico");
    }
    return null;
  }

  public Long validateLongNumberInRange(String fieldValue, String fieldName,
      long min, long max)
  {
    try
    {
      long value = Long.parseLong(fieldValue.trim());
      if (value < min)
      {
        addError(fieldName,
            "Valore non permesso, il valore minimo ammesso è " + min);
        return null;
      }
      else
      {
        if (value > max)
        {
          addError(fieldName,
              "Valore non permesso, il valore massimo ammesso è " + max);
          return null;
        }
      }
      return value;
    }
    catch (Exception e)
    {
      addError(fieldName, "Valore non numerico");
    }
    return null;
  }

  public BigDecimal validateBigDecimalNumber(String fieldValue,
      String fieldName, int numDecimalDigit)
  {
    try
    {
      BigDecimal value = new BigDecimal(fieldValue.trim().replace(',', '.'));
      if (value.scale() > numDecimalDigit)
      {
        addError(fieldName, "Valore non valido, sono ammesse al massimo "
            + numDecimalDigit + " cifre decimali");
      }
      else
      {
        return value;
      }
    }
    catch (Exception e)
    {
      addError(fieldName, "Valore non numerico");
    }
    return null;
  }

  public BigDecimal validateBigDecimalNumberInRange(String fieldValue,
      String fieldName, int numDecimalDigit, BigDecimal min, BigDecimal max)
  {
    try
    {
      BigDecimal value = new BigDecimal(fieldValue.trim().replace(',', '.'));
      if (value.scale() > numDecimalDigit)
      {
        addError(fieldName, "Valore non valido, sono ammesse al massimo "
            + numDecimalDigit + " cifre decimali");
        return null;
      }

      if (min != null && value.compareTo(min) < 0)
      {
        addError(fieldName, "Valore non permesso, il valore minimo ammesso è "
            + min.toString().replace(".", ","));
        return null;
      }
      else
      {
        if (max != null && value.compareTo(max) > 0)
        {
          addError(fieldName,
              "Valore non permesso, il valore massimo ammesso è "
                  + max.toString().replace(".", ","));
          return null;
        }
      }
      return value;
    }
    catch (Exception e)
    {
      addError(fieldName, "Valore non numerico");
    }
    return null;
  }

  public static final class VALIDATIONERRORS
  {
    public static final String VALIDAZIONE_CAMPO_NON_PERMESSO      = "Campo non permesso";
    public static final String VALIDAZIONE_CAMPO_OBBLIGATORIO      = "Campo obbligatorio";
    public static final String VALIDAZIONE_TESTO_LUNGHEZZA_MINIMA  = "Campo troppo corto, la lunghezza deve essere maggiore o uguale a ";
    public static final String VALIDAZIONE_TESTO_LUNGHEZZA_MASSIMA = "Campo troppo lungo, la lunghezza deve essere minore o uguale a ";

  }
}
