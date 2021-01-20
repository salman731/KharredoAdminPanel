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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.ProductCategoryRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.CategoriesData;
import com.muqit.KharredoAdminPanel.Models.CategoriesResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.ProductsData;
import com.muqit.KharredoAdminPanel.Models.UsersData;
import com.muqit.KharredoAdminPanel.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductCategoryFragment extends Fragment implements OnItemSelectedListener {
    /* access modifiers changed from: private */
    public ImageView Category_Photo;
    private MaterialButton ChooseBTN;
    private TextInputEditText Commission;
    /* access modifiers changed from: private */
    public TextInputEditText Name;
    List<String> ProductList;
    /* access modifiers changed from: private */
    public Spinner ProductSpinner;
    /* access modifiers changed from: private */
    public RecyclerView ProductsCategoryRecyclerView;
    /* access modifiers changed from: private */
    public Bitmap bitmap;
    ArrayList<CategoriesData> categoriesData;
    CategoriesData categoriesDataforID;
    /* access modifiers changed from: private */
    public ProductCategoryRecyclerViewAdapter productCategoryRecyclerViewAdapter;
    private ArrayList<ProductsData> productsData;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_product_category, viewGroup, false);
        this.ProductsCategoryRecyclerView = (RecyclerView) inflate.findViewById(R.id.Product_category_recyclerview);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        setHasOptionsMenu(true);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PoulateData();
        return inflate;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.category_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                ProductCategoryFragment.this.productCategoryRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 21 && i2 == -1 && intent != null) {
            try {
                Bitmap bitmap2 = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                this.bitmap = bitmap2;
                this.Category_Photo.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    boolean sortID = true;
    boolean sortCategoryName = true;
    boolean sortParentCategoryName = true;
    boolean sortCommission = true;

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.category_new_menu_btn:
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.category_form_layout, null);
                this.ProductSpinner = (Spinner) inflate.findViewById(R.id.Category_Parent_Products_Spinner);
                this.Name = (TextInputEditText) inflate.findViewById(R.id.Category_Name);
                this.Commission = (TextInputEditText) inflate.findViewById(R.id.Category_Commsion);
                this.Category_Photo = (ImageView) inflate.findViewById(R.id.Category_Photo);
                MaterialButton materialButton = (MaterialButton) inflate.findViewById(R.id.Choose_Category_img_btn);
                this.ChooseBTN = materialButton;
                materialButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        ProductCategoryFragment.this.startActivityForResult(intent, 21);
                    }
                });
                this.ProductList = new ArrayList();
                this.categoriesData = new ArrayList();
                this.ProductSpinner.setOnItemSelectedListener(this);
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getCategoriesDetails().enqueue(new Callback<CategoriesResponse>() {
                    public void onFailure(Call<CategoriesResponse> call, Throwable th) {
                    }

                    public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                        Iterator it = ((CategoriesResponse) response.body()).getCategoriesData().iterator();
                        while (it.hasNext()) {
                            CategoriesData categoriesData = (CategoriesData) it.next();
                            ProductCategoryFragment.this.ProductList.add(categoriesData.getName());
                            ProductCategoryFragment.this.categoriesData.add(categoriesData);
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(ProductCategoryFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, ProductCategoryFragment.this.ProductList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ProductCategoryFragment.this.ProductSpinner.setAdapter(arrayAdapter);
                    }
                });
                String str = "Save";
                new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(ProductCategoryFragment.this.Name.getText().toString().trim()) || ProductCategoryFragment.this.Category_Photo.getDrawable() == null) {
                            Toast.makeText(ProductCategoryFragment.this.getActivity(), "Please Enter Name and Select Image.....", 1).show();
                            return;
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ProductCategoryFragment.this.bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
                        String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("data:image/jpeg;base64,");
                        sb.append(encodeToString);
                        String sb2 = sb.toString();
                        ProductCategoryFragment.this.progressDialog = new ProgressDialog(ProductCategoryFragment.this.getContext());
                        ProductCategoryFragment.this.progressDialog.setMessage("Saving....");
                        ProductCategoryFragment.this.progressDialog.setProgressStyle(0);
                        ProductCategoryFragment.this.progressDialog.setCancelable(false);
                        ProductCategoryFragment.this.progressDialog.show();
                        for (CategoriesData categoriesData : ProductCategoryFragment.this.categoriesData) {
                            if (categoriesData.getName().equals(ProductCategoryFragment.this.ProductSpinner.getSelectedItem().toString())) {
                                ProductCategoryFragment.this.categoriesDataforID = categoriesData;
                            }
                        }
                        FragmentActivity activity = ProductCategoryFragment.this.getActivity();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(ProductCategoryFragment.this.ProductSpinner.getSelectedItem().toString());
                        sb3.append(" ID");
                        sb3.append(ProductCategoryFragment.this.categoriesDataforID.getID());
                        Toast.makeText(activity, sb3.toString(), Toast.LENGTH_LONG).show();
                        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).AddCategory(ProductCategoryFragment.this.Name.getText().toString().trim(), ProductCategoryFragment.this.categoriesDataforID.getID(), sb2).enqueue(new Callback<CommonResponse>() {
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                Toast.makeText(ProductCategoryFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), 1).show();
                                ProductCategoryFragment.this.PoulateData();
                                ProductCategoryFragment.this.progressDialog.dismiss();
                            }

                            public void onFailure(Call<CommonResponse> call, Throwable th) {
                                Toast.makeText(ProductCategoryFragment.this.getActivity(), th.getLocalizedMessage(), 1).show();
                                ProductCategoryFragment.this.progressDialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false).show();
                return true;
            case R.id.ProductsCategory_byID:
                if(sortID)
                {
                    sortID = false;
                }
                else
                {
                    sortID = true;
                }
                Collections.sort(categoriesData, new Comparator<CategoriesData>() {
                    @Override
                    public int compare(CategoriesData lhs, CategoriesData rhs) {
                        return sortID ? (Integer.valueOf(lhs.getID()) < Integer.valueOf(rhs.getID()))? -1 : (Integer.valueOf(lhs.getID()) > Integer.valueOf(rhs.getID())) ? 1 : 0 : (Integer.valueOf(lhs.getID()) > Integer.valueOf(rhs.getID()))? -1 : (Integer.valueOf(lhs.getID()) < Integer.valueOf(rhs.getID())) ? 1 : 0;
                    }

                });
                productCategoryRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ProductsCategory_byName:
                if(sortCategoryName)
                {
                    sortCategoryName = false;
                }
                else
                {
                    sortCategoryName = true;
                }
                Collections.sort(categoriesData, new Comparator<CategoriesData>() {
                    @Override
                    public int compare(CategoriesData lhs, CategoriesData rhs) {
                        return sortCategoryName ? lhs.getName().compareTo(rhs.getName()) : rhs.getName().compareTo(lhs.getName());
                    }
                });
                productCategoryRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ProductsCategory_byCommission:
                if(sortCommission)
                {
                    sortCommission = false;
                }
                else
                {
                    sortCommission = true;
                }
                Collections.sort(categoriesData, new Comparator<CategoriesData>() {
                    @Override
                    public int compare(CategoriesData lhs, CategoriesData rhs) {
                        return sortCommission ? (Integer.valueOf(lhs.getCommission()) < Integer.valueOf(rhs.getCommission()))? -1 : (Integer.valueOf(lhs.getCommission()) > Integer.valueOf(rhs.getCommission())) ? 1 : 0 : (Integer.valueOf(lhs.getCommission()) > Integer.valueOf(rhs.getCommission()))? -1 : (Integer.valueOf(lhs.getCommission()) < Integer.valueOf(rhs.getCommission())) ? 1 : 0;
                    }
                });
                productCategoryRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.ProductsCategory_byPName:
                if(sortParentCategoryName)
                {
                    sortParentCategoryName = false;
                }
                else
                {
                    sortParentCategoryName = true;
                }
                getParentNames();
                Collections.sort(categoriesData, new Comparator<CategoriesData>() {
                    @Override
                    public int compare(CategoriesData lhs, CategoriesData rhs) {
                        return sortParentCategoryName ? lhs.getPName().compareTo(rhs.getPName()) : rhs.getName().compareTo(lhs.getName());
                    }
                });
                productCategoryRecyclerViewAdapter.notifyDataSetChanged();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /* access modifiers changed from: private */
    public void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getCategoriesDetails().enqueue(new Callback<CategoriesResponse>() {
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                categoriesData = response.body().getCategoriesData();
                ProductCategoryFragment.this.productCategoryRecyclerViewAdapter = new ProductCategoryRecyclerViewAdapter(categoriesData, ProductCategoryFragment.this.getActivity());
                ProductCategoryFragment.this.ProductsCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(ProductCategoryFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ProductCategoryFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ProductCategoryFragment.this.getContext(), R.drawable.divider));
                ProductCategoryFragment.this.ProductsCategoryRecyclerView.addItemDecoration(dividerItemDecoration);
                ProductCategoryFragment.this.ProductsCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ProductCategoryFragment.this.ProductsCategoryRecyclerView.setAdapter(ProductCategoryFragment.this.productCategoryRecyclerViewAdapter);
                ProductCategoryFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<CategoriesResponse> call, Throwable th) {
                Toast.makeText(ProductCategoryFragment.this.getActivity(), th.getLocalizedMessage(), 1).show();
                ProductCategoryFragment.this.progressDialog.dismiss();
            }
        });


    }

    CategoriesData parentcategory = null;
    public  void getParentNames()
    {
        ListIterator it1 = categoriesData.listIterator();

        while (it1.hasNext())
        {

            CategoriesData categoriesdata = (CategoriesData) it1.next();
            String str = "0";
            if (categoriesdata.getCategory_ID().trim().equals(str)) {
               categoriesData.get(it1.nextIndex() - 1).setPName("");
            } else {
                ListIterator it = categoriesData.listIterator();

                while (it.hasNext()) {
                    CategoriesData categoriesData2 = (CategoriesData) it.next();
                    if (categoriesData2.getID().trim().equals(categoriesdata.getCategory_ID().trim()) && categoriesdata.getCategory_ID().trim() != str) {
                        this.parentcategory = categoriesData2;
                    }
                }
                if (this.parentcategory != null) {
                    categoriesData.get(it1.nextIndex() - 1).setPName(parentcategory.getName());
                }
            }
        }

    }
}
