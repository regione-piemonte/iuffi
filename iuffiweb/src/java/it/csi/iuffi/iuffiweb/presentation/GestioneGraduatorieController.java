package it.csi.iuffi.iuffiweb.presentation;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.GraduatoriaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/elencoBandi")
@IuffiSecurity(value = "000", controllo = IuffiSecurity.Controllo.NESSUNO)
public class GestioneGraduatorieController extends BaseController
{

  @Autowired
  private IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "elencograduatorie_{idBando}")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "elencoBandi/elencoGraduatorie";
  }

  @RequestMapping(value = "getElencoGraduatorie", produces = "application/json")
  @ResponseBody
  public List<GraduatoriaDTO> getGraduatorieJson(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("idBando");
    List<GraduatoriaDTO> graduatorie = ricercaEJB
        .getGraduatorieBando(bando.getIdBando());

    for (GraduatoriaDTO g : graduatorie)
    {
      List<FileAllegatoDTO> allegati = ricercaEJB
          .getAllegatiGraduatoria(g.getIdGraduatoria());
      g.setElencoAllegati(allegati);
    }

    return graduatorie;
  }

  @RequestMapping(value = "downloadgraduatoria_{idAllegatiGraduatoria}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> downloadAllegato(
      @PathVariable("idAllegatiGraduatoria") long idAllegatiGraduatoria,
      HttpSession session) throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO file = ricercaEJB
        .getFileAllegatoGraduatoria(idAllegatiGraduatoria);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(file.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + file.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        file.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }
}
