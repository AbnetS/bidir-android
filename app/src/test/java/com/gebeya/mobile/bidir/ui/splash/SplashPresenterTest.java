package com.gebeya.mobile.bidir.ui.splash;

import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocalTest;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import toothpick.Toothpick;
import toothpick.config.Module;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link SplashPresenter} Presenter
 */
@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {

    private SplashContract.Presenter presenter;

    private SplashContract.View view;
    private UserLocalSource userLocal;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = UserLocalTest.createUser();

        userLocal = mock(UserLocalSource.class);
        when(userLocal.first()).thenReturn(Observable.just(new Data<>(user)));

        Toothpick.openScope(Scopes.SCOPE_LOCAL_SOURCES)
                .installModules(new Module() {
                    {
                        bind(UserLocalSource.class).toInstance(userLocal);
                    }
                });

        view = mock(SplashContract.View.class);

        presenter = new SplashPresenter();
        presenter.attachView(view);
    }

    @Test
    public void testAttach() throws Exception {
        System.out.println("Should attach view and presenter correctly");
        // GIVEN, WHEN, THEN
        assertNotNull(presenter.getView());
    }

    @Test
    public void testSkipToHomeScreenWhenLoggedIn() throws  Exception {
        System.out.println("Should skip to the home screen if the user is already logged in");
        // GIVEN, WHEN
        presenter.start();
        // THEN
        verify(userLocal).first();
        verify(view).openHomeScreen();
        verify(view).close();
        verify(view, never()).loadLogo(anyString());
        verify(view, never()).startLogoDisplayTimeout();
    }

    @Test
    public void testNoSkipToHoeScreenWhenLoggedOut() throws Exception {
        System.out.println("Should load the splash screen when the user is not logged in");
        // GIVEN
        when(userLocal.first()).thenReturn(Observable.just(new Data<>(null)));
        // WHEN
        presenter.start();
        // THEN
        verify(userLocal).first();
        verify(view).loadLogo(anyString());
        verify(view, never()).openHomeScreen();
        verify(view, never()).close();
    }

    @Test
    public void testStartLogoDisplayTimeout() throws Exception {
        System.out.println("Should start the logo display timeout when the logo finishes loading");
        // GIVEN, WHEN
        presenter.onLogoLoaded();
        // THEN
        verify(view).startLogoDisplayTimeout();
    }

    @Test
    public void testNoStartTimeoutDetached() throws Exception {
        System.out.println("Should not start the timeout when the logo finishes loading while view detached");
        // GIVEN, WHEN
        presenter.detachView();
        presenter.onLogoLoaded();
        // THEN
        verify(view, never()).startLogoDisplayTimeout();
    }

    @Test
    public void testStopTimeoutOnDetach() throws  Exception {
        System.out.println("Should stop/cancel the logo display timeout when the view detaches");
        // GIVEN, WHEN
       presenter.detachView();
        // THEN
        verify(view).stopLogoDisplayTimeout();
    }

    @Test
    public void testOpenLoginScreenOnDisplayTimeout() throws Exception {
        System.out.println("Should open the login screen when the logo display times out");
        // GIVEN, WHEN
        presenter.onLogoDisplayTimeout();
        // THEN
        verify(view).openLoginScreen();
        verify(view).close();
    }

    @Test
    public void testNoOpenLoginOnDisplayTimeoutDetached() throws Exception {
        System.out.println("Should not open login screen when logo display times out while view detached");
        // GIVEN, WHEN
        presenter.detachView();
        presenter.onLogoDisplayTimeout();
        // THEN
        verify(view, never()).openLoginScreen();
        verify(view, never()).close();
    }

    @Test
    public void testDetach() throws Exception {
        System.out.println("Should detach view and presenter correctly");
        // GIVEN, WHEN
        presenter.detachView();
        // THEN
        assertNull(presenter.getView());
    }

    @After
    public void tearDown() throws Exception {
        Toothpick.closeScope(Scopes.SCOPE_LOCAL_SOURCES);
        reset(userLocal, view);
    }
}