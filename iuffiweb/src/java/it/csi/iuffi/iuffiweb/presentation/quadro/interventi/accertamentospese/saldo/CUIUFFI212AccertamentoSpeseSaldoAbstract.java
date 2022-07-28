package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.accertamentospese.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import it.csi.iuffi.iuffiweb.business.IRendicontazioneEAccertamentoSpeseEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class CUIUFFI212AccertamentoSpeseSaldoAbstract extends BaseController
{
  @Autowired
  protected IRendicontazioneEAccertamentoSpeseEJB rendicontazioneEAccertamentoSpeseEJB;
  public static final String                      JSP_BASE_PATH = "interventi/accertamentospese/saldo/finale/";
  public static final String                      CU_BASE_NAME  = "cuiuffi212";

  public void addInfoRendicontazioneIVA(Model model, long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String flagRendicontazioneIVA = rendicontazioneEAccertamentoSpeseEJB
        .getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto(
            idProcedimentoOggetto);
    switch (flagRendicontazioneIVA)
    {
      case IuffiConstants.FLAGS.NO:
        model.addAttribute("msgIVA",
            "Il beneficiario NON può rendicontare l'IVA");
        break;
      case IuffiConstants.FLAGS.SI:
        model.addAttribute("msgIVA", "Il beneficiario può rendicontare l'IVA");
        break;
    }
  }
}
