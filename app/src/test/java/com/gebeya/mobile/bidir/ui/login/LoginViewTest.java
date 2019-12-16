package com.gebeya.mobile.bidir.ui.login;

/**
 * Test for the {@link LoginFragment} view implementation
 */
//@RunWith(RobolectricTestRunner.class)
public class LoginViewTest {

/*    private LoginFragment fragment;
    private LoginContract.Presenter presenter;

    private static final String USERNAME = "saladthieves";
    private static final String PASSWORD = "something_here";

    @Mock
    private UserRepoSource userRepo;
    @Mock
    private FormRepoSource formRepo;
    @Mock
    private ClientRepoSource clientRepo;
    @Mock
    private ScreeningRepoSource screeningRepo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(userRepo.login(USERNAME, PASSWORD)).thenReturn(Observable.just(true));
        when(formRepo.downloadAll()).thenReturn(Observable.just(true));
        when(clientRepo.downloadAll()).thenReturn(Observable.just(true));
        when(screeningRepo.downloadAll()).thenReturn(Observable.just(true));

        presenter = spy(new LoginPresenter(
                userRepo,
                formRepo,
                clientRepo,
                screeningRepo,
                new LoginState(),
                TestSchedulersProvider.getInstance()
        ));

        fragment = spy(LoginFragment.newInstance());
        presenter.attachView(fragment);

        reset(presenter);
    }

    @Test
    public void testAttachment() throws Exception {
        System.out.println("Should attach view to presenter when starting up");

        startFragment(fragment);
        verify(presenter).attachView(fragment);
    }

    @Test
    public void testPresenterStart() {
        System.out.println("Should start presenter when fragment starts");

        startFragment(fragment);
        verify(presenter).start();
    }

    @Test
    public void testLoginLogoLoad() throws Exception {
        System.out.println("Should load the login logo when the fragment starts");

        startFragment(fragment);
        verify(fragment).loadLogo(any());
    }

    @Test
    public void testOnLoginClick() throws Exception {
        System.out.println("Should call the onLoginPressed method when login button is clicked");

        startFragment(fragment);
        View root = fragment.getView();
        assertNotNull(root);
        View loginButton = root.findViewById(R.id.loginButton);
        assertNotNull(loginButton);
        loginButton.performClick();
        verify(presenter).onLoginPressed("", "");
        verify(fragment, never()).showLoginProgress();
    }

    @Test
    public void testNoUsernameError() throws Exception {
        System.out.println("Should display an error message for missing username");

        startFragment(fragment);
        View root = fragment.getView();
        assertNotNull(root);
        EditText usernameInput = root.findViewById(R.id.loginUsernameInput);
        assertNotNull(usernameInput);
        EditText passwordInput = root.findViewById(R.id.loginPasswordInput);
        assertNotNull(passwordInput);

        passwordInput.setText(PASSWORD);
        View loginButton = root.findViewById(R.id.loginButton);

        loginButton.performClick();

        verify(presenter).onLoginPressed("", PASSWORD);
        verify(fragment).showUsernameMissingError();
        verify(fragment, never()).showLoginProgress();
    }

    @Test
    public void testNoPasswordError() throws Exception {
        System.out.println("Should display an error message for missing password");

        startFragment(fragment);
        View root = fragment.getView();
        assertNotNull(root);
        EditText usernameInput = root.findViewById(R.id.loginUsernameInput);
        assertNotNull(usernameInput);
        EditText passwordInput = root.findViewById(R.id.loginPasswordInput);
        assertNotNull(passwordInput);

        usernameInput.setText(USERNAME);
        View loginButton = root.findViewById(R.id.loginButton);

        loginButton.performClick();

        verify(presenter).onLoginPressed(USERNAME, "");
        verify(fragment).showPasswordMissingError();
        verify(fragment, never()).showLoginProgress();
    }

    @Test
    public void testLoginUsernamePasswordShowProgress() throws Exception {
        System.out.println("Should login with a username and passord and show a progress dialog");

        startFragment(fragment);
        View root = fragment.getView();
        assertNotNull(root);
        EditText usernameInput = root.findViewById(R.id.loginUsernameInput);
        assertNotNull(usernameInput);
        EditText passwordInput = root.findViewById(R.id.loginPasswordInput);
        assertNotNull(passwordInput);

        usernameInput.setText(USERNAME);
        passwordInput.setText(PASSWORD);

        View loginButton = root.findViewById(R.id.loginButton);
        loginButton.performClick();

        verify(fragment).showLoginProgress();
        verify(presenter).onLoginPressed(USERNAME, PASSWORD);
        verify(fragment, never()).showUsernameMissingError();
        verify(fragment, never()).showPasswordMissingError();
    }

    @Test
    public void testLoginErrorShowsDialog() throws Exception {
        System.out.println("Should show an error dialog when there is a login problem");

        when(userRepo.login(anyString(), anyString())).thenReturn(Observable.error(UserApiResponses.getLoginResponseInvalidUsername()));

        startFragment(fragment);
        View root = fragment.getView();
        assertNotNull(root);

        EditText usernameInput = root.findViewById(R.id.loginUsernameInput);
        EditText passwordInput = root.findViewById(R.id.loginPasswordInput);

        usernameInput.setText(USERNAME);
        passwordInput.setText(PASSWORD);

        View loginButton = root.findViewById(R.id.loginButton);
        loginButton.performClick();

        verify(fragment).showLoginProgress();
        verify(fragment).hideLoginProgress();
        verify(fragment).showError(any());
    }

    @Test
    public void testDetachment() throws Exception {
        System.out.println("Should detach view from presenter on stopping");

        startFragment(fragment);

        fragment.onPause();
        fragment.onStop();

        verify(presenter).detachView();
    }*/
}