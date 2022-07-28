package it.csi.iuffi.iuffiweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;


public class IntegerPropertyEditor extends PropertyEditorSupport {

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".business");

	@Override
	public String getAsText() {
		if (this.getValue() == null) {
			return "0";
		}
		Integer d = (Integer) getValue();
		return d.toString().replace(".", ",");
	}

	@Override
	public void setAsText(String text) {
	  try {
	    setValue(Integer.parseInt(text));
	  } catch (Exception e) {
	      setValue(0);
	      if (StringUtils.isNotBlank(text))
	        logger.error(e);
	  }
	}

}
