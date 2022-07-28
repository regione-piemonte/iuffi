package it.csi.iuffi.iuffiweb.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.iuffi.iuffiweb.util.stampa.AmmissioneFinanziamento;
import it.csi.iuffi.iuffiweb.util.stampa.AmmissioneFinanziamentoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.AmmissioneFinanziamentoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.DomandaPSL;
import it.csi.iuffi.iuffiweb.util.stampa.LetteraIntegPagamento;
import it.csi.iuffi.iuffiweb.util.stampa.Pratica;
import it.csi.iuffi.iuffiweb.util.stampa.PreavvisoDiRigettoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.PreavvisoDiRigettoTotale;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;
import it.csi.iuffi.iuffiweb.util.stampa.StampaListaLiquidazione;
import it.csi.iuffi.iuffiweb.util.stampa.VerbaleIntegPagamento;
import it.csi.iuffi.iuffiweb.util.stampa.VerbaleIstruttoriaDomandaSostegno;
import it.csi.iuffi.iuffiweb.util.stampa.VerbaleIstruttoriaDomandaSostegnoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.VerbaleIstruttoriaDomandaSostegnoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.LetteraIstruttoriaAcconto;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.LetteraIstruttoriaAccontoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.LetteraIstruttoriaAccontoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.LetteraIstruttoriaAccontoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.LetteraIstruttoriaAccontoPreavvisoRigetto;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.VerbaleIstruttoriaAcconto;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.VerbaleIstruttoriaAccontoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.VerbaleIstruttoriaAccontoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.acconto.VerbaleIstruttoriaAccontoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.annullamento.LetteraAnnullamento;
import it.csi.iuffi.iuffiweb.util.stampa.annullamento.LetteraAnnullamentoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.annullamento.LetteraAnnullamentoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.LetteraIstruttoriaAnticipo;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.LetteraIstruttoriaAnticipoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.LetteraIstruttoriaAnticipoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.LetteraIstruttoriaAnticipoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.LetteraIstruttoriaAnticipoPreavvisoRigetto;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.VerbaleIstruttoriaAnticipo;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.VerbaleIstruttoriaAnticipoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.VerbaleIstruttoriaAnticipoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.anticipo.VerbaleIstruttoriaAnticipoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.proroga.LetteraProroga;
import it.csi.iuffi.iuffiweb.util.stampa.proroga.LetteraProrogaNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.proroga.LetteraProrogaPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.proroga.VerbaleProroga;
import it.csi.iuffi.iuffiweb.util.stampa.proroga.VerbaleProrogaNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.proroga.VerbaleProrogaPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.LetteraRevoca;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.LetteraRevocaNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.LetteraRevocaParziale;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.LetteraRevocaPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.VerbalePreavvisoRevocaPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.VerbaleRevoca;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.VerbaleRevocaNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.revoca.VerbaleRevocaPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.LetteraIstruttoriaSaldo;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.LetteraIstruttoriaSaldoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.LetteraIstruttoriaSaldoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.LetteraIstruttoriaSaldoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.LetteraIstruttoriaSaldoPreavvisoRigetto;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.VerbaleIstruttoriaSaldo;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.VerbaleIstruttoriaSaldoNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.VerbaleIstruttoriaSaldoParziale;
import it.csi.iuffi.iuffiweb.util.stampa.saldo.VerbaleIstruttoriaSaldoPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.LetteraVoltura;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.LetteraVolturaNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.LetteraVolturaParziale;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.LetteraVolturaPositivo;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.VerbaleVoltura;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.VerbaleVolturaNegativo;
import it.csi.iuffi.iuffiweb.util.stampa.voltura.VerbaleVolturaPositivo;

/**
 * Classe astratta per le funzioni di utilità sulle stringhe. La classe è
 * abstract perchè non deve essere usata direttamente ma solo dalla sua
 * implementazione nella costante Utils.STRING
 * 
 * @author Stefano Einaudi (Matr. 70399)
 * 
 */
