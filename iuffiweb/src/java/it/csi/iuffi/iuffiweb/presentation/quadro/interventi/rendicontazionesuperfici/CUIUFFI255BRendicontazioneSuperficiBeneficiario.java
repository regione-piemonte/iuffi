package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.rendicontazionesuperfici;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONRendicontazioneSuperficiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.UpdateSuperficieLocalizzazioneDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-255-B", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi255b")
public class CUIUFFI255BRendicontazioneSuperficiBeneficiario
    extends AbstractRendicontazioneSuperfici
{
  @Override
  protected boolean requireIstanza()
  {
    return true;
  }

  @Override
  protected String getBasePath()
  {
    return "rendicontazionesuperifici/beneficiario/";
  }

  @Override
  public boolean validateAndUpdate(Model model, long idIntervento,
      List<RigaJSONRendicontazioneSuperficiDTO> elencoParticelle,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    Errors errors = new Errors();
    List<UpdateSuperficieLocalizzazioneDTO> list = new ArrayList<>();
    for (RigaJSONRendicontazioneSuperficiDTO particella : elencoParticelle)
    {
      final long idConduzioneDichiarata = particella
          .getIdConduzioneDichiarata();
      final long idUtilizzoDichiarato = particella.getIdUtilizzoDichiarato();
      final String key = "superficieEffettiva_" + idConduzioneDichiarata + "_"
          + idUtilizzoDichiarato;
      String superficieEffettiva = request.getParameter(key);
      BigDecimal bdSuperficieEffettiva = errors
          .validateMandatoryBigDecimalInRange(superficieEffettiva, key, 4,
              BigDecimal.ZERO,
              new BigDecimal(particella.getSuperficieImpegno().replace(".", "")
                  .replace(",", ".")));
      if (bdSuperficieEffettiva != null)
      {
        UpdateSuperficieLocalizzazioneDTO superficie = new UpdateSuperficieLocalizzazioneDTO();
        superficie.setIdConduzioneDichiarata(idConduzioneDichiarata);
        superficie.setIdUtilizzoDichiarato(idUtilizzoDichiarato);
        superficie.setSuperficie(bdSuperficieEffettiva);
        list.add(superficie);
      }
    }
    if (errors.addToModelIfNotEmpty(model))
    {
      return false;
    }
    else
    {
      rendicontazioneEAccertamentoSpeseEJB
          .updateSuperficieEffettivaLocalizzazioneIntervento(idIntervento,
              getLogOperationOggettoQuadroDTO(request.getSession()), list);
      return true;
    }
  }

  @Override
  public String getParentUrl()
  {
    return "/cuiuffi211l/index.do";
  }

  @Override
  protected void calcolaTotali(Model model,
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
  {
    CUIUFFI255DBDettaglioRendicontazioneSuperficiBeneficiario
        .calcolaTotali(model, elenco);
  }

  @Override
  protected void onFirstPageLoad(
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
  {
    if (elenco != null)
    {
      for (RigaJSONRendicontazioneSuperficiDTO rigaJSONRendicontazioneSuperficiDTO : elenco)
      {
        if (rigaJSONRendicontazioneSuperficiDTO
            .getSuperficieEffettiva() == null)
        {
          rigaJSONRendicontazioneSuperficiDTO
              .setSuperficieEffettiva(rigaJSONRendicontazioneSuperficiDTO
                  .getSuperficieImpegno().replace(".", ""));
        }
      }
    }
  }

}