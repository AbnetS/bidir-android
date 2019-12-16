package com.gebeya.apps.framework.data.schema;

import com.gebeya.apps.framework.data.column.Column;

public abstract class BaseSchema {

    public static Column COL_ID = Column.createPrimary(SchemaConstants.COL_ID_NAME);

    public String TABLE;
    public String CREATE;
    public String DELETE;

    public BaseSchema() {
        TABLE = null;
        CREATE = null;
        DELETE = null;
    }
}
