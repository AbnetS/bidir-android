package com.gebeya.apps.framework.backed.api.request;

import com.gebeya.apps.framework.backend.api.request.BaseRequestWrapper;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class BaseRequestWrapperTest {

    private static final String TEST_METHOD = BaseRequestWrapper.METHOD_GET;
    private static final String TEST_URL = "http://www.example.com";
    private static final String TEST_TOKEN = "0123456789abcdef";

    private BaseRequestWrapper requestWrapper;

    @Before
    public void setUp() {
        requestWrapper = spy(BaseRequestWrapper.class);
    }

    @Test
    public void testUrl() {
        assertEquals(requestWrapper.url(TEST_URL).getUrl(), TEST_URL);
    }

    @Test
    public void testMethod() {
        assertEquals(requestWrapper.method(TEST_METHOD).getMethod(), TEST_METHOD);
    }

    @Test
    public void testToken() {
        assertEquals(requestWrapper.token(TEST_TOKEN).getToken(), TEST_TOKEN);
    }
}