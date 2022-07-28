package it.csi.iuffi.iuffiweb.util;

import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;

public class ListUtils
{
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

  public <T> DecodificaDTO<T> findDecodificaById(List<DecodificaDTO<T>> list,
      T id)
  {
    if (id == null)
    {
      // Cerco il primo null;
      for (DecodificaDTO<T> decodifica : list)
      {
        if (decodifica.getId() == null)
        {
          return decodifica;
        }
      }
    }
    else
    {
      // Cerco un valore not null specifico
      for (DecodificaDTO<T> decodifica : list)
      {
        final T currentID = decodifica.getId();
        if (id.equals(currentID))
        {
          return decodifica;
        }
      }
    }
    return null;
  }
}