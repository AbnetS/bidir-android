package com.gebeya.mobile.bidir.data.user.local;

import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.modules.TestRootModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.observers.TestObserver;
import toothpick.Toothpick;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Test for the {@link UserLocal} UserLocalSource implementation
 */
public class UserLocalTest {

    private static final String TEST_ID = UUID.randomUUID().toString();
    private static final String TEST_TOKEN = "/e/l8rZ684xzOkXP4WWfzHjaLZVZvvkQLvWd1cQTNAU=";
    private static final String TEST_EMAIL = "admin@mail.com";
    private static final String TEST_USERNAME = "admin";
    private static final String TEST_PHONE_NUMBER = "0987362783";
    private static final String TEST_FIRST_NAME = "Super";
    private static final String TEST_LAST_NAME = "Admin";
    private static final String TEST_STATUS = "active";
    private static final String TEST_BRANCH_ID = UUID.randomUUID().toString();

    private UserLocalSource local;

    private TestRootModule module;

    public static User createUser() {
        User user = new User();

        user._id = TEST_ID;
        user.token = TEST_TOKEN;
        user.email = TEST_EMAIL;
        user.username = TEST_USERNAME;
        user.phoneNumber = TEST_PHONE_NUMBER;
        user.firstName = TEST_FIRST_NAME;
        user.lastName = TEST_LAST_NAME;
        user.status = TEST_STATUS;
        user.branchId = TEST_BRANCH_ID;

        return user;
    }

    public static List<Permission> createPermission() {
        final List<Permission> permissions = new ArrayList<>();
        Permission permission = new Permission();

        permission._id = UUID.randomUUID().toString();
        permission.entityModule = PermissionHelper.ENTITY_FORM;
        permission.operation = PermissionHelper.OPERATION_CREATE;

        permissions.add(permission);
        return permissions;
    }

    @Before
    public void setUp() throws Exception {
        module = new TestRootModule();
        Toothpick.openScope(Scopes.SCOPE_ROOT).installModules(module);
        local = new UserLocal();
    }

    @Test
    public void testFirstStored() throws Exception {
        System.out.println("Should retrieve the first User if stored");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        local.put(createUser());
        // WHEN
        local.first().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        assertFalse(values.get(0).empty());
    }

    @Test
    public void testFirstNotStored() throws Exception {
        System.out.println("Should not retrieve the first User if not stored");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        // WHEN
        local.first().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        assertTrue(values.get(0).empty());
    }

    @Test
    public void testGetByIdStored() throws Exception {
        System.out.println("Should retrieve the User by the given ID if stored");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        local.put(createUser());
        // WHEN
        local.get(TEST_ID).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        final Data<User> data = values.get(0);
        assertFalse(data.empty());
        assertEquals(TEST_ID, data.get()._id);
    }

    @Test
    public void testGetByIdNotStored() throws Exception {
        System.out.println("Should not retrieve the User by the given ID if not stored");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        // WHEN
        local.get(TEST_ID).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        assertTrue(values.get(0).empty());
    }

    @Test
    public void testGetByPositionStored() throws Exception {
        System.out.println("Should retrieve the User by the given position if stored");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        local.put(createUser());
        // WHEN
        local.get(0).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        assertFalse(values.get(0).empty());
    }

    @Test
    public void testGetByPositionNotStored() throws Exception {
        System.out.println("Should not retrieve the User by the given position if not stored");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        // WHEN
        local.get(0).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        assertTrue(values.get(0).empty());
    }

    @Test
    public void testPut() throws Exception {
        System.out.println("Should store a User to device");
        // GIVEN
        final User user = createUser();
        TestObserver<User> observer = new TestObserver<>();
        // WHEN
        local.put(user).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        final List<User> users = observer.values();
        assertFalse(users.isEmpty());
        assertEquals(user._id, users.get(0)._id);
    }

    @Test
    public void testPutOverwrite() throws Exception {
        System.out.println("Should overwrite all User data when storing a user");
        // GIVEN
        TestObserver<User> observer = new TestObserver<>();
        // WHEN
        local.put(createUser()).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        List<User> users = observer.values();
        assertFalse(users.isEmpty());
        assertEquals(users.size(), 1);

        // GIVEN
        observer = new TestObserver<>();
        // WHEN
        local.put(createUser()).subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        users = observer.values();
        assertFalse(users.isEmpty());
        assertEquals(users.size(), 1);
    }

    @Test
    public void testRemoveAll() throws Exception {
        System.out.println("Should remove all User data");
        // GIVEN
        TestObserver<Data<User>> observer = new TestObserver<>();
        local.put(createUser());
        // WHEN
        local.removeAll();
        local.first().subscribe(observer);
        // THEN
        observer.assertSubscribed().assertNoErrors().assertComplete();
        List<Data<User>> values = observer.values();
        assertFalse(values.isEmpty());
        assertTrue(values.get(0).empty());
    }

    @After
    public void tearDown() throws Exception {
        module.cleanUp();
        Toothpick.closeScope(Scopes.SCOPE_ROOT);
    }
}