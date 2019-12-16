package com.gebeya.mobile.bidir.data.groupedcomplexscreening.local;

import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groups.Group;

import java.util.Comparator;

public class GroupedScreeningComparator implements Comparator<GroupedComplexScreening> {
    @Override
    public int compare(GroupedComplexScreening left, GroupedComplexScreening right) {
        return right.updatedAt.compareTo(left.updatedAt);
    }
}
