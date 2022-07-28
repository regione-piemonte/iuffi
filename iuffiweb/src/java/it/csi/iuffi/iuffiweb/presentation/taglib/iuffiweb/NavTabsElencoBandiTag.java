package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;

public class NavTabsElencoBandiTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4558965659815071438L;

  public enum TABS
  {
    GRAFICI, REPORT, GRADUATORIE
  };

  private String activeTab;

  @Override
  public int doEndTag() throws JspException
  {
    JspWriter writer = this.pageContext.getOut();
    try
    {
      BandoDTO bando = (BandoDTO) this.pageContext.getSession()
          .getAttribute("idBando");
      writer.write("<div class=\"container-fluid\">");
      if (bando != null)
      {
        writer.write("<div class=\"alert alert-info\" role=\"alert\">Bando: <b>"
            + bando.getDenominazione() + "</b></div>");
      }
      writer.write("<ul class=\"nav nav-tabs\">");

      if (bando.isHaveReport())
      {
        writer.write("<li role=\"presentation\" "
            + (TABS.REPORT.toString().equals(activeTab) ? "class=\"active\""
                : "")
            + "><a href=\"" + (TABS.REPORT.toString().equals(activeTab) ? "#"
                : "../reportistica/elencoreport_" + bando.getIdBando() + ".do")
            + "\" style=\"text-decoration: none;\">Reportistica</a></li>");
      }
      if (bando.isHaveChart())
      {
        writer.write("<li role=\"presentation\" "
            + (TABS.GRAFICI.toString().equals(activeTab) ? "class=\"active\""
                : "")
            + "><a href=\"" + (TABS.GRAFICI.toString().equals(activeTab) ? "#"
                : "../reportistica/mainreport_" + bando.getIdBando() + ".do")
            + "\" style=\"text-decoration: none;\">Grafici statistici</a></li>");
      }
      if (bando.isHaveGraduatorie())
      {
        writer.write("<li role=\"presentation\" "
            + (TABS.GRADUATORIE.toString().equals(activeTab)
                ? "class=\"active\"" : "")
            + "><a href=\""
            + (TABS.GRADUATORIE.toString().equals(activeTab) ? "#"
                : "../elencoBandi/elencograduatorie_" + bando.getIdBando()
                    + ".do")
            + "\" style=\"text-decoration: none;\">Graduatorie</a></li>");
      }

      writer.write("</ul>");
      writer.write("</div>");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return super.doEndTag();
  }

  public String getActiveTab()
  {
    return activeTab;
  }

  public void setActiveTab(String activeTab)
  {
    this.activeTab = activeTab;
  }

}
