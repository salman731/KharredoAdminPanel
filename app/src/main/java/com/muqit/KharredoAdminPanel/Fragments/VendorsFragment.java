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
import com.muqit.KharredoAdminPanel.Adapters.VendorsRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Adapters.VendorsRecyclerViewAdapter.OnItemClick;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.Models.VendorsData;
import com.muqit.KharredoAdminPanel.Models.VendorsResponse;
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

public class VendorsFragment extends Fragment {
    private TextInputEditText Address;
    private MaterialButton CNICSelectionBTN;
    private ImageView CNIC_IV;
    private TextInputEditText City;
    private TextInputEditText Company;
    private TextInputEditText ContactInfo;
    private TextInputEditText Country;
    private MaterialTextView DialogTitle;
    private TextInputEditText Email;
    private TextInputEditText FirstName;
    private int IMAGE_REQ = 21;
    /* access modifiers changed from: private */
    public String ImageStatus;
    private ImageView LOGO_IV;
    private TextInputEditText LastName;
    private TextInputEditText Password;
    private MaterialButton PhotoSelectoionBTN;
    /* access modifiers changed from: private */
    public String PhotoType;
    private TextInputEditText RePassword;
    private TextInputEditText State;
    /* access modifiers changed from: private */
    public RecyclerView VendorsRecyclerView;
    private Bitmap bitmapCnic;
    private Bitmap bitmapLogo;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;
    /* access modifiers changed from: private */
    public SessionManager sessionManager;
    private ArrayList<VendorsData> vendorsData;
    /* access modifiers changed from: private */
    public VendorsRecyclerViewAdapter vendorsRecyclerViewAdapter;

