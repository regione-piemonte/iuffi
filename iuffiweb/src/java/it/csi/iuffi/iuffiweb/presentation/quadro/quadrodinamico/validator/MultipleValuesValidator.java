package it.csi.iuffi.iuffiweb.presentation.quadro.quadrodinamico.validator;

import java.util.HashMap;
import java.util.Map;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.ElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class MultipleValuesValidator
{
  public static final Map<String, MultipleValuesValidator> MAP_VALIDATORS = new HashMap<String, MultipleValuesValidator>();

  public abstract String[] validate(ElementoQuadroDTO elemento, String[] valori,
      String nomeParametro, Errors errors);

  static
  {
    addStandarValidator();
  }

  private static void addStandarValidator()
  {
    addToValidatorMap(new MultipleValuesValidator()
    {
      @Override
      public String[] validate(ElementoQuadroDTO elemento, String[] valori,
          String nomeParametro, Errors errors)
      {
        if (elemento.isObbligatorio())
        {
          errors.validateMandatory(valori, nomeParametro);
        }
        return valori;
      }
    }, IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.CBT,
        IuffiConstants.QUADRO_DINAMICO.TIPO_DATO.LST);
  }

  protected static void addToValidatorMap(MultipleValuesValidator validator,
      String... codes)
  {
    for (String code : codes)
    {
      MAP_VALIDATORS.put(code, validator);
    }
  }
}
