package com.neituiquan.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private EditText editText;

    private Button button;

    private ChatAdapter chatAdapter;

    private LinearLayoutManager linearLayoutManager;

    private List<ChatEntity> chatList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.listView);
        editText = findViewById(R.id.inputEdit);
        button = findViewById(R.id.sendBtn);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatEntity entity = new ChatEntity();
                entity.type = 0;
                entity.msg = editText.getText().toString();

                ChatEntity entity2 = new ChatEntity();
                entity2.type = 1;
                entity2.msg = "哦哦，\nFuck you!";
                chatList.add(entity);
                chatList.add(entity2);
                chatAdapter.notifyDataSetChanged();

                editText.setText("");
                recyclerView.smoothScrollToPosition(chatList.size());
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    class ChatAdapter extends RecyclerView.Adapter<ItemViewHolder>{


        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewHolder holder;
            if(viewType == 0){
                holder = new ItemViewHolder(LayoutInflater.from(TestActivity.this).inflate(R.layout.item_chat_right,parent,false),0);
            }else{
                holder = new ItemViewHolder(LayoutInflater.from(TestActivity.this).inflate(R.layout.item_chat_left,parent,false),1);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.textView.setText(chatList.get(position).msg);
        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return chatList.get(position).type;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ItemViewHolder(View itemView,int type) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }


    static class ChatEntity{

        public String msg;

        public int type;
    }
}
