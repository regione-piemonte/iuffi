package it.csi.iuffi.iuffiweb.business;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ImportiRipartitiListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.StampaListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiCreazioneListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiListaDaCreareDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.LivelliBandoDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoImportiApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoPraticheApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONBandiNuovaListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Local
public interface IListeLiquidazioneEJB extends IIuffiAbstractEJB
{
  List<RigaJSONElencoListaLiquidazioneDTO> getListeLiquidazione()
      throws InternalUnexpectedException;

  List<Map<String, Object>> getAmministrazioniCompetenzaListe()
      throws InternalUnexpectedException;

  List<RigaJSONBandiNuovaListaDTO> getBandiProntiPerListeLiquidazione(
      List<Long> lIdAmmCompetenza) throws InternalUnexpectedException;

  LivelliBandoDTO getLivelliBando(long idBando)
      throws InternalUnexpectedException;

  List<DecodificaDTO<Long>> findAmministrazioniInProcedimentiBando(long idBando,
      List<Long> lIdAmmCompetenza) throws InternalUnexpectedException;

  void deleteListaLiquidazione(Long idListaLiquidazione)
      throws InternalUnexpectedException;

  StampaListaLiquidazioneDTO getStampaListaLiquidazione(
      long idListaLiquidazione) throws InternalUnexpectedException;

  String getStatoListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException;

  Boolean ripristinaStampaListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException;

  void setStatoListaLiquidazione(long idListaLiquidazione, int stato)
      throws InternalUnexpectedException;

  byte[] getContenutoFileListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException;

  RigaJSONElencoListaLiquidazioneDTO getListaLiquidazioneById(
      long idListaLiquidazione) throws InternalUnexpectedException;

  List<ImportiRipartitiListaLiquidazioneDTO> getImportiRipartitiListaLiquidazione(
      long idListaLiquidazione) throws InternalUnexpectedException;

  List<DecodificaDTO<Long>> getTecniciLiquidatori(long idAmmCompetenza, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException;

  DatiListaDaCreareDTO getDatiListaDaCreare(long idBando, long idAmmCompetenza,
      long idTipoImporto, List<Long> idsPODaEscludere)
      throws InternalUnexpectedException;

  DatiCreazioneListaDTO creaListaLiquidazione(long idBando,
      long idAmmCompetenza, int idTipoImporto, Long idTecnicoLiquidatore,
      DatiListaDaCreareDTO datiListaDaCreareDTO, long idUtenteAggiornamento,
      List<Long> idsPODaEscludere, long idProcedimentoAgricolo)
      throws InternalUnexpectedException, ApplicationException;

  List<RiepilogoImportiApprovazioneDTO> getRiepilogoImportiApprovazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException;

  void aggiornaStatoLista(long idListaLiquidazione, String flagStatoLista,
      long idUtenteAggiornamento)
      throws InternalUnexpectedException, ApplicationException;

  ApplicationException approvaLista(long idListaLiquidazione,
      MultipartFile stampaFirmata, UtenteAbilitazioni utenteAbilitazioni)
      throws InternalUnexpectedException;

  public List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheInNuovaLista(
      long idBando, long idAmmCompetenza,
      int idTipoImporto)
      throws InternalUnexpectedException;

  String getTitoloListaLiquidazioneByIdBando(long idBando)
      throws InternalUnexpectedException;

  boolean isListaLiquidazioneCorrotta(long idListaLiquidazione)
      throws InternalUnexpectedException;

  boolean isContenutoFileListaLiquidazioneDisponibile(long idListaLiquidazione)
      throws InternalUnexpectedException;

  List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheInNuovaListaWithAnomalia(
      long idBando, long idAmmCompetenza, int idTipoImporto,
      long idTecnicoLiquidatore, long idProcedimentoAgricolo) throws InternalUnexpectedException;

  long getIdBandoByIdListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException;

  List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheListaLiquidazione(
      long idListaLiquidazione, boolean isPremio)
      throws InternalUnexpectedException;

  List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException;

}
