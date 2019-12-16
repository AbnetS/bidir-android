package com.gebeya.mobile.bidir.data.cropacat;

import java.util.Comparator;

/**
 * Created by Samuel K. on 6/26/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropACATComparator implements Comparator<CropACAT> {

    @Override
    public int compare(CropACAT left, CropACAT right) {
        return left.cropName.compareTo(right.cropName);
    }
}
