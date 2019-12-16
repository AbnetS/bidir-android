package com.gebeya.mobile.bidir.data.user;

import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocal;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocalTest;
import com.gebeya.mobile.bidir.data.user.remote.UserApiResponses;
import com.gebeya.mobile.bidir.data.user.remote.UserRemoteSource;
import com.gebeya.mobile.bidir.data.user.remote.UserRemoteTest;
import com.gebeya.mobile.bidir.data.user.remote.UserResponse;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.impl.util.location.preference.PreferenceHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.modules.TestRootModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import toothpick.Toothpick;
import toothpick.config.Module;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link UserRepo} implementation
 */
@RunWith(JUnit4.class)
public class UserRepoTest {

    private TestRootModule module;

    private UserLocalSource local;
    @Mock UserRemoteSource remote;
    @Mock PermissionLocalSource permissionLocal;
    @Mock PreferenceHelper pref;

    private UserRepoSource repo;

    private User user;
    private List<Permission> permissions;
    private UserResponse response;

    @Before
    public void setUp() throws Exception {
        user = UserLocalTest.createUser();
        permissions = UserLocalTest.createPermission();
        response = new UserResponse();
        response.user = user;
        response.permissions = permissions;

        remote = mock(UserRemoteSource.class);

        permissionLocal = spy(PermissionLocalSource.class);
        when(permissionLocal.putAll(any())).thenReturn(Observable.just(permissions));

        module = new TestRootModule();
        Toothpick.openScopes(Scopes.SCOPE_ROOT).installModules(module);

        local = spy(new UserLocal());
        pref = mock(PreferenceHelper.class);

        Toothpick.openScopes(
                Scopes.SCOPE_ROOT,
                Scopes.SCOPE_REPOSITORIES
        ).installModules(new Module() {
            {
                bind(UserLocalSource.class).toInstance(local);
                bind(UserRemoteSource.class).toInstance(remote);
                bind(PermissionLocalSource.class).toInstance(permissionLocal);
                bind(PreferenceHelper.class).toInstance(pref);
            }
        });

        repo = new UserRepo();

    }

    @Test
    public void testLoginSuccessful() throws Exception {
        System.out.println("Should successfully store all the data when login works");
        // GIVEN
        when(remote.login(anyString(), anyString())).thenReturn(Observable.just(response));
        TestObserver<User> observer = new TestObserver<>();
        TestObserver<Data<User>> dataObserver = new TestObserver<>();
        // WHEN
        repo.login(UserRemoteTest.TEST_USERNAME, UserRemoteTest.TEST_PASSWORD).subscribe(observer);
        local.first().subscribe(dataObserver);
        // THEN
        // Test against returned User object
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<User> users = observer.values();
        assertFalse(users.isEmpty());
        final User returnedUser = users.get(0);
        assertEquals(returnedUser._id, user._id);

        // Test against locally stored User object
        dataObserver.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<User>> dataList = dataObserver.values();
        assertFalse(dataList.isEmpty());
        final Data<User> data = dataList.get(0);
        assertFalse(data.empty());
        final User storedUser = data.get();
        assertEquals(storedUser._id, user._id);
    }

    @Test
    public void testLoginError() throws Exception {
        System.out.println("Should not store any data if there was an error during login");
        // GIVEN
        final Throwable error = UserApiResponses.getLoginResponseInvalidUsername();
        when(remote.login(anyString(), anyString())).thenReturn(Observable.error(error));
        TestObserver<User> observer = new TestObserver<>();
        // WHEN
        repo.login(UserRemoteTest.TEST_USERNAME, UserRemoteTest.TEST_PASSWORD).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        final List<Throwable> errors = observer.errors();
        assertFalse(errors.isEmpty());
        verify(local, never()).put(any());
        verify(permissionLocal, never()).putAll(any());
    }

    @Test
    public void testLogout() throws  Exception {
        System.out.println("Should remove all the data when logging out");
        // GIVEN
        when(remote.login(anyString(), anyString())).thenReturn(Observable.just(response));
        TestObserver<Data<User>> observer = new TestObserver<>();
        repo.login(UserRemoteTest.TEST_USERNAME, UserRemoteTest.TEST_PASSWORD);
        local.first().subscribe(observer);
        observer.assertSubscribed().assertComplete().assertNoErrors();
        List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        observer = new TestObserver<>();
        // WHEN
        repo.logout().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoValues();
        observer = new TestObserver<>();
        local.first().subscribe(observer);
        observer.assertSubscribed().assertComplete().assertNoErrors();
        values = observer.values();
        assertFalse(values.isEmpty());
        assertTrue(values.get(0).empty());
    }

    @After
    public void tearDown() throws Exception {
        module.cleanUp();
        Toothpick.closeScope(Scopes.SCOPE_REPOSITORIES);
        Toothpick.closeScope(Scopes.SCOPE_ROOT);
        reset(local, remote, permissionLocal);
    }
}