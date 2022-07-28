package it.csi.iuffi.iuffiweb.util;

import java.util.List;

import it.csi.iuffi.iuffiweb.exception.IuffiAssertionException;

/**
 * Classe astratta per le funzioni di utilità sulle asserzioni (gestione
 * custom). La classe è abstract perchè non deve essere usata direttamente ma
 * solo dalla sua implementazione nella costante Utils.STRING
 * 
 * @author Stefano Einaudi (Matr. 70399)
 * 
 */
public class AssertionUtils
{
  public void notNull(Object obj, String name) throws IuffiAssertionException
  {
    if (obj == null)
    {
      throw new IuffiAssertionException("L'oggetto " + name + " è null");
    }
  }

  public void notEmpty(List<?> obj, String name)
      throws IuffiAssertionException
  {
    if (obj == null || obj.size() == 0)
    {
      throw new IuffiAssertionException(
          "La lista di oggetti " + name + " è vuota o null");
    }
  }
}
