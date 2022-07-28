package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiComuniElemQuadroDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                        serialVersionUID = -6573367363710431749L;
  protected long                                   idDatiComuniElemQuadro;
  protected long                                   numProgressivoRecord;
  protected Map<Long, List<DatoElementoQuadroDTO>> mapValori        = new HashMap<Long, List<DatoElementoQuadroDTO>>();

  public long getIdDatiComuniElemQuadro()
  {
    return idDatiComuniElemQuadro;
  }

  public void setIdDatiComuniElemQuadro(long idDatiComuniElemQuadro)
  {
    this.idDatiComuniElemQuadro = idDatiComuniElemQuadro;
  }

  public Map<Long, List<DatoElementoQuadroDTO>> getMapValori()
  {
    return mapValori;
  }

  public void setMapValori(Map<Long, List<DatoElementoQuadroDTO>> mapValori)
  {
    this.mapValori = mapValori;
  }

  public long getNumProgressivoRecord()
  {
    return numProgressivoRecord;
  }

  public void setNumProgressivoRecord(long numProgressivoRecord)
  {
    this.numProgressivoRecord = numProgressivoRecord;
  }

  public void addDato(DatoElementoQuadroDTO dato)
  {
    final long idElementoQuadro = dato.getIdElementoQuadro();
    List<DatoElementoQuadroDTO> listaValori = mapValori.get(idElementoQuadro);
    if (listaValori == null)
    {
      listaValori = new ArrayList<DatoElementoQuadroDTO>();
      mapValori.put(idElementoQuadro, listaValori);
    }
    listaValori.add(dato);
  }

}
