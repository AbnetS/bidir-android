package com.gebeya.mobile.bidir.ui.questions.acat;

import android.support.annotation.NonNull;

/**
 * Created by Samuel K. on 6/9/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATUtility {

    public static int getMonthIndex(@NonNull String firstExpenceMonth) {
        switch (firstExpenceMonth) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                return 0;
        }
    }

    public static int returnMonthPositionInOriginalList(String month){
        switch(month){
            case "Jan": return 0;
            case "Feb": return 1;
            case "Mar": return 2;
            case "Apr": return 3;
            case "May": return 4;
            case "Jun": return 5;
            case "Jul": return 6;
            case "Aug": return 7;
            case "Sep": return 8;
            case "Oct": return 9;
            case "Nov": return 10;
            case "Dec": return 11;
            default: return -1;
        }
    }
}
