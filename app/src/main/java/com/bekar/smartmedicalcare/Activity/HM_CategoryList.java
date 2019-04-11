package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class HM_CategoryList extends ArrayAdapter<HM_Category> {
    private Activity context;
    private List<HM_Category> categoryList;

    public HM_CategoryList(Activity context, List<HM_Category> categoryList){
        super(context, R.layout.hm_category_list,categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.hm_category_list,null,true);

        TextView viewCategoryName = (TextView) listViewItem.findViewById(R.id.viewCategoryName);
        HM_Category hmCategory = categoryList.get(position);

        viewCategoryName.setText(hmCategory.getCategoryName());

        return listViewItem;
    }
}
