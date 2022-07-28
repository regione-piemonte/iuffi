package it.csi.iuffi.iuffiweb.presentation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.dto.internal.MailAttachment;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.DumpUtils;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/error")
@IuffiSecurity(value = "N/A", controllo = IuffiSecurity.Controllo.NESSUNO)
@NoLoginRequired
public class ExceptionController extends BaseController
{
  private static final String SESSION_PARAM_EXCEPTION = "SESSION_PARAM_EXCEPTION";

  @RequestMapping(value = "notfound")
  public String notFound(ModelMap model, HttpServletRequest request)
      throws InternalServiceException
  {
    // Gestisce gli errori 404
    logger.error("[ExceptionController.notFound] Pagina non trovata "
        + request.getRequestURL());
    logger.error("[ExceptionController.notFound] Error request uri "
        + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
    logger.error("[ExceptionController.notFound] Request uri "
        + request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));
    logger.error("[ExceptionController.notFound] Servlet path "
        + request.getAttribute(RequestDispatcher.FORWARD_SERVLET_PATH));
    Enumeration<String> names = request.getAttributeNames();
    while (names.hasMoreElements())
    {
      final String nextElement = names.nextElement();
      System.out.print("attribute[" + nextElement + "]=	"
          + request.getAttribute(nextElement));
    }
    model.put("titolo", "Si e' verificato un errore");
    model.put("messaggio", "Pagina non trovata");
    return "errore/erroreInterno";
  }

