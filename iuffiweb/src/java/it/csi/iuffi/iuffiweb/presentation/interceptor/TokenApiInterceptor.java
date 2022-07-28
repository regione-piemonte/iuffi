package it.csi.iuffi.iuffiweb.presentation.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.TokenUtils;

public class TokenApiInterceptor implements HandlerInterceptor
{
  public List<BaseManager>      managers = new ArrayList<BaseManager>();

  protected static final Logger logger   = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".presentation");

  public TokenApiInterceptor()
  {
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
      HttpServletResponse response, Object handler, Exception ex)
      throws Exception
  {
    //logger.debug("afterCompletion");
  }

  @Override
  public void postHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler, ModelAndView modelAndView)
      throws Exception
  {
    //logger.debug("postHandle");
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    if (request.getRequestURI().indexOf("/rest/") > -1) {
      if (request.getRequestURI().indexOf("/proxywms") > -1)
        return true;
      // verifico il token
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      try {
        String cf = TokenUtils.verifyToken(jwt);
        return true;
      }
      catch (Exception e) {
        logger.error("Errore nella verifica del token: " + e.getMessage());
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        return false;
      }
    }
    else
      return true;
  }

}
