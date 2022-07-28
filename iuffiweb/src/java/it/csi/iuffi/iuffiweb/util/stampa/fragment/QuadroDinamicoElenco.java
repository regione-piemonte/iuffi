package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.DatiAnticipo;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.ElementoEValoreQuadroDinamicoVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.QuadroDinamicoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.RaggruppamentoQuadroDinamico;
import it.csi.iuffi.iuffiweb.integration.RigaAnticipoLivello;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDinamicoElenco extends Fragment
{
  public static final String TAG_NAME_FRAGMENT                = "QuadroDinamico";

  private String codQuadro;
  
  
  
  public QuadroDinamicoElenco(String codQuadro)
  {
    super();
    this.codQuadro = codQuadro;
  }

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    
    QuadroDinamicoDTO quadroDinamico = IuffiUtils.APPLICATION.getEJBQuadroDinamico().getQuadroDinamico(codQuadro, procedimentoOggetto.getIdProcedimentoOggetto(), null);
    String cu = "CU-IUFFI-116-E_" + codQuadro;
    QuadroDTO quadroDTO = procedimentoOggetto.findQuadroByCU(cu);
    
    String vis = Boolean.FALSE.toString();
    if(quadroDinamico.getDati()!=null && quadroDinamico.getDati().size()>0) {
      vis = Boolean.TRUE.toString();
    }
    
    writeTag(writer, "VisibilityQD", vis);
    writeTag(writer, "TitoloQuadro", quadroDTO.getDescQuadro());
    
    
    writer.writeStartElement("Etichette");
    int count = 1;
    for(String item: quadroDinamico.getIntestazioniElenco()) {
      writeTag(writer, "Etichetta"+getProgressivoEtichetta(count), item);
      count++;
    }
    writer.writeEndElement();
    
    for(RaggruppamentoQuadroDinamico record: quadroDinamico.getRecordMultipli()) {
      writer.writeStartElement("Dati");
      count = 1;
      for(ElementoEValoreQuadroDinamicoVO item: record.getElementi()) {
        writeTag(writer, "Dati"+getProgressivoEtichetta(count), item.getValoreCompletoStampa());
        count++;
      }
      writer.writeEndElement();
    }
    
    writer.writeEndElement();
  }

  private String getProgressivoEtichetta(int count)
  {
    if(count>9)
      return count+"";
    
    return "0"+count;
  }

  public String getCodQuadro()
  {
    return codQuadro;
  }

  public void setCodQuadro(String codQuadro)
  {
    this.codQuadro = codQuadro;
  }

  
 
}
