package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.BlogRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.BlogResponse;
import com.muqit.KharredoAdminPanel.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayBlogsFragment extends Fragment {
    /* access modifiers changed from: private */
    public BlogRecyclerViewAdapter blogRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public RecyclerView blogRecyclerview;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    public MaterialButton Edit_BTN,Delete_BTN;
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_display_blogs, viewGroup, false);
        this.blogRecyclerview = (RecyclerView) inflate.findViewById(R.id.Blog_recyclerview);
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        setHasOptionsMenu(true);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        PoulateData();
        return inflate;
    }

    private void PoulateData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).Blog().enqueue(new Callback<BlogResponse>() {
            public void onResponse(Call<BlogResponse> call, Response<BlogResponse> response) {
                DisplayBlogsFragment.this.blogRecyclerViewAdapter = new BlogRecyclerViewAdapter(((BlogResponse) response.body()).getBlogData(), DisplayBlogsFragment.this.getActivity());
                DisplayBlogsFragment.this.blogRecyclerview.setLayoutManager(new LinearLayoutManager(DisplayBlogsFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(DisplayBlogsFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(DisplayBlogsFragment.this.getContext(), R.drawable.divider));
                DisplayBlogsFragment.this.blogRecyclerview.addItemDecoration(dividerItemDecoration);
                DisplayBlogsFragment.this.blogRecyclerview.setItemAnimator(new DefaultItemAnimator());
                DisplayBlogsFragment.this.blogRecyclerview.setAdapter(DisplayBlogsFragment.this.blogRecyclerViewAdapter);
                DisplayBlogsFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<BlogResponse> call, Throwable th) {
                DisplayBlogsFragment.this.progressDialog.dismiss();
            }
        });
    }


}
