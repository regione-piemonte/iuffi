package it.csi.iuffi.iuffiweb.exception;

public class IuffiPermissionException extends Exception
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6672364238144307274L;

  public enum ExceptionType
  {
    PROCEDIMENTO_OGGETTO_NON_TROVATO(
        "Impossibile trovare l'oggetto indicato. E' possibile che sia stato cancellato da qualche altro utente"), PROCEDIMENTO_OGGETTO_CHIUSO(
            "Operazione non permessa: l'oggetto selezionato è chiuso"), PROCEDIMENTO_OGGETTO_APERTO(
                "Operazione non permessa: l'oggetto selezionato non è chiuso"), PROCEDIMENTO_CHIUSO(
                    "Operazione non permessa: il procedimento selezionato è chiuso"), OGGETTO_BANDO_NON_ATTIVO(
                        "Operazione non permessa: l'oggetto del bando non è attivo"), NOTIFICHE_BLOCCANTI(
                            "Operazione non permessa: sono state trovate delle notifiche bloccanti"), NOTIFICHE_GRAVI(
                                "Operazione non permessa: sono state trovate delle notifiche gravi"), PROCEDIMENTO_NON_IN_BOZZA(
                                    "Operazione non permessa: il procedimento non è nello stato di bozza");

    private String errorMessage;

    ExceptionType(String errorMessage)
    {
      this.errorMessage = errorMessage;
    }

    public String getErrorMessage()
    {
      return errorMessage;
    }
  }

  protected ExceptionType type;

  public IuffiPermissionException(ExceptionType type)
  {
    this.type = type;
  }

  public ExceptionType getType()
  {
    return type;
  }

  public String getMessage()
  {
    return type.getErrorMessage();
  }
}
