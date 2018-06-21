package com.neituiquan.work.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.dialog.EditHeadImgMenuDialog;
import com.neituiquan.gson.UserModel;
import com.neituiquan.httpEvent.UploadHeadImgEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.URI2FilePath;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HeadImgActivity extends BaseActivity implements View.OnClickListener {

    private View editHeadUI_statusView;
    private ImageView editHeadUI_backImg;
    private ImageView editHeadUI_moreImg;
    private ImageView editHeadUI_headImg;

    private final static int REQUEST_CODE_PICK_IMAGE = 6;

    private String filePath = null;

    private EditHeadImgMenuDialog menuDialog;

    private final int MAX_SIZE = 1024 * 1024 * 2;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_head);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        menuDialog = new EditHeadImgMenuDialog(this);
        menuDialog.setDialogCallBack(dialogCallBack);
        String headImg = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getHeadImg();
        Glide.with(this).load(FinalData.IMG + headImg).into(editHeadUI_headImg);
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        editHeadUI_statusView.setLayoutParams(params);
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
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    private void upload(){
        if(filePath == null){
            ToastUtils.showShort("请选择图片");
            return;
        }
        File file = new File(filePath);
        if(FileUtils.getFileLength(file) >= MAX_SIZE){
            ToastUtils.showShort("文件不能超过2MB");
            return;
        }
        String url = FinalData.BASE_URL + "/updateHeadImg?id="+ App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        HttpFactory.getHttpUtils().uploadMultiFile(file,url,new UploadHeadImgEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uploadResult(UploadHeadImgEventModel eventModel){
        if(eventModel.isSuccess){
            UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
            if(userModel.code == 0){

                App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

                //发送给UserFragment
                EventBus.getDefault().post(userModel);
                finish();
            }else{
                ToastUtils.showShort(userModel.msg);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        filePath = URI2FilePath.getRealPathFromUri(this,uri);
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        editHeadUI_headImg.setImageBitmap(bit);
                    } catch (Exception e) {
                        ToastUtils.showShort("失败了");
                    }
                }
                break;

            default:
                break;
        }
    }


    private void bindViews() {

        editHeadUI_statusView = (View) findViewById(R.id.editHeadUI_statusView);
        editHeadUI_backImg = (ImageView) findViewById(R.id.editHeadUI_backImg);
        editHeadUI_moreImg = (ImageView) findViewById(R.id.editHeadUI_moreImg);
        editHeadUI_headImg = (ImageView) findViewById(R.id.editHeadUI_headImg);

        editHeadUI_backImg.setOnClickListener(this);
        editHeadUI_moreImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editHeadUI_backImg:
                finish();
                break;
            case R.id.editHeadUI_moreImg:
                menuDialog.show();
                break;
        }
    }

    EditHeadImgMenuDialog.DialogCallBack dialogCallBack = new EditHeadImgMenuDialog.DialogCallBack() {
        @Override
        public void onClick(int id) {
            switch (id){
                case R.id.dialog_uploadTv:
                    upload();
                    break;
                case R.id.dialog_photoTv:
                    choosePhoto();
                    break;
                case R.id.dialog_cancelTv:
                    break;
            }
        }
    };
}
