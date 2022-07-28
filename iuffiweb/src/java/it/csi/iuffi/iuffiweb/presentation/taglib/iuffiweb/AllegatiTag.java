package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.FileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.DettaglioInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class AllegatiTag extends DichiarazioniTag
{
  /** serialVersionUID */
  protected static final long                serialVersionUID = -7620410490326047187L;
  protected List<GruppoInfoDTO>              allegati         = null;
  protected Map<Long, List<FileAllegatiDTO>> fileMap          = null;
  protected boolean                          canUpdate;

  @Override
  protected void createHtml(List<GruppoInfoDTO> allegati, StringBuilder buffer)
      throws Exception
  {
    @SuppressWarnings("unchecked")
    Map<String, String> errors = (Map<String, String>) getELVariableValue(
        "errors");

    buffer.append(
        "<table summary=\"Allegati\" class=\"myovertable table table-hover table-condensed table-bordered\"> 	\n"
            + "	<tbody>	\n");
    int numCols = isModifica ? 2 : (canUpdate ? 5 : 4);
    for (int x = 0; x < allegati.size(); x++)
    {
      GruppoInfoDTO info = allegati.get(x);
      buffer.append("<tr>\n<th colspan=\"").append(numCols)
          .append(
              "\"\n style=\"background-color: #DDDDDD; color: #000000; text-align: left;font-weight:bold\">\n")
          .append(info.getDescrizione()).append("</th>\n</tr>\n");
      for (int i = 0; i < info.getDettaglioInfo().size(); i++)
      {
        buffer.append(" <tr> \n");
        DettaglioInfoDTO dettInfo = info.getDettaglioInfo().get(i);
        List<FileAllegatiDTO> files = null;
        int rowspan = 1;
        if (fileMap != null)
        {
          files = fileMap.get(dettInfo.getIdDettaglioInfo());
          if (files != null && !files.isEmpty())
          {
            rowspan = files.size();
          }
        }
        if ((i < (info.getDettaglioInfo().size() - 1))
            || (x <= (allegati.size() - 1)))
        {
          buffer.append(" <th rowspan=\"").append(rowspan)
              .append("\" style=\"vertical-align:middle\">")
              .append(getInputCheck(dettInfo, errors))
              .append("</th>   \n")
              .append("<td rowspan=\"").append(rowspan)
              .append("\" style=\"vertical-align:middle\">")
              .append(sostituisciPlaceHolder(dettInfo, errors))
              .append("</td> \n");
        }
        else
        {
          buffer.append(" <th rowspan=\"").append(rowspan)
              .append("\" style=\"vertical-align:middle;\">")
              .append(getInputCheck(dettInfo, errors))
              .append("</th>   \n")
              .append(" <td rowspan=\"").append(rowspan)
              .append("\" style=\"vertical-align:middle;\">")
              .append(sostituisciPlaceHolder(dettInfo, errors))
              .append("</td> \n");
        }
        if (!isModifica)
        {
          if (canUpdate)
          {
            String flagGestioneFile = dettInfo.getFlagGestioneFile();
            String flagObbligatorio = dettInfo.getFlagObbligatorio();
            Long idSelezioneInfo = dettInfo.getIdSelezioneInfo();
            buffer.append("<td rowspan=\"").append(rowspan)
                .append("\" style=\"vertical-align:middle;\">");
            if ((DettaglioInfoDTO.FLAG_GESTIONE_FILE_OBBLIGATORIO
                .equals(flagGestioneFile)
                || DettaglioInfoDTO.FLAG_GESTIONE_FILE_FACOLTATIVO
                    .equals(flagGestioneFile))
                && (DettaglioInfoDTO.FLAG_ALLEGATO_OBBLIGATORIO
                    .equals(flagObbligatorio)
                    || (DettaglioInfoDTO.FLAG_ALLEGATO_NON_OBBLIGATORIO
                        .equals(flagObbligatorio) &&
                        idSelezioneInfo != null && idSelezioneInfo != 0)))
            {
              String tmpInfo = (DettaglioInfoDTO.FLAG_GESTIONE_FILE_OBBLIGATORIO
                  .equals(flagGestioneFile)) ? "Obbligatorio" : "Facoltativo";
              buffer.append(
                  "<a href=\"#\" data-toggle=\"modal\" data-target=\"#dlgInserisci\" onclick=\"return allegaFile(")
                  .append(dettInfo.getIdDettaglioInfo())
                  .append(")\">allega file (" + tmpInfo + ")</a>");
            }
            buffer.append("</td>\n");
          }
          if (files != null)
          {
            int count = 0;
            for (FileAllegatiDTO file : files)
            {
              if (count++ > 0)
              {
                buffer.append("</tr><tr>");
              }
              buffer.append("<td style=\"text-align:right\">");
              String icon = IuffiUtils.FILE
                  .getDocumentCSSIconClass(file.getNomeFisico());
              buffer.append(escapeHtml(file.getNomeLogico()));
              final long idFileAllegati = file.getIdFileAllegati();
              buffer.append(
                  "</td><td style=\"width:64px;text-align:center\" id=\"icons_")
                  .append(idFileAllegati)
                  .append("\"><a href=\"../cuiuffi121/scarica_allegato_")
                  .append(idFileAllegati).append(".do\" class=\"ico24 ")
                  .append(icon).append("\" title=\"")
                  .append(StringEscapeUtils.escapeHtml4(file.getNomeLogico()))
                  .append(" (").append(escapeHtml(file.getNomeFisico()) + ")")
                  .append("\"></a>");
              if (canUpdate)
              {
                buffer.append(
                    "<a href=\"#\" class=\"ico24 ico_trash\" data-toggle=\"modal\"  data-target=\"#dlgElimina\" title=\"elimina questo allegato\" onclick=\"return eliminaAllegato(")
                    .append(idFileAllegati).append(")\"></a><br />");
              }
              buffer.append("</td>");
            }
            if (count > 0)
            {
              buffer.append("</tr>");
            }
          }
          else
          {
            buffer.append("<td align=\"center\">&nbsp;</td>");
            if (canUpdate)
            {
              buffer.append("<td align=\"center\">&nbsp;</td>");
            }
          }
        }
      }
    }
    buffer.append(" </tbody> \n"
        + " </table>\n");
  }

  public List<GruppoInfoDTO> getAllegati()
  {
    return allegati;
  }

  public void setAllegati(List<GruppoInfoDTO> allegati)
  {
    this.allegati = allegati;
  }

  @Override
  protected List<GruppoInfoDTO> getGruppiInfo()
  {
    return allegati;
  }

  public Map<Long, List<FileAllegatiDTO>> getFileMap()
  {
    return fileMap;
  }

  public void setFileMap(Map<Long, List<FileAllegatiDTO>> fileMap)
  {
    this.fileMap = fileMap;
  }

  public boolean isCanUpdate()
  {
    return canUpdate;
  }

  public void setCanUpdate(boolean canUpdate)
  {
    this.canUpdate = canUpdate;
  }

}