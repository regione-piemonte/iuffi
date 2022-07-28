package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico;

import java.util.ArrayList;
import java.util.List;

public class RaggruppamentoQuadroDinamico extends GruppoQuadroDinamicoDTO
{
  /** serialVersionUID */
  private static final long                     serialVersionUID     = 5398006381719981894L;
  private List<ElementoEValoreQuadroDinamicoVO> elementi;
  protected long                                numProgressivoRecord = 0;

  public RaggruppamentoQuadroDinamico(List<ElementoQuadroDTO> elementiQuadro,
      DatiComuniElemQuadroDTO datiQuadro)
  {
    this.elementiQuadro = elementiQuadro;
    elementi = new ArrayList<ElementoEValoreQuadroDinamicoVO>();
    for (ElementoQuadroDTO elemento : elementiQuadro)
    {
      List<DatoElementoQuadroDTO> valori = null;
      if (datiQuadro != null)
      {
        valori = datiQuadro.getMapValori().get(elemento.getIdElementoQuadro());
      }
      elementi.add(new ElementoEValoreQuadroDinamicoVO(elemento, valori));
    }
    if (datiQuadro != null)
    {
      numProgressivoRecord = datiQuadro.getNumProgressivoRecord();
    }
  }

  public List<ElementoEValoreQuadroDinamicoVO> getElementi()
  {
    return elementi;
  }

  public void setElementi(List<ElementoEValoreQuadroDinamicoVO> elementi)
  {
    this.elementi = elementi;
  }

  public long getNumProgressivoRecord()
  {
    return numProgressivoRecord;
  }

  public void setNumProgressivoRecord(long numProgressivoRecord)
  {
    this.numProgressivoRecord = numProgressivoRecord;
  }
}
