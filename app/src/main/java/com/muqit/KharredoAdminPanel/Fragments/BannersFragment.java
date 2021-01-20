package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.BannersRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.BannersResponse;
import com.muqit.KharredoAdminPanel.Models.CategoriesData;
import com.muqit.KharredoAdminPanel.Models.CategoriesResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.OrdersData;
import com.muqit.KharredoAdminPanel.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BannersFragment extends Fragment {
    /* access modifiers changed from: private */
    public RecyclerView BannersRecyclerView;
    /* access modifiers changed from: private */
    public BannersRecyclerViewAdapter bannersRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;
    private Bitmap bitmap;
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_banners, viewGroup, false);
        this.BannersRecyclerView = (RecyclerView) inflate.findViewById(R.id.Banners_recyclerView);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        setHasOptionsMenu(true);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PoulateData();
        return inflate;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.banner_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                BannersFragment.this.bannersRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    TextInputEditText heading,description;
    ImageView bannerPhoto;
    MaterialButton ChooseBannerPhotoImg;
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.banner_new_menu_btn:
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.new_banner_layout, null);
                 heading = (TextInputEditText) inflate.findViewById(R.id.New_Banner_heading);
                 description = (TextInputEditText) inflate.findViewById(R.id.New_Banner_description);
                 bannerPhoto = (ImageView) inflate.findViewById(R.id.New_Banner_photo);
                 ChooseBannerPhotoImg = (MaterialButton) inflate.findViewById(R.id.Choose_new_banner_img_btn);
                ChooseBannerPhotoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        BannersFragment.this.startActivityForResult(intent, 21);
                    }
                });
                String str = "Save";
                new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!TextUtils.isEmpty(BannersFragment.this.heading.getText().toString().trim()) && BannersFragment.this.bannerPhoto.getDrawable() != null && !TextUtils.isEmpty(BannersFragment.this.description.getText().toString().trim()) ) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            BannersFragment.this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                            StringBuilder sb = new StringBuilder();
                            sb.append("data:image/jpeg;base64,");
                            sb.append(encodeToString);
                            String sb2 = sb.toString();
                            BannersFragment.this.progressDialog = new ProgressDialog(BannersFragment.this.getContext());
                            BannersFragment.this.progressDialog.setMessage("Saving....");
                            BannersFragment.this.progressDialog.setProgressStyle(0);
                            BannersFragment.this.progressDialog.setCancelable(false);
                            BannersFragment.this.progressDialog.show();


                            ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).InsertBanner(sb2, heading.getText().toString().trim(), description.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Toast.makeText(BannersFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), 1).show();
                                    BannersFragment.this.progressDialog.dismiss();
                                }

                                public void onFailure(Call<CommonResponse> call, Throwable th) {
                                    Toast.makeText(BannersFragment.this.getActivity(), th.getLocalizedMessage(), 1).show();
                                    BannersFragment.this.progressDialog.dismiss();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Enter Full Details......",Toast.LENGTH_LONG).show();
                        }
                    }

                }).setCancelable(false).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 21 && i2 == -1 && intent != null) {
            try {
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                this.bitmap = bitmap2;
                this.bannerPhoto.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getBannersDetails().enqueue(new Callback<BannersResponse>() {
            public void onResponse(Call<BannersResponse> call, Response<BannersResponse> response) {
                BannersFragment.this.bannersRecyclerViewAdapter = new BannersRecyclerViewAdapter(((BannersResponse) response.body()).getBannersData(), BannersFragment.this.getActivity());
                BannersFragment.this.BannersRecyclerView.setLayoutManager(new LinearLayoutManager(BannersFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(BannersFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(BannersFragment.this.getContext(), R.drawable.divider));
                BannersFragment.this.BannersRecyclerView.addItemDecoration(dividerItemDecoration);
                BannersFragment.this.BannersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                BannersFragment.this.BannersRecyclerView.setAdapter(BannersFragment.this.bannersRecyclerViewAdapter);
                BannersFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<BannersResponse> call, Throwable th) {
                Toast.makeText(BannersFragment.this.getActivity(), "Check Your Internet Connection and Try Again......", Toast.LENGTH_LONG).show();
                BannersFragment.this.progressDialog.dismiss();
            }
        });
    }
}
