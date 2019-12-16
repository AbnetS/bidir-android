package com.gebeya.mobile.bidir.data.client;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Comparator class for comparing clients
 */
public class ClientComparator implements Comparator<Client>{

    @Override
    public int compare(@NonNull Client left, @NonNull Client right) {
        return right.updatedAt.compareTo(left.updatedAt);
    }
}
