package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

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
@IuffiSecurity(value = "CU-IUFFI-133-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi133i")
public class CUIUFFI133IInserisciInterventi extends Inserisci
{
  @Override
  public String getFlagEscludiCatalogo()
  {
    return IuffiConstants.FLAGS.NO;
  }

  @Override
  protected List<DecodificaInterventoDTO> getListInterventiForInsert(
      long idProcedimentoOggetto, HttpServletRequest request) throws InternalUnexpectedException
  {
    return interventiEJB.getListInterventiPossibiliByIdProcedimentoOggetto(
        idProcedimentoOggetto);
  }

  @Override
  protected List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(
      List<Long> ids, long idBando) throws InternalUnexpectedException
  {
    return interventiEJB
        .getInfoInterventiPerInserimentoByIdDescrizioneIntervento(ids, idBando);
  }

@Override
public void aggiungiDannoAtmAlModel(Model model, HttpServletRequest request) {}

@Override
protected void insertInterventi(List<RigaModificaMultiplaInterventiDTO> list, HttpServletRequest request,
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException, ApplicationException
{
	interventiEJB.insertInterventi(list,null,logOperationOggettoQuadroDTO);
}

protected void getListDanniInterventi(Model model, long idProcedimentoOggetto) throws InternalUnexpectedException
{
	//non si gestiscono danni
}

}