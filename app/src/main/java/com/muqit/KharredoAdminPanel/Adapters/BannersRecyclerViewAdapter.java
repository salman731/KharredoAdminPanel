package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Fragments.DeleteBannerResponse;
import com.muqit.KharredoAdminPanel.Models.BannersResponse.BannersData;
import com.muqit.KharredoAdminPanel.Models.EmployeesData;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannersRecyclerViewAdapter extends Adapter<BannersRecyclerViewAdapter.BannersItemsViewHolder> implements Filterable {
    String IMAGE_BASE_URL = "https://kharredo.com/images/banners/";
    /* access modifiers changed from: private */
    public ArrayList<BannersData> bannersDataArrayList;
    public List<BannersData> bannersDataArrayListFullList;

    /* access modifiers changed from: private */
    public Context context;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private Filter BannersFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(BannersRecyclerViewAdapter.this.bannersDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (BannersData bannersData : BannersRecyclerViewAdapter.this.bannersDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(bannersData.getID()).toLowerCase().contains(trim) ) {
                        arrayList.add(bannersData);
                    }

                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            BannersRecyclerViewAdapter.this.bannersDataArrayList.clear();
            BannersRecyclerViewAdapter.this.bannersDataArrayList.addAll((List) filterResults.values);
            BannersRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };

    public BannersRecyclerViewAdapter(ArrayList<BannersData> arrayList, Context context2) {
        this.bannersDataArrayList = arrayList;
        this.context = context2;
        this.bannersDataArrayListFullList = new ArrayList<>(arrayList);
    }

    public BannersItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BannersItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.banners_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(BannersItemsViewHolder bannersItemsViewHolder, final int index) {
        final BannersData bannersData = this.bannersDataArrayList.get(index);
        bannersItemsViewHolder.ID.setText(bannersData.getID());
        bannersItemsViewHolder.Heading.setText("");
        Picasso picasso = Picasso.get();
        StringBuilder sb = new StringBuilder();
        sb.append(this.IMAGE_BASE_URL);
        sb.append(bannersData.getCoverURL());
        picasso.load(sb.toString()).into(bannersItemsViewHolder.Banners_IV);
        if (bannersItemsViewHolder.Banners_IV.getDrawable() == null) {
            bannersItemsViewHolder.Banners_IV.setImageResource(R.drawable.noimage);
        }
        bannersItemsViewHolder.Banners_delete_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "No";
                String str2 = "Yes";
                BannersRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(BannersRecyclerViewAdapter.this.context).setTitle((CharSequence) "Delete?").setMessage((CharSequence) "Are you sure you want to delete this?").setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(str,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(BannersRecyclerViewAdapter.this.context);
                        progressDialog.setMessage("Deleting....");
                        progressDialog.setProgressStyle(0);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).Deletebanner(Integer.valueOf(bannersData.getID()).intValue()).enqueue(new Callback<DeleteBannerResponse>() {
                            public void onResponse(Call<DeleteBannerResponse> call, Response<DeleteBannerResponse> response) {
                                BannersRecyclerViewAdapter.this.bannersDataArrayList.remove(index);
                                BannersRecyclerViewAdapter.this.notifyItemRemoved(index);
                                BannersRecyclerViewAdapter.this.notifyItemRangeChanged(index, BannersRecyclerViewAdapter.this.bannersDataArrayList.size());
                                progressDialog.dismiss();
                            }

                            public void onFailure(Call<DeleteBannerResponse> call, Throwable th) {
                                Toast.makeText(BannersRecyclerViewAdapter.this.context, "Check Your Internet Connection and Try Again......", 1).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
                BannersRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
            }
        });
    }

    public int getItemCount() {
        return this.bannersDataArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return BannersFilter;
    }

    public class BannersItemsViewHolder extends ViewHolder {
        public ImageView Banners_IV;
        public MaterialButton Banners_delete_btn;
        public MaterialTextView Heading;
        public MaterialTextView ID;

        public BannersItemsViewHolder(View view) {
            super(view);
            this.ID = (MaterialTextView) view.findViewById(R.id.Banners_id);
            this.Heading = (MaterialTextView) view.findViewById(R.id.Banners_heading);
            this.Banners_IV = (ImageView) view.findViewById(R.id.banner_image);
            this.Banners_delete_btn = (MaterialButton) view.findViewById(R.id.Banners_delete_btn);
        }
    }
}
