package com.gebeya.mobile.bidir.data.acatrevenuesection.local;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;

import java.util.Comparator;

/**
 * Comparator class used to sort the list of {@link ACATRevenueSection} sections, by their given number.
 */
public class ACATRevenueSectionComparator implements Comparator<ACATRevenueSection> {

    @Override
    public int compare(ACATRevenueSection left, ACATRevenueSection right) {
        return Integer.compare(left.number, right.number);
    }
}
