package com.neituiquan.work.account;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.dialog.EditHeadImgMenuDialog;
import com.neituiquan.gson.UserModel;
import com.neituiquan.httpEvent.UploadHeadImgEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.GlideUtils;
import com.neituiquan.utils.TecentetOOSUtils;
import com.neituiquan.work.R;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;

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
        GlideUtils.load(headImg,editHeadUI_headImg);
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
        TecentetOOSUtils.upload(FinalData.SRC_IMG,filePath,
                new CosXmlProgressListener() {
                    @Override
                    public void onProgress(long complete, long target) {
                        if(FinalData.DEBUG){
                            if(complete == target){
                                Log.e("TecentetOOSUtils","上传成功");
                            }
                        }
                    }
                },new CosXmlResultListener(){

                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        if(result.httpCode == 200){
                            String url = FinalData.BASE_URL + "/updateHeadImgSimple";
                            HashMap<String,String> params = new HashMap<>();
                            params.put("id",App.getAppInstance().getUserInfoUtils().getUserId());
                            params.put("headImg",result.accessUrl);
                            HttpFactory.getHttpUtils().post(params,url,new UploadHeadImgEventModel());
                            if(FinalData.DEBUG){
                                Log.e("TecentetOOSUtils","result.httpCode:"+result.accessUrl);
                            }
                        }
                    }

                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                        ToastUtils.showShort("上传失败");
                    }
                });
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
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        filePath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        editHeadUI_headImg.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        ToastUtils.showShort("失败了");
                    }
                }
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
