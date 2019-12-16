package com.gebeya.mobile.bidir.ui.splash;

import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;

import org.junit.Before;

/**
 * Test for the {@link SplashFragment} View
 */
//@RunWith(RobolectricTestRunner.class)
public class SplashViewTest {

    private SplashFragment fragment;
    private SplashContract.Presenter presenter;
    private UserLocalSource local;

    @Before
    public void setUp() throws Exception {
/*        local = mock(UserLocalSource.class);
        when(local.getByType()).thenReturn(Maybe.just(new User()));

        presenter = spy(new SplashPresenter(local));
        fragment = spy(SplashFragment.newInstance());
        presenter.attachView(fragment);

        reset(presenter);*/
    }

/*


    @Test
    public void testAttachment() throws Exception {
        System.out.println("Should attach view to presenter when starting up");

        startFragment(fragment);
        verify(presenter).attachView(fragment);
    }

    @Test
    public void testPresenterStart() throws Exception {
        System.out.println("Should start presenter when fragment starts");

        startFragment(fragment);
        verify(presenter).start();
    }

    @Test
    public void testSkipToHomeScreen() throws Exception {
        System.out.println("Should open the home screen if user already logged in");

        when(local.getByType()).thenReturn(Maybe.just(new User()));

        startFragment(fragment);

        verify(presenter).start();
        verify(fragment).openHomeScreen();
        verify(fragment).close();
        verify(fragment, never()).loadLogo(anyString());
    }

    @Test
    public void testLoadLogo() throws Exception {
        System.out.println("Should load the logo when the user is not logged in");

        when(local.getByType()).thenReturn(Maybe.empty());

        startFragment(fragment);
        verify(presenter).start();
        verify(fragment, never()).openHomeScreen();
        verify(fragment, never()).close();
        verify(fragment).loadLogo(anyString());
    }

    @Test
    public void testDetachment() throws Exception {
        System.out.println("Should detach view from presenter on stopping");

        startFragment(fragment);
        fragment.onStop();
        verify(presenter).detachView();
    }*/
}