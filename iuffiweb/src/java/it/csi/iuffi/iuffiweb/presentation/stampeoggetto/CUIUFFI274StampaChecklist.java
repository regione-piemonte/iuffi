package it.csi.iuffi.iuffiweb.presentation.stampeoggetto;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.modolxp.modolxpsrv.dto.Applicazione;
import it.csi.modolxp.modolxppdfgensrv.dto.pdfstatic.PdfStaticInputRequest;
import it.csi.modolxp.modolxpsrv.dto.Modello;
import it.csi.modolxp.modolxpsrv.dto.Modulo;
import it.csi.modolxp.modolxpsrv.dto.RendererModality;
import it.csi.modolxp.modolxpsrv.dto.RiferimentoAdobe;
import it.csi.modolxp.modolxpsrv.dto.XmlModel;
import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.ImportoLiquidatoDTO;
import it.csi.iuffi.iuffiweb.dto.RipartizioneImportoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.ControlloAmministrativoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.VisitaLuogoExtDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento.DatiSpecificiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiRappresentanteLegaleDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoByLivelloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-274", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI274StampaChecklist extends BaseController
{
  @Autowired
  IRicercaEJB                ricercaEJB       = null;
  @Autowired
  IQuadroEJB                 quadroEJB        = null;
  @Autowired
  IInterventiEJB             interventiEJB    = null;

  public static final String ROOT_TAG         = "Domanda";
  public static final String DEFAULT_ENCODING = "UTF-8";

  @RequestMapping(value = "/cuiuffi274/stampa", method = RequestMethod.GET)
  public ResponseEntity<byte[]> stampa(HttpSession session,
      HttpServletRequest request) throws Exception
  {
    TestataProcedimento testataProcedimento = (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);
    Procedimento procedimento = IuffiFactory.getProcedimento(request);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/pdf");
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + procedimento.getIdentificativo() + "_"
            + testataProcedimento.getCuaa() + "_Checklist.pdf" + "\"");

    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        generaContenuto(session), httpHeaders, HttpStatus.OK);
    return response;
  }

  public byte[] generaContenuto(HttpSession session) throws Exception
  {
    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    XMLStreamWriter writer = factory.createXMLStreamWriter(xmlOutputStream,
        DEFAULT_ENCODING);
    generaXML(writer, session);
    return callModol(xmlOutputStream.toByteArray());
  }

  protected void generaXML(XMLStreamWriter writer, HttpSession session)
      throws Exception
  {
    String codice;
    ProcedimentoOggetto procedimentoOggetto;

    writer.writeStartDocument("UTF-8", "1.0");
    writer.writeStartElement(ROOT_TAG);

    final UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(
        session);
    List<GruppoOggettoDTO> listGruppiOggetto = ricercaEJB
        .getElencoOggettiChiusi(getIdProcedimento(session),
            Arrays.asList(utenteAbilitazioni.getMacroCU()),
            IuffiUtils.PAPUASERV
                .isAttoreBeneficiarioOCAA(utenteAbilitazioni), utenteAbilitazioni.getIdProcedimento());
    if (listGruppiOggetto != null)
    {
      // Recupero oggetti comuni
      Procedimento procedimento = ricercaEJB
          .getProcedimento(listGruppiOggetto.get(0).getIdProcedimento());
      BandoDTO bandoDTO = ricercaEJB
          .getInformazioniBando(procedimento.getIdBando());
      procedimentoOggetto = quadroEJB.getProcedimentoOggetto(listGruppiOggetto
          .get(0).getOggetti().get(0).getIdProcedimentoOggetto());
      writeGlobal(writer, procedimento, bandoDTO);
      writeDatiIdentificativi(writer, procedimento, bandoDTO,
          procedimentoOggetto);

      for (GruppoOggettoDTO gruppo : listGruppiOggetto)
      {
        writer.writeStartElement("RaggruppamentoOggetti");
        for (OggettoDTO oggetto : gruppo.getOggetti())
        {
          procedimentoOggetto = quadroEJB
              .getProcedimentoOggetto(oggetto.getIdProcedimentoOggetto());
          codice = oggetto.getCodice();

          if (codice.equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_SOSTEGNO)
              || codice.equals(
                  IuffiConstants.OGGETTO.CODICE.DOMANDA_SOSTEGNO_PAGAMENTO)
              || codice.equals(
                  IuffiConstants.OGGETTO.CODICE.DOMANDA_SOSTEGNO_PREMIO))
          {
            writeDomandaAiuto(writer, oggetto, procedimentoOggetto);
          }
          else
            if (codice.equals(
                IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO)
                || codice.equals(
                    IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO_GAL)
                || codice.equals(
                    IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA_FINANZIAMENTO_GAL)
                || codice.equals(
                    IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA_FINANZIAMENTO))
            {
              writeIstruttoriaInvestimento(writer, oggetto,
                  procedimentoOggetto);
            }
            else
              if (codice.equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_SALDO)
                  || codice
                      .equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_ACCONTO)
                  || codice.equals(
                      IuffiConstants.OGGETTO.CODICE.DOMANDA_ANTICIPO))
              {
                writeDomandaPagamento(writer, oggetto, procedimentoOggetto);
              }
              else
                if (codice.equals(
                    IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_ACCONTO)
                    || codice.equals(
                        IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_SALDO)
                    || codice.equals(
                        IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_ANTICIPO))
                {
                  writeIstruttoriaPagamento(writer, oggetto,
                      procedimentoOggetto);
                }
        }
        writer.writeEndElement(); // RaggruppamentoOggetti
      }

      writeSezioneData(writer);
    }

    writer.writeEndElement();
    writer.writeEndDocument();
  }

  private void writeIstruttoriaPagamento(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    writer.writeStartElement("SezioneIstruttoriaPagamento");
    writeTag(writer, "NomeOggetto", oggetto.getDescrizione());
    writeSezioneCTAMM(writer, oggetto, procedimentoOggetto);
    writeSezioneCTAMMLuogo(writer, oggetto, procedimentoOggetto);
    writeSezioneDatiSpecifici(writer, oggetto, procedimentoOggetto);
    writeSezioneVisitaLuogo(writer, oggetto, procedimentoOggetto);
    writeSezioneEsitoTecnico(writer, oggetto, procedimentoOggetto);
    writeSezioneEsitoFinale(writer, oggetto, procedimentoOggetto);
    writeSezioneLiquidazione(writer, oggetto, procedimentoOggetto);
    writer.writeEndElement();
  }

  private void writeSezioneLiquidazione(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    List<ImportoLiquidatoDTO> importi = quadroEJB.getElencoImportiLiquidazione(
        procedimentoOggetto.getIdProcedimentoOggetto());
    writer.writeStartElement("SezioneLiquidazione");
    writeTag(writer, "VisibilityLiquidazione",
        String.valueOf(importi != null && importi.size() > 0));
    if (importi != null && importi.size() > 0)
    {
      writer.writeStartElement("TabellaImportoLiquidato");

      for (ImportoLiquidatoDTO item : importi)
      {
        writer.writeStartElement("RigaImportoLiquidato");

        writeTag(writer, "Livello", item.getCodiceLivello());
        writeTag(writer, "NLista",
            String.valueOf(IuffiUtils.STRING.nvl(item.getNumeroLista())));
        writeTag(writer, "StatoLista", item.getStatoLista());
        writeTag(writer, "TecnicoLiquidatore", item.getTecnico());
        writeTag(writer, "Importo",
            IuffiUtils.FORMAT.formatCurrency(item.getImportoLiquidato()));

        List<RipartizioneImportoDTO> quote = quadroEJB
            .getRipartizioneImporto(item.getIdListaLiquidazImpLiq());
        if (quote != null && !quote.isEmpty())
        {
          writer.writeStartElement("Ripartizione");

          for (RipartizioneImportoDTO itemq : quote)
          {
            writeTag(writer, "VoceRipart", itemq.getVoceRipartizione());
            writeTag(writer, "PercentRipart", IuffiUtils.FORMAT
                .formatCurrency(itemq.getPercentualeRipartizione()));
            writeTag(writer, "ImportoRipart", IuffiUtils.FORMAT
                .formatCurrency(itemq.getImportoRipartito()));
          }

          writer.writeEndElement(); // Ripartizione
        }

        writer.writeEndElement(); // RigaImportoLiquidato
      }

      writer.writeEndElement(); // TabellaImportoLiquidato
    }

    writer.writeEndElement(); // SezioneLiquidazione
  }

  private void writeSezioneDatiSpecifici(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    DatiSpecificiDTO datiSpecifici = quadroEJB.getDatiSpecifici(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        procedimentoOggetto.getIdProcedimento());
    if (datiSpecifici != null)
    {
      writer.writeStartElement("SezioneDatiSpecifici");
      writeTag(writer, "FlagEstrazione",
          ("S".equals(datiSpecifici.getFlagSottopostaEstrazione()) ? "Si"
              : "No"));
      writeTag(writer, "FlagCampione",
          ("S".equals(datiSpecifici.getFlagEstratta()) ? "Si" : "No"));
      writeTag(writer, "DataEstrazione",
          IuffiUtils.DATE.formatDate(datiSpecifici.getDataEstrazione()));
      writer.writeEndElement(); // SezioneDatiSpecifici
    }
  }

  private void writeDomandaPagamento(XMLStreamWriter writer, OggettoDTO oggetto,
      ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
	  DecodificaDTO<String> datiProt = new DecodificaDTO<>();
	  if((IuffiConstants.OGGETTO.CODICE.DOMANDA_SALDO).equals(oggetto.getCodice())){
		  datiProt =ricercaEJB.findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2007);
	  }else if((IuffiConstants.OGGETTO.CODICE.DOMANDA_ACCONTO).equals(oggetto.getCodice())){
		  datiProt =ricercaEJB.findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2006);
	  }else if((IuffiConstants.OGGETTO.CODICE.DOMANDA_ANTICIPO).equals(oggetto.getCodice())){
		  datiProt =ricercaEJB.findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2005);
	  }
    String organismoDelegato = quadroEJB
        .getTestataProcedimento(procedimentoOggetto.getIdProcedimento())
        .getDescAmmCompetenza();
    writer.writeStartElement("SezioneDomandaPagamento");
    writeTag(writer, "NomeOggetto", oggetto.getDescrizione());
    writeTag(writer, "NDomanda",
        IuffiUtils.STRING.nvl(oggetto.getCodiceDomanda()));
    writeTag(writer, "NProtocollo",
        IuffiUtils.STRING.nvl(datiProt.getCodice()));
    writeTag(writer, "DataProtocollo",
        IuffiUtils.STRING.nvl(datiProt.getDescrizione()));
    writeTag(writer, "OD", IuffiUtils.STRING.nvl(organismoDelegato));
    writeTag(writer, "RespProcedimento", IuffiUtils.STRING.nvl(quadroEJB
        .getResponsabileProcedimento(procedimentoOggetto.getIdProcedimento())));
    writer.writeEndElement();
  }

  private void writeIstruttoriaInvestimento(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    writer.writeStartElement("SezioneIstruttoriaAmmissione");
    writeTag(writer, "NomeOggetto", oggetto.getDescrizione());
    writeSezioneCTAMM(writer, oggetto, procedimentoOggetto);
    writeSezioneVisitaLuogo(writer, oggetto, procedimentoOggetto);
    writeSezioneEsitoTecnico(writer, oggetto, procedimentoOggetto);
    writeSezioneEsitoFinale(writer, oggetto, procedimentoOggetto);
    writer.writeEndElement();
  }

  private void writeSezioneData(XMLStreamWriter writer)
      throws XMLStreamException, InternalUnexpectedException
  {
    writer.writeStartElement("RiquadroData");
    writeTag(writer, "Data", IuffiUtils.DATE
        .formatDate(IuffiUtils.DATE.getCurrentDateNoTime()));
    writer.writeEndElement(); // RiquadroData
  }

  private void writeSezioneEsitoFinale(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    BigDecimal importoRichiestoTot = BigDecimal.ZERO;
    BigDecimal spesaAmmessaTot = BigDecimal.ZERO;
    BigDecimal contributoConcessoTot = BigDecimal.ZERO;
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-166-V");
    if (quadro != null)
    {
      EsitoFinaleDTO esitoFinale = quadroEJB.getEsitoFinale(
          procedimentoOggetto.getIdProcedimentoOggetto(),
          quadro.getIdQuadroOggetto());
      if (esitoFinale != null)
      {
        writer.writeStartElement("SezioneEsitoFinale");

        writeTag(writer, "EsitoFinale", esitoFinale.getDescrEsito());
        writeTag(writer, "Motivazioni",
            IuffiUtils.STRING.nvl(esitoFinale.getMotivazione()));
        writeTag(writer, "Prescrizioni",
            IuffiUtils.STRING.nvl(esitoFinale.getPrescrizioni()));
        writeTag(writer, "FunzionarioIstruttore",
            IuffiUtils.STRING.nvl(esitoFinale.getDescrTecnico()));
        writeTag(writer, "FunzionarioSup", esitoFinale.getDescrGradoSup());

        List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> interventi = interventiEJB
            .getElencoInterventiByLivelliQuadroEconomico(
                procedimentoOggetto.getIdProcedimentoOggetto());
        writeTag(writer, "VisibilityImporti",
            String.valueOf(interventi != null && interventi.size() > 0));

        if (interventi != null)
        {
          writer.writeStartElement("TabellaImporti");

          importoRichiestoTot = BigDecimal.ZERO;
          spesaAmmessaTot = BigDecimal.ZERO;
          contributoConcessoTot = BigDecimal.ZERO;

          for (RigaJSONInterventoQuadroEconomicoByLivelloDTO item : interventi)
          {
            importoRichiestoTot = importoRichiestoTot
                .add(item.getImportoInvestimento());
            spesaAmmessaTot = spesaAmmessaTot.add(item.getImportoAmmesso());
            contributoConcessoTot = contributoConcessoTot
                .add(item.getImportoContributo());

            writer.writeStartElement("Importi");
            writeTag(writer, "Misura", item.getCodiceLivello());
            writeTag(writer, "DataAmmissione",
                IuffiUtils.DATE.formatDate(item.getDataAmmissione()));
            writeTag(writer, "ImportoRichiesto", IuffiUtils.FORMAT
                .formatCurrency(item.getImportoInvestimento()));
            writeTag(writer, "SpesaAmmessa",
                IuffiUtils.FORMAT.formatCurrency(item.getImportoAmmesso()));
            writeTag(writer, "ContributoConcesso", IuffiUtils.FORMAT
                .formatCurrency(item.getImportoContributo()));
            writer.writeEndElement(); // Importi
          }

          writer.writeStartElement("Totali");
          writeTag(writer, "ImportoRichiestoTot",
              IuffiUtils.FORMAT.formatCurrency(importoRichiestoTot));
          writeTag(writer, "SpesaAmmessaTot",
              IuffiUtils.FORMAT.formatCurrency(spesaAmmessaTot));
          writeTag(writer, "ContributoConcessoTot",
              IuffiUtils.FORMAT.formatCurrency(contributoConcessoTot));
          writer.writeEndElement(); // Totali
          writer.writeEndElement(); // TabellaImporti
        }

        writer.writeEndElement();
      }
    }
  }

  private void writeSezioneEsitoTecnico(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
	  QuadroOggettoDTO quadroDTO = procedimentoOggetto.findQuadroByCodiceQuadro(IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI);
	  if(quadroDTO!=null){
		  Long idQuadroOggetto = quadroDTO.getIdQuadroOggetto();
		  if(idQuadroOggetto!=null){
			  EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto);
			  if(esito!=null)
			  {
				  writer.writeStartElement("SezioneEsitoTecnico");
				  writeTag(writer, "FunzionarioIstruttore", IuffiUtils.STRING.nvl(esito.getDescrTecnico()));
				  writeTag(writer, "FunzionarioSup", IuffiUtils.STRING.nvl(esito.getDescrGradoSup()));
				  writeTag(writer, "EsitoControlli", IuffiUtils.STRING.nvl(esito.getDescrEsito()));
				  writer.writeEndElement();
			  }
		  }
	  } 
  }

  private void writeSezioneCTAMMLuogo(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    List<ControlloAmministrativoDTO> controlliAmministrativi = quadroEJB
        .getControlliAmministrativi(oggetto.getIdProcedimentoOggetto(),
            IuffiConstants.QUADRO.CODICE.CONTROLLI_IN_LOCO_MISURE_INVESTIMENTO,
            null);
    writer.writeStartElement("SezioneCTAMMLuogo");
    writeTag(writer, "VisibilityCTAMMLuogo", String.valueOf(
        controlliAmministrativi != null && controlliAmministrativi.size() > 0));
    if (controlliAmministrativi != null)
    {
      writer.writeStartElement("TabellaCTAMM");
      for (ControlloAmministrativoDTO controllo : controlliAmministrativi)
      {
        writer.writeStartElement("RigaCTAMM");
        writeTag(writer, "isPadre",
            (controllo.getIdControlloAmministratPadre() != null) ? "true"
                : "false");
        writeTag(writer, "Codice", controllo.getCodice());
        writeTag(writer, "Descrizione", controllo.getDescrizione());
        writeTag(writer, "Esito", controllo.getDescEsito());
        writer.writeEndElement();
      }
      writer.writeEndElement();
    }
    writer.writeEndElement();
  }

  private void writeSezioneCTAMM(XMLStreamWriter writer, OggettoDTO oggetto,
      ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    List<ControlloAmministrativoDTO> controlliAmministrativi = quadroEJB
        .getControlliAmministrativi(oggetto.getIdProcedimentoOggetto(),
            IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI, null);
    writer.writeStartElement("SezioneCTAMM");
    writeTag(writer, "VisibilityCTAMM", String.valueOf(
        controlliAmministrativi != null && controlliAmministrativi.size() > 0));
    if (controlliAmministrativi != null)
    {
      writer.writeStartElement("TabellaCTAMM");
      for (ControlloAmministrativoDTO controllo : controlliAmministrativi)
      {
        writer.writeStartElement("RigaCTAMM");
        writeTag(writer, "isPadre",
            (controllo.getIdControlloAmministratPadre() != null) ? "true"
                : "false");
        writeTag(writer, "Codice", controllo.getCodice());
        writeTag(writer, "Descrizione", controllo.getDescrizione());
        writeTag(writer, "Esito", controllo.getDescEsito());
        writer.writeEndElement();
      }
      writer.writeEndElement();
    }
    writer.writeEndElement();
  }

  private void writeSezioneVisitaLuogo(XMLStreamWriter writer,
      OggettoDTO oggetto, ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
	  QuadroOggettoDTO quadroDTO = procedimentoOggetto.findQuadroByCodiceQuadro(IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI);
	  if(quadroDTO!=null){
		  Long idQuadroOggetto = quadroDTO.getIdQuadroOggetto();
		  if(idQuadroOggetto!=null){
			  List<VisitaLuogoExtDTO> visite = quadroEJB.getVisiteLuogo(procedimentoOggetto.getIdProcedimentoOggetto(),idQuadroOggetto, null);
			  writer.writeStartElement("SezioneVisitaLuogo");
			  writeTag(writer, "VisibilityVisitaLuogo", String.valueOf(visite!=null && visite.size()>0));
			  if(visite!=null)
			  {
				  
				  writer.writeStartElement("TabellaVisitaLuogo");
				  for(VisitaLuogoExtDTO item: visite)
			  	  {
					  writer.writeStartElement("RigaVisita");
					  writeTag(writer, "DataVisita", IuffiUtils.DATE.formatDate(item.getDataVisita()));
					  writeTag(writer, "FunzionarioControllore",IuffiUtils.STRING.nvl(item.getDescTecnico()));
					  writeTag(writer, "Esito",IuffiUtils.STRING.nvl(item.getDescEsito()));
					  writeTag(writer, "DataVerbale",IuffiUtils.DATE.formatDate(item.getDataVerbale()));
					  writeTag(writer, "NumeroVerbale",IuffiUtils.STRING.nvl(item.getNumeroVerbale()));
					  writer.writeEndElement();
			  	  }
				  writer.writeEndElement();
				  
			  }
			  writer.writeEndElement();
		  }
	  }  
  }

  private void writeDomandaAiuto(XMLStreamWriter writer, OggettoDTO oggetto,
      ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
    DecodificaDTO<String> datiProt = ricercaEJB
        .findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2002);
    String organismoDelegato = quadroEJB
        .getTestataProcedimento(procedimentoOggetto.getIdProcedimento())
        .getDescAmmCompetenza();
    writer.writeStartElement("SezioneDomandaSostegno");
    writeTag(writer, "NomeOggetto", oggetto.getDescrizione());
    writeTag(writer, "NDomanda",
        IuffiUtils.STRING.nvl(oggetto.getCodiceDomanda()));
    writeTag(writer, "NProtocollo",
        IuffiUtils.STRING.nvl(datiProt.getCodice()));
    writeTag(writer, "DataProtocollo",
        IuffiUtils.STRING.nvl(datiProt.getDescrizione()));
    writeTag(writer, "OD", IuffiUtils.STRING.nvl(organismoDelegato));
    writeTag(writer, "RespProcedimento", IuffiUtils.STRING.nvl(quadroEJB
        .getResponsabileProcedimento(procedimentoOggetto.getIdProcedimento())));
    writer.writeEndElement();
  }

  private void writeDatiIdentificativi(XMLStreamWriter writer,
      Procedimento procedimento, BandoDTO bandoDTO,
      ProcedimentoOggetto procedimentoOggetto)
      throws XMLStreamException, InternalUnexpectedException
  {
	    DatiIdentificativi dati = quadroEJB
        .getDatiIdentificativiProcedimentoOggetto(
            procedimentoOggetto.getIdProcedimentoOggetto(), procedimentoOggetto
                .findQuadroByCU(
                    IuffiConstants.USECASE.DATI_IDENTIFICATIVI.DETTAGLIO)
                .getIdQuadroOggetto(),
            procedimentoOggetto.getDataFine(), 0);

    writer.writeStartElement("SezioneDatiIdentificativi");

    writer.writeStartElement("DatiAzienda");
    DatiAziendaDTO azienda = dati.getAzienda();
    writeTag(writer, "CUAA", azienda.getCuaa());
    writeTag(writer, "PartitaIva", azienda.getPartitaIva());
    writeTag(writer, "Denominazione", azienda.getDenominazione());
    writeTag(writer, "FormaGiuridica", azienda.getFormaGiuridica());
    writeTag(writer, "SedeLegale", azienda.getIndirizzoSedeLegale());
    writer.writeEndElement();

    writer.writeStartElement("DatiTitolare");
    DatiRappresentanteLegaleDTO rappLegale = dati.getRappLegale();
    writeTag(writer, "CognomeTitolare", rappLegale.getCognome());
    writeTag(writer, "NomeTitolare", rappLegale.getNome());
    writeTag(writer, "CodiceFiscaleTitolare", rappLegale.getCodiceFiscale());
    writeTag(writer, "IndirizzoResidenzaTitolare",
        rappLegale.getIndirizzoResidenza());
    writer.writeEndElement();
    writer.writeEndElement();
  }

  private void writeGlobal(XMLStreamWriter writer, Procedimento procedimento,
      BandoDTO bando) throws XMLStreamException, InternalUnexpectedException
  {
    writer.writeStartElement("Global");
	//Rimosso IsGAL
	//Rimosso AmmGAL
    writer.writeEndElement();
  }

  public byte[] callModol(byte[] xmlInput) throws Exception
  {
    /*
     * imposto la modalità di rendering da utilizzare per la restituzione dei
     * dati
     */
    RendererModality rm = new RendererModality();
    rm.setIdRendererModality(new Integer(3)); // PDF
    rm.setSelezionataPerRendering(true);

    /*
     * imposto il percorso di memorizzazione del template interno al server
     * LiveCycle
     */
    RiferimentoAdobe rifAdobe = new RiferimentoAdobe();
    rifAdobe.setXdpURI(getRifAdobe());

    /* definisco il Modello da utilizzare */
    Modello modello = new Modello();
    modello.setCodiceModello(getCodiceModello());
    modello.setRendererModality(new RendererModality[]
    { rm });
    modello.setRiferimentoAdobe(rifAdobe);

    /* definisco il Modulo da utilizzare */
    Modulo modulo = new Modulo();
    modulo.setCodiceModulo(getCodiceModulo());
    modulo.setModello(modello);

    /* definisco l'Applicazione da utilizzare */
    Applicazione applicazione = new Applicazione();
    applicazione.setCodiceApplicazione(getCodiceApplicazione());
    applicazione.setDescrizioneApplicazione(getDescrizioneApplicazione());

    /*
     * predispongo l'oggetto con i dati da associare al modulo e
     * all'applicazione
     */
    XmlModel xml = new XmlModel();
    xml.setXmlContent(xmlInput);

    /*
     * finalmente invoco il servizio tramite la PD già istanziata in precedenza
     */
    Modulo moduloMerged = IuffiUtils.WS.getModolServClient()
        .mergeModulo(applicazione, null, modulo, xml);

    /*
     * recupero l'array di byte contenente il PDF e lo restituisco al chiamante
     */
    byte[] ba = moduloMerged.getDataContent();
    ba = trasformStaticPDF(ba, applicazione);
    return ba;
  }

  private byte[] trasformStaticPDF(byte[] pdfBytes, Applicazione applicazione)
      throws Exception
  {
    byte[] bXmlModol = null;

    /*
     * predispongo l'oggetto con i dati da associare al modulo e
     * all'applicazione
     */
    XmlModel xml = new XmlModel();
    xml.setXmlContent(pdfBytes);

    /*
     * recupero l'array di byte contenente il PDF e lo restituisco al chiamante
     */
    PdfStaticInputRequest pdfStatic = new PdfStaticInputRequest();
    pdfStatic.setPdfInput(pdfBytes);

    it.csi.modolxp.modolxppdfgensrv.dto.Applicazione applicazione2 = new it.csi.modolxp.modolxppdfgensrv.dto.Applicazione();
    applicazione.setCodiceApplicazione(applicazione.getCodiceApplicazione());
    applicazione
        .setDescrizioneApplicazione(applicazione.getDescrizioneApplicazione());

    bXmlModol = IuffiUtils.WS.getModolPDFGenServClient()
        .toStaticPdf(applicazione2, null, pdfStatic);

    return bXmlModol;
  }

  protected String getCodiceModulo()
  {
    return "IUF_checklist";
  }

  protected String getCodiceModello()
  {
    return "IUF_checklist";
  }

  protected String getRifAdobe()
  {
    return Stampa.BASE_RIF_ADOBE + "Checklist.xdp";
  }

    public String getDefaultFileName(long idProcedimentoOggetto)
  {
    return "Checklist.pdf";
  }

  public String getDescrizioneApplicazione()
  {
    return "Gestione Pratiche IUFFI";
  }

  public String getCodiceApplicazione()
  {
    return "IUFFIWEB";
  }

  protected void writeTag(XMLStreamWriter writer, String name, String value)
      throws XMLStreamException
  {
    writeTag(writer, name, value, false);
  }

  protected void writeTag(XMLStreamWriter writer, String name, String value,
      boolean blankAsNull) throws XMLStreamException
  {
    if (value == null)
    {
      if (blankAsNull)
      {
        value = "";
      }
      else
      {
        return;
      }
    }
    writer.writeStartElement(name);
    try
    {
      writer.writeCharacters(value);
    }
    catch (Exception e)
    {
      throw new XMLStreamException(e);
    }
    writer.writeEndElement();
  }
}
