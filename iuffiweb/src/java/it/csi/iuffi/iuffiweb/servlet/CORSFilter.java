package it.csi.iuffi.iuffiweb.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class CORSFilter implements Filter {

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    
    logger.debug("********** doFilter start **********");
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    //httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8100");
    httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, HEAD");
    //httpResponse.setHeader("Access-Control-Allow-Headers", "X-Requested-With, X-Auth-Token");
    httpResponse.setHeader("Access-Control-Allow-Headers", "*");
    httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    logger.debug(httpRequest.getRequestURI());
    if (httpRequest.getMethod().equalsIgnoreCase("GET")) {
      httpResponse.setContentType("image/png");
    }
 // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
/*    if (httpRequest.getMethod().equals("OPTIONS")) {
      httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
      return;
    }*/
    chain.doFilter(httpRequest, httpResponse);
    logger.debug("********** doFilter end **********");
  }

  @Override
  public void destroy() {

  }

}