public abstract class StampaUtils
{
  public static Map<String, Stampa> MAP_STAMPE = new HashMap<String, Stampa>();
  static
  {
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_STAMPA_PRATICA,
        new Pratica());
    MAP_STAMPE.put(IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_STAMPA_PSL,
        new DomandaPSL());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO,
        new AmmissioneFinanziamento());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_POSITIVO_1,
        new AmmissioneFinanziamentoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_POSITIVO_2,
        new AmmissioneFinanziamentoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_NEGATIVO_1,
        new PreavvisoDiRigettoTotale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_NEGATIVO_2,
        new AmmissioneFinanziamentoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_PARZIALE_1,
        new PreavvisoDiRigettoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_PARZIALE_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_PARZIALE_2,
        new PreavvisoDiRigettoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO_PARZIALE_2));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO,
        new VerbaleIstruttoriaDomandaSostegno());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_1,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_2,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_1,
        new VerbaleIstruttoriaDomandaSostegnoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_2,
        new VerbaleIstruttoriaDomandaSostegnoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_1,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_2,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_2));
    MAP_STAMPE.put(IuffiConstants.USECASE.LISTE_LIQUIDAZIONE.STAMPA,
        new StampaListaLiquidazione());

    // Gestione bandi misure a premio

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO,
        new VerbaleIstruttoriaAcconto());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_1,
        new VerbaleIstruttoriaAccontoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_1,
        new VerbaleIstruttoriaAccontoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_2,
        new VerbaleIstruttoriaAccontoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_2,
        new VerbaleIstruttoriaAccontoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_PARZIALE_1,
        new VerbaleIstruttoriaAccontoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_PARZIALE_1));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO,
        new VerbaleIstruttoriaAnticipo());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_1,
        new VerbaleIstruttoriaAnticipoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_1,
        new VerbaleIstruttoriaAnticipoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_2,
        new VerbaleIstruttoriaAnticipoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_2,
        new VerbaleIstruttoriaAnticipoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_PARZIALE_1,
        new VerbaleIstruttoriaAnticipoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_PARZIALE_1));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO,
        new VerbaleIstruttoriaSaldo());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_1,
        new VerbaleIstruttoriaSaldoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_1,
        new VerbaleIstruttoriaSaldoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_2,
        new VerbaleIstruttoriaSaldoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_2,
        new VerbaleIstruttoriaSaldoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_PARZIALE_1,
        new VerbaleIstruttoriaSaldoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_SALDO_PARZIALE_1));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO,
        new LetteraIstruttoriaAcconto());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_1,
        new LetteraIstruttoriaAccontoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_1,
        new LetteraIstruttoriaAccontoPreavvisoRigetto(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_2,
        new LetteraIstruttoriaAccontoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_2,
        new LetteraIstruttoriaAccontoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_PARZIALE_1,
        new LetteraIstruttoriaAccontoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_PARZIALE_1));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO,
        new LetteraIstruttoriaAnticipo());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_1,
        new LetteraIstruttoriaAnticipoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_1,
        new LetteraIstruttoriaAnticipoPreavvisoRigetto(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_2,
        new LetteraIstruttoriaAnticipoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_2,
        new LetteraIstruttoriaAnticipoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_PARZIALE_1,
        new LetteraIstruttoriaAnticipoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_PARZIALE_1));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO,
        new LetteraIstruttoriaSaldo());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_1,
        new LetteraIstruttoriaSaldoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_1,
        new LetteraIstruttoriaSaldoPreavvisoRigetto(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_2,
        new LetteraIstruttoriaSaldoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_2,
        new LetteraIstruttoriaSaldoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_PARZIALE_1,
        new LetteraIstruttoriaSaldoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ISTRUTTORIA_SALDO_PARZIALE_1));

    /* STAMPE VARIANTE */
    MAP_STAMPE.put(IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE,
        new AmmissioneFinanziamento());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_POSITIVO_1,
        new AmmissioneFinanziamentoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_POSITIVO_2,
        new AmmissioneFinanziamentoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_NEGATIVO_1,
        new PreavvisoDiRigettoTotale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_NEGATIVO_2,
        new AmmissioneFinanziamentoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_PARZIALE_1,
        new PreavvisoDiRigettoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_PARZIALE_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_PARZIALE_2,
        new PreavvisoDiRigettoParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE_PARZIALE_2));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE,
        new VerbaleIstruttoriaDomandaSostegno());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_1,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_2,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_1,
        new VerbaleIstruttoriaDomandaSostegnoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_2,
        new VerbaleIstruttoriaDomandaSostegnoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_1,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_1));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_2,
        new VerbaleIstruttoriaDomandaSostegnoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_2));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_INTEG_PAGAMENTO,
        new VerbaleIntegPagamento());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_INTEG_PAGAMENTO,
        new LetteraIntegPagamento());

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA,
        new LetteraProroga());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA_POSITIVO,
        new LetteraProrogaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA_POSITIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA_NEGATIVO,
        new LetteraProrogaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PROROGA_NEGATIVO));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_PROROGA,
        new VerbaleProroga());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_PROROGA_POSITIVO,
        new VerbaleProrogaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_PROROGA_POSITIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_PROROGA_NEGATIVO,
        new VerbaleProrogaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_PROROGA_NEGATIVO));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA,
        new LetteraVoltura());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_POSITIVO,
        new LetteraVolturaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_POSITIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_POSITIVO_2,
        new LetteraVolturaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_NEGATIVO,
        new LetteraVolturaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_NEGATIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_PREAVVISO_RIGETTO,
        new LetteraVolturaParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_VOLTURA_PREAVVISO_RIGETTO));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA,
        new VerbaleVoltura());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_POSITIVO,
        new VerbaleVolturaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_POSITIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_POSITIVO_2,
        new VerbaleVolturaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_NEGATIVO,
        new VerbaleVolturaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_NEGATIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_NEGATIVO_2,
        new VerbaleVolturaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_VOLTURA_NEGATIVO_2));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO,
        new LetteraAnnullamento());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO_POSITIVO,
        new LetteraAnnullamentoPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO_POSITIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO_NEGATIVO,
        new LetteraAnnullamentoNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_ANNULLAMENTO_NEGATIVO));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA,
        new VerbaleRevoca());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA_POSITIVO,
        new VerbalePreavvisoRevocaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA_POSITIVO));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA_POSITIVO_2,
        new VerbaleRevocaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA_POSITIVO_2));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA_NEGATIVO,
        new VerbaleRevocaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_REVOCA_NEGATIVO));

    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_REVOCA,
        new LetteraRevoca());
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PREAVVISO_REVOCA,
        new LetteraRevocaParziale(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_PREAVVISO_REVOCA));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_REVOCA_DEFINITIVA,
        new LetteraRevocaPositivo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_REVOCA_DEFINITIVA));
    MAP_STAMPE.put(
        IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_REVOCA_NEGATIVA,
        new LetteraRevocaNegativo(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_LETTERA_REVOCA_NEGATIVA));

  }

  public Stampa getStampaFromCdU(String cuName)
  {
    return MAP_STAMPE.get(cuName);
  }

  public boolean hasInvioPec(String flagInvioPec, List<StampaOggettoDTO> stampe)
  {
    if (IuffiConstants.FLAGS.SI.equals(flagInvioPec))
    {
      for (StampaOggettoDTO item : stampe)
      {
        if (IuffiConstants.FLAGS.SI.equals(item.getFlagInviaMail()))
        {
          return true;
        }
      }
    }
    return false;
  }

}
