package com.exasol.connect.jdbc.dialect;

import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.errors.ConnectException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.confluent.connect.jdbc.dialect.BaseDialectTest;

public class ExasolDatabaseDialectTest extends BaseDialectTest<ExasolDatabaseDialect> {

  @Override
  protected ExasolDatabaseDialect createDialect() {
    return new ExasolDatabaseDialect(sourceConfigWithUrl("jdbc:exa://something"));
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void shouldMapPrimitiveSchemaTypeToSqlTypes() {
    assertPrimitiveMapping(Type.INT8, "DECIMAL(3,0)");
    assertPrimitiveMapping(Type.INT16, "DECIMAL(5,0)");
    assertPrimitiveMapping(Type.INT32, "DECIMAL(10,0)");
    assertPrimitiveMapping(Type.INT64, "DECIMAL(19,0)");
    assertPrimitiveMapping(Type.FLOAT32, "FLOAT");
    assertPrimitiveMapping(Type.FLOAT64, "DOUBLE");
    assertPrimitiveMapping(Type.BOOLEAN, "BOOLEAN");
    assertPrimitiveMapping(Type.STRING, "CLOB");
    // BLOB is not supported
    exception.expect(ConnectException.class);
    assertPrimitiveMapping(Type.BYTES, "BLOB");
  }

}