    public VendorsFragment() {
        String str = "";
        this.ImageStatus = str;
        this.PhotoType = str;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_vendors, viewGroup, false);
        this.VendorsRecyclerView = (RecyclerView) inflate.findViewById(R.id.Vendors_recyclerview);
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
        menuInflater.inflate(R.menu.vendor_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                VendorsFragment.this.vendorsRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    boolean sortEmail = true;
    boolean sortName = true;
    boolean sortStatus = true;
    boolean sortDate = true;
    boolean sortRole = true;

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.vendor_new_menu_btn:
                View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.vendor_form_layout, null);
                this.Email = (TextInputEditText) inflate.findViewById(R.id.vendor_Email);
                this.FirstName = (TextInputEditText) inflate.findViewById(R.id.vendor_Firstname);
                this.LastName = (TextInputEditText) inflate.findViewById(R.id.vendor_Lastname);
                this.Password = (TextInputEditText) inflate.findViewById(R.id.vendor_Password);
                this.RePassword = (TextInputEditText) inflate.findViewById(R.id.vendor_Repassword);
                this.Address = (TextInputEditText) inflate.findViewById(R.id.vendor_Address);
                this.ContactInfo = (TextInputEditText) inflate.findViewById(R.id.vendor_Phone);
                this.City = (TextInputEditText) inflate.findViewById(R.id.vendor_City);
                this.State = (TextInputEditText) inflate.findViewById(R.id.vendor_State);
                this.Country = (TextInputEditText) inflate.findViewById(R.id.vendor_Country);
                this.Company = (TextInputEditText) inflate.findViewById(R.id.vendor_Country);
                this.PhotoSelectoionBTN = (MaterialButton) inflate.findViewById(R.id.choose_vendor_logo_photo_btn);
                this.CNICSelectionBTN = (MaterialButton) inflate.findViewById(R.id.choose_vendor_cnic_photo_btn);
                this.LOGO_IV = (ImageView) inflate.findViewById(R.id.vendor_logo_photo);
                this.CNIC_IV = (ImageView) inflate.findViewById(R.id.vendor_cnic_photo);
                MaterialTextView materialTextView = (MaterialTextView) inflate.findViewById(R.id.Edit_Title_tv);
                this.DialogTitle = materialTextView;
                materialTextView.setText("Add Vendor");
                this.PhotoSelectoionBTN.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        VendorsFragment.this.ImageStatus = "Add";
                        VendorsFragment.this.PhotoType = "Logo";
                        VendorsFragment.this.startActivityForResult(intent, 21);
                    }
                });
                this.CNICSelectionBTN.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        VendorsFragment.this.ImageStatus = "Add";
                        VendorsFragment.this.PhotoType = "Cnic";
                        VendorsFragment.this.startActivityForResult(intent, 21);
                    }
                });
                String str = "Save";
                new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VendorsFragment.this.UploadVendorData();
                    }
                }).setCancelable(false).show();
                return true;
            case R.id.Vendors_byEmail:
                if(sortEmail)
                {
                    sortEmail = false;
                }
                else
                {
                    sortEmail = true;
                }
                Collections.sort(vendorsData, new Comparator<VendorsData>() {
                    @Override
                    public int compare(VendorsData lhs, VendorsData rhs) {
                        return sortEmail ? lhs.getEmail().compareTo(rhs.getEmail()) : rhs.getEmail().compareTo(lhs.getEmail());
                    }

                });
                vendorsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Vendors_byName:
                if(sortName)
                {
                    sortName = false;
                }
                else
                {
                    sortName = true;
                }
                Collections.sort(vendorsData, new Comparator<VendorsData>() {
                    @Override
                    public int compare(VendorsData lhs, VendorsData rhs) {
                        return sortName ? (lhs.getFirstName().toLowerCase().trim() + " " + lhs.getLastName().toLowerCase().trim()).compareTo(rhs.getFirstName().toLowerCase().trim() + " " + rhs.getLastName().toLowerCase().trim()) : (rhs.getFirstName().toLowerCase().trim() + " " + rhs.getLastName().toLowerCase().trim()).compareTo(lhs.getFirstName().toLowerCase().trim() + " " + lhs.getLastName().toLowerCase().trim());
                    }
                });
                vendorsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Vendors_byStatus:
                if(sortStatus)
                {
                    sortStatus = false;
                }
                else
                {
                    sortStatus = true;
                }
                Collections.sort(vendorsData, new Comparator<VendorsData>() {
                    @Override
                    public int compare(VendorsData lhs, VendorsData rhs) {
                        return sortStatus ? (lhs.getStatus() < rhs.getStatus())? -1 : (lhs.getStatus() > rhs.getStatus()) ? 1 : 0 : (lhs.getStatus() > rhs.getStatus())? -1 : (lhs.getStatus() < rhs.getStatus()) ? 1 : 0 ;
                    }
                });
                vendorsRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.Vendors_byDate:
                if(sortDate)
                {
                    sortDate = false;
                }
                else
                {
                    sortDate = true;
                }
                Collections.sort(vendorsData, new Comparator<VendorsData>() {
                    @Override
                    public int compare(VendorsData lhs, VendorsData rhs) {
                        if (lhs.getDate_Created() == null && rhs.getDate_Created() == null) return 0;
                        if (lhs.getDate_Created() == null) return -1;
                        if (rhs.getDate_Created() == null) return 1;
                        return sortDate ? lhs.getDate_Created().compareTo(rhs.getDate_Created()) : rhs.getDate_Created().compareTo(lhs.getDate_Created());

                    }
                });
                vendorsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Vendors_byRole:
                if(sortRole)
                {
                    sortRole = false;
                }
                else
                {
                    sortRole = true;
                }
                Collections.sort(vendorsData, new Comparator<VendorsData>() {
                    @Override
                    public int compare(VendorsData lhs, VendorsData rhs) {
                        return sortRole ? lhs.getRole().compareTo(rhs.getRole()) : rhs.getRole().compareTo(lhs.getRole());

                    }
                });
                vendorsRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    /* access modifiers changed from: private */
    public void UploadVendorData() {
        if (TextUtils.isEmpty(this.Email.getText().toString()) || TextUtils.isEmpty(this.FirstName.getText().toString()) || TextUtils.isEmpty(this.LastName.getText().toString()) || TextUtils.isEmpty(this.Password.getText().toString()) || ((TextUtils.isEmpty(this.Address.getText().toString()) && TextUtils.isEmpty(this.ContactInfo.getText().toString())) || TextUtils.isEmpty(this.RePassword.getText().toString()) || TextUtils.isEmpty(this.City.getText().toString()) || TextUtils.isEmpty(this.State.getText().toString()) || TextUtils.isEmpty(this.Country.getText().toString()) || TextUtils.isEmpty(this.Company.getText().toString()) || this.LOGO_IV.getDrawable() == null || this.CNIC_IV.getDrawable() == null)) {
            Toast.makeText(getActivity(), "Enter Complete Data.....", Toast.LENGTH_LONG).show();
        } else if (!this.Password.getText().toString().equals(this.RePassword.getText().toString())) {
            Toast.makeText(getActivity(), "Password does not match......", Toast.LENGTH_LONG).show();
        } else if (validEmail(this.Email.getText().toString())) {
            ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
            this.progressDialog = progressDialog2;
            progressDialog2.setMessage("Saving....");
            this.progressDialog.setProgressStyle(0);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.bitmapLogo.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            StringBuilder sb = new StringBuilder();
            String str = "data:image/jpeg;base64,";
            sb.append(str);
            sb.append(encodeToString);
            String sb2 = sb.toString();
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            this.bitmapCnic.compress(CompressFormat.JPEG, 100, byteArrayOutputStream2);
            String encodeToString2 = Base64.encodeToString(byteArrayOutputStream2.toByteArray(), 0);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(encodeToString2);
            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).AddVendor(this.FirstName.getText().toString().trim(), this.LastName.getText().toString().trim(), this.Email.getText().toString().trim(), this.Password.getText().toString().trim(), this.RePassword.getText().toString().trim(), this.ContactInfo.getText().toString().trim(), this.Address.getText().toString().trim(), sb2, sb3.toString(), this.City.getText().toString().trim(), this.Country.getText().toString().trim(), this.State.getText().toString().trim(), this.Company.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    Toast.makeText(VendorsFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    VendorsFragment.this.progressDialog.dismiss();
                    VendorsFragment.this.PoulateData();
                }

                public void onFailure(Call<CommonResponse> call, Throwable th) {
                    VendorsFragment.this.progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Invalid Email Address.......", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        String str = "Add";
        if (i == this.IMAGE_REQ && i2 == -1 && intent != null && this.ImageStatus.equals(str) && this.PhotoType.equals("Logo")) {
            try {
                Bitmap bitmap = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                this.bitmapLogo = bitmap;
                this.LOGO_IV.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (i == this.IMAGE_REQ && i2 == -1 && intent != null && this.ImageStatus.equals(str) && this.PhotoType.equals("Cnic")) {
            try {
                Bitmap bitmap2 = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                this.bitmapCnic = bitmap2;
                this.CNIC_IV.setImageBitmap(bitmap2);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (i == this.IMAGE_REQ && i2 == -1 && intent != null && this.ImageStatus.equals("Update")) {
            try {
                this.bitmapCnic = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.bitmapCnic.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/jpeg;base64,");
            sb.append(encodeToString);
            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateEmployeeImage(this.sessionManager.getStringData("Eposition"), sb.toString()).enqueue(new Callback<CommonResponse>() {
                public void onFailure(Call<CommonResponse> call, Throwable th) {
                }

                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    Toast.makeText(VendorsFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    VendorsFragment.this.PoulateData();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getVendorsDetails().enqueue(new Callback<VendorsResponse>() {
            public void onResponse(Call<VendorsResponse> call, Response<VendorsResponse> response) {
                vendorsData = response.body().getVendorsData();
                VendorsFragment.this.vendorsRecyclerViewAdapter = new VendorsRecyclerViewAdapter(((VendorsResponse) response.body()).getVendorsData(), VendorsFragment.this.getActivity());
                VendorsFragment.this.VendorsRecyclerView.setLayoutManager(new LinearLayoutManager(VendorsFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(VendorsFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(VendorsFragment.this.getContext(), R.drawable.divider));
                VendorsFragment.this.VendorsRecyclerView.addItemDecoration(dividerItemDecoration);
                VendorsFragment.this.VendorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                VendorsFragment.this.vendorsRecyclerViewAdapter.setOnItemClick(new OnItemClick() {
                    public void getPosition(String str) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        VendorsFragment.this.ImageStatus = "Update";
                        VendorsFragment.this.sessionManager.setStringData("Eposition", str);
                        VendorsFragment.this.startActivityForResult(intent, 21);
                    }
                });
                VendorsFragment.this.VendorsRecyclerView.setAdapter(VendorsFragment.this.vendorsRecyclerViewAdapter);
                VendorsFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<VendorsResponse> call, Throwable th) {
                Toast.makeText(VendorsFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                VendorsFragment.this.progressDialog.dismiss();
            }
        });
    }
}
