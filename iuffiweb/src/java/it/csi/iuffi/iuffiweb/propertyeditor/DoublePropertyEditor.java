package it.csi.iuffi.iuffiweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;


public class DoublePropertyEditor extends PropertyEditorSupport {

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".business");

	@Override
	public String getAsText() {
		if (this.getValue() == null) {
			return "0,00";
		}
		Double d = (Double) getValue();
		return d.toString().replace(".", ",");
	}

	@Override
	public void setAsText(String text) {
		try {
			setValue(Double.parseDouble(text.replace(",", ".")));
		} catch (Exception e) {
			setValue(0d);
			logger.error(e);
		}
	}

}
