package com.neituiquan.work.chat;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.work.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/13.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 图片表情
 */

public class ChatEmotionFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;

    private static final int spanCount = 4;

    private ChatEmotionAdapter adapter;

    private List<String> emotionList = new ArrayList<>();

    private ChatEmotionFragmentCallBack chatEmotionFragmentCallBack;

    public static ChatEmotionFragment newInstance() {

        Bundle args = new Bundle();

        ChatEmotionFragment fragment = new ChatEmotionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(),spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        return recyclerView;
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/13/20180713442185_crHFNU.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/13/20180713442185_GmcLYJ.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/13/20180713442186_LkuPVD.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/13/20180713442186_QCHody.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/13/20180713442187_uYJWIi.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/13/20180713441756_TBkQRn.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/12/20180712365006_PSmMwF.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/12/20180712365007_TJofYI.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/12/20180712365007_yxhSVN.jpg");
        emotionList.add("http://img.doutula.com/production/uploads/image/2018/07/12/20180712365007_mYnVbJ.jpg");
        emotionList.add("https://ws2.sinaimg.cn/bmiddle/6af89bc8gw1f8p91olxemj206y06yq36.jpg");
        emotionList.add("https://ws2.sinaimg.cn/bmiddle/6af89bc8gw1f8qjb8na2uj20cs0csjs1.jpg");
        emotionList.add("https://ws3.sinaimg.cn/bmiddle/6af89bc8gw1f8rlydxuggj20b40b4wfl.jpg");
        emotionList.add("https://ws4.sinaimg.cn/bmiddle/6af89bc8gw1f8r359tjsuj204h052q2r.jpg");
        emotionList.add("https://ws4.sinaimg.cn/bmiddle/6af89bc8gw1f8s6dsvhxaj203x03x746.jpg");
        emotionList.add("https://ws1.sinaimg.cn/bmiddle/6af89bc8gw1f8qnnh2rwij20640640sr.jpg");
        emotionList.add("https://ws1.sinaimg.cn/bmiddle/6af89bc8gw1f8npct4duxj202m02d3ya.jpg");
        emotionList.add("https://ws1.sinaimg.cn/bmiddle/9150e4e5ly1fcrfy16gxgj205k04gt9o.jpg");
        emotionList.add("https://ws2.sinaimg.cn/bmiddle/9150e4e5ly1fjrjc926mfg206o06o7cb.gif");
        emotionList.add("https://ws1.sinaimg.cn/bmiddle/9150e4e5ly1fdshc9jvmbg205003cwfa.gif");
        adapter = new ChatEmotionAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void setChatEmotionFragmentCallBack(ChatEmotionFragmentCallBack chatEmotionFragmentCallBack) {
        this.chatEmotionFragmentCallBack = chatEmotionFragmentCallBack;
    }

    class ChatEmotionAdapter extends RecyclerView.Adapter<ItemViewHolder>{

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_chat_emotion,parent,false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            Glide.with(getContext()).load(emotionList.get(position)).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chatEmotionFragmentCallBack != null){
                        chatEmotionFragmentCallBack.onItemClick(emotionList.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return emotionList.size();
        }

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_imageView);
        }
    }

    public interface ChatEmotionFragmentCallBack{

        public void onItemClick(String emotionUrl);

    }
}
