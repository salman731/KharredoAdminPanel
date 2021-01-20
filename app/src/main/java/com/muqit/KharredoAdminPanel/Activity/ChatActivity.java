package com.muqit.KharredoAdminPanel.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.ChattingData;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.R;
import com.samsao.messageui.models.Message;
import com.samsao.messageui.views.MessagesWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    MessagesWindow messagesWindow;
    EditText Msg;
    MaterialButton Send_btn;
    ProgressDialog progressDialog;
    String msgtxt;
    Timer t = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messagesWindow = (MessagesWindow) findViewById(R.id.customized_messages_window);
        View inflate = LayoutInflater.from(this).inflate(R.layout.empty_layout, null);
        Msg = inflate.findViewById(R.id.Msg_et);
        Send_btn = inflate.findViewById(R.id.SendMsg_btn);
        messagesWindow.setWritingMessageView(inflate);
        ProgressDialog progressDialog2 = new ProgressDialog(ChatActivity.this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading....");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgtxt = Msg.getText().toString();
                Msg.getText().clear();
                (new RetrofitClient().getRetrofitClient()).create(RetrofitAPI.class).sendMessage(getIntent().getStringExtra("userid"),Msg.getText().toString()).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateTime = dateFormat.format(new Date());
                        messagesWindow.sendMessage(msgtxt +"\n" + currentDateTime);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this,"Something went wrong.......",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        GetChattingData();

     /*   t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      GetChattingData();

                                  }

                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                0,
//Set the amount of time between each execution (in milliseconds)
                5000);*/



    }
    void GetChattingData()
    {

        (new RetrofitClient().getRetrofitClient()).create(RetrofitAPI.class).getChattingData(getIntent().getStringExtra("userid")).enqueue(new Callback<ArrayList<ChattingData>>() {
            @Override
            public void onResponse(Call<ArrayList<ChattingData>> call, Response<ArrayList<ChattingData>> response) {
                Collections.reverse(response.body());
                getSupportActionBar().setTitle(response.body().get(0).getFirstName() + " " +response.body().get(0).getLastName());
                for(int i = 0 ;i<response.body().size();i++)
                {
                    if(response.body().get(i).getClient().equals("1"))
                    {
                        messagesWindow.receiveMessage(response.body().get(i).getMessage() +"\n"+ response.body().get(i).getCreatedAt());
                    }
                    else if (response.body().get(i).getClient().equals("0"))
                    {
                        messagesWindow.sendMessage(response.body().get(i).getMessage() +"\n"+ response.body().get(i).getCreatedAt());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<ChattingData>> call, Throwable t) {
                Toast.makeText(ChatActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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
}