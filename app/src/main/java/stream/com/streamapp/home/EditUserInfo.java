package stream.com.streamapp.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stream.com.streamapp.R;

public class EditUserInfo extends AppCompatActivity {
    ImageView backBTN = null;
    private TextView mChangePhoto;
    private TextView mChangeName;
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
    private final String uploadPhoto="http://47.95.245.4:9999/editphoto";
    private final OkHttpClient client = new OkHttpClient();
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle bd=msg.getData();
            String content=bd.getString("Return");
            if(content.equals("0"))
            {
                Toast.makeText(getApplicationContext(),"上传图片成功",Toast.LENGTH_LONG).show();
            }
            else if(content.equals("1"))
            {
                Toast.makeText(getApplicationContext(),"上传图片失败，请检查权限与网络",Toast.LENGTH_LONG).show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initView();
        setListener();
    }
    private void initView()
    {
        backBTN = (ImageView) findViewById(R.id.back_button);
        backBTN.setVisibility(View.VISIBLE);
        mChangeName=(TextView)findViewById(R.id.changeName);
        mChangePhoto=(TextView)findViewById(R.id.changePhoto);
    }
    private void setListener()
    {
        backBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePhoto();
            }
        });
        mChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInfo.this);
                View dialogView = LayoutInflater.from(EditUserInfo.this).inflate(R.layout.edit_username,null);
                final EditText usernameET = dialogView.findViewById(R.id.new_username);
                builder.setView(dialogView);
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUsername=usernameET.getText().toString().trim();
                        //TODO:修改用户名为newUsername

                        Toast.makeText(EditUserInfo.this,"用户名已修改",Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }
    private void changePhoto()
    {
        Picker.from(this)
                .count(1)
                .enableCamera(true)
                //.setEngine(new GlideEngine())
                .setEngine(new PicassoEngine())
//                .setEngine(new ImageLoaderEngine())
                //.setEngine(new CustomEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                //Bundle id=data.getExtras();
                Log.e("fff", u.toString());
                Log.i("picture", u.getPath());
                try {
                    Bitmap mBitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(u, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    Log.e("fff", imagePath);
                    final File mFile = new File(imagePath);
                    Thread uploadThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), mFile);


                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", "1", fileBody)
                                    .addFormDataPart("id", "1")//Integer.toString(getUser_id()))
                                    .build();

                            Request request = new Request.Builder()
                                    .url(uploadPhoto)
                                    .post(requestBody)
                                    .build();

                            Response response;
                            try {
                                response = client.newCall(request).execute();
                                String jsonString = response.body().string();
                                Log.e("fff","photo:"+jsonString);
                                if (jsonString.contains("<p>1</p>")) {
                                    Bundle re=new Bundle();
                                    re.putString("Return","0");
                                    Message msg=new Message();
                                    msg.setData(re);
                                    mHandler.sendMessage(msg);
                                } else {
                                    Bundle re=new Bundle();
                                    re.putString("Return","1");
                                    Message msg=new Message();
                                    msg.setData(re);
                                    mHandler.sendMessage(msg);
                                }

                            } catch (IOException e) {
                                Bundle re=new Bundle();
                                re.putString("Return","1");
                                Message msg=new Message();
                                msg.setData(re);
                                mHandler.sendMessage(msg);
                                Log.d("ff", "upload IOException ", e);
                            }
                        }
                    });
                    uploadThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
