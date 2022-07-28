package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class WizardPianoGraficoTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4558965659815071438L;

  public enum STEP
  {
    ASSENTE, CREATO, GENERAZIONE_IN_CORSO, IMPORTATO, CONSOLIDATO
  };

  private String activeStep;

  @Override
  public int doEndTag() throws JspException
  {
    JspWriter writer = this.pageContext.getOut();
    try
    {

      // (TABS.REPORT.toString().equals(activeTab)

      writer.write("<div class=\"container-fluid\">");
      writer.write(
          " <div class=\"col-sm-12\" >                                                                  \n"
              + "         <div class=\"stepwizard\">                                                          \n"
              + "             <div class=\"stepwizard-row setup-panel\">                                      \n"
              + "               <div class=\"stepwizard-step\">                                               \n"
              + "                 <a href=\"#step-1\" onclick=\"return false;\" type=\"button\" class=\""
              + getClassType(STEP.ASSENTE) + "\">1</a>  \n"
              + "                 <p>Domanda grafica assente</p>                                              \n"
              + "               </div>                                                                        \n"
              + "               <div class=\"stepwizard-step\">                                               \n"
              + "                 <a href=\"#step-2\" onclick=\"return false;\" type=\"button\" class=\""
              + getClassType(STEP.GENERAZIONE_IN_CORSO) + "\" >2</a> \n"
              + "                 <p>Domanda grafica in fase di creazione</p>                                               \n"
              + "               </div>                                                                        \n"

              + "               <div class=\"stepwizard-step\">                                               \n"
              + "                 <a href=\"#step-2\" onclick=\"return false;\" type=\"button\" class=\""
              + getClassType(STEP.CREATO) + "\" >3</a> \n"
              + "                 <p>Domanda grafica creata</p>                                               \n"
              + "               </div>                                                                        \n"
              + "               <div class=\"stepwizard-step\">                                               \n"
              + "                 <a href=\"#step-3\" onclick=\"return false;\" type=\"button\" class=\""
              + getClassType(STEP.IMPORTATO) + "\" >4</a> \n"
              + "                 <p>Domanda grafica importata</p>                                            \n"
              + "               </div>                                                                        \n"
              + "             </div>                                                                        	\n"
              + "         </div>                                                                            	\n"
              + "       </div>                                                                              	\n");

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return super.doEndTag();
  }

  public String getClassType(STEP currentStep)
  {
    return (this.activeStep.equals(currentStep.toString())
        ? "btn btn-primary btn-circle" : "btn btn-default btn-circle");
  }

  public String getActiveStep()
  {
    return activeStep;
  }

  public void setActiveStep(String activeStep)
  {
    this.activeStep = activeStep;
  }

}
