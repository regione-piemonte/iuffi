package it.csi.iuffi.iuffiweb.presentation.quadro.allegati;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.DettaglioInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.ValoriInseritiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.quadro.dichiarazioni.DichiarazioniController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi109")
@IuffiSecurity(value = "CU-IUFFI-109", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI109ModificaAllegatiOggetto extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-108";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    List<GruppoInfoDTO> allegati = quadroEJB.getDichiarazioniOggetto(
        idProcedimentoOggetto, quadro.getIdQuadroOggetto(),
        procedimentoOggetto.getIdBandoOggetto());

    model.addAttribute("allegati", allegati);
    return "allegati/modificaAllegatiOggetto";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String indexPost(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException,
      IuffiPermissionException, ApplicationException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    List<GruppoInfoDTO> allegati = quadroEJB.getDichiarazioniOggetto(
        idProcedimentoOggetto, quadro.getIdQuadroOggetto(),
        procedimentoOggetto.getIdBandoOggetto());
    Errors errors = new Errors();
    List<GruppoInfoDTO> allegatiSelezionate = validateForm(errors, model,
        allegati, request);

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("allegati", allegatiSelezionate);
      return "allegati/modificaAllegatiOggetto";
    }
    else
    {
      quadroEJB.updateDichiarazioniOAllegatiOggetto(
          getIdProcedimentoOggetto(session), quadro.getIdQuadroOggetto(),
          procedimentoOggetto.getIdBandoOggetto(),
          allegatiSelezionate, getIdUtenteLogin(session));
    }
    return "redirect:/cuiuffi108/index.do";
  }

  private List<GruppoInfoDTO> validateForm(Errors errors, Model model,
      List<GruppoInfoDTO> allegati, HttpServletRequest request)
  {
    Map<String, String[]> mapParam = request.getParameterMap();

    HashMap<String, String> chkMap = new HashMap<String, String>();
    HashMap<String, String> inStringMap = new HashMap<String, String>();
    HashMap<String, String> inIntegerMap = new HashMap<String, String>();
    HashMap<String, String> inNumberMap = new HashMap<String, String>();
    HashMap<String, String> inDateMap = new HashMap<String, String>();
    String idDettaglioInfo = "";

    for (Map.Entry<String, String[]> entry : mapParam.entrySet())
    {
      model.addAttribute(entry.getKey(), entry.getValue()[0]);
      if (entry.getKey().startsWith("chkdich"))
      {
        idDettaglioInfo = entry.getKey().split("_")[1];
        chkMap.put(entry.getKey(), entry.getKey());
      }
      else
        if (entry.getKey().startsWith("inString"))
        {
          inStringMap.put(entry.getKey(), entry.getValue()[0]);
        }
        else
          if (entry.getKey().startsWith("inInteger"))
          {
            inIntegerMap.put(entry.getKey(), entry.getValue()[0]);
          }
          else
            if (entry.getKey().startsWith("inNumber"))
            {
              inNumberMap.put(entry.getKey(), entry.getValue()[0]);
            }
            else
              if (entry.getKey().startsWith("inDate"))
              {
                inDateMap.put(entry.getKey(), entry.getValue()[0]);
              }
    }

    // Per ogni dichiarazione selezionata valido tutti i campi input presenti a
    // video.
    // Il name è ad es. chkdich_7 dove 7 è il rowNum
    idDettaglioInfo = "";
    for (Map.Entry<String, String> entry : chkMap.entrySet())
    {
      idDettaglioInfo = entry.getKey().split("_")[1];

      for (Map.Entry<String, String> inStr : inStringMap.entrySet())
      {
        if (idDettaglioInfo.equals(inStr.getKey().split("_")[1]))
          errors.validateMandatory(inStr.getValue(), inStr.getKey());
      }
      for (Map.Entry<String, String> inInt : inIntegerMap.entrySet())
      {
        if (idDettaglioInfo.equals(inInt.getKey().split("_")[1]))
          errors.validateMandatoryLong(inInt.getValue(), inInt.getKey());
      }
      for (Map.Entry<String, String> inNum : inNumberMap.entrySet())
      {
        if (idDettaglioInfo.equals(inNum.getKey().split("_")[1]))
          errors.validateMandatoryDouble(inNum.getValue(), inNum.getKey());
      }
      for (Map.Entry<String, String> inDate : inDateMap.entrySet())
      {
        if (idDettaglioInfo.equals(inDate.getKey().split("_")[1]))
          errors.validateMandatoryDate(inDate.getValue(), inDate.getKey(),
              true);
      }
    }

    for (GruppoInfoDTO info : allegati)
    {
      for (DettaglioInfoDTO dettaglio : info.getDettaglioInfo())
      {
        if (dettaglio.getFlagObbligatorio() != null
            && dettaglio.getFlagObbligatorio().equals("S"))
        {
          chkMap.put("chkdich_" + dettaglio.getIdDettaglioInfo(),
              "chkdich_" + dettaglio.getIdDettaglioInfo());
          // controllo eventuali campi obbligatori non inseriti per le
          // dichiarazioni senza check
          idDettaglioInfo = String.valueOf(dettaglio.getIdDettaglioInfo());
          for (Map.Entry<String, String> inStr : inStringMap.entrySet())
          {
            if (idDettaglioInfo.equals(inStr.getKey().split("_")[1]))
              errors.validateMandatory(inStr.getValue(), inStr.getKey());
          }
          for (Map.Entry<String, String> inInt : inIntegerMap.entrySet())
          {
            if (idDettaglioInfo.equals(inInt.getKey().split("_")[1]))
              errors.validateMandatoryLong(inInt.getValue(), inInt.getKey());
          }
          for (Map.Entry<String, String> inNum : inNumberMap.entrySet())
          {
            if (idDettaglioInfo.equals(inNum.getKey().split("_")[1]))
              errors.validateMandatoryDouble(inNum.getValue(), inNum.getKey());
          }
          for (Map.Entry<String, String> inDate : inDateMap.entrySet())
          {
            if (idDettaglioInfo.equals(inDate.getKey().split("_")[1]))
              errors.validateMandatoryDate(inDate.getValue(), inDate.getKey(),
                  true);
          }
        }
      }
    }

    // Verifico specifiche di vincolo per le dichiarazioni facoltati
    verificaVincoli(errors, allegati, chkMap);
    List<ValoriInseritiDTO> valoriInseriti = null;

    for (GruppoInfoDTO info : allegati)
    {
      for (DettaglioInfoDTO dettaglio : info.getDettaglioInfo())
      {
        idDettaglioInfo = String.valueOf(dettaglio.getIdDettaglioInfo());
        valoriInseriti = new ArrayList<ValoriInseritiDTO>();
        dettaglio.setChecked(false);

        if (chkMap.containsKey("chkdich_" + dettaglio.getIdDettaglioInfo()))
          dettaglio.setChecked(true);

        for (Map.Entry<String, String> inStr : inStringMap.entrySet())
        {
          if (idDettaglioInfo.equals(inStr.getKey().split("_")[1]))
            valoriInseriti.add(new ValoriInseritiDTO(
                dettaglio.getIdSelezioneInfo(), inStr.getValue(),
                Long.parseLong(inStr.getKey().split("_")[2]) + 1));
        }
        for (Map.Entry<String, String> inInt : inIntegerMap.entrySet())
        {
          if (idDettaglioInfo.equals(inInt.getKey().split("_")[1]))
            valoriInseriti.add(new ValoriInseritiDTO(
                dettaglio.getIdSelezioneInfo(), inInt.getValue(),
                Long.parseLong(inInt.getKey().split("_")[2]) + 1));
        }
        for (Map.Entry<String, String> inNum : inNumberMap.entrySet())
        {
          if (idDettaglioInfo.equals(inNum.getKey().split("_")[1]))
            valoriInseriti.add(new ValoriInseritiDTO(
                dettaglio.getIdSelezioneInfo(), inNum.getValue(),
                Long.parseLong(inNum.getKey().split("_")[2]) + 1));
        }
        for (Map.Entry<String, String> inDate : inDateMap.entrySet())
        {
          if (idDettaglioInfo.equals(inDate.getKey().split("_")[1]))
            valoriInseriti.add(new ValoriInseritiDTO(
                dettaglio.getIdSelezioneInfo(), inDate.getValue(),
                Long.parseLong(inDate.getKey().split("_")[2]) + 1));
        }

        dettaglio.setValoriInseriti(valoriInseriti);
      }
    }

    return allegati;
  }

  private void verificaVincoli(Errors errors, List<GruppoInfoDTO> dichiarazioni,
      HashMap<String, String> selectedIdDettaglioInfo)
  {
    for (GruppoInfoDTO info : dichiarazioni)
    {
      for (DettaglioInfoDTO dettaglio : info.getDettaglioInfo())
      {
        if (!dettaglio.getFlagObbligatorio().equals("S")
            && (dettaglio.getIdLegameInfo() != null
                && dettaglio.getIdLegameInfo().longValue() > 0))
        {
          List<Long> lIds = getSelectedChekByIdLegameInfo(dichiarazioni,
              selectedIdDettaglioInfo, dettaglio.getIdLegameInfo().longValue());

          if (dettaglio.getTipoVincolo().equals(
              DichiarazioniController.TIPO_VINCOLO_UNIVOCO) && lIds.size() > 1)
          {
            errors.addError("chkdich_" + dettaglio.getIdDettaglioInfo(),
                "E' possibile selezionare non più di una dichiarazione con vincolo univoco");
          }
          else
            if (dettaglio.getTipoVincolo()
                .equals(DichiarazioniController.TIPO_VINCOLO_OPZIONALE_UNIVOCO)
                && lIds.size() > 1)
            {
              errors.addError("chkdich_" + dettaglio.getIdDettaglioInfo(),
                  "E' possibile selezionare non più di una dichiarazione con vincolo opzionale univoco");
            }
            else
              if (dettaglio.getTipoVincolo()
                  .equals(DichiarazioniController.TIPO_VINCOLO_UNIVOCO)
                  && lIds.size() == 0)
              {
                errors.addError("chkdich_" + dettaglio.getIdDettaglioInfo(),
                    "E' necessario selezionare una dichiarazione con vincolo univoco");
              }
              else
                if (dettaglio.getTipoVincolo()
                    .equals(DichiarazioniController.TIPO_VINCOLO_OBBLIGATORIO)
                    && lIds.size() <= 0)
                {
                  errors.addError("chkdich_" + dettaglio.getIdDettaglioInfo(),
                      "E' necessario selezionare almeno una dichiarazione con vincolo obbligatorio");
                }
        }
      }
    }
  }

  private List<Long> getSelectedChekByIdLegameInfo(
      List<GruppoInfoDTO> dichiarazioni,
      HashMap<String, String> selectedIdDettaglioInfo, long idLegameInfo)
  {
    List<Long> retList = new ArrayList<Long>();
    String idDettaglioInfo = "";
    for (GruppoInfoDTO info : dichiarazioni)
    {
      for (DettaglioInfoDTO dettaglio : info.getDettaglioInfo())
      {
        if (dettaglio.getIdLegameInfo() != null
            && dettaglio.getIdLegameInfo().longValue() == idLegameInfo)
        {
          for (Map.Entry<String, String> entry : selectedIdDettaglioInfo
              .entrySet())
          {
            idDettaglioInfo = entry.getKey().split("_")[1];
            if (dettaglio.getIdDettaglioInfo() == Long
                .parseLong(idDettaglioInfo))
            {
              retList.add(dettaglio.getIdLegameInfo().longValue());
              break;
            }
          }
        }
      }
    }
    return retList;
  }
}
