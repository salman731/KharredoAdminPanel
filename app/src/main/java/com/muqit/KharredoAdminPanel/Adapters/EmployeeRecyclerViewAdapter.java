package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.EmployeeDetailData;
import com.muqit.KharredoAdminPanel.Models.EmployeesData;
import com.muqit.KharredoAdminPanel.Models.EmployeesResponse;
import com.muqit.KharredoAdminPanel.Models.SalesData;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRecyclerViewAdapter extends Adapter<EmployeeRecyclerViewAdapter.EmployeeItemViewHolder> implements Filterable {

    private Filter EmployeesFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(EmployeeRecyclerViewAdapter.this.employeesDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                String str = "Active";
                String str1 = "Inactive";
                for (EmployeesData employeesDataData : EmployeeRecyclerViewAdapter.this.employeesDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(employeesDataData.getEmail()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(employeesDataData.getFirstName().toLowerCase() + " " + employeesDataData.getLastName().toLowerCase()).contains(trim) || SalesRecyclerViewAdapter.blankIfNull(employeesDataData.getCreated_On()).toLowerCase().contains(trim)) {
                        arrayList.add(employeesDataData);
                    }
                    else if(str.toLowerCase().contains(trim) && String.valueOf(employeesDataData.getStatus()).contains("1"))
                    {
                        arrayList.add(employeesDataData);
                    }
                    else if (str1.toLowerCase().contains(trim) && String.valueOf(employeesDataData.getStatus()).contains("0"))
                    {
                        arrayList.add(employeesDataData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            EmployeeRecyclerViewAdapter.this.employeesDataArrayList.clear();
            EmployeeRecyclerViewAdapter.this.employeesDataArrayList.addAll((List) filterResults.values);
            EmployeeRecyclerViewAdapter.this.notifyDataSetChanged();
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
    public ArrayList<EmployeesData> employeesDataArrayList;
    public List<EmployeesData> employeesDataArrayListFullList;
    public MaterialAlertDialogBuilder materialAlertDialogBuilder;
    OnItemClick onItemClick;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    @Override
    public Filter getFilter() {
        return EmployeesFilter;
    }

    public class EmployeeItemViewHolder extends ViewHolder {
        public MaterialTextView ActivateBTN;
        public MaterialButton Delete_BTN;
        public MaterialTextView EDate_Added;
        public MaterialTextView EEmail;
        public MaterialTextView EName;
        public MaterialTextView EStatus;
        public MaterialButton Edit_BTN;
        public ImageView EmployeeImage;
        public MaterialTextView UpdateImageTV;

        public EmployeeItemViewHolder(View view) {
            super(view);
            this.EmployeeImage = (ImageView) view.findViewById(R.id.Employees_image);
            this.EName = (MaterialTextView) view.findViewById(R.id.Employees_name);
            this.EEmail = (MaterialTextView) view.findViewById(R.id.Employees_email);
            this.EDate_Added = (MaterialTextView) view.findViewById(R.id.Employees_date_added);
            this.EStatus = (MaterialTextView) view.findViewById(R.id.Employees_status);
            this.ActivateBTN = (MaterialTextView) view.findViewById(R.id.Employees_activate_btn);
            this.UpdateImageTV = (MaterialTextView) view.findViewById(R.id.update_image_tv);
            this.Edit_BTN = (MaterialButton) view.findViewById(R.id.Employees_edit_btn);
            this.Delete_BTN = (MaterialButton) view.findViewById(R.id.Employees_delete_btn);
        }
    }

    public interface OnItemClick {
        void getPosition(String str);
    }

    public EmployeeRecyclerViewAdapter(ArrayList<EmployeesData> arrayList, Context context2) {
        this.employeesDataArrayList = arrayList;
        this.context = context2;
        this.employeesDataArrayListFullList = new ArrayList<>(arrayList);
    }

    public EmployeeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new EmployeeItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_list_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(EmployeeItemViewHolder employeeItemViewHolder, int i) {
        final EmployeesData employeesData = (EmployeesData) this.employeesDataArrayList.get(i);
        employeeItemViewHolder.EEmail.setText(employeesData.getEmail());
        MaterialTextView materialTextView = employeeItemViewHolder.EName;
        StringBuilder sb = new StringBuilder();
        sb.append(employeesData.getFirstName());
        sb.append(" ");
        sb.append(employeesData.getLastName());
        materialTextView.setText(sb.toString());
        if (employeesData.getStatus() == 0) {
            employeeItemViewHolder.EStatus.setBackground(ContextCompat.getDrawable(this.context, R.drawable.inactive_status));
            employeeItemViewHolder.EStatus.setText("Inactive");
            employeeItemViewHolder.Delete_BTN.setEnabled(false);
            employeeItemViewHolder.ActivateBTN.setVisibility(View.VISIBLE);
        } else if (employeesData.getStatus() == 1) {
            employeeItemViewHolder.EStatus.setBackground(ContextCompat.getDrawable(this.context, R.drawable.active_status));
            employeeItemViewHolder.EStatus.setText("Active");
            employeeItemViewHolder.Delete_BTN.setEnabled(true);
            employeeItemViewHolder.ActivateBTN.setVisibility(View.INVISIBLE);
        }
        final String id = ((EmployeesData) this.employeesDataArrayList.get(i)).getID();
        employeeItemViewHolder.UpdateImageTV.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EmployeeRecyclerViewAdapter.this.onItemClick.getPosition(id);
            }
        });
        employeeItemViewHolder.EDate_Added.setText(employeesData.getCreated_On());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.IMAGE_BASE_URL);
        sb2.append(employeesData.getPhotoURL());
        Picasso.get().load(sb2.toString()).into(employeeItemViewHolder.EmployeeImage);
        if (employeeItemViewHolder.EmployeeImage.getDrawable() == null) {
            employeeItemViewHolder.EmployeeImage.setImageResource(R.drawable.noimage);
        }
        employeeItemViewHolder.Delete_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EmployeeRecyclerViewAdapter.this.progressDialog = new ProgressDialog(EmployeeRecyclerViewAdapter.this.context);
                EmployeeRecyclerViewAdapter.this.progressDialog.setMessage("Blocking....");
                EmployeeRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                EmployeeRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                EmployeeRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockUser(employeesData.getID(), 0).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(EmployeeRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        EmployeeRecyclerViewAdapter.this.progressDialog.dismiss();
                        EmployeeRecyclerViewAdapter.this.ReloadRecyclerViewData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(EmployeeRecyclerViewAdapter.this.context, "Something Went Wrong....", Toast.LENGTH_LONG).show();
                        EmployeeRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        employeeItemViewHolder.ActivateBTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EmployeeRecyclerViewAdapter.this.progressDialog = new ProgressDialog(EmployeeRecyclerViewAdapter.this.context);
                EmployeeRecyclerViewAdapter.this.progressDialog.setMessage("Activating....");
                EmployeeRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                EmployeeRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                EmployeeRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockUser(employeesData.getID(), 1).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(EmployeeRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        EmployeeRecyclerViewAdapter.this.progressDialog.dismiss();
                        EmployeeRecyclerViewAdapter.this.ReloadRecyclerViewData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(EmployeeRecyclerViewAdapter.this.context, "Something Went Wrong....", Toast.LENGTH_LONG).show();
                        EmployeeRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        employeeItemViewHolder.Edit_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = LayoutInflater.from(EmployeeRecyclerViewAdapter.this.context).inflate(R.layout.edit_details_layout, null);
                EmployeeRecyclerViewAdapter.this.Email = (TextInputEditText) inflate.findViewById(R.id.edit_Email);
                EmployeeRecyclerViewAdapter.this.FirstName = (TextInputEditText) inflate.findViewById(R.id.edit_firstname);
                EmployeeRecyclerViewAdapter.this.LastName = (TextInputEditText) inflate.findViewById(R.id.edit_lastname);
                EmployeeRecyclerViewAdapter.this.Password = (TextInputEditText) inflate.findViewById(R.id.edit_password);
                EmployeeRecyclerViewAdapter.this.Address = (TextInputEditText) inflate.findViewById(R.id.edit_address);
                EmployeeRecyclerViewAdapter.this.ContactInfo = (TextInputEditText) inflate.findViewById(R.id.edit_contactinfo);
                MaterialTextView materialTextView = (MaterialTextView) inflate.findViewById(R.id.Edit_Title_tv);
                EmployeeRecyclerViewAdapter.this.Photo_IV = (ImageView) inflate.findViewById(R.id.edit_photo);
                EmployeeRecyclerViewAdapter.this.UploadImageBTN = (MaterialButton) inflate.findViewById(R.id.edit_upload_btn);
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getEmployeeInfo(employeesData.getID()).enqueue(new Callback<EmployeeDetailData>() {
                    public void onResponse(Call<EmployeeDetailData> call, Response<EmployeeDetailData> response) {
                        EmployeeRecyclerViewAdapter.this.Email.setText(((EmployeeDetailData) response.body()).getEmployeedata().getEmail());
                        EmployeeRecyclerViewAdapter.this.FirstName.setText(((EmployeeDetailData) response.body()).getEmployeedata().getFirstName());
                        EmployeeRecyclerViewAdapter.this.LastName.setText(((EmployeeDetailData) response.body()).getEmployeedata().getLastName());
                        EmployeeRecyclerViewAdapter.this.Password.setText(((EmployeeDetailData) response.body()).getEmployeedata().getPassword());
                        EmployeeRecyclerViewAdapter.this.Address.setText(((EmployeeDetailData) response.body()).getEmployeedata().getAddress());
                        EmployeeRecyclerViewAdapter.this.ContactInfo.setText(((EmployeeDetailData) response.body()).getEmployeedata().getPhone());
                    }

                    public void onFailure(Call<EmployeeDetailData> call, Throwable th) {
                        Toast.makeText(EmployeeRecyclerViewAdapter.this.context, th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                materialTextView.setText("Update Employee");
                EmployeeRecyclerViewAdapter.this.Photo_IV.setVisibility(View.INVISIBLE);
                EmployeeRecyclerViewAdapter.this.UploadImageBTN.setVisibility(View.INVISIBLE);
                String str = "Save";
                EmployeeRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(EmployeeRecyclerViewAdapter.this.context).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(EmployeeRecyclerViewAdapter.this.Email.getText().toString()) || TextUtils.isEmpty(EmployeeRecyclerViewAdapter.this.FirstName.getText().toString()) || TextUtils.isEmpty(EmployeeRecyclerViewAdapter.this.LastName.getText().toString()) || TextUtils.isEmpty(EmployeeRecyclerViewAdapter.this.Password.getText().toString()) || (TextUtils.isEmpty(EmployeeRecyclerViewAdapter.this.Address.getText().toString()) && TextUtils.isEmpty(EmployeeRecyclerViewAdapter.this.ContactInfo.getText().toString()))) {
                            Toast.makeText(EmployeeRecyclerViewAdapter.this.context, "Enter Complete Data.....", Toast.LENGTH_LONG).show();
                        } else if (EmployeeRecyclerViewAdapter.this.validEmail(EmployeeRecyclerViewAdapter.this.Email.getText().toString())) {
                            EmployeeRecyclerViewAdapter.this.progressDialog = new ProgressDialog(EmployeeRecyclerViewAdapter.this.context);
                            EmployeeRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                            EmployeeRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                            EmployeeRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                            EmployeeRecyclerViewAdapter.this.progressDialog.show();
                            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateEmployee(employeesData.getID(), EmployeeRecyclerViewAdapter.this.FirstName.getText().toString().trim(), EmployeeRecyclerViewAdapter.this.LastName.getText().toString().trim(), EmployeeRecyclerViewAdapter.this.Password.getText().toString().trim(), EmployeeRecyclerViewAdapter.this.Address.getText().toString().trim(), EmployeeRecyclerViewAdapter.this.ContactInfo.getText().toString().trim(), EmployeeRecyclerViewAdapter.this.Email.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Toast.makeText(EmployeeRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                                    EmployeeRecyclerViewAdapter.this.ReloadRecyclerViewData();
                                    EmployeeRecyclerViewAdapter.this.progressDialog.dismiss();
                                }

                                public void onFailure(Call<CommonResponse> call, Throwable th) {
                                    Toast.makeText(EmployeeRecyclerViewAdapter.this.context, "Something Went Wrong......", Toast.LENGTH_LONG).show();
                                    EmployeeRecyclerViewAdapter.this.progressDialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(EmployeeRecyclerViewAdapter.this.context, "Please Enter Valid Email Address......", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                EmployeeRecyclerViewAdapter.this.materialAlertDialogBuilder.setCancelable(false).show();
            }
        });
    }

    public int getItemCount() {
        return this.employeesDataArrayList.size();
    }

    /* access modifiers changed from: private */
    public boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public void setOnItemClick(OnItemClick onItemClick2) {
        this.onItemClick = onItemClick2;
    }

    /* access modifiers changed from: private */
    public void ReloadRecyclerViewData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getEmployeesDetails().enqueue(new Callback<EmployeesResponse>() {
            public void onFailure(Call<EmployeesResponse> call, Throwable th) {
            }

            public void onResponse(Call<EmployeesResponse> call, Response<EmployeesResponse> response) {
                EmployeeRecyclerViewAdapter.this.employeesDataArrayList = ((EmployeesResponse) response.body()).getEmployeesData();
                EmployeeRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
