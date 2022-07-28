package it.csi.iuffi.iuffiweb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IGestioneCampioneEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneVisualEJB;
import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.business.IIspezioneVisivaPiantaEJB;
import it.csi.iuffi.iuffiweb.business.IShapeEJB;
import it.csi.iuffi.iuffiweb.geo.Feature;
import it.csi.iuffi.iuffiweb.geo.FeatureCollection;
import it.csi.iuffi.iuffiweb.geo.LineString;
import it.csi.iuffi.iuffiweb.geo.Point;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.GpsDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaPiantaDTO;
import it.csi.iuffi.iuffiweb.model.ShapeDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;
import it.csi.iuffi.iuffiweb.model.api.PointsApiRequest;
import it.csi.iuffi.iuffiweb.model.request.CampionamentoRequest;
import it.csi.iuffi.iuffiweb.model.request.IspezioneVisivaRequest;
import it.csi.iuffi.iuffiweb.model.request.TrappolaggioRequest;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "GEO", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GeoController extends TabelleController
{
  protected static final Logger        logger     = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  ResourceBundle res = ResourceBundle.getBundle("config");
  @Autowired
  private IGpsFotoEJB gpsFotoEJB;
  @Autowired
  private IGestioneVisualEJB ispezioneVisivaEJB;
  @Autowired
  private IGestioneCampioneEJB campioneEJB; 
  @Autowired
  private IGestioneTrappolaEJB trappoleEJB; 
  @Autowired
  private IIspezioneVisivaPiantaEJB ispVisivaPiantaEJB;
  @Autowired
  private IShapeEJB shapeEJB;
  
  /*  static List<GeoDTO> pojos = Arrays.asList(        
        new GeoDTO("1",44.91516924756185, 7.761974639608473, Type.PHOTO, "La prima foto"),
        new GeoDTO("2",44.91510086861215, 7.76477486561939, Type.PHOTO, "La seconda foto"),
        new GeoDTO("3",44.91392321837831, 7.764764136784097, Type.CAMPIONE, "primo campione"),
        new GeoDTO("4",44.912654368215094, 7.763712710925593, Type.CAMPIONE, "secondo campione"),
        new GeoDTO("5",44.91231245769104, 7.762114114467256, Type.TRAPPOLA, "trappola per merdone"),
        new GeoDTO("6",44.913072256093265, 7.7602151106207735, Type.TRAPPOLA, "trappola per scarrafoni")

      );

   */
  @RequestMapping(value = "/geo/comuni", produces = "application/json")
  public ResponseEntity<String> getComuni(HttpServletRequest request,HttpSession session) {
    try {
      ServletContext servletContext = request.getSession().getServletContext();
      String root = servletContext.getRealPath("/");
      String fileComuni = "comuni.json";
      logger.debug("/geo/comuni");
      String comuniFile = root + res.getString("conf.path") + File.separator + fileComuni;
      logger.debug("PATH FILE comuniPiemonte ::"+comuniFile);
      File file = new File(comuniFile);

      if(!file.exists()) {
        System.out.println("File not exists");
        comuniFile = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + fileComuni;
        file = new File(comuniFile);                      
        logger.info("Cerco in " + comuniFile);  

        if (!file.exists()) {
          logger.info("File dei comuni non trovato. File: " + comuniFile);
          ErrorResponse err = new ErrorResponse();
          err.addError("Errore", "File dei comuni non trovato. File: " + comuniFile);
          err.setMessage("Errore: file comuni.json non trovato");
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }           
        logger.debug("File not exists su /geo/comuni");
      }

      String comuniFileAsString = getFileAsString(file);
      return new ResponseEntity<>(comuniFileAsString,HttpStatus.OK);
    }
    catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  protected static String getFileAsString(File file) 
  {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) 
    {

      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) 
      {
        contentBuilder.append(sCurrentLine).append("\n");
      }
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
    return contentBuilder.toString();
  }

  private double round(double value) {
    return (double) Math.round(value * 100000d) / 100000d;
  }
  
  @RequestMapping(value = "/geo/points", produces = "application/json")
  public ResponseEntity<FeatureCollection> getPoints(HttpServletRequest request,HttpSession session,@RequestParam(value = "id") Long id,
      @RequestParam(value = "tipo") String tipo,@RequestParam(value = "master") String master) {
    logger.debug("/geo/points");
    List<FotoDTO> listaFoto;
    List<GpsDTO> listaGps;
    List<IspezioneVisivaDTO> listaVisual;
    List<CampionamentoDTO> listaCampioni;
    List<TrappolaggioDTO> listaTrappole;
    IspezioneVisivaPiantaDTO pianta;
    FotoDTO foto=null;
    GpsDTO gps=null;
    IspezioneVisivaRequest ispezione=null;
    CampionamentoRequest campioni=null;
    TrappolaggioRequest trappole = null;
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);

    //sezione gps
    if(master.equals("gps")) {
      logger.debug("/geo/points per funzione gps");
      if(id!=null && id>0) {
        gps= new GpsDTO();
        gps.setIdRecord(id.intValue());
        gps.setTipologia(tipo);
      } else {
        gps= (GpsDTO) session.getAttribute("gpsRequest");
      }
      //sezione visual
    } else if(master.equals("visual")) {
      logger.debug("/geo/points per funzione visual");
      if(id!=null && id>0) {
        ispezione= new IspezioneVisivaRequest();
        ispezione.setIdIspezione(id.intValue());
        //ispezione.setTipologia(tipo);          
      } else{
        ispezione= (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");   
      }
      //sezione campioni
    } else if(master.equals("campioni")) {
      logger.debug("/geo/points per funzione campioni");
      if(id!=null && id>0) {
        campioni= new CampionamentoRequest();
        campioni.setIdCampionamento(id.intValue());
        //ispezione.setTipologia(tipo);
      } else{
        campioni= (CampionamentoRequest) session.getAttribute("campionamentoRequest");   
      }          
      //sezione trappole
    } else if(master.equals("trappole")) {
      logger.debug("/geo/points per funzione trappole");
      if(id!=null && id>0) {
        trappole = new TrappolaggioRequest();
        trappole.setIdTrappolaggio(id.intValue());
        //ispezione.setTipologia(tipo);
      } else {
        trappole = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
      }          
      //sezione foto
    } else if (master.equals("foto")) {
      logger.debug("/geo/points per funzione foto");
      /*if(id!=null && id>0) {
        gps = new GpsDTO();
        gps.setIdRecord(id.intValue());
        gps.setTipologia(tipo);
      } else{
        gps= (GpsDTO) session.getAttribute("gpsRequest");
      }*/
      if(id!=null && id>0) {
        foto = new FotoDTO();
        foto.setIdRecord(id.intValue());
        foto.setTipologia(tipo);
        //foto = (FotoDTO) session.getAttribute("foto");
        foto.setIdFoto(id.intValue());
      }else {
        foto = (FotoDTO) session.getAttribute("foto");
      }
    }
    try {
      
      List<Feature> features = new ArrayList<Feature>();

      if(foto!=null) {
        listaFoto = gpsFotoEJB.findFotoByFilter(foto, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte(), "latitudine,longitudine");
        FotoDTO fotoPrec = null;
        double latitudine = 0;
        double longitudine = 0;
        int markersTot = 1;
        int markersR = 1;
        int markersL = 1;
        int offset = 2;
        for(FotoDTO pojo : listaFoto) {
          //for(GeoDTO pojo : pojos) {
          //pojos.forEach(p->{
          
          // inizio controllo per coordinate coincidenti
          latitudine = pojo.getLatitudine().doubleValue();
          longitudine = pojo.getLongitudine().doubleValue();
          if (fotoPrec != null && round(fotoPrec.getLatitudine().doubleValue()) == round(pojo.getLatitudine().doubleValue())
              && round(fotoPrec.getLongitudine().doubleValue()) == round(pojo.getLongitudine().doubleValue())) {
            if (markersTot % 2 == 1) {
              latitudine += Double.valueOf("0.0000"+markersR*offset);   // sposto il punto leggermente
              longitudine += Double.valueOf("0.0000"+markersR*offset);  // in alto a dx
              markersR++;
            }
            else {
              latitudine += Double.valueOf("0.0000"+markersL*offset);   // sposto il punto leggermente
              longitudine -= Double.valueOf("0.0000"+markersL*offset);  // in basso a sx
              markersL++;
            }
            markersTot++;
          }
          else
            markersTot = 1;
          // fine controllo per coordinate coincidenti

          Point point = new Point(latitudine, longitudine);
          fotoPrec = pojo;
          
          Map<String, Object> props = new HashMap<String,Object>();
          props.put("id", pojo.getIdFoto());
          if(pojo.getTipologia().equals("C")) {
            props.put("type", "CAMPIONE");
            props.put("description", pojo.getSpecie()+"-"+pojo.getCampione());
            try {
              CampionamentoDTO campione = campioneEJB.findById(pojo.getIdRecord());
              props.put("data", campione.getDataInizioF());
              if (campione.getComune() != null)
                props.put("comune", campione.getComune());
              else
                props.put("comune" , " comune non indicato ");
              props.put("specie", campione.getPianta());
              if (campione.getOrganismi() != null) 
                props.put("on", campione.getOrganismi());
              else 
                props.put("on", "on non indicati");
              props.put("tipoCampione", campione.getTipoCampione());
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          if(pojo.getTipologia().equals("T")) {
            props.put("type", "TRAPPOLA");
            props.put("description", pojo.getSpecie()+"-"+pojo.getTrappola());
            TrappolaggioRequest tr = new TrappolaggioRequest();
            tr.setIdTrappolaggio(pojo.getIdRecord());
            try {
              List<TrappolaggioDTO> list = trappoleEJB.findByFilterByTrappolaggioRequest(tr, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
              if (list != null && list.size() > 0) {
                TrappolaggioDTO trappolaggio = list.get(0);
                props.put("data", trappolaggio.getDataOraInizioF());
                if (trappolaggio.getDescrComune() != null)
                  props.put("comune", trappolaggio.getDescrComune());
                else
                  props.put("comune" , " Comune non indicato ");
                props.put("pianta", (trappolaggio.getPianta()!=null)?trappolaggio.getPianta():" Pianta non indicata ");
                if(trappolaggio.getNomeLatino() != null)
                  props.put("on", trappolaggio.getNomeLatino());
                else 
                  props.put("on", "ON non indicati");
                props.put("tipoTrappola", trappolaggio.getDescrTrappola());
                props.put("codiceTrappola", trappolaggio.getCodiceSfr());
              } else {
                props.put("data", "non disponibile");
                props.put("comune" , " Comune non indicato ");
                props.put("pianta", "");
                props.put("on", "ON non indicati");
                props.put("tipoTrappola", "Tipo trappola non disponibile");
                props.put("codiceTrappola", "Codice trappola non disponibile");
              }
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
          if(pojo.getTipologia().equals("I")) {
            props.put("type", "ISPEZIONE VISIVA");
            props.put("description", pojo.getSpecie());
            try {
              IspezioneVisivaDTO visual = ispezioneVisivaEJB.findById(pojo.getIdRecord());
              if (visual !=null) {
                props.put("data", visual.getDataInizio());
                if (visual.getComune() != null)
                  props.put("comune", visual.getComune());
                else
                  props.put("comune" , " Comune non indicato ");
                props.put("area", visual.getDettaglioArea());
                if(visual.getPianta() != null)
                  props.put("description", visual.getPianta());
                else if(visual.getNomeVolgareSpecie() != null)
                  props.put("description", visual.getNomeVolgareSpecie());
                else
                  props.put("description", " Pianta non indicata ");
                if(visual.getOnIspezionati() != null) 
                  props.put("on", visual.getOnIspezionati());
                else 
                  props.put("on", "ON non indicati");

                Integer numCampionamenti = ispezioneVisivaEJB.findCampionamentiCountByVisual(visual.getIdIspezione());
                String campionamentiPresenti = "Presenti " + numCampionamenti + " campionamenti";
                props.put("campionamento", campionamentiPresenti);
                Integer numTrappolaggi = ispezioneVisivaEJB.findTrappolaggiCountByVisual(visual.getIdIspezione());
                String  trappolaggiPresenti = "Presenti " + numTrappolaggi + " trappolaggi";
                props.put("trappolaggio", trappolaggiPresenti);

              }
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
          Feature feature = new Feature(point, props);
          features.add(feature);
        }
      }
      else
        if (gps!=null) {
          listaGps = gpsFotoEJB.findGpsByFilter(gps, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte(), "latitudine,longitudine");
          
          GpsDTO gpsPrec = null;
          double latitudine = 0;
          double longitudine = 0;
          int markersTot = 1;
          int markersR = 1;
          int markersL = 1;
          int offset = 2;

          for (GpsDTO pojo : listaGps) {
            //for(GeoDTO pojo : pojos) {
            //pojos.forEach(p->{
            
            
            // inizio controllo per coordinate coincidenti
            latitudine = pojo.getLatitudine().doubleValue();
            longitudine = pojo.getLongitudine().doubleValue();
            if (gpsPrec != null && round(gpsPrec.getLatitudine().doubleValue()) == round(pojo.getLatitudine().doubleValue())
                && round(gpsPrec.getLongitudine().doubleValue()) == round(pojo.getLongitudine().doubleValue())) {
              if (markersTot % 2 == 1) {
                latitudine += Double.valueOf("0.0000"+markersR*offset);   // sposto il punto leggermente
                longitudine += Double.valueOf("0.0000"+markersR*offset);  // in alto a dx
                markersR++;
              }
              else {
                latitudine -= Double.valueOf("0.0000"+markersL*offset);   // sposto il punto leggermente
                longitudine -= Double.valueOf("0.0000"+markersL*offset);  // in basso a sx
                markersL++;
              }
              markersTot++;
            }
            else
              markersTot = 1;
            // fine controllo per coordinate coincidenti

            Point point = new Point(latitudine, longitudine);
            gpsPrec = pojo;
            
            Map<String, Object> props = new HashMap<String,Object>();
            props.put("id", pojo.getIdRilevazione());
            if (pojo.getTipologia().equals("C")) {
              props.put("type", "CAMPIONE");
              props.put("description", pojo.getSpecie()+"-"+pojo.getCampione());

              try {
                CampionamentoDTO campione = campioneEJB.findById(pojo.getIdRecord());
                props.put("data", campione.getDataInizioF());
                if (campione.getComune() != null)
                  props.put("comune", campione.getComune());
                else
                  props.put("comune" , " comune non indicato ");
                props.put("specie", (campione.getPianta()!=null)?campione.getPianta():" Pianta non indicata ");
                if (campione.getOrganismi() != null) 
                  props.put("on", campione.getOrganismi());
                else 
                  props.put("on", "on non indicati");
                props.put("tipoCampione", campione.getTipoCampione());
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            if (pojo.getTipologia().equals("T")) {
              props.put("type", "TRAPPOLA");
              props.put("description", pojo.getSpecie()+"-"+pojo.getTrappola());

              TrappolaggioRequest tr = new TrappolaggioRequest();
              tr.setIdTrappolaggio(pojo.getIdRecord());
              try {
                List<TrappolaggioDTO> list = trappoleEJB.findByFilterByTrappolaggioRequest(tr, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
                if (list != null && list.size() > 0) {
                  TrappolaggioDTO trappolaggio = list.get(0);
                  props.put("data", trappolaggio.getDataOraInizioF());
                  if (trappolaggio.getDescrComune() != null)
                    props.put("comune", trappolaggio.getDescrComune());
                  else
                    props.put("comune" , " Comune non indicato ");
                  props.put("pianta", (trappolaggio.getPianta()!=null)?trappolaggio.getPianta():" Pianta non indicata ");
                  if(trappolaggio.getNomeLatino() != null)
                    props.put("on", trappolaggio.getNomeLatino());
                  else 
                    props.put("on", "ON non indicati");
                  props.put("tipoTrappola", trappolaggio.getDescrTrappola());
                  props.put("codiceTrappola", trappolaggio.getCodiceSfr());
                } else {
                  props.put("data", "non disponibile");
                  props.put("comune" , " Comune non indicato ");
                  props.put("pianta", "");
                  props.put("on", "ON non indicati");
                  props.put("tipoTrappola", "Tipo trappola non disponibile");
                  props.put("codiceTrappola", "Codice trappola non disponibile");
                }
              }
              catch (Exception e) {
                e.printStackTrace();
              }
            }
            if(pojo.getTipologia().equals("I")) {
              props.put("type", "ISPEZIONE VISIVA");
              props.put("description", pojo.getSpecie());
              try {
                IspezioneVisivaDTO visual = ispezioneVisivaEJB.findById(pojo.getIdRecord());
                if (visual !=null) {
                  props.put("data", visual.getDataInizio());
                  if (visual.getComune() != null)
                    props.put("comune", visual.getComune());
                  else
                    props.put("comune" , " Comune non indicato ");
                  props.put("area", visual.getDettaglioArea());
                  if(visual.getPianta() != null)
                    props.put("description", visual.getPianta());
                  else if(visual.getNomeVolgareSpecie() != null)
                    props.put("description", visual.getNomeVolgareSpecie());
                  else
                    props.put("description", " Pianta non indicata ");
                  if(visual.getOnIspezionati() != null) 
                    props.put("on", visual.getOnIspezionati());
                  else 
                    props.put("on", "ON non indicati");

                  Integer numCampionamenti = ispezioneVisivaEJB.findCampionamentiCountByVisual(visual.getIdIspezione());
                  String campionamentiPresenti = "Presenti " + numCampionamenti + " campionamenti";
                  props.put("campionamento", campionamentiPresenti);
                  Integer numTrappolaggi = ispezioneVisivaEJB.findTrappolaggiCountByVisual(visual.getIdIspezione());
                  String  trappolaggiPresenti = "Presenti " + numTrappolaggi + " trappolaggi";
                  props.put("trappolaggio", trappolaggiPresenti);

                }
              }
              catch (Exception e) {
                e.printStackTrace();
              }
            }
            Feature feature = new Feature(point, props);
            features.add(feature);
          }
        }
        else
          if(ispezione!=null) {
            //data OK, comune OK, specie OK, ON, presenza campionamento e presenza trappolaggio
            try {
              listaVisual = ispezioneVisivaEJB.findByFilterFromRequest(ispezione, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
              if(listaVisual!=null && !listaVisual.isEmpty() && listaVisual.get(0) != null) {
                for(IspezioneVisivaDTO pojo : listaVisual) {
                  Point point = new Point(pojo.getLatitudine(), pojo.getLongitudine());
                  Map<String, Object> props = new HashMap<String,Object>();
                  props.put("id", pojo.getIdRilevazione());
                  props.put("type", "ISPEZIONE VISIVA");
                  props.put("data", pojo.getDataInizio());
                  if(pojo.getComune() != null)
                    props.put("comune", pojo.getComune());
                  else
                    props.put("comune" , " Comune non indicato ");
                  props.put("area", pojo.getDettaglioArea());
                  if(pojo.getPianta() != null)
                    props.put("description", pojo.getPianta());
                  else if(pojo.getNomeVolgareSpecie() != null)
                    props.put("description", pojo.getNomeVolgareSpecie());
                  else
                    props.put("description", " Pianta non indicata ");
                  if(pojo.getOnIspezionati() != null) 
                    props.put("on", pojo.getOnIspezionati());
                  else 
                    props.put("on", "ON non indicati");

                  Integer numCampionamenti = ispezioneVisivaEJB.findCampionamentiCountByVisual(pojo.getIdIspezione());
                  String  campionamentiPresenti = "Presenti " + numCampionamenti + " campionamenti";
                  props.put("campionamento", campionamentiPresenti);
                  Integer numTrappolaggi = ispezioneVisivaEJB.findTrappolaggiCountByVisual(pojo.getIdIspezione());
                  String  trappolaggiPresenti = "Presenti " + numTrappolaggi + " trappolaggi";
                  props.put("trappolaggio", trappolaggiPresenti);

                  Feature feature = new Feature(point, props);
                  features.add(feature);
                }
              }
              else {
                pianta = ispVisivaPiantaEJB.findById(ispezione.getIdIspezione());
                Point point = new Point(pianta.getLatitudine(), pianta.getLongitudine());
                Map<String, Object> props = new HashMap<String,Object>();
                props.put("type", "PIANTA");
                props.put("description", pianta.getSpecie());
                Feature feature = new Feature(point, props);
                features.add(feature);
              }
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
          else
            if(campioni!=null) {  //data, comune, specie, on, tipo campione
              try {
                listaCampioni = campioneEJB.findByFilter(campioni, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
                for(CampionamentoDTO pojo : listaCampioni) {
                  Point point = new Point(pojo.getLatitudine(), pojo.getLongitudine());
                  Map<String, Object> props = new HashMap<String,Object>();
                  props.put("id", pojo.getIdCampionamento());
                  props.put("type", "CAMPIONE");
                  props.put("description", pojo.getSpecie());
                  props.put("data", pojo.getDataInizioF());
                  if(pojo.getComune() != null)
                    props.put("comune", pojo.getComune());
                  else
                    props.put("comune" , " comune non indicato ");
                  props.put("specie", pojo.getSpecie());
                  if(pojo.getOrganismi() != null) 
                    props.put("on", pojo.getOrganismi());
                  else 
                    props.put("on", "on non indicati");
                  props.put("tipoCampione", pojo.getTipoCampione());
                  Feature feature = new Feature(point, props);
                  features.add(feature);
                }
              }
              catch (Exception e) {
                e.printStackTrace();
              }
            }
            else
              if (trappole != null) {
                try {
                  listaTrappole = trappoleEJB.findByFilterByTrappolaggioRequest(trappole, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
                  for (TrappolaggioDTO pojo : listaTrappole) {
                    TrappolaDTO trappola = trappoleEJB.findTrappolaById(pojo.getIdTrappola());
                    Point point = new Point(trappola.getLatitudine(), trappola.getLongitudine());
                    Map<String, Object> props = new HashMap<String,Object>();
                    props.put("id", pojo.getIdTrappola());
                    //TRAPPOLA - data - comune - pianta- ON - tipo trappola - codice trappola
                    props.put("type", "TRAPPOLA");
                    props.put("data", pojo.getDataOraInizioF());
                    if(pojo.getDescrComune() != null)
                      props.put("comune", pojo.getDescrComune());
                    else
                      props.put("comune" , " Comune non indicato ");
                    props.put("pianta", (pojo.getPianta()!=null)?pojo.getPianta():" Pianta non indicata ");
                    if(pojo.getNomeLatino() != null)
                      props.put("on", pojo.getNomeLatino());
                    else 
                      props.put("on", "ON non indicati");
                    props.put("tipoTrappola", pojo.getDescrTrappola());
                    props.put("codiceTrappola", pojo.getCodiceSfr());
                    Feature feature = new Feature(point, props);
                    features.add(feature);
                  }
                }
                catch (Exception e) {
                  e.printStackTrace();
                }
              }

      FeatureCollection geojson = new FeatureCollection(features);
      return new ResponseEntity<>(geojson,HttpStatus.OK);
    }
    catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ResponseBody
  @RequestMapping(value = "/rest/geopoints", consumes = { "application/json" }, produces = { "application/json" }, method = RequestMethod.POST)
  public ResponseEntity<?> recuperaPunti(@Valid @RequestBody PointsApiRequest body, HttpServletRequest request) {
    try {
      List<Feature> features = new ArrayList<Feature>();
      
      if(body.getTipo() != null && (!body.getTipo().trim().toUpperCase().matches("|V|C|T"))) {
        logger.debug("Attributo tipo non valido: "+body.getTipo());
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", "Errore attributo tipo non valido: "+body.getTipo());
        err.setMessage("Attributo tipo non valido: "+body.getTipo());
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.BAD_REQUEST);
      }
      
      List<ShapeDTO> list = shapeEJB.findByType(body.getOrganismi(), body.getTipo(), "POINTS");
      if(list != null) {
        for(ShapeDTO shapeDTO : list) {
          //per ogni figuri creo il punto leggendo le cordinate dei details
          if(shapeDTO.getDetails() != null) {
            Coordinate c = shapeDTO.getDetails().get(0);

            Point point = new Point(c.getLatitudine(), c.getLongitudine());
            Map<String, Object> props = new HashMap<String,Object>();
            props.put("on", shapeDTO.getIdOrganismoNocivo());
            if(shapeDTO.getIdTipoAttivita() != null) {
              if(shapeDTO.getIdTipoAttivita().intValue() == 1) {
                props.put("type", "V");
                props.put("n_visual", shapeDTO.getnVisual());
                props.put("comune", shapeDTO.getComune());
                props.put("note", shapeDTO.getNote());
              }else if(shapeDTO.getIdTipoAttivita().intValue() == 2) {
                props.put("type", "C");
                props.put("id_campione", shapeDTO.getCampionamento());
                props.put("comune", shapeDTO.getComune());
                props.put("note", shapeDTO.getNote());
              }else if(shapeDTO.getIdTipoAttivita().intValue() == 3) {
                props.put("type", "T");
                props.put("id_trappola", shapeDTO.getTrappola());
                props.put("cod_tipo_trappola", shapeDTO.getCodTipoTrappola());
                props.put("desc_tipo_trappola", shapeDTO.getDescTipoTrappola());
                props.put("comune", shapeDTO.getComune());
                props.put("note", shapeDTO.getNote());
              }
            }
            Feature feature = new Feature(point, props);
            features.add(feature);
          }
        }
      }

      FeatureCollection geojson = new FeatureCollection(features);
      return new ResponseEntity<>(geojson,HttpStatus.OK);
    }
    catch (Exception e) {
      logger.debug("Errore nel metodo recuperaPunti durante il recupero dati: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo recuperaPunti durante il recupero dati: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @ResponseBody
  @RequestMapping(value = "/rest/geoareas", consumes = { "application/json" }, produces = { "application/json" }, method = RequestMethod.POST)
  public ResponseEntity<?> recuperaAree(@Valid @RequestBody PointsApiRequest body, HttpServletRequest request) {
    try {
      List<Feature> features = new ArrayList<Feature>();
      
      List<ShapeDTO> list = shapeEJB.findByType(body.getOrganismi(), null, "AREA");
      if(list != null) {
        for(ShapeDTO shapeDTO : list) {
          //per ogni figuri creo il polygon leggendo le cordinate dei details
          if(shapeDTO.getDetails() != null) {
            List<Point> points = new ArrayList();
            for(Coordinate c : shapeDTO.getDetails()) {
              Point point = new Point(c.getLatitudine(), c.getLongitudine());
              points.add(point);
            }
            LineString polygon = new LineString(points);
            
            Map<String, Object> props = new HashMap<String,Object>();
            props.put("on", shapeDTO.getIdOrganismoNocivo());
            props.put("quadrante", shapeDTO.getQuadrante());
            Feature feature = new Feature(polygon, props);
            features.add(feature);
          }
        }
      }
      FeatureCollection geojson = new FeatureCollection(features);
      return new ResponseEntity<>(geojson,HttpStatus.OK);
    }
    catch (Exception e) {
      logger.debug("Errore nel metodo recuperaAree durante il recupero dati: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo recuperaAree durante il recupero dati: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
//  @RequestMapping(value = "/geo/pointstodo", produces = "application/json")
//  public ResponseEntity<FeatureCollection> getPointsToDo(HttpServletRequest request,HttpSession session,@RequestParam(value = "id") Long id,
//      @RequestParam(value = "tipo") String tipo,@RequestParam(value = "master") String master) {
//    try {
//      List<Feature> features = new ArrayList<Feature>();
//      
//      List<ShapeDTO> list = shapeEJB.findByType("POINTS");
//      if(list != null) {
//        for(ShapeDTO shapeDTO : list) {
//          //per ogni figuri creo il punto leggendo le cordinate dei details
//          if(shapeDTO.getDetails() != null) {
//            Coordinate c = shapeDTO.getDetails().get(0);
//
//            Point point = new Point(c.getLatitudine(), c.getLongitudine());
//            Map<String, Object> props = new HashMap<String,Object>();
//            props.put("type", "POINTS TO DO");
//            Feature feature = new Feature(point, props);
//            features.add(feature);
//          }
//        }
//      }
//
//      FeatureCollection geojson = new FeatureCollection(features);
//      return new ResponseEntity<>(geojson,HttpStatus.OK);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

  
//  @RequestMapping(value = "/geo/aree", produces = "application/json")
//  public ResponseEntity<FeatureCollection> getAree(HttpServletRequest request,HttpSession session,@RequestParam(value = "id") Long id,
//      @RequestParam(value = "tipo") String tipo,@RequestParam(value = "master") String master) {
//    try {
//      List<Feature> features = new ArrayList<Feature>();
//      
//      List<ShapeDTO> list = shapeEJB.findByType("AREA");
//      if(list != null) {
//        for(ShapeDTO shapeDTO : list) {
//          //per ogni figuri creo il polygon leggendo le cordinate dei details
//          if(shapeDTO.getDetails() != null) {
//            List<Point> points = new ArrayList();
//            for(Coordinate c : shapeDTO.getDetails()) {
//              Point point = new Point(c.getLatitudine(), c.getLongitudine());
//              points.add(point);
//            }
//            LineString polygon = new LineString(points);
//            
//            Map<String, Object> props = new HashMap<String,Object>();
//            //aggiungere eventualmente altre proprietà da mostrare sull'onClick
//            props.put("type", "AREE");
//            Feature feature = new Feature(polygon, props);
//            features.add(feature);
//          }
//        }
//      }
//
//      FeatureCollection geojson = new FeatureCollection(features);
//      return new ResponseEntity<>(geojson,HttpStatus.OK);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

  /* @RequestMapping(value = "/geo/table", produces = "application/json")
    public ResponseEntity<List<GeoDTO>> getTable() {
          return new ResponseEntity<>(pojos,HttpStatus.OK);

    }*/
}
