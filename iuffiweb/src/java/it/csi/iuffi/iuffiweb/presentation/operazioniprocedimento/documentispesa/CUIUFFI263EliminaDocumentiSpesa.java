package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.RicevutaPagamentoVO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi263e")
@IuffiSecurity(value = "CU-IUFFI-263-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI263EliminaDocumentiSpesa extends BaseController
{
  @Autowired
  IQuadroEJB              quadroEJB           = null;

  protected static String COMMON_SESSION_NAME = "CU-IUFFI-263";

  @IsPopup
  @RequestMapping(value = "/confermaElimina_{idDocumentoSpesa}", method = RequestMethod.GET)
  public String confermaElimina(
      @PathVariable("idDocumentoSpesa") long idDocumentoSpesa,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    model.addAttribute("idDocumentoSpesa", idDocumentoSpesa);
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        request.getSession(), false);
    String msgElimina = (String) common.get("msgElimina");
    model.addAttribute("msgElimina", msgElimina);
    common.remove("msgElimina");
    saveCommonInSession(common, request.getSession());

    return "documentispesa/confermaElimina";
  }

  @RequestMapping(value = "/elimina_{idDocumentoSpesa}", method = RequestMethod.GET)
  public String elimina(@PathVariable("idDocumentoSpesa") long idDocumentoSpesa,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {

    if (!quadroEJB.canDeleteDocSpesa(idDocumentoSpesa))
      return "Impossibile eliminare. Documento già associato ad una domanda.";

    quadroEJB.eliminaDocumentoSpesaById(idDocumentoSpesa);

    return "dialog/success";
  }

  @RequestMapping(value = "/canDeleteDoc_{idDocSpesa}", method = RequestMethod.GET)
  public String canDeleteDoc(@PathVariable("idDocSpesa") long idDocSpesa,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        request.getSession(), false);

    // checkAssociazioneInterventi
    if (quadroEJB.docHasInterventi(idDocSpesa))
    {
      common.put("msgElimina",
          "Il documento è già stato associato almeno in parte ad uno o più interventi. Se si conferma l'operazione saranno eliminate anche queste associazioni. Si desidera proseguire?");
      saveCommonInSession(common, request.getSession());
      return "dialog/success";
    }

    // checkRicevute
    long[] ids = new long[1];
    ids[0] = idDocSpesa;
    List<RicevutaPagamentoVO> ricevute = quadroEJB
        .getElencoRicevutePagamento(ids);
    if (ricevute != null && !ricevute.isEmpty())
    {
      common.put("msgElimina",
          "Il documento è già stato associato ad una o più ricevute di pagamento. Se si conferma l'operazione saranno eliminate anche queste associazioni. Si desidera proseguire?");
      saveCommonInSession(common, request.getSession());
      return "dialog/success";
    }

    common.put("msgElimina", "Eliminare il documento selezionato?");
    return "dialog/success";
  }

  // RICEVUTE PAGAMENTO

  protected static String COMMON_DATA_LIST_ID_DOCUMENTI = "listIdDocumentiSpesa";

  @IsPopup
  @RequestMapping(value = "/confermaEliminaRicevuta_{idDettRicevutaPagamento}", method = RequestMethod.GET)
  public String confermaEliminaRicevuta(
      @PathVariable("idDettRicevutaPagamento") long idDettRicevutaPagamento,
      HttpServletRequest request,
      Model model) throws InternalUnexpectedException
  {
    model.addAttribute("idDettRicevutaPagamento", idDettRicevutaPagamento);
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        request.getSession(), false);
    if (common.containsKey("isDocGiaRendicontato"))
    {
      model.addAttribute("giaRend", "S");
    }

    if (common.containsKey(COMMON_DATA_LIST_ID_DOCUMENTI))
    {
      long[] ids = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
      model.addAttribute("idDocSpesa", ids[0]);
    }

    if (common.containsKey("msgElimina"))
    {
      String msgElimina = (String) common.get("msgElimina");
      model.addAttribute("msgElimina", msgElimina);
      common.remove("msgElimina");
    }
    else
    {
      model.addAttribute("msgElimina", "Eliminare la ricevuta selezionata?");
    }

    return "documentispesa/confermaEliminaRicevuta";
  }

  protected static String COMMON_ID_DETTAGLIO_RICEVUTA = "modIdDettRicevutaPagamento";

  @RequestMapping(value = "/eliminaRicevuta_{idDettRicevutaPagamento}", method = RequestMethod.GET)
  public String eliminaRicevuta(
      @PathVariable("idDettRicevutaPagamento") long idDettRicevutaPagamento,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        request.getSession(), false);
    if (common.containsKey("isDocGiaRendicontato"))
    {
      if (quadroEJB
          .canDeleteRicevutaDocGiaRendicontato(idDettRicevutaPagamento))
        quadroEJB.eliminaRicevutaSpesaById(idDettRicevutaPagamento);
      if (common.containsKey(COMMON_ID_DETTAGLIO_RICEVUTA))
        common.remove(COMMON_ID_DETTAGLIO_RICEVUTA);
      return "dialog/success";
    }
    else
      if (quadroEJB.canDeleteRicevuta(idDettRicevutaPagamento))
      {
        quadroEJB.eliminaRicevutaSpesaById(idDettRicevutaPagamento);
        if (common.containsKey(COMMON_ID_DETTAGLIO_RICEVUTA))
          common.remove(COMMON_ID_DETTAGLIO_RICEVUTA);
        return "dialog/success";
      }

    return "dialog/error";

  }

  @RequestMapping(value = "/canDeleteRicevuta_{idDettRicevutaPagamento}", method = RequestMethod.GET)
  @ResponseBody
  public String canDeleteRicevuta(
      @PathVariable("idDettRicevutaPagamento") long idDettRicevutaPagamento,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        request.getSession(), false);
    // ricevuta gia usata per rendicontazione (IUF_R_DOC_SPESA_PROC_OGG )
    Long idRicevutaPagamento = quadroEJB
        .getIdRicevutaPagamento(idDettRicevutaPagamento);
    if (quadroEJB.ricEsisteInDocSpesRicPag(idRicevutaPagamento))
    {
      return "La ricevuta di pagamento è già stata utilizzata per la rendicontazione spese, per cui non è più possibile eliminarla.";
    }

    if (quadroEJB.ricEsisteInDocSpesIntRicPag(idRicevutaPagamento))
    {
      common.put("msgElimina",
          "La ricevuta di pagamento è già stata associata almeno in parte ad uno o più interventi. Se si conferma l'operazione saranno eliminate anche queste associazioni. Si desidera proseguire?");
      return "<success>";
    }

    return "<success>";
  }

  /*
   * GESTIONE ELIMINAZIONE FILE DEL DOCUMENTO DI SPESA GIA RENDICONTATO
   */

  @RequestMapping(value = "eliminaAllegato_{idDocumentoSpesaFile}", produces = "application/json")
  @ResponseBody
  public String eliminaAllegato(
      @PathVariable("idDocumentoSpesaFile") long idDocumentoSpesaFile,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    if (quadroEJB.canDeleteAndDeleteFile(idDocumentoSpesaFile))
    {
      return "success";
    }
    return "ERR";
  }

  @IsPopup
  @RequestMapping(value = "confermaEliminaAllegato_{idDocumentoSpesaFile}", method = RequestMethod.GET)
  public String confermaEliminaAllegato(
      @PathVariable("idDocumentoSpesaFile") long idDocumentoSpesaFile,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    model.addAttribute("idDocumentoSpesaFile", idDocumentoSpesaFile);
    return "documentispesa/confermaEliminaAllegato";
  }

  @RequestMapping(value = "canDeleteAllegato_{idDocumentoSpesaFile}", produces = "application/json")
  @ResponseBody
  public String canDeleteAllegato(
      @PathVariable("idDocumentoSpesaFile") long idDocumentoSpesaFile,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    if (quadroEJB.canDeleteAllegato(idDocumentoSpesaFile))
    {
      return "success";
    }
    return "ERR";
  }

}