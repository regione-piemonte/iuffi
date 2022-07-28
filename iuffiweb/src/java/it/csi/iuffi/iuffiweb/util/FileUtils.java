package it.csi.iuffi.iuffiweb.util;

import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.validator.GenericValidator;

public abstract class FileUtils
{
  public static final Map<String, String>  MAP_EXTENSIONS_TO_CSS = new HashMap<String, String>();
  public static final MimetypesFileTypeMap MIMETYPESFILETYPE     = new MimetypesFileTypeMap();
  static
  {
    MAP_EXTENSIONS_TO_CSS.put(".jpg", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".jpeg", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".png", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".bmp", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".tiff", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".xls", "ico_excel");
    MAP_EXTENSIONS_TO_CSS.put(".xlsx", "ico_excel");
    MAP_EXTENSIONS_TO_CSS.put(".pdf", "ico_pdf");
    MAP_EXTENSIONS_TO_CSS.put(".doc", "ico_word");
    MAP_EXTENSIONS_TO_CSS.put(".docx", "ico_word");
    MAP_EXTENSIONS_TO_CSS.put(".rtf", "ico_word");
    MAP_EXTENSIONS_TO_CSS.put(".txt", "ico_text");

  }

  public String getFileExtension(String fileName, boolean removeDot)
  {
    try
    {
      int pos = fileName.lastIndexOf('.');
      if (pos < 0)
      {
        return "";
      }
      if (removeDot)
      {
        pos++;
      }
      return fileName.substring(pos);
    }
    catch (Exception e)
    {
      return "";
    }
  }

  public String getOnlyFileExtension(String fileName)
  {
    try
    {
      int pos = fileName.lastIndexOf('.');
      if (pos < 0)
      {
        return "";
      }
      return fileName.substring(pos + 1);
    }
    catch (Exception e)
    {
      return "";
    }
  }

  public String getDocumentCSSIconClass(String fileName)
  {
    String iconClass = null;
    if (!GenericValidator.isBlankOrNull(fileName))
    {
      iconClass = MAP_EXTENSIONS_TO_CSS.get(getFileExtension(fileName, false));
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

  public String getMimeType(String fileName)
  {
    return MIMETYPESFILETYPE.getContentType(fileName);
  }

  public String getSafeSubmittedFileName(String originalFilename)
  {
    try
    {
      String fileName = originalFilename.replace("\\", "/");
      int pos = fileName.lastIndexOf('/');
      if (pos < 0)
      {
        return originalFilename;
      }
      return fileName.substring(pos);
    }
    catch (Exception e)
    {
      return originalFilename;
    }
  }

  public String getFileName(String originalFilename)
  {
    try
    {
      String fileName = originalFilename.replace("\\", "/");
      int pos = fileName.lastIndexOf('/');
      if (pos < 0)
      {
        return originalFilename;
      }
      return fileName.substring(pos);
    }
    catch (Exception e)
    {
      return originalFilename;
    }
  }

}
