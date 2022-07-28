package it.csi.iuffi.iuffiweb.presentation.monitoraggio;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException_Exception;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.WsUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
@IuffiSecurity(value = "CU-IUFFI-206", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping(value = "/cuiuffi206")
public class CUIUFFI206Monitoraggio extends BaseController
{
  @Autowired
  private IQuadroEJB            quadroEJB;
  protected static final String IMAGE_SUCCESS   = "success_big.png";
  protected static final String IMAGE_ERROR     = "fail_big.png";
  protected static final String DB_OBJECT_VALID = "VALID";

  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session)
  {
    final UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(
        session);
    final boolean utenteServiziAgri = utenteAbilitazioni.getRuolo()
        .isUtenteServiziAgri();
    List<String[]> servizi = new ArrayList<String[]>();
    servizi.add(checkWSProfilazione(utenteAbilitazioni));
    servizi.add(checkWSMessaggistica(utenteAbilitazioni));
    servizi.add(checkAgriwell());
    servizi.add(checkModolXPSerClient());
    servizi.add(checkModolXPPDFGenServClient());
    boolean serviziOK = true;
    for (String[] servizio : servizi)
    {
      if (IMAGE_ERROR.equals(servizio[1]))
      {
        serviziOK = false;
        break;
      }
    }
    if (serviziOK)
    {
      model.addAttribute("serviziOK", Boolean.valueOf(serviziOK));
    }
    model.addAttribute("servizi", servizi);
    if (utenteServiziAgri)
    {
      List<String[]> statoDB = listDatabaseStatus();
      model.addAttribute("statoDB", statoDB);
      model.addAttribute("pulsanteBack", Boolean.TRUE);
    }
    else
    {
      model.addAttribute("pulsanteBack", Boolean.FALSE);
    }
    model.addAttribute("applicativoOK", checkDatabase());
    String target = readVersionPropertyFile(model);
    if (target != null)
    {
      if (target.startsWith("prod-"))
      {
        String msgCooerenza = verificaCoorenzaAmbienteProd(servizi);
        model.addAttribute("msgCoerenza", msgCooerenza);
      }
      else
      {
        if (target.startsWith("coll-"))
        {
          String msgCooerenza = verificaCoorenzaAmbienteColl(servizi);
          model.addAttribute("msgCoerenza", msgCooerenza);
        }
      }
    }
    return "monitoraggio/monitoraggio";
  }

  private String verificaCoorenzaAmbienteProd(List<String[]> servizi)
  {
    try
    {
      for (String[] servizio : servizi)
      {
        String url = servizio[3];
        if (url != null)
        {
          url = url.toLowerCase();
          final boolean isTst = url.contains("tst-");
          if (isTst || url.contains("test"))
          {
            if (isTst)
            {
              return "Il pacchetto è stato prodotto per l'ambiente di PRODUZIONE ma è stata rilevata la presenza sospetta della stringa \"tst-\" (che in genere indica i server di test) in almeno uno dei puntamenti dei servizi, potrebbe esserci un problema... N.B. Non è necessariamente un errore ma si prega di verificare";
            }
            else
            {
              return "Il pacchetto è stato prodotto per l'ambiente di PRODUZIONE ma è stata rilevata la presenza sospetta della stringa \"test\" (che in genere indica i server di test) in almeno uno dei puntamenti dei servizi, potrebbe esserci un problema... N.B. Non è necessariamente un errore ma si prega di verificare";
            }
          }
          if (url.contains("coll-"))
          {
            return "Il pacchetto è stato prodotto per l'ambiente di PRODUZIONE ma è stata rilevata la presenza sospetta della stringa \"coll-\" (che in genere indica i server di collaudo) in almeno uno dei puntamenti dei servizi, potrebbe esserci un problema... N.B. Non è necessariamente un errore ma si prega di verificare";
          }
        }
      }
    }
    catch (Exception e)
    {
      // Impossibile eseguire la validazione, per il momento non visualizzo
      // nessun warning
    }
    return null;
  }

  private String verificaCoorenzaAmbienteColl(List<String[]> servizi)
  {
    try
    {
      for (String[] servizio : servizi)
      {
        String url = servizio[3];
        if (url != null)
        {
          url = url.toLowerCase();
          if (url.contains("tst-"))
          {
            return "Il pacchetto è stato prodotto per l'ambiente di COLLAUDO ma è stata rilevata la presenza sospetta della stringa \"tst-\" (che in genere indica i server di test) in almeno uno dei puntamenti dei servizi, potrebbe esserci un problema... N.B. Non è necessariamente un errore ma si prega di verificare";
          }
        }
      }
    }
    catch (Exception e)
    {
      // Impossibile eseguire la validazione, per il momento non visualizzo
      // nessun warning
    }
    return null;
  }

  public String readManifestInfo(Model model)
  {
    Enumeration<URL> resources;
    try
    {
      resources = getClass().getClassLoader()
          .getResources("/META-INF/MANIFEST.MF");
      while (resources.hasMoreElements())
      {
        try
        {
          Manifest manifest = new Manifest(
              resources.nextElement().openStream());
          Attributes attr = manifest.getMainAttributes();
          final String componentName = attr.getValue("ComponentName");
          if (componentName != null)
          {
            model.addAttribute("componentName", componentName.toUpperCase());
            model.addAttribute("componentVersion",
                attr.getValue("ComponentVersion"));
            model.addAttribute("builtBy", attr.getValue("Built-By"));
            model.addAttribute("builtDate",
                convertBuiltDate(attr.getValue("Built-Date")));
            String target = attr.getValue("Target");
            model.addAttribute("target", target);
            model.addAttribute("antVersion", attr.getValue("Ant-Version"));
            model.addAttribute("createdBy", attr.getValue("Created-By"));
            return target;
          }
        }
        catch (IOException E)
        {
          // handle
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public String readVersionPropertyFile(Model model)
  {
    try
    {
      ResourceBundle version = ResourceBundle.getBundle("version");
      final String componentName = version.getString("ComponentName");
      if (componentName != null)
      {
        model.addAttribute("componentName", componentName.toUpperCase());
        model.addAttribute("componentVersion",
            version.getString("ComponentVersion"));
        model.addAttribute("builtBy", version.getString("Built-By"));
        model.addAttribute("builtDate",
            convertBuiltDate(version.getString("Built-Date")));
        String target = version.getString("Target");
        model.addAttribute("target", target);
        model.addAttribute("antVersion", version.getString("Ant-Version"));
        model.addAttribute("createdBy", version.getString("Created-By"));
        return target;
      }
    }
    catch (Exception E)
    {
      // handle
    }
    return null;
  }

  private String convertBuiltDate(String value)
  {
    SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    try
    {
      return IuffiUtils.DATE.formatDateTime(sd.parse(value));
    }
    catch (ParseException e)
    {
      return value;
    }
  }

  private String[] checkWSProfilazione(UtenteAbilitazioni utenteAbilitazioni)
  {
    String[] result =
    { "Profilazione", null, null, IuffiUtils.STRING.concat("<br />",
        PapuaservProfilazioneServiceFactory.getRestServiceClient()
            .getRestServiceUrl()) };
    try
    {
      PapuaservProfilazioneServiceFactory.getRestServiceClient()
          .findAttoriForRuoloInApplicazione(
              utenteAbilitazioni.getRuolo().getCodice(), 15,
              utenteAbilitazioni.getIdProcedimento());
      result[1] = IMAGE_SUCCESS;
      return result;
    }
    catch (Exception e)
    {
      result[1] = IMAGE_ERROR;
      result[2] = e.toString();
      return result;
    }
  }

  private String[] checkWSMessaggistica(UtenteAbilitazioni utenteAbilitazioni)
  {
    String[] result =
    { "Messaggistica", null, null, IuffiUtils.STRING.concat("<br />",
        WsUtils.PAPUASERV_MESSAGGISTICA_WSDL) };
    try
    {
      IuffiUtils.WS.getMessaggistica().getListaMessaggi(
          utenteAbilitazioni.getIdProcedimento(),
          utenteAbilitazioni.getRuolo().getCodice(),
          utenteAbilitazioni.getCodiceFiscale(),
          MessaggisticaManager.FLAG_GENERICO, Boolean.FALSE, Boolean.TRUE,
          Boolean.TRUE);
      result[1] = IMAGE_SUCCESS;
      return result;
    }
    catch (LogoutException_Exception e)
    {
      result[1] = IMAGE_SUCCESS; // LogoutException non è un malfunzionamento
                                 // del servizio
      return result;
    }
    catch (Exception e)
    {
      result[1] = IMAGE_ERROR;
      result[2] = e.toString();
      return result;
    }
  }
  
  private String[] checkModolXPSerClient()
  {
    String[] result =
    { "Modol XP Service Client", null, null, IuffiUtils.WS.getModolXPServClientURL() };
    try
    {
      IuffiUtils.WS.getModolServClient().testResources();
      result[1] = IMAGE_SUCCESS;
      return result;
    }
    catch (Exception e)
    {
      result[1] = IMAGE_ERROR;
      result[2] = e.toString();
      return result;
    }
  }
  
  private String[] checkModolXPPDFGenServClient()
  {
    String[] result =
      { "Modol XP PDF Gen Service Client", null, null, IuffiUtils.WS.getModolXPPDFGenServClientURL() };
    try
    {
      IuffiUtils.WS.getModolPDFGenServClient().testResources();
      result[1] = IMAGE_SUCCESS;
      return result;
    }
    catch (Exception e)
    {
      result[1] = IMAGE_ERROR;
      result[2] = e.toString();
      return result;
    }
  }
  
  private String[] checkAgriwell()
  {
    String[] result =
    { "Agriwell", null, null,
        IuffiUtils.PORTADELEGATA.getProviderURLAgriwell() };
    try
    {
      IuffiUtils.PORTADELEGATA.getAgriwellCSIInterface()
          .agriwellServiceLeggiDoquiAgri(-1l);
      result[1] = IMAGE_SUCCESS;
      return result;
    }
    catch (Exception e)
    {
      result[1] = IMAGE_ERROR;
      result[2] = e.toString();
      return result;
    }
  }

  private Boolean checkDatabase()
  {
    try
    {
      // TEST del Database
      quadroEJB.getSysDate(); // Ignoro il valore, mi interessa che esegua la
                              // query e non vada in eccezione
      // Se sono arrivato qui non ci sono state eccezioni, quindi test eseguito
      // con successo ==> DATABASE OK
      return Boolean.TRUE;
    }
    catch (Exception e)
    {
      return Boolean.FALSE;
    }
  }

  private List<String[]> listDatabaseStatus()
  {
    try
    {
      // TEST dei package/function
      List<String[]> list = quadroEJB.getStatoDatabase();
      if (list != null)
      {
        for (String[] dbObject : list)
        {
          if (DB_OBJECT_VALID.equalsIgnoreCase(dbObject[1]))
          {
            dbObject[2] = IMAGE_SUCCESS;
          }
          else
          {
            dbObject[2] = IMAGE_ERROR;
          }
        }
      }
      // Se sono arrivato qui non ci sono state eccezioni, quindi test eseguito
      // con successo
      return list;
    }
    catch (Exception e)
    {
      return null;
    }
  }
}
