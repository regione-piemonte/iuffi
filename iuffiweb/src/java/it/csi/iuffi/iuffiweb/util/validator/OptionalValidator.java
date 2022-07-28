package it.csi.iuffi.iuffiweb.util.validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class OptionalValidator extends DateValidator
{
  /** serialVersionUID */
  private static final long serialVersionUID = 807054363415376393L;

  public Date validateOptionalDate(String fieldValue, String filedName,
      boolean onlyDate)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return null;
    }
    return super.validateDate(fieldValue, filedName, onlyDate);
  }

  public boolean validateOptionalDateInRange(Date fieldValue, String fieldName,
      Date dateMin, Date dateMax,
      boolean equalsAccepted, boolean onlyDate)
  {
    if (fieldValue == null)
    {
      return true;
    }
    return super.validateDateInRange(fieldValue, fieldName, dateMin, dateMax,
        equalsAccepted, onlyDate);
  }

  public Date validateOptionalDateInRange(String fieldValue, String fieldName,
      Date dateMin, Date dateMax,
      boolean equalsAccepted, boolean onlyDate)
  {
    Date date = validateOptionalDate(fieldValue, fieldName, onlyDate);
    if (date != null)
    {
      if (super.validateDateInRange(date, fieldName, dateMin, dateMax,
          equalsAccepted, onlyDate))
      {
        return date;
      }
    }
    return null;
  }

  public Long validateOptionalLong(String fieldValue, String fieldName)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return null;
    }
    return super.validateLong(fieldValue, fieldName);
  }

  public boolean validateOptionalLongInRange(Long fieldValue, String fieldName,
      long min, long max)
  {
    if (fieldValue == null)
    {
      return true;
    }
    return super.validateLongInRange(fieldValue, fieldName, min, max);
  }

  public Long validateOptionalLongInRange(String fieldValue, String fieldName,
      long min, long max)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return null;
    }
    return super.validateLongInRange(fieldValue, fieldName, min, max);
  }

  public BigDecimal validateOptionalBigDecimal(String fieldValue,
      String fieldName, int numDecimalDigit)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return null;
    }
    return super.validateBigDecimal(fieldValue, fieldName, numDecimalDigit);
  }

  public boolean validateOptionalBigDecimalInRange(BigDecimal fieldValue,
      String fieldName, BigDecimal min,
      BigDecimal max)
  {
    if (fieldValue == null)
    {
      return true;
    }
    return super.validateBigDecimalInRange(fieldValue, fieldName, min, max);
  }

  public BigDecimal validateOptionalBigDecimalInRange(String fieldValue,
      String fieldName, int numDecimalDigit,
      BigDecimal min, BigDecimal max)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return null;
    }
    return super.validateBigDecimalInRange(fieldValue, fieldName,
        numDecimalDigit, min, max);
  }

  public boolean validateOptionalFieldLength(String fieldValue,
      String fieldName, int min, int max)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    return super.validateFieldLength(fieldValue, fieldName, min, max);
  }

  public boolean validateOptionalFieldLength(String fieldValue,
      String fieldName, int min, int max, boolean trim)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    return super.validateFieldLength(fieldValue, fieldName, min, max, trim);
  }

  public boolean validateOptionalFieldMaxLength(String fieldValue, int max,
      String fieldName)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    return super.validateFieldMaxLength(fieldValue, fieldName, max);
  }

  public boolean validateOptionalEmail(String fieldValue, String fieldName)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    return super.validateEmail(fieldValue, fieldName);
  }

  public boolean validateOptionalCAP(String fieldValue, String fieldName)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    return super.validateCAP(fieldValue, fieldName);
  }

  public String validateMandatoryValueList(String fieldValue, String fieldName,
      String[] values)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return null;
    }
    int len = values == null ? 0 : values.length;
    for (int i = 0; i < len; ++i)
    {
      if (fieldValue.equals(values[i]))
      {
        return fieldValue;
      }
    }
    addError(fieldValue, "Valore non consentito");
    return null;
  }

  public Long validateValueList(Long fieldValue, String fieldName,
      Long[] values)
  {
    if (fieldValue == null)
    {
      return null;
    }
    int len = values == null ? 0 : values.length;
    for (int i = 0; i < len; ++i)
    {
      if (fieldValue.equals(values[i]))
      {
        return fieldValue;
      }
    }
    addError(fieldName, "Valore non consentito");
    return null;
  }

  public <T> DecodificaDTO<T> validateIDInDecodificaRange(T fieldValue,
      String fieldName,
      List<DecodificaDTO<T>> list, String... customErrorMessage)
  {
    if (fieldValue != null)
    {
      DecodificaDTO<T> decodifica = IuffiUtils.LIST.findDecodificaById(list,
          fieldValue);
      if (decodifica == null)
      {
        addError(fieldName, getDefaultIfNoCustomMessage(
            "Identificativo non trovato", customErrorMessage));
      }
      return decodifica;
    }
    return null;
  }

  protected String getDefaultIfNoCustomMessage(String defaultMessage,
      String... customMessage)
  {
    // In linea teorica potrebbero esserci più stringhe ma viene usata solo la
    // prima
    if (customMessage == null || customMessage.length == 0)
    {
      return defaultMessage;
    }
    else
    {
      return customMessage[0];
    }
  }
}
