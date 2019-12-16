package com.gebeya.mobile.bidir.data.groups.local;

import com.gebeya.mobile.bidir.data.groups.Group;

import java.util.Comparator;

public class GroupComparator implements Comparator<Group> {
    @Override
    public int compare(Group left, Group right) {
        return right.updatedAt.compareTo(left.updatedAt);
    }
}
