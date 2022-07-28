package it.csi.iuffi.iuffiweb.util.stampa.anticipo;

import javax.naming.NamingException;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;

public class VerbaleIstruttoriaAnticipo extends Stampa
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
     * Questa classe è solo di utilità per smistare su quella giusta. Comunque,
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

    return ret + "_Verbale istruttoria anticipo.pdf";
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

    int progressivoIstanzaISACC = 0;
    ProcedimentoDTO procIter = quadroEJB.getIterProcedimento(
        po.getIdProcedimento(), po.getCodiceRaggruppamento());
    for (ProcedimentoOggettoDTO procOggDTO : procIter.getProcedimentoOggetto())
    {
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_ANTICIPO
          .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISACC++;
      }
    }

    if (progressivoIstanzaISACC > 2)
      progressivoIstanzaISACC = 2;

    boolean esitoPositivo = esitoFinaleDTO == null
        || IuffiConstants.ESITO.TIPO.POSITIVO
            .equals(esitoFinaleDTO.getCodiceEsito());
    if (esitoPositivo)
    {
      if (progressivoIstanzaISACC == 1)
        return IuffiUtils.STAMPA.getStampaFromCdU(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_1);
      else
        if (progressivoIstanzaISACC == 2)
          return IuffiUtils.STAMPA.getStampaFromCdU(
              IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_2);
    }
    else
      if (IuffiConstants.ESITO.TIPO.PARZIALMENTE_POSITIVO
          .equals(esitoFinaleDTO.getCodiceEsito()))
      {
        return IuffiUtils.STAMPA.getStampaFromCdU(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_PARZIALE_1);
      }
      else
      {
        if (progressivoIstanzaISACC == 1)
          return IuffiUtils.STAMPA.getStampaFromCdU(
              IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_1);
        else
          if (progressivoIstanzaISACC == 2)
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_2);
      }

    return null;
  }
}