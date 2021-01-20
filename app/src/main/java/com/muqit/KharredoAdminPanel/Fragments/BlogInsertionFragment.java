package com.muqit.KharredoAdminPanel.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.BlogResponse;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

import jp.wasabeef.richeditor.RichEditor;
import jp.wasabeef.richeditor.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogInsertionFragment extends Fragment {
    private RichEditor mEditor;
    private TextView Preview;
    private ImageView MainIMG, BannerIMG;
    private MaterialButton MainIMG_BTN,BannerIMG_BTN,Submit_BTN;
    public TextInputEditText Blog_Title,Blog_Description;
    String HTMLCODE = "";
    public Bitmap Mainbitmap,Bannerbitmap;
    boolean isMainPhoto;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_blog_insertion, viewGroup, false);
        MainIMG = (ImageView) inflate.findViewById(R.id.Main_Photo);
        BannerIMG = (ImageView) inflate.findViewById(R.id.Banner_Photo);
        Blog_Title = (TextInputEditText) inflate.findViewById(R.id.Blog_title);
        Blog_Description = (TextInputEditText) inflate.findViewById(R.id.Blog_description);
        MainIMG_BTN = (MaterialButton) inflate.findViewById(R.id.Choose_main_img_btn);
        BannerIMG_BTN = (MaterialButton) inflate.findViewById(R.id.Choose_banner_img_btn);
        Submit_BTN = (MaterialButton) inflate.findViewById(R.id.Blog_Submit_BTN);
        MainIMG_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMainPhoto = true;
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction("android.intent.action.GET_CONTENT");
                BlogInsertionFragment.this.startActivityForResult(intent, 21);
            }
        });
        BannerIMG_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMainPhoto = false;
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction("android.intent.action.GET_CONTENT");
                BlogInsertionFragment.this.startActivityForResult(intent, 21);
            }
        });
        Submit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogInsertionFragment.this.progressDialog = new ProgressDialog(BlogInsertionFragment.this.getActivity());
                BlogInsertionFragment.this.progressDialog.setMessage("Saving....");
                BlogInsertionFragment.this.progressDialog.setProgressStyle(0);
                BlogInsertionFragment.this.progressDialog.setCancelable(false);
                BlogInsertionFragment.this.progressDialog.show();
                ByteArrayOutputStream main_byteArrayOutputStream = new ByteArrayOutputStream();
                BlogInsertionFragment.this.Mainbitmap.compress(Bitmap.CompressFormat.JPEG, 100, main_byteArrayOutputStream);
                String main_encodeToString = Base64.encodeToString(main_byteArrayOutputStream.toByteArray(), 0);
                StringBuilder Main_sb = new StringBuilder();
                Main_sb.append("data:image/jpeg;base64,");
                Main_sb.append(main_encodeToString);
                ByteArrayOutputStream banner_byteArrayOutputStream = new ByteArrayOutputStream();
                BlogInsertionFragment.this.Bannerbitmap.compress(Bitmap.CompressFormat.JPEG, 100, banner_byteArrayOutputStream);
                String banner_encodeToString = Base64.encodeToString(main_byteArrayOutputStream.toByteArray(), 0);
                StringBuilder Banner_sb = new StringBuilder();
                Banner_sb.append("data:image/jpeg;base64,");
                Banner_sb.append(banner_encodeToString);

                if(MainIMG.getDrawable() != null && BannerIMG.getDrawable() != null && !TextUtils.isEmpty(Blog_Title.getText()) && !TextUtils.isEmpty(Blog_Description.getText()) && !TextUtils.isEmpty(HTMLCODE))
                {
                    ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).InsertBlog(Blog_Title.getText().toString().trim(),Main_sb.toString(),Banner_sb.toString(),Blog_Description.getText().toString().trim(),HTMLCODE).enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Toast.makeText(getActivity(),"Check your internet connection.....",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(),"Enter Full Details......",Toast.LENGTH_LONG).show();
                }

            }
        });
        this.mEditor = (RichEditor) inflate.findViewById(R.id.editor);
        this.Preview = (TextView) inflate.findViewById(R.id.preview);
        this.mEditor.setEditorHeight(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.mEditor.setEditorFontSize(22);
        this.mEditor.setEditorFontColor(SupportMenu.CATEGORY_MASK);
        this.mEditor.setPadding(10, 10, 10, 10);
        this.mEditor.setPlaceholder("Description");
        this.mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            public void onTextChange(String str) {
                BlogInsertionFragment.this.HTMLCODE = str;
            }
        });
        inflate.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.undo();
            }
        });
        inflate.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.redo();
            }
        });
        inflate.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setBold();
            }
        });
        inflate.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setItalic();
            }
        });
        inflate.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setSubscript();
            }
        });
        inflate.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setSuperscript();
            }
        });
        inflate.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setStrikeThrough();
            }
        });
        inflate.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setUnderline();
            }
        });
        inflate.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setHeading(1);
            }
        });
        inflate.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setHeading(2);
            }
        });
        inflate.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setHeading(3);
            }
        });
        inflate.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setHeading(4);
            }
        });
        inflate.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setHeading(5);
            }
        });
        inflate.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setHeading(6);
            }
        });
        inflate.findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setTextColor(this.isChanged ? ViewCompat.MEASURED_STATE_MASK : SupportMenu.CATEGORY_MASK);
                this.isChanged = !this.isChanged;
            }
        });
        inflate.findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setTextBackgroundColor(this.isChanged ? 0 : InputDeviceCompat.SOURCE_ANY);
                this.isChanged = !this.isChanged;
            }
        });
        inflate.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setIndent();
            }
        });
        inflate.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setOutdent();
            }
        });
        inflate.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setAlignLeft();
            }
        });
        inflate.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setAlignCenter();
            }
        });
        inflate.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setAlignRight();
            }
        });
        inflate.findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setBlockquote();
            }
        });
        inflate.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setBullets();
            }
        });
        inflate.findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.setNumbers();
            }
        });
        inflate.findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG", "dachshund");
            }
        });
        inflate.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        inflate.findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlogInsertionFragment.this.mEditor.insertTodo();
            }
        });
        return inflate;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 21 && i2 == -1 && intent != null) {
            try {
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                if(isMainPhoto)
                {
                    this.Mainbitmap = bitmap2;
                    this.MainIMG.setImageBitmap(bitmap2);
                }
                else
                {
                    this.Bannerbitmap = bitmap2;
                    this.BannerIMG.setImageBitmap(bitmap2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
