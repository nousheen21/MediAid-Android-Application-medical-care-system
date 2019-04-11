package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class HM_ElementList extends ArrayAdapter<HM_Element> {
    private Activity context;
    private List<HM_Element> elementList;

    public HM_ElementList(Activity context, List<HM_Element> elementList){
        super(context, R.layout.hm_element_list,elementList);
        this.context = context;
        this.elementList = elementList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.hm_element_list,null,true);

        TextView viewElementName = (TextView) listViewItem.findViewById(R.id.viewElementName);
        TextView viewElementInfo = (TextView) listViewItem.findViewById(R.id.viewElementInfo);
        TextView viewElementDept = (TextView) listViewItem.findViewById(R.id.viewElementDept);
        HM_Element element = elementList.get(position);

        viewElementName.setText(element.getElementName());
        viewElementInfo.setText(element.getElementInfo());
        viewElementDept.setText(element.getElementDept());

        return listViewItem;
    }
}
