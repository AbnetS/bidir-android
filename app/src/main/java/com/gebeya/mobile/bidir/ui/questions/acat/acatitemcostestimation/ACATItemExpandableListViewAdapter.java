package com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel K. on 6/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATItemExpandableListViewAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    ViewHolder viewHolder;

    public ACATItemExpandableListViewAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
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
            convertView = inflater.inflate(R.layout.acat_item_header_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }

        viewHolder.expandableListViewHeader = convertView.findViewById(R.id.expandable_list_view_group_header);
        viewHolder.expandableListViewHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLstChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.acat_item_child_layout, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }

        viewHolder.expandableListViewChildList = convertView.findViewById(R.id.expandable_list_view_child_item_label);
        viewHolder.expandableListViewChildList.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        TextView expandableListViewHeader;
        TextView expandableListViewChildList;
    }

}
