package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;

public class RicercaProcedimentiVO implements ILoggable {
    /** serialVersionUID */
    private static final long serialVersionUID = 4601339650061084885L;

    // ricerca per procedimento
    private String identificativo;
    private String cuaa;

    // ricerca per filtri
    private Vector<String> vctIdEventi;
    private Vector<String> vctIdLivelli;
    private HashMap<Long, List<Long>> idAziedaIdBandiProfessionsita;

    private Vector<String> vctIdBando;
    private Vector<String> vctIdAmministrazione;
    private Vector<String> vctIdStatoProcedimento;
    private Vector<String> vctIdStatoAmmProcedimento;
    private Vector<String> vctFlagEstrazione;
    private Vector<String> vctFlagEstrazioneExPost;
    private Vector<String> vctNotifiche;

    private List<EventiDTO> eventi;
    private List<BandoDTO> bandi;
    private List<LivelloDTO> livelli;
    private List<AmmCompetenzaDTO> amministrazioni;
    private List<ProcedimentoDTO> statiProcedimento;
    private List<ProcedimentoDTO> statiAmmProcedimento;
    private boolean flagShowAllAmministrazioni = false;

    // ricerca per dati anagrafici
    private String cuaaProcedimenti;
    private String piva;
    private String denominazione;
    private String provSedeLegale;
    private String comuneSedeLegale;

    private Date istanzaDataDa;
    private Date istanzaDataA;

    // ricerca per dati anagrafici
    private String tipoFiltroOggetto;

    private String tipoFiltroGruppo;
    // mappa del tipo <idOggetto, Vector<idEsito>>
    private HashMap<Long, Vector<Long>> mapOggetti;
    private HashMap<Long, Vector<Long>> mapGruppi;

    // mappa del tipo <idGruppo, Vector<idStati>>
    private HashMap<Long, Vector<Long>> mapGruppiStati;

    private Long idTipoDocumento;
    private Long idModalitaPagamento;
    private Long idFornitore;
    private Date dataDocumentoDa;
    private Date dataDocumentoA;

    public Long getIdTipoDocumento() {
	return idTipoDocumento;
    }

    public void setIdTipoDocumento(Long idTipoDocumento) {
	this.idTipoDocumento = idTipoDocumento;
    }

    public Long getIdModalitaPagamento() {
	return idModalitaPagamento;
    }

    public void setIdModalitaPagamento(Long idModalitaPagamento) {
	this.idModalitaPagamento = idModalitaPagamento;
    }

    public Long getIdFornitore() {
	return idFornitore;
    }

    public void setIdFornitore(Long idFornitore) {
	this.idFornitore = idFornitore;
    }

    public Date getDataDocumentoDa() {
	return dataDocumentoDa;
    }

    public void setDataDocumentoDa(Date dataDocumentoDa) {
	this.dataDocumentoDa = dataDocumentoDa;
    }

    public Date getDataDocumentoA() {
	return dataDocumentoA;
    }

    public void setDataDocumentoA(Date dataDocumentoA) {
	this.dataDocumentoA = dataDocumentoA;
    }

    public String getIdentificativo() {
	return identificativo;
    }

    public void setIdentificativo(String identificativo) {
	this.identificativo = identificativo;
    }

    public String getCuaa() {
	return cuaa;
    }

    public void setCuaa(String cuaa) {
	this.cuaa = cuaa;
    }

    public Vector<String> getVctIdEventi() {
	return vctIdEventi;
    }

    public void setVctIdEventi(Vector<String> vctIdEventi) {
	this.vctIdEventi = vctIdEventi;
    }

    public Vector<String> getVctIdLivelli() {
	return vctIdLivelli;
    }

    public void setVctIdLivelli(Vector<String> vctIdLivelli) {
	this.vctIdLivelli = vctIdLivelli;
    }

    public Vector<String> getVctIdBando() {
	return vctIdBando;
    }

    public void setVctIdBando(Vector<String> vctIdBando) {
	this.vctIdBando = vctIdBando;
    }

    public Vector<String> getVctIdAmministrazione() {
	return vctIdAmministrazione;
    }

    public void setVctIdAmministrazione(Vector<String> vctIdAmministrazione) {
	this.vctIdAmministrazione = vctIdAmministrazione;
    }

    public Vector<String> getVctIdStatoProcedimento() {
	return vctIdStatoProcedimento;
    }

    public void setVctIdStatoProcedimento(Vector<String> vctIdStatoProcedimento) {
	this.vctIdStatoProcedimento = vctIdStatoProcedimento;
    }

    public List<ProcedimentoDTO> getStatiProcedimento() {
	return statiProcedimento;
    }

    public List<ProcedimentoDTO> getStatiAmmProcedimento() {
	return statiAmmProcedimento;
    }

    public String getPiva() {
	return piva;
    }

    public void setPiva(String piva) {
	this.piva = piva;
    }

    public String getDenominazione() {
	return denominazione;
    }

    public void setDenominazione(String denominazione) {
	this.denominazione = denominazione;
    }

    public String getProvSedeLegale() {
	return provSedeLegale;
    }

    public void setProvSedeLegale(String provSedeLegale) {
	this.provSedeLegale = provSedeLegale;
    }

