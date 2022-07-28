package it.csi.iuffi.iuffiweb.presentation;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.Allegato;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.DettagliMessaggio;
import it.csi.iuffi.iuffiweb.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public abstract class MessaggisticaBaseController extends BaseController
{
  public void forceRefreshMessaggistica(HttpSession session)
  {
    @SuppressWarnings("unchecked")
    Map<String, Object> mapMessaggistica = (Map<String, Object>) session
        .getAttribute("messaggistica");
    mapMessaggistica.put(MessaggisticaManager.KEY_TIMESTAMP, Long.valueOf(0));
    MessaggisticaManager.checkMessaggistica(session, mapMessaggistica);
  }

  public ResponseEntity<byte[]> downloadAllegato(HttpSession session,
      long idElencoMessaggi, long idAllegato) throws InternalUnexpectedException
  {
    try
    {
      DettagliMessaggio dm = IuffiUtils.WS.getMessaggistica()
          .getDettagliMessaggio(idElencoMessaggi,
              getUtenteAbilitazioni(session).getCodiceFiscale());
      List<Allegato> allegati = dm.getAllegati();
      Allegato infoAllegato = null;
      if (allegati.size() > 0)
      {
        for (Allegato allegato : allegati)
        {
          if (allegato.getIdAllegato() == idAllegato)
          {
            infoAllegato = allegato;
            break;
          }
        }
      }
      ResponseEntity<byte[]> response = null;
      HttpHeaders httpHeaders = new HttpHeaders();
      if (infoAllegato != null)
      {
        byte[] fileAllegato = IuffiUtils.WS.getMessaggistica()
            .getAllegato(idAllegato);
        final String nomeFile = infoAllegato.getNomeFile();
        httpHeaders.add("Content-type",
            IuffiUtils.FILE.getMimeType(nomeFile));
        httpHeaders.add("Content-Disposition",
            "attachment; filename=\"" + nomeFile + "\"");
        response = new ResponseEntity<byte[]>(fileAllegato, httpHeaders,
            HttpStatus.OK);
        return response;
      }
      else
      {
        response = new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
      }
      return response;
    }
    catch (Exception e)
    {
      throw new InternalUnexpectedException(e);
    }
  }
}
