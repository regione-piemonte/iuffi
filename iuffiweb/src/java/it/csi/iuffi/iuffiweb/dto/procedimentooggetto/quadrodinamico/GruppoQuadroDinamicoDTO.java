package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico;

import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class GruppoQuadroDinamicoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long         serialVersionUID = -8367378343513677665L;
  protected boolean                 sezioneConTitolo = false;
  protected String                  titolo           = null;
  protected List<ElementoQuadroDTO> elementiQuadro   = null;

  public boolean isSezioneConTitolo()
  {
    return sezioneConTitolo;
  }

  public void setSezioneConTitolo(boolean sezioneEspandibileConTitolo)
  {
    this.sezioneConTitolo = sezioneEspandibileConTitolo;
  }

  public List<ElementoQuadroDTO> getElementiQuadro()
  {
    return elementiQuadro;
  }

  public void setElementiQuadro(List<ElementoQuadroDTO> elementiQuadro)
  {
    this.elementiQuadro = elementiQuadro;
  }

  public void addElemento(ElementoQuadroDTO elemento)
  {
    if (elementiQuadro == null)
    {
      elementiQuadro = new ArrayList<ElementoQuadroDTO>();
      sezioneConTitolo = elemento.isTipoTIT();
      if (sezioneConTitolo)
      {
        titolo = elemento.getNomeLabel();
      }
      else
      {
        elementiQuadro.add(elemento);
      }
    }
    else
    {
      elementiQuadro.add(elemento);
    }
  }

  public String getTitolo()
  {
    return titolo;
  }

  public void setTitolo(String titolo)
  {
    this.titolo = titolo;
  }
}
