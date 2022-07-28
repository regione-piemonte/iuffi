package it.csi.iuffi.iuffiweb.presentation.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;

import it.csi.iuffi.iuffiweb.exception.InterceptorException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public abstract class BaseManager
{
  public enum Return
  {
    CONTINUE_TO_NEXT_MANAGER, SKIP_MANAGERS_AND_GO_TO_CONTROLLER, SKIP_ALL_MANAGER_AND_CONTROLLER
  }

  protected static final Logger logger = Logger
      .getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".presentation");

  public abstract Return validate(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception;

  public static void errorPage(String title, String message,
      HttpServletRequest request, HttpServletResponse response)
      throws InterceptorException
  {
    request.setAttribute("titolo", title);
    request.setAttribute("message", message);
    forward(IuffiConstants.PAGE.JSP.ERROR.INTERNAL_ERROR, request, response);
  }

  public static void forward(String url, HttpServletRequest request,
      HttpServletResponse response) throws InterceptorException
  {
    try
    {
      request.getRequestDispatcher(url).forward(request, response);
    }
    catch (Exception e)
    {
      throw new InterceptorException("Errore nel forward verso l'url " + url,
          e);
    }
  }

  public static Annotation findHandlerAnnotation(
      Class<? extends Annotation> annotationClass, Object handler)
  {
    Annotation annotation = null;
    if (handler instanceof HandlerMethod)
    {
      HandlerMethod h = (HandlerMethod) handler;
      annotation = h.getMethod().getAnnotation(annotationClass);
      if (annotation == null)
      {
        annotation = h.getBeanType().getAnnotation(annotationClass);
      }
    }
    return annotation;
  }
}
