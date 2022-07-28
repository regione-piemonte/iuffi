package it.csi.iuffi.iuffiweb.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.AccertamentoSpeseDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DocumentoAllegatoDownloadDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoRendicontazioneDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.PotenzialeErogabileESanzioniRendicontazioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONRendicontazioneSuperficiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaProspetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.TotaleContributoAccertamentoElencoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.UpdateSuperficieLocalizzazioneDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IRendicontazioneEAccertamentoSpeseEJB
    extends IIuffiAbstractEJB
{
  List<RigaRendicontazioneSpese> getElencoRendicontazioneSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException;

  void updateRendicontazioneSpese(List<RigaRendicontazioneSpese> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  List<RigaAccertamentoSpese> getElencoAccertamentoSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException;

  void updateAccertamentoSpeseAcconto(List<AccertamentoSpeseDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  Map<String, BigDecimal[]> getCalcoloImportiPerRendicontazioneSpese(
      long idProcedimentoOggetto, Date dataRiferimento, List<Long> ids)
      throws InternalUnexpectedException;

  List<TotaleContributoAccertamentoElencoDTO> getTotaleContributoErogabileNonErogabileESanzioniAcconto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  void updateAccertamentoSpeseSaldo(List<AccertamentoSpeseDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public PotenzialeErogabileESanzioniRendicontazioneDTO getPotenzialeErogabileETotaleSanzioniRendicontazione(
      long idProcedimentoOggetto, boolean isSaldo)
      throws InternalUnexpectedException;

  List<RigaProspetto> getElencoProspetto(long idProcedimento,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  BigDecimal getPercentualeMassimaContributoInRendicontazioneSpese(
      long idProcedimento, long idBandoOggetto)
      throws InternalUnexpectedException, ApplicationException;

  List<RigaJSONRendicontazioneSuperficiDTO> getRendicontazioniSuperficiJSON(
      long idProcedimentoOggetto,
      long idIntervento) throws InternalUnexpectedException;

  void updateSuperficieEffettivaLocalizzazioneIntervento(long idIntervento,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      List<UpdateSuperficieLocalizzazioneDTO> list)
      throws InternalUnexpectedException;

  void importaSuperficiGIS(
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, int anno)
      throws InternalUnexpectedException, ApplicationException;

  void updateSuperficieIstruttoriaLocalizzazioneIntervento(long idIntervento,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      List<UpdateSuperficieLocalizzazioneDTO> list)
      throws InternalUnexpectedException;

  Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> getRendicontazioneDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto,
      List<Long> idIntervento) throws InternalUnexpectedException;

  void updateRendicontazioneSpeseDocumenti(
      Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> interventi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException, ApplicationException;

  String getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  Map<Long, InterventoAccertamentoDocumentiSpesaDTO> getAccertamentoDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto,
      List<Long> idIntervento) throws InternalUnexpectedException;

  void updateAccertamentoSpeseDocumenti(
      Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException, ApplicationException;

  void updateAccertamentoSpeseSaldoDocumenti(
      Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException, ApplicationException;

  DocumentoAllegatoDownloadDTO getDocumentoSpesaCorrenteByIdProcedimentoOggetto(
      long idProcedimentoOggetto,
      long idDocumentoSpesa) throws InternalUnexpectedException;

  DocumentoAllegatoDownloadDTO getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria(
      long idProcedimento, long idDocumentoSpesa)
      throws InternalUnexpectedException;
}
