package it.csi.iuffi.iuffiweb.presentation.superficicolture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.ControlloColturaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioPsrDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-299-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi299m")
public class CUIUFFI299MModificaSuperficiColture extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index_{idSuperficieColtura}", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model,
			  @PathVariable("idSuperficieColtura") long idSuperficieColtura) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  SuperficiColtureDettaglioDTO superficiColturaDettaglio = quadroIuffiEJB.getSuperficiColtureDettaglio(idProcedimentoOggetto,idSuperficieColtura);
		  SuperficiColtureDettaglioPsrDTO superficiColtureDettaglioPsrDTO = quadroIuffiEJB.getSuperficiColtureDettaglioPsrDTO(idProcedimentoOggetto, idSuperficieColtura);
		  List<DecodificaDTO<String>> listUnitaMisura = getDecodificaDTOUnitaMisura();
		  
		  model.addAttribute("idSuperficieColtura", idSuperficieColtura);
		  model.addAttribute("superficiColturaDettaglio",superficiColturaDettaglio);
		  model.addAttribute("superficiColtureDettaglioPsrDTO",superficiColtureDettaglioPsrDTO);
		  model.addAttribute("listUnitaMisura",listUnitaMisura);
		  return "superficicolture/modificaSuperficiColture";
	  }

	private List<DecodificaDTO<String>> getDecodificaDTOUnitaMisura()
	{
		List<DecodificaDTO<String>> qliUf = new ArrayList<DecodificaDTO<String>>();
		  DecodificaDTO<String> ufDD = new DecodificaDTO<String>();
		  DecodificaDTO<String> qliDD = new DecodificaDTO<String>();
		  qliDD.setId("qli");
		  qliDD.setCodice("qli");
		  qliDD.setDescrizione("Quintali");
		  ufDD.setId("uf");
		  ufDD.setCodice("uf");
		  ufDD.setDescrizione("Unità foraggere");
		  qliUf.add(qliDD);
		  qliUf.add(ufDD);
		return qliUf;
	}
	
	  @RequestMapping(value = "/conferma_modifica", method = RequestMethod.POST)
	  public String confermaModifica(HttpSession session, HttpServletRequest request, Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  Errors errors = new Errors();
		  BigDecimal produzioneHa,giornateLavorate,prezzo;
		  BigDecimal qliReimpiegati=null,ufReimpiegate=null;
		  

		  String fieldNameIdSuperficieColtura = "idSuperficieColtura";
		  String fieldNameProduzioneHa = "txtProduzioneHa";
		  String fieldNameGiornateLavorate = "txtGiornateLavorate";
		  String fieldNameReimpieghiUnitaMisura = "slcUnitaMisura";
		  String fieldNameReimpieghiQnt = "txtReimpieghiQnt";
		  String fieldNamePrezzo = "txtPrezzo";
		  String fieldNameNote = "txtNote";
		
		  String fieldIdSuperficieColtura = request.getParameter(fieldNameIdSuperficieColtura);
		  long idSuperficieColtura = Long.parseLong(fieldIdSuperficieColtura);
		  
		  String fieldProduzioneHa = request.getParameter(fieldNameProduzioneHa);
		  String fieldGiornateLavorate= request.getParameter(fieldNameGiornateLavorate);
		  String fieldReimpieghiUnitaMisura = request.getParameter(fieldNameReimpieghiUnitaMisura);//
		  String fieldReimpieghiQnt = request.getParameter(fieldNameReimpieghiQnt);//
		  String fieldPrezzo = request.getParameter(fieldNamePrezzo);
		  String fieldNote = request.getParameter(fieldNameNote);
		  
		  produzioneHa = errors.validateMandatoryBigDecimalInRange(fieldProduzioneHa, fieldNameProduzioneHa,2, new BigDecimal("0.0"), new BigDecimal("999999.99"));
		  giornateLavorate = errors.validateMandatoryBigDecimalInRange(fieldGiornateLavorate, fieldNameGiornateLavorate, 1, new BigDecimal("0.0"), new BigDecimal("99999.9"));
		  prezzo = errors.validateMandatoryBigDecimalInRange(fieldPrezzo, fieldNamePrezzo, 2, new BigDecimal("0.0"), new BigDecimal("9999.99"));
		  errors.validateFieldMaxLength(fieldNote, fieldNameNote, 4000);
		  SuperficiColtureDettaglioPsrDTO superficieColtura = quadroIuffiEJB.getSuperficiColtureDettaglioPsrDTO(idProcedimentoOggetto, idSuperficieColtura);

		  if(fieldReimpieghiUnitaMisura.equals("qli") || fieldReimpieghiUnitaMisura.equals("uf"))
		  {
			  
			  BigDecimal prodTotale = new BigDecimal("999999999");
			  if(produzioneHa != null)
			  {
				  prodTotale = produzioneHa.multiply(superficieColtura.getSumSuperficieUtilizzata());
			  }
			  if(fieldReimpieghiUnitaMisura.equals("qli"))
			  {
				  qliReimpiegati = errors.validateMandatoryBigDecimalInRange(fieldReimpieghiQnt, fieldNameReimpieghiQnt,2,new BigDecimal("0.0"), prodTotale.min(new BigDecimal("999999.99")).setScale(2, RoundingMode.HALF_UP));
			  }
			  else if(fieldReimpieghiUnitaMisura.equals("uf"))
			  {
				 if(superficieColtura.getUfProdotte().compareTo(new BigDecimal("0.0")) <= 0)
				  {
					  String[] listUnitaMisuraValide = new String[2];
					  listUnitaMisuraValide[0]="";
					  listUnitaMisuraValide[1]="qli";
					  errors.validateMandatoryValueList(fieldReimpieghiUnitaMisura, fieldNameReimpieghiUnitaMisura, listUnitaMisuraValide);
				  }
				  ufReimpiegate = errors.validateMandatoryBigDecimalInRange(fieldReimpieghiQnt, fieldNameReimpieghiQnt,0,new BigDecimal("0.0"), prodTotale.min(new BigDecimal("99999999")).setScale(0, RoundingMode.HALF_UP));
			  }
		  }
		  else
		  {
			  errors.validateMandatoryBigDecimalInRange(fieldReimpieghiQnt, fieldNameReimpieghiQnt,0,new BigDecimal("0.0"), new BigDecimal("0.0"));
		  }
		  
		  if(errors.addToModelIfNotEmpty(model))
		  {
			  model.addAttribute("preferRequest", Boolean.TRUE);  
			  return index(session, model, idSuperficieColtura);
		  }
		  else
		  {
			  SuperficiColtureDettaglioPsrDTO superficieColturaDettaglioDTO = new SuperficiColtureDettaglioPsrDTO();
			  List<ControlloColturaDTO> listControlloColtura = new ArrayList<ControlloColturaDTO>();
			  superficieColturaDettaglioDTO.setNote(fieldNote);
			  superficieColturaDettaglioDTO.setProduzioneHa(produzioneHa);
			  superficieColturaDettaglioDTO.setIdSuperficieColtura(idSuperficieColtura);
			  superficieColturaDettaglioDTO.setUfReimpiegate(ufReimpiegate);
			  superficieColturaDettaglioDTO.setGiornateLavorate(giornateLavorate);
			  superficieColturaDettaglioDTO.setPrezzo(prezzo);
			  if(qliReimpiegati == null || qliReimpiegati.compareTo(new BigDecimal("0")) == 0)
			  {
				  superficieColturaDettaglioDTO.setQliReimpiegati(null);
			  }
			  else
			  {
				  superficieColturaDettaglioDTO.setQliReimpiegati(qliReimpiegati);
			  }
			  if(ufReimpiegate == null || ufReimpiegate.compareTo(new BigDecimal("0")) == 0)
			  {
				  superficieColturaDettaglioDTO.setUfReimpiegate(null);
			  }
			  else
			  {
				  superficieColturaDettaglioDTO.setUfReimpiegate(ufReimpiegate);
			  }
			  SuperficiColtureDettaglioPsrDTO superficiColtureMinMax = quadroIuffiEJB.getSuperficiColtureDettaglioPsrDTO(idProcedimentoOggetto, idSuperficieColtura);
			  if(produzioneHa.compareTo(superficiColtureMinMax.getProduzioneHaMin())<0 || 
					  produzioneHa.compareTo(superficiColtureMinMax.getProduzioneHaMax())>0)
			  {
				  ControlloColturaDTO controlloColtura = new ControlloColturaDTO();
				  controlloColtura.setIdSuperficieColtura(idSuperficieColtura);
				  controlloColtura.setDescrizioneAnomalia("La prodizione q/ha deve essere un valore compreso tra " + superficiColtureMinMax.getProduzioneHaMin() + " e " + superficiColtureMinMax.getProduzioneHaMax());
				  controlloColtura.setBloccante("N");
				  listControlloColtura.add(controlloColtura);
			  }
			  if(giornateLavorate.compareTo(superficiColtureMinMax.getGiornateLavorateMin())< 0 ||
					  giornateLavorate.compareTo(superficiColtureMinMax.getGiornateLavorateMax()) > 0)
			  {
				  ControlloColturaDTO controlloColtura = new ControlloColturaDTO();
				  controlloColtura.setIdSuperficieColtura(idSuperficieColtura);
				  controlloColtura.setDescrizioneAnomalia("Le giornate lavorative ad ha devono essere un valore compreso tra " + superficiColtureMinMax.getGiornateLavorateMin() + " e " + superficiColtureMinMax.getGiornateLavorateMax());
				  controlloColtura.setBloccante("N");
				  listControlloColtura.add(controlloColtura);
			  }
			  if(prezzo.compareTo(superficiColtureMinMax.getPrezzoMin())<0 || 
					  prezzo.compareTo(superficiColtureMinMax.getPrezzoMax())>0 )
			  {
				  ControlloColturaDTO controlloColtura = new ControlloColturaDTO();
				  controlloColtura.setIdSuperficieColtura(idSuperficieColtura);
				  controlloColtura.setDescrizioneAnomalia("Il prezzo/q deve essere un valore compreso tra " + superficiColtureMinMax.getPrezzoMin() + " e " + superficiColtureMinMax.getPrezzoMax());
				  controlloColtura.setBloccante("N");
				  listControlloColtura.add(controlloColtura);				  
			  }
			  LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
			  quadroIuffiEJB.modificaSuperficieColtura(idProcedimentoOggetto,superficieColturaDettaglioDTO,listControlloColtura,logOperationOggettoQuadroDTO);
			  return "redirect:../cuiuffi299l/index.do";
		  }
	  }
}
