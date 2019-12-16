package com.gebeya.mobile.bidir.data.acatapplication;

import java.util.Comparator;

public class ACATApplicationLatest implements Comparator<ACATApplication> {

    @Override
    public int compare(ACATApplication left, ACATApplication right) {
        return right.createdAt.compareTo(left.createdAt);
    }

}
