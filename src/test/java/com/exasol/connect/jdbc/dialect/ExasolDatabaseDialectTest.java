package com.exasol.connect.jdbc.dialect;

import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.data.Timestamp;
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

  @Test
  public void shouldMapDecimalSchemaTypeToDecimalSqlType() {
    assertDecimalMapping(0, "DECIMAL(36,0)");
    assertDecimalMapping(3, "DECIMAL(36,3)");
    assertDecimalMapping(4, "DECIMAL(36,4)");
    assertDecimalMapping(5, "DECIMAL(36,5)");
  }

  @Test
  public void shouldMapDataTypes() {
    verifyDataTypeMapping("DECIMAL(3,0)", Schema.INT8_SCHEMA);
    verifyDataTypeMapping("DECIMAL(5,0)", Schema.INT16_SCHEMA);
    verifyDataTypeMapping("DECIMAL(10,0)", Schema.INT32_SCHEMA);
    verifyDataTypeMapping("DECIMAL(19,0)", Schema.INT64_SCHEMA);
    verifyDataTypeMapping("FLOAT", Schema.FLOAT32_SCHEMA);
    verifyDataTypeMapping("DOUBLE", Schema.FLOAT64_SCHEMA);
    verifyDataTypeMapping("BOOLEAN", Schema.BOOLEAN_SCHEMA);
    verifyDataTypeMapping("CLOB", Schema.STRING_SCHEMA);
    verifyDataTypeMapping("DECIMAL(36,0)", Decimal.schema(0));
    verifyDataTypeMapping("DECIMAL(36,4)", Decimal.schema(4));
    verifyDataTypeMapping("DATE", Date.SCHEMA);
    verifyDataTypeMapping("TIMESTAMP", Time.SCHEMA);
    verifyDataTypeMapping("TIMESTAMP", Timestamp.SCHEMA);
    // BLOB is not supported
    exception.expect(ConnectException.class);
    verifyDataTypeMapping("BLOB", Schema.BYTES_SCHEMA);
  }

  @Test
  public void shouldMapDateSchemaTypeToDateSqlType() {
    assertDateMapping("DATE");
  }

  @Test
  public void shouldMapTimeSchemaTypeToTimeSqlType() {
    assertTimeMapping("TIMESTAMP");
  }

  @Test
  public void shouldMapTimestampSchemaTypeToTimestampSqlType() {
    assertTimestampMapping("TIMESTAMP");
  }

}
