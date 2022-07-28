package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.investimenti;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Modifica;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI20-167-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuIUFFI20167m")
public class CUIUFFI20167MModificaInvestimenti extends Modifica
{
  @Override
  protected List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(List<Long> ids, final long idProcedimentoOggetto, final long idBando)
      throws InternalUnexpectedException
  {
    return interventiEJB.getInfoInvestimentiPerModifica(idProcedimentoOggetto, ids);
  }

  @RequestMapping(value = "/richiesto412_{idIntervento}")
  public String richiesto412(Model model, HttpServletRequest request, @PathVariable("idIntervento") long idIntervento) throws InternalUnexpectedException
  {
    interventiEJB.modificaFlagAssociatoAltraMisura(idIntervento, IuffiConstants.FLAGS.SI, getLogOperationOggettoQuadroDTO(request.getSession()));
    return "dialog/success";
  }

  @RequestMapping(value = "/nonrichiesto412_{idIntervento}")
  public String nonrichiesto412(Model model, HttpServletRequest request, @PathVariable("idIntervento") long idIntervento) throws InternalUnexpectedException
  {
    interventiEJB.modificaFlagAssociatoAltraMisura(idIntervento, IuffiConstants.FLAGS.NO, getLogOperationOggettoQuadroDTO(request.getSession()));
    return "dialog/success";
  }
}