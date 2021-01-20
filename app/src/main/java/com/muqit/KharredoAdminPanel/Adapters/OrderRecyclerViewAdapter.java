package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.CustomerResposne;
import com.muqit.KharredoAdminPanel.Models.OrderItemResponse;
import com.muqit.KharredoAdminPanel.Models.OrdersData;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRecyclerViewAdapter extends Adapter<OrderRecyclerViewAdapter.OrderViewHolder> implements Filterable {
    public String IMAGE_BASE_URL = "https://kharredo.com/images/";
    int OrderStatusinInt = -1;
    /* access modifiers changed from: private */
    public Context contxt;
    private Filter exampleFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(OrderRecyclerViewAdapter.this.ordersDataFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                String str = "Order in Queue";
                String str2 = "Completed";
                String str3 = "Processing Order";
                String str4 = "Shipping Product";
                for (OrdersData ordersData : OrderRecyclerViewAdapter.this.ordersDataFullList) {
                    if (ordersData.getDate().toLowerCase().contains(trim) || String.valueOf(ordersData.getOrder_ID()).contains(trim) || String.valueOf(ordersData.getUser_ID()).contains(trim) || String.valueOf(ordersData.getAmount()).contains(trim) || String.valueOf(ordersData.getQuantity()).contains(trim)) {
                        arrayList.add(ordersData);
                    }
                    else if ((str.toLowerCase().contains(trim) && String.valueOf(ordersData.getOrder_Status()).contains("0")) )
                    {
                        arrayList.add(ordersData);

                    }
                    else if((str2.toLowerCase().contains(trim) && String.valueOf(ordersData.getOrder_Status()).contains("4"))) {
                        arrayList.add(ordersData);
                    }
                    else if((str4.toLowerCase().contains(trim) && String.valueOf(ordersData.getOrder_Status()).contains("3")))
                    {

                        arrayList.add(ordersData);

                    }
                    else if((str3.toLowerCase().contains(trim) && String.valueOf(ordersData.getOrder_Status()).contains("1")))
                    {
                        Toast.makeText(contxt,"Processing Data Found",Toast.LENGTH_SHORT).show();
                        arrayList.add(ordersData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }


        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            OrderRecyclerViewAdapter.this.ordersData.clear();
            OrderRecyclerViewAdapter.this.ordersData.addAll((List) filterResults.values);
            OrderRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    /* access modifiers changed from: private */
    public ArrayList<OrdersData> ordersData;
    /* access modifiers changed from: private */
    public List<OrdersData> ordersDataFullList;
    String orderstatus = "";
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    public class OrderViewHolder extends ViewHolder {
        public MaterialButton Action_BTN;
        public MaterialTextView Amount;
        public MaterialTextView Date;
        public MaterialTextView Order_ID;
        public MaterialTextView Order_Status;
        public MaterialTextView Quantity;
        public MaterialTextView User_ID;

        public OrderViewHolder(View view) {
            super(view);
            this.Order_ID = (MaterialTextView) view.findViewById(R.id.Orders_order_no);
            this.User_ID = (MaterialTextView) view.findViewById(R.id.Orders_user_id);
            this.Amount = (MaterialTextView) view.findViewById(R.id.Order_amount);
            this.Quantity = (MaterialTextView) view.findViewById(R.id.Orders_quantity);
            this.Date = (MaterialTextView) view.findViewById(R.id.Orders_date);
            this.Order_Status = (MaterialTextView) view.findViewById(R.id.Orders_status_order);
            this.Action_BTN = (MaterialButton) view.findViewById(R.id.Orders_action_btn);
        }
    }

    public OrderRecyclerViewAdapter(ArrayList<OrdersData> arrayList, Context context) {
        this.ordersData = arrayList;
        this.contxt = context;
        this.ordersDataFullList = new ArrayList(arrayList);
    }

    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OrderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_list_view_layout, viewGroup, false));
    }

    public void onBindViewHolder(final OrderViewHolder orderViewHolder, int i) {
        final OrdersData ordersData2 = (OrdersData) this.ordersData.get(i);
        orderViewHolder.Order_ID.setText(String.valueOf(ordersData2.getOrder_ID()));
        orderViewHolder.User_ID.setText(String.valueOf(ordersData2.getUser_ID()));
        orderViewHolder.Amount.setText(String.valueOf(ordersData2.getAmount()));
        orderViewHolder.Quantity.setText(String.valueOf(ordersData2.getQuantity()));
        if (ordersData2.getOrder_Status() == 0) {
            String str = "Order in Queue";
            orderViewHolder.Order_Status.setText(str);
            this.orderstatus = str;
        } else if (ordersData2.getOrder_Status() == 4) {
            String str2 = "Completed";
            orderViewHolder.Order_Status.setText(str2);
            this.orderstatus = str2;
        } else if (ordersData2.getOrder_Status() == 1) {
            String str3 = "Processing Order";
            orderViewHolder.Order_Status.setText(str3);
            this.orderstatus = str3;
        } else if (ordersData2.getOrder_Status() == 3) {
            String str4 = "Shipping Product";
            orderViewHolder.Order_Status.setText(str4);
            this.orderstatus = str4;
        }
        orderViewHolder.Date.setText(ordersData2.getDate());
        orderViewHolder.Action_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(OrderRecyclerViewAdapter.this.contxt, orderViewHolder.Action_BTN);
                popupMenu.getMenuInflater().inflate(R.menu.order_actions_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Change Order Status")) {
                            if (ordersData2.getOrder_Status() == 0) {
                                OrderRecyclerViewAdapter.this.orderstatus = "Order in Queue";
                            } else if (ordersData2.getOrder_Status() == 4) {
                                OrderRecyclerViewAdapter.this.orderstatus = "Completed";
                            } else if (ordersData2.getOrder_Status() == 3) {
                                OrderRecyclerViewAdapter.this.orderstatus = "Shipping Product";
                            } else if (ordersData2.getOrder_Status() == 1) {
                                OrderRecyclerViewAdapter.this.orderstatus = "Processing Order";
                            }
                            View inflate = LayoutInflater.from(OrderRecyclerViewAdapter.this.contxt).inflate(R.layout.order_status_change_layout, null);
                            final Spinner spinner = (Spinner) inflate.findViewById(R.id.Order_status_spinner);
                            ((MaterialTextView) inflate.findViewById(R.id.current_order)).setText(OrderRecyclerViewAdapter.this.orderstatus);
                            OrderRecyclerViewAdapter orderRecyclerViewAdapter = OrderRecyclerViewAdapter.this;
                            MaterialAlertDialogBuilder view = new MaterialAlertDialogBuilder(OrderRecyclerViewAdapter.this.contxt).setView(inflate);
                            StringBuilder sb = new StringBuilder();
                            sb.append("Order #: ");
                            sb.append(String.valueOf(ordersData2.getOrder_ID()));
                            orderRecyclerViewAdapter.materialAlertDialogBuilder = view.setTitle((CharSequence) sb.toString()).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setPositiveButton((CharSequence) "Save", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (spinner.getSelectedItem().toString().equals("Shipping Product")) {
                                        OrderRecyclerViewAdapter.this.OrderStatusinInt = 3;
                                    } else if (spinner.getSelectedItem().toString().equals("Processing Order")) {
                                        OrderRecyclerViewAdapter.this.OrderStatusinInt = 1;
                                    } else if (spinner.getSelectedItem().toString().equals("Completed")) {
                                        OrderRecyclerViewAdapter.this.OrderStatusinInt = 4;
                                    }
                                    Toast.makeText(OrderRecyclerViewAdapter.this.contxt, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                                    OrderRecyclerViewAdapter.this.progressDialog = new ProgressDialog(OrderRecyclerViewAdapter.this.contxt);
                                    OrderRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                                    OrderRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                                    OrderRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                                    OrderRecyclerViewAdapter.this.progressDialog.show();
                                    ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).ChangeOrderStatus(String.valueOf(ordersData2.getOrder_ID()), String.valueOf(OrderRecyclerViewAdapter.this.OrderStatusinInt)).enqueue(new Callback<CommonResponse>() {
                                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                            Toast.makeText(OrderRecyclerViewAdapter.this.contxt, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                                            OrderRecyclerViewAdapter.this.progressDialog.dismiss();
                                        }

                                        public void onFailure(Call<CommonResponse> call, Throwable th) {
                                            Toast.makeText(OrderRecyclerViewAdapter.this.contxt, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                            OrderRecyclerViewAdapter.this.progressDialog.dismiss();
                                        }
                                    });
                                }
                            });
                            OrderRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
                        }
                        String str = "Ok";
                        if (menuItem.getTitle().equals("Customer Details")) {
                            View inflate2 = LayoutInflater.from(OrderRecyclerViewAdapter.this.contxt).inflate(R.layout.customer_details_layout, null);
                            final MaterialTextView materialTextView = (MaterialTextView) inflate2.findViewById(R.id.Customer_user_id);
                            final MaterialTextView materialTextView2 = (MaterialTextView) inflate2.findViewById(R.id.Customer_name);
                            final MaterialTextView materialTextView3 = (MaterialTextView) inflate2.findViewById(R.id.Cusomer_address);
                            final MaterialTextView materialTextView4 = (MaterialTextView) inflate2.findViewById(R.id.Cusomer_email);
                            final MaterialTextView materialTextView5 = (MaterialTextView) inflate2.findViewById(R.id.Cusomer_phoneno);
                            try {
                                Call customerInfo = ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getCustomerInfo(String.valueOf(ordersData2.getUser_ID()));
                                Callback<CustomerResposne> r5 = new Callback<CustomerResposne>() {
                                    public void onResponse(Call<CustomerResposne> call, Response<CustomerResposne> response) {
                                        materialTextView.setText(((CustomerResposne) response.body()).getCustomerData().getID());
                                        MaterialTextView materialTextView = materialTextView2;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(((CustomerResposne) response.body()).getCustomerData().getFirstName());
                                        sb.append(" ");
                                        sb.append(((CustomerResposne) response.body()).getCustomerData().getLastName());
                                        materialTextView.setText(sb.toString());
                                        materialTextView3.setText(((CustomerResposne) response.body()).getCustomerData().getAddress());
                                        materialTextView4.setText(((CustomerResposne) response.body()).getCustomerData().getEmail());
                                        materialTextView5.setText(((CustomerResposne) response.body()).getCustomerData().getPhone());
                                    }

                                    public void onFailure(Call<CustomerResposne> call, Throwable th) {
                                        Toast.makeText(OrderRecyclerViewAdapter.this.contxt, th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                };
                                customerInfo.enqueue(r5);
                            } catch (Exception unused) {
                            }
                            OrderRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(OrderRecyclerViewAdapter.this.contxt).setView(inflate2).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            OrderRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
                        }
                        if (menuItem.getTitle().equals("View Ordered Items")) {
                            View inflate3 = LayoutInflater.from(OrderRecyclerViewAdapter.this.contxt).inflate(R.layout.ordered_items_item_layout, null);
                            final MaterialTextView materialTextView6 = (MaterialTextView) inflate3.findViewById(R.id.order_item_ID);
                            final MaterialTextView materialTextView7 = (MaterialTextView) inflate3.findViewById(R.id.order_item_Name);
                            final MaterialTextView materialTextView8 = (MaterialTextView) inflate3.findViewById(R.id.order_item_Price);
                            final MaterialTextView materialTextView9 = (MaterialTextView) inflate3.findViewById(R.id.order_item_Price);
                            final ImageView imageView = (ImageView) inflate3.findViewById(R.id.Product_image);
                            Call OrderItemDetails = ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).OrderItemDetails(String.valueOf(ordersData2.getOrder_ID()));
                            Callback<OrderItemResponse> r4 = new Callback<OrderItemResponse>() {
                                public void onResponse(Call<OrderItemResponse> call, Response<OrderItemResponse> response) {
                                    materialTextView6.setText(((OrderItemResponse) response.body()).getOrderItemData().getID());
                                    materialTextView7.setText(((OrderItemResponse) response.body()).getOrderItemData().getName());
                                    materialTextView8.setText(((OrderItemResponse) response.body()).getOrderItemData().getPrice());
                                    materialTextView9.setText(((OrderItemResponse) response.body()).getOrderItemData().getPrice());
                                    Picasso picasso = Picasso.get();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(OrderRecyclerViewAdapter.this.IMAGE_BASE_URL);
                                    sb.append(((OrderItemResponse) response.body()).getOrderItemData().getPhoto());
                                    picasso.load(sb.toString()).into(imageView);
                                }

                                public void onFailure(Call<OrderItemResponse> call, Throwable th) {
                                    Toast.makeText(OrderRecyclerViewAdapter.this.contxt, th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            };
                            OrderItemDetails.enqueue(r4);
                            OrderRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(OrderRecyclerViewAdapter.this.contxt).setView(inflate3).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            OrderRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public int getItemCount() {
        return this.ordersData.size();
    }

    public Filter getFilter() {
        return this.exampleFilter;
    }
}
