package com.gebeya.mobile.bidir.data.task.local;

import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.modules.TestRootModule;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.observers.TestObserver;
import toothpick.Toothpick;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Local Test for the {@link TaskLocal} source implementation.
 */
public class TaskLocalTest {

    private static final String TEST_ID = UUID.randomUUID().toString();
    private static final String TEST_DESCRIPTION = "Loan Approve Task";
    private static final DateTime TEST_UPDATED_AT = new DateTime("2018-01-21T09:54:04.256Z");
    private static final String TEST_TYPE = TaskHelper.API_TYPE_APPROVE;
    private static final String TEST_REFERENCE_ID = UUID.randomUUID().toString();
    private static final String TEST_REFERENCE_TYPE = TaskHelper.API_REF_LOAN;
    private static final String TEST_CREATED_BY = UUID.randomUUID().toString();
    private static final String TEST_COMMENT = "This is a comment";
    private static final String TEST_STATUS = TaskHelper.STATUS_PENDING;
    private static final String TEST_USER_ID = UUID.randomUUID().toString();

    private TaskLocalSource local;

    private TestRootModule module;

    public static Task createTask() {
        Task task = new Task();

        task._id = TEST_ID;
        task.description = TEST_DESCRIPTION;
        task.updatedAt = TEST_UPDATED_AT;
        task.type = TEST_TYPE;
        task.referenceId = TEST_REFERENCE_ID;
        task.referenceType = TEST_REFERENCE_TYPE;
        task.createdBy = TEST_CREATED_BY;
        task.comment = TEST_COMMENT;
        task.status = TEST_STATUS;
        task.userId = TEST_USER_ID;

        return task;
    }

    @Before
    public void setUp() throws Exception {
        module = new TestRootModule();
        Toothpick.openScopes(Scopes.SCOPE_ROOT).installModules(module);
        local = new TaskLocal();
    }

    @Test
    public void testFirstStored() throws Exception {
        System.out.println("Should return the first task when stored");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        local.put(createTask());
        // WHEN
        local.first().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        assertFalse(values.get(0).empty());
    }

    @Test
    public void testFirstNotStored() throws Exception {
        System.out.println("Should not return the first task when not stored");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        // WHEN
        local.first().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        assertTrue(values.get(0).empty());
    }

    @Test
    public void testGetByIdStored() throws Exception {
        System.out.println("Should retrieve a single task by ID when stored");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        final Task task = createTask();
        local.put(task);
        // WHEN
        local.get(TEST_ID).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        final Data<Task> data = values.get(0);
        assertFalse(data.empty());
        assertEquals(data.get()._id, TEST_ID);
    }

    @Test
    public void testGetByIdNotStored() throws Exception {
        System.out.println("Should not retrieve a single task by ID when it doesn't exist");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        // WHEN
        local.get(TEST_ID).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        final Data<Task> data = values.get(0);
        assertTrue(data.empty());
    }

    @Test
    public void testGetByPositionStored() throws Exception {
        System.out.println("Should retrieve a single task by its position when stored");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        final Task task = createTask();
        local.put(task);
        // WHEN
        local.get(0).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        final Data<Task> data = values.get(0);
        assertFalse(data.empty());
    }

    @Test
    public void testGetByPositionNotStored() throws Exception {
        System.out.println("Should not retrieve a single task by its position when not stored");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        // WHEN
        local.get(0).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        final Data<Task> data = values.get(0);
        assertTrue(data.empty());
    }

    @Test
    public void testGetTasks() throws Exception {
        System.out.println("Should retrieve a list of tasks");
        // GIVEN
        TestObserver<List<Task>> observer = new TestObserver<>();
        local.put(createTask());
        final Task task = createTask();
        task._id = UUID.randomUUID().toString();
        local.put(task);
        final Task task2 = createTask();
        task2._id = UUID.randomUUID().toString();
        local.put(task2);
        // WHEN
        local.getAll().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Task> tasks = observer.values().get(0);
        assertFalse(tasks.isEmpty());
        assertEquals(3, tasks.size());
    }

    @Test
    public void testStoreTask() throws Exception {
        System.out.println("Should store a task locally");
        // GIVEN
        TestObserver<Task> observer = new TestObserver<>();
        final Task task = createTask();
        // WHEN
        local.put(task).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Task> values = observer.values();
        assertFalse(values.isEmpty());
        final Task storedTask = values.get(0);
        assertEquals(storedTask._id, task._id);
    }

    @Test
    public void testUpdateTaskStored() throws Exception {
        System.out.println("Should saveLoanProposal a task locally");
        // GIVEN
        TestObserver<Data<Task>> observer = new TestObserver<>();
        final Task task = createTask();
        local.put(task);
        local.get(task._id).subscribe(observer);
        final Task stored = observer.values().get(0).get();
        assertEquals(task._id, stored._id);
        // WHEN
        stored.comment = "Approved!";
        observer = new TestObserver<>();
        local.put(stored);
        local.get(task._id).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertComplete().assertNoErrors();
        final List<Data<Task>> values = observer.values();
        assertFalse(values.isEmpty());
        final Task updated = values.get(0).get();
        assertEquals(stored.comment, updated.comment);
    }

    @Test
    public void testGetStoredSize() throws Exception {
        System.out.println("Should return the size of all the tasks stored locally");
        // GIVEN
        final List<Task> tasks = new ArrayList<>();
        tasks.add(createTask());
        tasks.add(createTask());
        tasks.add(createTask());
        local.putAll(tasks);
        // WHEN
        final int size = local.size();
        // THEN
        assertEquals(tasks.size(), size);
    }

    @After
    public void tearDown() throws Exception {
        module.cleanUp();
        Toothpick.closeScope(Scopes.SCOPE_ROOT);
    }
}