package com.gebeya.apps.framework.data.schema;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;

public class SchemaTest {

    private TestSchema testSchema;

    @Before
    public void setup() {
        testSchema = new TestSchema();
    }

    @Test
    public void testTable() {
        assertEquals(TestSchema.TABLE_NAME, testSchema.TABLE);
    }

    @Test
    public void testCreate() {
        String expected = String.format(Locale.getDefault(),
                "CREATE TABLE IF NOT EXISTS %s (\n" +
                        "\t%s,\n" +
                        "\t%s,\n" +
                        "\t%s,\n" +
                        "\t%s\n" +
                        ")",
                TestSchema.TABLE_NAME,
                BaseSchema.COL_ID.toSql(),
                TestSchema.COL_NAME.toSql(),
                TestSchema.COL_AGE.toSql(),
                TestSchema.COL_SALARY.toSql()
        );
        String actual = testSchema.CREATE;
        assertEquals(expected, actual);
    }

    @Test
    public void testDelete() {
        String expected = "DROP TABLE IF EXISTS " + testSchema.TABLE;
        String actual = testSchema.DELETE;
        assertEquals(expected, actual);
    }
}
