package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico;

import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.CheckBox;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ElementoEValoreQuadroDinamicoVO implements ILoggable
{
  /** serialVersionUID */
  private static final long           serialVersionUID = -3057636963346210192L;
  private ElementoQuadroDTO           elemento;
  private List<DatoElementoQuadroDTO> valori;

  public ElementoEValoreQuadroDinamicoVO(ElementoQuadroDTO elemento,
      List<DatoElementoQuadroDTO> valori)
  {
    this.elemento = elemento;
    this.valori = valori;
  }

  public String getNomeLabel()
  {
    return elemento.getNomeLabel();
  }

  public boolean isTipoSTR()
  {
    return elemento.isTipoSTR();
  }

  public boolean isTipoMST()
  {
    return elemento.isTipoMST();
  }

  public boolean isTipoDTA()
  {
    return elemento.isTipoDTA();
  }

  public boolean isTipoNUM()
  {
    return elemento.isTipoNUM();
  }

  public boolean isTipoEUR()
  {
    return elemento.isTipoEUR();
  }

  public boolean isTipoPCT()
  {
    return elemento.isTipoPCT();
  }

  public boolean isTipoCMB()
  {
    return elemento.isTipoCMB();
  }

  public boolean isTipoRBT()
  {
    return elemento.isTipoRBT();
  }

  public boolean isTipoCBT()
  {
    return elemento.isTipoCBT();
  }

  public boolean isTipoLST()
  {
    return elemento.isTipoLST();
  }

  public boolean isTipoTXT()
  {
    return elemento.isTipoTXT();
  }

  public boolean isTipoDTQ()
  {
    return elemento.isTipoDTQ();
  }

  public boolean isTipoDTF()
  {
    return elemento.isTipoDTF();
  }

  public boolean isTipoNU0()
  {
    return elemento.isTipoNU0();
  }

  public boolean isTipoANN()
  {
    return elemento.isTipoANN();
  }

  public boolean isTipoNUQ()
  {
    return elemento.isTipoNUQ();
  }

  public boolean isTipoTIT()
  {
    return elemento.isTipoTIT();
  }

  public boolean isTipoSTM()
  {
    return elemento.isTipoSTM();
  }

  public boolean isTipoHTM()
  {
    return elemento.isTipoHTM();
  }

  public boolean isTipoNumerico()
  {
    return elemento.isTipoNumerico();
  }

  public long getIdElementoQuadro()
  {
    return elemento.getIdElementoQuadro();
  }

  public int getMaxLength()
  {
    return elemento.getMaxLength();
  }

  public Integer getPrecisione()
  {
    return elemento.getPrecisione();
  }

  public List<VoceElementoDTO> getVociElemento()
  {
    return elemento.getVociElemento();
  }

  public String getValoreCompleto()
  {
    if (valori != null)
    {
      StringBuilder sb = new StringBuilder();
      for (DatoElementoQuadroDTO dato : valori)
      {
        if (sb.length() > 0)
        {
          sb.append(",<br />");
        }
        sb.append(IuffiUtils.QUADRODINAMICO.formatValoreElementoQuadro(dato,
            elemento));
      }
      return sb.toString();
    }
    return null;
  }
  
  public String getValoreCompletoStampa()
  {
    if (valori != null)
    {
      StringBuilder sb = new StringBuilder();
      for (DatoElementoQuadroDTO dato : valori)
      {
        if (sb.length() > 0)
        {
          sb.append(",<br />");
        }
        sb.append(IuffiUtils.QUADRODINAMICO.formatValoreElementoQuadro(dato,
            elemento));
      }
      return sb.toString().replace("&euro;", "€");
    }
    return null;
  }

  public String getValoreGrezzo()
  {
    return valori == null || valori.isEmpty() ? null
        : valori.get(0).getValoreElemento();
  }

  public String getValorePerModifica()
  {
    return valori == null || valori.isEmpty() ? null
        : IuffiUtils.QUADRODINAMICO.formatValoreElementoQuadro(valori.get(0),
            elemento, true);
  }

  public String[] getValori()
  {
    String[] arrayValori = null;
    if (valori != null && !valori.isEmpty())
    {
      final int size = valori.size();
      arrayValori = new String[size];
      for (int i = 0; i < size; ++i)
      {
        arrayValori[i] = valori.get(i).getValoreElemento();
      }
    }
    return arrayValori;
  }

  public List<CheckBox> getValoriCheckbox()
  {
    List<VoceElementoDTO> vociElemento = elemento.getVociElemento();
    List<CheckBox> list = new ArrayList<CheckBox>();
    if (vociElemento != null && !vociElemento.isEmpty())
    {
      final int size = vociElemento.size();
      String[] valori = getValori();
      for (int i = 0; i < size; ++i)
      {
        VoceElementoDTO voce = vociElemento.get(i);
        final String codiceVoce = voce.getCodice();
        CheckBox chk = new CheckBox(codiceVoce, voce.getValore(),
            IuffiUtils.ARRAY.contains(valori, codiceVoce));
        list.add(chk);
      }
    }
    return list;
  }

  public String getNote()
  {
    return elemento.getNote();
  }

  public boolean isProtetto()
  {
    return elemento.isProtetto();
  }
}
