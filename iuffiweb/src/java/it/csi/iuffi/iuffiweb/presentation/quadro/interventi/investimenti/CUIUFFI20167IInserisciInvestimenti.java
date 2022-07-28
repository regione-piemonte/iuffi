package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.investimenti;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DecodificaInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Inserisci;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI20-167-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuIUFFI20167i")
public class CUIUFFI20167IInserisciInvestimenti extends Inserisci
{
  @Override
  public String getFlagEscludiCatalogo()
  {
    return IuffiConstants.FLAGS.SI;
  }

  @Override
  protected List<DecodificaInterventoDTO> getListInterventiForInsert(long idProcedimentoOggetto, HttpServletRequest request) throws InternalUnexpectedException
  {
    return interventiEJB.getListInvestimentiPossibili(idProcedimentoOggetto);
  }

  @Override
  protected List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(List<Long> ids, long idBando) throws InternalUnexpectedException
  {
    return interventiEJB.getInfoInvestimentiPerInserimentoByIdDescrizioneIntervento(ids);
  }



@Override
public void aggiungiDannoAtmAlModel(Model model, HttpServletRequest request)
{ }

@Override
protected void insertInterventi(List<RigaModificaMultiplaInterventiDTO> list, HttpServletRequest request,
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException, ApplicationException
{
	interventiEJB.insertInterventi(list,null,logOperationOggettoQuadroDTO);
}

@Override
protected void getListDanniInterventi(Model model, long idProcedimentoOggetto) throws InternalUnexpectedException
{
	//non gestisco danni
}
}