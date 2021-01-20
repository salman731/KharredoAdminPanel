package com.muqit.KharredoAdminPanel.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.CustomerResposne;
import com.muqit.KharredoAdminPanel.Models.SalesData;
import com.muqit.KharredoAdminPanel.Models.SalesDetail;
import com.muqit.KharredoAdminPanel.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SalesRecyclerViewAdapter extends Adapter<SalesRecyclerViewAdapter.SalesItemsViewHolder> implements Filterable {
    private Filter SalesFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(SalesRecyclerViewAdapter.this.salesDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (SalesData salesData : SalesRecyclerViewAdapter.this.salesDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(salesData.getFirstName()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(salesData.getLastName()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(String.valueOf(salesData.getTotalAmount())).contains(trim) || SalesRecyclerViewAdapter.blankIfNull(salesData.getSalesDate()).toLowerCase().contains(trim) || (salesData.getFirstName().toLowerCase() + " "+ salesData.getLastName().toLowerCase()).contains(trim)) {

                        arrayList.add(salesData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            SalesRecyclerViewAdapter.this.salesDataArrayList.clear();
            SalesRecyclerViewAdapter.this.salesDataArrayList.addAll((List) filterResults.values);
            SalesRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    private Context context;
    /* access modifiers changed from: private */
    public ArrayList<SalesData> salesDataArrayList;
    /* access modifiers changed from: private */
    public List<SalesData> salesDataArrayListFullList;
    public Retrofit retrofit;
    public MaterialAlertDialogBuilder materialAlertDialogBuilder;

    public class SalesItemsViewHolder extends ViewHolder {
        public MaterialTextView AmountTV;
        public MaterialTextView BuyerNameTV;
        public MaterialTextView DateTV;
        public MaterialTextView TransationNoTV;
        public MaterialButton SalesDetails_BTN;

        public SalesItemsViewHolder(View view) {
            super(view);
            this.DateTV = (MaterialTextView) view.findViewById(R.id.Sales_date);
            this.BuyerNameTV = (MaterialTextView) view.findViewById(R.id.Sales_buyer_name);
            this.TransationNoTV = (MaterialTextView) view.findViewById(R.id.Sales_transaction_no);
            this.AmountTV = (MaterialTextView) view.findViewById(R.id.Sales_amount);
            this.SalesDetails_BTN = (MaterialButton) view.findViewById(R.id.Sale_view_detail);

        }
    }

    public static String blankIfNull(String str) {
        return str == null ? "" : str;
    }

    public SalesRecyclerViewAdapter(ArrayList<SalesData> arrayList, Context context2) {
        this.salesDataArrayList = arrayList;
        this.context = context2;
        this.salesDataArrayListFullList = new ArrayList(arrayList);
    }

    public SalesItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SalesItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sale_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(SalesItemsViewHolder salesItemsViewHolder, int i) {
        SalesData salesData = (SalesData) this.salesDataArrayList.get(i);
        salesItemsViewHolder.DateTV.setText(salesData.getSalesDate().trim());
        MaterialTextView materialTextView = salesItemsViewHolder.BuyerNameTV;
        StringBuilder sb = new StringBuilder();
        sb.append(blankIfNull(salesData.getFirstName()));
        sb.append(" ");
        sb.append(blankIfNull(salesData.getLastName()));
        materialTextView.setText(sb.toString());
        salesItemsViewHolder.TransationNoTV.setText(salesData.getPayID() == null ? "" : salesData.getPayID());
        salesItemsViewHolder.AmountTV.setText(String.valueOf(salesData.getTotalAmount()));
        salesItemsViewHolder.SalesDetails_BTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View inflate2 = LayoutInflater.from(SalesRecyclerViewAdapter.this.context).inflate(R.layout.sales_details_layout, null);
                MaterialTextView date = inflate2.findViewById(R.id.Sales_details_date);
                MaterialTextView productname = inflate2.findViewById(R.id.Sales_details_product_name);
                MaterialTextView totalamount = inflate2.findViewById(R.id.Sales_details_product_Total);
                MaterialTextView price = inflate2.findViewById(R.id.Sales_details_product_Price);
                MaterialTextView quantity = inflate2.findViewById(R.id.Sales_details_product_Quantity);
                MaterialTextView subtotal = inflate2.findViewById(R.id.Sales_details_product_Subtotal);
                MaterialTextView transaction = inflate2.findViewById(R.id.Sales_details_Transaction);
                Call<ArrayList<SalesDetail>> salesdetailscall = new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class).getSaleDetail(salesData.getID());
                salesdetailscall.enqueue(new Callback<ArrayList<SalesDetail>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SalesDetail>> call, Response<ArrayList<SalesDetail>> response) {
                        date.setText(salesData.getSalesDate());
                        productname.setText(response.body().get(0).ProductName);
                        totalamount.setText(response.body().get(0).TotalAmount);
                        quantity.setText(response.body().get(0).TotalQuantity);
                        subtotal.setText(response.body().get(0).TotalAmount);
                        price.setText(response.body().get(0).Price);
                        transaction.setText(salesData.getPayID() == null ? " " :salesData.getPayID());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SalesDetail>> call, Throwable t) {
                        Toast.makeText(context,"Check your internet connection.....",Toast.LENGTH_LONG).show();
                    }
                });
                SalesRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(SalesRecyclerViewAdapter.this.context).setView(inflate2).setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                SalesRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
            }
        });
    }

    public int getItemCount() {
        return this.salesDataArrayList.size();
    }

    public Filter getFilter() {
        return this.SalesFilter;
    }
}
