package it.csi.iuffi.iuffiweb.presentation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/logout")
@IuffiSecurity(value = "", controllo = IuffiSecurity.Controllo.NESSUNO)
public class LogoutController extends BaseController
{
  @RequestMapping("conferma")
  public String conferma(Model model, HttpServletRequest request)
  {
    Cookie[] cookies = request.getCookies();
    if (cookies != null)
    {
      for (Cookie cookie : cookies)
      {
        if ("IUFFIWEB_LOGIN_PORTAL".equals(cookie.getName()))
        {
          if (IuffiConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE
              .equals(cookie.getValue()))
          {
            model.addAttribute("shibbolethLogoutUrl",
                "/sps_liv1_WRUP/Shibboleth.sso/Logout");
          }
          else
          {
            if (IuffiConstants.PORTAL.SPID.equals(cookie.getValue()))
            {
              model.addAttribute("shibbolethLogoutUrl",
                  "/ssp_liv2_spid_GASPRP_AGRIC/logout.do");
            }
            else
            {
              model.addAttribute("shibbolethLogoutUrl",
                  "/liv1/Shibboleth.sso/Logout");
            }
          }
          break;
        }
      }
    }
    
    if(request.getSession()!=null && request.getSession().getAttribute(IuffiConstants.IUFFI.GRUPPO_HOME_PAGE) != null) {
      String gruppoHomePage = (String) request.getSession().getAttribute(IuffiConstants.IUFFI.GRUPPO_HOME_PAGE);
      if(StringUtils.isNotBlank(gruppoHomePage)) {
        model.addAttribute("gruppoHomePage", gruppoHomePage);
      }
      
    }
    
    model.addAttribute("webContext", IuffiConstants.IUFFIWEB.WEB_CONTEXT);
    return "logout/conferma";
  }

  @RequestMapping("esegui")
  @ResponseBody
  public String esegui(HttpSession session)
  {
    session.invalidate();
    return "<success>true</success>";
  }
}
