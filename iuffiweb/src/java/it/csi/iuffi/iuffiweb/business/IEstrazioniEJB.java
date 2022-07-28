package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.RigaFiltroDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.FlagEstrazioneDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.ImportiAttualiPrecedentiDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.ImportiTotaliDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.NumeroLottoDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IEstrazioniEJB extends IIuffiAbstractEJB
{
  public ImportiTotaliDTO getImportiTotali(long idNumeroLotto)
      throws InternalUnexpectedException;

  public ImportiAttualiPrecedentiDTO getImportiAttualiPrecedenti(
      long idNumeroLotto) throws InternalUnexpectedException;

  public List<RigaSimulazioneEstrazioneDTO> getElencoRisultati(
      long idNumeroLotto) throws InternalUnexpectedException;

  public MainControlloDTO callRegistraDP(long idNumeroLotto,
      long idTipoEstrazione) throws InternalUnexpectedException;

  public void eliminaEstrazioni(long idNumeroLotto)
      throws InternalUnexpectedException;

  public Boolean isEstrazioneAnnullabile(long idNumeroLotto,
      long idTipoEstrazione) throws InternalUnexpectedException;

  public void annullaEstrazioni(long idNumeroLotto, String motivo)
      throws InternalUnexpectedException;

  public List<RigaFiltroDTO> getElencoTipoEstrazioniCaricabili()
      throws InternalUnexpectedException;

  public long caricamentoEstrazioni(long idTipoEstrazione,
      String utenteDescrizione)
      throws InternalUnexpectedException, ApplicationException;

  public MainControlloDTO callEstraiDP(long idNumeroLotto,
      long idTipoEstrazione, String chkRegistra)
      throws InternalUnexpectedException;

  public NumeroLottoDTO getNumeroLottoDto(long idNumeroLotto)
      throws InternalUnexpectedException;

  public List<FlagEstrazioneDTO> getElencoFlagEstrazioni()
      throws InternalUnexpectedException;

  public List<FlagEstrazioneDTO> getElencoFlagEstrazioniExPost()
      throws InternalUnexpectedException;
}
