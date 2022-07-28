package it.csi.iuffi.iuffiweb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

/**
 * Classe astratta per le funzioni di utilità sulle stringhe. La classe è
 * abstract perchè non deve essere usata direttamente ma solo dalla sua
 * implementazione nella costante Utils.STRING
 * 
 * @author Stefano Einaudi (Matr. 70399)
 * 
 */
public abstract class StringUtils
{
  public String nvl(Object obj, String defaultValue)
  {
    return obj == null ? defaultValue : obj.toString();
  }

  public String toUpperCaseTrim(String str)
  {
    return str == null ? null : str.trim().toUpperCase();
  }

  public String toLowerCaseTrim(String str)
  {
    return str == null ? null : str.trim().toLowerCase();
  }

  public String toUpperCase(String str)
  {
    return str == null ? null : str.toUpperCase();
  }

  public String toLowerCase(String str)
  {
    return str == null ? null : str.toLowerCase();
  }

  public String abbreviate(String str, int maxLenght)
  {
    return str.substring(0, Math.min(str.length(), maxLenght));
  }

  public String nvl(Object obj)
  {
    return nvl(obj, "");
  }

  public String trim(String str)
  {
    return str == null ? null : str.trim();
  }

  public String nvl(String obj, String defaultValue)
  {
    return obj == null ? defaultValue : obj;
  }

  public String nvl(String obj)
  {
    return nvl(obj, "");
  }

  public String safeHTMLText(String str)
  {
    return nvl(str).replace("<", "&lt;").replace(">", "&gt;")
        .replace("\n", "<br/>").replace("ò", "&ograve").replace("à", "&agrave")
        .replace("è", "&egrave").replace("ì", "&igrave");
  }

  public String compactText(String str)
  {
    return nvl(str).replace("\n", "").replace("\r", "").replace("\t", "");
  }

  public String checkNull(Object object)
  {
    if (object != null)
    {
      return object.toString();
    }
    else
    {
      return null;
    }
  }

  public String concat(String separator, String... strings)
  {
    StringBuilder sb = new StringBuilder();
    if (strings != null)
    {
      for (String s : strings)
      {
        if (s != null)
        {
          if (sb.length() > 0)
          {
            sb.append(separator);
          }
          sb.append(s);
        }
      }
    }
    return sb.toString();
  }

  public String concat(String separator, Object... strings)
  {
    StringBuilder sb = new StringBuilder();
    if (strings != null)
    {
      for (Object s : strings)
      {
        if (s != null)
        {
          if (sb.length() > 0)
          {
            sb.append(separator);
          }
          sb.append(s);
        }
      }
    }
    return sb.toString();
  }

  // Inserisce a sinistra il carattere di riempimento, fino alla lunghezza
  // specificata.
  public String lpad(String str, char padChar, int length)
  {
    if (str == null)
      return "";

    int chrMancanti = length - str.length();
    for (int i = 0; i < chrMancanti; i++)
      str = padChar + str;

    return str;
  }

  public String insertCharAt(String str, int index, char chr)
  {
    if (index > str.length() - 1)
      return str;
    return str.substring(0, index) + chr + str.substring(index);
  }

  public String urlEncode(String text) throws UnsupportedEncodingException
  {
    return urlEncode(text, StandardCharsets.ISO_8859_1.toString());
  }

  public String urlEncode(String text, String encoding)
      throws UnsupportedEncodingException
  {
    return URLEncoder.encode(text, encoding);
  }

  public String escapeSpecialsChar(String text)
  {
    if (text == null)
    {
      return null;
    }
    // System.out.println((int)motivazione.charAt(0)); per sapere il numero
    return text.replace("&#61656;", "-")
        .replace(new Character((char) 149).charValue(), (char) 0xb7) // rimpiazzo
                                                                     // elenco
                                                                     // puntato
                                                                     // con
                                                                     // punto
        .replace(new Character((char) 146).charValue(), (char) 0x27) // rimpiazzo
                                                                     // apostrofo
                                                                     // con '
        .replace(new Character((char) 147).charValue(), (char) 0x22) // rimpiazzo
                                                                     // aperte
                                                                     // virgolette
                                                                     // con "
        .replace(new Character((char) 148).charValue(), (char) 0x22) // rimpiazzo
                                                                     // chiuse
                                                                     // virgolette
                                                                     // con ";
        .replace(new Character((char) 128).charValue(), (char) 0x20AC); // rimpiazzo
                                                                        // euro
                                                                        // con
                                                                        // €";
  }

  public String normalizeString(String str, String delimiter)
  {
    StringBuilder strRet = new StringBuilder();
    StringTokenizer st = new StringTokenizer(str);
    while (st.hasMoreElements())
    {
      strRet.append(st.nextElement() + delimiter);
    }
    return strRet.toString().trim();
  }

  public String substr(String s, int start, int end)
  {
    if (s == null)
    {
      return s;
    }
    int len = s.length();
    if (len <= end)
    {
      return s;
    }
    return s.substring(start, end);
  }
}
