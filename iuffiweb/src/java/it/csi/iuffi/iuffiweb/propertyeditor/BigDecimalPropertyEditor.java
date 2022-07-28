package it.csi.iuffi.iuffiweb.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;


public class BigDecimalPropertyEditor extends PropertyEditorSupport {

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".business");

	@Override
	public String getAsText() {
		if (this.getValue() == null) {
			return "0,00";
		}
		BigDecimal d = (BigDecimal) getValue();
		return d.toString().replace(".", ",");
	}

	@Override
	public void setAsText(String text) {
		try {
			setValue((text!=null && text.trim().length()>0)? new BigDecimal(text.replace(",", ".")) : null);
		} catch (Exception e) {
			setValue(0d);
			logger.error(e);
		}
	}

}
