package it.csi.iuffi.iuffiweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.JTS;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IShapeEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.ShapeDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "GPS", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class ShapeController extends TabelleController
{

  protected static final Logger        logger     = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");

  ResourceBundle res = ResourceBundle.getBundle("config");
  
  @Autowired
  private IShapeEJB shapeEJB;

  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;


  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }


  @RequestMapping(value = "/shape/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByPianificazione();
    model.addAttribute("listaOn", listaOn);
    setBreadcrumbs(model, request);
    return "gestioneGpsFoto/ricercaShape";
  }

  @RequestMapping(value = "/shape/uploadFile", method = RequestMethod.POST)
  public String upload(@RequestParam("shapeFile") MultipartFile shapeFile, @RequestParam("idOrganismoNocivo") Integer idOrganismoNocivo, Model model,
      HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException {
    
      Errors errors = new Errors();
      String success = null;
      OutputStream os = null;
      OutputStream osZip = null;
      GeoPackage geo = null;
      DataStore datastore = null;
      FileDataStore store = null;
      UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      String tempFile = null;
      File file = null;
      File zipFile = null;
      
      try {
        
        if (idOrganismoNocivo == null || idOrganismoNocivo < 1) {
          logger.error("Organismo nocivo non valido. Id = " + idOrganismoNocivo);
          model.addAttribute("error", "Organismo nocivo non inserito");
          errors.addError("shapeFile", "Organismo nocivo non inserito");
          this.showFilter(model, session, request, response);
          return "gestioneGpsFoto/ricercaShape";
        }

        if (shapeFile == null || shapeFile.isEmpty()) {
          logger.error("Shape file non inserito");
          model.addAttribute("error", "Shape file non inserito");
          errors.addError("shapeFile", "Shape file non inserito");
          this.showFilter(model, session, request, response);
          return "gestioneGpsFoto/ricercaShape";
        }
        
        ServletContext servletContext = request.getSession().getServletContext();
        String root = servletContext.getRealPath("/");
        String serverPath = root + res.getString("conf.path") + File.separator + res.getString("application.env");
        
        File directory = new File(serverPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        boolean shp = false;
        boolean shx = false;
        boolean dbf = false;

        if (shapeFile.getOriginalFilename().toLowerCase().endsWith(".zip")) {   // file zip (deve contenere 3 file: il .shp + .shx + .dbf)
          tempFile = serverPath + File.separator + "temp_file.zip";
          zipFile = new File(tempFile);
          osZip = new FileOutputStream(zipFile);
          osZip.write(shapeFile.getBytes());
          
          String fileZip = tempFile;
          File destDir = new File(serverPath + File.separator + "unzipDir");
          byte[] buffer = new byte[1024];
          ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
          ZipEntry zipEntry = zis.getNextEntry();
          while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                //
                if (zipEntry.getName().toLowerCase().endsWith(".shp")) {
                  tempFile = destDir.getAbsolutePath() + File.separator + "tempfile.shp";
                  shp = true;
                }
                if (zipEntry.getName().toLowerCase().endsWith(".shx")) {
                  shx = true;
                }
                if (zipEntry.getName().toLowerCase().endsWith(".dbf")) {
                  dbf = true;
                }
                //
                fos.close();
                //zis.closeEntry();
            }
            zipEntry = zis.getNextEntry();
          }
          osZip.close();
          zis.close();
          if (!shp || !shx || !dbf) {
            logger.error("Il file zip deve contenere un file .shp, un file .shx ed un file .dbf");
            model.addAttribute("error", "Il file zip deve contenere un file .shp, un file .shx ed un file .dbf");
            errors.addError("shapeFile", "Il file zip deve contenere un file .shp, un file .shx ed un file .dbf");
            this.showFilter(model, session, request, response);
            return "gestioneGpsFoto/ricercaShape";
          }
          file = new File(tempFile);
        }
        else
        if (shapeFile.getOriginalFilename().toLowerCase().endsWith(".gpkg")) {
          // Geo Package
          tempFile = serverPath += File.separator + "tempfile.gpkg";
          file = new File(tempFile);
          os = new FileOutputStream(file);
          os.write(shapeFile.getBytes());
        }
        else
        {
          logger.error("File non di tipo zip o GeoPackage");
          model.addAttribute("error", "File non di tipo zip o GeoPackage");
          model.addAttribute("success", null);
          errors.addError("shapeFile", "File non di tipo zip o GeoPackage");
          this.showFilter(model, session, request, response);
          return "gestioneGpsFoto/ricercaShape";
        }

        if(!file.exists()) {
          logger.error("Errore di scrittura del file " + tempFile);
          model.addAttribute("error", "Errore di scrittura del file " + tempFile);
          model.addAttribute("success", null);
          errors.addError("shapeFile", "Errore di scrittura del file " + tempFile);
          this.showFilter(model, session, request, response);
          return "gestioneGpsFoto/ricercaShape";
        }
        
//        file.setReadOnly();

        //shapeEJB.deleteByOn(idOrganismoNocivo);   // Elimino i record relativi all'organismo nocivo da inserire
        
        int entries = 0;
        int skipped = 0;
        int wrongs = 0;
        boolean visualDeleted = false;
        boolean campioniDeleted = false;
        boolean trappoleDeleted = false;

        if (file.getName().toLowerCase().endsWith(".gpkg")) {   // File GeoPackage
          geo = new GeoPackage(file);
          geo.init();
          Map<String, String> params = new HashMap<String, String>();
          params.put("dbtype", "geopkg");
          params.put("database", tempFile);
          datastore = DataStoreFinder.getDataStore(params);
          
          String[] names = datastore.getTypeNames();

          for (String name : names) {
            logger.debug(name);
            SimpleFeatureCollection geoFeatures = datastore.getFeatureSource(name).getFeatures();

            try (SimpleFeatureIterator it = geoFeatures.features()) {

              while (it.hasNext()) {
                SimpleFeature f = it.next();
                Geometry g = (Geometry) f.getDefaultGeometry(); //"org.locationtech.jts.geom.Geometry", NOT "org.opengis.geometry.Geometry"!
                
                Coordinate[] coordinates = g.getCoordinates();
                boolean isReticolo = (coordinates.length == 5);
                List<it.csi.iuffi.iuffiweb.model.api.Coordinate> details = new ArrayList<it.csi.iuffi.iuffiweb.model.api.Coordinate>();
                for(Coordinate c : coordinates) {
                //sourceCRS is to convert the X and Y coordinates to lat long
                  CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:25832");
                  CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
                  MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
                  Coordinate targetGeometry = JTS.transform(c, c, transform);    
                  details.add(new it.csi.iuffi.iuffiweb.model.api.Coordinate(targetGeometry.getX(), targetGeometry.getY()));
                }
                // Preparo il dto e lo inserisco
                ShapeDTO shape = new ShapeDTO();
                shape.setDescrizione(f.getID());
                shape.setIdOrganismoNocivo(idOrganismoNocivo);
                boolean skip = false;

                try {

                  long idTipoAttivita = 0;
                  
                  if (isReticolo) {
                    try {
                      Long quadrante = (f.getAttribute("id")!=null)?Long.decode(f.getAttribute("id").toString()):null;
                      shape.setQuadrante(quadrante);
                    } catch (Exception e) {
                        logger.error("Attributo quadrante non valido: " + f.getAttribute("id"));
                        shape.setQuadrante(null);
                    }
                  }
                  else
                  {
                    switch (f.getAttribute("intervento").toString().trim().toUpperCase()) {
                      case "V":
                        idTipoAttivita = 1;
                        if (!visualDeleted) {
                          shapeEJB.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
                          visualDeleted = true;
                        }
                        shape.setnVisual((f.getAttribute("n_visual")!=null)?f.getAttribute("n_visual").toString().trim():"");
                        shape.setComune((f.getAttribute("comune")!=null)?f.getAttribute("comune").toString().trim():"");
                        shape.setNote((f.getAttribute("note")!=null)?f.getAttribute("note").toString().trim():"");
                        break;
                      case "C":
                        idTipoAttivita = 2;
                        if (!campioniDeleted) {
                          shapeEJB.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
                          campioniDeleted = true;
                        }
                        shape.setCampionamento((f.getAttribute("id_camp")!=null)?f.getAttribute("id_camp").toString().trim():"");
                        shape.setComune((f.getAttribute("comune")!=null)?f.getAttribute("comune").toString().trim():"");
                        shape.setNote((f.getAttribute("note")!=null)?f.getAttribute("note").toString().trim():"");
                        break;
                      case "T":
                        idTipoAttivita = 3;
                        if (!trappoleDeleted) {
                          shapeEJB.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
                          trappoleDeleted = true;
                        }
                        shape.setTrappola((f.getAttribute("id_trap")!=null)?f.getAttribute("id_trap").toString().trim():"");
                        shape.setCodTipoTrappola((f.getAttribute("tipo_tp")!=null)?f.getAttribute("tipo_tp").toString().trim():"");
                        shape.setDescTipoTrappola((f.getAttribute("tipo_desc")!=null)?f.getAttribute("tipo_desc").toString().trim():"");
                        shape.setComune((f.getAttribute("comune")!=null)?f.getAttribute("comune").toString().trim():"");
                        shape.setNote((f.getAttribute("note")!=null)?f.getAttribute("note").toString().trim():"");
                        break;
                      default:
                        if (f.getAttribute("intervento").toString().trim().length() > 0) {
                          skip = true;
                          skipped++;
                        }
                    }
                  }
                  if (!skip) {
                    shape.setIdTipoAttivita(idTipoAttivita);
                    shape.setDetails(details);
                    shape.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
                    shapeEJB.insert(shape);
                    entries++;
                  }
                } catch (Exception e) {
                    logger.error("Impossibile caricare il record: " + f.getAttributes());
                    logger.error("Errore: " + e.getMessage());
                    wrongs++;
                }
              }   // end while features
            }   // end try features
          }   // end for names
        }
        else  // *********************** File .shp ***********************
        {
          //file = new File(tempFile);
          try {
            store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            store.dispose();
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureIterator it = featureCollection.features();

            while (it.hasNext()) {
              SimpleFeature f = it.next();
              Geometry g = (Geometry) f.getDefaultGeometry(); //"org.locationtech.jts.geom.Geometry", NOT "org.opengis.geometry.Geometry"!
              
              Coordinate[] coordinates = g.getCoordinates();
              boolean isReticolo = (coordinates.length == 5);
              List<it.csi.iuffi.iuffiweb.model.api.Coordinate> details = new ArrayList<it.csi.iuffi.iuffiweb.model.api.Coordinate>();
              for(Coordinate c : coordinates) {
              //sourceCRS is to convert the X and Y coordinates to lat long
                CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:25832");
                CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
                MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
                Coordinate targetGeometry = JTS.transform(c, c, transform);
                details.add(new it.csi.iuffi.iuffiweb.model.api.Coordinate(targetGeometry.getX(), targetGeometry.getY()));
              }

              // Preparo il dto e lo inserisco
              ShapeDTO shape = new ShapeDTO();
              shape.setDescrizione(f.getID());
              shape.setIdOrganismoNocivo(idOrganismoNocivo);
              boolean skip = false;
              try {
                
                Long idTipoAttivita = null;
                
                if (isReticolo) {
                  try {
                    Long quadrante = (f.getAttribute("id")!=null)?Long.decode(f.getAttribute("id").toString()):null;
                    shape.setQuadrante(quadrante);
                  } catch (Exception e) {
                      logger.error("Attributo quadrante non valido: " + f.getAttribute("id"));
                      shape.setQuadrante(null);
                  }
                }
                else
                {
                  switch (f.getAttribute("intervento").toString().trim().toUpperCase()) {
                    
                    case "V":
                      idTipoAttivita = 1L;
                      if (!visualDeleted) {
                        shapeEJB.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
                        visualDeleted = true;
                      }
                      shape.setnVisual((f.getAttribute("n_visual")!=null)?f.getAttribute("n_visual").toString().trim():"");
                      shape.setComune((f.getAttribute("comune")!=null)?f.getAttribute("comune").toString().trim():"");
                      shape.setNote((f.getAttribute("note")!=null)?f.getAttribute("note").toString().trim():"");
                      break;
                      
                    case "C":
                      idTipoAttivita = 2L;
                      if (!campioniDeleted) {
                        shapeEJB.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
                        campioniDeleted = true;
                      }
                      shape.setCampionamento((f.getAttribute("id_camp")!=null)?f.getAttribute("id_camp").toString().trim():"");
                      shape.setComune((f.getAttribute("comune")!=null)?f.getAttribute("comune").toString().trim():"");
                      shape.setNote((f.getAttribute("note")!=null)?f.getAttribute("note").toString().trim():"");
                      break;
                      
                    case "T":
                      idTipoAttivita = 3L;
                      if (!trappoleDeleted) {
                        shapeEJB.deleteByOnAndTipoAttivita(idOrganismoNocivo, idTipoAttivita);
                        trappoleDeleted = true;
                      }
                      shape.setTrappola((f.getAttribute("id_trap")!=null)?f.getAttribute("id_trap").toString().trim():"");
                      shape.setCodTipoTrappola((f.getAttribute("tipo_tp")!=null)?f.getAttribute("tipo_tp").toString().trim():"");
                      shape.setDescTipoTrappola((f.getAttribute("tipo_desc")!=null)?f.getAttribute("tipo_desc").toString().trim():"");
                      shape.setComune((f.getAttribute("comune")!=null)?f.getAttribute("comune").toString().trim():"");
                      shape.setNote((f.getAttribute("note")!=null)?f.getAttribute("note").toString().trim():"");
                      break;
                      
                    default:
                      if (f.getAttribute("intervento").toString().trim().length() > 0) {
                        skip = true;
                        skipped++;
                      }
                  }
                }
                if (!skip) {
                  shape.setIdTipoAttivita(idTipoAttivita);
                  shape.setDetails(details);
                  shape.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
                  shapeEJB.insert(shape);
                  entries++;
                }
              } catch (Exception e) {
                  logger.error("Impossibile caricare il record: " + f.getAttributes());
                  logger.error("Errore: " + e.getMessage());
                  wrongs++;
              }
            }   // end while features
          }
          catch (Exception e) {
            e.printStackTrace();
            logger.error("Errore nell'upload del file: " + e.getMessage());
            errors.addError("shapeFile", "Errore nell'upload del file: " + e.getMessage());
            model.addAttribute("error", "Errore nell'upload del file: " + e.getMessage());
            model.addAttribute("success", null);
            this.showFilter(model, session, request, response);
            return "gestioneGpsFoto/ricercaShape";
          }
        }
        
        logger.info("record caricati: " + entries);
        logger.info("record scartati: " + skipped);
        logger.info("record in errore: " + wrongs);

        if (entries == 0) {
          errors.addError("shapeFile", "Shape file non importato. Record scartati: " + skipped + " - Record in errore: " + wrongs);
          model.addAttribute("error", "Shape file non importato. Record scartati: " + skipped + " - Record in errore: " + wrongs);
          model.addAttribute("success", null);
        }
        else
        if (entries > 0 && skipped == 0 && wrongs == 0) {
          errors = null;
          success = "Shape file importato correttamente";
        }
        else
        {
          errors.addError("shapeFile", "Shape file importato parzialmente. Record caricati: " + entries + " - Record scartati: " + skipped + " - Record in errore: " + wrongs);
          model.addAttribute("error", "Shape file non importato. Record scartati: " + skipped + " - Record in errore: " + wrongs);
          model.addAttribute("success", null);
        }
      }
      catch (Exception e) {
        logger.error("Errore nell'upload del file: " + e.getMessage());
        errors.addError("shapeFile", "Errore nell'upload del file: " + e.getMessage());
        model.addAttribute("error", "Errore nell'upload del file: " + e.getMessage());
        model.addAttribute("success", null);
        this.showFilter(model, session, request, response);
        return "gestioneGpsFoto/ricercaShape";
      }
      finally {
        if (datastore != null) {
          datastore.dispose();
        }
        if (geo != null) {
          geo.close();
        }
        if (os != null) {
          try
          {
            os.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
      model.addAttribute("success", success);
      this.showFilter(model, session, request, response);
      return "gestioneGpsFoto/ricercaShape";
  }

  private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
    
    File destFile = new File(destinationDir, "tempfile." + FilenameUtils.getExtension(zipEntry.getName()));
    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
    }

    return destFile;
  }

}
