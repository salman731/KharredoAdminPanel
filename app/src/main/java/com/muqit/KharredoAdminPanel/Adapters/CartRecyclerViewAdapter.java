package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Activity.CartActivity;
import com.muqit.KharredoAdminPanel.Models.BannersResponse;
import com.muqit.KharredoAdminPanel.Models.BlogResponse;
import com.muqit.KharredoAdminPanel.Models.CartData;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.ProductsData;
import com.muqit.KharredoAdminPanel.Models.ProductsResponse;
import com.muqit.KharredoAdminPanel.Models.VendorsResponse;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartItemsViewHolder> implements Filterable {

    private Filter cartFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(CartRecyclerViewAdapter.this.cartDataArrayFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (CartData cartData : CartRecyclerViewAdapter.this.cartDataArrayFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(cartData.getProductName()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(cartData.getQuantity()).toLowerCase().contains(trim) ) {
                        arrayList.add(cartData);
                    }

                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            CartRecyclerViewAdapter.this.cartDataArrayList.clear();
            CartRecyclerViewAdapter.this.cartDataArrayList.addAll((List) filterResults.values);
            CartRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    ArrayList<CartData> cartDataArrayList;
    List<CartData> cartDataArrayFullList;
    Context context;
    String UID;

    public ProductsData ProductDataforID;

    public CartRecyclerViewAdapter(ArrayList<CartData> arrayList, Context context2,String id) {
        this.cartDataArrayList = arrayList;
        this.context = context2;
        this.UID = id;
        this.cartDataArrayFullList = new ArrayList<>(arrayList);
    }
    @NonNull
    @Override
    public CartRecyclerViewAdapter.CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CartRecyclerViewAdapter.CartItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false));
    }
    Spinner ProductSpinner;
    TextInputEditText Quantity;
    List<ProductsData> productsDataList;
    List<String> ProductList;
    MaterialTextView titletv;
    ProgressDialog progressDialog;
    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        CartData cartData = cartDataArrayList.get(position);
        holder.ProductName.setText(cartData.getProductName());
        holder.ProductQuantity.setText(cartData.getQuantity());

        holder.Edit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View inflate = LayoutInflater.from(context).inflate(R.layout.new_cart_product_layout, null);
                ProductSpinner = (Spinner) inflate.findViewById(R.id.cart_Products_Spinner);
                Quantity = inflate.findViewById(R.id.cart_product_quantity);
                titletv = inflate.findViewById(R.id.cart_Edit_Title_tv);
                titletv.setText("Update Product");
                Quantity.setText(cartData.getQuantity());
                ProductList = new ArrayList();
                productsDataList = new ArrayList();
                ProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final Call productsDetails = ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getProductsDetails();
                productsDetails.enqueue(new Callback<ProductsResponse>() {
                    public void onFailure(Call<ProductsResponse> call, Throwable th) {
                    }

                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        Iterator it = ((ProductsResponse) response.body()).getProductsData().iterator();
                        while (it.hasNext()) {
                            ProductsData productsData = (ProductsData) it.next();
                            CartRecyclerViewAdapter.this.ProductList.add(productsData.getName());
                            CartRecyclerViewAdapter.this.productsDataList.add(productsData);
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, CartRecyclerViewAdapter.this.ProductList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        CartRecyclerViewAdapter.this.ProductSpinner.setAdapter(arrayAdapter);
                        int selectionPosition= arrayAdapter.getPosition(cartData.getProductName());
                        ProductSpinner.setSelection(selectionPosition);

                    }
                });
                new MaterialAlertDialogBuilder(context).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) "Update", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CartRecyclerViewAdapter.this.progressDialog = new ProgressDialog(context);
                        CartRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                        CartRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                        CartRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                        CartRecyclerViewAdapter.this.progressDialog.show();
                        for (ProductsData productsData : CartRecyclerViewAdapter.this.productsDataList) {
                            if (productsData.getName().equals(CartRecyclerViewAdapter.this.ProductSpinner.getSelectedItem().toString())) {
                                CartRecyclerViewAdapter.this.ProductDataforID = productsData;
                            }
                        }
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).updateCartProduct(cartData.getID(),ProductDataforID.getID(),Quantity.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                                RefreshData();
                                CartRecyclerViewAdapter.this.progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(context,t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                RefreshData();
                                CartRecyclerViewAdapter.this.progressDialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false).show();
            }
        });
        holder.Delete_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(CartRecyclerViewAdapter.this.context);
                progressDialog.setMessage("Deleting....");
                progressDialog.setProgressStyle(0);
                progressDialog.setCancelable(false);
                progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).deleteCartProduct(cartData.getID()).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        CartRecyclerViewAdapter.this.cartDataArrayList.remove(position);
                        CartRecyclerViewAdapter.this.notifyItemRemoved(position);
                        CartRecyclerViewAdapter.this.notifyItemRangeChanged(position, CartRecyclerViewAdapter.this.cartDataArrayList.size());
                        progressDialog.dismiss();
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context,t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void RefreshData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getUserCartProducts(UID).enqueue(new Callback<ArrayList<CartData>>() {
            @Override
            public void onResponse(Call<ArrayList<CartData>> call, Response<ArrayList<CartData>> response) {
                CartRecyclerViewAdapter.this.cartDataArrayList = response.body();
                CartRecyclerViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<CartData>> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartDataArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return cartFilter;
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView ProductName,ProductQuantity;
        public MaterialButton Edit_BTN,Delete_BTN;

        public CartItemsViewHolder(View view) {
            super(view);
            this.ProductName = (MaterialTextView) view.findViewById(R.id.Cart_product_name);
            this.ProductQuantity = (MaterialTextView) view.findViewById(R.id.Cart_product_quantity);
            this.Edit_BTN  = (MaterialButton) view.findViewById(R.id.Cart_edit_btn);
            this.Delete_BTN  = (MaterialButton) view.findViewById(R.id.Cart_delete_btn);
        }
    }
}
