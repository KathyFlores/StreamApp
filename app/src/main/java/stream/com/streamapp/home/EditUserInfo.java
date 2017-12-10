package stream.com.streamapp.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import stream.com.streamapp.R;

public class EditUserInfo extends AppCompatActivity {

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
        mChangeName=(TextView)findViewById(R.id.changeName);
        mChangePhoto=(TextView)findViewById(R.id.changePhoto);
    }
    private void setListener()
    {
        mChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePhoto();
            }
        });
    }
    private void changePhoto()
    {
        Picker.from(this)
                .count(3)
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
                Log.i("picture", u.getPath());
            }
        }
    }


    static class CustomEngine implements LoadEngine {
        @Override
        public void displayImage(String path, ImageView imageView) {
            Log.i("picture", path);
        }

        @Override
        public void displayCameraItem(ImageView imageView) {

        }

        @Override
        public void scrolling(GridView view) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public CustomEngine() {

        }

        protected CustomEngine(Parcel in) {
        }

        public static final Creator<CustomEngine> CREATOR = new Creator<CustomEngine>() {
            public CustomEngine createFromParcel(Parcel source) {
                return new CustomEngine(source);
            }

            public CustomEngine[] newArray(int size) {
                return new CustomEngine[size];
            }
        };
    }
}
