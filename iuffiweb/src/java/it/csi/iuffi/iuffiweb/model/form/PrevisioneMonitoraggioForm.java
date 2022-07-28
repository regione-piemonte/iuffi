package it.csi.iuffi.iuffiweb.model.form;

import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.model.PrevisioneMonitoraggioDTO;

public class PrevisioneMonitoraggioForm
{

  List<PrevisioneMonitoraggioDTO> previsioneList = new ArrayList<PrevisioneMonitoraggioDTO>();

  public List<PrevisioneMonitoraggioDTO> getPrevisioneList()
  {
    return previsioneList;
  }

  public void setPrevisioneList(List<PrevisioneMonitoraggioDTO> previsioneList)
  {
    this.previsioneList = previsioneList;
  }

}
