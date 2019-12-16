package com.gebeya.apps.framework.data.datasource;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.data.DataSourceContract;
import com.gebeya.apps.framework.data.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class DataSourceContractTestImpl implements DataSourceContract<BaseModel> {

    private ArrayList<BaseModel> models = new ArrayList<>();

    @Override
    public void getItem(int position, @NonNull LoadItemCallback<BaseModel> callback) {

    }

    @Override
    public int size() {
        return models.size();
    }

    @Override
    public void getItem(@NonNull String id, @NonNull LoadItemCallback<BaseModel> callback) {

    }

    @Override
    public void getItems(@NonNull LoadItemsCallback<BaseModel> callback) {

    }

    @Override
    public void addItems(@NonNull List<BaseModel> items, @NonNull AddItemsCallback<BaseModel> callback) {

    }

    @Override
    public void removeItem(@NonNull BaseModel item, @NonNull RemoveItemCallback<BaseModel> callback) {

    }

    @Override
    public void removeItems(@NonNull List<BaseModel> items, @NonNull RemoveItemsCallback<BaseModel> callback) {

    }

    @Override
    public void removeAll(@NonNull RemoveItemsCallback<BaseModel> callback) {

    }
}
