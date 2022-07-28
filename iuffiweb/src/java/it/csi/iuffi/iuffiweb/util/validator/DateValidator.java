package it.csi.iuffi.iuffiweb.util.validator;

import java.util.Date;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DateValidator extends NumericValidator
{
  public static final String ERRORE_DATA_NON_VALIDA     = "Data o Formato non valido (Formato accettato gg/mm/yyyy)";
  public static final String ERRORE_DATA_ORA_NON_VALIDA = "Data o Formato non valido (Formato accettato gg/mm/yyyy hh:mm:ss)";
  public static final String ERRORE_DATA_RANGE_MIN      = "La data deve essere successiva ${data}";
  public static final String ERRORE_DATA_RANGE_MAX      = "La data deve essere precedente ${data}";
  /** serialVersionUID */
  private static final long  serialVersionUID           = 807054363415376393L;

  public Date validateDate(String data, String filedName, boolean onlyDate)
  {
    Date date = onlyDate ? IuffiUtils.DATE.parseDate(data)
        : IuffiUtils.DATE.parseDateTime(data);
    if (date != null)
    {
      // Data formalmente valida
      return date;
    }
    else
    {
      // Data errata
      addError(filedName,
          ERRORE_DATA_NON_VALIDA);
      return null;
    }
  }

  public boolean validateDateInRange(Date fieldValue, String fieldName,
      Date dateMin, Date dateMax, boolean equalsAccepted, boolean onlyDate)
  {
    if (dateMin != null)
    {
      if (dateMin.after(fieldValue)
          || (!equalsAccepted && dateMin.equals(fieldValue)))
      {
        addError(fieldName, getDateError(ERRORE_DATA_RANGE_MIN, dateMin,
            equalsAccepted, onlyDate));
        return false;
      }
    }
    if (dateMax != null)
    {
      if (dateMax.before(fieldValue)
          || (!equalsAccepted && dateMax.equals(fieldValue)))
      {
        addError(fieldName, getDateError(ERRORE_DATA_RANGE_MAX, dateMax,
            equalsAccepted, onlyDate));
        return false;
      }
    }
    return true;
  }

  private String getDateError(String erroMsg, Date date, boolean equalsAccepted,
      boolean onlyDate)
  {
    String dateFormat = null;
    date = IuffiUtils.DATE.removeTime(date);
    Date today = IuffiUtils.DATE.getCurrentDateNoTime();
    if (date.equals(today))
    {
      dateFormat = "alla data corrente";
    }
    else
    {
      dateFormat = "a " + (onlyDate ? IuffiUtils.DATE.formatDate(date)
          : IuffiUtils.DATE.formatDateTime(date));
    }
    if (equalsAccepted)
    {
      return erroMsg.replace("${data}", "o uguale " + dateFormat);
    }
    else
    {
      return erroMsg.replace("${data}", dateFormat);
    }
  }
}
