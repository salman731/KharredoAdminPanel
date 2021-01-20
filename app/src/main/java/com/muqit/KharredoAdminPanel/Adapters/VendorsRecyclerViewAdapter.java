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
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.Models.VendorDetailResponse;
import com.muqit.KharredoAdminPanel.Models.VendorsData;
import com.muqit.KharredoAdminPanel.Models.VendorsResponse;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorsRecyclerViewAdapter extends Adapter<VendorsRecyclerViewAdapter.VendorsItemsViewHolder> implements Filterable {
    private Filter VendorsFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(VendorsRecyclerViewAdapter.this.vendorsDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                String str = "Active";
                String str1 = "Inactive";
                for (VendorsData vendorsData : VendorsRecyclerViewAdapter.this.vendorsDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(vendorsData.getEmail()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(vendorsData.getFirstName().toLowerCase() + " " + vendorsData.getLastName().toLowerCase()).contains(trim) || SalesRecyclerViewAdapter.blankIfNull(vendorsData.getDate_Created()).toLowerCase().contains(trim)) {
                        arrayList.add(vendorsData);
                    }
                    else if(str.toLowerCase().contains(trim) && String.valueOf(vendorsData.getStatus()).contains("1"))
                    {
                        arrayList.add(vendorsData);
                    }
                    else if (str1.toLowerCase().contains(trim) && String.valueOf(vendorsData.getStatus()).contains("0"))
                    {
                        arrayList.add(vendorsData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            VendorsRecyclerViewAdapter.this.vendorsDataArrayList.clear();
            VendorsRecyclerViewAdapter.this.vendorsDataArrayList.addAll((List) filterResults.values);
            VendorsRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    /* access modifiers changed from: private */
    public TextInputEditText Address;
    /* access modifiers changed from: private */
    public MaterialButton CNICSelectionBTN;
    /* access modifiers changed from: private */
    public ImageView CNIC_IV;
    /* access modifiers changed from: private */
    public TextInputEditText City;
    /* access modifiers changed from: private */
    public TextInputEditText Company;
    /* access modifiers changed from: private */
    public TextInputEditText ContactInfo;
    /* access modifiers changed from: private */
    public TextInputEditText Country;
    /* access modifiers changed from: private */
    public MaterialTextView DialogTitle;
    /* access modifiers changed from: private */
    public TextInputEditText Email;
    /* access modifiers changed from: private */
    public TextInputEditText FirstName;
    public String IMAGE_BASE_URL = "https://kharredo.com";
    /* access modifiers changed from: private */
    public ImageView LOGO_IV;
    /* access modifiers changed from: private */
    public TextInputEditText LastName;
    /* access modifiers changed from: private */
    public TextInputEditText Password;
    /* access modifiers changed from: private */
    public MaterialButton PhotoSelectoionBTN;
    /* access modifiers changed from: private */
    public TextInputEditText RePassword;
    /* access modifiers changed from: private */
    public TextInputEditText State;
    public String V_IMAGE_BASE_URL = "https://kharredo.com/CnicVendors/";
    /* access modifiers changed from: private */
    public Context context;
    OnItemClick onItemClick;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public ArrayList<VendorsData> vendorsDataArrayList;
    public List<VendorsData> vendorsDataArrayListFullList;

    @Override
    public Filter getFilter() {
        return VendorsFilter;
    }

    public interface OnItemClick {
        void getPosition(String str);
    }

    public class VendorsItemsViewHolder extends ViewHolder {
        public MaterialTextView Date_Created;
        public MaterialButton Delete_BTN;
        public MaterialButton Edit_BTN;
        public MaterialTextView Email;
        public MaterialButton Make_Feature_BTN;
        public MaterialButton Make_Shop_BUTN;
        public MaterialButton Make_Whole_Seller_BTN;
        public MaterialTextView Name;
        public MaterialTextView Role;
        public MaterialTextView Status;
        public MaterialTextView VendorUpdateImage_TV;
        public ImageView Vendor_CNIC_Image;
        public ImageView Vendor_Image;
        public MaterialTextView Vendors_Activation_BTN;

        public VendorsItemsViewHolder(View view) {
            super(view);
            this.Name = (MaterialTextView) view.findViewById(R.id.Vendors_name);
            this.Email = (MaterialTextView) view.findViewById(R.id.Vendors_email);
            this.Status = (MaterialTextView) view.findViewById(R.id.Vendors_status);
            this.Role = (MaterialTextView) view.findViewById(R.id.Vendors_role);
            this.Date_Created = (MaterialTextView) view.findViewById(R.id.Vendors_date_added);
            this.Edit_BTN = (MaterialButton) view.findViewById(R.id.Vendors_edit_btn);
            this.Delete_BTN = (MaterialButton) view.findViewById(R.id.Vendors_delete_btn);
            this.Make_Feature_BTN = (MaterialButton) view.findViewById(R.id.Vendors_make_featured);
            this.Make_Shop_BUTN = (MaterialButton) view.findViewById(R.id.Vendors_make_shop);
            this.Vendor_Image = (ImageView) view.findViewById(R.id.Vendors_image);
            this.Vendor_CNIC_Image = (ImageView) view.findViewById(R.id.Vendors_CNIC_image);
            this.Vendors_Activation_BTN = (MaterialTextView) view.findViewById(R.id.Vendors_activate_btn);
            this.Make_Whole_Seller_BTN = (MaterialButton) view.findViewById(R.id.Vendors_make_whole_seller);
            this.VendorUpdateImage_TV = (MaterialTextView) view.findViewById(R.id.Vendor_logo_image_update_iv);
        }
    }

    private void UpdateVendorData() {
    }

    public VendorsRecyclerViewAdapter(ArrayList<VendorsData> arrayList, Context context2) {
        this.vendorsDataArrayList = arrayList;
        this.context = context2;
        this.vendorsDataArrayListFullList = new ArrayList(arrayList);
    }

    public VendorsItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VendorsItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendor_list_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(final VendorsItemsViewHolder vendorsItemsViewHolder, int i) {
        final VendorsData vendorsData = (VendorsData) this.vendorsDataArrayList.get(i);
        String str = "Make Shop";
        if (vendorsData.getRole().equals("brand")) {
            vendorsItemsViewHolder.Make_Shop_BUTN.setText(str);
            vendorsItemsViewHolder.Make_Shop_BUTN.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shop, 0, 0, 0);
            vendorsItemsViewHolder.Make_Feature_BTN.setVisibility(4);
            vendorsItemsViewHolder.Make_Whole_Seller_BTN.setEnabled(true);
        } else if (vendorsData.getRole().equals("shop")) {
            vendorsItemsViewHolder.Make_Shop_BUTN.setText("Make Brand");
            vendorsItemsViewHolder.Make_Shop_BUTN.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_brand, 0, 0, 0);
            vendorsItemsViewHolder.Make_Feature_BTN.setVisibility(0);
            vendorsItemsViewHolder.Make_Whole_Seller_BTN.setEnabled(true);
        } else if (vendorsData.getRole().equals("wholesale")) {
            vendorsItemsViewHolder.Make_Shop_BUTN.setText(str);
            vendorsItemsViewHolder.Make_Shop_BUTN.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shop, 0, 0, 0);
            vendorsItemsViewHolder.Make_Feature_BTN.setVisibility(4);
            vendorsItemsViewHolder.Make_Whole_Seller_BTN.setEnabled(false);
        }
        MaterialTextView materialTextView = vendorsItemsViewHolder.Name;
        StringBuilder sb = new StringBuilder();
        sb.append(vendorsData.getFirstName());
        sb.append(" ");
        sb.append(vendorsData.getLastName());
        materialTextView.setText(sb.toString());
        vendorsItemsViewHolder.Date_Created.setText(vendorsData.getDate_Created());
        vendorsItemsViewHolder.Role.setText(vendorsData.getRole());
        vendorsItemsViewHolder.Email.setText(vendorsData.getEmail());
        if (vendorsData.getStatus() == 0) {
            vendorsItemsViewHolder.Status.setBackground(ContextCompat.getDrawable(this.context, R.drawable.inactive_status));
            vendorsItemsViewHolder.Status.setText("Inactive");
            vendorsItemsViewHolder.Vendors_Activation_BTN.setVisibility(0);
            vendorsItemsViewHolder.Delete_BTN.setEnabled(false);
        } else if (vendorsData.getStatus() == 1) {
            vendorsItemsViewHolder.Status.setBackground(ContextCompat.getDrawable(this.context, R.drawable.active_status));
            vendorsItemsViewHolder.Status.setText("Active");
            vendorsItemsViewHolder.Vendors_Activation_BTN.setVisibility(8);
            vendorsItemsViewHolder.Delete_BTN.setEnabled(true);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.IMAGE_BASE_URL);
        sb2.append(vendorsData.getVendorPhotoURL());
        String sb3 = sb2.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.V_IMAGE_BASE_URL);
        sb4.append(vendorsData.getVendorCNICURL());
        String sb5 = sb4.toString();
        Picasso.get().load(sb3).fit().into(vendorsItemsViewHolder.Vendor_Image);
        Picasso.get().load(sb5).fit().into(vendorsItemsViewHolder.Vendor_CNIC_Image);
        if (vendorsItemsViewHolder.Vendor_CNIC_Image.getDrawable() == null) {
            vendorsItemsViewHolder.Vendor_CNIC_Image.setImageResource(R.drawable.noimage);
        }
        if (vendorsItemsViewHolder.Vendor_Image.getDrawable() == null) {
            vendorsItemsViewHolder.Vendor_Image.setImageResource(R.drawable.noimage);
        }
        vendorsItemsViewHolder.Make_Shop_BUTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(VendorsRecyclerViewAdapter.this.context, vendorsItemsViewHolder.Make_Shop_BUTN.getText(), 1).show();
                String str = "Updating....";
                if (vendorsItemsViewHolder.Make_Shop_BUTN.getText().equals("Make Brand")) {
                    VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                    VendorsRecyclerViewAdapter.this.progressDialog.setMessage(str);
                    VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                    VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                    VendorsRecyclerViewAdapter.this.progressDialog.show();
                    ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).MakeBrand(vendorsData.getID()).enqueue(new Callback<CommonResponse>() {
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                            VendorsRecyclerViewAdapter.this.RefreshData();
                            VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        }

                        public void onFailure(Call<CommonResponse> call, Throwable th) {
                            VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        }
                    });
                }
                if (vendorsItemsViewHolder.Make_Shop_BUTN.getText().equals("Make Shop")) {
                    VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                    VendorsRecyclerViewAdapter.this.progressDialog.setMessage(str);
                    VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                    VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                    VendorsRecyclerViewAdapter.this.progressDialog.show();
                    ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).MakeShop(vendorsData.getID()).enqueue(new Callback<CommonResponse>() {
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                            VendorsRecyclerViewAdapter.this.RefreshData();
                            VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        }

                        public void onFailure(Call<CommonResponse> call, Throwable th) {
                            VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        }
                    });
                }
            }
        });
        vendorsItemsViewHolder.Make_Feature_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                VendorsRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                VendorsRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).MakeFeatured(vendorsData.getID()).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        VendorsRecyclerViewAdapter.this.RefreshData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        vendorsItemsViewHolder.Make_Whole_Seller_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                VendorsRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                VendorsRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).MakeWholeSale(vendorsData.getID()).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                        VendorsRecyclerViewAdapter.this.RefreshData();
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        vendorsItemsViewHolder.Delete_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                VendorsRecyclerViewAdapter.this.progressDialog.setMessage("Blocking....");
                VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                VendorsRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockUser(vendorsData.getID(), 0).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        VendorsRecyclerViewAdapter.this.RefreshData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        vendorsItemsViewHolder.Vendors_Activation_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                VendorsRecyclerViewAdapter.this.progressDialog.setMessage("Activating....");
                VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                VendorsRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockUser(vendorsData.getID(), 1).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                        VendorsRecyclerViewAdapter.this.RefreshData();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
        vendorsItemsViewHolder.Edit_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = LayoutInflater.from(VendorsRecyclerViewAdapter.this.context).inflate(R.layout.vendor_form_layout, null);
                VendorsRecyclerViewAdapter.this.Email = (TextInputEditText) inflate.findViewById(R.id.vendor_Email);
                VendorsRecyclerViewAdapter.this.FirstName = (TextInputEditText) inflate.findViewById(R.id.vendor_Firstname);
                VendorsRecyclerViewAdapter.this.LastName = (TextInputEditText) inflate.findViewById(R.id.vendor_Lastname);
                VendorsRecyclerViewAdapter.this.Password = (TextInputEditText) inflate.findViewById(R.id.vendor_Password);
                VendorsRecyclerViewAdapter.this.RePassword = (TextInputEditText) inflate.findViewById(R.id.vendor_Repassword);
                VendorsRecyclerViewAdapter.this.Address = (TextInputEditText) inflate.findViewById(R.id.vendor_Address);
                VendorsRecyclerViewAdapter.this.ContactInfo = (TextInputEditText) inflate.findViewById(R.id.vendor_Phone);
                VendorsRecyclerViewAdapter.this.City = (TextInputEditText) inflate.findViewById(R.id.vendor_City);
                VendorsRecyclerViewAdapter.this.State = (TextInputEditText) inflate.findViewById(R.id.vendor_State);
                VendorsRecyclerViewAdapter.this.Country = (TextInputEditText) inflate.findViewById(R.id.vendor_Country);
                VendorsRecyclerViewAdapter.this.Company = (TextInputEditText) inflate.findViewById(R.id.vendor_Company);
                VendorsRecyclerViewAdapter.this.PhotoSelectoionBTN = (MaterialButton) inflate.findViewById(R.id.choose_vendor_logo_photo_btn);
                VendorsRecyclerViewAdapter.this.CNICSelectionBTN = (MaterialButton) inflate.findViewById(R.id.choose_vendor_cnic_photo_btn);
                VendorsRecyclerViewAdapter.this.LOGO_IV = (ImageView) inflate.findViewById(R.id.vendor_logo_photo);
                VendorsRecyclerViewAdapter.this.CNIC_IV = (ImageView) inflate.findViewById(R.id.vendor_cnic_photo);
                VendorsRecyclerViewAdapter.this.PhotoSelectoionBTN.setVisibility(4);
                VendorsRecyclerViewAdapter.this.CNICSelectionBTN.setVisibility(4);
                VendorsRecyclerViewAdapter.this.LOGO_IV.setVisibility(4);
                VendorsRecyclerViewAdapter.this.CNIC_IV.setVisibility(4);
                VendorsRecyclerViewAdapter.this.DialogTitle = (MaterialTextView) inflate.findViewById(R.id.Edit_Title_tv);
                VendorsRecyclerViewAdapter.this.DialogTitle.setText("Update Vendor");
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).VendorDetail(vendorsData.getID()).enqueue(new Callback<VendorDetailResponse>() {
                    public void onFailure(Call<VendorDetailResponse> call, Throwable th) {
                    }

                    public void onResponse(Call<VendorDetailResponse> call, Response<VendorDetailResponse> response) {
                        VendorsRecyclerViewAdapter.this.Email.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getEmail());
                        VendorsRecyclerViewAdapter.this.FirstName.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getFirstName());
                        VendorsRecyclerViewAdapter.this.LastName.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getLastName());
                        VendorsRecyclerViewAdapter.this.Password.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getPassword());
                        VendorsRecyclerViewAdapter.this.RePassword.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getPassword());
                        VendorsRecyclerViewAdapter.this.ContactInfo.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getPhone());
                        VendorsRecyclerViewAdapter.this.Address.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getAddress());
                        VendorsRecyclerViewAdapter.this.State.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getState());
                        VendorsRecyclerViewAdapter.this.Country.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getCountry());
                        VendorsRecyclerViewAdapter.this.Company.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getCompany());
                        VendorsRecyclerViewAdapter.this.City.setText(((VendorDetailResponse) response.body()).getVendorDetailData().getCity());
                    }
                });
                String str = "Save";
                new MaterialAlertDialogBuilder(VendorsRecyclerViewAdapter.this.context).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.Email.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.FirstName.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.LastName.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.Password.getText().toString()) || ((TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.Address.getText().toString()) && TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.ContactInfo.getText().toString())) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.RePassword.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.City.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.State.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.Country.getText().toString()) || TextUtils.isEmpty(VendorsRecyclerViewAdapter.this.Company.getText().toString()))) {
                            Toast.makeText(VendorsRecyclerViewAdapter.this.context, "Enter Complete Data.....", 1).show();
                        } else if (VendorsRecyclerViewAdapter.this.Password.getText().toString().equals(VendorsRecyclerViewAdapter.this.RePassword.getText().toString()) && VendorsRecyclerViewAdapter.this.validEmail(VendorsRecyclerViewAdapter.this.Email.getText().toString())) {
                            VendorsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(VendorsRecyclerViewAdapter.this.context);
                            VendorsRecyclerViewAdapter.this.progressDialog.setMessage("Updating....");
                            VendorsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                            VendorsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                            VendorsRecyclerViewAdapter.this.progressDialog.show();
                            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateVendor(vendorsData.getID(), VendorsRecyclerViewAdapter.this.FirstName.getText().toString().trim(), VendorsRecyclerViewAdapter.this.LastName.getText().toString().trim(), VendorsRecyclerViewAdapter.this.Email.getText().toString().trim(), VendorsRecyclerViewAdapter.this.Password.getText().toString().trim(), VendorsRecyclerViewAdapter.this.ContactInfo.getText().toString().trim(), VendorsRecyclerViewAdapter.this.Address.getText().toString().trim(), VendorsRecyclerViewAdapter.this.City.getText().toString().trim(), VendorsRecyclerViewAdapter.this.Country.getText().toString().trim(), VendorsRecyclerViewAdapter.this.State.getText().toString().trim(), VendorsRecyclerViewAdapter.this.Company.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Toast.makeText(VendorsRecyclerViewAdapter.this.context, ((CommonResponse) response.body()).getMessage(), 1).show();
                                    VendorsRecyclerViewAdapter.this.RefreshData();
                                    VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                                }

                                public void onFailure(Call<CommonResponse> call, Throwable th) {
                                    Toast.makeText(VendorsRecyclerViewAdapter.this.context, "Something Went Wrong.....", 1).show();
                                    VendorsRecyclerViewAdapter.this.progressDialog.dismiss();
                                }
                            });
                        }
                    }
                }).setCancelable(false).show();
            }
        });
        final String id = ((VendorsData) this.vendorsDataArrayList.get(i)).getID();
        vendorsItemsViewHolder.VendorUpdateImage_TV.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VendorsRecyclerViewAdapter.this.onItemClick.getPosition(id);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public int getItemCount() {
        return this.vendorsDataArrayList.size();
    }

    /* access modifiers changed from: private */
    public void RefreshData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getVendorsDetails().enqueue(new Callback<VendorsResponse>() {
            public void onFailure(Call<VendorsResponse> call, Throwable th) {
            }

            public void onResponse(Call<VendorsResponse> call, Response<VendorsResponse> response) {
                VendorsRecyclerViewAdapter.this.vendorsDataArrayList = ((VendorsResponse) response.body()).getVendorsData();
                VendorsRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public void setOnItemClick(OnItemClick onItemClick2) {
        this.onItemClick = onItemClick2;
    }
}
