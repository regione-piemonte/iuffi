package it.csi.iuffiauth.exception;

import it.csi.iuffiauth.util.IuffiauthConstants;

public class SecretStringNotValidException extends LocalException
{
	private static final long   serialVersionUID = 4774633548241221012L;
	public static final  int    ERRORE_SECRETSTRING_NON_VALIDA = 0x00000002;
	public static final  String MSG_ERRORE_SECRETSTRING_NON_VALIDA = IuffiauthConstants.GENERIC_ERRORS.SECRETSTRING_NON_VALIDA;

	public SecretStringNotValidException()
	{
		super(MSG_ERRORE_SECRETSTRING_NON_VALIDA, ERRORE_SECRETSTRING_NON_VALIDA);
	}

	public SecretStringNotValidException(Throwable cause)
	{
		super(MSG_ERRORE_SECRETSTRING_NON_VALIDA + " ("+cause.getMessage()+")", ERRORE_SECRETSTRING_NON_VALIDA, cause);
	}
}
