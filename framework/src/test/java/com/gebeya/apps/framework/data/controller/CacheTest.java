package com.gebeya.apps.framework.data.controller;

import com.gebeya.apps.framework.data.model.BaseModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class CacheTest {

    private static final String MODEL_ID = "12345";
    private static final String MODEL_ID_2 = "54321";
    private static final String MODEL_ID_3 = "12321";

    private Cache<BaseModel> cache;
    private BaseModel testModel;

    @Before
    public void init() {
        cache = new Cache<>();
        testModel = new BaseModel();
        testModel.id = MODEL_ID;
        cache.add(testModel);
    }

    @Test
    public void testFindByModel() {
        BaseModel found = cache.find(testModel);
        assertEquals(testModel, found);
    }

    @Test
    public void testFindById() {
        BaseModel found = cache.find(MODEL_ID);
        assertEquals(testModel, found);
    }

    @Test
    public void testAddModels() {
        ArrayList<BaseModel> models = new ArrayList<>();
        BaseModel newModel2 = new BaseModel();
        BaseModel newModel3 = new BaseModel();
        newModel2.id = MODEL_ID_2;
        newModel3.id = MODEL_ID_3;
        models.add(newModel2);
        models.add(newModel3);

        cache.add(models);

        assertEquals(testModel, cache.find(MODEL_ID));
        assertEquals(newModel2, cache.find(newModel2));
        assertEquals(newModel3, cache.find(MODEL_ID_3));
    }
}
