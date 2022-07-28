package it.csi.iuffi.iuffiweb.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IReportisticaEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ElencoQueryBandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ParametriQueryReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ReportVO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ReportisticaDAO;

@Stateless()
@EJB(name = "java:app/Reportistica", beanInterface = IReportisticaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ReportisticaEJB extends IuffiAbstractEJB<ReportisticaDAO>
    implements IReportisticaEJB
{

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getInformazioniBando(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public GraficoVO getGrafico(long idElencoQuery, ParametriQueryReportVO params)
      throws InternalUnexpectedException
  {
    GraficoVO graficoVO = dao.getDatiGrafico(idElencoQuery);

    params.setIstruzioneSQL(graficoVO.getIstruzioneSQL());
    params.setIdTipoVisualizzazione(graficoVO.getIdTipoVisualizzazione());

    ReportVO reportVO = dao.getReportBando(params);
    graficoVO.setReportVO(reportVO);
    graficoVO.setJsonData(reportVO.getJSON());
    return graficoVO;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> elencoQueryBando(long idBando,
      Boolean flagElenco, String attore) throws InternalUnexpectedException
  {
    return dao.elencoQueryBando(idBando, flagElenco, attore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean graficiTabellariPresenti(long idBando, String attore)
      throws InternalUnexpectedException
  {
    return dao.graficiTabellariPresenti(idBando, attore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public ReportVO getReportBando(ParametriQueryReportVO parametri)
      throws InternalUnexpectedException
  {
    return dao.getReportBando(parametri);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public String getQueryParametroPagamenti() throws InternalUnexpectedException
  {
    return dao.getQueryParametroPagamenti();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean hasExcelTemplate(long idElencoQuery)
      throws InternalUnexpectedException
  {
    return dao.hasExcelTemplate(idElencoQuery);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public byte[] getExcelParametroDiElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException
  {
    return dao.getExcelParametroDiElencoQuery(idElencoQuery);
  }

  @Override
  public List<ElencoQueryBandoDTO> getElencoReport(String extCodAttore)
      throws InternalUnexpectedException
  {
    return dao.getElencoReport(extCodAttore);
  }

  @Override
  public List<ElencoQueryBandoDTO> getElencoGrafici(String extCodAttore)
      throws InternalUnexpectedException
  {
    return dao.getElencoGrafici(extCodAttore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean hasExcelTemplateInElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException
  {
    return dao.hasExcelTemplateInElencoQuery(idElencoQuery);
  }
}
