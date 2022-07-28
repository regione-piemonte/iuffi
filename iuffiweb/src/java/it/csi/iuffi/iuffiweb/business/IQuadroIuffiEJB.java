package it.csi.iuffi.iuffiweb.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.DocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.dto.PraticaConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.ReferenteProgettoDTO;
import it.csi.iuffi.iuffiweb.dto.SezioneDocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.SoggettoDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDettaglioPlvDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.ProduzioneCategoriaAnimaleDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.UtilizzoReseDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DannoDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.IstitutoDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.ParticelleFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.RiepilogoDannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.dannicolture.DanniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.datibilancio.DatiBilancioDTO;
import it.csi.iuffi.iuffiweb.dto.fabbricati.FabbricatiDTO;
import it.csi.iuffi.iuffiweb.dto.gestionelog.LogDTO;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.ImportoLicenzaDTO;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.VersamentoLicenzaDTO;
import it.csi.iuffi.iuffiweb.dto.motoriagricoli.MotoriAgricoliDTO;
import it.csi.iuffi.iuffiweb.dto.prestitiagrari.PrestitiAgrariDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.CaratteristicheAziendali;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.RicercaDistretto;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.ControlloColturaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioParticellareDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioPsrDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColturePlvVegetaleDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureRiepilogoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ws.regata.DatiAziendaDTO;

@Local
public interface IQuadroIuffiEJB extends IIuffiAbstractEJB
{

