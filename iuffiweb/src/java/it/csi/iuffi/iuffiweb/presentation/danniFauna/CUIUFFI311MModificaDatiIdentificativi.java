package it.csi.iuffi.iuffiweb.presentation.danniFauna;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.IstitutoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-311-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi311m")
public class CUIUFFI311MModificaDatiIdentificativi extends BaseController
{
  @Autowired
  private IQuadroIuffiEJB quadroIuffiEJB = null;
  @Autowired
  private IInterventiEJB  interventiEJB  = null;
  @Autowired
  private IQuadroEJB      quadroEJB      = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session)
        .getIdProcedimentoOggetto();
    long idProcedimento = getIdProcedimento(session);
    Long idAmmCompetenza = quadroEJB
        .getIdAmmCompetenzaProcedimento(idProcedimento);
    if (idAmmCompetenza == null)
    {
      model.addAttribute("errIdAmmCompetenza", "true");
    }
    else
    {
      String nominativoSelEnable = "none";
      String nominativoTextEnable = "none";
      String nominativoTextEnable2 = "none";
      Date dataDanno = new Date();

      Integer numRow = quadroIuffiEJB.getCountDanniFauna(idProcedimentoOggetto);
      if (numRow != 0)
      {
        DannoDaFaunaDTO datiIdentificativi = quadroIuffiEJB
            .getDatiIdentificativiDanniDaFauna(idProcedimentoOggetto);
        model.addAttribute("datiIdentificativi", datiIdentificativi);
        List<DecodificaDTO<String>> comuni = interventiEJB.getDecodificheComuni(
            IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE,
            datiIdentificativi.getIdIstatProvincia(), null);
        model.addAttribute("listaComuni", comuni);
        if (datiIdentificativi.getUrgenzaPerizia() != null
            && datiIdentificativi.getUrgenzaPerizia())
        {
          model.addAttribute("isUrgente", "checked='checked'");
        }
        Long idIstituto = datiIdentificativi.getIdIstituto();
        if (idIstituto != null)
        {
          if (idIstituto == 4L)
          {
            nominativoTextEnable = "block";
          }
          else
          {
            nominativoSelEnable = "block";
            List<IstitutoDTO> listaNominativi = quadroIuffiEJB
                .getListaNominativiDanniFauna(idIstituto);
            if(listaNominativi!=null)
            for (IstitutoDTO n : listaNominativi)
            {
              if (n.getIdIstituto() == datiIdentificativi.getIdNominativo())
              {
                if (StringUtils.trim(n.getDescrizione())
                    .equalsIgnoreCase("Altro"))
                {
                  nominativoTextEnable2 = "block";
                  model.addAttribute("nominativoSel", "Altro");
                }
              }
            }
            model.addAttribute("listaNominativi", listaNominativi);
          }
        }
        dataDanno = datiIdentificativi.getDataDanno();
      }
      else
      {
        model.addAttribute("datiIdentificativi", null);
      }

      model.addAttribute("nominativoSelEnable", nominativoSelEnable);
      model.addAttribute("nominativoTextEnable", nominativoTextEnable);
      model.addAttribute("nominativoTextEnable2", nominativoTextEnable2);

      List<IstitutoDTO> listaIstituti = quadroIuffiEJB
          .getListaIstitutiDanniFauna(idAmmCompetenza);
      model.addAttribute("listaIstituti", listaIstituti);
      List<DecodificaDTO<String>> province = interventiEJB
          .getProvincie(IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE);
      model.addAttribute("listaProvince", province);
      List<DecodificaDTO<Long>> listaMotivazioni = quadroIuffiEJB
          .getListaMotiviUrgenza(dataDanno);
      model.addAttribute("listaMotivazioni", listaMotivazioni);

      Map<String, String> parametri = quadroEJB.getParametri(new String[]
      { IuffiConstants.PARAMETRO.PERIZIA_URG_NOTA });
      String commentoUrgenza = parametri
          .get(IuffiConstants.PARAMETRO.PERIZIA_URG_NOTA);
      model.addAttribute("commentoUrgenza", commentoUrgenza);

      model.addAttribute("preferRequestValues", false);

    }
    return "danniFauna/modificaDatiIdentificativi";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String conferma(HttpSession session, HttpServletRequest request,
      Model model)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    long idProcedimento = getIdProcedimento(session);
    Long idAmmCompetenza = quadroEJB
        .getIdAmmCompetenzaProcedimento(idProcedimento);
    if (idAmmCompetenza == null)
    {
      model.addAttribute("errIdAmmCompetenza", "true");
      return "danniFauna/modificaDatiIdentificativi";
    }
    else
    {
      String note = request.getParameter("note");
      errors.validateFieldMaxLength(note, "note", 4000);

      String dataDanno = request.getParameter("dataDanno");
      errors.validateMandatoryDate(dataDanno, "dataDanno", true);

      String idIstituto = request.getParameter("idIstituto");
      errors.validateMandatoryLong(idIstituto, "idIstituto");

      String idNominativo = request.getParameter("idNominativo");
      String nominativo = request.getParameter("nominativo");
      String nominativoSel = request.getParameter("nominativoSel");
      String nominativoAltro = request.getParameter("nominativoAltro");
      if (StringUtils.isNotBlank(idIstituto))
      {
        if ("4".equalsIgnoreCase(idIstituto))
        {
          idNominativo = null;
          errors.validateMandatory(nominativo, "nominativo");
        }
        else
        {
          nominativo = null;
          //errors.validateMandatoryLong(idNominativo, "idNominativo");
        }
      }
      if ("Altro".equalsIgnoreCase(StringUtils.trim(nominativoSel)))
      {
        nominativo = nominativoAltro;
        errors.validateMandatory(nominativoAltro, "nominativoAltro");
      }
      String urgenzaPerizia = request.getParameter("urgenzaPerizia");
      if (urgenzaPerizia == null)
      {
        urgenzaPerizia = IuffiConstants.FLAGS.NO;
      }
      else
      {
        urgenzaPerizia = IuffiConstants.FLAGS.SI;
      }

      String idMotivoUrgenza = request.getParameter("idMotivoUrgenza");
      String motivazione = request.getParameter("motivazione");
      errors.validateFieldMaxLength(motivazione, "motivazione", 4000);

      if (urgenzaPerizia.equals(IuffiConstants.FLAGS.SI))
      {
        errors.validateMandatoryLong(idMotivoUrgenza, "idMotivoUrgenza");
        if (idMotivoUrgenza != null)
        {
          String decMotUrg = quadroIuffiEJB
              .getDecodificaMotivoUrgenza(Long.valueOf(idMotivoUrgenza));
          if ("Altro".equalsIgnoreCase(StringUtils.trim(decMotUrg)))
          {
            errors.validateMandatory(motivazione, "motivazione");
          }
        }
      }

      String comune = request.getParameter("istatComune");
      errors.validateMandatoryLong(comune, "istatComune");

      String provincia = request.getParameter("istatProvincia");
      errors.validateMandatory(provincia, "istatProvincia");

      if (!errors.isEmpty())
      {
        model.addAttribute("errors", errors);

        Long idIstitutoLong = StringUtils.isNotBlank(idIstituto)
            ? Long.parseLong(idIstituto)
            : null;
            Long idNominativoLong = StringUtils.isNotBlank(idNominativo)
                ? Long.parseLong(idIstituto)
                : null;
        Boolean isUrgente = urgenzaPerizia.equals(IuffiConstants.FLAGS.SI)
            ? true
            : false;
        return indexError(session, model, isUrgente, motivazione,
            idAmmCompetenza, idIstitutoLong,idNominativoLong);
      }

      DannoDaFaunaDTO danno = new DannoDaFaunaDTO();
      danno.setIdProcedimentoOggetto(getIdProcedimentoOggetto(session));
      danno.setDataDanno(IuffiUtils.DATE.parseDate(dataDanno));
      danno.setIdIstituto(Long.parseLong(idIstituto));
      danno.setIdIstatComune(comune);
      danno.setUrgenzaPerizia(
          urgenzaPerizia.equals(IuffiConstants.FLAGS.SI) ? true : false);
      danno.setNoteUrgenza(motivazione);
      if (danno.getUrgenzaPerizia())
      {
        danno.setIdMotivoUrgenza(Long.parseLong(idMotivoUrgenza));
      }
      danno.setNote(note);
      if (idNominativo != null && idNominativo != ""
          && idNominativo.length() != 0 && idNominativo.indexOf("selezionare")<0)
      {
        danno.setIdNominativo(Long.parseLong(idNominativo));
      }

      danno.setNominativo(nominativo);

      quadroIuffiEJB.updateDatiIdentificativiDanniDaFauna(danno,
          getLogOperationOggettoQuadroDTO(session));

      return "redirect:../cuiuffi311l/index.do";

    }

  }

  public String indexError(HttpSession session, Model model, Boolean isUrgente,
      String motivazione, long idAmmCompetenza, Long idIstituto, Long idNominativoLong)
      throws InternalUnexpectedException
  {
    long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session)
        .getIdProcedimentoOggetto();
    DannoDaFaunaDTO datiIdentificativi = quadroIuffiEJB
        .getDatiIdentificativiDanniDaFauna(idProcedimentoOggetto);
    if(datiIdentificativi==null){
      datiIdentificativi=new DannoDaFaunaDTO();
    }
    if(idNominativoLong!=null)
      datiIdentificativi.setIdNominativo(idNominativoLong);
    datiIdentificativi.setUrgenzaPerizia(isUrgente);
    datiIdentificativi.setNoteUrgenza(motivazione);
    model.addAttribute("datiIdentificativi", datiIdentificativi);
    List<IstitutoDTO> listaIstituti = quadroIuffiEJB
        .getListaIstitutiDanniFauna(idAmmCompetenza);
    model.addAttribute("listaIstituti", listaIstituti);

    String nominativoSelEnable = "none";
    String nominativoTextEnable = "none";
    String nominativoTextEnable2 = "none";
    if (idIstituto != null)
    {
      if (idIstituto == 4L)
      {
        nominativoTextEnable = "block";
      }
      else
      {
        nominativoSelEnable = "block";
        List<IstitutoDTO> listaNominativi = quadroIuffiEJB
            .getListaNominativiDanniFauna(idIstituto);
        if(datiIdentificativi.getIdNominativo()!=null)
        for (IstitutoDTO n : listaNominativi)
        {
          
            if (StringUtils.trim(n.getDescrizione()).equalsIgnoreCase("Altro"))
            {
              nominativoTextEnable2 = "block";
              model.addAttribute("nominativoSel", "Altro");
            }
        }
        model.addAttribute("listaNominativi", listaNominativi);
      }
    }
    model.addAttribute("nominativoSelEnable", nominativoSelEnable);
    model.addAttribute("nominativoTextEnable", nominativoTextEnable);
    model.addAttribute("nominativoTextEnable2", nominativoTextEnable2);

    List<DecodificaDTO<String>> province = interventiEJB
        .getProvincie(IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE);
    List<DecodificaDTO<String>> comuni = interventiEJB.getDecodificheComuni(
        null, datiIdentificativi.getIdIstatProvincia(), null);
    model.addAttribute("listaProvince", province);
    model.addAttribute("listaComuni", comuni);
    if (datiIdentificativi.getUrgenzaPerizia())
    {
      model.addAttribute("isUrgente", "checked='checked'");
    }

    List<DecodificaDTO<Long>> listaMotivazioni = quadroIuffiEJB
        .getListaMotiviUrgenza(datiIdentificativi.getDataDanno());
    model.addAttribute("listaMotivazioni", listaMotivazioni);

    Map<String, String> parametri = quadroEJB.getParametri(new String[]
    { IuffiConstants.PARAMETRO.PERIZIA_URG_NOTA });
    String commentoUrgenza = parametri
        .get(IuffiConstants.PARAMETRO.PERIZIA_URG_NOTA);
    model.addAttribute("commentoUrgenza", commentoUrgenza);

    model.addAttribute("preferRequestValues", true);

    return "danniFauna/modificaDatiIdentificativi";
  }

  @RequestMapping(value = "/get_comuni_by_provincia_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoComuniPerProvincia(Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return interventiEJB.getDecodificheComuni(null, istatProvincia, null);
  }

  @RequestMapping(value = "/get_nominativi_by_istituto_{idIstitutoDanniFauna}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<IstitutoDTO> elencoIstitutiNominativo(Model model,
      @PathVariable("idIstitutoDanniFauna") String idIstitutoDanniFauna,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return quadroIuffiEJB
        .getListaNominativiDanniFauna(new Long(idIstitutoDanniFauna));
  }
}
