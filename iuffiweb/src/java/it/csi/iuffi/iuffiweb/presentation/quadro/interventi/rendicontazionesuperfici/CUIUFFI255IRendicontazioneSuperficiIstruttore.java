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
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-255-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi255i")
public class CUIUFFI255IRendicontazioneSuperficiIstruttore
    extends AbstractRendicontazioneSuperfici
{

  @Override
  protected boolean requireIstanza()
  {
    return false;
  }

  @Override
  protected String getBasePath()
  {
    return "rendicontazionesuperifici/istruttore/";
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
      final String key = "superficieIstruttoria_" + idConduzioneDichiarata + "_"
          + idUtilizzoDichiarato;
      String superficieIstruttoria = request.getParameter(key);

      BigDecimal bdSuperficieEffettiva = IuffiUtils.NUMBERS
          .getBigDecimal(particella.getSuperficieEffettiva().replace(".", "")
              .replace(",", "."));
      BigDecimal bdSuperficieIstruttoria = null;
      bdSuperficieIstruttoria = errors.validateMandatoryBigDecimalInRange(
          superficieIstruttoria, key, 4,
          BigDecimal.ZERO, IuffiUtils.NUMBERS.min(
              particella.getSuperficieAccertataGis(), bdSuperficieEffettiva));
      UpdateSuperficieLocalizzazioneDTO superficie = new UpdateSuperficieLocalizzazioneDTO();
      superficie.setIdConduzioneDichiarata(idConduzioneDichiarata);
      superficie.setIdUtilizzoDichiarato(idUtilizzoDichiarato);
      superficie.setSuperficie(bdSuperficieIstruttoria);
      list.add(superficie);
    }
    if (errors.addToModelIfNotEmpty(model))
    {
      return false;
    }
    else
    {
      rendicontazioneEAccertamentoSpeseEJB
          .updateSuperficieIstruttoriaLocalizzazioneIntervento(idIntervento,
              getLogOperationOggettoQuadroDTO(request.getSession()), list);
      return true;
    }
  }

  @Override
  public String getParentUrl()
  {
    return "/cuiuffi212l/index.do";
  }

  @Override
  protected void verificaAmmissibilitaDatiSuDB(
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
      throws InternalUnexpectedException, ApplicationException
  {
    for (RigaJSONRendicontazioneSuperficiDTO riga : elenco)
    {
      if (riga.getSuperficieAccertataGis() == null)
      {
        throw new ApplicationException(
            "Errore: prima di proseguire è necessario eseguire l'importazione delle superfici GIS");
      }
      if (riga.getSuperficieEffettiva() == null)
      {
        throw new ApplicationException(
            "Errore grave: per almeno una localizzazione non risulta presente la superficie effettiva");
      }
    }
    super.verificaAmmissibilitaDatiSuDB(elenco);
  }

  @Override
  protected void calcolaTotali(Model model,
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
  {
    CUIUFFI255DIDettaglioRendicontazioneSuperficiIstruttore.calcolaTotali(model,
        elenco);
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
            .getSuperficieIstruttoria() == null)
        {
          BigDecimal bdSuperficieEffettiva = IuffiUtils.NUMBERS
              .getBigDecimal(
                  rigaJSONRendicontazioneSuperficiDTO.getSuperficieEffettiva()
                      .replace(".", "").replace(",", "."));
          BigDecimal min = IuffiUtils.NUMBERS.min(bdSuperficieEffettiva,
              rigaJSONRendicontazioneSuperficiDTO.getSuperficieAccertataGis());
          rigaJSONRendicontazioneSuperficiDTO.setSuperficieIstruttoria(
              IuffiUtils.FORMAT.formatDecimal4(min).replace(".", ""));
        }
      }
    }
  }
}