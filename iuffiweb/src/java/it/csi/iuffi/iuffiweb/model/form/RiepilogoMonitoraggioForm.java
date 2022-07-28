package it.csi.iuffi.iuffiweb.model.form;

import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.model.RiepilogoMonitoraggioDTO;

public class RiepilogoMonitoraggioForm
{

  List<RiepilogoMonitoraggioDTO> riepilogoList = new ArrayList<RiepilogoMonitoraggioDTO>();

  public List<RiepilogoMonitoraggioDTO> getRiepilogoList()
  {
    return riepilogoList;
  }

  public void setRiepilogoList(List<RiepilogoMonitoraggioDTO> riepilogoList)
  {
    this.riepilogoList = riepilogoList;
  }

}