    public String getComuneSedeLegale() {
	return comuneSedeLegale;
    }

    public void setComuneSedeLegale(String comuneSedeLegale) {
	this.comuneSedeLegale = comuneSedeLegale;
    }

    public List<EventiDTO> getEventi() {
	return eventi;
    }

    public void setEventi(List<EventiDTO> eventi) {
	this.eventi = eventi;
    }

    public List<BandoDTO> getBandi() {
	return bandi;
    }

    public void setBandi(List<BandoDTO> bandi) {
	this.bandi = bandi;
    }

    public List<LivelloDTO> getLivelli() {
	return livelli;
    }

    public void setLivelli(List<LivelloDTO> livelli) {
	this.livelli = livelli;
    }

    public List<AmmCompetenzaDTO> getAmministrazioni() {
	return amministrazioni;
    }

    public void setAmministrazioni(List<AmmCompetenzaDTO> amministrazioni) {
	this.amministrazioni = amministrazioni;
    }

    public List<ProcedimentoDTO> getStatiProcediemnto() {
	return statiProcedimento;
    }

    public void setStatiProcedimento(List<ProcedimentoDTO> statiProcedimento) {
	this.statiProcedimento = statiProcedimento;
    }

    public String getCuaaProcedimenti() {
	return cuaaProcedimenti;
    }

    public void setCuaaProcedimenti(String cuaaProcedimenti) {
	this.cuaaProcedimenti = cuaaProcedimenti;
    }

    public HashMap<Long, Vector<Long>> getMapOggetti() {
	return mapOggetti;
    }

    public void setMapOggetti(HashMap<Long, Vector<Long>> mapOggetti) {
	this.mapOggetti = mapOggetti;
    }

    public HashMap<Long, Vector<Long>> getMapGruppi() {
	return mapGruppi;
    }

    public void setMapGruppi(HashMap<Long, Vector<Long>> mapGruppi) {
	this.mapGruppi = mapGruppi;
    }

    public String getTipoFiltroOggetto() {
	return tipoFiltroOggetto;
    }

    public void setTipoFiltroOggetto(String tipoFiltroOggetto) {
	this.tipoFiltroOggetto = tipoFiltroOggetto;
    }

    public boolean isFlagShowAllAmministrazioni() {
	return flagShowAllAmministrazioni;
    }

    public void setFlagShowAllAmministrazioni(boolean flagShowAllAmministrazioni) {
	this.flagShowAllAmministrazioni = flagShowAllAmministrazioni;
    }

    public Vector<String> getVctFlagEstrazione() {
	return vctFlagEstrazione;
    }

    public void setVctFlagEstrazione(Vector<String> vctFlagEstrazione) {
	this.vctFlagEstrazione = vctFlagEstrazione;
    }

    public Vector<String> getVctFlagEstrazioneExPost() {
	return vctFlagEstrazioneExPost;
    }

    public void setVctFlagEstrazioneExPost(Vector<String> vctFlagEstrazioneExPost) {
	this.vctFlagEstrazioneExPost = vctFlagEstrazioneExPost;
    }

    public Date getIstanzaDataDa() {
	return istanzaDataDa;
    }

    public void setIstanzaDataDa(Date istanzaDataDa) {
	this.istanzaDataDa = istanzaDataDa;
    }

    public Date getIstanzaDataA() {
	return istanzaDataA;
    }

    public void setIstanzaDataA(Date istanzaDataA) {
	this.istanzaDataA = istanzaDataA;
    }

    public Vector<String> getVctNotifiche() {
	return vctNotifiche;
    }

    public void setVctNotifiche(Vector<String> vctNotifiche) {
	this.vctNotifiche = vctNotifiche;
    }

    public Vector<String> getVctIdStatoAmmProcedimento() {
	return vctIdStatoAmmProcedimento;
    }

    public void setVctIdStatoAmmProcedimento(Vector<String> vctIdStatoAmmProcedimento) {
	this.vctIdStatoAmmProcedimento = vctIdStatoAmmProcedimento;
    }

    public List<ProcedimentoDTO> getStatiAmmProcediemnto() {
	return statiAmmProcedimento;
    }

    public void setStatiAmmProcedimento(List<ProcedimentoDTO> statiAmmProcedimento) {
	this.statiAmmProcedimento = statiAmmProcedimento;
    }

    public HashMap<Long, Vector<Long>> getMapGruppiStati() {
	return mapGruppiStati;
    }

    public void setMapGruppiStati(HashMap<Long, Vector<Long>> mapGruppiStati) {
	this.mapGruppiStati = mapGruppiStati;
    }

    public String getTipoFiltroGruppo() {
	return tipoFiltroGruppo;
    }

    public void setTipoFiltroGruppo(String tipoFiltroGruppo) {
	this.tipoFiltroGruppo = tipoFiltroGruppo;
    }

    public HashMap<Long, List<Long>> getIdAziedaIdBandiProfessionsita() {
	return idAziedaIdBandiProfessionsita;
    }

    public void setIdAziedaIdBandiProfessionsita(HashMap<Long, List<Long>> idAziedaIdBandiProfessionsita) {
        this.idAziedaIdBandiProfessionsita = idAziedaIdBandiProfessionsita;
    }

}
