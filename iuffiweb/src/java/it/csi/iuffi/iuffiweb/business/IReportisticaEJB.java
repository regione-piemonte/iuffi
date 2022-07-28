package it.csi.iuffi.iuffiweb.business;

import java.util.List;

import javax.ejb.Local;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ElencoQueryBandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ParametriQueryReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ReportVO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

@Local
public interface IReportisticaEJB extends IIuffiAbstractEJB
{

  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException;

  public GraficoVO getGrafico(long idElencoQuery, ParametriQueryReportVO params)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> elencoQueryBando(long attribute,
      Boolean true1, String string) throws InternalUnexpectedException;

  public boolean graficiTabellariPresenti(long idBando, String string)
      throws InternalUnexpectedException;

  public ReportVO getReportBando(ParametriQueryReportVO parametri)
      throws InternalUnexpectedException;

  public String getQueryParametroPagamenti() throws InternalUnexpectedException;

  boolean hasExcelTemplate(long idElencoQuery)
      throws InternalUnexpectedException;

  byte[] getExcelParametroDiElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException;

  boolean hasExcelTemplateInElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException;

  public List<ElencoQueryBandoDTO> getElencoReport(String extCodAttore)
      throws InternalUnexpectedException;

  public List<ElencoQueryBandoDTO> getElencoGrafici(String extCodAttore)
      throws InternalUnexpectedException;

}
