package com.gebeya.mobile.bidir.data.complexloanapplication.local;

import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;

import java.util.Comparator;

/**
 * Comparator for sorting {@link com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication}
 * objects.
 */
public class ComplexLoanApplicationComparator implements Comparator<ComplexLoanApplication>{

    @Override
    public int compare(ComplexLoanApplication left, ComplexLoanApplication right) {
        return right.updatedAt.compareTo(left.updatedAt);
    }
}
