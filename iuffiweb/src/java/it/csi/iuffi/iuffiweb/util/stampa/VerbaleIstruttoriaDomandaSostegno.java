package it.csi.iuffi.iuffiweb.util.stampa;

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

public class VerbaleIstruttoriaDomandaSostegno extends Stampa
{
  public static final String ROOT_TAG = "Domanda";

  /* ATTENZIONE. ATTENZIONE. ATTENZIONE. ATTENZIONE */
  /*
   * NON USARE PROPRIETA' IN SCRITTURA PERCHE' QUESTA CLASSE VIENE ISTANZIATA
   * COME SINGLETON
   */
  private String             cuName;

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
  public String getDefaultFileName(long idProcedimentoOggetto)
      throws InternalUnexpectedException, NamingException
  {
    String cuaaBeneficiario = IuffiUtils.APPLICATION.getEjbQuadro()
        .getCuaaByIdProcedimentoOggetto(idProcedimentoOggetto);
    String identificativoProcedimento = IuffiUtils.APPLICATION.getEjbQuadro()
        .getIdentificativo(idProcedimentoOggetto);
    String ret = "";
    if (cuaaBeneficiario != null)
      ret += cuaaBeneficiario;

    if (identificativoProcedimento != null)
      ret += "_" + identificativoProcedimento;

    if (cuName
        .startsWith(IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE)
        || cuName.startsWith(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE))
      return ret + "_Verbale istruttoria domanda variante.pdf";
    else
      return ret + "_Verbale istruttoria domanda sostegno.pdf";
  }

  @Override
  public Stampa findStampaFinale(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    IQuadroEJB quadroEJB = IuffiUtils.APPLICATION.getEjbQuadro();
    this.cuName = cuName;
    ProcedimentoOggetto po = quadroEJB
        .getProcedimentoOggetto(idProcedimentoOggetto);
    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-V");
    EsitoFinaleDTO esitoFinaleDTO = quadroEJB.getEsitoFinale(
        po.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto());

    int progressivoIstanza = 0;
    int progressivoIstanzaISAMM = 0;
    int progressivoIstanzaISAMB = 0;
    int progressivoIstanzaISVAR = 0;

    boolean isIsamm = false;
    boolean isIsvar = false;

    ProcedimentoDTO procIter = quadroEJB
        .getIterProcedimento(po.getIdProcedimento());
    for (ProcedimentoOggettoDTO procOggDTO : procIter.getProcedimentoOggetto())
    {
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO
          .equals(procOggDTO.getCodOggetto())
          || IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO_GAL
              .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISAMM++;
      }
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA
          .equals(procOggDTO.getCodOggetto())
          || IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA_FINANZIAMENTO_GAL
              .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISAMB++;
      }
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_VARIANTE
          .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISVAR++;
      }
      if (procOggDTO.getIdProcedimentoOggetto() == idProcedimentoOggetto)
      {
        if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO
            .equals(procOggDTO.getCodOggetto())
            || IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO_GAL
                .equals(procOggDTO.getCodOggetto()))
        {
          isIsamm = true;
        }
        else
          if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_VARIANTE
              .equals(procOggDTO.getCodOggetto()))
          {
            isIsvar = true;
          }
        break;
      }
    }

    if (isIsamm)
    {
      progressivoIstanza = progressivoIstanzaISAMM;
    }
    else
      if (isIsvar)
      {
        progressivoIstanza = progressivoIstanzaISVAR;
      }
      else
      {
        progressivoIstanza = progressivoIstanzaISAMB;
      }

    if (progressivoIstanza == 0)
    {
      progressivoIstanza = 1;
    }

    if (progressivoIstanza > 2)
      progressivoIstanza = 2;

    boolean isVariante = false;
    if (cuName
        .startsWith(IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE)
        || cuName.startsWith(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE))
      isVariante = true;

    boolean esitoPositivo = esitoFinaleDTO == null
        || IuffiConstants.ESITO.TIPO.POSITIVO
            .equals(esitoFinaleDTO.getCodiceEsito());
    if (esitoPositivo)
    {
      if (progressivoIstanza == 1)
      {
        if (isVariante)
          return IuffiUtils.STAMPA.getStampaFromCdU(
              IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_1);
        else
          return IuffiUtils.STAMPA.getStampaFromCdU(
              IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_1);
      }
      else
        if (progressivoIstanza == 2)
        {
          if (isVariante)
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_2);
          else
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_2);
        }
    }
    else
      if (IuffiConstants.ESITO.TIPO.PARZIALMENTE_POSITIVO
          .equals(esitoFinaleDTO.getCodiceEsito()))
      {
        if (progressivoIstanza == 1)
        {
          if (isVariante)
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_1);
          else
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_1);
        }
        else
          if (progressivoIstanza == 2)
          {
            if (isVariante)
              return IuffiUtils.STAMPA.getStampaFromCdU(
                  IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_2);
            else
              return IuffiUtils.STAMPA.getStampaFromCdU(
                  IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_2);
          }
      }
      else
      {
        if (progressivoIstanza == 1)
        {
          if (isVariante)
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_1);
          else
            return IuffiUtils.STAMPA.getStampaFromCdU(
                IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_1);
        }
        else
          if (progressivoIstanza == 2)
          {
            if (isVariante)
              return IuffiUtils.STAMPA.getStampaFromCdU(
                  IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_2);
            else
              return IuffiUtils.STAMPA.getStampaFromCdU(
                  IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_2);
          }
      }

    return null;
  }
}