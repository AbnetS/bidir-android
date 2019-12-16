package com.gebeya.mobile.bidir.toothpick.modules;

import com.gebeya.mobile.bidir.data.MyObjectBox;

import java.io.File;

import io.objectbox.BoxStore;

/**
 * Test root module for {@link toothpick.Toothpick}
 */
public class TestRootModule extends TestModule {

    private final BoxStore store;

    public TestRootModule() throws Exception {
        File temp = File.createTempFile("object-store-test", "");
        temp.delete();
        store = MyObjectBox.builder().directory(temp).build();
        bind(BoxStore.class).toInstance(store);
    }

    @Override
    public void cleanUp() {
        if (store != null) {
            store.close();
            store.deleteAllFiles();
        }
    }
}
