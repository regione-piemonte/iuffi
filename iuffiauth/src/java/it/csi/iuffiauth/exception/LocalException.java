package it.csi.iuffiauth.exception;


public class LocalException extends Exception
{
	private static final long serialVersionUID = -966406311072642768L;
	public static final int   GENERIC_ERROR    = 0x00000000;
	private int               errorCode;

	public LocalException(String message)
	{
		this(message, GENERIC_ERROR);
	}

	public LocalException(String message, int errorCode)
	{
		super(message);
		this.errorCode = errorCode;
	}

	public LocalException(String message, Throwable cause)
	{
		this(message, GENERIC_ERROR, cause);
	}

	public LocalException(String message, int errorCode, Throwable cause)
	{
		super(message, cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}
}
