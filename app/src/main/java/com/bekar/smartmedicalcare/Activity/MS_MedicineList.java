package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class MS_MedicineList extends ArrayAdapter<MS_Medicine> {
    private Activity context;
    private List<MS_Medicine> medicineList;

    public MS_MedicineList(Activity context, List<MS_Medicine> medicineList){
        super(context, R.layout.ms_medicine_list,medicineList);
        this.context = context;
        this.medicineList = medicineList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ms_medicine_list,null,true);

        TextView viewMedicineName = (TextView) listViewItem.findViewById(R.id.viewMedicineName);
        TextView viewCompanyName = (TextView) listViewItem.findViewById(R.id.viewCompanyName);
        TextView viewPrice = (TextView) listViewItem.findViewById(R.id.viewPrice);
        TextView viewStock = (TextView) listViewItem.findViewById(R.id.viewStock);

        MS_Medicine msMedicine = medicineList.get(position);

        viewMedicineName.setText(msMedicine.getMedicineName());
        viewCompanyName.setText("Company: " + msMedicine.getCompanyName());
        viewPrice.setText("Price: " + msMedicine.getMedicinePrice() + " (BDT)");
        viewStock.setText(msMedicine.getAvailable());


        return listViewItem;
    }
}
