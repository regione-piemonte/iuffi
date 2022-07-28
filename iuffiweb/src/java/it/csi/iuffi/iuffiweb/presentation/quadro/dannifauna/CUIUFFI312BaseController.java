package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public class CUIUFFI312BaseController extends BaseController
{
  protected static final String FIELD_NAME_ID     = "idAccertamentoDanno";
  protected static final String FIELD_NAME_SOPRALLUOGO     = "sopralluogo";
  protected static final String FIELD_NAME_DATA            = "dataSopralluogo";
  protected static final String FIELD_NAME_PERITO          = "perito";
  protected static final String FIELD_NAME_NUMERO          = "numeroPerizia";
  protected static final String FIELD_NAME_IMPORTORIP      = "importoRipristino";
  protected static final String FIELD_NAME_SPESEPERIZIA    = "spesePerizia";
  protected static final String FIELD_NAME_SPESEPREV       = "spesePrevenzione";
  protected static final String FIELD_NAME_DESCRIZIONE     = "descrizionePrevenzione";
  protected static final String FIELD_NAME_REITERATI       = "reiteratiDanni";
  protected static final String FIELD_NAME_NOTE       = "note";


  @Autowired
  protected IQuadroIuffiEJB     quadroIuffiEJB;
  
  protected Errors validaInserimento(HttpServletRequest request,
      AccertamentoDannoDTO acdan) throws InternalUnexpectedException
  {
    Errors errors = new Errors();

    String idAccDanno = request.getParameter(FIELD_NAME_ID);
    if(StringUtils.isNotBlank(idAccDanno)) {
      acdan.setIdAccertamentoDanno(new Long(idAccDanno));
    }
    
    String sopralluogo = request.getParameter(FIELD_NAME_SOPRALLUOGO);
    String data = request.getParameter(FIELD_NAME_DATA);
    String perito = request.getParameter(FIELD_NAME_PERITO);
    String numero = request.getParameter(FIELD_NAME_NUMERO);
    String importoRipristino = request.getParameter(FIELD_NAME_IMPORTORIP);
    String spesePerizia = request.getParameter(FIELD_NAME_SPESEPERIZIA);
    String spesePrevenzione = request.getParameter(FIELD_NAME_SPESEPREV);
    String descrizione = request.getParameter(FIELD_NAME_DESCRIZIONE);
    String reiterati = request.getParameter(FIELD_NAME_REITERATI);
    String note = request.getParameter(FIELD_NAME_NOTE);

    errors.validateMandatory(sopralluogo, FIELD_NAME_SOPRALLUOGO);
    acdan.setSopralluogo(sopralluogo);

    if ("S".equalsIgnoreCase(sopralluogo))
    {
      if (errors.validateMandatoryDate(data, FIELD_NAME_DATA,
          true) != null)
      {
        acdan.setDataSopralluogo(IuffiUtils.DATE.parseDate(data));
      }
    }
    errors.validateMandatoryFieldMaxLength(perito, FIELD_NAME_PERITO, 200);
    acdan.setPerito(perito);

    errors.validateMandatoryFieldMaxLength(numero, FIELD_NAME_NUMERO, 50);
    acdan.setNumeroPerizia(numero);

    BigDecimal importoRipristinoBD = errors
        .validateMandatoryCurrency(importoRipristino, FIELD_NAME_IMPORTORIP);
    acdan.setImportoRipristino(importoRipristinoBD);

    if(importoRipristinoBD!=null) {
      long idProcedimentoOggetto = getIdProcedimentoOggetto(request.getSession());
      BigDecimal importoTotaleAccertato = quadroIuffiEJB.getImportoTotaleAccertato(idProcedimentoOggetto);
      if(importoRipristinoBD.compareTo(importoTotaleAccertato) == 1) {
        errors.addError(FIELD_NAME_IMPORTORIP, "Non può essere maggiore di importo totale accertato");
      }
    }
    
    BigDecimal spesePeriziaBD = errors.validateMandatoryCurrency(spesePerizia,
        FIELD_NAME_SPESEPERIZIA);
    acdan.setSpesePerizia(spesePeriziaBD);

    BigDecimal spesePrevenzioneBD = errors
        .validateMandatoryCurrency(spesePrevenzione, FIELD_NAME_SPESEPREV);
    acdan.setSpesePrevenzione(spesePrevenzioneBD);

    errors.validateFieldMaxLength(descrizione, FIELD_NAME_DESCRIZIONE, 4000);
    acdan.setDescrizionePrevenzione(descrizione);

    errors.validateMandatory(reiterati, FIELD_NAME_REITERATI);
    acdan.setReiteratiDanni(reiterati);

    acdan.setNote(note);
    
    return errors;
  }

  protected void common(HttpServletRequest request, HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> opt = new ArrayList<>();
    opt.add(new DecodificaDTO<String>("N", "N", "No"));
    opt.add(new DecodificaDTO<String>("S", "S", "Sì"));
    model.addAttribute("option", opt);
  }


}
