package it.csi.iuffi.iuffiweb.business;

import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface ITrasmissioneEJB
{
  public String trasmettiIstanzaElaborazioneMassiva(long idProcedimentoOggetto,
      Date dataTrasmissione, long idUtenteLogin,String parametro) throws Exception;

  public String firmaGrafometrica(long oldIdDocumentoIndex,
      long newIdDocumentoIndex, long idUtenteAggiornamento,
      Date timestamp) throws Exception;
}
