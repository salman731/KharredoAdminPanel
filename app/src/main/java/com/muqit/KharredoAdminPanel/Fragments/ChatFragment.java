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

import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.ChatRecyclerViewAdpater;
import com.muqit.KharredoAdminPanel.Models.ChatHistoryData;
import com.muqit.KharredoAdminPanel.Models.ChatResponse;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    /* access modifiers changed from: private */
    public ChatRecyclerViewAdpater chatRecyclerViewAdpater;
    /* access modifiers changed from: private */
    public RecyclerView chatRecyclerview;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_chat, viewGroup, false);
        this.chatRecyclerview = (RecyclerView) inflate.findViewById(R.id.Chat_recyclerview);
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

        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getChatHistory().enqueue(new Callback<ArrayList<ChatHistoryData>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatHistoryData>> call, Response<ArrayList<ChatHistoryData>> response) {
                ChatFragment.this.chatRecyclerViewAdpater = new ChatRecyclerViewAdpater(response.body(), ChatFragment.this.getActivity());
                ChatFragment.this.chatRecyclerview.setLayoutManager(new LinearLayoutManager(ChatFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ChatFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ChatFragment.this.getContext(), R.drawable.divider));
                ChatFragment.this.chatRecyclerview.addItemDecoration(dividerItemDecoration);
                ChatFragment.this.chatRecyclerview.setItemAnimator(new DefaultItemAnimator());
                ChatFragment.this.chatRecyclerview.setAdapter(ChatFragment.this.chatRecyclerViewAdpater);
                ChatFragment.this.progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<ChatHistoryData>> call, Throwable t) {
                Toast.makeText(getActivity(),"Something went wrong.....",Toast.LENGTH_LONG).show();
                ChatFragment.this.progressDialog.dismiss();
            }
        });
    }
}
