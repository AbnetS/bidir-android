package com.gebeya.mobile.bidir.data.acatapplication;

import java.util.Comparator;

public class ACATApplicationComparator implements Comparator<ACATApplication> {
    @Override
    public int compare(ACATApplication left, ACATApplication right) {
        return right.updatedAt.compareTo(left.updatedAt);
    }
}
