package com.gebeya.apps.framework.data.prefs;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class BasePrefsTest {

    private static final String KEY_TEST = "TEST";
    private static final String KEY_UNKNOWN = "UNKNOWN";

    private static final int INT_VALUE = 153;
    private static final boolean BOOLEAN_VALUE = true;
    private static final String STRING_VALUE = "STRING";
    private static final float FLOAT_VALUE = 35.8F;
    private static final long LONG_VALUE = 1267L;

    private Prefs prefs;

    @Before
    public void setUp() {
        prefs = BasePrefs.getInstance();
        prefs.open().clear().close();
    }

    @Test
    public void testInt() {
        prefs.open().put(KEY_TEST, INT_VALUE).close();
        assertEquals(INT_VALUE, prefs.getInt(KEY_TEST));
        assertEquals(0, prefs.getInt(KEY_UNKNOWN));
    }

    @Test
    public void testBool() {
        prefs.open().put(KEY_TEST, BOOLEAN_VALUE).close();
        assertEquals(BOOLEAN_VALUE, prefs.getBool(KEY_TEST));
        assertEquals(false, prefs.getBool(KEY_UNKNOWN));
    }

    @Test
    public void testString() {
        prefs.open().put(KEY_TEST, STRING_VALUE).close();
        assertEquals(STRING_VALUE, prefs.getString(KEY_TEST));
        assertNull(prefs.getString(KEY_UNKNOWN));
    }

    @Test
    public void testFloat() {
        prefs.open().put(KEY_TEST, FLOAT_VALUE).close();
        assertEquals(FLOAT_VALUE, prefs.getFloat(KEY_TEST));
        assertEquals(0.0F, prefs.getFloat(KEY_UNKNOWN));
    }

    @Test
    public void testLong() {
        prefs.open().put(KEY_TEST, LONG_VALUE).close();
        assertEquals(LONG_VALUE, prefs.getLong(KEY_TEST));
        assertEquals(0L, prefs.getLong(KEY_UNKNOWN));
    }

    @After
    public void cleanUp() {
        prefs.open().clear().close();
    }
}
