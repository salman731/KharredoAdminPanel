package com.muqit.KharredoAdminPanel.Adapters;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.CategoriesData;
import com.muqit.KharredoAdminPanel.Models.CategoriesResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.ProductsData;
import com.muqit.KharredoAdminPanel.Models.VendorsData;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;
import jp.wasabeef.richeditor.RichEditor.OnTextChangeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsRecyclerViewAdapter extends Adapter<ProductsRecyclerViewAdapter.ProductsItemsViewHolder> implements Filterable {
    private Filter ProductsFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(ProductsRecyclerViewAdapter.this.productsDataArrayListFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();

                for (ProductsData productsData : ProductsRecyclerViewAdapter.this.productsDataArrayListFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(productsData.getName()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(productsData.getPrice()).toLowerCase().contains(trim) || SalesRecyclerViewAdapter.blankIfNull(productsData.getQuantity()).toLowerCase().contains(trim)) {
                        arrayList.add(productsData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ProductsRecyclerViewAdapter.this.productsDataArrayList.clear();
            ProductsRecyclerViewAdapter.this.productsDataArrayList.addAll((List) filterResults.values);
            ProductsRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };
    /* access modifiers changed from: private */
    public Spinner CategorySpinner;
    private TextInputEditText Dimension;
    /* access modifiers changed from: private */
    public TextInputEditText EndDate;
    private ImageView FrontImage;
    /* access modifiers changed from: private */
    public String HTMLCODE = "";
    public String IMAGE_BASE_URL = "https://kharredo.com/images/";
    private ImageView MultipleImages;
    /* access modifiers changed from: private */
    public TextInputEditText Name;
    private MaterialButton PhotoSelectionBTN;
    private TextView Preview;
    /* access modifiers changed from: private */
    public TextInputEditText Price;
    List<String> ProductList;
    private TextInputEditText Quantity;
    /* access modifiers changed from: private */
    public TextInputEditText Sale;
    private TextInputEditText ShippingPrice;
    /* access modifiers changed from: private */
    public TextInputEditText StartDate;
    private TextInputEditText Weight;
    List<CategoriesData> categoriesDataList;
    CategoriesData categoriesDataforID;
    /* access modifiers changed from: private */
    public Context contxt;
    /* access modifiers changed from: private */
    public RichEditor mEditor;
    /* access modifiers changed from: private */
    public MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private ArrayList<ProductsData> productsDataArrayList;
    private List<ProductsData> productsDataArrayListFullList;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    @Override
    public Filter getFilter() {
        return ProductsFilter;
    }

    public class ProductsItemsViewHolder extends ViewHolder {
        public MaterialTextView KharredoCut;
        public MaterialTextView Name;
        public MaterialTextView Price;
        public ImageView Product_IV;
        public MaterialButton Products_Delete_BTN;
        public MaterialButton Products_Edit_BTN;
        public MaterialButton Products_View_Details_BTN;
        public MaterialTextView Quantity;
        public MaterialTextView SellersCut;
        public MaterialTextView Status;
        public MaterialTextView ViewsToday;

        public ProductsItemsViewHolder(View view) {
            super(view);
            this.Name = (MaterialTextView) view.findViewById(R.id.Products_name);
            this.Price = (MaterialTextView) view.findViewById(R.id.Products_price);
            this.Quantity = (MaterialTextView) view.findViewById(R.id.Products_quantity);
            this.ViewsToday = (MaterialTextView) view.findViewById(R.id.Products_views_today);
            this.KharredoCut = (MaterialTextView) view.findViewById(R.id.Products_kharredo_cut);
            this.SellersCut = (MaterialTextView) view.findViewById(R.id.Products_sellers_cut);
            this.Status = (MaterialTextView) view.findViewById(R.id.Products_status);
            this.Products_View_Details_BTN = (MaterialButton) view.findViewById(R.id.Products_view_details_btn);
            this.Products_Edit_BTN = (MaterialButton) view.findViewById(R.id.Products_edit_btn);
            this.Products_Delete_BTN = (MaterialButton) view.findViewById(R.id.Products_delete_btn);
            this.Product_IV = (ImageView) view.findViewById(R.id.Product_image);
        }
    }

    public ProductsRecyclerViewAdapter(ArrayList<ProductsData> arrayList, Context context) {
        this.productsDataArrayList = arrayList;
        this.contxt = context;
        this.productsDataArrayListFullList = new ArrayList(arrayList);
    }

    public ProductsItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProductsItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(ProductsItemsViewHolder productsItemsViewHolder, int i) {
        final ProductsData productsData = (ProductsData) this.productsDataArrayList.get(i);
        productsItemsViewHolder.Name.setText(productsData.getName());
        productsItemsViewHolder.Price.setText(productsData.getPrice());
        productsItemsViewHolder.Quantity.setText(productsData.getQuantity());
        productsItemsViewHolder.KharredoCut.setText(productsData.getPrice());
        productsItemsViewHolder.SellersCut.setText(productsData.getPrice());
        productsItemsViewHolder.Status.setBackground(ContextCompat.getDrawable(this.contxt, R.drawable.active_status));
        productsItemsViewHolder.Status.setText("Active");
        productsItemsViewHolder.ViewsToday.setText("0");
        Picasso picasso = Picasso.get();
        StringBuilder sb = new StringBuilder();
        sb.append(this.IMAGE_BASE_URL);
        sb.append(productsData.getPhotoURL());
        picasso.load(sb.toString()).into(productsItemsViewHolder.Product_IV);
        if (productsItemsViewHolder.Product_IV.getDrawable() == null) {
            productsItemsViewHolder.Product_IV.setImageResource(R.drawable.noimage);
        }
        productsItemsViewHolder.Products_View_Details_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = LayoutInflater.from(ProductsRecyclerViewAdapter.this.contxt).inflate(R.layout.product_details_dialog_layout, null);
                ((WebView) inflate.findViewById(R.id.webview)).loadDataWithBaseURL("", productsData.getDescription(), "text/html", "UTF-8", "");
                ProductsRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductsRecyclerViewAdapter.this.contxt).setView(inflate).setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ProductsRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
            }
        });
        productsItemsViewHolder.Products_Edit_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.CreatProductForm(productsData);
            }
        });
        productsItemsViewHolder.Products_Delete_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(ProductsRecyclerViewAdapter.this.contxt);
                ProductsRecyclerViewAdapter.this.progressDialog.setMessage("Blocking....");
                ProductsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                ProductsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                ProductsRecyclerViewAdapter.this.progressDialog.show();
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).BlockProduct(productsData.getID(), 0).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        ProductsRecyclerViewAdapter.this.progressDialog.dismiss();
                        Toast.makeText(ProductsRecyclerViewAdapter.this.contxt, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(ProductsRecyclerViewAdapter.this.contxt, "Something Went Wrong.....", Toast.LENGTH_LONG).show();
                        ProductsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        });
    }

    public int getItemCount() {
        return this.productsDataArrayList.size();
    }

    /* access modifiers changed from: private */
    public void CreatProductForm(final ProductsData productsData) {
        View inflate = LayoutInflater.from(this.contxt).inflate(R.layout.product_form_layout, null);
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
        this.Weight.setVisibility(View.GONE);
        this.Dimension.setVisibility(View.GONE);
        this.Quantity.setVisibility(View.GONE);
        this.ShippingPrice.setVisibility(View.GONE);
        this.FrontImage.setVisibility(View.GONE);
        this.PhotoSelectionBTN.setVisibility(View.GONE);
        ShowDates();
        this.mEditor.setEditorHeight(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.mEditor.setEditorFontSize(22);
        this.mEditor.setEditorFontColor(SupportMenu.CATEGORY_MASK);
        this.mEditor.setPadding(10, 10, 10, 10);
        this.mEditor.setPlaceholder("Description");
        this.categoriesDataList = new ArrayList();
        this.ProductList = new ArrayList();
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).getCategoriesDetails().enqueue(new Callback<CategoriesResponse>() {
            public void onFailure(Call<CategoriesResponse> call, Throwable th) {
            }

            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Iterator it = ((CategoriesResponse) response.body()).getCategoriesData().iterator();
                while (it.hasNext()) {
                    CategoriesData categoriesData = (CategoriesData) it.next();
                    ProductsRecyclerViewAdapter.this.ProductList.add(categoriesData.getName());
                    ProductsRecyclerViewAdapter.this.categoriesDataList.add(categoriesData);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(ProductsRecyclerViewAdapter.this.contxt, android.R.layout.simple_spinner_dropdown_item, ProductsRecyclerViewAdapter.this.ProductList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ProductsRecyclerViewAdapter.this.CategorySpinner.setAdapter(arrayAdapter);
            }
        });
        new MaterialAlertDialogBuilder(this.contxt).setView(inflate).setNeutralButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton((CharSequence) "Save", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ProductsRecyclerViewAdapter.this.progressDialog = new ProgressDialog(ProductsRecyclerViewAdapter.this.contxt);
                ProductsRecyclerViewAdapter.this.progressDialog.setMessage("Loading....");
                ProductsRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                ProductsRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                ProductsRecyclerViewAdapter.this.progressDialog.show();
                for (CategoriesData categoriesData : ProductsRecyclerViewAdapter.this.categoriesDataList) {
                    if (categoriesData.getName().equals(ProductsRecyclerViewAdapter.this.CategorySpinner.getSelectedItem().toString())) {
                        ProductsRecyclerViewAdapter.this.categoriesDataforID = categoriesData;
                    }
                }
                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).EditProduct(productsData.getID(), ProductsRecyclerViewAdapter.this.Name.getText().toString().trim(), ProductsRecyclerViewAdapter.this.categoriesDataforID.getID().trim(), ProductsRecyclerViewAdapter.this.Price.getText().toString().trim(), ProductsRecyclerViewAdapter.this.HTMLCODE, ProductsRecyclerViewAdapter.this.StartDate.getText().toString().trim(), ProductsRecyclerViewAdapter.this.EndDate.getText().toString().trim(), ProductsRecyclerViewAdapter.this.Sale.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(ProductsRecyclerViewAdapter.this.contxt, ((CommonResponse) response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        ProductsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }

                    public void onFailure(Call<CommonResponse> call, Throwable th) {
                        Toast.makeText(ProductsRecyclerViewAdapter.this.contxt, th.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        ProductsRecyclerViewAdapter.this.progressDialog.dismiss();
                    }
                });
            }
        }).setCancelable(false).show();
        this.mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            public void onTextChange(String str) {
                ProductsRecyclerViewAdapter.this.HTMLCODE = str;
            }
        });
        inflate.findViewById(R.id.action_undo).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.undo();
            }
        });
        inflate.findViewById(R.id.action_redo).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.redo();
            }
        });
        inflate.findViewById(R.id.action_bold).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setBold();
            }
        });
        inflate.findViewById(R.id.action_italic).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setItalic();
            }
        });
        inflate.findViewById(R.id.action_subscript).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setSubscript();
            }
        });
        inflate.findViewById(R.id.action_superscript).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setSuperscript();
            }
        });
        inflate.findViewById(R.id.action_strikethrough).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setStrikeThrough();
            }
        });
        inflate.findViewById(R.id.action_underline).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setUnderline();
            }
        });
        inflate.findViewById(R.id.action_heading1).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setHeading(1);
            }
        });
        inflate.findViewById(R.id.action_heading2).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setHeading(2);
            }
        });
        inflate.findViewById(R.id.action_heading3).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setHeading(3);
            }
        });
        inflate.findViewById(R.id.action_heading4).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setHeading(4);
            }
        });
        inflate.findViewById(R.id.action_heading5).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setHeading(5);
            }
        });
        inflate.findViewById(R.id.action_heading6).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setHeading(6);
            }
        });
        inflate.findViewById(R.id.action_txt_color).setOnClickListener(new OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setTextColor(this.isChanged ? ViewCompat.MEASURED_STATE_MASK : SupportMenu.CATEGORY_MASK);
                this.isChanged = !this.isChanged;
            }
        });
        inflate.findViewById(R.id.action_bg_color).setOnClickListener(new OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setTextBackgroundColor(this.isChanged ? 0 : InputDeviceCompat.SOURCE_ANY);
                this.isChanged = !this.isChanged;
            }
        });
        inflate.findViewById(R.id.action_indent).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setIndent();
            }
        });
        inflate.findViewById(R.id.action_outdent).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setOutdent();
            }
        });
        inflate.findViewById(R.id.action_align_left).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setAlignLeft();
            }
        });
        inflate.findViewById(R.id.action_align_center).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setAlignCenter();
            }
        });
        inflate.findViewById(R.id.action_align_right).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setAlignRight();
            }
        });
        inflate.findViewById(R.id.action_blockquote).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setBlockquote();
            }
        });
        inflate.findViewById(R.id.action_insert_bullets).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setBullets();
            }
        });
        inflate.findViewById(R.id.action_insert_numbers).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.setNumbers();
            }
        });
        inflate.findViewById(R.id.action_insert_image).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG", "dachshund");
            }
        });
        inflate.findViewById(R.id.action_insert_link).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        inflate.findViewById(R.id.action_insert_checkbox).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsRecyclerViewAdapter.this.mEditor.insertTodo();
            }
        });
    }

    private void ShowDates() {
        this.StartDate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Calendar instance = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductsRecyclerViewAdapter.this.contxt, new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        instance.set(1, i);
                        instance.set(2, i2);
                        instance.set(5, i3);
                        ProductsRecyclerViewAdapter.this.StartDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(instance.getTime()));
                    }
                }, instance.get(1), instance.get(2), instance.get(5));
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });
        this.EndDate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Calendar instance = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductsRecyclerViewAdapter.this.contxt, new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        instance.set(1, i);
                        instance.set(2, i2);
                        instance.set(5, i3);
                        ProductsRecyclerViewAdapter.this.EndDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(instance.getTime()));
                    }
                }, instance.get(1), instance.get(2), instance.get(5));
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });
    }
}
