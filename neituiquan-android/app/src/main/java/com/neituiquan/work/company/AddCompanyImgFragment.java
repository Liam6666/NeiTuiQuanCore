package com.neituiquan.work.company;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.CompanyEntity;
import com.neituiquan.entity.CompanyImgEntity;
import com.neituiquan.gson.AbsModel;
import com.neituiquan.gson.StringModel;
import com.neituiquan.httpEvent.AddCompanyEventModel;
import com.neituiquan.httpEvent.UploadCompanyImgEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.net.RequestEventModel;
import com.neituiquan.utils.URI2FilePath;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 添加企业图片
 */

public class AddCompanyImgFragment extends BaseFragment implements View.OnClickListener {

    public static AddCompanyImgFragment newInstance(CompanyEntity entity) {

        Bundle args = new Bundle();
        args.putSerializable("entity",entity);
        AddCompanyImgFragment fragment = new AddCompanyImgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView bindCompanyFG_backImg;
    private TextView bindCompanyFG_saveTv;
    private LinearLayout bindCompanyFG_imgLayout;
    private TextView bindCompanyFG_choosePhotoTv;

    private CompanyEntity entity;

    private List<String> imgPathList = new ArrayList<>();

    private FrameLayout emptyItemView;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_add_company_img,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        entity = (CompanyEntity) getArguments().getSerializable("entity");
        emptyItemView = createEmptyView();
        if(entity == null){
            bindCompanyFG_imgLayout.addView(emptyItemView);
        }else{
            initValues();
        }
    }

    private void initValues(){
        bindCompanyFG_imgLayout.removeAllViews();
        for(CompanyImgEntity imgEntity : entity.getImgList()){
            bindCompanyFG_imgLayout.addView(createItems(imgEntity));
        }
        if(bindCompanyFG_imgLayout.getChildCount() == 0){
            //当前没有图片

            bindCompanyFG_imgLayout.addView(emptyItemView);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bindCompanyFG_backImg:
//                getFragmentManager().popBackStack();
                ((BindCompanyActivity)getContext()).finish();
                break;
            case R.id.bindCompanyFG_saveTv:
//                if(entity == null){
                    save();
//                }else{
//                    saveChanged();
//                }
                break;
            case R.id.bindCompanyFG_choosePhotoTv:
                choosePhoto();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BindCompanyActivity.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == BindCompanyActivity.RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        String filePath = URI2FilePath.getRealPathFromUri(getContext(),uri);
                        Bitmap bit = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
                        bindCompanyFG_imgLayout.removeView(emptyItemView);
                        bindCompanyFG_imgLayout.addView(createItemsForPhoto(bit,filePath));
                    } catch (Exception e) {
                        ToastUtils.showShort("失败了");
                    }
                }
                break;
        }
    }

    private void delItem(String id){
        String url = FinalData.BASE_URL + "/delCompanyImg?id=" + id;
        HttpFactory.getHttpUtils().get(url,new RequestEventModel());
    }

    private void save(){
        String url = FinalData.BASE_URL + "/uploadImg";
        for(String filePath : imgPathList){
            File file = new File(filePath);
            HttpFactory.getHttpUtils().uploadMultiFile(file,url,new UploadCompanyImgEventModel());
        }
    }

    private int successCount = 0;

    private List<String> resultImgUrl = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uploadResult(UploadCompanyImgEventModel eventModel){
        if(eventModel.isSuccess){
            successCount ++;
            StringModel stringModel = new Gson().fromJson(eventModel.resultStr,StringModel.class);
            resultImgUrl.add(stringModel.data);
            if(FinalData.DEBUG){
                Log.e("resultImgUrl",eventModel.resultStr);
            }
            if(successCount == imgPathList.size()){
                //上传完毕
//                ToastUtils.showShort("上传完毕");
                String url = FinalData.BASE_URL + "/addCompanyImgList";
                List<CompanyImgEntity> entityList = new ArrayList<>();
                int sort = 0;
                for(String imgUrl : resultImgUrl){
                    CompanyImgEntity entity = new CompanyImgEntity();
                    entity.setImgUrl(imgUrl);
                    entity.setSort(sort + "");
                    entity.setCompanyId(this.entity.getId());
                    sort ++;
                    entityList.add(entity);
                }
                String json = new Gson().toJson(entityList);
                HttpFactory.getHttpUtils().post(json,url,new AddCompanyEventModel(BindCompanyActivity.ADD_COMPANY_IMG));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addCompanyImgResult(AddCompanyEventModel eventModel){
        if(eventModel.eventId == BindCompanyActivity.ADD_COMPANY_IMG){
            if(eventModel.isSuccess){
//                ToastUtils.showShort("上传完毕");
                ((BindCompanyActivity)getContext()).finish();
            }
        }
    }

    private void saveChanged(){

    }

    /**
     * 从相册选取图片
     */
    private void choosePhoto(){
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, BindCompanyActivity.REQUEST_CODE_PICK_IMAGE);

    }

    private FrameLayout createItemsForPhoto(Bitmap bit, final String filePath){
        final FrameLayout itemView = (FrameLayout) View.inflate(getContext(),R.layout.item_edit_company_img,null);
        ImageView item_contentImg = itemView.findViewById(R.id.item_contentImg);
        ImageView item_delImg = itemView.findViewById(R.id.item_delImg);
        item_contentImg.setImageBitmap(bit);
        imgPathList.add(filePath);
        item_delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPathList.remove(filePath);
                bindCompanyFG_imgLayout.removeView(itemView);
                if(bindCompanyFG_imgLayout.getChildCount() == 0){
                    bindCompanyFG_imgLayout.addView(emptyItemView);
                }
            }
        });
        return itemView;
    }

    private FrameLayout createItems(final CompanyImgEntity entity){
//        final FrameLayout itemView = (FrameLayout) View.inflate(getContext(),R.layout.item_edit_company_img,null);
        final FrameLayout itemView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_edit_company_img,bindCompanyFG_imgLayout,false);
        ImageView item_contentImg = itemView.findViewById(R.id.item_contentImg);
        ImageView item_delImg = itemView.findViewById(R.id.item_delImg);
        Glide.with(getContext()).load(FinalData.IMG + entity.getImgUrl()).into(item_contentImg);
        item_delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delItem(entity.getId());
                bindCompanyFG_imgLayout.removeView(itemView);
                if(bindCompanyFG_imgLayout.getChildCount() == 0){
                    bindCompanyFG_imgLayout.addView(emptyItemView);
                }
            }
        });
        return itemView;
    }

    private FrameLayout createEmptyView(){
        FrameLayout itemView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_edit_company_empty,bindCompanyFG_imgLayout,false);
        return itemView;
    }

    private void bindViews() {

        bindCompanyFG_backImg = (ImageView) findViewById(R.id.bindCompanyFG_backImg);
        bindCompanyFG_saveTv = (TextView) findViewById(R.id.bindCompanyFG_saveTv);
        bindCompanyFG_imgLayout = (LinearLayout) findViewById(R.id.bindCompanyFG_imgLayout);
        bindCompanyFG_choosePhotoTv = (TextView) findViewById(R.id.bindCompanyFG_choosePhotoTv);

        bindCompanyFG_backImg.setOnClickListener(this);
        bindCompanyFG_saveTv.setOnClickListener(this);
        bindCompanyFG_choosePhotoTv.setOnClickListener(this);
    }

}
