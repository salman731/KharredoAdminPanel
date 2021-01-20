package com.muqit.KharredoAdminPanel.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.BlogRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Adapters.CartRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Adapters.ChatRecyclerViewAdpater;
import com.muqit.KharredoAdminPanel.Fragments.BannersFragment;
import com.muqit.KharredoAdminPanel.Fragments.ChatFragment;
import com.muqit.KharredoAdminPanel.Fragments.ProductFeaturedFragment;
import com.muqit.KharredoAdminPanel.Models.CartData;
import com.muqit.KharredoAdminPanel.Models.ChatResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.EmployeesData;
import com.muqit.KharredoAdminPanel.Models.ProductsData;
import com.muqit.KharredoAdminPanel.Models.ProductsResponse;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    public CartRecyclerViewAdapter cartRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public RecyclerView cartRecyclerview;
    /* access modifiers changed from: private */
    public ProgressDialog progressdialog;

    private ArrayList<CartData> cartDataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");
        this.cartRecyclerview = (RecyclerView) findViewById(R.id.Cart_recyclerview);
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        PoulateData();

    }

    private void PoulateData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getUserCartProducts(getIntent().getStringExtra("UID")).enqueue(new Callback<ArrayList<CartData>>() {
            @Override
            public void onResponse(Call<ArrayList<CartData>> call, Response<ArrayList<CartData>> response) {
                if(response.body() != null)
                {
                    cartDataArrayList = response.body();
                    CartActivity.this.cartRecyclerViewAdapter = new CartRecyclerViewAdapter(cartDataArrayList, CartActivity.this,getIntent().getStringExtra("UID"));
                    CartActivity.this.cartRecyclerview.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CartActivity.this, 1);
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(CartActivity.this, R.drawable.divider));
                    CartActivity.this.cartRecyclerview.addItemDecoration(dividerItemDecoration);
                    CartActivity.this.cartRecyclerview.setItemAnimator(new DefaultItemAnimator());
                    CartActivity.this.cartRecyclerview.setAdapter(CartActivity.this.cartRecyclerViewAdapter);
                }
                CartActivity.this.progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<CartData>> call, Throwable t) {
                Toast.makeText(CartActivity.this,"Something Went Wrong.....",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
               CartActivity.this.cartRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        return true;
    }

    Spinner ProductSpinner;
    TextInputEditText Quantity;
    List<ProductsData> productsDataList;
    List<String> ProductList;
    ProgressDialog progressDialog;
    public ProductsData ProductDataforID;
    boolean sortName = true;
    boolean sortQuantity = true;
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.cart_new_menu_btn:
                View inflate = LayoutInflater.from(this).inflate(R.layout.new_cart_product_layout, null);
                this.ProductSpinner = (Spinner) inflate.findViewById(R.id.cart_Products_Spinner);
                this.Quantity = inflate.findViewById(R.id.cart_product_quantity);
                this.ProductList = new ArrayList();
                this.productsDataList = new ArrayList();
                this.ProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            CartActivity.this.ProductList.add(productsData.getName());
                            CartActivity.this.productsDataList.add(productsData);
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(CartActivity.this, android.R.layout.simple_spinner_dropdown_item, CartActivity.this.ProductList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        CartActivity.this.ProductSpinner.setAdapter(arrayAdapter);
                    }
                });
                new MaterialAlertDialogBuilder(this).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) "Save", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CartActivity.this.progressDialog = new ProgressDialog(CartActivity.this);
                        CartActivity.this.progressDialog.setMessage("Saving....");
                        CartActivity.this.progressDialog.setProgressStyle(0);
                        CartActivity.this.progressDialog.setCancelable(false);
                        CartActivity.this.progressDialog.show();
                        for (ProductsData productsData : CartActivity.this.productsDataList) {
                            if (productsData.getName().equals(CartActivity.this.ProductSpinner.getSelectedItem().toString())) {
                                CartActivity.this.ProductDataforID = productsData;
                            }
                        }
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).AddCartProduct(getIntent().getStringExtra("UID"),ProductDataforID.getID(),Quantity.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                Toast.makeText(CartActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                                PoulateData();
                                CartActivity.this.progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(CartActivity.this,"Someting went wrong.....",Toast.LENGTH_LONG).show();
                                CartActivity.this.progressDialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false).show();
                return true;
            case R.id.Cart_byName:
                if(sortName)
                {
                    sortName = false;
                }
                else
                {
                    sortName = true;
                }
                Collections.sort(cartDataArrayList, new Comparator<CartData>() {
                    @Override
                    public int compare(CartData lhs, CartData rhs) {
                        return sortName ? lhs.getProductName().compareTo(rhs.getProductName()) : rhs.getProductName().compareTo(lhs.getProductName());
                    }

                });
                cartRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Cart_byQuantity:
                if(sortQuantity)
                {
                    sortQuantity = false;
                }
                else
                {
                    sortQuantity = true;
                }
                Collections.sort(cartDataArrayList, new Comparator<CartData>() {
                    @Override
                    public int compare(CartData lhs, CartData rhs) {
                        return sortQuantity ? lhs.getQuantity().compareTo(rhs.getQuantity()) : rhs.getQuantity().compareTo(lhs.getQuantity());
                    }

                });
                cartRecyclerViewAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}