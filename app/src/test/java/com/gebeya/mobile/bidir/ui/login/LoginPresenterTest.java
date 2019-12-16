package com.gebeya.mobile.bidir.ui.login;

/**
 * Test for the {@link LoginPresenter} presenter
 */
//@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

/*    @Mock
    private UserRepoSource userRepo;
    @Mock
    private FormRepoSource formRepo;
    @Mock
    private ClientRepoSource clientRepo;
    @Mock
    private ScreeningRepoSource screeningRepo;

    private RequestState state;

    private LoginContract.Presenter presenter;

    @Mock
    private LoginContract.View view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(userRepo.login(anyString(), anyString())).thenReturn(Observable.just(true));
        when(formRepo.downloadAll()).thenReturn(Observable.just(true));
        when(clientRepo.downloadAll()).thenReturn(Observable.just(true));
        when(screeningRepo.downloadAll()).thenReturn(Observable.just(true));

        state = spy(new LoginState());

        presenter = spy(new LoginPresenter(
                userRepo,
                formRepo,
                clientRepo,
                screeningRepo,
                state,
                TestSchedulersProvider.getInstance()
        ));

        presenter.attachView(view);
    }

    @Test
    public void testAttachment() throws Exception {
        System.out.println("Should attach presenter and view");

        verify(view).attachPresenter(presenter);
        assertNotNull(presenter.getView());
    }

    @Test
    public void testLoadLogoOnStart() throws Exception {
        System.out.println("Should load the logo when the presenter starts");

        presenter.start();
        verify(view).loadLogo(any());
    }

    @Test
    public void testLoadWithNoRestore() throws Exception {
        System.out.println("Should start and not restore any states");

        state.reset();
        presenter.start();

        verify(view, never()).showLoginProgress();
        verify(presenter, never()).onLoginComplete();
        verify(presenter, never()).onLoginFailed(any());
    }

    @Test
    public void testRestoreLoadingOnStart() throws Exception {
        System.out.println("Should restore the loading when the presenter starts");

        state.setLoading();
        presenter.start();

        verify(view).showLoginProgress();
        verify(presenter, never()).onLoginComplete();
        verify(presenter, never()).onLoginFailed(any());
    }

    @Test
    public void testRestoreCompleteOnStart() throws Exception {
        System.out.println("Should restore the complete status when the presenter starts");

        state.setComplete();
        presenter.start();

        verify(presenter).onLoginComplete();
        verify(view, never()).showLoginProgress();
        verify(presenter, never()).onLoginFailed(any());
    }

    @Test
    public void testRestoreErrorOnStart() throws Exception {
        System.out.println("Should restore the error status when the presenter starts");

        final Exception exception = new Exception("");
        state.setError(exception);
        presenter.start();

        verify(presenter).onLoginFailed(exception);
        verify(presenter, never()).onLoginComplete();
        verify(view, never()).showLoginProgress();
    }

    @Test
    public void testAvoidDoubleLogin() throws Exception {
        System.out.println("Should not start the login process again if it is already in progress");

        state.setLoading();
        presenter.onLoginPressed("username", "password");

        reset(state);

        verify(view, never()).showLoginProgress();
        verify(state, never()).setLoading();
    }

    @Test
    public void testLoginMissingUsername() throws Exception {
        System.out.println("Should not login if username is missing");

        presenter.onLoginPressed("", "password");

        verify(view).showUsernameMissingError();
        verify(view, never()).showLoginProgress();
    }

    @Test
    public void testLoginMissingPassword() throws Exception {
        System.out.println("Should not login if password is missing");

        presenter.onLoginPressed("password", "");

        verify(view).showPasswordMissingError();
        verify(view, never()).showLoginProgress();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        System.out.println("Should successfully login");

        presenter.onLoginPressed("username", "password");

        verify(view).showLoginProgress();
        verify(state).setLoading();
        verify(state).setComplete();
        verify(presenter).onLoginComplete();
        verify(presenter, never()).onLoginFailed(any());
    }

    @Test
    public void testLoginSuccessDetached() throws Exception {
        System.out.println("Should not show success message when login completes while detached");

        presenter.onLoginPressed("hello", "world");
        verify(view).showLoginProgress();
        verify(state).setLoading();

        presenter.detachView();

        reset(view);

        verify(state).setComplete();
        verify(presenter).onLoginComplete();
        verify(view, never()).openHomeScreen();
        verify(view, never()).showSuccessMessage();
    }

    @Test
    public void testLoginErrorOffline() throws Exception {
        System.out.println("Should show appropriate error when offline");

        final Throwable throwable = ApiResponses.createOfflineError();
        when(userRepo.login(anyString(), anyString())).thenReturn(Observable.error(throwable));

        presenter.onLoginPressed("hello", "world");
        verify(view).showLoginProgress();
        verify(view).hideLoginProgress();
        verify(state).setLoading();
        verify(state).setError(throwable);
        verify(userRepo).logout();
        verify(presenter).onLoginFailed(throwable);
    }

    @Test
    public void testLoginErrorDetached() throws Exception {
        System.out.println("Should not show error when login fails while detached");

        final Throwable throwable = ApiResponses.createOfflineError();
        when(userRepo.login(anyString(), anyString())).thenReturn(Observable.error(throwable));

        presenter.onLoginPressed("hello", "world");
        verify(view).showLoginProgress();
        presenter.detachView();

        reset(view);

        verify(view, never()).hideLoginProgress();
        verify(state).setLoading();
        verify(state).setError(throwable);
        verify(userRepo).logout();
        verify(view, never()).showError(any());
    }

    @Test
    public void testDetachment() throws Exception {
        System.out.println("Should detach view from presenter");

        presenter.detachView();
        assertNull(presenter.getView());
    }

    @Test
    public void testHideProgressOnDetach() throws Exception {
        System.out.println("Should hide the progress during detachment");

        presenter.detachView();
        verify(view).hideLoginProgress();
    }

    @Test
    public void testHideErrorOnDetach() throws Exception {
        System.out.println("Should hide the error during detachment");

        presenter.detachView();
        verify(view).hideError();
    }*/
}