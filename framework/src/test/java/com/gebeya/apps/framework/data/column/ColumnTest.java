package com.gebeya.apps.framework.data.column;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ColumnTest {

    private Column column;

    private static final String COL_NAME = "names";
    private static final String COL_TYPE = ColumnTypes.TEXT;

    @Before
    public void setup() {
        column = Column.create(COL_NAME, COL_TYPE, true);
    }

    @Test
    public void testName() {
        assertEquals(column.toString(), COL_NAME);
    }

    @Test
    public void testType() {
        assertEquals(column.type, COL_TYPE);
    }

    @Test
    public void testNullable() {
        assertTrue(column.nullable);
    }

    @Test
    public void testPrimary() {
        assertFalse(column.primary);
    }

    @Test
    public void testToSql() {
        String actual = String.format(Locale.getDefault(),
                "%s %s NULL",
                COL_NAME,
                COL_TYPE
        );
        assertEquals(column.toSql().toString(), actual);
    }
}
