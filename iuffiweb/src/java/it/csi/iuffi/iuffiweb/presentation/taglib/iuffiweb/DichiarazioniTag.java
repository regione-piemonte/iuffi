package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.DettaglioInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.ValoriInseritiDTO;
import it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap.TextArea;
import it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap.TextField;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public class DichiarazioniTag extends BaseTag
{
  /** serialVersionUID */
  protected static final long serialVersionUID = -7620410490326047187L;
  public Boolean              isModifica;

  public int doEndTag() throws JspException
  {
    StringBuilder buffer = new StringBuilder();
    List<GruppoInfoDTO> dichiarazioni = getGruppiInfo();

    try
    {
      createHtml(dichiarazioni, buffer);
      this.pageContext.getOut().write(buffer.toString());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new JspException(e);
    }

    return super.doEndTag();
  }

  @SuppressWarnings("unchecked")
  protected List<GruppoInfoDTO> getGruppiInfo()
  {
    return (List<GruppoInfoDTO>) getELVariableValue("dichiarazioni");
  }

  protected void createHtml(List<GruppoInfoDTO> gruppiInfo,
      StringBuilder buffer) throws Exception
  {
    @SuppressWarnings("unchecked")
    Map<String, String> errors = (Map<String, String>) getELVariableValue(
        "errors");

    buffer.append(
        "<table summary=\"Dichiarazioni\" class=\"myovertable table table-hover table-condensed table-bordered\"> 	\n"
            // +" <colgroup><col width=\"2%\" /><col width=\"95%\" /></colgroup>
            // \n"
            + "	<tbody>																									\n");
    for (int x = 0; x < gruppiInfo.size(); x++)
    {
      GruppoInfoDTO info = gruppiInfo.get(x);
      buffer.append(
          "<tr> 																												\n"
              + "	<th colspan=\"2\" 																								\n"
              + " 	style=\"background-color: #DDDDDD; color: #000000; text-align: left;font-weight:bold\">		\n"
              + "		" + info.getDescrizione()
              + "							 														\n"
              + "	</th> 																											\n"
              + "</tr> 																												\n");
      for (int i = 0; i < info.getDettaglioInfo().size(); i++)
      {
        buffer.append(" <tr> \n");
        if ((i < (info.getDettaglioInfo().size() - 1))
            || (x <= (gruppiInfo.size() - 1)))
        {
          buffer.append(" <th style=\"vertical-align:middle\">"
              + getInputCheck(info.getDettaglioInfo().get(i), errors)
              + "</th>   \n"
              + "<td style=\"vertical-align:middle\">"
              + sostituisciPlaceHolder(info.getDettaglioInfo().get(i), errors)
              + "</td> \n");
        }
        else
        {
          buffer.append(" <th style=\"vertical-align:middle;\">"
              + getInputCheck(info.getDettaglioInfo().get(i), errors)
              + "</th>   \n"
              + " <td style=\"vertical-align:middle;\">"
              + sostituisciPlaceHolder(info.getDettaglioInfo().get(i), errors)
              + "</td> \n");
        }
        buffer.append(" </tr> ");

      }
    }
    buffer.append(" </tbody> \n"
        + " </table>\n");
  }

  protected String getInputCheck(DettaglioInfoDTO dettaglio,
      Map<String, String> errors)
  {
    if (dettaglio.getFlagObbligatorio().equals("S"))
    {
      return "&nbsp;";
    }
    else
      if (dettaglio.getFlagObbligatorio().equals("N"))
      {
        String disabled = "";
        if (!isModifica)
        {
          disabled = " disabled=\"disabled\" readonly=\"readonly\" ";
        }
        String id = "chkdich_" + dettaglio.getIdDettaglioInfo();
        String checked = "";
        if (((dettaglio.getIdSelezioneInfo() == null
            || dettaglio.getIdSelezioneInfo().longValue() <= 0)
            && ((String) getELVariableValue(id) == null))
            || !dettaglio.isChecked())
        {
          checked = " ";
        }
        else
          checked = " checked = \"checked\"";

        String chkTag = "";

        if (errors != null && errors.containsKey(id))
        {
          if (dettaglio.isChecked())
          {
            checked = " checked = \"checked\"";
          }
          else
          {
            checked = " ";
          }

          chkTag = "<div class=\"form-group\"><div class=\"col-sm-2 has-error has-error-checkbox red-tooltip has-feedback form-control\" data-toggle=\"error-tooltip\" data-original-title=\""
              + errors.get(id) + "\" >"
              + "  <input type=\"checkbox\" name=\"" + id + "\" id=\"" + id
              + "\" value=\"" + dettaglio.getIdDettaglioInfo() + "\" "
              + disabled + " " + checked + "  />"
              + "</div></div>";
        }
        else
        {
          chkTag = "<div class=\"form-group\"><div class=\"col-sm-2\" >"
              + "  <input type=\"checkbox\" name=\"" + id + "\" id=\"" + id
              + "\" value=\"" + dettaglio.getIdDettaglioInfo() + "\" "
              + disabled + " " + checked + "  />"
              + "</div></div>";

        }

        return chkTag;
        // return "<input type=\"checkbox\" name=\""+id+"\" id=\""+id+"\"
        // value=\""+dettaglio.getIdDettaglioInfo()+"\" "+disabled+" "+checked+"
        // />"+checkNull(ErrorTag.getErrorHtml(errors, id));
      }

    return "&nbsp;";
  }

  protected String createLink(String testo)
  {
    try
    {
      if (testo == null)
      {
        return testo;
      }

      String link = "";
      Vector<Integer> vTemp = new Vector<Integer>();
      HashMap<String, Integer> tmpMap = null;
      HashMap<String, HashMap<String, Integer>> results = new HashMap<String, HashMap<String, Integer>>();

      String urlRegex = "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
          "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
          "|mil|biz|info|mobi|name|aero|jobs|museum" +
          "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
          "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
          "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
          "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
          "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
          "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
          "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b";

      Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
      Matcher urlMatcher = pattern.matcher(testo);

      while (urlMatcher.find())
      {
        if (!vTemp.contains(urlMatcher.start(0)))
        {
          link = testo.substring(urlMatcher.start(0), urlMatcher.end(0));
          vTemp.add(urlMatcher.start(0));
          tmpMap = new HashMap<String, Integer>();
          tmpMap.put("INIZIO", urlMatcher.start(0));
          tmpMap.put("FINE", urlMatcher.end(0));
          results.put(link, tmpMap);
        }
      }

      for (Map.Entry<String, HashMap<String, Integer>> entry : results
          .entrySet())
      {
        link = entry.getKey();

        if (link.indexOf("http://") == -1 && link.indexOf("https://") == -1)
        {
          testo = testo.substring(0, entry.getValue().get("INIZIO")) +
              "<a style=\"cursor:pointer;\" target=\"_blank\" href=\""
              + "http://" + link + "\">" + link + "</a>" +
              testo.substring(entry.getValue().get("FINE"));
        }
        else
        {
          testo = testo.substring(0, entry.getValue().get("INIZIO")) +
              "<a style=\"cursor:pointer;\" target=\"_blank\" href=\"" + link
              + "\">" + link + "</a>" +
              testo.substring(entry.getValue().get("FINE"));
        }
      }

      return testo;
    }
    catch (Exception e)
    {
      return testo;
    }
  }

  protected String sostituisciPlaceHolder(DettaglioInfoDTO dettaglio,
      Map<String, String> errors) throws Exception
  {
    String testo = dettaglio.getDescrizione();
    testo = createLink(testo);

    if (isModifica)
    {
      Errors errorsVal = (Errors) getELVariableValue("errors");
      String inputStart = "<div class=\"form-group\"><div class=\"col-md-1\"> ";
      TextField taInput = null;

      int position = 0;
      String repfrom = "\\$\\$STRING";
      String repto = "";
      String id = "";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        id = "inString_" + dettaglio.getIdDettaglioInfo() + "_" + position;
        position++;
        taInput = new TextField();
        taInput.setName(id);
        taInput.setId(id);
        taInput.setCssClass("dich_string form-control");
        taInput.setControlSize(2);
        taInput.setValue(
            getValoreInput(id, dettaglio.getValoriInseriti(), position));

        repto = inputStart + TextArea.getHtmlStaticText(taInput, errorsVal)
            + " </div></div>";
        testo = testo.replaceFirst(repfrom, repto);
      }

      repfrom = "\\$\\$INTEGER";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        id = "inInteger_" + dettaglio.getIdDettaglioInfo() + "_" + position;
        position++;
        taInput = new TextField();
        taInput.setName(id);
        taInput.setId(id);
        taInput.setCssClass("dich_integer form-control");
        taInput.setControlSize(2);
        taInput.setValue(
            getValoreInput(id, dettaglio.getValoriInseriti(), position));

        repto = inputStart + TextArea.getHtmlStaticText(taInput, errorsVal)
            + " </div></div>";
        testo = testo.replaceFirst(repfrom, repto);
      }

      repfrom = "\\$\\$NUMBER";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        id = "inNumber_" + dettaglio.getIdDettaglioInfo() + "_" + position;
        position++;
        taInput = new TextField();
        taInput.setName(id);
        taInput.setId(id);
        taInput.setCssClass("dich_number form-control");
        taInput.setControlSize(2);
        taInput.setValue(
            getValoreInput(id, dettaglio.getValoriInseriti(), position));

        repto = inputStart + TextArea.getHtmlStaticText(taInput, errorsVal)
            + " </div></div>";
        testo = testo.replaceFirst(repfrom, repto);
      }

      repfrom = "\\$\\$DATE";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        id = "inDate_" + dettaglio.getIdDettaglioInfo() + "_" + position;
        position++;
        taInput = new TextField();
        taInput.setName(id);
        taInput.setId(id);
        taInput.setValue(
            getValoreInput(id, dettaglio.getValoriInseriti(), position));
        taInput.setType("DATE");

        repto = "<div class=\"form-group\"><div class=\"col-sm-12\"> \n"
            + TextArea.getHtmlStaticText(taInput, errorsVal) + " \n"
            + "</div>";
        testo = testo.replaceFirst(repfrom, repto);
      }
    }
    else
    {
      int position = 1;
      String repfrom = "\\$\\$STRING";
      String repto = "";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        repto = getValore(dettaglio.getValoriInseriti(), position);
        testo = testo.replaceFirst(repfrom, repto);
        position++;
      }

      repfrom = "\\$\\$INTEGER";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        repto = getValore(dettaglio.getValoriInseriti(), position);
        testo = testo.replaceFirst(repfrom, repto);
        position++;
      }

      repfrom = "\\$\\$NUMBER";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        repto = getValore(dettaglio.getValoriInseriti(), position);
        testo = testo.replaceFirst(repfrom, repto);
        position++;
      }

      repfrom = "\\$\\$DATE";
      while (!testo.replaceFirst(repfrom, repto).equals(testo))
      {
        repto = getValore(dettaglio.getValoriInseriti(), position);
        testo = testo.replaceFirst(repfrom, repto);
        position++;
      }
    }

    return testo;
  }

  protected String getValoreInput(String id,
      List<ValoriInseritiDTO> valoriInseriti, int position)
  {
    String ret = checkNull((String) getELVariableValue(id));
    if (ret == null || ret.trim().length() <= 0)
    {
      ret = "";
      if (valoriInseriti != null)
      {
        for (ValoriInseritiDTO val : valoriInseriti)
        {
          if (val.getPosizione() == position)
            ret = val.getValore();
        }
      }
    }

    return ret;
  }

  protected String getValore(List<ValoriInseritiDTO> valoriInseriti,
      int position)
  {
    if (valoriInseriti != null)
    {
      for (ValoriInseritiDTO val : valoriInseriti)
      {
        if (val.getPosizione() == position)
          return val.getValore();
      }
    }
    return "_______";
  }

  protected String checkNull(String val)
  {
    return (val == null || val.equals("null")) ? "" : val;
  }

  public Boolean getIsModifica()
  {
    return isModifica;
  }

  public void setIsModifica(Boolean isModifica)
  {
    this.isModifica = isModifica;
  }

}