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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.EmailFeedRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.EmailFeedData;
import com.muqit.KharredoAdminPanel.Models.EmailFeedResponse;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmailFeedFragment extends Fragment {
    /* access modifiers changed from: private */
    public RecyclerView EmailFeedRecyclerView;
    private ArrayList<EmailFeedData> emailFeedDataArrayList;
    /* access modifiers changed from: private */
    public EmailFeedRecyclerViewAdapter emailFeedRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_emailfeed, viewGroup, false);
        setHasOptionsMenu(true);
        this.EmailFeedRecyclerView = (RecyclerView) inflate.findViewById(R.id.EmailFeedRecyclerView);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PopulateData();
        return inflate;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_view_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                EmailFeedFragment.this.emailFeedRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    /* access modifiers changed from: 0000 */
    public void PopulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getEmailFeedData().enqueue(new Callback<EmailFeedResponse>() {
            public void onResponse(Call<EmailFeedResponse> call, Response<EmailFeedResponse> response) {
                EmailFeedFragment.this.emailFeedRecyclerViewAdapter = new EmailFeedRecyclerViewAdapter(((EmailFeedResponse) response.body()).getEmailFeedData(), EmailFeedFragment.this.getActivity());
                EmailFeedFragment.this.EmailFeedRecyclerView.setLayoutManager(new LinearLayoutManager(EmailFeedFragment.this.getActivity()));
                EmailFeedFragment.this.EmailFeedRecyclerView.addItemDecoration(new DividerItemDecoration(EmailFeedFragment.this.getContext(), 1));
                EmailFeedFragment.this.EmailFeedRecyclerView.setItemAnimator(new DefaultItemAnimator());
                EmailFeedFragment.this.EmailFeedRecyclerView.setAdapter(EmailFeedFragment.this.emailFeedRecyclerViewAdapter);
                EmailFeedFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<EmailFeedResponse> call, Throwable th) {
                Toast.makeText(EmailFeedFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", 1).show();
                EmailFeedFragment.this.progressDialog.dismiss();
            }
        });
    }
}
