package it.csi.iuffi.iuffiweb.util.validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class MandatoryValidator extends OptionalValidator
{
  /** serialVersionUID */
  private static final long  serialVersionUID          = 807054363415376393L;
  public static final String ERRORE_CAMPO_NON_PERMESSO = "Campo non permesso";
  public static final String ERRORE_CAMPO_OBBLIGATORIO = "Campo obbligatorio";
  private static char carattere[] = {'A','B','C','D','E','F','G','H','I','J','K','L',
	      'M','N','O','P','Q','R','S','T','U','V','W','X',
	      'Y','Z','0','1','2','3','4','5','6','7','8','9'};
  private static int valore_pari[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,
	      19,20,21,22,23,24,25,0,1,2,3,4,5,6,7,8,9};
  private static int valore_dispari[] = {1,0,5,7,9,13,15,17,19,21,2,4,18,20,11,3,6,8,
	  12,14,16,10,22,25,24,23,1,0,5,7,9,13,15,17,19,21};

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
    return validateNotAllowed(fieldValue, fieldName, ERRORE_CAMPO_NON_PERMESSO);
  }

  public boolean validateMandatory(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
  }

  public boolean validateMandatory(Object fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
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
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
  }

  public boolean validateMandatory(Object[] fieldValue, String fieldName,
      String errorMessage)
  {
    if (fieldValue == null || fieldValue.length == 0)
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateMandatory(Object[] fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
  }

  public Date validateMandatoryDate(String fieldValue, String fieldName,
      boolean onlyDate)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateDate(fieldValue, fieldName, onlyDate);
    }
    return null;
  }

  public boolean validateMandatoryDateInRange(Date fieldValue, String fieldName,
      Date dateMin, Date dateMax,
      boolean equalsAccepted, boolean onlyDate)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateDateInRange(fieldValue, fieldName, dateMin, dateMax,
            equalsAccepted, onlyDate);
  }

  public Date validateMandatoryDateInRange(String fieldValue, String fieldName,
      Date dateMin, Date dateMax,
      boolean equalsAccepted, boolean onlyDate)
  {
    Date date = validateMandatoryDate(fieldValue, fieldName, onlyDate);
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

  public Long validateMandatoryLong(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateLong(fieldValue, fieldName);
    }
    return null;
  }

  public Double validateMandatoryDouble(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateDouble(fieldValue, fieldName);
    }
    return null;
  }

  public Long validateMandatoryLongInRange(String fieldValue, String fieldName,
      Long min, Long max)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateLongInRange(fieldValue, fieldName, min, max);
    }
    return null;
  }

  public BigDecimal validateMandatoryCurrency(String fieldValue,
      String fieldName)
  {
    return validateMandatoryBigDecimal(fieldValue, fieldName, 2);
  }

  public BigDecimal validateMandatoryBigDecimal(String fieldValue,
      String fieldName, int numDecimalDigit)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateBigDecimal(fieldValue, fieldName, numDecimalDigit);
    }
    return null;
  }

  public boolean validateMandatoryBigDecimalInRange(BigDecimal fieldValue,
      String fieldName, BigDecimal min,
      BigDecimal max)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateBigDecimalInRange(fieldValue, fieldName, min, max);
  }

  public BigDecimal validateMandatoryCurrencyInRange(String fieldValue,
      String fieldName, BigDecimal min,
      BigDecimal max)
  {
    return validateMandatoryBigDecimalInRange(fieldValue, fieldName, 2, min,
        max);
  }

  public BigDecimal validateMandatoryBigDecimalInRange(String fieldValue,
      String fieldName, int numDecimalDigit,
      BigDecimal min, BigDecimal max)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateBigDecimalInRange(fieldValue, fieldName,
          numDecimalDigit, min, max);
    }
    return null;
  }

  public boolean validateMandatoryFieldLength(String fieldValue, int min,
      int max, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateFieldLength(fieldValue, fieldName, min, max);
  }

  public boolean validateMandatoryFieldLength(String fieldValue, int min,
      int max, String fieldName, boolean trim)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateFieldLength(fieldValue, fieldName, min, max, trim);
  }

  public boolean validateMandatoryFieldMaxLength(String fieldValue,
      String fieldName, int max)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateFieldMaxLength(fieldValue, fieldName, max);
  }

  public boolean validateMandatoryEmail(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateEmail(fieldValue, fieldName);
  }

  public boolean validateMandatoryCAP(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateCAP(fieldValue, fieldName);
  }

  public boolean validateCuaa(String cuaa, String fieldName)
  {
    if (IuffiUtils.VALIDATION.isEmpty(cuaa))
    {
      addError(fieldName, IuffiConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    if (cuaa.length() != 11 && cuaa.length() != 16)
    {
      addError(fieldName, IuffiConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    if (cuaa.length() == 16 && !IuffiUtils.VALIDATION.controlloCf(cuaa))
    {
      addError(fieldName, IuffiConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    if (cuaa.length() == 11 && !IuffiUtils.VALIDATION.controlloPIVA(cuaa))
    {
      addError(fieldName, IuffiConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    return true;
  }

  public Long validateMandatoryID(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return validateLong(fieldValue, fieldName);
    }
    else
    {
      return null;
    }
  }

  public String validateMandatoryValueList(String fieldValue, String fieldName,
      String[] values)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
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
    else
    {
      addError(fieldName, "Valore non consentito");
      return null;
    }
  }

  public Long validateMandatoryValueList(Long fieldValue, String fieldName,
      Long[] values)
  {
    if (fieldValue == null)
    {
      addError(fieldName, "Campo Obbligatorio");
      return null;
    }
    return validateValueList(fieldValue, fieldName, values);
  }
  
  public boolean controlloCf(String stringa, String fieldName) {
	    char caratt;
	    int controllo = -1;
	    boolean ok = false;
	    int resto;
	    int sum_pari = 0;
	    int sum_dispari = 0;

	    if((stringa != null) && (stringa.length()==16)) {
	      stringa = stringa.toUpperCase();
	      for (int i=1; i<=15; i++) {
	        int row;
	        caratt = stringa.charAt(0);
	        stringa = stringa.substring(1);

	        for (row=1; row<=36; row++) {
	          if (carattere[row-1] == caratt) {
	            if ( (i/2)*2 == i ) {
	              sum_pari = sum_pari + valore_pari[row-1];
	              break;
	            }
	            else {
	              sum_dispari = sum_dispari + valore_dispari[row-1];
	              break;
	            }
	          }
	        }
	        //Occorre controllare se l'utente ha inserito caratteri non alfanumerici,
	        //perché in alcuni casi, con probabilità minima ma non nulla, il metodo
	        //potrebbe non restituire il messaggio di errore
	        if(row>36) {
	          //Il carattere non corrisponde a nessun valore salvato nell'array
	          //'carattere', per cui viene creato il messaggio di errore e si
	          //forza l'uscita dal metodo, per non eseguire altro codice a questo
	          //punto inutile
	          //createValidationException().addMessage("Codice Fiscale errato", name);

	          return ok;
	        }
	      }

	      resto = (sum_pari + sum_dispari) - ((sum_pari + sum_dispari)/26) * 26;

	      caratt =  stringa.charAt(0);

	      for (int row=1; row<=36; row++) {
	        if (carattere[row-1]  == caratt ) {
	          controllo = valore_pari[row-1];
	          break;
	        }
	      }

	      if (controllo == resto)
	        ok = true;
	    }
	    if(ok==false){
	    	addError(fieldName, "Codice fiscale scorretto");
	    }
	    return ok;
	  }
}
