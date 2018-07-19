package com.neituiquan.work.widgets;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.dialog.ListDialog;
import com.neituiquan.entity.AbsEntity;
import com.neituiquan.view.AutoLoadRecyclerView;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/17.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 读取本地所有的图片
 */

public class PhotoExtractActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener {

    private static final String TAG = "PhotoExtractActivity";

    private View photoExtractUI_statusView;
    private ImageView photoExtractUI_backImg;
    private TextView photoExtractUI_titleTv;
    private SmartRefreshLayout photoExtractUI_refreshLayout;
    private AutoLoadRecyclerView photoExtractUI_recyclerView;
    private FrameLayout photoExtractUI_frameLayout;
    private LinearLayout photoExtractUI_pathLayout;
    private TextView photoExtractUI_doneTv;

    private static final int NO_SELECT = 0;

    private static final int SELECT = 1;

    public static final int RESULT_CODE = 89481;

    private int maxCount = 1;

    private List<String> selectList = new ArrayList<>();

    private int spanCount = 4;

    class MediaImgEntity extends AbsEntity{


        //完整图路径
        String fullPath;

        //图片名字
        String title;

        long id;

        //文件夹名字
        String groupName;

        //缩略图路径
        String thumbnailPath;

        int isSelect = NO_SELECT;

    }

    private static final int QUERY_OK = 111;

    private static final int SWITCHER_GROUP = 3821;

    private Handler handler;

    private List<MediaImgEntity> contentList = new ArrayList<>();

    private List<MediaImgEntity> fullList = new ArrayList<>();

    private GridLayoutManager gridLayoutManager;

    static class Handler extends android.os.Handler{

        private PhotoExtractActivity activity;

        public Handler(PhotoExtractActivity activity) {
            this.activity = activity;
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(activity == null){
                return;
            }
            switch (msg.what){
                case QUERY_OK:
                    activity.getLoadingDialog().dismiss();
//                Log.e(TAG,new Gson().toJson(activity.contentList));
                    activity.initViews();
                    return;
                case SWITCHER_GROUP:
                    activity.getLoadingDialog().dismiss();
                    activity.photoAdapter.notifyDataSetChanged();
                    activity.photoExtractUI_refreshLayout.finishRefresh();
                    return;
            }
        }
    }

    class PhotoAdapter extends RecyclerView.Adapter<ItemViewHolder>{

        private PhotoExtractActivity activity;

        private int space = 10;

        private int width = ScreenUtils.getScreenWidth() / spanCount - space;

        private int height = (int) (width * 1.4f);

        public PhotoAdapter(PhotoExtractActivity activity) {
            this.activity = activity;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout itemView = (FrameLayout) LayoutInflater.from(activity).inflate(R.layout.item_photo_extract,parent,false);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,height);
            itemView.setLayoutParams(params);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            //同步加载
//            Bitmap bitmap = activity.getThumbnailBitmap(activity.contentList.get(position).id);
//            ((ImageView)holder.itemView).setImageBitmap(bitmap);

            final MediaImgEntity entity = contentList.get(position);
            if(entity.isSelect == NO_SELECT){
                holder.item_coverView.setVisibility(View.GONE);
                holder.item_selectView.setVisibility(View.GONE);
            }else{
                holder.item_coverView.setVisibility(View.VISIBLE);
                holder.item_selectView.setVisibility(View.VISIBLE);
            }
            //glide 异步加载
            if(StringUtils.isEmpty(entity.thumbnailPath)){
                Glide.with(activity)
                        .load(new File(entity.fullPath))
                        .into(holder.item_imageView);
            }else{
                Glide.with(activity)
                        .load(new File(entity.thumbnailPath))
                        .into(holder.item_imageView);
            }

            holder.item_contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(entity.isSelect == NO_SELECT){
                        if(selectList.size() < maxCount){
                            entity.isSelect = SELECT;
                            if(!selectList.contains(entity.fullPath)){
                                selectList.add(entity.fullPath);
                            }
                        }else {
                            ToastUtils.showShort("最多只能选择"+maxCount+"张");
                        }
                    }else{
                        entity.isSelect = NO_SELECT;
                        selectList.remove(entity.fullPath);
                    }
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return contentList.get(position).isSelect;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        FrameLayout item_contentLayout;

        ImageView item_imageView;

