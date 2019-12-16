package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel K. on 6/7/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCostEstimationUtlity {

    public final static String INPUTS_AND_ACTIVITY_COST = "Inputs And Activity Costs";
    public final static String INPUT = "Input";
    public final static String LABOUR_COST = "Labour Cost";
    public final static String OTHER_COSTS = "Other Costs";

    public static List<ACATCostSection> getCostSubSection(@NonNull String parent, @NonNull ACATCostSection acatCostSection, @NonNull List<ACATCostSection> acatCostSectionList) {
        List<ACATCostSection> costSections = new ArrayList<>();
        if (acatCostSection.title.equalsIgnoreCase(parent)) {
            costSections = getCostSubSection(acatCostSection.subSectionIDs, acatCostSectionList);
        }
        return costSections;
    }

    public static ACATCostSection getcostSection(@NonNull String parent, @NonNull List<ACATCostSection> costSections) {
        for (ACATCostSection costSection : costSections) {
            if (costSection.title.equalsIgnoreCase(parent))
                return costSection;
        }

        return null;
    }
    /**
     *
     * @param parent Here is "Input", "Labour Cst", and "Other Cost".
     * @param acatCostSections
     * @return
     */
    public static List<String> getACATsubSectionIds(@NonNull String parent, @NonNull List<ACATCostSection> acatCostSections) {
        for (ACATCostSection costSection : acatCostSections) {
            costSection.title.equalsIgnoreCase(parent);
            return costSection.subSectionIDs;
        }
        return null;
    }

    public static List<ACATCostSection> getCostSectionList(ACATCostSection costSection, List<ACATCostSection> costSections) {
        List<ACATCostSection> costSectionList = new ArrayList<>();
        int size = costSection.subSectionIDs.size();

        for (int i = 0; i < size; i++) {
            for (ACATCostSection section : costSections) {
                if (section._id.equalsIgnoreCase(costSection.subSectionIDs.get(i)))
                    costSectionList.add(section);
            }
        }

        List<ACATCostSection> acatCostSections = getChildCostSection(costSectionList, costSections);
        costSectionList.addAll(acatCostSections);
        return costSectionList;
    }

    public static List<ACATCostSection> getChildCostSection(List<ACATCostSection> costSections, List<ACATCostSection> costSectionList) {
        List<ACATCostSection> acatCostSections = new ArrayList<>();
        ACATCostSection costSection = getCostSection(INPUT, costSections);

        int size = costSection.subSectionIDs.size();

        for (int i = 0; i < size; i++) {
            for (ACATCostSection section : costSectionList) {
                if (section._id.equalsIgnoreCase(costSection.subSectionIDs.get(i)))
                    acatCostSections.add(section);
            }
        }
        return acatCostSections;
    }

    public static ACATCostSection getCostSection(String groupHeader, List<ACATCostSection> costSections) {
        for (ACATCostSection costSection : costSections) {
            if (costSection.title.equalsIgnoreCase(groupHeader))
                return costSection;
        }
        return null;
    }

    public static List<ACATCostSection> getCostSubSection(@NonNull List<String> subsectionIds, @NonNull List<ACATCostSection> acatCostSections) {
        List<ACATCostSection> costSections = new ArrayList<>();
        for (int i = 0; i < subsectionIds.size(); i++) {
            for (ACATCostSection acatCostSection : acatCostSections) {
                if (acatCostSection._id.equalsIgnoreCase(subsectionIds.get(i)))
                    costSections.add(acatCostSection);
            }
        }
        return costSections;
    }

    /**
     *
     * @param parent Here is "Fertilizer", "Seed", and "Chemicals"
     * @param
     * @return
     */
    public static String getACATItemSectionId(@NonNull String parent, @NonNull List<ACATCostSection> acatCostSections) {
        for (ACATCostSection costSection : acatCostSections) {
            if (costSection.title.equalsIgnoreCase(parent)) return costSection._id;
        }
        return null;
    }

    public static double getACATItemEstimatedSubtotal(@NonNull String parent, @NonNull List<ACATCostSection> acatCostSections) {
        for (ACATCostSection costSection : acatCostSections) {
            if (costSection.title.equalsIgnoreCase(parent)) {
                return costSection.estimatedSubTotal;
            }
        }
        return 0;
    }

    public static double getACATItemActualSubtotal(@NonNull String parent, @NonNull List<ACATCostSection> acatCostSections) {
        for (ACATCostSection costSection : acatCostSections) {
            if (costSection.title.equalsIgnoreCase(parent)) {
                return costSection.actualSubTotal;
            }
        }
        return 0;
    }

    public static String getCostSectionId(String title, List<ACATCostSection> sections) {
        for (ACATCostSection costSection : sections) {
            if (costSection.title.equalsIgnoreCase(title))
                return costSection._id;
        }
        return null;
    }

    public static CashFlow getCashFlow(String costEstimationId, List<CashFlow> cashFlows) {
        for (CashFlow cashFlow : cashFlows) {
            if (costEstimationId.equalsIgnoreCase(cashFlow.referenceId) && cashFlow.type.equalsIgnoreCase(CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE))
                return cashFlow;
        }
        return null;
    }

    public static CashFlow getCostEstimationItemCashFlow(@NonNull String costEstimationItem, @NonNull List<ACATCostSection> costSections, @NonNull List<CashFlow> cashFlowList) {
        final String costEstimationId = getCostSectionId(costEstimationItem, costSections);
        return getCashFlow(costEstimationId, cashFlowList);
    }

}