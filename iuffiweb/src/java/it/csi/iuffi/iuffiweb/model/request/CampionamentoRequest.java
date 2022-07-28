package it.csi.iuffi.iuffiweb.model.request;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class CampionamentoRequest implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4621339630061084884L;
  
  private Integer idCampionamento;
  private Integer idRilevazione;
  private Integer idSpecieVegetale;
  private Integer idTipoCampione;
  private String istatComune;
  private String descComune;
  private String comune;
  private Integer idAnfi;
  private String testLaboratorio;
  private Double latitudine;
  private Double longitudine;
  
  private Date dataRilevazione;
  private Date dataMissione;
  private String pianta;
  
  private Integer idIspezioneVisiva;
  private Integer idAnagrafica;
  private String ispettoreCampionamento;
  
  //aggiuntivi
  private String ispettore;
  private String specie;
  private String area;
  private String dettaglioArea;
  private String organismi;
  private String tipoCampione;
  private String esito;
  private String foto;
  private Integer idMissione;
  private Integer idRegistroCampioni;
  private String anagraficaAziendale;
  private Integer idTecnicoLab;
  private Integer idOrganismoNocivo;
  //ricerca
  private Integer anno;
  private Date dallaData;
  private Date allaData;
  private String dallaDataS;
  private String allaDataS;
  private List<Integer> tipoArea;
  private List<Integer> ispettoreAssegnato;
  private List<Integer> ispettoriSecondari;
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  private List<Integer> tipoCampioni;
  private List<Integer> tipologiaTest;
  private List<Integer> codiciTest;
  private String cuaa;
  private String obiettivoIndagineUfficiale;
  private String obiettivoEmergenza;  
  private String numRegistroLab;
  
  public CampionamentoRequest()
  {
    super();
  }


  public CampionamentoRequest(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }


  public CampionamentoRequest(Integer idCampionamento, Integer idRilevazione,
      Integer idSpecieVegetale, Integer idTipoCampione, String istatComune,
      Integer idAnfi, Integer idCategoria, Double latitudine,
      Double longitudine, Date dataRilevazione)
  {
    super();
    this.idCampionamento = idCampionamento;
    this.idRilevazione = idRilevazione;
    this.idSpecieVegetale = idSpecieVegetale;
    this.idTipoCampione = idTipoCampione;
    this.istatComune = istatComune;
    this.idAnfi = idAnfi;
    this.latitudine = latitudine;
    this.longitudine = longitudine;
    this.dataRilevazione = dataRilevazione;
  }

  public Integer getIdCampionamento()
  {
    return idCampionamento;
  }
  public void setIdCampionamento(Integer idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }
  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }
  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }
  public long getIdTipoCampione()
  {
    return idTipoCampione;
  }
  public void setIdTipoCampione(Integer idTipoCampione)
  {
    this.idTipoCampione = idTipoCampione;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public long getIdAnfi()
  {
    return idAnfi;
  }
  public void setIdAnfi(Integer idAnfi)
  {
    this.idAnfi = idAnfi;
  }
  public Double getLatitudine()
  {
    return latitudine;
  }
  public void setLatitudine(Double latitudine)
  {
    this.latitudine = latitudine;
  }
  public Double getLongitudine()
  {
    return longitudine;
  }
  public void setLongitudine(Double longitudine)
  {
    this.longitudine = longitudine;
  }
  public Date getDataRilevazione()
  {
    return dataRilevazione;
  }
  public void setDataRilevazione(Date dataRilevazione)
  {
    this.dataRilevazione = dataRilevazione;
  }
  public String getIspettore()
  {
    return ispettore;
  }

  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
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

  public String getOrganismi()
  {
    return organismi;
  }

  public void setOrganismi(String organismi)
  {
    this.organismi = organismi;
  }

  public String getEsito()
  {
    return esito;
  }

  public void setEsito(String esito)
  {
    this.esito = esito;
  }

  public String getFoto()
  {
    return foto;
  }

  public void setFoto(String foto)
  {
    this.foto = foto;
  }

  public Integer getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }

  public Integer getIdRegistroCampioni()
  {
    return idRegistroCampioni;
  }

  public void setIdRegistroCampioni(Integer idRegistroCampioni)
  {
    this.idRegistroCampioni = idRegistroCampioni;
  }

  public String getAnagraficaAziendale()
  {
    return anagraficaAziendale;
  }

  public void setAnagraficaAziendale(String anagraficaAziendale)
  {
    this.anagraficaAziendale = anagraficaAziendale;
  }

  public Integer getIdTecnicoLab()
  {
    return idTecnicoLab;
  }

  public void setIdTecnicoLab(Integer idTecnicoLab)
  {
    this.idTecnicoLab = idTecnicoLab;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
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

  public String getDallaDataS()
  {
    return dallaDataS;
  }

  public void setDallaDataS(String dallaDataS)
  {
    this.dallaDataS = dallaDataS;
  }

  public String getAllaDataS()
  {
    return allaDataS;
  }

  public void setAllaDataS(String allaDataS)
  {
    this.allaDataS = allaDataS;
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

  public List<Integer> getIspettoriSecondari()
  {
    return ispettoriSecondari;
  }

  public void setIspettoriSecondari(List<Integer> ispettoriSecondari)
  {
    this.ispettoriSecondari = ispettoriSecondari;
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

  public List<Integer> getTipoCampioni()
  {
    return tipoCampioni;
  }

  public void setTipoCampioni(List<Integer> tipoCampioni)
  {
    this.tipoCampioni = tipoCampioni;
  }

  public String getTipoCampione()
  {
    return tipoCampione;
  }

  public void setTipoCampione(String tipoCampione)
  {
    this.tipoCampione = tipoCampione;
  }

  public String getDataRilevazioneF()
  {
    return IuffiUtils.DATE.formatDate(dataRilevazione);
  }
  
  public void setDataRilevazioneF(Date dataRilevazione)
  {
    this.dataRilevazione = dataRilevazione;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }

  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }

  public String getDettaglioArea()
  {
    return dettaglioArea;
  }

  public void setDettaglioArea(String dettaglioArea)
  {
    this.dettaglioArea = dettaglioArea;
  }

  public Date getDataMissione()
  {
    return dataMissione;
  }

  public void setDataMissione(Date dataMissione)
  {
    this.dataMissione = dataMissione;
  }

  public String getPianta()
  {
    return pianta;
  }

  public void setPianta(String pianta)
  {
    this.pianta = pianta;
  }

  public String getTestLaboratorio()
  {
    return testLaboratorio;
  }

  public void setTestLaboratorio(String testLaboratorio)
  {
    this.testLaboratorio = testLaboratorio;
  }

  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }

  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }

  public String getIspettoreCampionamento()
  {
    return ispettoreCampionamento;
  }

  public void setIspettoreCampionamento(String ispettoreCampionamento)
  {
    this.ispettoreCampionamento = ispettoreCampionamento;
  }

  public boolean checkNull() throws IllegalAccessException {
    for (Field f : getClass().getDeclaredFields())
        if (f.get(this) != null && !f.getName().equalsIgnoreCase("serialVersionUID"))
            return false;
    return true;
  }


  public String getObiettivoEmergenza()
  {
    return obiettivoEmergenza;
  }


  public void setObiettivoEmergenza(String obiettivoEmergenza)
  {
    this.obiettivoEmergenza = obiettivoEmergenza;
  }


  public String getObiettivoIndagineUfficiale()
  {
    return obiettivoIndagineUfficiale;
  }


  public void setObiettivoIndagineUfficiale(String obiettivoIndagineUfficiale)
  {
    this.obiettivoIndagineUfficiale = obiettivoIndagineUfficiale;
  }


  public String getCuaa()
  {
    return cuaa;
  }


  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }



  public String getDescComune()
  {
    return descComune;
  }


  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }


  public List<Integer> getCodiciTest()
  {
    return codiciTest;
  }


  public void setCodiciTest(List<Integer> codiciTest)
  {
    this.codiciTest = codiciTest;
  }


  public List<Integer> getTipologiaTest()
  {
    return tipologiaTest;
  }


  public void setTipologiaTest(List<Integer> tipologiaTest)
  {
    this.tipologiaTest = tipologiaTest;
  }


  public String getNumRegistroLab()
  {
    return numRegistroLab;
  }


  public void setNumRegistroLab(String numRegistroLab)
  {
    this.numRegistroLab = numRegistroLab;
  }


 }
