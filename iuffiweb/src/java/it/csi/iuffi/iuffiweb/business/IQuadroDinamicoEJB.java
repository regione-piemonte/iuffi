package it.csi.iuffi.iuffiweb.business;

import java.util.Map;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.QuadroDinamicoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IQuadroDinamicoEJB extends IIuffiAbstractEJB
{
  public QuadroDinamicoDTO getQuadroDinamico(String codiceQuadro,
      long idProcedimentoOggetto, Long numProgressivoRecord)
      throws InternalUnexpectedException;

  public QuadroDinamicoDTO getStrutturaQuadroDinamico(String codiceQuadro,
      long idProcedimentoOggettoPerCaricamentoVociElemento)
      throws InternalUnexpectedException;

  public String aggiornaInserisciRecordQuadroDinamico(long idQuadro,
      Map<Long, String[]> mapValues,
      LogOperationOggettoQuadroDTO logOperationDTO, Long numProgressivoRecord)
      throws InternalUnexpectedException;

  public String executeIstruzioneControlli(String istruzioneSqlControlli,
      String valoreElemento) throws InternalUnexpectedException;

  public String eliminaRecordQuadroDinamico(long idQuadro,
      int numProgressivoRecord, LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;
}