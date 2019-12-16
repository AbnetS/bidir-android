package com.gebeya.mobile.bidir.data.task.remote;

import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.ApiResponses;
import com.gebeya.mobile.bidir.data.base.remote.backend.BaseEndpointProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.ConnectionProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.EndpointProvider;
import com.gebeya.mobile.bidir.data.permission.BasePermissionHelper;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.screening.BaseScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.screening.ScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.task.BaseTaskHelper;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.data.task.local.TaskApiResponses;
import com.gebeya.mobile.bidir.impl.util.error.BaseErrorHandler;
import com.gebeya.mobile.bidir.impl.util.error.ErrorHandler;
import com.gebeya.mobile.bidir.impl.util.network.NetworkWatcher;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.modules.TestRootModule;
import com.google.gson.JsonObject;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link TaskRemote} source.
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskRemoteTest {

    private TestRootModule module;

    private TaskService service;
    private ConnectionProvider provider;
    private NetworkWatcher watcher;

    private TaskRemoteSource remote;

    @Before
    public void setUp() throws Exception {
        module = new TestRootModule();

        service = mock(TaskService.class);
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
                        bind(TaskHelper.class).toInstance(new BaseTaskHelper());
                        bind(ScreeningStatusHelper.class).toInstance(new BaseScreeningStatusHelper());
                        bind(ConnectionProvider.class).toInstance(provider);
                        bind(NetworkWatcher.class).toInstance(watcher);
                    }
                });

        remote = new TaskRemote();
    }

    @Test
    public void testGetAllOffline() throws Exception {
        System.out.println("Should throw an exception while trying to getByType tasks while offline");
        // GIVEN
        final Throwable error = ApiResponses.createOfflineError();
        when(watcher.connected()).thenReturn(Observable.error(error));
        TestObserver<List<Task>> observer = new TestObserver<>();
        // WHEN
        remote.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertErrorMessage(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION);
    }

    @Test
    public void testGetAllInvalidStatus() throws Exception {
        System.out.println("Should return an error while getByType tasks with invalid status");
        // GIVEN
        when(service.getAll()).thenReturn(Observable.just(TaskApiResponses.getAllInvalidStatus()));
        TestObserver<List<Task>> observer = new TestObserver<>();
        // WHEN
        remote.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
                throwable.getMessage().contains("Unknown API Task status")
        );
    }

    @Test
    public void testGetAllInvalidRefType() throws Exception {
        System.out.println("Should return an error while getByType tasks with invalid reference type");
        // GIVEN
        when(service.getAll()).thenReturn(Observable.just(TaskApiResponses.getAllInvalidRefType()));
        TestObserver<List<Task>> observer = new TestObserver<>();
        // WHEN
        remote.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
                throwable.getMessage().contains("Unknown API Task Ref type")
        );
    }

    @Test
    public void testGetAllInvalidUserAndComment() throws Exception {
        System.out.println("Should provide default values for user and comment containing null values");
        // GIVEN
        final JsonObject error = TaskApiResponses.getAllInvalidTaskUserCommentNull();
        when(service.getAll()).thenReturn(Observable.just(error));
        TestObserver<List<Task>> observer = new TestObserver<>();
        // WHEN
        remote.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Task> tasks = observer.values().get(0);
        assertFalse(tasks.isEmpty());
        final Task task = tasks.get(0);
        assertEquals("-", task.comment);
        assertEquals("-", task.userId);
    }

    @Test
    public void testGetAllInvalidType() throws Exception {
        System.out.println("Should return an error while getByType tasks with invalid type");
        // GIVEN
        when(service.getAll()).thenReturn(Observable.just(TaskApiResponses.getAllInvalidTaskType()));
        TestObserver<List<Task>> observer = new TestObserver<>();
        // WHEN
        remote.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
                throwable.getMessage().contains("Unknown API Task type")
        );
    }

    @Test
    public void testUpdateTaskInvalidId() throws Exception {
        System.out.println("Should return an error while trying to saveLoanProposal an invalid API task");
        // GIVEN
        final Throwable error = TaskApiResponses.getUpdateStatusInvalidTaskId();
        when(watcher.connected()).thenReturn(Observable.error(error));
        TestObserver<Task> observer = new TestObserver<>();
        // WHEN
        remote.updateStatus(new Task(), TaskHelper.STATUS_APPROVED, "cool comment")
                .subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNotComplete().assertNoValues();
        observer.assertError(throwable ->
                throwable.getMessage().contains("Cannot read property")
        );
    }

    @Test
    public void testUpdateTaskApiSuccessful() throws Exception {
        System.out.println("Should saveLoanProposal the existing API task");
        assertTrue(true);   // TODO: Implement the actual pageResponse test (don't know what it is like)
    }

    @Test
    public void testGetAllSuccessful() throws Exception {
        System.out.println("Should getByType tasks from the API");
        // GIVEN
        when(service.getAll()).thenReturn(Observable.just(TaskApiResponses.getAllTasksSuccessful()));
        TestObserver<List<Task>> observer = new TestObserver<>();
        // WHEN
        remote.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Task> tasks = observer.values().get(0);
        assertFalse(tasks.isEmpty());
    }

    @After
    public void tearDown() throws Exception {
        reset(provider, watcher);
        module.cleanUp();
        Toothpick.closeScope(Scopes.SCOPE_REMOTE_SOURCES);
    }
}