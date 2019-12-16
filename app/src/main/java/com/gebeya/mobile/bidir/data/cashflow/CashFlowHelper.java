package com.gebeya.mobile.bidir.data.cashflow;

import android.support.annotation.NonNull;

/**
 * Created by Samuel K. on 5/8/2018.
 * <p>
 * samkura47@gmail.com
 */
public interface CashFlowHelper {

    String JANUARY = "Jan";
    String API_JANUARY = "jan";
    String FEBRUARY = "Feb";
    String API_FEBRUARY = "feb";
    String MARCH = "Mar";
    String API_MARCH = "mar";
    String APRIL = "Apr";
    String API_APRIL = "apr";
    String MAY = "May";
    String API_MAY = "may";
    String JUNE = "June";
    String API_JUNE = "june";
    String JULY = "July";
    String API_JULY = "july";
    String AUGUST = "Aug";
    String API_AUGUST = "aug";
    String SEPTEMBER = "Sept";
    String API_SEPTEMBER = "sep";
    String OCTOBER = "Oct";
    String API_OCTOBER = "oct";
    String NOVEMBER = "Nov";
    String API_NOVEMBER = "nov";
    String DECEMBER = "Dec";
    String API_DECEMBER = "dec";
    String UNKNOWN_MONTH = "unknown_month";

    String ESTIMATED_CASH_FLOW_TYPE = "ESTIMATED";
    String ACTUAL_CASH_FLOW_TYPE = "ACTUAL";
    String ITEM_CASH_FLOW = "ITEM_CASH_FLOW";
    String NET_SECTION_CASH_FLOW_TYPE = "NET_SECTION_CASH_FLOW";
    String CUMULATIVE_SECTION_CASH_FLOW_TYPE = "CUMULATIVE_SECTION_CASH_FLOW";
    String NET_CROPACAT_CASH_FLOW_TYPE = "NET_CROPACAT_CASH_FLOW";
    String CUMULATIVE_CROPACAT_CASH_FLOW_TYPE = "CUMULATIVE_CROPACAT_CASH_FLOW";
    String NET_ACATAPP_CASH_FLOW_TYPE = "NET_ACATAPP_CASH_FLOW";
    String CUMULATIVE_ACATAPP_CASH_FLOW_TYPE = "CUMULATIVE_ACATAPP_CASH_FLOW";

    /**
     * Return the API month value.
     * @param local Local abbreviation of month.
     * @return
     */
    String getApiMonth(@NonNull String local);

    /**
     * Return the local month version.
     * @param api APi abbreviation of month.
     * @return
     */
    String getLocalMonth(@NonNull String api);



}
