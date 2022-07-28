package it.csi.iuffi.iuffiweb.presentation.quadro.punteggi;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainPunteggioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-162", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi162")
public class CUIUFFI162CalcolaPunti extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEJB;

  @RequestMapping("/index")
  public final String calcola(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    final Procedimento procedimento = getProcedimentoFromSession(session);
    final ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    final TestataProcedimento testataProcedimento = (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);
    final Long idBando = procedimento.getIdBando();
    final LogOperationOggettoQuadroDTO logOperationOggettoQuadro = getLogOperationOggettoQuadroDTO(
        session);
    final List<LivelloDTO> livelli = this.quadroEJB
        .getLivelliProcOggetto(procedimento.getIdProcedimento());
    Long idLivelloRef = null;
    if (livelli != null && livelli.size() == 1)
    {
      idLivelloRef = livelli.get(0).getIdLivello();
    }

    final MainPunteggioDTO punteggiDTO = this.quadroEJB.callMainCalcoloPunteggi(
        logOperationOggettoQuadro, procedimento.getIdProcedimento(),
        procedimentoOggetto.getIdProcedimentoOggetto(),
        testataProcedimento.getIdAzienda(), idBando, idLivelloRef);

    final int risultato = punteggiDTO.getRisultato();
    final String messaggio = punteggiDTO.getMessaggio();
    final String avanti = ". Premere \"Avanti\" per visualizzare il risultato";
    if (risultato == IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO)
    {
      model.addAttribute("msgErrore",
          "Si è verificato un errore di sistema. Contattare l'assistenza comunicando il seguente messaggio: "
              + messaggio + avanti);
    }
    else
      if (risultato == IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE)
      {
        model.addAttribute("msgErrore",
            "Si è verificato il seguente errore bloccante: " + messaggio
                + avanti);
      }
      else
        if (risultato == IuffiConstants.SQL.RESULT_CODE.ERRORE_NON_BLOCCANTE)
        {
          model.addAttribute("msgErrore",
              "Elaborazione terminata correttamente con la seguente segnalazione: "
                  + messaggio + avanti);
        }
        else
        {
          model.addAttribute("msgInfo",
              "Elaborazione terminata correttamente; premere \"Avanti\" per visualizzare il risultato");
        }

    return "punteggi/esitoCalcoloPunteggi";
  }

}
