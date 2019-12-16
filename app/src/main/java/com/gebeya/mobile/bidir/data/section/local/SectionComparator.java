package com.gebeya.mobile.bidir.data.section.local;

import com.gebeya.mobile.bidir.data.section.Section;

import java.util.Comparator;

/**
 * Comparator class used for sorting sections
 */
public class SectionComparator implements Comparator<Section> {
    @Override
    public int compare(Section left, Section right) {
        if (left.number < right.number) return -1;
        if (left.number > right.number) return 1;
        return 0;
    }
}
