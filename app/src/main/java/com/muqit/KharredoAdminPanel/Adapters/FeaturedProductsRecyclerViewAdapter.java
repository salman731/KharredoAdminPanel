package com.muqit.KharredoAdminPanel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.Models.FeaturedProductsData;
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeaturedProductsRecyclerViewAdapter extends Adapter<FeaturedProductsRecyclerViewAdapter.FeaturedProductsItemsViewHolder> implements Filterable {
    private Filter FeaturedProductFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(FeaturedProductsRecyclerViewAdapter.this.featuredProductsDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (FeaturedProductsData featuredProductsData : FeaturedProductsRecyclerViewAdapter.this.featuredProductsDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(featuredProductsData.getName()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(featuredProductsData.getPrice()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(featuredProductsData.getFeaturedEnd()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(featuredProductsData.getFeaturedStart()).toLowerCase().contains(trim)) {
                        arrayList.add(featuredProductsData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            FeaturedProductsRecyclerViewAdapter.this.featuredProductsDataArrayList.clear();
            FeaturedProductsRecyclerViewAdapter.this.featuredProductsDataArrayList.addAll((List) filterResults.values);
            FeaturedProductsRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    public String IMAGE_BASE_URL = "https://kharredo.com/images/";
    private Context contxt;
    private ArrayList<FeaturedProductsData> featuredProductsDataArrayList;
    private ArrayList<FeaturedProductsData> featuredProductsDataArrayListFullList;

    @Override
    public Filter getFilter() {
        return FeaturedProductFilter;
    }

    public class FeaturedProductsItemsViewHolder extends ViewHolder {
        public MaterialTextView FeatureEnd;
        public ImageView FeatureProduct_IV;
        public MaterialTextView FeatureStart;
        public MaterialTextView Name;
        public MaterialTextView Price;

        public FeaturedProductsItemsViewHolder(View view) {
            super(view);
            this.Name = (MaterialTextView) view.findViewById(R.id.Featured_product_name);
            this.Price = (MaterialTextView) view.findViewById(R.id.Featured_product_price);
            this.FeatureStart = (MaterialTextView) view.findViewById(R.id.Featured_product_start);
            this.FeatureEnd = (MaterialTextView) view.findViewById(R.id.Featured_product_end);
            this.FeatureProduct_IV = (ImageView) view.findViewById(R.id.Featured_product_image);
        }
    }

    public FeaturedProductsRecyclerViewAdapter(ArrayList<FeaturedProductsData> arrayList, Context context) {
        this.featuredProductsDataArrayList = arrayList;
        this.contxt = context;
        this.featuredProductsDataArrayListFullList = new ArrayList(arrayList);
    }

    public FeaturedProductsItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FeaturedProductsItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.featured_products_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(FeaturedProductsItemsViewHolder featuredProductsItemsViewHolder, int i) {
        FeaturedProductsData featuredProductsData = (FeaturedProductsData) this.featuredProductsDataArrayList.get(i);
        featuredProductsItemsViewHolder.Name.setText(featuredProductsData.getName());
        featuredProductsItemsViewHolder.Price.setText(featuredProductsData.getPrice());
        featuredProductsItemsViewHolder.FeatureEnd.setText(featuredProductsData.getFeaturedEnd());
        featuredProductsItemsViewHolder.FeatureStart.setText(featuredProductsData.getFeaturedStart());
        Picasso picasso = Picasso.get();
        StringBuilder sb = new StringBuilder();
        sb.append(this.IMAGE_BASE_URL);
        sb.append(featuredProductsData.getPhotoURL());
        picasso.load(sb.toString()).into(featuredProductsItemsViewHolder.FeatureProduct_IV);
        if (featuredProductsItemsViewHolder.FeatureProduct_IV.getDrawable() == null) {
            featuredProductsItemsViewHolder.FeatureProduct_IV.setImageResource(R.drawable.noimage);
        }
    }

    public int getItemCount() {
        return this.featuredProductsDataArrayList.size();
    }
}
