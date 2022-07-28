package it.csi.iuffi.iuffiweb.presentation.quadro.quadrodinamico.validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.csi.iuffi.iuffiweb.business.IQuadroDinamicoEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.ElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class SingleValueValidator
{
  public static final Map<String, SingleValueValidator> MAP_VALIDATORS = new HashMap<String, SingleValueValidator>();

  public String validate(ElementoQuadroDTO elemento, String valore,
      String nomeParametro, Errors errors, IQuadroDinamicoEJB quadroDinamicoEJB)
      throws ApplicationException, InternalUnexpectedException
  {
    String value = validateElement(elemento, valore, nomeParametro, errors);
    final String istruzioneSqlControlli = elemento.getIstruzioneSqlControlli();
    if (istruzioneSqlControlli != null)
    {
      if (!errors.hasError(nomeParametro))
      {
        /*
         * Non ci sono errori su questo campo ==> posso richiamare la query di
         * validazione registrata su db. Richiamo solo se il campo è valorizzato
         * o se è null ma l'elemento non è obbligatorio
         */
        if (value != null || (value == null && !elemento.isObbligatorio()))
        {
          String errorMessage = quadroDinamicoEJB
              .executeIstruzioneControlli(istruzioneSqlControlli, value);
          if (errorMessage != null)
          {
            errors.addError(nomeParametro, errorMessage);
            return null;
          }
        }
      }
    }
    return value;
  }

  public abstract String validateElement(ElementoQuadroDTO elemento,
      String valore, String nomeParametro, Errors errors)
      throws ApplicationException;

  static
  {
    addValidatorDTA();
    addValidatorDTF();
    addValidatorDTQ();
    addGenericTextValidator();
    addGenericUppercaseTextValidator();
    addValidatorPredefinedSingleSelection();
    addValidatorNUM();
    addValidatorNUQ();
    addValidatorNU0();
    addValidatorPCT();
    addValidatorANN();
  }

  private static void addValidatorNU0()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors) throws ApplicationException
      {
        int lunghezza = validaLunghezzaObbligatoriaEPositiva(elemento);
        int precisione = IuffiUtils.NUMBERS.nvl(elemento.getPrecisione(),
            Integer.valueOf(0));
        BigDecimal bdMax = getMaxBigDecimal(lunghezza, precisione);
        BigDecimal bdValue = null;
        if (elemento.isObbligatorio())
        {
          bdValue = errors.validateMandatoryBigDecimalInRange(valore,
              nomeParametro, precisione, BigDecimal.ZERO, bdMax);
        }
        else
        {
          bdValue = errors.validateOptionalBigDecimalInRange(valore,
              nomeParametro, precisione, BigDecimal.ZERO, bdMax);
        }
        if (bdValue != null)
        {
          return bdValue.toString();
        }
        else
        {
          // Non è un numero valido ==> quindi non serve tenerlo in memoria
          return null;
        }
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NU0,
        IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.EUR);
  }

  private static void addValidatorANN()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors) throws ApplicationException
      {
        int lunghezza = 4;
        int precisione = 0;
        BigDecimal bdMax = getMaxBigDecimal(lunghezza, precisione);
        BigDecimal bdValue = null;
        if (elemento.isObbligatorio())
        {
          bdValue = errors.validateMandatoryBigDecimalInRange(valore,
              nomeParametro, precisione, new BigDecimal(1000), bdMax);
        }
        else
        {
          bdValue = errors.validateOptionalBigDecimalInRange(valore,
              nomeParametro, precisione, new BigDecimal(1000), bdMax);
        }
        if (bdValue != null)
        {
          return bdValue.toString();
        }
        else
        {
          // Non è un numero valido ==> quindi non serve tenerlo in memoria
          return null;
        }
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.ANN);
  }

  private static void addValidatorNUQ()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors) throws ApplicationException
      {
        int lunghezza = validaLunghezzaObbligatoriaEPositiva(elemento);
        int precisione = IuffiUtils.NUMBERS.nvl(elemento.getPrecisione(),
            Integer.valueOf(0));
        BigDecimal bdMax = getMaxBigDecimal(lunghezza, precisione);
        BigDecimal bdMin = bdMax.negate();
        BigDecimal bdValue = null;
        if (elemento.isObbligatorio())
        {
          bdValue = errors.validateMandatoryBigDecimalInRange(valore,
              nomeParametro, precisione, bdMin, bdMax);
        }
        else
        {
          bdValue = errors.validateOptionalBigDecimalInRange(valore,
              nomeParametro, precisione, bdMin, bdMax);
        }
        if (bdValue != null)
        {
          return bdValue.toString();
        }
        else
        {
          // Non è un numero valido ==> quindi non serve tenerlo in memoria
          return null;
        }
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NUQ);
  }

  private static void addValidatorNUM()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors) throws ApplicationException
      {
        int lunghezza = validaLunghezzaObbligatoriaEPositiva(elemento);
        int precisione = IuffiUtils.NUMBERS.nvl(elemento.getPrecisione(),
            Integer.valueOf(0));
        BigDecimal bdMax = getMaxBigDecimal(lunghezza, precisione);
        BigDecimal bdMin = getMinBigDecimal(precisione);
        BigDecimal bdValue = null;
        if (elemento.isObbligatorio())
        {
          bdValue = errors.validateMandatoryBigDecimalInRange(valore,
              nomeParametro, precisione, bdMin, bdMax);
        }
        else
        {
          bdValue = errors.validateOptionalBigDecimalInRange(valore,
              nomeParametro, precisione, bdMin, bdMax);
        }
        if (bdValue != null)
        {
          return bdValue.toString();
        }
        else
        {
          // Non è un numero valido ==> quindi non serve tenerlo in memoria
          return null;
        }
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.NUM);
  }

  private static void addValidatorPCT()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors) throws ApplicationException
      {
        int lunghezza = validaLunghezzaObbligatoriaEPositiva(elemento);
        int precisione = IuffiUtils.NUMBERS.nvl(elemento.getPrecisione(),
            Integer.valueOf(0));
        BigDecimal bdMax = getMaxBigDecimal(lunghezza, precisione);
        if (bdMax.compareTo(IuffiConstants.MAX.PERCENTUALE) > 1)
        {
          bdMax = IuffiConstants.MAX.PERCENTUALE;
        }
        BigDecimal bdMin = getMinBigDecimal(precisione);
        BigDecimal bdValue = null;
        if (elemento.isObbligatorio())
        {
          bdValue = errors.validateMandatoryBigDecimalInRange(valore,
              nomeParametro, 2, bdMin, bdMax);
        }
        else
        {
          bdValue = errors.validateOptionalBigDecimalInRange(valore,
              nomeParametro, 2, bdMin, bdMax);
        }
        if (bdValue != null)
        {
          return bdValue.toString();
        }
        else
        {
          // Non è un numero valido ==> quindi non serve tenerlo in memoria
          return null;
        }
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.PCT);
  }

  protected static BigDecimal getMaxBigDecimal(int lunghezza, int precisione)
  {
    StringBuilder sb = new StringBuilder();
    int r = lunghezza - precisione;
    for (int i = 0; i < r; ++i)
    {
      sb.append('9');
    }
    if (precisione > 0)
    {
      sb.append(".");
      for (int i = 0; i < precisione; ++i)
      {
        sb.append('9');
      }
    }
    return new BigDecimal(sb.toString()).setScale(precisione);
  }

  protected static BigDecimal getMinBigDecimal(int precisione)
  {
    StringBuilder sb = new StringBuilder();
    sb.append('0');
    if (precisione > 0)
    {
      sb.append(".");
      int numZeri = precisione - 1;
      for (int i = 0; i < numZeri; ++i)
      {
        sb.append('0');
      }
      sb.append('1');
    }
    return new BigDecimal(sb.toString()).setScale(precisione);
  }

  private static void addValidatorPredefinedSingleSelection()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatory(valore, nomeParametro);
        }
        return valore;
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.CMB,
        IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.RBT);
  }

  private static void addGenericTextValidator()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatory(valore, nomeParametro);
        }
        return valore;
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.STR,
        IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.MST);
  }

  private static void addGenericUppercaseTextValidator()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatory(valore, nomeParametro);
        }
        return IuffiUtils.STRING.toUpperCase(valore);
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.STM);
  }

  private static void addValidatorDTA()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatoryDateInRange(valore, nomeParametro, null,
              IuffiUtils.DATE.getCurrentDateNoTime(), true, true);
        }
        else
        {
          errors.validateOptionalDateInRange(valore, nomeParametro, null,
              IuffiUtils.DATE.getCurrentDateNoTime(), true, true);
        }
        return valore;
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.DTA);

  }

  private static void addValidatorDTF()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatoryDateInRange(valore, nomeParametro,
              IuffiUtils.DATE.getCurrentDateNoTime(), null, true, true);
        }
        else
        {
          errors.validateOptionalDateInRange(valore, nomeParametro,
              IuffiUtils.DATE.getCurrentDateNoTime(), new Date(), true,
              true);
        }
        return valore;
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.DTF);

  }

  private static void addValidatorDTQ()
  {
    addToValidatorMap(new SingleValueValidator()
    {
      @Override
      public String validateElement(ElementoQuadroDTO elemento, String valore,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatoryDate(valore, nomeParametro, true);
        }
        else
        {
          errors.validateOptionalDate(valore, nomeParametro, true);
        }
        return valore;
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.DTQ);

  }

  protected static void addToValidatorMap(SingleValueValidator validator,
      String... codes)
  {
    for (String code : codes)
    {
      MAP_VALIDATORS.put(code, validator);
    }
  }

  protected int validaLunghezzaObbligatoriaEPositiva(ElementoQuadroDTO elemento)
      throws ApplicationException
  {
    Integer lunghezza = elemento.getLunghezza();
    if (lunghezza == null)
    {
      throw new ApplicationException(
          "Si è verificato un errore grave nella configurazione del quadro dinamico. Contattare l'assistenza tecnica comunicando il seguente messaggio: Campo lunghezza dell'elemento #"
              + elemento.getIdElementoQuadro() + " non valorizzato!");
    }
    if (lunghezza <= 0)
    {
      throw new ApplicationException(
          "Si è verificato un errore grave nella configurazione del quadro dinamico. Contattare l'assistenza tecnica comunicando il seguente messaggio: Campo lunghezza dell'elemento #"
              + elemento.getIdElementoQuadro() + " è zero o negativo!");
    }
    return lunghezza;
  }
}
