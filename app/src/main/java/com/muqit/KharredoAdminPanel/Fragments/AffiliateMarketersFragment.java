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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.AffiliateMarketersRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.AffiliateMarketersData;
import com.muqit.KharredoAdminPanel.Models.AffiliateMarketersResponse;
import com.muqit.KharredoAdminPanel.Models.CartData;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AffiliateMarketersFragment extends Fragment {
    /* access modifiers changed from: private */
    public RecyclerView AffiliateMarketersRecyclerView;
    private ArrayList<AffiliateMarketersData> affiliateMarketersData;
    /* access modifiers changed from: private */
    public AffiliateMarketersRecyclerViewAdapter affiliateMarketersRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_affiliate_marketers, viewGroup, false);
        this.AffiliateMarketersRecyclerView = (RecyclerView) inflate.findViewById(R.id.Affiliate_marketers_recyclerView);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        setHasOptionsMenu(true);
        PoulateData();
        return inflate;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.affiliatemarketers_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                AffiliateMarketersFragment.this.affiliateMarketersRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    boolean isAscending = false;
    boolean isDescending = true;
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.AM_byAscending:
                Collections.sort(affiliateMarketersData, new Comparator<AffiliateMarketersData>() {
                    @Override
                    public int compare(AffiliateMarketersData lhs, AffiliateMarketersData rhs) {
                        return isAscending ? lhs.getFullName().compareTo(rhs.getFullName()) : rhs.getFullName().compareTo(lhs.getFullName());
                    }

                });
                affiliateMarketersRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.AM_byDecending:
                Collections.sort(affiliateMarketersData, new Comparator<AffiliateMarketersData>() {
                    @Override
                    public int compare(AffiliateMarketersData lhs, AffiliateMarketersData rhs) {
                        return isDescending ? lhs.getFullName().compareTo(rhs.getFullName()) : rhs.getFullName().compareTo(lhs.getFullName());
                    }

                });
                affiliateMarketersRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getAffiliateMarketersDetails().enqueue(new Callback<AffiliateMarketersResponse>() {
            public void onResponse(Call<AffiliateMarketersResponse> call, Response<AffiliateMarketersResponse> response) {
                affiliateMarketersData = response.body().getAffiliateMarketersData();
                AffiliateMarketersFragment.this.affiliateMarketersRecyclerViewAdapter = new AffiliateMarketersRecyclerViewAdapter(affiliateMarketersData, AffiliateMarketersFragment.this.getActivity());
                AffiliateMarketersFragment.this.AffiliateMarketersRecyclerView.setLayoutManager(new LinearLayoutManager(AffiliateMarketersFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(AffiliateMarketersFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(AffiliateMarketersFragment.this.getContext(), R.drawable.divider));
                AffiliateMarketersFragment.this.AffiliateMarketersRecyclerView.addItemDecoration(dividerItemDecoration);
                AffiliateMarketersFragment.this.AffiliateMarketersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                AffiliateMarketersFragment.this.AffiliateMarketersRecyclerView.setAdapter(AffiliateMarketersFragment.this.affiliateMarketersRecyclerViewAdapter);
                AffiliateMarketersFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<AffiliateMarketersResponse> call, Throwable th) {
                Toast.makeText(AffiliateMarketersFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                AffiliateMarketersFragment.this.progressDialog.dismiss();
            }
        });
    }
}
