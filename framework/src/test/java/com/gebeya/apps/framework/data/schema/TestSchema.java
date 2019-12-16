package com.gebeya.apps.framework.data.schema;

import com.gebeya.apps.framework.data.column.Column;
import com.gebeya.apps.framework.data.column.ColumnTypes;

public class TestSchema extends BaseSchema {

    public static final String TABLE_NAME = "test_schema";

    public static final Column COL_NAME = Column.create("name");
    public static final Column COL_AGE = Column.create("age", ColumnTypes.INTEGER);
    public static final Column COL_SALARY = Column.create("salary", ColumnTypes.REAL, false);

    public TestSchema() {
        TABLE = TABLE_NAME;
        CREATE = SchemaUtils.buildCreate(
                TABLE,
                COL_ID,
                COL_NAME,
                COL_AGE,
                COL_SALARY
        );
        DELETE = SchemaUtils.buildDelete(TABLE);
    }
}
