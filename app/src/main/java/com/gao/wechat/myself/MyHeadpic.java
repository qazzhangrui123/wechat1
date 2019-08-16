package com.gao.wechat.myself;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.wechat.MainActivity;
import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;
import com.loonggg.bottomsheetpopupdialoglib.ShareBottomPopupDialog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MyHeadpic extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.myself.MyHeadpic";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private TextView takepic;
    private TextView choosepic;
    private ImageView imageView;
    private Uri imageUri;

    public static final int TAKE_PTHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_headpic);
        setCustomToolBar();

        init();

    }

    private void init(){
        linearLayout = findViewById(R.id.my_head_pic);

        imageView  = findViewById(R.id.myself_head_pic);
        userInfo = AppData.getInstance().getMyInfo();
    }

    private void setCustomToolBar() {
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle("个人头像");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.finish:
                showDialog();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.myname_toolbar,menu);
        return true;
    }

    public void showDialog(){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.share_bottom_dialog, null);
        ShareBottomPopupDialog shareBottomPopupDialog = new ShareBottomPopupDialog(this, dialogView);
        shareBottomPopupDialog.showPopup(linearLayout);
        takepic = dialogView.findViewById(R.id.phone_take_photo);
        choosepic = dialogView.findViewById(R.id.choose_from_phone);
        setclick();

    }
    private void setclick(){
        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建File对象，用于存储拍照后的照片
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(MyHeadpic.this,"com.gao.wechat.myself.MyHeadpic.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent , TAKE_PTHOTO);
            }
        });
        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MyHeadpic.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MyHeadpic.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum( ) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO); // 打开相册
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions , int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length>0 & grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission" , Toast. LENGTH_SHORT). show();
                }
                break;
            default:
        }
    }

    protected void onActivityResult(int requestCode, int resultCode ,Intent data){
        switch (requestCode){
            case TAKE_PTHOTO:
                if(requestCode == RESULT_OK){
                    try{
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                //判断手机系统版本号
                if(Build.VERSION.SDK_INT >= 19){
                    //4.4及以上系统使用这个方法处理图片
                    handLeImage0nKitKat(data);
                }else{
                    handleImageBeforeKitKat(data);
                }
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handLeImage0nKitKat (Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" + "://downloads/public_downLoads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); //根据图片路径显示图片
    }
    private void handleImageBeforeKitKat (Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径.
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close() ;
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if ( imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap) ;
        } else {
            Toast.makeText(this, "failed to get image" , Toast. LENGTH_SHORT) . show();
        }
    }
}
