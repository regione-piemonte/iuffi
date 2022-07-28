package it.csi.iuffi.iuffiweb.integration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DBUpdateColumn
{
  String value();

  boolean whereClause() default false;

  String defaultUpdateValue() default "";

  String defaultInsertValue() default "";

  Class<?> type() default Object.class;
}
