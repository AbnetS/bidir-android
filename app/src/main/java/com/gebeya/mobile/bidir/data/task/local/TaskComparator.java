package com.gebeya.mobile.bidir.data.task.local;

import com.gebeya.mobile.bidir.data.task.Task;

import java.util.Comparator;

/**
 * Comparator class used to sort the tasks.
 */
public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task left, Task right) {
        return left.updatedAt.compareTo(right.updatedAt);
    }
}
