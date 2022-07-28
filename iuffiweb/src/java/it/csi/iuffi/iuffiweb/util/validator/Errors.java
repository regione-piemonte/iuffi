package it.csi.iuffi.iuffiweb.util.validator;

import org.springframework.ui.Model;

public class Errors extends MandatoryValidator
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3770166489356520835L;

  public boolean addToModelIfNotEmpty(Model model)
  {
    if (!isEmpty())
    {
      model.addAttribute("errors", this);
      return true;
    }
    else
    {
      return false;
    }
  }

  public void addToModel(Model model)
  {
    model.addAttribute("errors", this);
  }

  public boolean hasError(String fieldName)
  {
    if (!isEmpty())
    {
      return get(fieldName) != null;
    }
    else
    {
      return false;
    }
  }

}
