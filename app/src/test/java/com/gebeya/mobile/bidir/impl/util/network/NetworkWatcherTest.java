package com.gebeya.mobile.bidir.impl.util.network;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link NetworkWatcher} watcher
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkWatcherTest {

    private NetworkWatcher watcher;

    @Before
    public void setUp() throws  Exception {
        watcher = spy(NetworkWatcher.class);
    }

    @Test
    public void testCheckConnectionIsTrue() throws Exception {
        System.out.println("Should check the connection and return true");
        TestObserver<Boolean> observer = new TestObserver<>();

        when(watcher.connected()).thenReturn(Observable.just(true));
        watcher.connected().subscribe(observer);

        observer.assertSubscribed();
        observer.assertNoErrors();
        observer.assertComplete();

        observer.assertValue(true);
    }

    @Test
    public void testCheckConnectionIsFalse() throws Exception {
        System.out.println("Should check the connection and return false");
        TestObserver<Boolean> observer = new TestObserver<>();

        when(watcher.connected()).thenReturn(Observable.just(false));
        watcher.connected().subscribe(observer);

        observer.assertSubscribed();
        observer.assertNoErrors();
        observer.assertComplete();

        observer.assertValue(false);
    }

    @Test
    public void testContinueWhenConnected() throws Exception {
        System.out.println("Should continue with normal operations if Internet connection is available");

        final JsonObject object = new JsonObject();
        object.addProperty("test", true);
        final Observable<JsonObject> observable = Observable.just(object);

        TestObserver<JsonObject> observer = new TestObserver<>();

        observable.subscribe(observer);
        observer.assertSubscribed();
        observer.assertNoErrors();
        observer.assertComplete();

        List<JsonObject> objects = observer.values();
        assertThat(objects.size(), is(1));
        JsonObject actual = objects.get(0);
        assertThat(actual.get("test").getAsBoolean(), is(true));
    }
}