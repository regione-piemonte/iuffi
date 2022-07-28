package it.csi.iuffi.iuffiweb.dto.internal;

/**
 * Classe di utilità che rappresenta una variabile un metodo. Viene utilizzato
 * dai metodi di logging per ricevere l'elenco delle variabili di un metodo
 * quando si verifica un errore e bisogna stamparle.
 * 
 * @author Stefano Einaudi
 * @since 1.0
 */
public class LogVariable implements ILoggable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -4835724540697548318L;
  /**
   * Nome del Variabile
   */
  private String            nome;
  /**
   * Valore del Variabile
   */
  private Object            valore;

  public LogVariable(String nome, Object valore)
  {
    this.nome = nome;
    this.valore = valore;
  }

  public LogVariable(String nome, long valore)
  {
    this.nome = nome;
    this.valore = new Long(valore);
  }

  public LogVariable(String nome, int valore)
  {
    this.nome = nome;
    this.valore = new Long(valore);
  }

  public LogVariable(String nome, boolean valore)
  {
    this.nome = nome;
    this.valore = new Boolean(valore);
  }

  public LogVariable(String nome, char valore)
  {
    this.nome = nome;
    this.valore = String.valueOf(valore);
  }

  public LogVariable(String nome, double valore)
  {
    this.nome = nome;
    this.valore = new Double(valore);
  }

  public LogVariable(String nome, float valore)
  {
    this.nome = nome;
    this.valore = new Float(valore);
  }

  public String getNome()
  {
    return nome;
  }

  public Object getValore()
  {
    return valore;
  }
}
