package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.FeaturedProductsRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.FeaturedProductsData;
import com.muqit.KharredoAdminPanel.Models.ProductsData;
import com.muqit.KharredoAdminPanel.Models.ProductsResponse;
import com.muqit.KharredoAdminPanel.Models.VendorsData;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductFeaturedFragment extends Fragment implements OnItemSelectedListener {
    /* access modifiers changed from: private */
    public RecyclerView FeaturedProductsCategoryRecyclerView;
    /* access modifiers changed from: private */
    public ProductsData ProductDataforID;
    List<String> ProductList;
    /* access modifiers changed from: private */
    public Spinner ProductSpinner;
    /* access modifiers changed from: private */
    public FeaturedProductsRecyclerViewAdapter featuredProductsRecyclerViewAdapter;
    public ArrayList<FeaturedProductsData> featuredProductsData;
    List<ProductsData> productsDataList;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;

    private void PopulateSpinner() {
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_product_featured, viewGroup, false);
        setHasOptionsMenu(true);
        this.FeaturedProductsCategoryRecyclerView = (RecyclerView) inflate.findViewById(R.id.FeaturedProducts_recyclerView);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PoulateData();
        return inflate;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.featured_product_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                ProductFeaturedFragment.this.featuredProductsRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    boolean sortName = true;
    boolean sortPrice = true;
    boolean sortStart = true;
    boolean sortEnd = true;


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.featured_product_new_menu_btn:
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.new_featured_product_layout, null);
                this.ProductSpinner = (Spinner) inflate.findViewById(R.id.Featured_Products_Spinner);
                this.ProductList = new ArrayList();
                this.productsDataList = new ArrayList();
                this.ProductSpinner.setOnItemSelectedListener(this);
                final Call productsDetails = ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getProductsDetails();
                productsDetails.enqueue(new Callback<ProductsResponse>() {
                    public void onFailure(Call<ProductsResponse> call, Throwable th) {
                    }

                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        Iterator it = ((ProductsResponse) response.body()).getProductsData().iterator();
                        while (it.hasNext()) {
                            ProductsData productsData = (ProductsData) it.next();
                            ProductFeaturedFragment.this.ProductList.add(productsData.getName());
                            ProductFeaturedFragment.this.productsDataList.add(productsData);
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(ProductFeaturedFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, ProductFeaturedFragment.this.ProductList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ProductFeaturedFragment.this.ProductSpinner.setAdapter(arrayAdapter);
                    }
                });
                new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) "Save", (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProductFeaturedFragment.this.progressDialog = new ProgressDialog(ProductFeaturedFragment.this.getContext());
                        ProductFeaturedFragment.this.progressDialog.setMessage("Saving....");
                        ProductFeaturedFragment.this.progressDialog.setProgressStyle(0);
                        ProductFeaturedFragment.this.progressDialog.setCancelable(false);
                        ProductFeaturedFragment.this.progressDialog.show();
                        for (ProductsData productsData : ProductFeaturedFragment.this.productsDataList) {
                            if (productsData.getName().equals(ProductFeaturedFragment.this.ProductSpinner.getSelectedItem().toString())) {
                                ProductFeaturedFragment.this.ProductDataforID = productsData;
                            }
                        }
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).AddFeaturedProduct(ProductFeaturedFragment.this.ProductDataforID.getID());
                        productsDetails.enqueue(new Callback<ProductsResponse>() {
                            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                                Toast.makeText(ProductFeaturedFragment.this.getActivity(), ((ProductsResponse) response.body()).getMessage(), 1).show();
                                ProductFeaturedFragment.this.progressDialog.dismiss();
                            }

                            public void onFailure(Call<ProductsResponse> call, Throwable th) {
                                Toast.makeText(ProductFeaturedFragment.this.getActivity(), "Something Went Wrong.....", 1).show();
                                ProductFeaturedFragment.this.progressDialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false).show();
                return true;
            case R.id.FeaturedProduct_byName:
                if(sortName)
                {
                    sortName = false;
                }
                else
                {
                    sortName = true;
                }
                Collections.sort(featuredProductsData, new Comparator<FeaturedProductsData>() {
                    @Override
                    public int compare(FeaturedProductsData lhs, FeaturedProductsData rhs) {
                        return sortName ? lhs.getName().compareTo(rhs.getName()) : rhs.getName().compareTo(lhs.getName());
                    }

                });
                featuredProductsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.FeaturedProduct_byPrice:
                if(sortPrice)
                {
                    sortPrice = false;
                }
                else
                {
                    sortPrice = true;
                }
                Collections.sort(featuredProductsData, new Comparator<FeaturedProductsData>() {
                    @Override
                    public int compare(FeaturedProductsData lhs, FeaturedProductsData rhs) {
                        return sortPrice ? (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice())) ? 1 : 0 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice())) ? 1 : 0;
                    }
                });
                featuredProductsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.FeaturedProduct_byStartDate:
                if(sortStart)
                {
                    sortStart = false;
                }
                else
                {
                    sortStart = true;
                }
                Collections.sort(featuredProductsData, new Comparator<FeaturedProductsData>() {
                    @Override
                    public int compare(FeaturedProductsData lhs, FeaturedProductsData rhs) {
                        return sortStart ? lhs.getFeaturedStart().compareTo(rhs.getFeaturedStart()) : rhs.getFeaturedStart().compareTo(lhs.getFeaturedStart());
                    }
                });
                featuredProductsRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.FeaturedProduct_byEndDate:
                if(sortEnd)
                {
                    sortEnd = false;
                }
                else
                {
                    sortEnd = true;
                }
                Collections.sort(featuredProductsData, new Comparator<FeaturedProductsData>() {
                    @Override
                    public int compare(FeaturedProductsData lhs, FeaturedProductsData rhs) {
                        return sortEnd ? lhs.getFeaturedEnd().compareTo(rhs.getFeaturedEnd()) : rhs.getFeaturedEnd().compareTo(lhs.getFeaturedEnd());

                    }
                });
                featuredProductsRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getFeaturedProductDetails().enqueue(new Callback<ArrayList<FeaturedProductsData>>() {
            public void onResponse(Call<ArrayList<FeaturedProductsData>> call, Response<ArrayList<FeaturedProductsData>> response) {
                featuredProductsData = response.body();
                ProductFeaturedFragment.this.featuredProductsRecyclerViewAdapter = new FeaturedProductsRecyclerViewAdapter(featuredProductsData, ProductFeaturedFragment.this.getActivity());
                ProductFeaturedFragment.this.FeaturedProductsCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(ProductFeaturedFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ProductFeaturedFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ProductFeaturedFragment.this.getContext(), R.drawable.divider));
                ProductFeaturedFragment.this.FeaturedProductsCategoryRecyclerView.addItemDecoration(dividerItemDecoration);
                ProductFeaturedFragment.this.FeaturedProductsCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ProductFeaturedFragment.this.FeaturedProductsCategoryRecyclerView.setAdapter(ProductFeaturedFragment.this.featuredProductsRecyclerViewAdapter);
                ProductFeaturedFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<ArrayList<FeaturedProductsData>> call, Throwable th) {
                Toast.makeText(ProductFeaturedFragment.this.getActivity(), th.getLocalizedMessage(), 1).show();
                ProductFeaturedFragment.this.progressDialog.dismiss();
            }
        });
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        adapterView.getItemAtPosition(i).toString();
    }
}
