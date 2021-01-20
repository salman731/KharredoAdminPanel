package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.SalesRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.OrdersData;
import com.muqit.KharredoAdminPanel.Models.SalesData;
import com.muqit.KharredoAdminPanel.Models.SalesResponse;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SalesFragment extends Fragment {
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;
    private ArrayList<SalesData> salesData;
    /* access modifiers changed from: private */
    public RecyclerView salesRecyclerView;
    /* access modifiers changed from: private */
    public SalesRecyclerViewAdapter salesRecyclerViewAdapter;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sales, viewGroup, false);
        this.salesRecyclerView = (RecyclerView) inflate.findViewById(R.id.Sales_recyclerview);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        setHasOptionsMenu(true);
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PoulateData();
        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.sales_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                SalesFragment.this.salesRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    boolean sortAmount = true;
    boolean sortDate = true;
    boolean sortBuyerName = true;
    boolean sortTransaction;
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Sales_byDate:
                if(sortDate)
                {
                    sortDate = false;
                }
                else
                {
                    sortDate = true;
                }
                Collections.sort(salesData, new Comparator<SalesData>() {
                    @Override
                    public int compare(SalesData lhs, SalesData rhs) {
                        return sortDate ? lhs.getSalesDate().compareTo(rhs.getSalesDate()) : rhs.getSalesDate().compareTo(lhs.getSalesDate());}

                });
                salesRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Sales_byBuyerName:
                if(sortBuyerName)
                {
                    sortBuyerName = false;
                }
                else
                {
                    sortBuyerName = true;
                }
                Collections.sort(salesData, new Comparator<SalesData>() {
                    @Override
                    public int compare(SalesData lhs, SalesData rhs) {
                        return sortBuyerName ? (lhs.getSalesDate() + " " + lhs.getLastName()).compareTo(rhs.getSalesDate() + " " + rhs.getLastName()) : (rhs.getSalesDate() + " " + rhs.getLastName()).compareTo(lhs.getSalesDate() + " " + lhs.getLastName());
                    }
                });
                salesRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Sales_byAmount:
                if(sortAmount)
                {
                    sortAmount = false;
                }
                else
                {
                    sortAmount = true;
                }
                Collections.sort(salesData, new Comparator<SalesData>() {
                    @Override
                    public int compare(SalesData lhs, SalesData rhs) {
                        return sortAmount ? (lhs.getTotalAmount() < rhs.getTotalAmount())? -1 : (lhs.getTotalAmount() > rhs.getTotalAmount()) ? 1 : 0 : (lhs.getTotalAmount() > rhs.getTotalAmount())? -1 : (lhs.getTotalAmount() < rhs.getTotalAmount()) ? 1 : 0 ;
                    }
                });
                salesRecyclerViewAdapter.notifyDataSetChanged();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getSalesDetails().enqueue(new Callback<SalesResponse>() {
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                salesData = response.body().getSalesData();
                SalesFragment.this.salesRecyclerViewAdapter = new SalesRecyclerViewAdapter(salesData, SalesFragment.this.getActivity());
                SalesFragment.this.salesRecyclerView.setLayoutManager(new LinearLayoutManager(SalesFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SalesFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(SalesFragment.this.getContext(), R.drawable.divider));
                SalesFragment.this.salesRecyclerView.addItemDecoration(dividerItemDecoration);
                SalesFragment.this.salesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                SalesFragment.this.salesRecyclerView.setAdapter(SalesFragment.this.salesRecyclerViewAdapter);
                SalesFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<SalesResponse> call, Throwable th) {
                Toast.makeText(SalesFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                SalesFragment.this.progressDialog.dismiss();
            }
        });
    }
}
