package com.example.apple.pdfgenerator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

class InvoiceAdapter extends ArrayAdapter<InvoiceItems> {


    List<InvoiceItems> ItemsList;
    Context context;
    TextView sno,particulars,amount,quantity;

    public InvoiceAdapter(Context context, List<InvoiceItems> heroList) {
        super(context, R.layout.invoice_items, heroList);
        this.context = context;
        this.ItemsList = heroList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.invoice_items, null, false);

        final InvoiceItems invoiceItem = ItemsList.get(position);
        sno =  view.findViewById(R.id.sno);
        particulars =  view.findViewById(R.id.particulars);
        amount =  view.findViewById(R.id.amount);
        quantity =  view.findViewById(R.id.quantity);

        sno.setText(invoiceItem.getSno());
        amount.setText(invoiceItem.getAmount());
        quantity.setText(invoiceItem.getQuantity());
        particulars.setText(invoiceItem.getParticulars());

        return view;
    }
}