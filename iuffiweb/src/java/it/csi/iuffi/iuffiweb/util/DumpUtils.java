package it.csi.iuffi.iuffiweb.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public abstract class DumpUtils
{
  public static final int MAX_RECURSION           = 5;
  public static final int MAX_EXCEPTION_RECURSION = 1;

  public static void logInternalUnexpectedException(Logger logger,
      InternalUnexpectedException e, String logHeader)
  {
    logGenericException(logger, e.getQuery(), e.getCause(), e.getParameters(),
        e.getVariables(), logHeader);
  }

  private static final String semaphore       = "ID";
  static public long          lastExceptionID = 0;

  public static void logGenericException(Logger logger, String query,
      Throwable e, LogParameter[] parameters, LogVariable variables[],
      String logHeader)
  {
    String xmlParameters = dump(parameters, "Parametri");
    String xmlVariables = dump(variables, "Variabili");
    logGenericException(logger, query, e, xmlParameters, xmlVariables,
        logHeader);
  }

  public static void logGenericException(Logger logger, String query,
      Throwable e, String xmlParameters, String xmlVariables, String logHeader)
  {
    long id = getUniqueId();
    logHeader += " [ID = " + id + "] ";
    logger.error(logHeader
        + " *********************************** INIZIO DUMP ECCEZIONE  ***********************************");
    logger.error(logHeader + " Query:\n" + query + "\n");
    logger.error(logHeader + " Parametri del metodo:\n" + xmlParameters + "\n");
    logger.error(logHeader + " Variabili del metodo:\n" + xmlVariables + "\n");
    logger
        .error(logHeader + " Stacktrace:\n" + getExceptionStackTrace(e) + "\n");
    logger.error(logHeader
        + " ************************************ FINE DUMP ECCEZIONE *************************************");
  }

  private static long getUniqueId()
  {
    synchronized (semaphore)
    {
      return ++lastExceptionID;
    }
  }

  public static String dump(Object obj, String name)
  {
    try
    {
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
      // XMLStreamWriter writer = new
      // IndentingXMLStreamWriter(factory.createXMLStreamWriter(xmlOutputStream));
      XMLStreamWriter writer = factory.createXMLStreamWriter(xmlOutputStream);
      writer.writeStartDocument();
      dump(0, writer, obj, name);
      writer.writeEndDocument();
      writer.close();
      return xmlOutputStream.toString();
    }
    catch (Exception e)
    {
      return "<" + name
          + ">Eccezione nella generazione dell'xml di dump dell'oggetto " + name
          + "<" + name + ">";
    }
  }

  public static void dump(int recursionDepth, XMLStreamWriter writer,
      Object obj, String name) throws XMLStreamException
  {
    if (recursionDepth > MAX_RECURSION)
    {
      writeMaxRecursionException(writer);
      return;
    }
    if (obj == null || obj instanceof ILoggable)
    {
      // Non incremento il recursionDepth in quanto sto scrivendo l'oggetto
      // corrente (forzato ad un altro tipo)
      dump(recursionDepth, writer, (ILoggable) obj, name);
    }
    else
    {
      Class<?> objClass = obj.getClass();
      if (isPrimitive(obj))
      {
        writePrimitive(writer, obj, name);
      }
      else
      {
        if (objClass.isArray())
        {
          if (obj instanceof byte[])
          {
            // Array generico di byte ==> potrebbe essere un blob == devo
            // valutare se abbia senso scriverlo
            writer.writeStartElement(name);
            byte[] bytes = (byte[]) obj;
            final int MAX_RAW_BYTE_ARRAY_SIZE = 40 * 1024; // 40 KB
            writer.writeAttribute("array_length", String.valueOf(bytes.length));
            if (bytes.length > MAX_RAW_BYTE_ARRAY_SIZE)
            {
              writer.writeCharacters(
                  "ATTENZIONE: Array di byte con dimensione superiore a "
                      + MAX_RAW_BYTE_ARRAY_SIZE
                      + " (possibile blob o file), dimensione troppo grande per il dump!");
            }
            else
            {
              writer.writeCharacters(String.valueOf(bytes));
            }
            writer.writeEndElement();
          }
          else
          {
            int index = 0;
            Object[] array = (Object[]) obj;
            writer.writeStartElement(name);
            writer.writeAttribute("array_length", String.valueOf(array.length));
            for (Object element : array)
            {
              dump(recursionDepth + 1, writer, element, name + (index++));
            }
            writer.writeEndElement();
          }
        }
        else
        {
          if (obj instanceof Iterable)
          {
            int index = 0;
            Iterable<?> iterable = (Iterable<?>) obj;
            Iterator<?> iterator = iterable.iterator();
            writer.writeStartElement(name);
            while (iterator.hasNext())
            {
              dump(recursionDepth + 1, writer, iterator.next(),
                  name + (index++));
            }
            writer.writeEndElement();
          }
          else
          {
            String value = null;
            try
            {
              value = obj.toString();
            }
            catch (Exception e)
            {
              value = "toString() Error! Stacktrace:\n" + e.toString();
            }
            writePrimitive(writer, value, name);
          }
        }
      }
    }
  }

  public static String getStackTraceAsString(Throwable e)
  {

    String result = null;
    try
    {
      if (e != null)
      {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.flush();
        result = stringWriter.toString();
      }
    }
    catch (Exception t)
    {
      result = "ERRORE getStackTraceAsString()";
    }
    return result;
  }

  private static boolean isPrimitive(Object obj)
  {
    return obj instanceof Number || obj instanceof CharSequence
        || obj instanceof java.util.Date || obj instanceof Boolean;
  }

  private static void writeMaxRecursionException(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement("MaxRecursionException");
    writer.writeEndElement();
  }

  public static void dump(int recursionDepth, XMLStreamWriter writer,
      ILoggable obj, String name) throws XMLStreamException
  {
    if (recursionDepth > MAX_RECURSION)
    {
      writeMaxRecursionException(writer);
      return;
    }
    writer.writeStartElement(name);
    if (obj == null)
    {
      writer.writeAttribute("null", "true");
    }
    else
    {
      for (Method method : getMethods(obj.getClass()))
      {
        Object value = getMethodResult(method, obj);
        dump(recursionDepth + 1, writer, value, method.getName().substring(3));
      }
    }
    writer.writeEndElement();
  }

  private static ArrayList<Method> getMethods(Class<?> objClass)
  {
    ArrayList<Method> list = new ArrayList<Method>();
    Method[] methods = objClass.getMethods();
    if (methods != null)
    {
      Class<?> parameters[] = null;
      for (Method method : methods)
      {
        parameters = method.getParameterTypes();
        int size = parameters == null ? 0 : parameters.length;
        if (size == 0)
        {
          String name = method.getName();
          if (name.startsWith("get") && name.length() > 3)
          {
            list.add(method);
          }
        }
      }
    }
    return list;
  }

  private static void writePrimitive(XMLStreamWriter writer, Object value,
      String name) throws XMLStreamException
  {
    writer.writeStartElement(name);
    try
    {
      if (value == null)
      {
        writer.writeCharacters("null");
      }
      else
      {
        writer.writeCharacters(value.toString());
      }
    }
    catch (Exception e)
    {
      writer.writeCharacters(e.toString());
    }
    writer.writeEndElement();
  }

  public static Object getMethodResult(Method m, Object obj)
  {
    try
    {
      return m.invoke(obj);
    }
    catch (Throwable e)
    {
      return "Reflection Error! Stacktrace:\n" + e.toString();
    }
  }

  public static String getExceptionStackTrace(Throwable cause)
  {
    if (cause == null)
    {
      return "ERROR: No StackTrace Available! See logs for detail (exception IS null)";
    }
    StringBuilder sbStackTrace = new StringBuilder();
    int countRecursion = 0;
    while (cause != null && countRecursion <= MAX_EXCEPTION_RECURSION)
    {
      if (sbStackTrace.length() > 0)
      {
        sbStackTrace.append("\n\n Caused by:\n\n");
      }
      sbStackTrace.append(getStackTraceAsString(cause));
      countRecursion++;
      cause = cause.getCause();
    }

    return sbStackTrace.toString();
  }

}
