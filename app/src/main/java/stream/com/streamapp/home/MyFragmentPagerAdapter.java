package stream.com.streamapp.home;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by KathyF on 2017/11/26.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private List<Fragment> mlist;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list)
    {
        super(fm);
        mFragmentManager = fm;
        mlist=list;
    }
    @Override
    public Fragment getItem(int index)
    {
        return mlist.get(index);
    }

    @Override
    public int getCount()
    {
        return mlist.size();
    }

}
