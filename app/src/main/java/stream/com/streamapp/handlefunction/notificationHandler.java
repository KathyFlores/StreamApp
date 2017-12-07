package stream.com.streamapp.handlefunction;

import android.os.Handler;

/**
 * Created by Alan on 2017/12/7.
 */

public class notificationHandler {
    public static Handler mHandler;

    public static Handler getHandler() {
        return mHandler;
    }

    public static void setHandler(Handler mHandler) {
        smsHandler.mHandler = mHandler;
    }
}
