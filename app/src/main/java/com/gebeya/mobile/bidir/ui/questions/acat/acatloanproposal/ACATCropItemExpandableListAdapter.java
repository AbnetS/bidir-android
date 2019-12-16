package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATQuestionsNetCashFlowAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCropItemExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<CropACAT>> listDataChild;
    private List<CashFlow> cashFlow;
    ViewHolder viewHolder;
    private static final int CHILD_TYPE_ONE = 1;
    private static final int CHILD_TYPE_TWO = 2;

    public ACATCropItemExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<CropACAT>> listDataChild, List<CashFlow> cashFlow) {
        this.context = context;
        this.listDataChild = listDataChild;
        this.listDataHeader = listDataHeader;
        this.cashFlow = cashFlow;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        LayoutInflater inflater;

        if (convertView == null) {
            inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loan_assessmet_expandable_group_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }

        viewHolder.expandableListViewGroupHeader = convertView.findViewById(R.id.expandable_list_view_group_header);
        viewHolder.groupIndicatorImage = convertView.findViewById(R.id.expandable_list_view_group_indicator);
        viewHolder.expandableListViewGroupHeader.setText(headerTitle);
        if (isExpanded) {
            viewHolder.groupIndicatorImage.setImageResource(R.drawable.ic_expand_less);
        } else {
            viewHolder.groupIndicatorImage.setImageResource(R.drawable.ic_expand_more);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isListChild, View convertView, ViewGroup parent) {
        CropACAT child = (CropACAT) getChild(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewHolder = new ViewHolder();
        viewHolder.childType = new Integer(getChildType(groupPosition, childPosition));

        if (convertView == null || convertView.getTag() != viewHolder.childType) {
            switch (viewHolder.childType) {
                case CHILD_TYPE_ONE:
                    convertView = inflater.inflate(R.layout.crop_item_cash_flow_layout, null);
                    convertView.setTag(viewHolder);

                    viewHolder.expandableListViewChildLabel = convertView.findViewById(R.id.cropNameLabel);
                    viewHolder.horizontalChildListView = convertView.findViewById(R.id.cropItemCashFlowRecyclerView);
                    LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    viewHolder.horizontalChildListView.setLayoutManager(manager);
                    viewHolder.horizontalChildListView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL));

                    ACATQuestionsNetCashFlowAdapter adapter = new ACATQuestionsNetCashFlowAdapter(child.firstExpenseMonth, new ArrayList<>());
                    viewHolder.horizontalChildListView.setAdapter(adapter);
                    adapter.notify(cashFlow.get(childPosition));
                    viewHolder.expandableListViewChildLabel.setText(child.cropName);

                    break;
                case CHILD_TYPE_TWO:
                    convertView = inflater.inflate(R.layout.crop_income_layout, null);
                    convertView.setTag(viewHolder);

                    viewHolder.expandableListViewChildLabel = convertView.findViewById(R.id.crop_income_crop_label);
                    viewHolder.cropIncomeTotalCost = convertView.findViewById(R.id.loan_assessment_total_cost_label);
                    viewHolder.cropIncomeTotalRevenue = convertView.findViewById(R.id.loan_assessment_total_revenue_label);
                    viewHolder.cropIncomeNet = convertView.findViewById(R.id.loan_assessment_net_income);

                    viewHolder.expandableListViewChildLabel.setText(child.cropName);
                    viewHolder.cropIncomeTotalCost.setText(String.valueOf(child.estimatedTotalCost));
                    viewHolder.cropIncomeTotalRevenue.setText(String.valueOf(child.estimatedTotalRevenue));
                    viewHolder.cropIncomeNet.setText(String.valueOf(child.estimatedNetIncome));
                    break;
                default:
                    break;

            }
        } else {
            convertView.getTag();
        }

        return convertView;
    }

    @Override
    public int getChildTypeCount() {
        return 3;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case 0:
                return CHILD_TYPE_ONE;
            case 1:
                return CHILD_TYPE_TWO;
            default:
                return 0;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ViewHolder {
        TextView expandableListViewGroupHeader;
        TextView expandableListViewChildLabel;
        TextView cropIncomeTotalRevenue;
        TextView cropIncomeTotalCost;
        TextView cropIncomeNet;
        RecyclerView horizontalChildListView;
        ImageView groupIndicatorImage;
        Integer childType;
    }
}

