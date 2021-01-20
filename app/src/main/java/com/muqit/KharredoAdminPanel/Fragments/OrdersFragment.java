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
import com.muqit.KharredoAdminPanel.Adapters.OrderRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.OrderResponse;
import com.muqit.KharredoAdminPanel.Models.OrdersData;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.util.Comparator;

public class OrdersFragment extends Fragment {
    /* access modifiers changed from: private */
    public RecyclerView OrdersRecyclerView;
    /* access modifiers changed from: private */
    public OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private ArrayList<OrdersData> ordersData;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_orders, viewGroup, false);
        setHasOptionsMenu(true);
        this.OrdersRecyclerView = (RecyclerView) inflate.findViewById(R.id.Order_recyclerview);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PoulateData();
        return inflate;
    }
    boolean sortOrderNoBy = true;
    boolean sortUserID = true;
    boolean sortDate = true;
    boolean sortAmount = true;
    boolean sortQuantity = true;
    boolean sortOrderStatus = true;



    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Orders_byOrderNo:
                if(sortOrderNoBy)
                {
                    sortOrderNoBy = false;
                }
                else
                {
                    sortOrderNoBy = true;
                }
                Collections.sort(ordersData, new Comparator<OrdersData>() {
                    @Override
                    public int compare(OrdersData lhs, OrdersData rhs) {
                           return sortOrderNoBy ? (lhs.getOrder_ID() < rhs.getOrder_ID())? -1 : (lhs.getOrder_ID() > rhs.getOrder_ID()) ? 1 : 0 : (lhs.getOrder_ID() > rhs.getOrder_ID())? -1 : (lhs.getOrder_ID() < rhs.getOrder_ID()) ? 1 : 0 ;
                    }
                });
                orderRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Orders_byOrderStatus:
                if(sortOrderStatus)
                {
                    sortOrderStatus = false;
                }
                else
                {
                    sortOrderStatus = true;
                }
                Collections.sort(ordersData, new Comparator<OrdersData>() {
                    @Override
                    public int compare(OrdersData lhs, OrdersData rhs) {
                        return sortOrderStatus ? (lhs.getOrder_Status() < rhs.getOrder_Status())? -1 : (lhs.getOrder_Status() > rhs.getOrder_Status()) ? 1 : 0 : (lhs.getOrder_Status() > rhs.getOrder_Status())? -1 : (lhs.getOrder_Status() < rhs.getOrder_Status()) ? 1 : 0 ;
                    }
                });
                orderRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Orders_byAmount:
                if(sortAmount)
                {
                    sortAmount = false;
                }
                else
                {
                    sortAmount = true;
                }
                Collections.sort(ordersData, new Comparator<OrdersData>() {
                    @Override
                    public int compare(OrdersData lhs, OrdersData rhs) {
                        return sortAmount ? (lhs.getAmount() < rhs.getAmount())? -1 : (lhs.getAmount() > rhs.getAmount()) ? 1 : 0 : (lhs.getAmount() > rhs.getAmount())? -1 : (lhs.getAmount() < rhs.getAmount()) ? 1 : 0 ;
                    }
                });
                orderRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Orders_byUserId:
                if(sortUserID)
                {
                    sortUserID = false;
                }
                else
                {
                    sortUserID = true;
                }
                Collections.sort(ordersData, new Comparator<OrdersData>() {
                    @Override
                    public int compare(OrdersData lhs, OrdersData rhs) {
                        return sortUserID ? (lhs.getUser_ID() < rhs.getUser_ID())? -1 : (lhs.getUser_ID() > rhs.getUser_ID()) ? 1 : 0 : (lhs.getUser_ID() > rhs.getUser_ID())? -1 : (lhs.getUser_ID() < rhs.getUser_ID()) ? 1 : 0 ;
                    }
                });
                orderRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Orders_byQuantity:
                if(sortQuantity)
                {
                    sortQuantity = false;
                }
                else
                {
                    sortQuantity = true;
                }
                Collections.sort(ordersData, new Comparator<OrdersData>() {
                    @Override
                    public int compare(OrdersData lhs, OrdersData rhs) {
                        return sortQuantity ? (lhs.getQuantity() < rhs.getQuantity())? -1 : (lhs.getQuantity() > rhs.getQuantity()) ? 1 : 0 : (lhs.getQuantity() > rhs.getQuantity())? -1 : (lhs.getQuantity() < rhs.getQuantity()) ? 1 : 0 ;
                    }
                });
                orderRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Orders_byDate:
                if(sortDate)
                {
                    sortDate = false;
                }
                else
                {
                    sortDate = true;
                }
                Collections.sort(ordersData, new Comparator<OrdersData>() {
                    @Override
                    public int compare(OrdersData lhs, OrdersData rhs) {
                        return sortDate ? lhs.getDate().compareTo(rhs.getDate()) : rhs.getDate().compareTo(lhs.getDate());}
                });
                orderRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.order_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                OrdersFragment.this.orderRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    /* access modifiers changed from: 0000 */
    public void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getOrdersDetails().enqueue(new Callback<OrderResponse>() {
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                ordersData = response.body().getOrdersData();
                OrdersFragment.this.orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(ordersData, OrdersFragment.this.getActivity());
                OrdersFragment.this.OrdersRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(OrdersFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(OrdersFragment.this.getContext(), R.drawable.divider));
                OrdersFragment.this.OrdersRecyclerView.addItemDecoration(dividerItemDecoration);
                OrdersFragment.this.OrdersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                OrdersFragment.this.OrdersRecyclerView.setAdapter(OrdersFragment.this.orderRecyclerViewAdapter);
                OrdersFragment.this.progressDialog.dismiss();


            }

            public void onFailure(Call<OrderResponse> call, Throwable th) {
                Toast.makeText(OrdersFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                OrdersFragment.this.progressDialog.dismiss();
            }
        });
    }
}
