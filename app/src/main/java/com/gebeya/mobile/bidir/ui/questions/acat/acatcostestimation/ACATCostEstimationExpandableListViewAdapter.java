package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel K. on 3/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCostEstimationExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    ViewHolder viewHolder;
    private List<String> subTotalHeader;
    private HashMap<String, List<String>> subTotalChild;
    public ACATCostEstimationExpandableListViewAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild, List<String> subTotalHeader, HashMap<String, List<String>> subTotalChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.subTotalHeader = subTotalHeader;
        this.subTotalChild = subTotalChild;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
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
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosition);
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
        String groupSubTotal = (String) getGroupSubTotalPosition(groupPosition);
        LayoutInflater inflater;

        if (convertView == null) {
            inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.input_and_activities_group_header_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }

        viewHolder.expandableListViewHeader = convertView.findViewById(R.id.expandable_list_view_group_header);
        viewHolder.expandableListHeaderSubtotal = convertView.findViewById(R.id.subtotal_item_group_label);
        viewHolder.groupIndicatorImage = convertView.findViewById(R.id.expandable_list_view_group_indicator);
        viewHolder.expandableListViewHeader.setText(headerTitle);
        viewHolder.expandableListHeaderSubtotal.setText(groupSubTotal);
        if (isExpanded) {
            viewHolder.groupIndicatorImage.setImageResource(R.drawable.ic_expand_less);
        } else {
            viewHolder.groupIndicatorImage.setImageResource(R.drawable.ic_expand_more);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLstChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        String childSubtotal = (String) getChildSubTotalPosition(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.input_and_activities_cost_list_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }

        viewHolder.expandableListViewChildList = convertView.findViewById(R.id.expandable_list_view_child_item_label);
        viewHolder.expandableListChildSubtotal = convertView.findViewById(R.id.subtotal_item_label);
        viewHolder.expandableListViewChildList.setText(childTitle);
        viewHolder.expandableListChildSubtotal.setText(childSubtotal);

        return convertView;
    }

    private Object getChildSubTotalPosition(int groupPosition, int childPosition) {
        return this.subTotalChild.get(this.subTotalHeader.get(groupPosition))
                .get(childPosition);
    }
    private Object getGroupSubTotalPosition(int groupPosition) {
        return this.subTotalHeader.get(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        TextView expandableListViewHeader;
        TextView expandableListHeaderSubtotal;
        TextView expandableListViewChildList;
        TextView expandableListChildSubtotal;
        ImageView groupIndicatorImage;
    }
}
