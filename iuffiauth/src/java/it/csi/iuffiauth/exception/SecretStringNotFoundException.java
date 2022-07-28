package it.csi.iuffiauth.exception;

import it.csi.iuffiauth.util.IuffiauthConstants;

public class SecretStringNotFoundException extends LocalException
{
	private static final long   serialVersionUID = 4774633548241221012L;
	public static final  int    ERRORE_SECRETSTRING_NON_TROVATA = 0x00000001;
	public static final  String MSG_ERRORE_SECRETSTRING_NON_TROVATA = IuffiauthConstants.GENERIC_ERRORS.SECRETSTRING_NON_TROVATA;

	public SecretStringNotFoundException()
	{
		super(MSG_ERRORE_SECRETSTRING_NON_TROVATA, ERRORE_SECRETSTRING_NON_TROVATA);
	}

	public SecretStringNotFoundException(Throwable cause)
	{
		super(MSG_ERRORE_SECRETSTRING_NON_TROVATA + " ("+cause.getMessage()+")", ERRORE_SECRETSTRING_NON_TROVATA, cause);
	}
}
