package it.csi.iuffi.iuffiweb.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.DateUtils;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;


/**
 * @author paolo.loddo
 */
public class DateTimePropertyEditor extends PropertyEditorSupport {

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".business");

	private SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);

	@Override
	public String getAsText() {
		if (this.getValue() == null) {
			return null;
		}
		return format.format(this.getValue());
	}

	@Override
	public void setAsText(String text) {

		try {
			setValue(format.parse(text));
		} catch (ParseException e) {
			setValue(null);
			logger.error(e);
		}
	}

}