  public List<ScorteDTO> getListaScorteByProcedimentoOggetto(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public long getIdStatoProcedimento(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoTipologieScorte() throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getListUnitaDiMisura() throws InternalUnexpectedException;

  public Long getUnitaMisuraByScorta(long idScorta) throws InternalUnexpectedException;

  public long getIdScorteAltro() throws InternalUnexpectedException;

  public long inserisciScorte(long idProcedimentoOggetto, long idScorta, BigDecimal quantita, Long idUnitaMisura, String descrizione, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public long modificaScorte(List<ScorteDTO> listScorte, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, long idProcedimentoOggetto) throws InternalUnexpectedException;

  public ScorteDTO getScortaByIdScortaMagazzino(long idScortaMagazzino) throws InternalUnexpectedException;

  public long eliminaScorte(List<Long> listIdScortaMagazzino, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, long idProcedimentoOggetto) throws InternalUnexpectedException, ApplicationException;

  public List<DanniDTO> getListaDanniByProcedimentoOggetto(long idProcedimentoOggetto, int idProcedimentoAgricolo) throws InternalUnexpectedException;

  public List<DanniDTO> getListDanniByIdsProcedimentoOggetto(long idProcedimentoOggetto, long[] arrayIdDannoAtm, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public long eliminaDanni(List<Long> listIdDannoAtm, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, long idProcedimentoOggetto) throws InternalUnexpectedException, ApplicationException;

  public List<ScorteDTO> getScorteByIds(long[] arrayIdScortaMagazzino, long idProcedimentoOggetto) throws InternalUnexpectedException;

  public long inserisciDanni(List<DanniDTO> listDanniDTO, long idProcedimentoOggetto, Integer idDanno, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public int inserisciDanniConduzioni(DanniDTO danno, long idProcedimentoOggetto, long[] arrayIdUtilizzoDichiarato, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public UnitaMisuraDTO getUnitaMisuraByIdDanno(Integer idDanno) throws InternalUnexpectedException;

  public DannoDTO getDannoByIdDanno(int idDanno) throws InternalUnexpectedException;

  public List<DanniDTO> getDanniByIdDannoAtm(long[] arrayIdDannoAtm, long idProcedimentoOggetto, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public int modificaDanni(List<DanniDTO> listDanni, long idProcedimentoOggetto, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public int modificaDanniConduzioni(DanniDTO danno, long idProcedimentoOggetto, long[] arrayIdUtilizzoDichiarato, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public Map<Long, Long> getMapTipologiaScorteUnitaDiMisura() throws InternalUnexpectedException;

  public List<MotoriAgricoliDTO> getListMotoriAgricoli(long idProcedimentoOggetto, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public List<MotoriAgricoliDTO> getListMotoriAgricoli(long idProcedimentoOggetto, long[] arrayIdMacchina, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public List<MotoriAgricoliDTO> getListMotoriAgricoliNonDanneggiati(long idProcedimentoOggetto, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public List<MotoriAgricoliDTO> getListMotoriAgricoliNonDanneggiati(long[] arrayIdMacchina, long idProcedimentoOggetto, int idProcedimentoAgricoltura) throws InternalUnexpectedException;

  public List<ScorteDTO> getListaScorteNonDanneggiateByProcedimentoOggetto(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<PrestitiAgrariDTO> getListPrestitiAgrari(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<PrestitiAgrariDTO> getListPrestitiAgrari(long idProcedimentoOggetto, long[] arrayIdPrestitiAgrari) throws InternalUnexpectedException;

  public long inserisciPrestitoAgrario(PrestitiAgrariDTO prestito, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public int eliminaPrestitiAgrari(List<Long> listIdPrestitiAgrari, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, long idProcedimentoOggetto) throws InternalUnexpectedException;

  public int modificaPrestitiAgrari(long idProcedimentoOggetto, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, List<PrestitiAgrariDTO> listPrestitiAgrari) throws InternalUnexpectedException;

  public List<FabbricatiDTO> getListFabbricati(long idProcedimentoOggetto, int idProcedimentoAgricolo) throws InternalUnexpectedException;

  public FabbricatiDTO getFabbricato(long idProcedimentoOggetto, long idFabbricato, int idProcedimentoAgricolo) throws InternalUnexpectedException;

  public Long getNDanniScorte(long idProcedimentoOggetto, long[] arrayIdScortaMagazzino) throws InternalUnexpectedException;

  public List<FabbricatiDTO> getListFabbricatiNonDanneggiati(long idProcedimentoOggetto, long[] arrayIdFabbricato, int idProcedimentoAgricolo) throws InternalUnexpectedException;

  public SuperficiColtureRiepilogoDTO getSuperficiColtureRiepilogo(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<SuperficiColtureDettaglioDTO> getListSuperficiColtureDettaglio(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public SuperficiColtureDettaglioDTO getSuperficiColtureDettaglio(long idProcedimentoOggetto, long idSuperficieColtura) throws InternalUnexpectedException;

  public List<ControlloColturaDTO> getListControlloColtura(long idProcedimentoOggetto, long[] arrayIdSuperficieColtura) throws InternalUnexpectedException;

  public SuperficiColtureDettaglioPsrDTO getSuperficiColtureDettaglioPsrDTO(long idProcedimentoOggetto, long idSuperficieColtura) throws InternalUnexpectedException;

  public List<SuperficiColtureDettaglioParticellareDTO> getListDettaglioParticellareSuperficiColture(long idProcedimentoOggetto, long idSuperficieColtura) throws InternalUnexpectedException;

  public void modificaSuperficieColtura(long idProcedimentoOggetto, SuperficiColtureDettaglioPsrDTO superficieColturaDettaglioDTO, List<ControlloColturaDTO> listControlloColtura, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public List<SuperficiColturePlvVegetaleDTO> getListSuperficiColturePlvVegetale(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getListProvinciaConTerreniInConduzione(long idProcedimentoOggetto, String ID_REGIONE_PIEMONTE) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getListComuniPerProvinciaConTerreniInConduzione(long idProcedimentoOggetto, String istatProvincia) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getListSezioniPerComuneDanniSuperficiColture(long idProcedimentoOggetto, String istatComune) throws InternalUnexpectedException;

  public List<ParticelleDanniDTO> getListConduzioniDannoEscludendoGiaSelezionate(long idProcedimentoOggetto, FiltroRicercaConduzioni filtroRicercaConduzioni, boolean piantagioniArboree) throws InternalUnexpectedException;

  public List<ParticelleDanniDTO> getListConduzioniEscludendoGiaSelezionate(long idProcedimentoOggetto, FiltroRicercaConduzioni filtroRicercaConduzioni) throws InternalUnexpectedException;

  public List<ParticelleDanniDTO> getListConduzioniDannoGiaSelezionate(long idProcedimentoOggetto, long[] arrayIdUtilizzoDichiarato, boolean piantagioniArboree) throws InternalUnexpectedException;

  public List<ParticelleDanniDTO> getListConduzioniDanno(long idProcedimentoOggetto, long idDannoAtm) throws InternalUnexpectedException;

  public List<ParticelleDanniDTO> getListConduzioniDanni(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public BigDecimal getSumSuperficiCatastaliParticelle(long idProcedimentoOggetto, long[] arrayIdUtilizzoDichiarato) throws InternalUnexpectedException;

  public List<AllevamentiDTO> getListAllevamenti(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<AllevamentiDTO> getListAllevamentiSingoliNonDanneggiati(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<AllevamentiDTO> getListAllevamentiSingoliNonDanneggiati(long idProcedimentoOggetto, long[] arrayIdAllevamento) throws InternalUnexpectedException;

  public List<AllevamentiDTO> getListAllevamentiSingoli(long idProcedimentoOggetto, long[] arrayIdAllevamento) throws InternalUnexpectedException;

  public AllevamentiDTO getDettaglioAllevamento(long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune) throws InternalUnexpectedException;

  public List<AllevamentiDettaglioPlvDTO> getListDettaglioAllevamenti(long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune) throws InternalUnexpectedException;

  public List<DecodificaDTO<Integer>> getListProduzioniVendibili(long idCategoriaAnimale) throws InternalUnexpectedException;

  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioniCategorieAnimali(long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune) throws InternalUnexpectedException;

  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioniVendibiliGiaInserite(long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune) throws InternalUnexpectedException;

  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioni(long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune) throws InternalUnexpectedException;

  public void inserisciProduzioneZootecnicaEProduzioniVendibili(long idProcedimentoOggetto, AllevamentiDTO produzioneZootecnica, List<ProduzioneCategoriaAnimaleDTO> listProduzioniVendibili, long idUtenteLogin, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public List<DecodificaDTO<Integer>> getListUnitaMisuraProduzioniVendibili(long idCategoriaAnimale) throws InternalUnexpectedException;

  public BigDecimal getPlvZootecnicaUfProdotte(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public BigDecimal getPlvZootecnicaUfNecessarie(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public BigDecimal getPlvZootecnicaUba(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public BigDecimal getPlvZootecnicaSau(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<AllevamentiDettaglioPlvDTO> getListPlvZootecnicaDettaglioAllevamenti(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<AllevamentiDTO> getListAllevamentiByIdDannoAtm(long idProcedimentoOggetto, long[] arrayIdAllevamento) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getListDanniDecodificaDTO(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public long getNInterventiAssociatiDanni(long idProcedimentoOggetto, long[] arrayIdDannoAtm) throws InternalUnexpectedException;

  public long getNInterventiAssociatiDanniScorte(long idProcedimentoOggetto, List<Long> listIdScortaMagazzino) throws InternalUnexpectedException;

  public ColtureAziendaliDTO getRiepilogoColtureAziendali(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public ColtureAziendaliDTO getRiepilogoColtureAziendali(long idProcedimentoOggetto, long idProcedimento) throws InternalUnexpectedException;

  public List<ColtureAziendaliDettaglioDTO> getListColtureAziendali(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<ColtureAziendaliDettaglioDTO> getListColtureAziendali(long idProcedimentoOggetto, long[] arrayIdSuperficieColture) throws InternalUnexpectedException;
  
  public UtilizzoReseDTO getUtilizzoRese(ColtureAziendaliDettaglioDTO coltura) throws InternalUnexpectedException;
  
//  public Integer getCountBandoUtilizzoDanno(long idBando, long extIdUtilizzo) throws InternalUnexpectedException;

  public void updateColtureAziendali(long idProcedimentoOggetto, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, List<ColtureAziendaliDettaglioDTO> listColtureAziendaliModificate) throws InternalUnexpectedException;

  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public AssicurazioniColtureDTO getRiepilogoAssicurazioniColture(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(long idProcedimentoOggetto, long[] idAssicurazioniColture) throws InternalUnexpectedException;

  public int eliminaAssicurazioniColture(long idProcedimentoOggetto, long[] arrayIdAssicurazioniColture) throws InternalUnexpectedException;

  public long inserisciAssicurazioniColture(long idProcedimentoOggetto, AssicurazioniColtureDTO assicurazioniColture, LogOperationOggettoQuadroDTO logOperationOggettoQuadro) throws InternalUnexpectedException;

  public List<DecodificaDTO<Integer>> getListConsorzi(String idProvincia) throws InternalUnexpectedException;

  public long modificaAssicurazioniColture(long idProcedimentoOggetto, AssicurazioniColtureDTO assicurazioniColture, LogOperationOggettoQuadroDTO logOperationOggettoQuadro) throws InternalUnexpectedException;;

  public List<DanniColtureDTO> getListDanniColture(long idProcedimentoOggetto, long idProcedimento) throws InternalUnexpectedException;

  public Long getNColtureDanneggiate(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void deleteRTipoDocumentiRichiesti(long idDocumentiRichiesti, long idTipoDocumentiRichiesti) throws InternalUnexpectedException;

  public List<SezioneDocumentiRichiestiDTO> getListDocumentiRichiestiDaVisualizzare(long idProcedimentoOggetto, Boolean isVisualizzazione) throws InternalUnexpectedException;

  public List<DocumentiRichiestiDTO> getDocumentiRichiesti(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public int aggiornaDocumentiRichiesti(long idProcedimentoOggetto, List<String> requestList, String HValue) throws InternalUnexpectedException;

  public ReferenteProgettoDTO getReferenteProgettoByIdProcedimentoOggetto(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void insertOrUpdateReferenteProgettoByIdProcedimentoOggetto(long idProcedimentoOggetto, String nome, String cognome, String codiceFiscale, String comune, String cap, String telefono, String cellulare, String email, LogOperationOggettoQuadroDTO logOperationOggQuadro) throws InternalUnexpectedException;

  public void insertOrUpdateCaratteristicheAziendali(CaratteristicheAziendali dati, boolean inserimento, long idProcedimentoOggetto, String nome, String cognome, String codiceFiscale, String comune, String cap, String telefono, String cellulare, String email, LogOperationOggettoQuadroDTO logOperationOggQuadro, String [] filiere, String [] metodiCol, String [] descmulti) throws InternalUnexpectedException;
  
  public void insertCensitoPrelievoOgur(Long idPianoDistretto, long idSpecie, int progressivo, int censito, int prelevato, BigDecimal percentuale, String esito, LogOperationOggettoQuadroDTO logOperationOggQuadro) throws InternalUnexpectedException;

  public void insertDateCensimento(Long idPianoDistretto, Date data, Long metodo, BigDecimal valore, long extIdUtenteAggiornamento, LogOperationOggettoQuadroDTO logOperationOggQuadro) throws InternalUnexpectedException;

  public void eliminaCensitoPrelievoOgur(long idPianoDistretto) throws InternalUnexpectedException;
  
  public void eliminaDateCensimento(long idPianoDistretto) throws InternalUnexpectedException;
  
  public void updatePianoDistrettoOgur(long piano, int totaleC, int indetC,  int totaleP, int indetP, BigDecimal max, String esito, long utente) throws InternalUnexpectedException;

  public void insertConsistenzaAziendaleVegetale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, long idConduzione, long idUtilizzo, long idUtilizzoDichiarato, String pubblica) throws InternalUnexpectedException;

  public void updateConsistenzaAziendaleVegetale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, String pubblica) throws InternalUnexpectedException;

  public void updateConsistenzaAziendaleAnimale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, String pubblica) throws InternalUnexpectedException;

  public void insertConsistenzaAziendaleAnimale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, long idAllevamento, long idSpecie, long idCategoria, String pubblica) throws InternalUnexpectedException;

  public void insertProdottiTrasformati(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, long idProdottoo, String desc) throws InternalUnexpectedException;
  
  public void insertProduzioniCertificate(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, Long idProduzioneCertificata, Long idProduzioneTradizionale, String flagBio) throws InternalUnexpectedException;
  
  public long insertCanaliVendita(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, 
      String altriCanali, String sitoWeb, String amazon, String orari, String indirizzo, String telefono, String email, String luogo, String info, String facebook, String instagram, String note, Long idImmagine) throws InternalUnexpectedException;

  public void insertCanaliContatti(long idDatiProcedimento, Long idImmagine) throws InternalUnexpectedException;
  
  public VersamentoLicenzaDTO cercaIuv(String iuv, String cf, String cittadinanza) throws InternalUnexpectedException;
  
  public String cercaAnagrafica(String cf) throws InternalUnexpectedException;
  
  public void getImmagineDaVisualizzare(Long idImmagine) throws InternalUnexpectedException;
  
  public boolean getGestisciUnitaMisuraByIdDanno(int idDanno) throws InternalUnexpectedException;

  public DannoDaFaunaDTO getDatiIdentificativiDanniDaFauna(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void updateDatiIdentificativiDanniDaFauna(DannoDaFaunaDTO danno, LogOperationOggettoQuadroDTO logOperation) throws InternalUnexpectedException;

  public Integer getCountDanniFauna(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<IstitutoDTO> getListaIstitutiDanniFauna(long idAmmCompetenza) throws InternalUnexpectedException;

  public List<IstitutoDTO> getListaNominativiDanniFauna(long idIstitutoDF) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getListaMotiviUrgenza(Date dataValidita) throws InternalUnexpectedException;

  public void eliminaDanniFauna(long idProcedimentoOggetto, long[] idsDannoFauna, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public List<DannoFaunaDTO> getListaDanniFauna(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DannoFaunaDTO> getListaDanniFauna(long idProcedimentoOggetto, long[] idsDannoFauna) throws InternalUnexpectedException;

  public List<DannoFaunaDTO> getListaDanniFaunaDettaglio(long idProcedimentoOggetto, long[] idsDannoFauna, boolean onlyLocalizzati) throws InternalUnexpectedException;

  public List<RiepilogoDannoFaunaDTO> getListaRiepilogoDanniFaunaDettaglio(long idProcedimentoOggetto, String[] idsRiepilogo) throws InternalUnexpectedException;

  public RiepilogoDannoFaunaDTO getTotaliRiepilogoDanniFauna(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getListaSpecieFauna() throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getListaTipoDannoFauna(long idSpecieFauna) throws InternalUnexpectedException;

  public void inserisciDannoFaunaParticelleFauna(long idProcedimentoOggetto, DannoFaunaDTO danno, List<ParticelleFaunaDTO> particelleFauna, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public void inserisciParticelleFauna(long idProcedimentoOggetto, long idDannoFauna, List<ParticelleFaunaDTO> particelleFauna, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public long memorizzaDannoFauna(long idProcedimentoOggetto, DannoFaunaDTO danno, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public AccertamentoDannoDTO getAccertamentoDanno(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getListaFunzionariIstruttoriByIdAmmComp(Long idProcedimento, Long idProcedimentoAgricolo) throws InternalUnexpectedException;

  public BigDecimal getImportoTotaleAccertato(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public long inserisciAccertamentoDanno(AccertamentoDannoDTO acdan, long idProcedimentoOggetto, long extIdUtenteAggiornamento) throws InternalUnexpectedException;

  public long modificaAccertamentoDanno(AccertamentoDannoDTO acdan, long idProcedimentoOggetto, long extIdUtenteAggiornamento) throws InternalUnexpectedException;

  public long eliminaAccertamentoDanno(long idAccertamentoDanno) throws InternalUnexpectedException;

  public void updateRiepilogoDannoFauna(long idProcedimentoOggetto, List<RiepilogoDannoFaunaDTO> riepilogoDanni) throws InternalUnexpectedException;

  public void deleteConcessioni(long idConcessione) throws InternalUnexpectedException;

  public List<ConcessioneDTO> getElencoConcessioni() throws InternalUnexpectedException;
  
  public ConcessioneDTO getConcessione(long idConcessione) throws InternalUnexpectedException;

  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(long idConcessione) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoAmmCompetenza(List<Long> idAmmComp, Long idPadre) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoBandi(int idProcedimentoAgricolo) throws InternalUnexpectedException;

  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(long idBando, long idAmministrazione) throws InternalUnexpectedException;

  public void inserisciConcessione(ConcessioneDTO concessione, long idUtenteLogin) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoTipiAtti() throws InternalUnexpectedException;

  public void approvaConcessione(long idConcessione, Long numeroProtocollo, Date dataProtocollo, Long idTipoAtto, long idUtenteLogin) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoBandiConcessioni() throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoStatiConcessioni() throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoTipiAttiConcessione() throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getAmmCompetenzaConcessioni() throws InternalUnexpectedException;

  public void deletePraticaConcessione(long idPraticaConcessione) throws InternalUnexpectedException;

  public void chiudiConcessione(long idConcessione, long idUtenteLogin) throws InternalUnexpectedException;

  public boolean canCambioStato(long idConcessione) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoTipoAttoConcessione() throws InternalUnexpectedException;

  public String getDecodificaMotivoUrgenza(long idMotivoUrgenza) throws InternalUnexpectedException;

  public void deleteOgur(long idOgur) throws InternalUnexpectedException;

  public List<OgurDTO> getElencoOgur(long idProcedimentoOggetto, Long idOgur) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoSpecieOgur(boolean forInsert, Long idProcedimentoOggetto) throws InternalUnexpectedException;
  
  public List<DecodificaDTO<Long>> getMetodiCensimento(Long idProcedimentoOggetto) throws InternalUnexpectedException;
  
  public List<RicercaDistretto> getIdDistrettoOgur(Long idProcedimentoOggetto) throws InternalUnexpectedException;
  
  public List<Distretto> getElencoDistrettiOgur(boolean regionale, String idDistretto, long specie, Long piano, long procedimento) throws InternalUnexpectedException;
  
  public List<DataCensimento> getDateCensimento(long idPiano, long specie) throws InternalUnexpectedException;
  
  public long getMetodoSpecieCensimento(long metodo, long specie) throws InternalUnexpectedException;

  public void inserisciModificaOgur(Long idProcedimentoOggetto, Long idOgur, Long idSpecieOgur, BigDecimal superficie, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public void deleteDistretto(long idOgur, long idDistretto) throws InternalUnexpectedException;

  public OgurDTO getOgur(long idOgur, boolean showDetails) throws InternalUnexpectedException;

  public void inserisciModificaDistrettoOgur(long idProcedimentoOggetto, Long idDistretto, long idOgur, DistrettoDTO distretto, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;

  public List<OgurDTO> getElencoOgur(long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void aggiornaVisuraConcessione(long idConcessione, Long visuraRitornataDalWs, long idUtenteLogin) throws InternalUnexpectedException;

  public void aggiornaFlagVisuraConcessione(long idConcessione, long idUtenteLogin) throws InternalUnexpectedException;

  public void aggiornaNotePraticaConcessione(long idPraticaConcessione, String note, long idUtenteLogin) throws InternalUnexpectedException;

  public String getNotePratica(long idPraticaConcessione) throws InternalUnexpectedException;

  public List<DatiAziendaDTO> getElencoAziendeConcessione(long idConcessione) throws InternalUnexpectedException;

  public int getNumeroAziendeInseriteCorrettamente(List<String> cuaaAziende, Long extIdVisMassivaRna) throws InternalUnexpectedException;

  public boolean esisteIdVisMassivaRnaSuVista(Long extIdVisMassivaRna) throws InternalUnexpectedException;

  public Long getIdRichiesta(long idConcessione) throws InternalUnexpectedException;

  public List<ProcedimentoOggettoVO> getAziendeDaVistaRegata(Long extIdVisMassivaRna) throws InternalUnexpectedException;

  public void updateVercorProcedimento(long idProcedimento, Date dataEsitoRna, String numeroVercor, long idUtenteLogin) throws InternalUnexpectedException;

  public void updatePraticaConcessioneEVercorProcedimento(long idProcedimento, long idPraticheConcessione, String numeroVercor, Date dataEsitoRna, long idUtenteLogin)throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoMetodiCensimento() throws InternalUnexpectedException;

  public UnitaMisuraDTO getUnitaMisuraByIdMetodoCensimento(long idMetodoCensimento)throws InternalUnexpectedException;

  public void deletePianoSelettivo(long idInfoCinghiali, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)throws InternalUnexpectedException;

  public void deleteDataCensimento(long idInfoCinghiali, long progressivoInfo, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)throws InternalUnexpectedException;

  public PianoSelettivoDTO getPianoSelettivo(long idProcedimentoOggetto)throws InternalUnexpectedException;

  public void inserisciInfoCinghiale(PianoSelettivoDTO piano, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)throws InternalUnexpectedException;

  public void updateInfoCinghiale(PianoSelettivoDTO piano, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)throws InternalUnexpectedException;
  
  Long callPckSmrgaaUtilityGraficheScriviParametri(Map<String, String> pArrayParametri)throws InternalUnexpectedException, ApplicationException;

  public long getIdDichConsistenza(long idProcedimentoOggetto) throws InternalUnexpectedException;
  
  public DatiBilancioDTO getDatiBilancio(long idProcedimentoOggetto) throws InternalUnexpectedException;
  
  public void insertDatiBilancio(DatiBilancioDTO dati, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException;
  
  public Long getIdUteMaxSuperficie(long idDichiarazioneConsistenza, boolean flagSauEqualsS) throws InternalUnexpectedException;
  
  public DecodificaDTO<Long> getZonAltimetricaByIdUte(long idUte) throws InternalUnexpectedException;
  
  public List<DecodificaDTO<Long>> getDecodificheUtilizzo() throws InternalUnexpectedException;
  
  public List<LogDTO> getElencoLog(Date istanzaDataDa, Date istanzaDataA, String rtesto) throws InternalUnexpectedException;

 
  public SoggettoDTO getSoggettoAnagrafePesca(String codiceFiscale)throws InternalUnexpectedException;

  public String getNextCodiceFiscaleEstero()throws InternalUnexpectedException;
  
  public List<ImportoLicenzaDTO> getImportiLicenzePesca()throws InternalUnexpectedException;
  
  public List<ImportoLicenzaDTO> getAllImportiLicenzePesca()throws InternalUnexpectedException;

  public Long aggiornaAnagraficaPescatore(SoggettoDTO soggettoDTO)throws InternalUnexpectedException;
  
  public void inserisciPagamento(String iuv, String idAnagraficaPesca, String esito, String tariffa, String tipo_pagamento)throws InternalUnexpectedException;
  
  public List<DecodificaDTO<Long>> getZoneAltimetricheByIdAzienda(long idAzienda)throws InternalUnexpectedException;

  public String getStatoConcessione(long idConcessione)throws InternalUnexpectedException;
}
