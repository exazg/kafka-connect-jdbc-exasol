package com.exasol.connect.jdbc.dialect;

import org.apache.kafka.common.config.AbstractConfig;

import io.confluent.connect.jdbc.dialect.DatabaseDialect;
import io.confluent.connect.jdbc.dialect.DatabaseDialectProvider.SubprotocolBasedProvider;
import io.confluent.connect.jdbc.dialect.GenericDatabaseDialect;
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

}
