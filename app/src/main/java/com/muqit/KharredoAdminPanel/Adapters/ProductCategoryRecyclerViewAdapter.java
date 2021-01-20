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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.CategoriesData;
import com.muqit.KharredoAdminPanel.Models.CategoriesResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryRecyclerViewAdapter extends Adapter<ProductCategoryRecyclerViewAdapter.ProductCategoryItemsViewHolder> implements Filterable {
     ArrayList<CategoriesData> tempParentCategory = new ArrayList<>();
    private Filter ProductsCategoryFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(ProductCategoryRecyclerViewAdapter.this.categoriesDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();

                for (CategoriesData categoriesData : ProductCategoryRecyclerViewAdapter.this.categoriesDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(categoriesData.getID()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(categoriesData.getName().toLowerCase()).contains(trim) || SalesRecyclerViewAdapter.blankIfNull(categoriesData.getCommission()).toLowerCase().contains(trim)) {
                        arrayList.add(categoriesData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ProductCategoryRecyclerViewAdapter.this.categoriesDataArrayList.clear();
            ProductCategoryRecyclerViewAdapter.this.categoriesDataArrayList.addAll((List) filterResults.values);
            ProductCategoryRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    /* access modifiers changed from: private */
    public TextInputEditText CategoryName;
    /* access modifiers changed from: private */
    public TextInputEditText Commission;
    public String IMAGE_BASE_URL = "https://kharredo.com/images/";
    /* access modifiers changed from: private */
    public ArrayList<CategoriesData> categoriesDataArrayList;
    public ArrayList<CategoriesData> categoriesDataArrayListFullList;
    /* access modifiers changed from: private */
    public Context context;
    CategoriesData parentcategory = null;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    @Override
    public Filter getFilter() {
        return ProductsCategoryFilter;
    }

    public class ProductCategoryItemsViewHolder extends ViewHolder {
        public MaterialTextView CategoryName;
        public MaterialTextView CategoryParentName;
        public MaterialButton Category_Delete_BTN;
        public MaterialButton Category_Edit_BTN;
        public ImageView Category_IV;
        public MaterialTextView Commision;
        public MaterialTextView ID;

        public ProductCategoryItemsViewHolder(View view) {
            super(view);
            this.ID = (MaterialTextView) view.findViewById(R.id.Products_category_id);
            this.Commision = (MaterialTextView) view.findViewById(R.id.Products_commision);
            this.CategoryParentName = (MaterialTextView) view.findViewById(R.id.Products_category_parent);
            this.CategoryName = (MaterialTextView) view.findViewById(R.id.Products_category_name);
            this.Category_IV = (ImageView) view.findViewById(R.id.Product_category_image);
            this.Category_Edit_BTN = (MaterialButton) view.findViewById(R.id.Products_category_edit_btn);
            this.Category_Delete_BTN = (MaterialButton) view.findViewById(R.id.Products_category_delete_btn);
        }
    }

    public ProductCategoryRecyclerViewAdapter(ArrayList<CategoriesData> arrayList, Context context2) {
        this.categoriesDataArrayList = arrayList;
        this.context = context2;
        this.categoriesDataArrayListFullList = new ArrayList(arrayList);
    }

    public ProductCategoryItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProductCategoryItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_category_item_layout, viewGroup, false));
    }

    int index = -1;
    public void onBindViewHolder(ProductCategoryItemsViewHolder productCategoryItemsViewHolder, int i) {
        final CategoriesData categoriesData = (CategoriesData) this.categoriesDataArrayList.get(i);
        String str = "0";
        if (categoriesData.getCategory_ID().trim().equals(str)) {
            productCategoryItemsViewHolder.CategoryParentName.setText("");
        } else {
            Iterator it = this.categoriesDataArrayList.iterator();

            while (it.hasNext()) {
                index++;
                CategoriesData categoriesData2 = (CategoriesData) it.next();
                if (categoriesData2.getID().trim().equals(categoriesData.getCategory_ID().trim()) && categoriesData.getCategory_ID().trim() != str) {
                    this.parentcategory = categoriesData2;
                    break;
                }
            }
            if (this.parentcategory != null) {
                productCategoryItemsViewHolder.CategoryParentName.setText(this.parentcategory.getName());
            }
        }

        categoriesDataArrayList.get(i).setPName(productCategoryItemsViewHolder.CategoryParentName.getText().toString());
        productCategoryItemsViewHolder.ID.setText(categoriesData.getID());
        productCategoryItemsViewHolder.CategoryName.setText(categoriesData.getName());
        productCategoryItemsViewHolder.Commision.setText(categoriesData.getCommission());
        Picasso picasso = Picasso.get();
        StringBuilder sb = new StringBuilder();
        sb.append(this.IMAGE_BASE_URL);
        sb.append(categoriesData.getPhotoURL());
        picasso.load(sb.toString()).into(productCategoryItemsViewHolder.Category_IV);
        if (productCategoryItemsViewHolder.Category_IV.getDrawable() == null) {
            productCategoryItemsViewHolder.Category_IV.setImageResource(R.drawable.noimage);
        }
        productCategoryItemsViewHolder.Category_Edit_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = LayoutInflater.from(ProductCategoryRecyclerViewAdapter.this.context).inflate(R.layout.categroy_layout, null);
                ProductCategoryRecyclerViewAdapter.this.Commission = (TextInputEditText) inflate.findViewById(R.id.category_commission);
                ProductCategoryRecyclerViewAdapter.this.CategoryName = (TextInputEditText) inflate.findViewById(R.id.category_name);
                ProductCategoryRecyclerViewAdapter.this.CategoryName.setText(categoriesData.getName());
                String str = "Save";
                new MaterialAlertDialogBuilder(ProductCategoryRecyclerViewAdapter.this.context).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProductCategoryRecyclerViewAdapter.this.progressDialog = new ProgressDialog(ProductCategoryRecyclerViewAdapter.this.context);
                        ProductCategoryRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                        ProductCategoryRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                        ProductCategoryRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                        ProductCategoryRecyclerViewAdapter.this.progressDialog.show();
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateCategory(categoriesData.getID(), ProductCategoryRecyclerViewAdapter.this.Commission.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                Toast.makeText(ProductCategoryRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                                ProductCategoryRecyclerViewAdapter.this.RefreshData();
                                ProductCategoryRecyclerViewAdapter.this.progressDialog.dismiss();
                            }

                            public void onFailure(Call<CommonResponse> call, Throwable th) {
                                Toast.makeText(ProductCategoryRecyclerViewAdapter.this.context, "Something Went Wrong......", Toast.LENGTH_LONG).show();
                                ProductCategoryRecyclerViewAdapter.this.progressDialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false).show();
            }
        });
        productCategoryItemsViewHolder.Category_Delete_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductCategoryRecyclerViewAdapter.this.progressDialog = new ProgressDialog(ProductCategoryRecyclerViewAdapter.this.context);
                ProductCategoryRecyclerViewAdapter.this.progressDialog.setMessage("Blocking....");
                ProductCategoryRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                ProductCategoryRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                ProductCategoryRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).DeleteCategory(categoriesData.getID()).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(ProductCategoryRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        ProductCategoryRecyclerViewAdapter.this.RefreshData();
                        ProductCategoryRecyclerViewAdapter.this.progressDialog.dismiss();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(ProductCategoryRecyclerViewAdapter.this.context, "Something Went Wrong......", Toast.LENGTH_LONG).show();
                        ProductCategoryRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void RefreshData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getCategoriesDetails().enqueue(new Callback<CategoriesResponse>() {
            public void onFailure(Call<CategoriesResponse> call, Throwable th) {
            }

            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                ProductCategoryRecyclerViewAdapter.this.categoriesDataArrayList = ((CategoriesResponse) response.body()).getCategoriesData();
                ProductCategoryRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public int getItemCount() {
        return this.categoriesDataArrayList.size();


    }


}
