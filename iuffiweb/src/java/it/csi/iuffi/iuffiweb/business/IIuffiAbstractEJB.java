package it.csi.iuffi.iuffiweb.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoEProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public interface IIuffiAbstractEJB
{
  public List<DecodificaDTO<Integer>> getTabellaDecodifica(String nomeTabella,
      boolean orderByDesc) throws InternalUnexpectedException;

  public Map<String, String> getParametri(String[] paramNames)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getProvincie(String idRegione)
      throws InternalUnexpectedException;
  
  public List<DecodificaDTO<String>> getSiglaProvince(String idRegione)
      throws InternalUnexpectedException;
  
  public List<String> getProvinceCodici(final String idRegione)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getProvincie(String idRegione,
      boolean visualizzaStatiEsteri) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getDecodificheComuni(String idRegione,
      String istatProvincia, String flagEstinto)
      throws InternalUnexpectedException;

  public List<ComuneDTO> getDecodificheComuniWidthProv(String idRegione,
      String istatProvincia, String flagEstinto)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getSezioniPerComune(String istatComune)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getProvincieConTerreniInConduzione(
      long idProcedimentoOggetto, String istatRegione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getComuniPerProvinciaConTerreniInConduzione(
      long idProcedimentoOggetto, String istatProvincia, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getSezioniPerComuneConTerreniInConduzione(
      long idProcedimentoOggetto, String istatProvincia, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  public List<RigaJSONConduzioneInteventoDTO> ricercaConduzioni(
      FiltroRicercaConduzioni filtro, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public String getParametroComune(String idParametro)
      throws InternalUnexpectedException;

  public Map<String, String> getParametriComune(String... idParametro)
      throws InternalUnexpectedException;

  public LogOperationOggettoQuadroDTO getIdUtenteUltimoModifica(
      long idProcediemntoOggetto, long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException;

  public String getHelpCdu(String codcdu, Long idQuadroOggetto)
      throws InternalUnexpectedException;

  public Map<String, String> getMapHelpCdu(String... codcdu)
      throws InternalUnexpectedException;

  public List<String[]> getStatoDatabase() throws InternalUnexpectedException;

  public Date getSysDate() throws InternalUnexpectedException;

  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException;

  List<ComuneDTO> getComuni(String idRegione, String istatProvincia,
      String flagEstinto, String flagEstero) throws InternalUnexpectedException;

  ComuneDTO getComune(String istatComune) throws InternalUnexpectedException;

  ProcedimentoEProcedimentoOggetto getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto, boolean ricaricaQuadri)
      throws InternalUnexpectedException;

  public Procedimento getProcedimento(long idProcedimento)
      throws InternalUnexpectedException;

  public List<ComuneDTO> getDecodificheComuniWidthProvByComune(
      String denominazioneComune, String flagEstinto)
      throws InternalUnexpectedException;

  public int getIdProcedimentoAgricoloByIdProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

}
