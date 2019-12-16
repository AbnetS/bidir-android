package com.gebeya.mobile.bidir.data.task.local;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.task.Task;

/**
 * Local source interface for the tasks.
 */
public interface TaskLocalSource extends ReadableSource<Task>, WritableSource<Task>, ReadSize {

}