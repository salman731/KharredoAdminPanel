package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Activity.CartActivity;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.EmployeeDetailData;
import com.muqit.KharredoAdminPanel.Models.EmployeesData;
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.Models.UsersResponse;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRecyclerViewAdapter extends Adapter<UserRecyclerViewAdapter.UserItemViewHolder> implements Filterable {
    private Filter UsersFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(UserRecyclerViewAdapter.this.usersDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                String str = "Active";
                String str1 = "Inactive";
                for (UsersData usersData : UserRecyclerViewAdapter.this.usersDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(usersData.getEmail()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(usersData.getFirstName().toLowerCase() + " " + usersData.getLastName().toLowerCase()).contains(trim) || SalesRecyclerViewAdapter.blankIfNull(usersData.getCreated_Date()).toLowerCase().contains(trim)) {
                        arrayList.add(usersData);
                    }
                    else if(str.toLowerCase().contains(trim) && String.valueOf(usersData.getStatus()).contains("1"))
                    {
                        arrayList.add(usersData);
                    }
                    else if (str1.toLowerCase().contains(trim) && String.valueOf(usersData.getStatus()).contains("0"))
                    {
                        arrayList.add(usersData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            UserRecyclerViewAdapter.this.usersDataArrayList.clear();
            UserRecyclerViewAdapter.this.usersDataArrayList.addAll((List) filterResults.values);
            UserRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    /* access modifiers changed from: private */
    public TextInputEditText Address;
    /* access modifiers changed from: private */
    public TextInputEditText ContactInfo;
    private MaterialTextView DialogTitle;
    /* access modifiers changed from: private */
    public TextInputEditText Email;
    /* access modifiers changed from: private */
    public TextInputEditText FirstName;
    public String IMAGE_BASE_URL = "https://kharredo.com";
    /* access modifiers changed from: private */
    public TextInputEditText LastName;
    /* access modifiers changed from: private */
    public TextInputEditText Password;
    /* access modifiers changed from: private */
    public ImageView Photo_IV;
    /* access modifiers changed from: private */
    public MaterialButton UploadImageBTN;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public MaterialAlertDialogBuilder materialAlertDialogBuilder;
    OnItemClick onItemClick;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public ArrayList<UsersData> usersDataArrayList;
    public List<UsersData> usersDataArrayListFullList;

    @Override
    public Filter getFilter() {
        return UsersFilter;
    }

    public interface OnItemClick {
        void getPosition(String str);
    }

    public class UserItemViewHolder extends ViewHolder {
        public MaterialButton UCart_btn;
        public MaterialTextView UDate_Added;
        public MaterialButton UDelete_btn;
        public MaterialButton UEdit_btn;
        public MaterialTextView UEmail;
        public ImageView UImage;
        public MaterialTextView UName;
        public MaterialTextView UPhoto_Update;
        public MaterialTextView UStatus;
        public MaterialTextView User_ActivateBTN;

        public UserItemViewHolder(View view) {
            super(view);
            this.UEmail = (MaterialTextView) view.findViewById(R.id.Users_email);
            this.UName = (MaterialTextView) view.findViewById(R.id.Users_name);
            this.UStatus = (MaterialTextView) view.findViewById(R.id.Users_status);
            this.UDate_Added = (MaterialTextView) view.findViewById(R.id.Users_date_added);
            this.UImage = (ImageView) view.findViewById(R.id.Users_image);
            this.UCart_btn = (MaterialButton) view.findViewById(R.id.Users_cart_btn);
            this.UPhoto_Update = (MaterialTextView) view.findViewById(R.id.users_update_image_tv);
            this.User_ActivateBTN = (MaterialTextView) view.findViewById(R.id.Users_activate_btn);
            this.UEdit_btn = (MaterialButton) view.findViewById(R.id.Users_edit_btn);
            this.UDelete_btn = (MaterialButton) view.findViewById(R.id.Users_delete_btn);
        }
    }

    public UserRecyclerViewAdapter(ArrayList<UsersData> arrayList, Context context2) {
        this.usersDataArrayList = arrayList;
        this.context = context2;
        this.usersDataArrayListFullList = new ArrayList(arrayList);
    }

    public UserItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new UserItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(UserItemViewHolder userItemViewHolder, int i) {
        final UsersData usersData = (UsersData) this.usersDataArrayList.get(i);
        MaterialTextView materialTextView = userItemViewHolder.UName;
        StringBuilder sb = new StringBuilder();
        sb.append(usersData.getFirstName());
        sb.append(" ");
        sb.append(usersData.getLastName());
        materialTextView.setText(sb.toString());
        userItemViewHolder.UEmail.setText(usersData.getEmail());
        if (usersData.getStatus() == 0) {
            userItemViewHolder.UStatus.setBackground(ContextCompat.getDrawable(this.context, R.drawable.inactive_status));
            userItemViewHolder.UStatus.setText("Inactive");
            userItemViewHolder.User_ActivateBTN.setVisibility(View.VISIBLE);
            userItemViewHolder.UDelete_btn.setEnabled(false);
        } else if (usersData.getStatus() == 1) {
            userItemViewHolder.UStatus.setBackground(ContextCompat.getDrawable(this.context, R.drawable.active_status));
            userItemViewHolder.UStatus.setText("Active");
            userItemViewHolder.User_ActivateBTN.setVisibility(View.INVISIBLE);
            userItemViewHolder.UDelete_btn.setEnabled(true);
        }
        userItemViewHolder.UDate_Added.setText(usersData.getCreated_Date());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.IMAGE_BASE_URL);
        sb2.append(usersData.getPhotoURL());
        Picasso.get().load(sb2.toString()).into(userItemViewHolder.UImage);
        if (userItemViewHolder.UImage.getDrawable() == null) {
            userItemViewHolder.UImage.setImageResource(R.drawable.noimage);
        }
        final String id = ((UsersData) this.usersDataArrayList.get(i)).getID();
        userItemViewHolder.UPhoto_Update.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UserRecyclerViewAdapter.this.onItemClick.getPosition(id);
            }
        });
        userItemViewHolder.User_ActivateBTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UserRecyclerViewAdapter.this.progressDialog = new ProgressDialog(UserRecyclerViewAdapter.this.context);
                UserRecyclerViewAdapter.this.progressDialog.setMessage("Activating....");
                UserRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                UserRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                UserRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockUser(usersData.getID(), 1).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(UserRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        UserRecyclerViewAdapter.this.progressDialog.dismiss();
                        UserRecyclerViewAdapter.this.ReloadRecyclerViewData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(UserRecyclerViewAdapter.this.context, "Something Went Wrong....", Toast.LENGTH_LONG).show();
                        UserRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        userItemViewHolder.UDelete_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UserRecyclerViewAdapter.this.progressDialog = new ProgressDialog(UserRecyclerViewAdapter.this.context);
                UserRecyclerViewAdapter.this.progressDialog.setMessage("Blocking....");
                UserRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                UserRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                UserRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockUser(usersData.getID(), 0).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(UserRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        UserRecyclerViewAdapter.this.progressDialog.dismiss();
                        UserRecyclerViewAdapter.this.ReloadRecyclerViewData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(UserRecyclerViewAdapter.this.context, "Something Went Wrong....", Toast.LENGTH_LONG).show();
                        UserRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        userItemViewHolder.UEdit_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = LayoutInflater.from(UserRecyclerViewAdapter.this.context).inflate(R.layout.edit_details_layout, null);
                UserRecyclerViewAdapter.this.Email = (TextInputEditText) inflate.findViewById(R.id.edit_Email);
                UserRecyclerViewAdapter.this.FirstName = (TextInputEditText) inflate.findViewById(R.id.edit_firstname);
                UserRecyclerViewAdapter.this.LastName = (TextInputEditText) inflate.findViewById(R.id.edit_lastname);
                UserRecyclerViewAdapter.this.Password = (TextInputEditText) inflate.findViewById(R.id.edit_password);
                UserRecyclerViewAdapter.this.Address = (TextInputEditText) inflate.findViewById(R.id.edit_address);
                UserRecyclerViewAdapter.this.ContactInfo = (TextInputEditText) inflate.findViewById(R.id.edit_contactinfo);
                MaterialTextView materialTextView = (MaterialTextView) inflate.findViewById(R.id.Edit_Title_tv);
                UserRecyclerViewAdapter.this.Photo_IV = (ImageView) inflate.findViewById(R.id.edit_photo);
                UserRecyclerViewAdapter.this.UploadImageBTN = (MaterialButton) inflate.findViewById(R.id.edit_upload_btn);
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getUserInfo(usersData.getID()).enqueue(new Callback<EmployeeDetailData>() {
                    public void onResponse(Call<EmployeeDetailData> call, Response<EmployeeDetailData> response) {
                        UserRecyclerViewAdapter.this.Email.setText(((EmployeeDetailData) response.body()).getEmployeedata().getEmail());
                        UserRecyclerViewAdapter.this.FirstName.setText(((EmployeeDetailData) response.body()).getEmployeedata().getFirstName());
                        UserRecyclerViewAdapter.this.LastName.setText(((EmployeeDetailData) response.body()).getEmployeedata().getLastName());
                        UserRecyclerViewAdapter.this.Password.setText(((EmployeeDetailData) response.body()).getEmployeedata().getPassword());
                        UserRecyclerViewAdapter.this.Address.setText(((EmployeeDetailData) response.body()).getEmployeedata().getAddress());
                        UserRecyclerViewAdapter.this.ContactInfo.setText(((EmployeeDetailData) response.body()).getEmployeedata().getPhone());
                    }

                    public void onFailure(Call<EmployeeDetailData> call, Throwable th) {
                        Toast.makeText(UserRecyclerViewAdapter.this.context, th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                materialTextView.setText("Update User");
                UserRecyclerViewAdapter.this.Photo_IV.setVisibility(View.INVISIBLE);
                UserRecyclerViewAdapter.this.UploadImageBTN.setVisibility(View.INVISIBLE);
                String str = "Save";
                UserRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(UserRecyclerViewAdapter.this.context).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(UserRecyclerViewAdapter.this.Email.getText().toString()) || TextUtils.isEmpty(UserRecyclerViewAdapter.this.FirstName.getText().toString()) || TextUtils.isEmpty(UserRecyclerViewAdapter.this.LastName.getText().toString()) || TextUtils.isEmpty(UserRecyclerViewAdapter.this.Password.getText().toString()) || (TextUtils.isEmpty(UserRecyclerViewAdapter.this.Address.getText().toString()) && TextUtils.isEmpty(UserRecyclerViewAdapter.this.ContactInfo.getText().toString()))) {
                            Toast.makeText(UserRecyclerViewAdapter.this.context, "Enter Complete Data.....", Toast.LENGTH_LONG).show();
                        } else if (UserRecyclerViewAdapter.this.validEmail(UserRecyclerViewAdapter.this.Email.getText().toString())) {
                            UserRecyclerViewAdapter.this.progressDialog = new ProgressDialog(UserRecyclerViewAdapter.this.context);
                            UserRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                            UserRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                            UserRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                            UserRecyclerViewAdapter.this.progressDialog.show();
                            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateEmployee(usersData.getID(), UserRecyclerViewAdapter.this.FirstName.getText().toString().trim(), UserRecyclerViewAdapter.this.LastName.getText().toString().trim(), UserRecyclerViewAdapter.this.Password.getText().toString().trim(), UserRecyclerViewAdapter.this.Address.getText().toString().trim(), UserRecyclerViewAdapter.this.ContactInfo.getText().toString().trim(), UserRecyclerViewAdapter.this.Email.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Toast.makeText(UserRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                                    UserRecyclerViewAdapter.this.ReloadRecyclerViewData();
                                    UserRecyclerViewAdapter.this.progressDialog.dismiss();
                                }

                                public void onFailure(Call<CommonResponse> call, Throwable th) {
                                    Toast.makeText(UserRecyclerViewAdapter.this.context, "Something Went Wrong......", Toast.LENGTH_LONG).show();
                                    UserRecyclerViewAdapter.this.progressDialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(UserRecyclerViewAdapter.this.context, "Please Enter Valid Email Address......", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                UserRecyclerViewAdapter.this.materialAlertDialogBuilder.setCancelable(false).show();
            }
        });
        userItemViewHolder.UCart_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CartActivity.class).putExtra("UID",usersData.getID()));
            }
        });


    }

    public int getItemCount() {
        return this.usersDataArrayList.size();
    }

    public void setOnItemClick(OnItemClick onItemClick2) {
        this.onItemClick = onItemClick2;
    }

    /* access modifiers changed from: private */
    public boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    /* access modifiers changed from: private */
    public void ReloadRecyclerViewData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getUsersDetails().enqueue(new Callback<UsersResponse>() {
            public void onFailure(Call<UsersResponse> call, Throwable th) {
            }

            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                UserRecyclerViewAdapter.this.usersDataArrayList = ((UsersResponse) response.body()).getUsersData();
                UserRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
