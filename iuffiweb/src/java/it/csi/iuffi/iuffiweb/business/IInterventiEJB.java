package it.csi.iuffi.iuffiweb.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDualLIstDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.AccertamentoSpeseDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DatiLocalizzazioneParticellarePerStampa;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DecodificaInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DettaglioInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FileAllegatoInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaParticelle;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoRiduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONAllegatiInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONParticellaInteventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.SuperficieConduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.punteggi.RaggruppamentoLivelloCriterio;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RangePercentuale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoByLivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.ZonaAltimetricaDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IInterventiEJB extends IIuffiAbstractEJB
{
  List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerInserimentoByIdDescrizioneIntervento(
      List<Long> listIdDescrizioneIntervento, long idBando)
      throws InternalUnexpectedException;

  List<RigaElencoInterventi> getElencoInterventiProcedimentoOggetto(
      long idProcedimentoOggetto, String flagEscludiCatalogo, Date dataValidita)
      throws InternalUnexpectedException;

  void insertInterventi(
	  List<RigaModificaMultiplaInterventiDTO> listInterventi,
	  Long idDannoAtm,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException, ApplicationException;

  void insertLocalizzazioneComuni(long idProcedimOggettoQuadro,
      long idIntervento, String[] ids,
      LogOperationOggettoQuadroDTO logOperationDTO,String flagCanale, String flagOpereDiPresa, String flagCondotta )
      throws InternalUnexpectedException;

  Map<String, Map<String, String>> getMapComuniPiemontesiNonInInterventoForJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException;

  Map<String, Map<String, String>> getMapComuniPiemontesiInInterventoForJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException;

  void eliminaIntervento(long idProcedimentoOggetto, List<Long> ids,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(
      long idProcedimentoOggetto, List<Long> idIntervento, long idBando)
      throws InternalUnexpectedException;

  void updateInterventi(List<RigaModificaMultiplaInterventiDTO> list,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  Map<String, Map<String, String>> getMapComuniParticelleNonInInterventoForJSON(
      long idProcedimentoOggetto, long idIntervento, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  List<RigaJSONConduzioneInteventoDTO> getElencoConduzioniJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException;

  List<RigaJSONParticellaInteventoDTO> getElencoParticelleJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException;

  List<RigaJSONParticellaInteventoDTO> ricercaParticelle(
      FiltroRicercaParticelle filtroRicercaParticelle)
      throws InternalUnexpectedException;

  void insertLocalizzazioneParticelle(long idProcedimentoOggetto,
      long idIntervento, List<Long> idParticellaCertificata,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  DettaglioInterventoDTO getDettaglioIntervento(long idProcedimentoOggetto,
      long idIntervento, String flagEscludiCatalogo, Date dataValidita)
      throws InternalUnexpectedException;

  List<RigaJSONAllegatiInterventoDTO> getAllegatiJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException;

  void insertLocalizzazioneMappeFile(long idProcedimentoOggetto,
      Long idIntervento, FileAllegatoInterventoDTO file,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  FileAllegatoInterventoDTO getFileFisicoAllegato(long idProcedimentoOggetto,
      long idIntervento, long idFileAllegatiIntervento)
      throws InternalUnexpectedException;

  void eliminaAllegati(long idProcedimentoOggetto, long idIntervento,
      List<Long> idFileAllegatiIntervento,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  List<DatiLocalizzazioneParticellarePerStampa> getLocalizzazioneParticellePerStampa(
      long idProcedimentoOggetto, String flag)
      throws InternalUnexpectedException;

  Map<String, Map<String, String>> getMapComuniUteNonInInterventoForJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException;

  boolean isBandoConPercentualeRiduzione(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  void updatePercentualeRibassoInterventi(long idProcedimentoOggetto,
      BigDecimal percentuale) throws InternalUnexpectedException;

  InfoRiduzione getInfoRiduzione(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  List<RigaJSONConduzioneInteventoDTO> getElencoConduzioniJSON(
      long idProcedimentoOggetto, String[] idChiaveConduzione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  void insertLocalizzazioneConduzioni(long idProcedimentoOggetto,
      long idIntervento, String[] idChiaveConduzione,
      LogOperationOggettoQuadroDTO logOperationDTO, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  void insertLocalizzazioneConduzioni(long idProcedimentoOggetto,
      long idIntervento, List<SuperficieConduzione> superfici,
      LogOperationOggettoQuadroDTO logOperationDTO, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  List<RigaJSONInterventoQuadroEconomicoDTO> getElencoInterventiQuadroEconomico(
      long idProcedimentoOggetto, Date dataValidita)
      throws InternalUnexpectedException;

  void updateInterventiQuadroEconomico(
      List<RigaJSONInterventoQuadroEconomicoDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  List<RigaJSONInterventoQuadroEconomicoDTO> getInterventiQuadroEconomicoPerModifica(
      long idProcedimentoOggetto, List<Long> idIntervento)
      throws InternalUnexpectedException;

  void modificaPercentualeInterventoQuadroEconomico(long[] idIntervento,
      Map<Long, BigDecimal> mapInterventiPercentuali,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;


  List<RigaModificaMultiplaInterventiDTO> getInfoInvestimentiPerInserimentoByIdDescrizioneIntervento(
      List<Long> listIdDescrizioneIntervento)
      throws InternalUnexpectedException;

  List<RigaModificaMultiplaInterventiDTO> getInfoInvestimentiPerModifica(
      long idProcedimentoOggetto, List<Long> idIntervento)
      throws InternalUnexpectedException;

  public List<RangePercentuale> getRangePercentuali(long[] idIntervento)
      throws InternalUnexpectedException;

  List<RigaRendicontazioneSpese> getElencoRendicontazioneSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException;

  void updateRendicontazioneSpese(List<RigaRendicontazioneSpese> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  List<RigaAccertamentoSpese> getElencoAccertamentoSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException;

  void updateAccertamentoSpese(List<AccertamentoSpeseDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  void updateMisurazioniInterventoSaldo(
      List<RigaModificaMultiplaInterventiDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  List<DecodificaDTO<Long>> findAttivitaDisponibiliPerProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  List<DecodificaDTO<Long>> findPartecipantiAttivitaInProcedimentoOggetto(
      long idProcedimentoOggetto, long idAttivita)
      throws InternalUnexpectedException;

  List<RigaElencoInterventi> getElencoInterventiProcedimentoOggetto(
      long idProcedimentoOggetto,
      String flagEscludiCatalogo, boolean partecipanti, Date dataValidita)
      throws InternalUnexpectedException;

  List<DecodificaDualLIstDTO<Long>> getElencoInterventiPerDocSpesa(
      long idProcedimento, Vector<Long> idsDocSpesa, boolean disponibili)
      throws InternalUnexpectedException;

  List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> getElencoInterventiByLivelliQuadroEconomico(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  RigaElencoInterventi getDettaglioInterventoById(long idProcedimento,
      long idIntervento) throws InternalUnexpectedException;

  BigDecimal getSommamportiDocumentoSpesaIntervento(long idDocumentoSpesa)
      throws InternalUnexpectedException;
  

  List<DecodificaInterventoDTO> getListInterventiPossibiliByIdProcedimentoOggetto(long idProcedimentoOggetto)
      throws InternalUnexpectedException;
  
  List<DecodificaInterventoDTO> getListInterventiPossibiliDannoAtmByIdProcedimentoOggetto(long idProcedimentoOggetto, long idDannoAtm) 
  	throws InternalUnexpectedException;

  List<DecodificaInterventoDTO> getListInvestimentiPossibili(long idProcedimentoOggetto) throws InternalUnexpectedException;
  

  void modificaFlagAssociatoAltraMisura(long idIntervento, String flagAssociatoAltraMisura, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  ZonaAltimetricaDTO getZonaAltimetricaProcedimento(long idProcedimentoOggetto, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  List<String> getFlagCanaleOpereCondotta(long idProcedimentoOggetto, long idIntervento) throws InternalUnexpectedException;

  public int insertInterventoPrevenzioneIstruttoria(long idProcedimentoOggetto,
			List<RaggruppamentoLivelloCriterio> listaRaggruppamento, 
			LogOperationOggettoQuadroDTO logOperationOggettoQuadro) throws InternalUnexpectedException, ApplicationException;
  
  
  
  String getCodiceIdentificativoIntervento(Long idIntervento) throws InternalUnexpectedException;
  
}
