package stream.com.streamapp.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stream.com.streamapp.R;

/**
 * Created by KathyF on 2017/11/26.
 */

public class UserFragment extends Fragment {
    private TextView username;
    private TextView edit;
    private TextView backup;
    private TextView about;
    private TextView help;
    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, null);
        initView();
        setListener();
        return view;
    }
    private void initView(){
        username=view.findViewById(R.id.username);
        edit=view.findViewById(R.id.edit);
        backup=view.findViewById(R.id.backup);
        about=view.findViewById(R.id.about);
        help=view.findViewById(R.id.help);
    }
    private void setListener(){

    }
}