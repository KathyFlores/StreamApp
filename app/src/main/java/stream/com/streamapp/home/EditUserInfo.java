package stream.com.streamapp.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import android.os.Message;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stream.com.streamapp.R;
import stream.com.streamapp.login;
import stream.com.streamapp.profile.photo;

import static stream.com.streamapp.login.setUser_id;

public class EditUserInfo extends AppCompatActivity {
    ImageView backBTN = null;
    private String mFilePath="";
    private TextView mChangePhoto;
    private TextView mChangeName;
    private TextView mChangeFace;
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
    private final String uploadPhoto="http://47.95.245.4:9999/editphoto";
    private final String serverUrl="http://47.95.245.4/query.php";
    private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();

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
            else if(content.equals("2"))
            {
                Toast.makeText(getApplicationContext(),"修改用户名成功",Toast.LENGTH_LONG).show();
            }
            else if(content.equals("3"))
            {
                Toast.makeText(getApplicationContext(),"修改用户名失败",Toast.LENGTH_LONG).show();
            }
            else if(content.equals("4"))
            {
                Toast.makeText(getApplicationContext(),"上传人脸成功",Toast.LENGTH_LONG).show();
            }
            else if(content.equals("5"))
            {
                Toast.makeText(getApplicationContext(),"人脸无法被识别，请重新上传",Toast.LENGTH_LONG).show();
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
        mChangeFace=(TextView)findViewById(R.id.changeFace);
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
                        final String newUsername=usernameET.getText().toString().trim();
                        //TODO:修改用户名为newUsername
                        Thread tt= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int id= login.getUser_id();
                                String query="update user set name=\""+newUsername+"\" where id ="+id+";";
                                RequestBody body = new FormBody.Builder()
                                        .add("content", query)
                                        .build();
                                Request tRequest = new Request.Builder()
                                        .url(serverUrl)
                                        .post(body)
                                        .build();
                                Call call = client.newCall(tRequest);
                                Response tResponse = null;
                                try {
                                    tResponse = call.execute();
                                    Bundle re=new Bundle();
                                    re.putString("Return","2");
                                    login.setUserName(newUsername);
                                    Message msg=new Message();
                                    msg.setData(re);
                                    mHandler.sendMessage(msg);
                                } catch (IOException e) {
                                    Bundle re=new Bundle();
                                    re.putString("Return","3");
                                    Message msg=new Message();
                                    msg.setData(re);
                                    e.printStackTrace();
                                }

                            }
                        });
                        tt.start();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        mChangeFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(login.getUser_id());
            }
        });
    }
    private void openCamera(int uid)
    {

        Intent id=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (id.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("wrong file", ex.getMessage(), ex);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "stream.com.streamapp.fileprovider", photoFile);
                id.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                id.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1);
                id.putExtra("android.intent.extras.CAMERA_FACING", 1);
                setUser_id(uid);
                startActivityForResult(id, 999);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
        mFilePath = image.getAbsolutePath();
        return image;
    }
    private void changePhoto()
    {
        Picker.from(this)
                .count(1)
                .enableCamera(true)
                .setEngine(new PicassoEngine())
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
                                    .addFormDataPart("id", ""+login.getUser_id())//Integer.toString(getUser_id()))
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
                                    photo.download(login.getUser_id());

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
                            } catch(Exception e)
                            {
                                Bundle re=new Bundle();
                                re.putString("Return","1");
                                Message msg=new Message();
                                msg.setData(re);
                                mHandler.sendMessage(msg);
                            }
                        }
                    });
                    uploadThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        else if(requestCode==999&&resultCode==RESULT_OK)
        {
            final File ff=new File(mFilePath);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), ff);


                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", "1", fileBody)
                            .addFormDataPart("id", ""+login.getUser_id())//Integer.toString(getUser_id()))
                            .addFormDataPart("isInsert", "1")
                            .build();

                    Request request = new Request.Builder()
                            .url("http://10.214.129.171:9999/upload")
                            .post(requestBody)
                            .build();

                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        String jsonString = response.body().string();
                        Pattern r = Pattern.compile("<p>.*?</p>");
                        Matcher m = r.matcher(jsonString);
                        if(m.find())
                        {
                            int re=Integer.valueOf(m.group(0).replace("<p>","").replace("</p>",""));
                            if (re==10)
                            {
                                Bundle reb=new Bundle();
                                reb.putString("Return","4");
                                Message msg=new Message();
                                msg.setData(reb);
                                mHandler.sendMessage(msg);
                            }
                            else{
                                Bundle reb=new Bundle();
                                reb.putString("Return","5");
                                Message msg=new Message();
                                msg.setData(reb);
                                mHandler.sendMessage(msg);
                            }
                        }

                    } catch (IOException e) {
                        Log.d("ff","upload IOException ",e);
                    }
                    return;
                }
            });
            t.start();
        }


    }
}
