package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.service.autofill.UserData;
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
import com.muqit.KharredoAdminPanel.Adapters.UserRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Adapters.UserRecyclerViewAdapter.OnItemClick;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.EmployeesData;
import com.muqit.KharredoAdminPanel.Models.NewUserResponse;
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.Models.UsersResponse;
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

public class UserFragment extends Fragment {
    private TextInputEditText Address;
    private TextInputEditText ContactInfo;
    private MaterialTextView DialogTitle;
    private TextInputEditText Email;
    private TextInputEditText FirstName;
    private int IMAGE_REQ = 21;
    /* access modifiers changed from: private */
    public String ImageStatus = "";
    private TextInputEditText LastName;
    private TextInputEditText Password;
    private ImageView Photo_IV;
    /* access modifiers changed from: private */
    public RecyclerView UsersRecyclerView;
    private Bitmap bitmap;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;
    /* access modifiers changed from: private */
    public SessionManager sessionManager;
    private ArrayList<UsersData> userData;
    /* access modifiers changed from: private */
    public UserRecyclerViewAdapter userRecyclerViewAdapter;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_user, viewGroup, false);
        this.UsersRecyclerView = (RecyclerView) inflate.findViewById(R.id.Users_recyclerview);
        setHasOptionsMenu(true);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        this.sessionManager = new SessionManager(getActivity());
        PoulateData();
        return inflate;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.user_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                UserFragment.this.userRecyclerViewAdapter.getFilter().filter(str);
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
            case R.id.user_new_menu_btn:
                View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.edit_details_layout, null);
                this.Email = (TextInputEditText) inflate.findViewById(R.id.edit_Email);
                this.FirstName = (TextInputEditText) inflate.findViewById(R.id.edit_firstname);
                this.LastName = (TextInputEditText) inflate.findViewById(R.id.edit_lastname);
                this.Password = (TextInputEditText) inflate.findViewById(R.id.edit_password);
                this.Address = (TextInputEditText) inflate.findViewById(R.id.edit_address);
                this.ContactInfo = (TextInputEditText) inflate.findViewById(R.id.edit_contactinfo);
                this.DialogTitle = (MaterialTextView) inflate.findViewById(R.id.Edit_Title_tv);
                this.Photo_IV = (ImageView) inflate.findViewById(R.id.edit_photo);
                this.DialogTitle.setText("Add User");
                ((MaterialButton) inflate.findViewById(R.id.edit_upload_btn)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        UserFragment.this.ImageStatus = "Add";
                        UserFragment.this.startActivityForResult(intent, 21);
                    }
                });
                String str = "Save";
                new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserFragment.this.UploadImage();
                    }
                }).setCancelable(false).show();
                return true;
            case R.id.Users_byEmail:
                if(sortEmail)
                {
                    sortEmail = false;
                }
                else
                {
                    sortEmail = true;
                }
                Collections.sort(userData, new Comparator<UsersData>() {
                    @Override
                    public int compare(UsersData lhs, UsersData rhs) {
                        return sortEmail ? lhs.getEmail().compareTo(rhs.getEmail()) : rhs.getEmail().compareTo(lhs.getEmail());
                    }

                });
                userRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Users_byName:
                if(sortName)
                {
                    sortName = false;
                }
                else
                {
                    sortName = true;
                }
                Collections.sort(userData, new Comparator<UsersData>() {
                    @Override
                    public int compare(UsersData lhs, UsersData rhs) {
                        return sortName ? (lhs.getFirstName().toLowerCase().trim() + " " + lhs.getLastName().toLowerCase().trim()).compareTo(rhs.getFirstName().toLowerCase().trim() + " " + rhs.getLastName().toLowerCase().trim()) : (rhs.getFirstName().toLowerCase().trim() + " " + rhs.getLastName().toLowerCase().trim()).compareTo(lhs.getFirstName().toLowerCase().trim() + " " + lhs.getLastName().toLowerCase().trim());
                    }
                });
                userRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Users_byStatus:
                if(sortStatus)
                {
                    sortStatus = false;
                }
                else
                {
                    sortStatus = true;
                }
                Collections.sort(userData, new Comparator<UsersData>() {
                    @Override
                    public int compare(UsersData lhs, UsersData rhs) {
                        return sortStatus ? (lhs.getStatus() < rhs.getStatus())? -1 : (lhs.getStatus() > rhs.getStatus()) ? 1 : 0 : (lhs.getStatus() > rhs.getStatus())? -1 : (lhs.getStatus() < rhs.getStatus()) ? 1 : 0 ;
                    }
                });
                userRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.Users_byDate:
                if(sortDate)
                {
                    sortDate = false;
                }
                else
                {
                    sortDate = true;
                }
                Collections.sort(userData, new Comparator<UsersData>() {
                    @Override
                    public int compare(UsersData lhs, UsersData rhs) {
                        if (lhs.getCreated_Date() == null && rhs.getCreated_Date() == null) return 0;
                        if (lhs.getCreated_Date() == null) return -1;
                        if (rhs.getCreated_Date() == null) return 1;
                        return sortDate ? lhs.getCreated_Date().compareTo(rhs.getCreated_Date()) : rhs.getCreated_Date().compareTo(lhs.getCreated_Date());

                    }
                });
                userRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
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
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/jpeg;base64,");
            sb.append(encodeToString);
            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateEmployeeImage(this.sessionManager.getStringData("Uposition"), sb.toString()).enqueue(new Callback<CommonResponse>() {
                public void onFailure(Call<CommonResponse> call, Throwable th) {
                }

                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    Toast.makeText(UserFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    UserFragment.this.PoulateData();
                }
            });
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
            this.progressDialog.setProgressStyle(0);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/jpeg;base64,");
            sb.append(encodeToString);
            retrofitAPI.AddUser(this.FirstName.getText().toString().trim(), this.LastName.getText().toString().trim(), this.Email.getText().toString().trim(), this.Password.getText().toString().trim(), this.ContactInfo.getText().toString().trim(), this.Address.getText().toString().trim(), sb.toString()).enqueue(new Callback<NewUserResponse>() {
                public void onResponse(Call<NewUserResponse> call, Response<NewUserResponse> response) {
                    Toast.makeText(UserFragment.this.getContext(), ((NewUserResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    UserFragment.this.progressDialog.dismiss();
                    UserFragment.this.PoulateData();
                }

                public void onFailure(Call<NewUserResponse> call, Throwable th) {
                    Toast.makeText(UserFragment.this.getContext(), th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    UserFragment.this.progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please Enter Valid Email Address......", Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: 0000 */
    public void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getUsersDetails().enqueue(new Callback<UsersResponse>() {
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                userData = response.body().getUsersData();
                UserFragment.this.userRecyclerViewAdapter = new UserRecyclerViewAdapter(((UsersResponse) response.body()).getUsersData(), UserFragment.this.getActivity());
                UserFragment.this.UsersRecyclerView.setLayoutManager(new LinearLayoutManager(UserFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(UserFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(UserFragment.this.getContext(), R.drawable.divider));
                UserFragment.this.UsersRecyclerView.addItemDecoration(dividerItemDecoration);
                UserFragment.this.UsersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                UserFragment.this.userRecyclerViewAdapter.setOnItemClick(new OnItemClick() {
                    public void getPosition(String str) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        UserFragment.this.ImageStatus = "Update";
                        UserFragment.this.sessionManager.setStringData("Uposition", str);
                        UserFragment.this.startActivityForResult(intent, 21);
                    }
                });
                UserFragment.this.UsersRecyclerView.setAdapter(UserFragment.this.userRecyclerViewAdapter);
                UserFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<UsersResponse> call, Throwable th) {
                Toast.makeText(UserFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                UserFragment.this.progressDialog.dismiss();
            }
        });
    }
}
