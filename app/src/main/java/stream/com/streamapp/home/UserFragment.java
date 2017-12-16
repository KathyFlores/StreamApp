package stream.com.streamapp.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

import stream.com.streamapp.R;
import stream.com.streamapp.excel.excelOperation;
import stream.com.streamapp.login;
import stream.com.streamapp.profile.photo;

/**
 * Created by KathyF on 2017/11/26.
 */

public class UserFragment extends Fragment {
    RoundedImageView avatar;
    private TextView username;
    private TextView edit;
    private TextView backup;
    private TextView about;
    private TextView help;
    private View view;
    private Button logoutBTN;
    private Drawable avatarSrc;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, null);
        initView();
        setListener();
        //TODO：刷新
        return view;
    }
    private void initView(){
        avatarSrc= ContextCompat.getDrawable(getActivity(),R.drawable.avatar);
        avatarSrc.setBounds(0,0,avatarSrc.getMinimumWidth(),avatarSrc.getMinimumHeight());
        avatar = view.findViewById(R.id.avatar);
        avatar.setImageResource(R.drawable.avatar);//默认头像
        //TODO: 设置头像，调用方法如下: 参数由edituserinfo.java得到
        if(photo.getPhotoState())
        {
            Log.e("aaaa",Uri.fromFile(new File(login.getBasicDir()+"/"+login.getUser_id()+".jpg")).toString());
            avatar.setImageURI(Uri.fromFile(new File(login.getBasicDir()+"/"+login.getUser_id()+".jpg")));
        }

        logoutBTN=view.findViewById(R.id.logout);
        username=view.findViewById(R.id.username);//TODO: 设置为用户的用户名
        username.setText(login.getUserName());
        edit=view.findViewById(R.id.edit);
        backup=view.findViewById(R.id.backup);
        about=view.findViewById(R.id.about);
        help=view.findViewById(R.id.help);

    }
    private void setListener(){
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excelOperation.setData();
                excelOperation.createExcel(getActivity());
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editUser = new Intent();
                editUser.setClass(getActivity(),EditUserInfo.class);
                startActivity(editUser);
            }
        });
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });
    }
}
