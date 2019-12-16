package com.gebeya.mobile.bidir.data.costlist2;

import com.gebeya.mobile.bidir.data.costlist2.groupedcostlist.GroupedCostList;
import com.gebeya.mobile.bidir.data.costlist2.linearcostlist.LinearCostList;

import java.util.List;

/**
 * Class for holding {@link CostList2} class and all it's contents.
 */

public class CostListResponse {
    public List<GroupedCostList> groupedCostLists;
    public List<LinearCostList> linearCostLists;
}
