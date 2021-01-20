package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.EmailFeedData;
import com.muqit.KharredoAdminPanel.Models.EmailFeedResponse;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailFeedRecyclerViewAdapter extends Adapter<EmailFeedRecyclerViewAdapter.EmailFeedViewHolder> implements Filterable {
    Context context;
    ArrayList<EmailFeedData> emailFeedDataList;
    List<EmailFeedData> emailFeedDataListFull;
    private Filter exampleFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(EmailFeedRecyclerViewAdapter.this.emailFeedDataListFull);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (EmailFeedData emailFeedData : EmailFeedRecyclerViewAdapter.this.emailFeedDataListFull) {
                    if (emailFeedData.getEmail().toLowerCase().contains(trim)) {
                        arrayList.add(emailFeedData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            EmailFeedRecyclerViewAdapter.this.emailFeedDataList.clear();
            EmailFeedRecyclerViewAdapter.this.emailFeedDataList.addAll((List) filterResults.values);
            EmailFeedRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    MaterialAlertDialogBuilder materialAlertDialogBuilder;

    public class EmailFeedViewHolder extends ViewHolder {
        public MaterialButton DeleteBTN;
        public MaterialTextView EmailTV;

        public EmailFeedViewHolder(View view) {
            super(view);
            this.DeleteBTN = (MaterialButton) view.findViewById(R.id.EmailFeed_Delete_BTN);
            this.EmailTV = (MaterialTextView) view.findViewById(R.id.EmailFeed_Email_TV);
        }
    }

    public EmailFeedRecyclerViewAdapter(ArrayList<EmailFeedData> arrayList, Context context2) {
        this.emailFeedDataList = arrayList;
        this.context = context2;
        this.emailFeedDataListFull = new ArrayList(this.emailFeedDataList);
    }

    public EmailFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new EmailFeedViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.email_feed_list_view, viewGroup, false));
    }

    public void onBindViewHolder(EmailFeedViewHolder emailFeedViewHolder, final int i) {
        final EmailFeedData emailFeedData = (EmailFeedData) this.emailFeedDataList.get(i);
        emailFeedViewHolder.EmailTV.setText(emailFeedData.getEmail());
        emailFeedViewHolder.DeleteBTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "No";
                String str2 = "Yes";
                EmailFeedRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(EmailFeedRecyclerViewAdapter.this.context).setTitle((CharSequence) "Delete?").setMessage((CharSequence) "Are you sure you want to delete this?").setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str2, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(EmailFeedRecyclerViewAdapter.this.context);
                        progressDialog.setMessage("Deleting....");
                        progressDialog.setProgressStyle(0);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).DeleteEmailFeed(emailFeedData.getID()).enqueue(new Callback<EmailFeedResponse>() {
                            public void onResponse(Call<EmailFeedResponse> call, Response<EmailFeedResponse> response) {
                                if (((EmailFeedResponse) response.body()).getMessage().equals("Email deleted successfully.")) {
                                    EmailFeedRecyclerViewAdapter.this.emailFeedDataList.remove(i);
                                    EmailFeedRecyclerViewAdapter.this.notifyItemRemoved(i);
                                    EmailFeedRecyclerViewAdapter.this.notifyItemRangeChanged(i, EmailFeedRecyclerViewAdapter.this.emailFeedDataList.size());
                                    progressDialog.dismiss();
                                }
                            }

                            public void onFailure(Call<EmailFeedResponse> call, Throwable th) {
                                Toast.makeText(EmailFeedRecyclerViewAdapter.this.context, "Check Your Internet Connection and Try Again......", 1).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
                EmailFeedRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
            }
        });
    }

    public int getItemCount() {
        return this.emailFeedDataList.size();
    }

    public Filter getFilter() {
        return this.exampleFilter;
    }
}
