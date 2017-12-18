package stream.com.streamapp.db;

import stream.com.streamapp.home.UpdateData;


/**
 * Created by WuYiQuan on 2017/12/14.
 */

public class ModifyAssets {
    public static void modify(int user_id, double amount, String date, String inOrOut, String type) throws InterruptedException {
        Assets assets = new Assets();
        assets.setUser_id(user_id);
        assets.setAmount(amount);
        assets.setDate(date);
        assets.setInOrOut(inOrOut);
        assets.setType(type);
        assets.setState(1);
        assets.setTimeStamp(date);
        assets.save();
        //UpdateData.UploadAssets();
        return;
    }
}
