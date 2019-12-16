package com.gebeya.mobile.bidir.data.user.remote;

import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.ApiResponses;
import com.gebeya.mobile.bidir.data.base.remote.backend.BaseEndpointProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.ConnectionProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.EndpointProvider;
import com.gebeya.mobile.bidir.data.permission.BasePermissionHelper;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.impl.util.error.BaseErrorHandler;
import com.gebeya.mobile.bidir.impl.util.error.ErrorHandler;
import com.gebeya.mobile.bidir.impl.util.network.NetworkWatcher;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.modules.TestRootModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import toothpick.Toothpick;
import toothpick.config.Module;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link UserRemote} UserRemote implementation
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRemoteTest {

    public static final String TEST_USERNAME = "username";
    public static final String TEST_PASSWORD = "password";

    private TestRootModule module;

    private UserService service;
    private ConnectionProvider provider;
    private NetworkWatcher watcher;

    private UserRemoteSource remote;

    @Before
    public void setUp() throws Exception {
        module = new TestRootModule();

        service = mock(UserService.class);
        provider = mock(ConnectionProvider.class);
        when(provider.createService(
                anyString(),
                anyString(),
                anyString(),
                any(),
                any())
        ).thenReturn(service);

        watcher = mock(NetworkWatcher.class);
        when(watcher.connected()).thenReturn(Observable.just(true));

        Toothpick.openScope(Scopes.SCOPE_REMOTE_SOURCES)
                .installModules(module, new Module() {
                    {
                        bind(PermissionHelper.class).toInstance(new BasePermissionHelper());
                        bind(ErrorHandler.class).toInstance(new BaseErrorHandler());
                        bind(EndpointProvider.class).toInstance(new BaseEndpointProvider());
                        bind(ConnectionProvider.class).toInstance(provider);
                        bind(NetworkWatcher.class).toInstance(watcher);
                    }
                });
        remote = new UserRemote();
    }

    @Test
    public void testLoginOffline() throws Exception {
        System.out.println("Should throw an exception while trying to login when offline");
        // GIVEN
        final Throwable error = ApiResponses.createOfflineError();
        when(watcher.connected()).thenReturn(Observable.error(error));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        Observable<UserResponse> observable = remote.login(TEST_USERNAME, TEST_PASSWORD);
        observable.subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertErrorMessage(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION);
    }

    @Test
    public void testLoginOnlineWrongUsername() throws Exception {
        System.out.println("Should return an error message when the username is invalid");
        // GIVEN
        final Throwable error = UserApiResponses.getLoginResponseInvalidUsername();
        when(watcher.connected()).thenReturn(Observable.error(error));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        remote.login("wrong!", TEST_PASSWORD).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertErrorMessage(ApiErrors.LOGIN_PART_INVALID_USERNAME_PASSWORD);
    }

    @Test
    public void testLoginOnlineWrongPassword() throws Exception {
        System.out.println("Should return an error message when the password is invalid");
        // GIVEN
        final Throwable error = UserApiResponses.getLoginResponseInvalidPassword();
        when(watcher.connected()).thenReturn(Observable.error(error));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        remote.login(TEST_USERNAME, "wrong!").subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertErrorMessage(ApiErrors.LOGIN_PART_INVALID_PASSWORD);
    }

    @Test
    public void testWrongLoginResponse() throws Exception {
        System.out.println("Should return an error message when unexpected pageResponse returns");
        // GIVEN
        when(service.login(any())).thenReturn(Observable.just(UserApiResponses.getLoginResponseInvalid()));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        remote.login(TEST_USERNAME, TEST_PASSWORD).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
            throwable.getMessage().contains("Error parsing UserResponse")
        );
    }

    @Test
    public void testWrongLoginPermissionInvalidEntity() throws Exception {
        System.out.println("Should return an error message when the permission entity doesn't exist");
        // GIVEN
        when(service.login(any())).thenReturn(Observable.just(UserApiResponses.getLoginResponseInvalidPermissionEntity()));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        remote.login(TEST_USERNAME, TEST_PASSWORD).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
                throwable.getMessage().contains("Error parsing Permission")
        );
    }

    @Test
    public void testWrongLoginPermissionInvalidOperation() throws Exception {
        System.out.println("Should return an error message when the permission operation doesn't exist");
        // GIVEN
        when(service.login(any())).thenReturn(Observable.just(UserApiResponses.getLoginResponseInvalidPermissionOperation()));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        remote.login(TEST_USERNAME, TEST_PASSWORD).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
                throwable.getMessage().contains("Error parsing Permission")
        );
    }

    @Test
    public void testLoginOnlineSuccessful() throws Exception {
        System.out.println("Should return an successful pageResponse when username and password are provided");
        // GIVEN
        when(service.login(any())).thenReturn(Observable.just(UserApiResponses.getLoginResponseSuccess()));
        TestObserver<UserResponse> observer = new TestObserver<>();
        // WHEN
        remote.login(TEST_USERNAME, TEST_PASSWORD).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<UserResponse> responses = observer.values();
        assertTrue(!responses.isEmpty());
    }

    @After
    public void tearDown() throws Exception {
        module.cleanUp();
        Toothpick.closeScope(Scopes.SCOPE_REMOTE_SOURCES);
    }
}