package stream.com.streamapp.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
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

import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import stream.com.streamapp.R;

public class EditUserInfo extends AppCompatActivity {
    ImageView backBTN = null;
    private TextView mChangePhoto;
    private TextView mChangeName;
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
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
                Log.e("picture", u.getPath());
                //TODO：修改头像
            }
        }
    }

}
