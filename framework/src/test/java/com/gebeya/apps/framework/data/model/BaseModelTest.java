package com.gebeya.apps.framework.data.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BaseModelTest {

    @Test
    public void testEquals() {
        BaseModel model1 = new BaseModel();
        model1.id = "9535DCDF";

        BaseModel model2 = new BaseModel();
        model2.id = model1.id;
        assertEquals(model1, model2);
    }
}
