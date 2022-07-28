package it.csi.iuffi.iuffiweb.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE, ElementType.METHOD })
public @interface IuffiSecurity
{
  public enum Controllo
  {
    NESSUNO, DEFAULT, PROCEDIMENTO, PROCEDIMENTO_OGGETTO, PROCEDIMENTO_OGGETTO_CHIUSO
  }

  public enum TipoMapping
  {
    USECASE, // Il mapping con il CU è statico ed è inserito nell'annotation, il
             // value contiene il codice del CU a cui è associata la controller
    QUADRO_DINAMICO // Il mapping con il CU è quello dei quadri dinamici, viene
                    // ricavato manualmente dall'url invocata
  }

  public String value(); // CU IUFFIWEB

  public Controllo controllo(); // Eventuale controllo da eseguire dopo la
                                // verifica iride

  public String errorPage() default ""; // Eventuale pagina di errore per
                                        // gestione problemi particolari (es
                                        // popup)

  public TipoMapping tipoMapping() default TipoMapping.USECASE; // Tipo di
                                                                // modalità con
                                                                // cui avviene
                                                                // il mapping
                                                                // tra la
                                                                // controller e
                                                                // il caso d'uso
                                                                // vedi l'enum
                                                                // TipoMapping
}