  @RequestMapping(value = "internalerror")
  public String internalError(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalServiceException
  {
    logger.error("[ExceptionController.internalError] Error request uri "
        + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
    logger.error("[ExceptionController.internalError] Request uri "
        + request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));
    logger.error("[ExceptionController.internalError] Servlet path "
        + request.getAttribute(RequestDispatcher.FORWARD_SERVLET_PATH));
    // Gestisce gli errori 500
    Throwable exception = (Throwable) request
        .getAttribute(RequestDispatcher.ERROR_EXCEPTION);// "javax.servlet.error.exception");
    if (exception instanceof InternalUnexpectedException)
    {
      InternalUnexpectedException unexpected = (InternalUnexpectedException) exception;
      DumpUtils.logInternalUnexpectedException(logger, unexpected,
          "[ExceptionController::internalError] ");
    }
    else
    {
      logger.error(
          "[ExceptionController::internalError] Si è verificato un errore nell'accesso alla pagina "
              + request.getRequestURL(),
          exception);
    }
    logger.error("[ExceptionController::internalError] "
        + dumpRequestAndSession(request));
    session.removeAttribute(SESSION_PARAM_EXCEPTION);
    session.setAttribute(SESSION_PARAM_EXCEPTION, exception.getCause());
    model.put("titolo", "Si e' verificato un errore");
    if (exception instanceof ApplicationException)
    {
      model.put("messaggio", ((ApplicationException) exception).getMessage());
    }
    else
    {
      Throwable cause = exception.getCause();
      if (cause instanceof ApplicationException)
      {
        model.put("messaggio", ((ApplicationException) cause).getMessage());
      }
      else
      {
        model.put("messaggio", "Si è verificato un errore interno del server");
        model.put("manage_mail", "manage_mail");
      }
    }
    return "errore/erroreInterno";
  }

  protected String dumpRequestAndSession(HttpServletRequest request)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "\n\n***************************** PARAMETRI PASSATI IN REQUEST *****************************\n\n");
    Map<String, String[]> parameters = request.getParameterMap();
    if (parameters != null)
    {
      Set<String> keys = parameters.keySet();
      if (keys != null)
      {
        for (String key : keys)
        {
          sb.append("\n").append(DumpUtils.dump(parameters.get(key), key));
        }
      }
    }
    sb.append(
        "\n\n********************************* ATTRIBUTI IN REQUEST *********************************\n\n");
    Enumeration<String> names = request.getAttributeNames();
    if (names != null)
    {
      while (names.hasMoreElements())
      {
        String name = names.nextElement();
        if (name != null && !name.startsWith("org.springframework"))
        {
          sb.append("\n")
              .append(DumpUtils.dump(request.getAttribute(name), name));
        }
      }
    }
    sb.append(
        "\n\n********************************* ATTRIBUTI IN SESSION *********************************\n\n");
    final HttpSession session = request.getSession();
    names = session.getAttributeNames();
    if (names != null)
    {
      while (names.hasMoreElements())
      {
        String name = names.nextElement();
        if (name != null && !name.startsWith("org.springframework"))
        {
          sb.append("\n")
              .append(DumpUtils.dump(session.getAttribute(name), name));
        }
      }
    }
    sb.append("\n");
    return sb.toString();
  }

  @RequestMapping(value = "inviamail")
  public String inviaMil(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    // Invia la mail di segnalazione
    Exception genericException = (Exception) session
        .getAttribute(SESSION_PARAM_EXCEPTION);
    InternalUnexpectedException exception = null;
    if (!(genericException instanceof InternalUnexpectedException))
    {
      exception = new InternalUnexpectedException(exception);
    }
    else
    {
      exception = (InternalUnexpectedException) genericException;
    }
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    StringBuffer message = new StringBuffer();
    message.append("Invio segnalazione di errore ");
    message.append("\nProdotto: IUFFI Pratiche");
    message.append("\n\nDati utente: \n");
    message.append(utente.getCognome()).append(" ").append(utente.getNome())
        .append(" (")
        .append(IuffiUtils.PAPUASERV.getDenominazioneUtente(utente))
        .append(") \n");
    message.append(utente.getCodiceFiscale()).append("\n\n");
    if (utente.getEnteAppartenenza() != null)
    {
      message.append("Ente: ").append(IuffiUtils.PAPUASERV
          .getDenominazioneEnte(utente.getEnteAppartenenza()));
    }

    message.append("\n\nErrore: " + exception.getMessage());
    message.append("\nQuery: " + exception.getQuery());

    try
    {
      ByteArrayOutputStream fos = new ByteArrayOutputStream();
      ZipOutputStream zos = new ZipOutputStream(fos);

      // Preparo file di testo con l'eccezione
      ByteArrayOutputStream fosTemp = new ByteArrayOutputStream();
      PrintStream printFile = new PrintStream(fosTemp);
      printFile.println(exception.getExceptionStackTrace());
      printFile.println("\n\nCodice di errore riportato dall'eccezione: "
          + exception.getErrorCode());
      printFile.println(
          "\n\n***************************** PARAMETRI PASSATI AL METODO *****************************\n\n");
      printFile.println(exception.getParameters());
      printFile.println(
          "\n\n******************************** VARIABILI DEL METODO  ********************************\n\n");
      printFile.println(exception.getVariables());
      printFile.println(
          "\n\n***************************************************************************************\n\n");

      addToZipFile(fosTemp, "Eccezione.txt", zos);
      zos.close();
      fos.close();
      fosTemp.close();

      MailAttachment[] attachments = new MailAttachment[1];
      attachments[0] = new MailAttachment();
      attachments[0].setAttach(fos.toByteArray());
      attachments[0].setFileName("Eccezione.zip");
      attachments[0].setFileType("application/zip");

      IuffiUtils.MAIL.postMail("<EMAIL_ADDRESS>",
          new String[]
          { "<EMAIL_ADDRESS>" },
          null,
          "Invio segnalazione IUFFI - Pratiche",
          message.toString(), attachments);

    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    session.removeAttribute(SESSION_PARAM_EXCEPTION);
    model.put("esito_mail", "Segnalazione inviata correttamente");
    model.put("manage_mail", "manage_mail");
    return "errore/erroreInterno";
  }

  private void addToZipFile(ByteArrayOutputStream os, String fileName,
      ZipOutputStream zos) throws FileNotFoundException, IOException
  {
    ZipEntry zipEntry = new ZipEntry(fileName);
    zos.putNextEntry(zipEntry);
    byte[] bytes = os.toByteArray();
    zos.write(bytes, 0, bytes.length);
    zos.closeEntry();
  }
}
