package com.gebeya.mobile.bidir.impl.util.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the {@link BaseRequestState} common state object.
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseRequestStateTest {

    private RequestState state;

    @Before
    public void setUp() throws Exception {
        state = new TestRequestState();
    }

    @Test
    public void testIdle() throws Exception {
        System.out.println("Should unset state upon creating a new test object");

        assertNull(state.getState());
        assertFalse(state.loading());
        assertFalse(state.error());
        assertFalse(state.complete());
    }

    @Test
    public void testLoading() throws Exception {
        System.out.println("Should have a state of loading when changed to loading");

        state.setLoading();

        assertThat(state.getState(), is(RequestState.State.LOADING));
        assertTrue(state.loading());
        assertFalse(state.error());
        assertFalse(state.complete());
    }

    @Test
    public void testComplete() throws Exception {
        System.out.println("Should have a state of completed when changed to completed");

        state.setComplete();

        assertThat(state.getState(), is(RequestState.State.COMPLETED));
        assertTrue(state.complete());
        assertFalse(state.loading());
        assertFalse(state.error());
    }

    @Test
    public void testError() throws Exception {
        System.out.println("Should have a state of error along with error object when changed to error");

        final String message = "Error test message - could not open app";
        state.setError(new Throwable(message));

        assertThat(state.getState(), is(RequestState.State.ERROR));
        assertFalse(state.complete());
        assertFalse(state.loading());
        assertTrue(state.error());
        assertNotNull(state.getError());
        assertThat(state.getError().getMessage(), is(message));
    }

    @Test
    public void testReset() throws Exception {
        System.out.println("Should reset a state to unset");

        state.setComplete();
        assertTrue(state.complete());
        state.reset();
        assertNull(state.getState());
        assertFalse(state.loading());
        assertFalse(state.error());
        assertFalse(state.complete());
    }
}
