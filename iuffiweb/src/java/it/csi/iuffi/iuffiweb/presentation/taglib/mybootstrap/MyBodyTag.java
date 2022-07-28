package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import it.csi.iuffi.iuffiweb.util.CustomTagUtils;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public abstract class MyBodyTag extends MyAbstractTag
{
  public static final String     CSS_ERROR_CLASSES = "has-error red-tooltip";
  protected final CustomTagUtils TAG_UTIL          = IuffiUtils.TAG;
  protected String               id;
  protected String               name;
  protected String               title;
  protected String               cssClass;
  protected String               style;
  protected String               label;
  protected Integer              labelSize;
  protected Boolean              disabled;
  protected Integer              controlSize;
  protected String               onClickItem;
  protected String               onchange;
  protected String               onkeyup;
  protected String               onkeypress;
  private String                 additionalCssClass;
  private String 				 additionalStyleCss;  
  private Integer                tabIndex;

  /** serialVersionUID */
  private static final long      serialVersionUID  = -5111481346287406888L;

  public MyBodyTag()
  {

  }

  public MyBodyTag(String additionalCssClass)
  {
    this.additionalCssClass = additionalCssClass;
  }
  
  public MyBodyTag(String additionalCssClass, String additionalStyleCss) 
  {
	  this(additionalCssClass);
	  this.additionalStyleCss = additionalStyleCss;
  }

  protected String processErrors()
  {
    return escapeHtml(getError(name));
  }

  protected void addErrorAttributes(StringBuilder sb, String errorMessage)
  {
    TAG_UTIL.addAttribute(sb, "data-original-title", errorMessage);
    TAG_UTIL.addAttribute(sb, "data-toggle", "error-tooltip");
  }

  @Override
  public int doEndTag() throws JspException
  {
    String errorMessage = processErrors();
    try
    {
      includeRequiredJSFile();
      boolean error = errorMessage != null;
      StringBuilder sb = new StringBuilder();
      boolean group = label != null;
      if (group)
      {
        sb.append("<div class=\"form-group\">");
        if (error) /* LABEL-ERROR */
        {
          sb.append("<div class=\"has-error\"");
          sb.append(">");
        }
        sb.append("<label ");
        if (error) /* LABEL-ERROR */
        {
          addErrorAttributes(sb, errorMessage);
        }
        int lSize = 3;
        int cSize = 9;
        if (labelSize != null || controlSize != null)
        {
          if (labelSize != null && controlSize != null)
          {
            lSize = labelSize;
            cSize = controlSize;
          }
          else
          {
            if (labelSize != null)
            {
              lSize = labelSize;
              cSize = 12 - lSize;
            }
            else
            {
              cSize = controlSize;
              lSize = 12 - cSize;
            }
          }
        }
        TAG_UTIL.addAttribute(sb, "class",
            IuffiUtils.STRING.concat(" ",
                error ? "alert alert-danger error-label" : "control-label",
                "col-sm-" + lSize));
        sb.append(">");
        sb.append(escapeHtml(label));
        if (error)
        {
          sb.append(
              " <span class=\"icon icon-exclamation-sign\" aria-hidden=\"true\"></span>");
        }
        sb.append("</label>");
        if (error) /* LABEL-ERROR */
        {
          sb.append("</div>");
        }
        sb.append("<div ");
        // TAG_UTIL.addAttribute(sb, "style", "padding-right:30px");
        if (error)
        {
          TAG_UTIL.addAttribute(sb, "class", IuffiUtils.STRING.concat(" ",
              CSS_ERROR_CLASSES, "col-sm-" + cSize));
          addErrorAttributes(sb, errorMessage);
          if (isCleanOnError())
          {
            errorMessage = null;
          }
        }
        else
        {
          TAG_UTIL.addAttribute(sb, "class",
              IuffiUtils.STRING.concat(" ", "col-sm-" + cSize));
        }
        sb.append(">");
      }
      else
      {
        if (error)
        {
          sb.append("<div ");
          TAG_UTIL.addAttribute(sb, "class", CSS_ERROR_CLASSES);
          addErrorAttributes(sb, errorMessage);
          if (isCleanOnError())
          {
            errorMessage = null;
          }
          sb.append(">");
        }
      }
      writeCustomTag(sb, errorMessage);
      if (group)
      {
        sb.append("</div></div>");
      }
      else
      {
        if (error)
        {
          sb.append("</div>");
        }
      }
      this.pageContext.getOut().write(sb.toString());
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  protected void includeRequiredJSFile()
  {
    // Di default non fa nulla, deve essere ridefinitito nelle classi chiamanti
    // per settare in request (c'è il metodo di utilità addJSFileToPage())
    // il/gli
    // script richiesti, che verranno inseriti nella pagina dal tag
    // <m:include-scripts/>
    // NOTA MOOOOOOLTO BENE:
    // OVVIAMENTE il tag <m:include-scripts/> DEVE ESSERE INCLUSO NELLA PAGINA
    // DOPO TUTTI I TAG CHE NE RICHIEDONO LE FUNZIONALITA'
  }

  @SuppressWarnings("unchecked")
  protected void addJSFileToPage(String jsFile)
  {
    List<String> list = (List<String>) this.pageContext
        .getAttribute("__scriptList");
    if (list == null)
    {
      list = new ArrayList<String>();
      this.pageContext.setAttribute("__scriptList", list);
    }
    else
    {
      if (list.contains(jsFile))
      {
        return; // Il file c'è già, inutile aggiungerlo di nuovo
      }
    }
    list.add(jsFile);
  }

  protected boolean isCleanOnError()
  {
    return true;
  }

  protected abstract void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception;

  /**
   * @param sb
   */
  protected void addBaseAttributes(StringBuilder sb, boolean error)
  {
    addIdAttribute(sb, error);
    addNameAttribute(sb, error);
    addCssClassAttribute(sb, error);
    addStyleAttribute(sb, error);
    addDisableAttributeIfSet(sb, error);
    addTitleAttribute(sb, error);
    addTabIndexAttribute(sb, error);
    addOnClickAttribute(sb, error);
    addOnChangeAttribute(sb, error);
    addOnKeyUpAttribute(sb, error);
    addOnKeyPressAttribute(sb, error);
  }

  private void addTabIndexAttribute(StringBuilder sb, boolean error)
  {
    if (tabIndex != null)
    {
      TAG_UTIL.addAttributeIfNotNull(sb, "tabindex", tabIndex);
    }
  }

  private void addOnKeyPressAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onkeypress", onkeypress);
  }

  private void addOnKeyUpAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onkeyup", onkeyup);
  }

  protected void addOnChangeAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onchange", onchange);
  }

  protected void addOnClickAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onclick", onClickItem);
  }

  protected void addDisableAttributeIfSet(StringBuilder sb, boolean error)
  {
    if (disabled != null && disabled.booleanValue())
    {
      TAG_UTIL.addAttribute(sb, "disabled", "disabled");
    }
  }

  protected void addTitleAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "title", title);
  }

  protected void addBaseAttributes(StringBuilder sb)
  {
    addBaseAttributes(sb, false);
  }

  protected void addStyleAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "style", style);
  }

  protected void addCssClassAttribute(StringBuilder sb, boolean error)
  {
    String cSizeClass = null;
    if (label != null)
    {
      int cSize = 9;
      if (labelSize != null || controlSize != null)
      {
        if (labelSize != null && controlSize != null)
        {
          cSize = controlSize;
        }
        else
        {
          if (labelSize != null)
          {
            cSize = 12 - labelSize;
          }
          else
          {
            cSize = controlSize;
          }
        }
      }
      cSizeClass = "col-sm-" + cSize;
    }
    if (error)
    {
      TAG_UTIL.addAttributeIfNotNull(sb, "class", IuffiUtils.STRING.concat(
          " ", CSS_ERROR_CLASSES, additionalCssClass, cSizeClass, cssClass));
    }
    else
    {
      TAG_UTIL.addAttributeIfNotNull(sb, "class", IuffiUtils.STRING
          .concat(" ", cSizeClass, additionalCssClass, cssClass));
    }
    
    if(additionalStyleCss != null)
    {
    	TAG_UTIL.addAttributeIfNotNull(sb, "style", additionalStyleCss);
    }
  }

  protected void addNameAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "name", name);
  }

  protected void addIdAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "id", id);
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }

  public String getStyle()
  {
    return style;
  }

  public void setStyle(String style)
  {
    this.style = style;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public Integer getLabelSize()
  {
    return labelSize;
  }

  public void setLabelSize(Integer labelSize)
  {
    this.labelSize = labelSize;
  }

  public Integer getControlSize()
  {
    return controlSize;
  }

  public void setControlSize(Integer controlSize)
  {
    this.controlSize = controlSize;
  }

  public StringBuilder addAttribute(StringBuilder sb, String name, Object value)
  {
    return TAG_UTIL.addAttribute(sb, name, value);
  }

  public StringBuilder addAttributeIfNotNull(StringBuilder sb, String name,
      Object value)
  {
    return TAG_UTIL.addAttributeIfNotNull(sb, name, value);
  }

  public Boolean getDisabled()
  {
    return disabled;
  }

  public void setDisabled(Boolean disabled)
  {
    this.disabled = disabled;
  }

  public String getOnclick()
  {
    return onClickItem;
  }

  public void setOnclick(String onclick)
  {
    this.onClickItem = onclick;
  }

  public String getOnchange()
  {
    return onchange;
  }

  public void setOnchange(String onchange)
  {
    this.onchange = onchange;
  }

  public String getOnkeyup()
  {
    return onkeyup;
  }

  public void setOnkeyup(String onkeyup)
  {
    this.onkeyup = onkeyup;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public Integer getTabIndex()
  {
    return tabIndex;
  }

  public void setTabIndex(Integer tabIndex)
  {
    this.tabIndex = tabIndex;
  }

  public String getOnkeypress()
  {
    return onkeypress;
  }

  public void setOnkeypress(String onkeypress)
  {
    this.onkeypress = onkeypress;
  }
}
