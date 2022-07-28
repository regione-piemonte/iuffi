package it.csi.iuffi.iuffiweb.integration.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.exception.DatabaseAutomationException;
import it.csi.iuffi.iuffiweb.integration.IPersistent;
import it.csi.iuffi.iuffiweb.integration.annotation.DBTable;
import it.csi.iuffi.iuffiweb.integration.annotation.DBUpdateColumn;

public class TableMappingForUpdateByAnnotation
{
  protected String       tableName       = null;
  protected String       objectClassName = null;
  protected List<Column> columns         = new ArrayList<Column>();

  protected String       sqlForInsert;
  protected String       sqlForUpdate;

  public TableMappingForUpdateByAnnotation(Class<?> classObj, DBTable dbTable)
      throws DatabaseAutomationException
  {
    tableName = dbTable.value();
    objectClassName = classObj.getName();
    parseClass(classObj);
  }

  public TableMappingForUpdateByAnnotation(Class<?> classObj)
      throws DatabaseAutomationException
  {
    this(classObj, classObj.getAnnotation(DBTable.class));
  }

  protected void parseClass(Class<?> classObj)
      throws DatabaseAutomationException
  {
    Class<?> baseClass = classObj;
    while (classObj != null && classObj != Object.class
        && !classObj.getName().contains("java.lang.Object"))
    {
      for (Field f : classObj.getDeclaredFields())
      {
        DBUpdateColumn dbUpdateColumnAnnotation = (DBUpdateColumn) f
            .getAnnotation(DBUpdateColumn.class);
        if (dbUpdateColumnAnnotation != null)
        {
          processDBUpdateColumnAnnotation(f, dbUpdateColumnAnnotation,
              baseClass);
        }
      }
      classObj = classObj.getSuperclass();
    }
  }

  public MapSqlParameterSource getSqlParameterSource(IPersistent updateObject,
      boolean update) throws DatabaseAutomationException
  {
    MapSqlParameterSource source = new MapSqlParameterSource();
    for (Column column : columns)
    {
      String defaultValue = update ? column.getDefaultUpdateValue()
          : column.getDefaultInsertValue();
      if (defaultValue == null || defaultValue.length() == 0)
      {
        Method m = column.getGetter();
        Object value;
        try
        {
          value = m.invoke(updateObject);
        }
        catch (Exception e)
        {
          throw new DatabaseAutomationException(
              "Errore di reperimento dati dalla classe " + objectClassName
                  + " durante la creazione della mappa di parametri per la query",
              e);
        }
        source.addValue(column.getName(), value, getSqlType(value));
      }
    }
    return source;
  }

  public static int getSqlType(Object value)
  {
    if (value == null)
    {
      return Types.NULL;
    }
    if (value instanceof Number)
    {
      return Types.NUMERIC;
    }
    if (value instanceof String)
    {
      return Types.VARCHAR;
    }
    if (value instanceof Timestamp)
    {
      return Types.TIMESTAMP;
    }
    if (value instanceof Date)
    {
      return Types.DATE;
    }
    // Non so bene cosa capita se si arriva qua!
    return Types.OTHER;
  }

  protected void processDBUpdateColumnAnnotation(Field f,
      DBUpdateColumn annotation, Class<?> baseClass)
      throws DatabaseAutomationException
  {
    try
    {
      Method getter = baseClass.getMethod(getGetter(f.getName()));
      Column column = new Column(annotation.value(), annotation.whereClause(),
          annotation.defaultInsertValue(), annotation.defaultUpdateValue(),
          annotation.type(), getter);
      columns.add(column);
    }
    catch (Exception e)
    {
      throw new DatabaseAutomationException("Errore di analisi della classe "
          + baseClass.getName() + " per creazione query SQL di update");
    }
  }

  protected String getGetter(String fieldName)
  {
    StringBuilder sb = new StringBuilder("get").append(fieldName);
    sb.replace(3, 4, String.valueOf(Character.toUpperCase(sb.charAt(3))));
    return sb.toString();
  }

  public String getTableName()
  {
    return tableName;
  }

  protected String createSQLForUpdate() throws DatabaseAutomationException
  {
    StringBuilder sbUpdate = new StringBuilder("UPDATE\n  ").append(tableName)
        .append("\nSET\n");
    StringBuilder sbWhere = new StringBuilder();
    boolean first = true;
    for (Column column : columns)
    {
      if (!column.isWhereClause())
      {
        if (first)
        {
          first = false;
        }
        else
        {
          sbUpdate.append(",\n");
        }
        sbUpdate.append("  ").append(column.getName()).append(" = ");
        if ("".equals(column.getDefaultUpdateValue()))
        {
          sbUpdate.append(":").append(column.getName());
        }
        else
        {
          sbUpdate.append(column.getDefaultUpdateValue());
        }
      }
      else
      {
        if (sbWhere.length() != 0)
        {
          sbWhere.append("\n  AND ");
        }
        else
        {
          sbWhere.append("  ");
        }
        sbWhere.append(column.getName()).append(" = ");
        if ("".equals(column.getDefaultUpdateValue()))
        {
          sbWhere.append(":").append(column.getName());
        }
        else
        {
          sbWhere.append(column.getDefaultUpdateValue());
        }
      }
    }
    if (sbWhere.length() == 0)
    {
      throw new DatabaseAutomationException(
          "Nessuna clausola WHERE nella creazione della query di UPDATE per l'oggetto di classe "
              + objectClassName);
    }
    sbUpdate.append("\nWHERE\n");
    sbUpdate.append(sbWhere);
    return sbUpdate.toString();
  }

  public String createSQLForInsert() throws DatabaseAutomationException
  {
    StringBuilder sbNames = new StringBuilder();
    StringBuilder sbValues = new StringBuilder();
    boolean first = true;
    for (Column column : columns)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        sbNames.append(",\n");
        sbValues.append(",\n");
      }
      String value = column.getName();
      sbNames.append("  ").append(value);
      String defaultValue = column.getDefaultInsertValue();
      if ("".equals(defaultValue))
      {
        sbValues.append("  :").append(value);
      }
      else
      {
        sbValues.append("  ").append(defaultValue);
      }

    }
    if (sbValues.length() == 0)
    {
      throw new DatabaseAutomationException(
          "Nessuna clausola WHERE nella creazione della query di UPDATE per l'oggetto di classe "
              + objectClassName);
    }
    return new StringBuilder("INSERT INTO\n  ").append(tableName)
        .append("\n(\n").append(sbNames).append("\n)\nVALUES\n(\n")
        .append(sbValues)
        .append("\n)")
        .toString();
  }

  public String getSqlForUpdate() throws DatabaseAutomationException
  {

    if (sqlForUpdate == null)
    {
      sqlForUpdate = createSQLForUpdate();
    }
    return sqlForUpdate;
  }

  public String getSqlForInsert() throws DatabaseAutomationException
  {

    if (sqlForInsert == null)
    {
      sqlForInsert = createSQLForInsert();
    }
    return sqlForInsert;
  }

}
