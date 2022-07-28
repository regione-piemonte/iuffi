package it.csi.iuffi.iuffiweb.model;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.integration.CustomMultipartFile;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;


public class FotoDTO extends TabelleDTO implements ILoggable
{

	private static final long serialVersionUID = 1L;
	
	private Integer idFoto;
	private MultipartFile foto;
	private byte[] fotoByte;
  private String nomeFile;
  private Date dataFoto;
  private String tag;
  private String note;
  private String base64;
  //campi ricerca
  private Integer anno;
  private Date dallaData;
  private Date allaData;
  private Boolean visual;
  private Boolean trappolaggio;
  private Boolean campionamento;
  private Integer numeroTrasferta;
  private String tipologia;
  private List<Integer> tipoArea;
  private List<Integer> ispettoreAssegnato;
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  private List<Integer> trappole;
  private List<Integer> campioni;

  private String specie;
  private String area;
  private String trappola;
  private String campione;
  private Double longitudine;
  private Double latitudine;
  private String ispettoreEvento;
  private String ispettoreAssegnatoM;
  private String descrComune;
  private String organismi;
  private Integer idVisual;
  private Integer idCampionamento;
  private Integer idTrappolaggio;
  private Date dataAcquisizione;  
  private String istatComune;
  private String descComune;
  private String comune;
  private Integer idRecord;
  
  public FotoDTO() {
    super();
  }

  public String getDataAcquisizioneF()
  {
    return IuffiUtils.DATE.formatDate(dataAcquisizione);
  }
  
  public void setDataAcquisizioneF(Date dataAcquisizione)
  {
    this.dataAcquisizione = dataAcquisizione;
  }
   

  public Date getDataAcquisizione()
  {
    return dataAcquisizione;
  }

  public void setDataAcquisizione(Date dataAcquisizione)
  {
    this.dataAcquisizione = dataAcquisizione;
  }
  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public String getDallaDataS()
  {
    return IuffiUtils.DATE.formatDate(dallaData);
  }

  public String getAllaDataS()
  {
    return IuffiUtils.DATE.formatDate(allaData);
  }
   
  public Integer getIdFoto()
  {
    return idFoto;
  }

  public void setIdFoto(Integer idFoto)
  {
    this.idFoto = idFoto;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }

  public Date getDataFoto()
  {
    return dataFoto;
  }

  public void setDataFoto(Date dataFoto)
  {
    this.dataFoto = dataFoto;
  }

  public String getTag()
  {
    return tag;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public MultipartFile getFoto()
  {
    return foto;
  }

  public void setFoto(MultipartFile foto)
  {
    this.foto = foto;
  }

  public String getDataFotoF()
  {
    return IuffiUtils.DATE.formatDate(dataFoto);
  }
  
  public void setDataFotoF(Date dataFoto)
  {
    this.dataFoto = dataFoto;
  }
  
  public List<Integer> getTipoArea()
  {
    return tipoArea;
  }

  public void setTipoArea(List<Integer> tipoArea)
  {
    this.tipoArea = tipoArea;
  }

  public List<Integer> getIspettoreAssegnato()
  {
    return ispettoreAssegnato;
  }

  public void setIspettoreAssegnato(List<Integer> ispettoreAssegnato)
  {
    this.ispettoreAssegnato = ispettoreAssegnato;
  }

  public List<Integer> getSpecieVegetale()
  {
    return specieVegetale;
  }

  public void setSpecieVegetale(List<Integer> specieVegetale)
  {
    this.specieVegetale = specieVegetale;
  }

  public List<Integer> getOrganismoNocivo()
  {
    return organismoNocivo;
  }

  public void setOrganismoNocivo(List<Integer> organismoNocivo)
  {
    this.organismoNocivo = organismoNocivo;
  }

  
  public Double getLongitudine()
  {
    return longitudine;
  }

  public void setLongitudine(Double longitudine)
  {
    this.longitudine = longitudine;
  }

  public Double getLatitudine()
  {
    return latitudine;
  }

  public void setLatitudine(Double latitudine)
  {
    this.latitudine = latitudine;
  }

  public void setFoto(byte[] bytes)
  {
    if (bytes != null) {
      String fileName = ((this.nomeFile+"_"+this.idFoto)!=null)?this.nomeFile:"file.jpeg";
      CustomMultipartFile customMultipartFile = new CustomMultipartFile(bytes, fileName);
      try {
        customMultipartFile.transferTo(customMultipartFile.getFile());
        this.foto = customMultipartFile;
      } catch (IllegalStateException e) {
          //log.info("IllegalStateException : " + e);
          e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
          //log.info("IOException : " + e);
      }
    } else
        this.foto = null;
  }

  public String getBase64()
  {
    return base64;
  }

  public void setBase64(String base64)
  {
    this.base64 = base64;
  }

  public Integer getAnno()
  {
    return anno;
  }

  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }

  
  public Date getDallaData()
  {
    return dallaData;
  }

