package com.gebeya.mobile.bidir.ui.questions.acat;

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
 * Created by Samuel K. on 3/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsCropCashFlowExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ViewHolder viewHolder;

    public ACATQuestionsCropCashFlowExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
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
        LayoutInflater inflater;

        if (convertView == null) {
            inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.crop_cash_flow_expandable_list_header_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        }

        viewHolder.expandableListViewHeader = convertView.findViewById(R.id.expandable_list_view_group_header);
        viewHolder.groupIndicatorImage = convertView.findViewById(R.id.expandable_list_view_group_indicator);
        viewHolder.expandableListViewHeader.setText(headerTitle);
        if (isExpanded) {
            viewHolder.groupIndicatorImage.setImageResource(R.drawable.ic_expand_less);
        } else {
            viewHolder.groupIndicatorImage.setImageResource(R.drawable.ic_expand_more);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.crop_cash_flow_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        }

        viewHolder.expandableListViewChildList = convertView.findViewById(R.id.expandable_list_view_child_item_label);
        viewHolder.expandableListViewChildList.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ViewHolder {
        TextView expandableListViewHeader;
        TextView expandableListViewChildList;
        ImageView groupIndicatorImage;
    }
}
