package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap.MyBodyTag;

/**
 * 
 * @author nicolo.mandrile
 * TagLib per lo step wizard. Versione base base base. 
 * Bisogna passare il numero di steps e una stringa con i nomi dei vari step
 * separati da "&&&". 
 * L'attributo "activeStep" riceve un int e indica qual è lo step corrente.
 * Il parametro "type" riceve "square" or "circle". E' facoltativo, di default
 * è "circle".
 */
public class MyStepWizard extends MyBodyTag {
	/** serialVersionUID */
	private static final long serialVersionUID = 92816793350454424L;
	protected Integer numberOfSteps;
	protected String labels;
	protected String type ="circle";
	protected Integer activeStep;


	@Override
  public void writeCustomTag(StringBuilder sb, String errorMessage) throws Exception
  {
	  String[] labelsArray = labels.split("&&&");
	  if(labelsArray.length!=numberOfSteps.intValue())
		  throw new Exception("Il numero di step è diverso dal numero di argomenti passati.");
	  
	  sb.append("<div class=\"col-sm-12\">");
	  sb.append("<div class=\"col-sm-10\">");
	  sb.append("<div class=\"stepwizard col-md-offset-1\">");
	  sb.append("<div class=\"stepwizard-row setup-panel\">");
	  for(int i=1; i <= numberOfSteps; i++)
	  {
		  String label = labelsArray[i-1];
		 
		  if(!"circle".equals(type) && !"square".equals(type))
			  type="circle";
		  
		  sb.append("<div class=\"stepwizard-step\" style=\"width: 100/"+numberOfSteps+"%;\">");
		  if(activeStep.intValue()==i)
			  sb.append("<a href=\"#step-"+i+"\" type=\"button\" class=\"btn btn-primary btn-"+type+"\">"+i+"</a>");
		  else
			  sb.append("<a href=\"#step-"+i+"\" type=\"button\" class=\"btn btn-default btn-"+type+"\">"+i+"</a>");

		  sb.append("<p>" + label + "</p>");
		  sb.append("</div>");
	  }
  

    sb.append("</div></div></div></div>");
  }


	public Integer getNumberOfSteps() {
		return numberOfSteps;
	}

	public void setNumberOfSteps(Integer numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public MyStepWizard() {
		super("form-control");
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getActiveStep() {
		return activeStep;
	}


	public void setActiveStep(Integer activeStep) {
		this.activeStep = activeStep;
	}

}