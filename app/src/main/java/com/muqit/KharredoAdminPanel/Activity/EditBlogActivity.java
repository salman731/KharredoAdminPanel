package com.muqit.KharredoAdminPanel.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Fragments.BlogInsertionFragment;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBlogActivity extends AppCompatActivity {

    private RichEditor mEditor;
    private TextView Preview;
    private ImageView MainIMG, BannerIMG;
    private MaterialButton MainIMG_BTN,BannerIMG_BTN,Submit_BTN;
    public TextInputEditText Blog_Title,Blog_Description;
    String HTMLCODE = "";
    public Bitmap Mainbitmap,Bannerbitmap;
    boolean isMainPhoto;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Blog");
        MainIMG = (ImageView) findViewById(R.id.Main_Photo);
        BannerIMG = (ImageView) findViewById(R.id.Banner_Photo);
        Blog_Title = (TextInputEditText) findViewById(R.id.Blog_title);
        Blog_Description = (TextInputEditText) findViewById(R.id.Blog_description);
        MainIMG_BTN = (MaterialButton) findViewById(R.id.Choose_main_img_btn);
        BannerIMG_BTN = (MaterialButton) findViewById(R.id.Choose_banner_img_btn);
        Submit_BTN = (MaterialButton) findViewById(R.id.Blog_Submit_BTN);
        MainIMG_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMainPhoto = true;
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(intent, 21);
            }
        });
        BannerIMG_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMainPhoto = false;
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(intent, 21);
            }
        });
        Submit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBlogActivity.this.progressDialog = new ProgressDialog(EditBlogActivity.this);
                EditBlogActivity.this.progressDialog.setMessage("Updating....");
                EditBlogActivity.this.progressDialog.setProgressStyle(0);
                EditBlogActivity.this.progressDialog.setCancelable(false);
                EditBlogActivity.this.progressDialog.show();
                ByteArrayOutputStream main_byteArrayOutputStream = new ByteArrayOutputStream();
                Mainbitmap.compress(Bitmap.CompressFormat.JPEG, 100, main_byteArrayOutputStream);
                String main_encodeToString = Base64.encodeToString(main_byteArrayOutputStream.toByteArray(), 0);
                StringBuilder Main_sb = new StringBuilder();
                Main_sb.append("data:image/jpeg;base64,");
                Main_sb.append(main_encodeToString);
                ByteArrayOutputStream banner_byteArrayOutputStream = new ByteArrayOutputStream();
                Bannerbitmap.compress(Bitmap.CompressFormat.JPEG, 100, banner_byteArrayOutputStream);
                String banner_encodeToString = Base64.encodeToString(main_byteArrayOutputStream.toByteArray(), 0);
                StringBuilder Banner_sb = new StringBuilder();
                Banner_sb.append("data:image/jpeg;base64,");
                Banner_sb.append(banner_encodeToString);

                if(MainIMG.getDrawable() != null && BannerIMG.getDrawable() != null && !TextUtils.isEmpty(Blog_Title.getText()) && !TextUtils.isEmpty(Blog_Description.getText()) && !TextUtils.isEmpty(HTMLCODE))
                {
                    ((RetrofitAPI) new RetrofitClient().getRetrofitClient().create(RetrofitAPI.class)).UpdateBlog(Blog_Title.getText().toString().trim(),Main_sb.toString(),Banner_sb.toString(),Blog_Description.getText().toString().trim(),HTMLCODE,getIntent().getStringExtra("id")).enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Toast.makeText(EditBlogActivity.this,"Updated.....",Toast.LENGTH_LONG).show();
                            EditBlogActivity.this.progressDialog.dismiss();

                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Toast.makeText(EditBlogActivity.this,"Check your internet connection.....",Toast.LENGTH_LONG).show();
                            EditBlogActivity.this.progressDialog.dismiss();
                        }
                    });
                }
                else
                {
                    Toast.makeText(EditBlogActivity.this,"Enter Full Details......",Toast.LENGTH_LONG).show();
                }

            }
        });
        this.mEditor = (RichEditor) findViewById(R.id.editor);
        this.Preview = (TextView) findViewById(R.id.preview);
        this.mEditor.setEditorHeight(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.mEditor.setEditorFontSize(22);
        this.mEditor.setEditorFontColor(SupportMenu.CATEGORY_MASK);
        this.mEditor.setPadding(10, 10, 10, 10);
        this.mEditor.setPlaceholder("Description");
        this.mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            public void onTextChange(String str) {
                HTMLCODE = str;
            }
        });
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.undo();
            }
        });
        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.redo();
            }
        });
        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setBold();
            }
        });
        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setItalic();
            }
        });
        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setSubscript();
            }
        });
        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setSuperscript();
            }
        });
        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setStrikeThrough();
            }
        });
        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setUnderline();
            }
        });
        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setHeading(1);
            }
        });
        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setHeading(2);
            }
        });
        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setHeading(3);
            }
        });
        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setHeading(4);
            }
        });
        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setHeading(5);
            }
        });
        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setHeading(6);
            }
        });
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                mEditor.setTextColor(this.isChanged ? ViewCompat.MEASURED_STATE_MASK : SupportMenu.CATEGORY_MASK);
                this.isChanged = !this.isChanged;
            }
        });
        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            public void onClick(View view) {
                mEditor.setTextBackgroundColor(this.isChanged ? 0 : InputDeviceCompat.SOURCE_ANY);
                this.isChanged = !this.isChanged;
            }
        });
        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setIndent();
            }
        });
        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setOutdent();
            }
        });
        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setAlignLeft();
            }
        });
        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setAlignCenter();
            }
        });
        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setAlignRight();
            }
        });
        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setBlockquote();
            }
        });
        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setBullets();
            }
        });
        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.setNumbers();
            }
        });
        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG", "dachshund");
            }
        });
        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEditor.insertTodo();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 21 && i2 == -1 && intent != null) {
            try {
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
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