        View item_coverView,item_selectView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_contentLayout = itemView.findViewById(R.id.item_contentLayout);
            item_imageView = itemView.findViewById(R.id.item_imageView);
            item_coverView = itemView.findViewById(R.id.item_coverView);
            item_selectView = itemView.findViewById(R.id.item_selectView);
        }
    }


    private PhotoAdapter photoAdapter;

    private List<String> groupList = new ArrayList<>();

    private ListDialog listDialog;

    private String currentGroupName = "";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.widgets_activity_photo_extract);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        initValues();
        listDialog = new ListDialog(this);

        maxCount = getIntent().getIntExtra("maxCount",1);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getLoadingDialog().show();
        initListForGroup(currentGroupName);
    }


    private void initValues(){
        getLoadingDialog().show();
        handler = new Handler(this);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor = getContentResolver()
                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                            MediaImgEntity entity = new MediaImgEntity();
                            entity.id = id;
                            entity.title = title;
                            entity.fullPath = path;
                            entity.groupName = getParentGroupName(title,path);

//                            entity.thumbnailPath = getThumbnailPath(entity.id);

                            entity.thumbnailPath = null;

                            fullList.add(entity);

                            if(!groupList.contains(entity.groupName)){
                                groupList.add(entity.groupName);
                            }
                        }
                        for(MediaImgEntity entity : fullList){
                            if(entity.groupName.equals(groupList.get(0))){
                                if(entity.thumbnailPath == null){
                                    entity.thumbnailPath = getThumbnailPath(entity.id);
                                }
                                contentList.add(entity);
                            }
                        }
                        handler.sendEmptyMessage(QUERY_OK);
                        cursor.close();
                    }
                }
        ).start();

    }


    /**
     * 07-17 09:29:39.101 9375-9499/com.neituiquan.work E/PhotoExtractActivity: path:   /storage/emulated/0/MagazineUnlock/magazine-unlock-01-2.3.1029-_2F8F9D7F084148E1EF1C99AE6EB14A24.jpg
     07-17 09:29:39.101 9375-9499/com.neituiquan.work E/PhotoExtractActivity: title:   magazine-unlock-01-2.3.1029-_2F8F9D7F084148E1EF1C99AE6EB14A24
     * @param title 文件名，不含尾缀
     * @param fillPath 绝对路径
     * @return 上一级的文件夹名字
     */
    private String getParentGroupName(String title,String fillPath){
        try{
            int index = fillPath.indexOf(title);
            String simplePath = fillPath.substring(0,index);
            String parentPath = simplePath.substring(0,simplePath.lastIndexOf("/"));
            return parentPath.substring(parentPath.lastIndexOf("/") + 1,parentPath.length());
        }catch (Exception e){
            return "other";
        }
    }

    /**
     * 根据Id获取一张图片的缩略图
     * @param id
     * @return
     */
    private Bitmap getThumbnailBitmap(long id){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPremultiplied = true;
        return MediaStore.Images.Thumbnails.getThumbnail(
                getContentResolver(),
                id,
                MediaStore.Images.Thumbnails.MINI_KIND,
                options);
    }

    /***
     * 获取缩略图的路径
     * @param id
     * @return
     */
    private String getThumbnailPath(long id){
        StringBuffer string = new StringBuffer();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{"_data"},
                "image_id = ? and kind = ?",
                new String[]{String.valueOf(id),String.valueOf(1)},
                null);
        while (cursor.moveToNext()){
            String path = cursor.getString(cursor.getColumnIndex("_data"));
            string.append(path);
        }
        cursor.close();
        return string.toString();
    }

    private void initViews(){
        photoAdapter = new PhotoAdapter(this);
        gridLayoutManager = new GridLayoutManager(this,spanCount);
        photoExtractUI_recyclerView.setLayoutManager(gridLayoutManager);
        photoExtractUI_recyclerView.setAdapter(photoAdapter);
        listDialog.setData(groupList);
        listDialog.getListAdapter().setListDialogCallBack(new ListDialog.ListDialogCallBack() {
            @Override
            public void onItemClick(int index, String msg) {
                if(currentGroupName.equals(msg)){
                    return;
                }
                currentGroupName = msg;
                getLoadingDialog().show();
                photoExtractUI_titleTv.setText(msg);
                initListForGroup(msg);
            }
        });
        photoExtractUI_titleTv.setText(groupList.get(0));
        currentGroupName = groupList.get(0);
    }

    /**
     * 加载一个文件夹的所有照片
     * @param groupName
     */
    private void initListForGroup(final String groupName){
        photoExtractUI_recyclerView.loadComplete();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contentList.clear();
                for(MediaImgEntity entity : fullList){
                    if(entity.groupName.equals(groupName)){
                        if(entity.thumbnailPath == null){
                            entity.thumbnailPath = getThumbnailPath(entity.id);
                        }
                        if(!contentList.contains(entity)){
                            contentList.add(entity);
                        }
                    }
                }
                handler.sendEmptyMessage(SWITCHER_GROUP);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photoExtractUI_backImg:
                finish();
                break;
            case R.id.photoExtractUI_pathLayout:
                listDialog.show();
                break;
            case R.id.photoExtractUI_doneTv:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selectList", (ArrayList<String>) selectList);
                setResult(RESULT_CODE,intent);
                finish();
                break;
        }
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        photoExtractUI_statusView.setLayoutParams(params);
    }


    private void bindViews() {

        photoExtractUI_statusView = (View) findViewById(R.id.photoExtractUI_statusView);
        photoExtractUI_backImg = (ImageView) findViewById(R.id.photoExtractUI_backImg);
        photoExtractUI_titleTv = (TextView) findViewById(R.id.photoExtractUI_titleTv);
        photoExtractUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.photoExtractUI_refreshLayout);
        photoExtractUI_recyclerView = (com.neituiquan.view.AutoLoadRecyclerView) findViewById(R.id.photoExtractUI_recyclerView);
        photoExtractUI_frameLayout = (FrameLayout) findViewById(R.id.photoExtractUI_frameLayout);
        photoExtractUI_pathLayout = findViewById(R.id.photoExtractUI_pathLayout);
        photoExtractUI_doneTv = findViewById(R.id.photoExtractUI_doneTv);

        photoExtractUI_backImg.setOnClickListener(this);
        photoExtractUI_pathLayout.setOnClickListener(this);
        photoExtractUI_doneTv.setOnClickListener(this);
        photoExtractUI_refreshLayout.setOnRefreshListener(this);
    }


}
