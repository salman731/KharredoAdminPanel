package com.muqit.KharredoAdminPanel.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Activity.EditBlogActivity;
import com.muqit.KharredoAdminPanel.Fragments.BannersFragment;
import com.muqit.KharredoAdminPanel.Fragments.BlogInsertionFragment;
import com.muqit.KharredoAdminPanel.Models.BlogResponse;
import com.muqit.KharredoAdminPanel.Models.BlogResponse.BlogData;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.VendorsResponse;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogRecyclerViewAdapter extends Adapter<BlogRecyclerViewAdapter.OrderedItemsViewHolder> {
    public String IMAGE_BASE_URL = "https://kharredo.com";
    private ArrayList<BlogData> blogDataArrayList;
    private Context context;



    public BlogRecyclerViewAdapter(ArrayList<BlogData> arrayList, Context context2) {
        this.blogDataArrayList = arrayList;
        this.context = context2;
    }

    public OrderedItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OrderedItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_layout, viewGroup, false));
    }

    private RichEditor mEditor;
    private TextView Preview;
    private ImageView MainIMG, BannerIMG;
    private MaterialButton MainIMG_BTN,BannerIMG_BTN,Submit_BTN;
    public TextInputEditText Blog_Title,Blog_Description;
    String HTMLCODE = "";
    public Bitmap Mainbitmap,Bannerbitmap;
    boolean isMainPhoto;
    private ProgressDialog progressDialog;

    public void onBindViewHolder(OrderedItemsViewHolder orderedItemsViewHolder, int i) {
        BlogData blogData = (BlogData) this.blogDataArrayList.get(i);
        orderedItemsViewHolder.title.setText(blogData.getTitle());
        int postion = i;
        Picasso picasso = Picasso.get();
        StringBuilder sb = new StringBuilder();
        sb.append(this.IMAGE_BASE_URL);
        sb.append(blogData.getPhotoURL().substring(2));
        picasso.load(sb.toString()).into(orderedItemsViewHolder.BlogPhoto);
        if (orderedItemsViewHolder.BlogPhoto.getDrawable() == null) {
            orderedItemsViewHolder.BlogPhoto.setImageResource(R.drawable.noimage);
        }
        orderedItemsViewHolder.Edit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditBlogActivity.class);
                Toast.makeText(context,blogDataArrayList.get(postion).getID().toString(),Toast.LENGTH_LONG).show();
                i.putExtra("id",blogDataArrayList.get(postion).getID().toString());
                context.startActivity(i);
            }
        });

        orderedItemsViewHolder.Delete_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogRecyclerViewAdapter.this.progressDialog = new ProgressDialog(context);
                BlogRecyclerViewAdapter.this.progressDialog.setMessage("Deleting....");
                BlogRecyclerViewAdapter.this.progressDialog.setProgressStyle(0);
                BlogRecyclerViewAdapter.this.progressDialog.setCancelable(false);
                BlogRecyclerViewAdapter.this.progressDialog.show();

                ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).DeleteBlog(blogDataArrayList.get(postion).getID().toString()).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Toast.makeText(BlogRecyclerViewAdapter.this.context,"Deleted....",Toast.LENGTH_LONG).show();
                        BlogRecyclerViewAdapter.this.progressDialog.dismiss();
                        RefreshData();
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Toast.makeText(BlogRecyclerViewAdapter.this.context,"Check your internet connection.....",Toast.LENGTH_LONG).show();
                        BlogRecyclerViewAdapter.this.progressDialog.dismiss();

                    }
                });
            }
        });
    }

    public int getItemCount() {
        return this.blogDataArrayList.size();
    }
    public void RefreshData() {
        ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).Blog().enqueue(new Callback<BlogResponse>() {
            @Override
            public void onResponse(Call<BlogResponse> call, Response<BlogResponse> response) {
                blogDataArrayList = response.body().getBlogData();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BlogResponse> call, Throwable t) {

            }
        });
    }
    public class OrderedItemsViewHolder extends ViewHolder {
        public ImageView BlogPhoto;
        public MaterialTextView title;
        public MaterialButton Edit_BTN,Delete_BTN;

        public OrderedItemsViewHolder(View view) {
            super(view);
            this.title = (MaterialTextView) view.findViewById(R.id.blog_title);
            this.BlogPhoto = (ImageView) view.findViewById(R.id.blog_image);
            this.Edit_BTN  = (MaterialButton) view.findViewById(R.id.Blog_edit_btn);
            this.Delete_BTN  = (MaterialButton) view.findViewById(R.id.Blog_delete_btn);
        }
    }
}
