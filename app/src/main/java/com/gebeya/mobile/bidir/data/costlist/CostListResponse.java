package com.gebeya.mobile.bidir.data.costlist;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedListResponse;

import java.util.List;

/**
 * Created by abuti on 5/11/2018.
 */


public class CostListResponse {
    public CostList costList;
    //public List<CashFlow> cashFlows;
    public List<ACATItemResponse> linear;
    public List<GroupedListResponse> grouped;
}
