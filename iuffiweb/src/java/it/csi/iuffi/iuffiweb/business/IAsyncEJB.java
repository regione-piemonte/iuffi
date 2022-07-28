package it.csi.iuffi.iuffiweb.business;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IAsyncEJB extends IIuffiAbstractEJB
{
  public void generaStampa(long idProcedimentoOggetto, long idProcedimento,
      long idOggettoIcona) throws InternalUnexpectedException;

  public void generaStampePerProcedimento(long idProcedimentoOggetto,
      long idProcedimento) throws InternalUnexpectedException;

  public void generaStampaListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException;
}
