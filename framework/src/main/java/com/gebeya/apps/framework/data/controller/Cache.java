package com.gebeya.apps.framework.data.controller;

import com.gebeya.apps.framework.data.model.BaseModel;

import java.util.ArrayList;

public class Cache<T extends BaseModel> {

    private ArrayList<T> items;

    public Cache() {
        items = new ArrayList<>();
    }

    public T find(String id) {
        for (T item : items) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }

    public T find(T item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            return items.get(index);
        }
        return null;
    }

    public void add(T item) {
        items.add(item);
    }

    public void add(ArrayList<T> other) {
        items.addAll(other);
    }

    public void clear() {
        items.clear();
    }
}