  public void setDallaData(Date dallaData)
  {
    this.dallaData = dallaData;
  }

  public Date getAllaData()
  {
    return allaData;
  }

  public void setAllaData(Date allaData)
  {
    this.allaData = allaData;
  }

  public Boolean getVisual()
  {
    return visual;
  }

  public void setVisual(Boolean visual)
  {
    this.visual = visual;
  }

  public Boolean getTrappolaggio()
  {
    return trappolaggio;
  }

  public void setTrappolaggio(Boolean trappolaggio)
  {
    this.trappolaggio = trappolaggio;
  }

  public Boolean getCampionamento()
  {
    return campionamento;
  }

  public void setCampionamento(Boolean campionamento)
  {
    this.campionamento = campionamento;
  }

  public Integer getNumeroTrasferta()
  {
    return numeroTrasferta;
  }

  public void setNumeroTrasferta(Integer numeroTrasferta)
  {
    this.numeroTrasferta = numeroTrasferta;
  }

  public String getTipologia()
  {
    return tipologia;
  }

  public void setTipologia(String tipologia)
  {
    this.tipologia = tipologia;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public List<Integer> getTrappole()
  {
    return trappole;
  }

  public void setTrappole(List<Integer> trappole)
  {
    this.trappole = trappole;
  }

  public List<Integer> getCampioni()
  {
    return campioni;
  }

  public void setCampioni(List<Integer> campioni)
  {
    this.campioni = campioni;
  }

  public String getSpecie()
  {
    return specie;
  }

  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

  public String getArea()
  {
    return area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }

  public String getTrappola()
  {
    return trappola;
  }

  public void setTrappola(String trappola)
  {
    this.trappola = trappola;
  }

  public String getCampione()
  {
    return campione;
  }

  public void setCampione(String campione)
  {
    this.campione = campione;
  }

  public String getIspettoreEvento()
  {
    return ispettoreEvento;
  }

  public void setIspettoreEvento(String ispettoreEvento)
  {
    this.ispettoreEvento = ispettoreEvento;
  }

  public String getIspettoreAssegnatoM()
  {
    return ispettoreAssegnatoM;
  }

  public void setIspettoreAssegnatoM(String ispettoreAssegnatoM)
  {
    this.ispettoreAssegnatoM = ispettoreAssegnatoM;
  }

  public String getDescrComune()
  {
    return descrComune;
  }

  public void setDescrComune(String descrComune)
  {
    this.descrComune = descrComune;
  }

  public String getOrganismi()
  {
    return organismi;
  }

  public void setOrganismi(String organismi)
  {
    this.organismi = organismi;
  }

  public Integer getIdVisual()
  {
    return idVisual;
  }

  public void setIdVisual(Integer idVisual)
  {
    this.idVisual = idVisual;
  }

  public Integer getIdCampionamento()
  {
    return idCampionamento;
  }

  public void setIdCampionamento(Integer idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }

  public Integer getIdTrappolaggio()
  {
    return idTrappolaggio;
  }

  public void setIdTrappolaggio(Integer idTrappolaggio)
  {
    this.idTrappolaggio = idTrappolaggio;
  }

  public byte[] getFotoByte()
  {
    return fotoByte;
  }

  public void setFotoByte(byte[] fotoByte)
  {
    this.fotoByte = fotoByte;
  }

  public String getDataFotoS()
  {
    return IuffiUtils.DATE.formatDate(dataFoto);
  }

  public Integer getIdRecord()
  {
    return idRecord;
  }

  public void setIdRecord(Integer idRecord)
  {
    this.idRecord = idRecord;
  }

  public boolean checkNull() throws IllegalAccessException {
    for (Field f : getClass().getDeclaredFields())
        if (f.get(this) != null && !f.getName().equalsIgnoreCase("serialVersionUID"))
            return false;
    return true;
  }

}
