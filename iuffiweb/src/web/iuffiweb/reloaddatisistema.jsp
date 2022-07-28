<%@page import="it.csi.iuffi.iuffiweb.util.IuffiConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.net.InetAddress"%>
<%@page import="it.csi.iuffi.iuffiweb.util.IuffiUtils"%>
<%@page import="it.csi.iuffi.iuffiweb.util.IuffiApplication"%>
<%
	Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".presentation");
	String host = InetAddress.getLocalHost().getHostName();
	ResourceBundle res = ResourceBundle.getBundle("config");
	String servers= res.getString("iuffi.gestione.servers");
	logger.debug("["+host+"] :: RICHIAMATA RELOAD.JSP"); 
	
	if(servers!= null && servers.indexOf(host)>0)
	{		
		IuffiUtils.APPLICATION.reloadCDU();
		logger.info("["+host+"] :: AGGIORNATO ELENCO CDU");	
		IuffiUtils.APPLICATION.loadSyncSegnapostiStampe();
		logger.info("["+host+"] :: AGGIORNATO SEGNAPOSTO STAMPE");	
	}
	
%>
