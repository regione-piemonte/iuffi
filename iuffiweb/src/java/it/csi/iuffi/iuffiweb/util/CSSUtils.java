package it.csi.iuffi.iuffiweb.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

public abstract class CSSUtils
{
  public static final Map<String, String> MAP_EXTENSIONS = new HashMap<String, String>();
  static
  {
    MAP_EXTENSIONS.put(".jpg", "ico_image");
    MAP_EXTENSIONS.put(".jpeg", "ico_image");
    MAP_EXTENSIONS.put(".png", "ico_image");
    MAP_EXTENSIONS.put(".bmp", "ico_image");
    MAP_EXTENSIONS.put(".tiff", "ico_image");
    MAP_EXTENSIONS.put(".xls", "ico_excel");
    MAP_EXTENSIONS.put(".xlsx", "ico_excel");
    MAP_EXTENSIONS.put(".pdf", "ico_pdf");
    MAP_EXTENSIONS.put(".doc", "ico_word");
    MAP_EXTENSIONS.put(".docx", "ico_word");
    MAP_EXTENSIONS.put(".rtf", "ico_word");
    MAP_EXTENSIONS.put(".txt", "ico_text");
  }

  private String getFileExtension(String fileName)
  {
    try
    {
      int pos = fileName.lastIndexOf('.');
      if (pos < 0)
      {
        return "";
      }
      return fileName.substring(pos);
    }
    catch (Exception e)
    {
      return "";
    }
  }

  public String getDocumentIconClass(String fileName)
  {
    String iconClass = null;
    if (!GenericValidator.isBlankOrNull(fileName))
    {
      iconClass = MAP_EXTENSIONS.get(getFileExtension(fileName));
    }
    if (iconClass != null)
    {
      return iconClass;
    }
    else
    {
      return "ico_file";
    }
  }
}
