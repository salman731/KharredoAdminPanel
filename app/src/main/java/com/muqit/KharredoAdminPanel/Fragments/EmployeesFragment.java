package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.EmployeeRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Adapters.EmployeeRecyclerViewAdapter.OnItemClick;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.EmployeesData;
import com.muqit.KharredoAdminPanel.Models.EmployeesResponse;
import com.muqit.KharredoAdminPanel.Models.NewUserResponse;
import com.muqit.KharredoAdminPanel.Models.SalesData;
import com.muqit.KharredoAdminPanel.R;
import com.muqit.KharredoAdminPanel.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmployeesFragment extends Fragment {
    private TextInputEditText Address;
    private TextInputEditText ContactInfo;
    private MaterialTextView DialogTitle;
    private TextInputEditText Email;
    private TextInputEditText FirstName;
    private int IMAGE_REQ = 21;
    /* access modifiers changed from: private */
    public String ImageStatus;
    private String ItemPostion;
    private TextInputEditText LastName;
    private TextInputEditText Password;
    private ImageView Photo_IV;
    private Bitmap bitmap;
    /* access modifiers changed from: private */
    public EmployeeRecyclerViewAdapter employeeRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public RecyclerView employeesRecylerView;
    private ArrayList<EmployeesData> employeesData;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;
    SessionManager sessionManager;

    public EmployeesFragment() {
        String str = "";
        this.ImageStatus = str;
        this.ItemPostion = str;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_employees, viewGroup, false);
        this.employeesRecylerView = (RecyclerView) inflate.findViewById(R.id.Employees_recyclerview);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        setHasOptionsMenu(true);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        this.sessionManager = new SessionManager(getActivity());
        this.progressDialog.show();
        PoulateData();
        return inflate;
    }

    /* access modifiers changed from: private */
    public void PoulateData() {
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getEmployeesDetails().enqueue(new Callback<EmployeesResponse>() {
            public void onResponse(Call<EmployeesResponse> call, Response<EmployeesResponse> response) {
                employeesData = response.body().getEmployeesData();
                EmployeesFragment.this.employeeRecyclerViewAdapter = new EmployeeRecyclerViewAdapter(((EmployeesResponse) response.body()).getEmployeesData(), EmployeesFragment.this.getActivity());
                EmployeesFragment.this.employeesRecylerView.setLayoutManager(new LinearLayoutManager(EmployeesFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(EmployeesFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(EmployeesFragment.this.getContext(), R.drawable.divider));
                EmployeesFragment.this.employeesRecylerView.addItemDecoration(dividerItemDecoration);
                EmployeesFragment.this.employeeRecyclerViewAdapter.setOnItemClick(new OnItemClick() {
                    public void getPosition(String str) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        EmployeesFragment.this.ImageStatus = "Update";
                        EmployeesFragment.this.sessionManager.setStringData("Eposition", str);
                        EmployeesFragment.this.startActivityForResult(intent, 21);
                    }
                });
                EmployeesFragment.this.employeesRecylerView.setItemAnimator(new DefaultItemAnimator());
                EmployeesFragment.this.employeesRecylerView.setAdapter(EmployeesFragment.this.employeeRecyclerViewAdapter);
                EmployeesFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<EmployeesResponse> call, Throwable th) {
                Toast.makeText(EmployeesFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                EmployeesFragment.this.progressDialog.dismiss();
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.employee_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                EmployeesFragment.this.employeeRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    boolean sortEmail = true;
    boolean sortName = true;
    boolean sortStatus = true;
    boolean sortDate = true;

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.employee_new_menu_btn:
                View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.edit_details_layout, null);
                this.Email = (TextInputEditText) inflate.findViewById(R.id.edit_Email);
                this.FirstName = (TextInputEditText) inflate.findViewById(R.id.edit_firstname);
                this.LastName = (TextInputEditText) inflate.findViewById(R.id.edit_lastname);
                this.Password = (TextInputEditText) inflate.findViewById(R.id.edit_password);
                this.Address = (TextInputEditText) inflate.findViewById(R.id.edit_address);
                this.ContactInfo = (TextInputEditText) inflate.findViewById(R.id.edit_contactinfo);
                MaterialTextView materialTextView = (MaterialTextView) inflate.findViewById(R.id.Edit_Title_tv);
                this.Photo_IV = (ImageView) inflate.findViewById(R.id.edit_photo);
                materialTextView.setText("Add Employee");
                ((MaterialButton) inflate.findViewById(R.id.edit_upload_btn)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        EmployeesFragment.this.ImageStatus = "Add";
                        EmployeesFragment.this.startActivityForResult(intent, 21);
                    }
                });
                String str = "Save";
                new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EmployeesFragment.this.UploadImage();
                    }
                }).setCancelable(false).show();
                return true;
            case R.id.Employee_byEmail:
                if(sortEmail)
                {
                    sortEmail = false;
                }
                else
                {
                    sortEmail = true;
                }
                Collections.sort(employeesData, new Comparator<EmployeesData>() {
                    @Override
                    public int compare(EmployeesData lhs, EmployeesData rhs) {
                        return sortEmail ? lhs.getEmail().compareTo(rhs.getEmail()) : rhs.getEmail().compareTo(lhs.getEmail());
                    }

                });
                employeeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Employee_byName:
                if(sortName)
                {
                    sortName = false;
                }
                else
                {
                    sortName = true;
                }
                Collections.sort(employeesData, new Comparator<EmployeesData>() {
                    @Override
                    public int compare(EmployeesData lhs, EmployeesData rhs) {
                        return sortName ? (lhs.getFirstName().toLowerCase().trim() + " " + lhs.getLastName().toLowerCase().trim()).compareTo(rhs.getFirstName().toLowerCase().trim() + " " + rhs.getLastName().toLowerCase().trim()) : (rhs.getFirstName().toLowerCase().trim() + " " + rhs.getLastName().toLowerCase().trim()).compareTo(lhs.getFirstName().toLowerCase().trim() + " " + lhs.getLastName().toLowerCase().trim());
                    }
                });
                employeeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Employee_byStatus:
                if(sortStatus)
                {
                    sortStatus = false;
                }
                else
                {
                    sortStatus = true;
                }
                Collections.sort(employeesData, new Comparator<EmployeesData>() {
                    @Override
                    public int compare(EmployeesData lhs, EmployeesData rhs) {
                        return sortStatus ? (lhs.getStatus() < rhs.getStatus())? -1 : (lhs.getStatus() > rhs.getStatus()) ? 1 : 0 : (lhs.getStatus() > rhs.getStatus())? -1 : (lhs.getStatus() < rhs.getStatus()) ? 1 : 0 ;
                    }
                });
                employeeRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.Employee_byDate:
                if(sortDate)
                {
                    sortDate = false;
                }
                else
                {
                    sortDate = true;
                }
                Collections.sort(employeesData, new Comparator<EmployeesData>() {
                    @Override
                    public int compare(EmployeesData lhs, EmployeesData rhs) {
                        return sortDate ? lhs.getCreated_On().compareTo(rhs.getCreated_On()) : rhs.getCreated_On().compareTo(lhs.getCreated_On());}
                });
                employeeRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.IMAGE_REQ && i2 == -1 && intent != null && this.ImageStatus.equals("Add")) {
            try {
                Bitmap bitmap2 = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                this.bitmap = bitmap2;
                this.Photo_IV.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (i == this.IMAGE_REQ && i2 == -1 && intent != null && this.ImageStatus.equals("Update")) {
            try {
                this.bitmap = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this.bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                StringBuilder sb = new StringBuilder();
                sb.append("data:image/jpeg;base64,");
                sb.append(encodeToString);
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateEmployeeImage(this.sessionManager.getStringData("Eposition"), sb.toString()).enqueue(new Callback<CommonResponse>() {
                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                    }

                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(EmployeesFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        EmployeesFragment.this.PoulateData();
                    }
                });
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }


    /* access modifiers changed from: private */
    public void UploadImage() {
        RetrofitAPI retrofitAPI = (RetrofitAPI) this.retrofit.create(RetrofitAPI.class);
        if (TextUtils.isEmpty(this.Email.getText().toString()) || TextUtils.isEmpty(this.FirstName.getText().toString()) || TextUtils.isEmpty(this.LastName.getText().toString()) || TextUtils.isEmpty(this.Password.getText().toString()) || ((TextUtils.isEmpty(this.Address.getText().toString()) && TextUtils.isEmpty(this.ContactInfo.getText().toString())) || this.Photo_IV.getDrawable() == null)) {
            Toast.makeText(getActivity(), "Enter Complete Data.....", Toast.LENGTH_LONG).show();
        } else if (validEmail(this.Email.getText().toString())) {
            ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
            this.progressDialog = progressDialog2;
            progressDialog2.setMessage("Saving....");
            setHasOptionsMenu(true);
            this.progressDialog.setProgressStyle(0);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/jpeg;base64,");
            sb.append(encodeToString);
            retrofitAPI.AddEmployee(this.FirstName.getText().toString().trim(), this.LastName.getText().toString().trim(), this.Email.getText().toString().trim(), this.Password.getText().toString().trim(), this.ContactInfo.getText().toString().trim(), this.Address.getText().toString().trim(), sb.toString()).enqueue(new Callback<NewUserResponse>() {
                public void onResponse(Call<NewUserResponse> call, Response<NewUserResponse> response) {
                    Toast.makeText(EmployeesFragment.this.getContext(), ((NewUserResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    EmployeesFragment.this.progressDialog.dismiss();
                    EmployeesFragment.this.PoulateData();
                }

                public void onFailure(Call<NewUserResponse> call, Throwable th) {
                    Toast.makeText(EmployeesFragment.this.getContext(), th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    EmployeesFragment.this.progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please Enter Valid Email Address......", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }
}
