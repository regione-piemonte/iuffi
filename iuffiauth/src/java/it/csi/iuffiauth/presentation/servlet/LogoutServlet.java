package it.csi.iuffiauth.presentation.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.csi.iuffiauth.util.IuffiauthConstants.LOGGING;

public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 3221226707085723730L;
	private static final String THIS_CLASS = LogoutServlet.class.getSimpleName();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws IOException, ServletException
	{
	    final String THIS_METHOD = "[" + THIS_CLASS + ".doGet()] ";
    	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + "Redirect to logout page");
    	resp.setContentType("text/html");  
    	PrintWriter pw=resp.getWriter();  
    	  
    	resp.sendRedirect("/ssp_liv1_sisp_liv1_spid_GASPRP_AGRIC/logout.do");
    	// TODO: implementare logout per utenti PA
    	  
    	pw.close();  
    }
}
