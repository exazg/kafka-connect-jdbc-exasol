package com.exasol.connect.jdbc.dialect;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.data.Timestamp;

import io.confluent.connect.jdbc.dialect.DatabaseDialect;
import io.confluent.connect.jdbc.dialect.DatabaseDialectProvider.SubprotocolBasedProvider;
import io.confluent.connect.jdbc.dialect.GenericDatabaseDialect;
import io.confluent.connect.jdbc.sink.metadata.SinkRecordField;
import io.confluent.connect.jdbc.util.IdentifierRules;

/**
 * A {@link DatabaseDialect} for Exasol.
 */
public class ExasolDatabaseDialect extends GenericDatabaseDialect {

  /**
   * The provider for {@link ExasolDatabaseDialect}.
   */
  public static class Provider extends SubprotocolBasedProvider {
    public Provider() {
      super(ExasolDatabaseDialect.class.getSimpleName(), "exasol");
    }

    @Override
    public DatabaseDialect create(AbstractConfig config) {
      return new ExasolDatabaseDialect(config);
    }
  }

  /**
   * Create a new dialect instance with the given connector configuration.
   *
   * @param config the connector configuration; may not be null
   */
  public ExasolDatabaseDialect(AbstractConfig config) {
    super(config, new IdentifierRules(".", "\"", "\""));
  }

  @Override
  protected String currentTimestampDatabaseQuery() {
    return "SELECT CURRENT_TIMESTAMP";
  }

  @Override
  protected String checkConnectionQuery() {
    return "SELECT 1";
  }

  @Override
  protected String getSqlType(SinkRecordField field) {
    if (field.schemaName() != null) {
      switch (field.schemaName()) {
        case Decimal.LOGICAL_NAME:
          return "DECIMAL(36," + field.schemaParameters().get(Decimal.SCALE_FIELD) + ")";
        case Date.LOGICAL_NAME:
          return "DATE";
        case Time.LOGICAL_NAME:
          return "TIMESTAMP";
        case Timestamp.LOGICAL_NAME:
          return "TIMESTAMP";
        default:
          // fall through to normal types
      }
    }
    switch (field.schemaType()) {
      case INT8:
        return "DECIMAL(3,0)";
      case INT16:
        return "DECIMAL(5,0)";
      case INT32:
        return "DECIMAL(10,0)";
      case INT64:
        return "DECIMAL(19,0)";
      case FLOAT32:
        return "FLOAT";
      case FLOAT64:
        return "DOUBLE";
      case BOOLEAN:
        return "BOOLEAN";
      case STRING:
        return "CLOB";
      // case BYTES:
      // BLOB is not supported
      default:
        return super.getSqlType(field);
    }
  }

}
