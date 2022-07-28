package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.util.Iterator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;



public class MySelectChoice extends MySelect
{
	/** serialVersionUID */
	private static final long serialVersionUID = -5412221111769893273L;
	private Object listChoice;
	private Object selectedChoice;
    String valueMethod = "getId";
    String textMethod = "getDescrizione";
    String codeMethod = "getCodice";
    String defaultSelected=null;
    private final static String DEFAULT_DATA_CHOICE = "";
    private static final String CHOICE = "_choice";

	public MySelectChoice() {
		super("form-control","display:block !important; border-top-left-radius: 4px; border-bottom-left-radius: 4px;");
	}

	
	
	@Override
	protected void writeCustomTag(StringBuilder sb, String errorMessage) throws Exception {
		this.pageContext.getRequest().removeAttribute(MY_SELECT_CHOICE_SELECTED_VALUE);
		super.writeCustomTag(sb, errorMessage);
		String mySelectChoiceSelectedValue = IuffiUtils.STRING.nvl(getTmpSelectedChoice(), DEFAULT_DATA_CHOICE);
		String idElementChoice = getId() +"_span_choice_" + mySelectChoiceSelectedValue;
		sb.append("<script type=\"text/javascript\">");
		sb.append("document.getElementById('" + idElementChoice + "').style=\"margin-right:5px;\"	\n");
		sb.append("document.getElementById('" + idElementChoice + "').className=\"glyphicon glyphicon-ok-circle\"	\n");
	    sb.append("</script>");
	}
	
	@Override
	protected void writeCustomTag(StringBuilder sb, String errorMessage, boolean wrappedInAGroup) throws Exception {
		super.writeCustomTag(sb, errorMessage, true);
		String mySelectChoiceSelectedValue = IuffiUtils.STRING.nvl(getTmpSelectedChoice(), DEFAULT_DATA_CHOICE);
	    sb.append("<script type=\"text/javascript\">");
	    sb.append("		var htmlSelect"+getId()+" = document.getElementById('" + getId() + "').innerHTML; 								\n");
	    if(mySelectChoiceSelectedValue != null && !mySelectChoiceSelectedValue.equals(DEFAULT_DATA_CHOICE))
	    {
	    	sb.append(
					  "     var htmlPagina = htmlSelect" + getId() +";																		\n"
					+ "		htmlPagina = htmlPagina.replace(/<option(.(?!data-code=[\"']" + mySelectChoiceSelectedValue + "[\"']))*>/gm, \"\"); 		\n"
					+ "		var optionDefault = '<option data-code=\"CODE-DEFAULT\" value=\"\">-- selezionare --</option>';     \n"
					+ "		htmlPagina = optionDefault + htmlPagina; 															\n"
					+ "		document.getElementById('" + getId() + "').innerHTML = htmlPagina; 									\n"
    			);
	    }
	    sb.append("</script>");
	}
	
	
	public void prepareInputGroup() throws Exception
	{
		
		this.addAddOn(true, null);
		addAddOn(false,MyInputAddOn.getAddOnButtonClass(
			"<button type=\"button\" class=\"btn btn-default dropdown-toggle"
			+ "\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">"
			+ "<span class=\"glyphicon glyphicon-filter\"></span>"
			+ "</button>"
			+ getContentChoice()
		));
		
	}
	
	/**
	 * 
	 * @return null if no choices are selected, the code in the form of a String otherwise
	 */
	private Object getTmpSelectedChoice()
	{
    	Object tmpSelectedChoice = null;
		if(Boolean.TRUE.equals(preferRequestValues) && getChoiceValueFromRequest()!=null) //se sto arrivando da una request fallita usa il filtro in request
    	{
    		tmpSelectedChoice = getChoiceValueFromRequest();
    	}
    	else //se non arriva da una request fallita usa il dato originale del filtro (quello corrispondente al funzionario selezionato)
    	{
    		tmpSelectedChoice = (String)this.pageContext.getRequest().getAttribute(MY_SELECT_CHOICE_SELECTED_VALUE);
    		if(tmpSelectedChoice == null)
    		{
    			tmpSelectedChoice = selectedChoice;
    		}
    	}
		return tmpSelectedChoice;
	}
	
	@Override
	public String getChoiceValueFromRequest()
	{
		return this.pageContext.getRequest().getParameter(name + CHOICE);
	}
	
	protected String getContentChoice() throws Exception
	{
		StringBuilder sb = new StringBuilder();  
		Object tmpSelectedChoice = getTmpSelectedChoice();
		String nameChoice = getName() + CHOICE;
		sb.append("<input type=\"hidden\" id=\"" + nameChoice + "\" name=\"" + nameChoice + "\" value=\"" + (tmpSelectedChoice==null?"":tmpSelectedChoice) + "\" />");
		sb.append("<ul class=\"dropdown-menu dropdown-menu-right\">\r\n");  

	    if (listChoice != null)
	    {
	      if (listChoice instanceof Iterable)
	      {
	    	Iterator<?> iterator = ((Iterable<?>) listChoice).iterator();
	        while (iterator.hasNext())
	        {
	          processFilterOption(sb, iterator.next(), valueMethod, textMethod, tmpSelectedChoice);
	        }
	      }
	      else
	      {
	        if (listChoice.getClass().isArray())
	        {
	          Object[] array = (Object[]) listChoice;
	          for (Object option : array)
	          {
	            processFilterOption(sb, option, valueMethod, textMethod, tmpSelectedChoice);
	          }
	        }
	      }
	    }
		
		sb.append("</ul>");
		return sb.toString();
	}
	
	private void processFilterOption(StringBuilder sb, Object next, String valueMethod, String textMethod2,
			Object defaultChoice) throws Exception

	{
		String currentValue = getProperty(next, valueMethod);
		String currentId = "'" + getId() + "'";
		String textShown = getProperty(next, textMethod);
		String currentValueFormatted = currentValue == null || currentValue.equals("") ? "''" : "'" + currentValue + "'";
		boolean selectedItem = false;
		if(defaultChoice !=null)
		{
			selectedItem = (defaultChoice.equals(currentValue) == true);
		}
		else if(defaultChoice == null && (currentValue == null || currentValue.equals(""))) //se non vi è nulla di default (null) oppure ho inserito una entry non esistente seleziona l'elemento che permette di visualizzare tutta la lista
		{
			selectedItem = true;
		}
		
		if (selectedItem)
		{
			defaultSelected = (String) defaultChoice;
		}
		sb.append("<li><a onclick=\"showOptionSelectChoice(" + currentId + "," + currentValueFormatted + ", htmlSelect" + getId() +")\""
				+ " id=\"" + getId() + "_a_choice_" + currentValue + "\""
				+ " name=\"" + getId() + "_a_choice\""
				+ ">" 
				+ " <span id=\"" + getId()+ "_span_choice_" + currentValue + "\" style=\"margin-right:19px;\" aria-hidden=\"true\"></span> " //14px + 5px
				+ textShown 
				+ "</a>"
				+ "</li>\r\n");
	}

	public Object getListChoice()
	{
		return listChoice;
	}

	public void setListChoice(Object listChoice)
	{
		this.listChoice = listChoice;
	}

	@Override
	public Object getSelectedChoice()
	{
		return selectedChoice;
	}

	public void setSelectedChoice(Object selectedChoice)
	{
		this.selectedChoice = selectedChoice;
	}
	

}
