package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class MS_StoreList extends ArrayAdapter<MS_Store> {
    private Activity context;
    private List<MS_Store> storeList;

    public MS_StoreList(Activity context, List<MS_Store> storeList){
        super(context, R.layout.ms_store_list,storeList);
        this.context = context;
        this.storeList = storeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ms_store_list,null,true);

        TextView viewStoreName = (TextView) listViewItem.findViewById(R.id.listViewStoreName);
        TextView viewStoreAddress = (TextView) listViewItem.findViewById(R.id.listViewStoreAddress);

        MS_Store msStore = storeList.get(position);

        viewStoreName.setText("Store: " + msStore.getStoreName());
        viewStoreAddress.setText("Address: " + msStore.getStoreAddress());


        return listViewItem;
    }
}
