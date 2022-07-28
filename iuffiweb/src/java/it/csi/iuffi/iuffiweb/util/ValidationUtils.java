package it.csi.iuffi.iuffiweb.util;

import java.util.Vector;

public class ValidationUtils
{

  private char carattere[]      =
  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
      'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
      'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
      '9' };
  private int  valore_pari[]    =
  { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
      14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 0, 1, 2, 3, 4, 5,
      6, 7, 8, 9 };
  private int  valore_dispari[] =
  { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4,
      18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23, 1, 0, 5, 7, 9,
      13, 15, 17, 19, 21 };

  public boolean isValidCuaa(String cuaa)
  {
    if (isEmpty(cuaa) || (cuaa.length() != 11 && cuaa.length() != 16)
        || (cuaa.length() == 16 && !controlloCf(cuaa))
        || (cuaa.length() == 11 && !controlloPIVA(cuaa)))
    {
      return false;
    }

    return true;
  }

  public boolean isEmpty(String field)
  {
    if (field == null || field.length() == 0)
      return true;
    return false;
  }

  public boolean isNotEmpty(String field)
  {
    if (field == null || field.length() == 0)
      return false;
    return true;
  }

  public boolean isNotEmpty(Object field)
  {
    if (field instanceof String)
      return isNotEmpty((String) field);
    return (field != null);
  }

  public boolean isDefaultComboValue(Object value)
  {
    return ("null".equals(value));
  }

  public boolean controlloCf(String stringa)
  {
    char caratt;
    int controllo = -1;
    boolean ok = false;
    int resto;
    int sum_pari = 0;
    int sum_dispari = 0;

    if ((stringa != null) && (stringa.length() == 16))
    {
      stringa = stringa.toUpperCase();
      for (int i = 1; i <= 15; i++)
      {
        int row;
        caratt = stringa.charAt(0);
        stringa = stringa.substring(1);

        for (row = 1; row <= 36; row++)
        {
          if (carattere[row - 1] == caratt)
          {
            if ((i / 2) * 2 == i)
            {
              sum_pari = sum_pari + valore_pari[row - 1];
              break;
            }
            else
            {
              sum_dispari = sum_dispari + valore_dispari[row - 1];
              break;
            }
          }
        }
        // Occorre controllare se l'utente ha inserito caratteri non
        // alfanumerici,
        // perché in alcuni casi, con probabilità minima ma non nulla,
        // il metodo
        // potrebbe non restituire il messaggio di errore
        if (row > 36)
        {
          // Il carattere non corrisponde a nessun valore salvato
          // nell'array
          // 'carattere', per cui viene creato il messaggio di errore
          // e si
          // forza l'uscita dal metodo, per non eseguire altro codice
          // a questo
          // punto inutile
          // createValidationException().addMessage("Codice Fiscale errato",
          // name);

          return ok;
        }
      }

      resto = (sum_pari + sum_dispari) - ((sum_pari + sum_dispari) / 26)
          * 26;

      caratt = stringa.charAt(0);

      for (int row = 1; row <= 36; row++)
      {
        if (carattere[row - 1] == caratt)
        {
          controllo = valore_pari[row - 1];
          break;
        }
      }

      if (controllo == resto)
        ok = true;
    }
    return ok;
  }

  public boolean controlloPIVA(String stringa)
  {
    boolean ok = false;

    int somma = 0;

    if (!isValidCurrency(stringa))
      return ok;

    if ((stringa != null) && (stringa.length() == 11))
    {
      for (int i = 0; i <= 8; i += 2)
      {
        somma += stringa.charAt(i) - '0';
      }

      for (int i = 1; i <= 9; i += 2)
      {
        int temp = (stringa.charAt(i) - '0') * 2;
        if (temp > 9)
          temp -= 9;
        somma += temp;
      }

      if ((10 - somma % 10) % 10 == stringa.charAt(10) - '0')
      {
        ok = true;
      }
    }
    return ok;
  }

  public boolean isValidCurrency(String field)
  {
    if (field == null || field.length() == 0)
      return false;
    try
    {
      Double.parseDouble(field);
      return true;
    }
    catch (NumberFormatException nfEx)
    {
      return false;
    }
  }

  public boolean isEsitoOggettoNonRiapribile(String codiceEsito)
  {
    if (codiceEsito != null)
    {
      return codiceEsito.startsWith("APP") ||
          codiceEsito.startsWith("NOLIQ") ||
          codiceEsito.equals("T");
    }
    return false;
  }

  public boolean isValidVectorOfFlags(Vector<String> flags)
  {
    for (String flag : flags)
    {
      if (!isValidFlag(flag))
      {
        return false;
      }
    }
    return true;
  }

  public boolean isValidFlag(String flag)
  {
    return (flag.length() == 1 &&
        (Character.isLetter(flag.charAt(0))
            || Character.isDigit(flag.charAt(0))));
  }
}
