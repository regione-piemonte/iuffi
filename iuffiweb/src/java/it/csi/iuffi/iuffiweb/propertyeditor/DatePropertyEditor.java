package it.csi.iuffi.iuffiweb.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


import it.csi.iuffi.iuffiweb.util.DateUtils;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;


public class DatePropertyEditor extends PropertyEditorSupport {

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".business");

	//private SimpleDateFormat format1 = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT_STRING);
  private SimpleDateFormat format2 = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);

	@Override
	public String getAsText() {
		if (this.getValue() == null) {
			return null;
		}
		return format2.format(this.getValue());
	}

	@Override
	public void setAsText(String text) {

		try {
			setValue(format2.parse(text));
		} catch (ParseException e) {
			  setValue(null);
			  if (StringUtils.isNotBlank(text))
			    logger.error(e);
		}
	}

}
