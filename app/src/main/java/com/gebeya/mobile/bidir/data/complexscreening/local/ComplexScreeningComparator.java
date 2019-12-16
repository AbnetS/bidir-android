package com.gebeya.mobile.bidir.data.complexscreening.local;

import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;

import java.util.Comparator;

/**
 * Comparator implementation class used in the sorting of the complex screening objects.
 */
public class ComplexScreeningComparator implements Comparator<ComplexScreening> {

    @Override
    public int compare(ComplexScreening left, ComplexScreening right) {
        return right.updatedAt.compareTo(left.updatedAt);
    }
}
