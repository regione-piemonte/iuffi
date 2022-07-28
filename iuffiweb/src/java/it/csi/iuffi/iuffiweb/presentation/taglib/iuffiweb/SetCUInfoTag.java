package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SetCUInfoTag extends SimpleTagSupport
{
  public static final Pattern PATTERN_CU_NUMBER = Pattern
      .compile("CU-IUFFI-([0-9]+)");

  @Override
  public void doTag() throws IOException, JspException
  {
    try
    {
      final PageContext context = (PageContext) this.getJspContext();
      final HttpServletRequest request = (HttpServletRequest) context
          .getRequest();
      final String useCaseController = (String) request
          .getAttribute("useCaseController");
      request.setAttribute("cuNumber",
          IuffiUtils.APPLICATION.getCUNumber(useCaseController));
      ProcedimentoOggetto po = IuffiFactory.getProcedimentoOggetto(request);
      if (po != null)
      {
        final QuadroOggettoDTO quadroDTO = po.findQuadroByCU(useCaseController);
        if (quadroDTO != null)
        {
          request.setAttribute("cuCodQuadro", quadroDTO.getCodQuadro());
        }
      }
    }
    catch (Exception e)
    {
      BaseTag.logger.error("[CUNumberTag.doEndTag] Exception:", e);
    }
    super.doTag();
  }

}
