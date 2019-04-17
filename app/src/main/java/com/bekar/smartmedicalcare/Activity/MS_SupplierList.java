package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class MS_SupplierList extends ArrayAdapter<MS_Supplier> {
    private Activity context;
    private List<MS_Supplier> supplierList;

    public MS_SupplierList(Activity context, List<MS_Supplier> supplierList){
        super(context, R.layout.ms_supplier_list,supplierList);
        this.context = context;
        this.supplierList = supplierList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ms_supplier_list,null,true);

        TextView viewSupplierName = (TextView) listViewItem.findViewById(R.id.viewSupplierName);
        TextView viewSupplierCompany = (TextView) listViewItem.findViewById(R.id.viewSupplierCompany);
        TextView viewStoreName = (TextView) listViewItem.findViewById(R.id.viewStoreName);

        MS_Supplier msSupplier = supplierList.get(position);

        viewSupplierName.setText(msSupplier.getSupplierName());
        viewSupplierCompany.setText("Company: " + msSupplier.getSupplierCompany());
        viewStoreName.setText("Store: " + msSupplier.getStoreName());


        return listViewItem;
    }
}
