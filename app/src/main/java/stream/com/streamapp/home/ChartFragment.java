package stream.com.streamapp.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stream.com.streamapp.R;

/**
 * Created by KathyF on 2017/11/26.
 */

public class ChartFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, null);
        return view;
    }
}
