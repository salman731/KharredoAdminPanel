package com.muqit.KharredoAdminPanel.Fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Adapters.ProductsRecyclerViewAdapter;
import com.muqit.KharredoAdminPanel.Models.CategoriesData;
import com.muqit.KharredoAdminPanel.Models.CategoriesResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.ProductsData;
import com.muqit.KharredoAdminPanel.Models.ProductsResponse;
import com.muqit.KharredoAdminPanel.Models.VendorsData;
import com.muqit.KharredoAdminPanel.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;
import jp.wasabeef.richeditor.RichEditor.OnTextChangeListener;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductListFragment extends Fragment {
    /* access modifiers changed from: private */
    public Spinner CategorySpinner;
    /* access modifiers changed from: private */
    public TextInputEditText Dimension;
    /* access modifiers changed from: private */
    public TextInputEditText EndDate;
    private ImageView FrontImage;
    /* access modifiers changed from: private */
    public String HTMLCODE = "";
    private ImageView MultipleImages;
    /* access modifiers changed from: private */
    public TextInputEditText Name;
    private MaterialButton PhotoSelectionBTN;
    private TextView Preview;
    /* access modifiers changed from: private */
    public TextInputEditText Price;
    List<String> ProductList;
    /* access modifiers changed from: private */
    public RecyclerView ProductsRecyclerView;
    /* access modifiers changed from: private */
    public TextInputEditText Quantity;
    /* access modifiers changed from: private */
    public TextInputEditText Sale;
    /* access modifiers changed from: private */
    public TextInputEditText ShippingPrice;
    /* access modifiers changed from: private */
    public TextInputEditText StartDate;
    /* access modifiers changed from: private */
    public TextInputEditText Weight;
    /* access modifiers changed from: private */
    public Bitmap bitmap;
    List<CategoriesData> categoriesDataList;
    CategoriesData categoriesDataforID;
    /* access modifiers changed from: private */
    public RichEditor mEditor;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private ArrayList<ProductsData> productsData;
    /* access modifiers changed from: private */
    public ProductsRecyclerViewAdapter productsRecyclerViewAdapter;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Retrofit retrofit;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_product_list, viewGroup, false);
        this.ProductsRecyclerView = (RecyclerView) inflate.findViewById(R.id.Products_recyclerview);
        setHasOptionsMenu(true);
        this.retrofit = new RetrofitClient().getRetrofitClient();
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        PoulateData();
        return inflate;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 21 && i2 == -1 && intent != null) {
            try {
                Bitmap bitmap2 = Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                this.bitmap = bitmap2;
                this.FrontImage.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.product_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.expandActionView();
        SearchView searchView = (SearchView) findItem.getActionView();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                ProductListFragment.this.productsRecyclerViewAdapter.getFilter().filter(str);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    boolean sortEmail = true;
    boolean sortName = true;
    boolean sortPrice = true;
    boolean sortQuantity = true;
    boolean sortView = true;
    boolean sortKcut = true;
    boolean sortScut = true;

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.product_new_menu_btn:
                CreatProductForm();
                return true;
            case R.id.Products_byName:
                if(sortName)
                {
                    sortName = false;
                }
                else
                {
                    sortName = true;
                }
                Collections.sort(productsData, new Comparator<ProductsData>() {
                    @Override
                    public int compare(ProductsData lhs, ProductsData rhs) {
                        return sortEmail ? lhs.getName().compareTo(rhs.getName()) : rhs.getName().compareTo(lhs.getName());
                    }

                });
                productsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Products_byPrice:
                if(sortPrice)
                {
                    sortPrice = false;
                }
                else
                {
                    sortPrice = true;
                }
                Collections.sort(productsData, new Comparator<ProductsData>() {
                    @Override
                    public int compare(ProductsData lhs, ProductsData rhs) {
                        return sortPrice ? (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice())) ? 1 : 0 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice())) ? 1 : 0;
                    }
                });
                productsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Products_byQuantity:
                if(sortQuantity)
                {
                    sortQuantity = false;
                }
                else
                {
                    sortQuantity = true;
                }
                Collections.sort(productsData, new Comparator<ProductsData>() {
                    @Override
                    public int compare(ProductsData lhs, ProductsData rhs) {
                        return sortQuantity ? (Integer.valueOf(lhs.getQuantity()) < Integer.valueOf(rhs.getQuantity()))? -1 : (Integer.valueOf(lhs.getQuantity()) > Integer.valueOf(rhs.getQuantity())) ? 1 : 0 : (Integer.valueOf(lhs.getQuantity()) > Integer.valueOf(rhs.getQuantity()))? -1 : (Integer.valueOf(lhs.getQuantity()) < Integer.valueOf(rhs.getQuantity())) ? 1 : 0;
                    }
                });
                productsRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.Products_byKharredoCut:
                if(sortKcut)
                {
                    sortKcut = false;
                }
                else
                {
                    sortKcut = true;
                }
                Collections.sort(productsData, new Comparator<ProductsData>() {
                    @Override
                    public int compare(ProductsData lhs, ProductsData rhs) {

                        return sortKcut ? (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice())) ? 1 : 0 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice())) ? 1 : 0;
                    }
                });
                productsRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.Products_bySellersCut:
                if(sortScut)
                {
                    sortScut = false;
                }
                else
                {
                    sortScut = true;
                }
                Collections.sort(productsData, new Comparator<ProductsData>() {
                    @Override
                    public int compare(ProductsData lhs, ProductsData rhs) {

                        return sortScut ? (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice())) ? 1 : 0 : (Integer.valueOf(lhs.getPrice()) > Integer.valueOf(rhs.getPrice()))? -1 : (Integer.valueOf(lhs.getPrice()) < Integer.valueOf(rhs.getPrice())) ? 1 : 0;
                    }
                });
                productsRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreatProductForm() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.product_form_layout, null);
        this.Name = (TextInputEditText) inflate.findViewById(R.id.Products_name);
        this.Price = (TextInputEditText) inflate.findViewById(R.id.Products_price);
        this.Quantity = (TextInputEditText) inflate.findViewById(R.id.Products_quantity);
        this.Sale = (TextInputEditText) inflate.findViewById(R.id.Products_sale);
        this.StartDate = (TextInputEditText) inflate.findViewById(R.id.Products_start_date);
        this.EndDate = (TextInputEditText) inflate.findViewById(R.id.Products_end_date);
        this.Weight = (TextInputEditText) inflate.findViewById(R.id.Products_weight);
        this.Dimension = (TextInputEditText) inflate.findViewById(R.id.Products_dimension);
        this.ShippingPrice = (TextInputEditText) inflate.findViewById(R.id.Products_shipping_price);
        this.CategorySpinner = (Spinner) inflate.findViewById(R.id.Products_category_spinner);
        this.FrontImage = (ImageView) inflate.findViewById(R.id.Product_Photo);
        this.PhotoSelectionBTN = (MaterialButton) inflate.findViewById(R.id.Choose_Product_img_btn);
        this.mEditor = (RichEditor) inflate.findViewById(R.id.editor);
        this.Preview = (TextView) inflate.findViewById(R.id.preview);
        TextView textView = (TextView) inflate.findViewById(R.id.html);
        ShowDates();
        this.mEditor.setEditorHeight(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.mEditor.setEditorFontSize(22);
        this.mEditor.setEditorFontColor(SupportMenu.CATEGORY_MASK);
        this.mEditor.setPadding(10, 10, 10, 10);
        this.mEditor.setPlaceholder("Description");
        this.categoriesDataList = new ArrayList();
        this.ProductList = new ArrayList();
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getCategoriesDetails().enqueue(new retrofit2.Callback<CategoriesResponse>() {
            public void onFailure(Call<CategoriesResponse> call, Throwable th) {
            }

            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Iterator it = ((CategoriesResponse) response.body()).getCategoriesData().iterator();
                while (it.hasNext()) {
                    CategoriesData categoriesData = (CategoriesData) it.next();
                    ProductListFragment.this.ProductList.add(categoriesData.getName());
                    ProductListFragment.this.categoriesDataList.add(categoriesData);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(ProductListFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, ProductListFragment.this.ProductList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ProductListFragment.this.CategorySpinner.setAdapter(arrayAdapter);
            }
        });
        this.PhotoSelectionBTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction("android.intent.action.GET_CONTENT");
                ProductListFragment.this.startActivityForResult(intent, 21);
            }
        });
        String str = "Save";
        new MaterialAlertDialogBuilder(getActivity()).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ProductListFragment.this.progressDialog = new ProgressDialog(ProductListFragment.this.getActivity());
                ProductListFragment.this.progressDialog.setMessage("Loading....");
                ProductListFragment.this.progressDialog.setProgressStyle(0);
                ProductListFragment.this.progressDialog.setCancelable(false);
                ProductListFragment.this.progressDialog.show();
                for (CategoriesData categoriesData : ProductListFragment.this.categoriesDataList) {
                    if (categoriesData.getName().equals(ProductListFragment.this.CategorySpinner.getSelectedItem().toString())) {
                        ProductListFragment.this.categoriesDataforID = categoriesData;
                    }
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ProductListFragment.this.bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                StringBuilder sb = new StringBuilder();
                sb.append("data:image/jpeg;base64,");
                sb.append(encodeToString);
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).AddProduct(ProductListFragment.this.Name.getText().toString().trim(), ProductListFragment.this.categoriesDataforID.getID(), ProductListFragment.this.Price.getText().toString().trim(), ProductListFragment.this.HTMLCODE, sb.toString(), ProductListFragment.this.Quantity.getText().toString().trim(), ProductListFragment.this.Weight.getText().toString().trim(), ProductListFragment.this.Dimension.getText().toString().trim(), ProductListFragment.this.ShippingPrice.getText().toString().trim(), ProductListFragment.this.StartDate.getText().toString().trim(), ProductListFragment.this.EndDate.getText().toString().trim(), ProductListFragment.this.Sale.getText().toString().trim()).enqueue(new retrofit2.Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(ProductListFragment.this.getActivity(), ((CommonResponse) response.body()).getMessage(), 1).show();
                        ProductListFragment.this.progressDialog.dismiss();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(ProductListFragment.this.getActivity(), th.getLocalizedMessage(), 1).show();
                        ProductListFragment.this.progressDialog.dismiss();
                    }
                });
            }
        }).setCancelable(false).show();
        this.mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            public void onTextChange(String str) {
                ProductListFragment.this.HTMLCODE = str;
            }
        });
        inflate.findViewById(R.id.action_undo).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.undo();
            }
        });
        inflate.findViewById(R.id.action_redo).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.redo();
            }
        });
        inflate.findViewById(R.id.action_bold).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setBold();
            }
        });
        inflate.findViewById(R.id.action_italic).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setItalic();
            }
        });
        inflate.findViewById(R.id.action_subscript).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setSubscript();
            }
        });
        inflate.findViewById(R.id.action_superscript).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setSuperscript();
            }
        });
        inflate.findViewById(R.id.action_strikethrough).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setStrikeThrough();
            }
        });
        inflate.findViewById(R.id.action_underline).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setUnderline();
            }
        });
        inflate.findViewById(R.id.action_heading1).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setHeading(1);
            }
        });
        inflate.findViewById(R.id.action_heading2).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setHeading(2);
            }
        });
        inflate.findViewById(R.id.action_heading3).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setHeading(3);
            }
        });
        inflate.findViewById(R.id.action_heading4).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setHeading(4);
            }
        });
        inflate.findViewById(R.id.action_heading5).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setHeading(5);
            }
        });
        inflate.findViewById(R.id.action_heading6).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setHeading(6);
            }
        });
        inflate.findViewById(R.id.action_txt_color).setOnClickListener(new OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                ProductListFragment.this.mEditor.setTextColor(this.isChanged ? ViewCompat.MEASURED_STATE_MASK : SupportMenu.CATEGORY_MASK);
                this.isChanged = !this.isChanged;
            }
        });
        inflate.findViewById(R.id.action_bg_color).setOnClickListener(new OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                ProductListFragment.this.mEditor.setTextBackgroundColor(this.isChanged ? 0 : InputDeviceCompat.SOURCE_ANY);
                this.isChanged = !this.isChanged;
            }
        });
        inflate.findViewById(R.id.action_indent).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setIndent();
            }
        });
        inflate.findViewById(R.id.action_outdent).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setOutdent();
            }
        });
        inflate.findViewById(R.id.action_align_left).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setAlignLeft();
            }
        });
        inflate.findViewById(R.id.action_align_center).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setAlignCenter();
            }
        });
        inflate.findViewById(R.id.action_align_right).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setAlignRight();
            }
        });
        inflate.findViewById(R.id.action_blockquote).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setBlockquote();
            }
        });
        inflate.findViewById(R.id.action_insert_bullets).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setBullets();
            }
        });
        inflate.findViewById(R.id.action_insert_numbers).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.setNumbers();
            }
        });
        inflate.findViewById(R.id.action_insert_image).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG", "dachshund");
            }
        });
        inflate.findViewById(R.id.action_insert_link).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        inflate.findViewById(R.id.action_insert_checkbox).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.mEditor.insertTodo();
            }
        });
    }

    private void ShowDates() {
        this.StartDate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Calendar instance = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductListFragment.this.getActivity(), new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        instance.set(1, i);
                        instance.set(2, i2);
                        instance.set(5, i3);
                        ProductListFragment.this.StartDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(instance.getTime()));
                    }
                }, instance.get(1), instance.get(2), instance.get(5));
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });
        this.EndDate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Calendar instance = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductListFragment.this.getActivity(), new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        instance.set(1, i);
                        instance.set(2, i2);
                        instance.set(5, i3);
                        ProductListFragment.this.EndDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(instance.getTime()));
                    }
                }, instance.get(1), instance.get(2), instance.get(5));
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });
    }

    private void PoulateData() {
        this.progressDialog.show();
        ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).getProductsDetails().enqueue(new retrofit2.Callback<ProductsResponse>() {
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                productsData = response.body().getProductsData();
                ProductListFragment.this.productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(((ProductsResponse) response.body()).getProductsData(), ProductListFragment.this.getActivity());
                ProductListFragment.this.ProductsRecyclerView.setLayoutManager(new LinearLayoutManager(ProductListFragment.this.getActivity()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ProductListFragment.this.getContext(), 1);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ProductListFragment.this.getContext(), R.drawable.divider));
                ProductListFragment.this.ProductsRecyclerView.addItemDecoration(dividerItemDecoration);
                ProductListFragment.this.ProductsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ProductListFragment.this.ProductsRecyclerView.setAdapter(ProductListFragment.this.productsRecyclerViewAdapter);
                ProductListFragment.this.progressDialog.dismiss();
            }

            public void onFailure(Call<ProductsResponse> call, Throwable th) {
                Toast.makeText(ProductListFragment.this.getActivity(), th.getLocalizedMessage(), 1).show();
                ProductListFragment.this.progressDialog.dismiss();
            }
        });
    }
}
