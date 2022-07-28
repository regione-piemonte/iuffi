package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.solmr.etc.profile.AgriConstants;

public class ElementoQuadroDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long       serialVersionUID      = 5444290540064814888L;
  private static final int        MAX_LUNGHEZZA_AMMESSA = 4000;
  protected String                nomeLabel;
  protected String                flagObbligatorio;
  protected String                istruzioneSqlControlli;
  protected long                  idTipoDato;
  protected String                codice;
  protected String                istruzioneSqlElenco;
  protected String                flagPresenzaInElenco;
  protected int                   ordineVisualizzazione;
  protected Integer               precisione;
  protected Integer               lunghezza;
  protected String                note;
  protected long                  idElementoQuadro;
  protected Integer               lunghezzaMin;
  protected String                flagProtetto;
  protected String                flagStampa;
  protected String                valoreDefault;
  protected String                codiceTipoDato;
  // protected List<DatoElementoQuadroDTO> dati;
  protected List<VoceElementoDTO> vociElemento;

  public String getNomeLabel()
  {
    return this.nomeLabel;
  }

  public void setNomeLabel(String nomeLabel)
  {
    this.nomeLabel = nomeLabel;
  }

  public String getFlagObbligatorio()
  {
    return this.flagObbligatorio;
  }

  // Metodo di utilità
  public boolean isObbligatorio()
  {
    return IuffiConstants.FLAGS.SI.equals(flagObbligatorio);
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
  }

  public String getIstruzioneSqlControlli()
  {
    return this.istruzioneSqlControlli;
  }

  public void setIstruzioneSqlControlli(String istruzioneSqlControlli)
  {
    this.istruzioneSqlControlli = istruzioneSqlControlli;
  }

  public long getIdTipoDato()
  {
    return this.idTipoDato;
  }

  public void setIdTipoDato(long idTipoDato)
  {
    this.idTipoDato = idTipoDato;
  }

  public String getCodice()
  {
    return this.codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getIstruzioneSqlElenco()
  {
    return this.istruzioneSqlElenco;
  }

  public void setIstruzioneSqlElenco(String istruzioneSqlElenco)
  {
    this.istruzioneSqlElenco = istruzioneSqlElenco;
  }

  public String getFlagPresenzaInElenco()
  {
    return this.flagPresenzaInElenco;
  }

  public void setFlagPresenzaInElenco(String flagPresenzaInElenco)
  {
    this.flagPresenzaInElenco = flagPresenzaInElenco;
  }

  public int getOrdineVisualizzazione()
  {
    return this.ordineVisualizzazione;
  }

  public void setOrdineVisualizzazione(int ordineVisualizzazione)
  {
    this.ordineVisualizzazione = ordineVisualizzazione;
  }

  public Integer getPrecisione()
  {
    return this.precisione;
  }

  public void setPrecisione(Integer precisione)
  {
    this.precisione = precisione;
  }

  public Integer getLunghezza()
  {
    return this.lunghezza;
  }

  public void setLunghezza(Integer lunghezza)
  {
    this.lunghezza = lunghezza;
  }

  public String getNote()
  {
    return this.note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Integer getLunghezzaMin()
  {
    return this.lunghezzaMin;
  }

  public void setLunghezzaMin(Integer lunghezzaMin)
  {
    this.lunghezzaMin = lunghezzaMin;
  }

  public String getFlagProtetto()
  {
    return this.flagProtetto;
  }

  public void setFlagProtetto(String flagProtetto)
  {
    this.flagProtetto = flagProtetto;
  }

  public String getFlagStampa()
  {
    return this.flagStampa;
  }

  public void setFlagStampa(String flagStampa)
  {
    this.flagStampa = flagStampa;
  }

  public String getValoreDefault()
  {
    return this.valoreDefault;
  }

  public void setValoreDefault(String valoreDefault)
  {
    this.valoreDefault = valoreDefault;
  }

  public String getCodiceTipoDato()
  {
    return codiceTipoDato;
  }

  public void setCodiceTipoDato(String codiceTipoDato)
  {
    this.codiceTipoDato = codiceTipoDato;
  }

  public boolean isTipoSTR()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.STR
        .equals(codiceTipoDato);
  }

  public boolean isTipoMST()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.MST
        .equals(codiceTipoDato);
  }

  public boolean isTipoDTA()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.DTA
        .equals(codiceTipoDato);
  }

  public boolean isTipoNUM()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NUM
        .equals(codiceTipoDato);
  }

  public boolean isTipoEUR()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.EUR
        .equals(codiceTipoDato);
  }

  public boolean isTipoPCT()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.PCT
        .equals(codiceTipoDato);
  }

  public boolean isTipoCMB()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.CMB
        .equals(codiceTipoDato);
  }

  public boolean isTipoRBT()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.RBT
        .equals(codiceTipoDato);
  }

  public boolean isTipoCBT()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.CBT
        .equals(codiceTipoDato);
  }

  public boolean isTipoLST()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.LST
        .equals(codiceTipoDato);
  }

  public boolean isTipoTXT()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.TXT
        .equals(codiceTipoDato);
  }

  public boolean isTipoDTQ()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.DTQ
        .equals(codiceTipoDato);
  }

  public boolean isTipoDTF()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.DTF
        .equals(codiceTipoDato);
  }

  public boolean isTipoNU0()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NU0
        .equals(codiceTipoDato);
  }

  public boolean isTipoANN()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.ANN
        .equals(codiceTipoDato);
  }

  public boolean isTipoNUQ()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NUQ
        .equals(codiceTipoDato);
  }

  public boolean isTipoTIT()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.TIT
        .equals(codiceTipoDato);
  }

  public boolean isTipoSTM()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.STM
        .equals(codiceTipoDato);
  }

  public boolean isTipoHTM()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.HTM
        .equals(codiceTipoDato);
  }

  public boolean isTipoNumerico()
  {
    return IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NU0
        .equals(codiceTipoDato)
        || IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NUM
            .equals(codiceTipoDato)
        || IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NUQ
            .equals(codiceTipoDato)
        || IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.PCT
            .equals(codiceTipoDato)
        || IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.EUR
            .equals(codiceTipoDato)
        || IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.ANN
            .equals(codiceTipoDato);
  }

  public long getIdElementoQuadro()
  {
    return idElementoQuadro;
  }

  public void setIdElementoQuadro(long idElementoQuadro)
  {
    this.idElementoQuadro = idElementoQuadro;
  }

  public List<VoceElementoDTO> getVociElemento()
  {
    return vociElemento;
  }

  public void setVociElemento(List<VoceElementoDTO> vociElemento)
  {
    this.vociElemento = vociElemento;
  }

  public int getMaxLength()
  {
    int _lunghezza;
    if (lunghezza != null)
    {
      _lunghezza = lunghezza;
      if (precisione != null && precisione > 0)
      {
        ++_lunghezza; // conteggio anche la virgola
      }
    }
    else
    {
      _lunghezza = MAX_LUNGHEZZA_AMMESSA;
    }
    return _lunghezza;
  }

  public boolean isPresenteInElenco()
  {
    return AgriConstants.FLAG_SI.equals(flagPresenzaInElenco);
  }

  public boolean isProtetto()
  {
    return IuffiConstants.FLAGS.SI.equals(flagProtetto);
  }
}
