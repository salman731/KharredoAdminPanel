package com.muqit.KharredoAdminPanel.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.Activity.ChatActivity;
import com.muqit.KharredoAdminPanel.Models.ChatHistoryData;
import com.muqit.KharredoAdminPanel.Models.ChatResponse.ChatData;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;

public class ChatRecyclerViewAdpater extends Adapter<ChatRecyclerViewAdpater.OrderedItemsViewHolder> {
    private ArrayList<ChatHistoryData> chatDataArrayList;
    private Context context;

    public class OrderedItemsViewHolder extends ViewHolder {
        public MaterialTextView title,subTitle;

        public OrderedItemsViewHolder(View view) {
            super(view);
            this.title = (MaterialTextView) view.findViewById(R.id.chat_title);
            this.subTitle = (MaterialTextView) view.findViewById(R.id.chat_subtitle);
        }
    }

    public ChatRecyclerViewAdpater(ArrayList<ChatHistoryData> arrayList, Context context2) {
        this.chatDataArrayList = arrayList;
        this.context = context2;
    }

    public OrderedItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OrderedItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(OrderedItemsViewHolder orderedItemsViewHolder, int i) {
        ChatHistoryData chatHistoryData = chatDataArrayList.get(i);
        orderedItemsViewHolder.title.setText(chatHistoryData.getFirstName());
        orderedItemsViewHolder.subTitle.setText(chatHistoryData.getMessage());
        orderedItemsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatActivity.class).putExtra("userid",chatHistoryData.getUserID()));
            }
        });
    }

    public int getItemCount() {
        return this.chatDataArrayList.size();
    }
}
