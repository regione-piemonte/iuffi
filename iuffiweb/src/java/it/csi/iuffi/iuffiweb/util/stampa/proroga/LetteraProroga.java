package it.csi.iuffi.iuffiweb.util.stampa.proroga;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;

public class LetteraProroga extends Stampa
{
  public static final String ROOT_TAG                   = "Domanda";

  private String             cuaaBeneficiario           = "";
  private String             identificativoProcedimento = "";

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
    public String getDefaultFileName(long idProcedimentoOggetto)
  {
    return cuaaBeneficiario + "_" + identificativoProcedimento
        + "_comunicazioneEsitoProroga.pdf";
  }

  @Override
  public Stampa findStampaFinale(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    IQuadroEJB quadroEJB = IuffiUtils.APPLICATION.getEjbQuadro();
    cuaaBeneficiario = quadroEJB
        .getCuaaByIdProcedimentoOggetto(idProcedimentoOggetto);
    identificativoProcedimento = quadroEJB
        .getIdentificativo(idProcedimentoOggetto);
    ProcedimentoOggetto po = quadroEJB
        .getProcedimentoOggetto(idProcedimentoOggetto);
    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-V");
    EsitoFinaleDTO esitoFinaleDTO = quadroEJB.getEsitoFinale(
        po.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto());

    boolean esitoPositivo = esitoFinaleDTO == null
        || esitoFinaleDTO.getCodiceEsito() == null
        || IuffiConstants.ESITO.TIPO.POSITIVO
            .equals(esitoFinaleDTO.getCodiceEsito())
        || IuffiConstants.ESITO.TIPO.PARZIALMENTE_POSITIVO
            .equals(esitoFinaleDTO.getCodiceEsito());

    if (esitoPositivo)
    {
      return IuffiUtils.STAMPA.getStampaFromCdU(
          IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA_POSITIVO);
    }
    else
    {
      return IuffiUtils.STAMPA.getStampaFromCdU(
          IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA_NEGATIVO);
    }
  }
}
