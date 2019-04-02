package com.example.administrator.fstorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Storage_Activity extends AppCompatActivity
{
    private EditText address;
    private Button upload;
    private Button download;
    private ImageView imageView;
    //创立变量
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri file;
    private File localFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_layout);
        storage=FirebaseStorage.getInstance();
        //创建一个FirebaseStorage实例
        storageRef=storage.getReference();
        //创建引用
        address=(EditText)findViewById(R.id.address);
        upload=(Button)findViewById(R.id.up);
        download=(Button)findViewById(R.id.down);
        imageView=(ImageView)findViewById(R.id.imageView);
        //获取实例
        requestMultiplePermissions();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string=address.getText().toString();
                //得到输入地址
                file=Uri.fromFile(new File(string));
                //本地文件上传
                final StorageReference testRef=storageRef.child(file.getLastPathSegment());
                //创建引用，储存地址
                testRef.putFile(file)
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception exception) {
                               Toast.makeText(Storage_Activity.this,"upload F",Toast.LENGTH_SHORT).show();
                           }
                       })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(Storage_Activity.this,"upload T",Toast.LENGTH_SHORT).show();
                            }
                        });
                        //上传
                }
        });
        //监听
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference tryRef=storage.getReferenceFromUrl("gs://firbase0124.appspot.com/"+file.getLastPathSegment());
                //创建引用
                try{
                    localFile=new File("/sdcard/"+"down"+file.getLastPathSegment());
                    localFile.createNewFile();
                    //本地存放地址
                }catch (IOException E){}
                tryRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Storage_Activity.this,"download T",Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(Storage_Activity.this,"download F",Toast.LENGTH_SHORT).show();
                            }
                        });
                        //下载
                }
        });
        //监听
    }
    private void requestMultiplePermissions()
    {
        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions,1);
    }
    //运行时权限处理
}
