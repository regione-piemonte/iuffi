package it.csi.iuffi.iuffiweb.util.stampa.annullamento;

import javax.naming.NamingException;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;

public class LetteraAnnullamento extends Stampa
{
  public static final String ROOT_TAG = "Domanda";

  /* ATTENZIONE. ATTENZIONE. ATTENZIONE. ATTENZIONE */   
  /* NON USARE PROPRIETA' IN SCRITTURA PERCHE' QUESTA CLASSE VIENE ISTANZIATA COME SINGLETON */

  @Override
  public byte[] genera(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    /*
     * WARN: Questo metodo non dovrebbe mai essere richiamato direttamente in
     * quanto bisognerebbe passare dal findStampaFinale() che mi dovrebbe
     * restituire una delle 2 stampe (ammissione positiva e negativa) possibili.
     * Questa classe è solo di utilità per smistare su quella giusta Comunque,
     * l'implementazione sottostante dovrebbe non generare problemi
     */
    return findStampaFinale(idProcedimentoOggetto, cuName)
        .genera(idProcedimentoOggetto, cuName);
  }

  protected String getCodiceModulo()
  {
    return "";
  }

  protected String getCodiceModello()
  {
    return "";
  }

  protected String getRifAdobe()
  {
    return "";
  }

  @Override
  public String getDefaultFileName(long idProcedimentoOggetto) throws InternalUnexpectedException, NamingException
  {
    String cuaaBeneficiario = IuffiUtils.APPLICATION.getEjbQuadro().getCuaaByIdProcedimentoOggetto(idProcedimentoOggetto);
    String identificativoProcedimento = IuffiUtils.APPLICATION.getEjbQuadro().getIdentificativo(idProcedimentoOggetto);
    String ret = "";
    if (cuaaBeneficiario != null)
      ret += cuaaBeneficiario;

    if (identificativoProcedimento != null)
      ret += "_" + identificativoProcedimento;

    return ret + "_Lettera di annullamento.pdf";
  }

  @Override
  public Stampa findStampaFinale(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    IQuadroEJB quadroEJB = IuffiUtils.APPLICATION.getEjbQuadro();
    ProcedimentoOggetto po = quadroEJB
        .getProcedimentoOggetto(idProcedimentoOggetto);
    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-V");

    EsitoFinaleDTO esitoFinaleDTO = quadroEJB.getEsitoFinale(
        po.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto());

    boolean esitoPositivo = esitoFinaleDTO == null
        || IuffiConstants.ESITO.TIPO.POSITIVO
            .equals(esitoFinaleDTO.getCodiceEsito());
    if (esitoPositivo)
    {
      return IuffiUtils.STAMPA.getStampaFromCdU(
          IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO_POSITIVO);
    }
    else
    {
      return IuffiUtils.STAMPA.getStampaFromCdU(
          IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO_NEGATIVO);

    }
  }
}
