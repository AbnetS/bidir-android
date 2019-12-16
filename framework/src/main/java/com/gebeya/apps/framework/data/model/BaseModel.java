package com.gebeya.apps.framework.data.model;

public class BaseModel {

    public String id;

    public BaseModel() {
        id = "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseModel) {
            BaseModel other = (BaseModel)obj;
            return other.id.equals(id);
        }
        return false;
    }
}