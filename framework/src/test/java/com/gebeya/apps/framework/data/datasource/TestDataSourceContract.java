package com.gebeya.apps.framework.data.datasource;

import com.gebeya.apps.framework.data.DataSourceContract;
import com.gebeya.apps.framework.data.model.BaseModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;

public class TestDataSourceContract {

    private static final String TEST_MODEL_ID = "987D9KFJD";

    private DataSourceContract<BaseModel> dataSource;
    private BaseModel testModel;

    @Before
    public void setup() {
        dataSource = new DataSourceContractTestImpl();
        testModel = new BaseModel();
        testModel.id = TEST_MODEL_ID;
    }

    @Test
    public void testAddItem() {
        final List<BaseModel> models = new ArrayList<>();
        models.add(testModel);
        dataSource.addItems(models, new DataSourceContract.AddItemsCallback<BaseModel>() {
            @Override
            public void onItemsAdded(List<BaseModel> items) {
                assertNull(items);
            }

            @Override
            public void onFailed(List<String> messages) {

            }
        });
    }
}
