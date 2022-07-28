package it.csi.iuffi.iuffiweb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class ArrayUtils
{

  public long[] toLong(String[] array) throws InternalUnexpectedException
  {
    return toLong(array, false);
  }

  /**
   * Converte un Vector<String> in Vector<Long>
   * 
   * @author michele.macagno
   * @param vector
   * @return Vector<Long>
   * @throws InternalUnexpectedException
   *           nel caso in cui una conversione numerica non sia andata a buon
   *           fine
   */
  public Vector<Long> toVectorLong(Vector<String> vector)
      throws InternalUnexpectedException
  {
    Vector<Long> vl = new Vector<Long>();
    try
    {
      if (vector != null)
      {
        for (String s : vector)
        {
          vl.add(Long.parseLong(s));
        }
      }
    }
    catch (Exception e)
    {
      throw new InternalUnexpectedException(e);
    }
    return vl;
  }

  public long[] toLong(String[] array, boolean zeroOnError)
      throws InternalUnexpectedException
  {
    if (array == null)
    {
      return null;
    }
    int len = array == null ? 0 : array.length;
    long newArray[] = new long[len];
    for (int i = 0; i < len; ++i)
    {
      try
      {
        newArray[i] = new Long(array[i]);
      }
      catch (Exception e)
      {
        // Se il parametro zeroOnError è true allora non faccio nulla in caso di
        // errore, il valore i-esimo dell'array sarà quello di
        // default ossia 0
        if (!zeroOnError)
        {
          // altrimenti genero un'eccezione
          throw new InternalUnexpectedException(e, new LogParameter[]
          { new LogParameter("array", array) });
        }
      }
    }
    return newArray;
  }

  public List<Integer> toListOfInteger(String[] array)
  {
    int len = array == null ? 0 : array.length;
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < len; ++i)
    {
      String value = array[i];
      if (value == null)
      {
        list.add(null);
      }
      else
      {
        list.add(new Integer(value));
      }
    }
    return list;
  }

  public List<Long> toListOfLong(String[] array)
  {
    int len = array == null ? 0 : array.length;
    ArrayList<Long> list = new ArrayList<Long>();
    for (int i = 0; i < len; ++i)
    {
      String value = array[i];
      if (value == null)
      {
        list.add(null);
      }
      else
      {
        list.add(new Long(value));
      }
    }
    return list;
  }
  
  public List<String> toListOfString(String[] array)
  {
    int len = array == null ? 0 : array.length;
    ArrayList<String> list = new ArrayList<String>();
    for (int i = 0; i < len; ++i)
    {
      String value = array[i];
      if (value == null)
      {
        list.add(null);
      }
      else
      {
        list.add(value);
      }
    }
    return list;
  }

  public List<Long> toListOfLong(Long[] array)
  {
    int len = array == null ? 0 : array.length;
    ArrayList<Long> list = new ArrayList<Long>();
    for (int i = 0; i < len; ++i)
    {
      Long value = array[i];
      if (value == null)
      {
        list.add(null);
      }
      else
      {
        list.add(value);
      }
    }
    return list;
  }

  public boolean contains(Object[] array, Object value)
  {
    boolean found = false;
    if (array != null)
    {
      for (Object obj : array)
      {
        if (value == obj) // Principalmente null == null
        {
          found = true;
          break;
        }
        if (obj != null && obj.equals(value))
        {
          found = true;
          break;
        }
      }
    }
    return found;
  }

  public long[] toLong(Vector<String> vector) throws InternalUnexpectedException
  {
    String[] arrayString;
    if (vector != null)
    {
      arrayString = vector.toArray(new String[vector.size()]);
    }
    else
    {
      arrayString = new String[]
      {};
    }
    return toLong(arrayString);
  }

  public Long[] toArray(Vector<Long> vector) throws InternalUnexpectedException
  {
    return vector.toArray(new Long[vector.size()]);
  }
}