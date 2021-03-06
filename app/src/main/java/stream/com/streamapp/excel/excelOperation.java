package stream.com.streamapp.excel;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.home.BasicActivity;
import stream.com.streamapp.login;

/**
 * Created by Alan on 2017/12/10.
 */

public class excelOperation {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private static ArrayList<ArrayList<String>> excelData=new ArrayList<>();
    public static  void setData()
    {
        List<Bills> bills;

        //TODO: set data here
        /*      excelData是一个二维的ArrayList 第一维为bill
                "收入或支出",//in or out   String:"收入"
                "类型",     // Type       String:"买书"
                "时间",     //date        String:"2017-1-1"
                "地点",     //place       String:玉泉
                "金额",     //amount      String:"100RMB"
                "备注"      //Note        String:"无"
         */
        excelData.clear();
        bills = DataSupport.where("user_id = ? ", String.valueOf(login.getUser_id())).order("date asc").find(Bills.class);
        for(int i=0;i<bills.size();i++)
        {
            ArrayList<String> bill= new ArrayList<>();
            bill.add(bills.get(i).getInOrOut());
            bill.add(bills.get(i).getType());
            bill.add(bills.get(i).getDate());
            bill.add(bills.get(i).getPlace());
            bill.add(""+bills.get(i).getAmount());
            bill.add(bills.get(i).getNote());
            excelData.add(bill);
        }
    }
    public static void createExcel(Activity activity)
    {
        try {
            verifyStoragePermissions(activity);
            File saveFile= new File("mnt/sdcard/text.xls");
            if(saveFile.isFile()&&saveFile.exists())
            {
                saveFile.delete();
            }
            WritableWorkbook book = Workbook.createWorkbook(saveFile);
            WritableSheet sheet = book.createSheet("账单",0);
            String[] labelNames= {  "收入或支出",//in or out
                                    "类型",// Type
                                    "时间",//date
                                    "地点",//place
                                    "金额",//amount
                                    "备注" //Note
            };
            for (int i=0;i<labelNames.length;i++)
            {
                Label tmp=new Label(i,0,labelNames[i]);
                sheet.addCell(tmp);
            }
            for(int i=0;i<excelData.size();i++)
            {
                ArrayList<String> bill= excelData.get(i);
                for(int j=0;j<bill.size();j++)
                {
                    sheet.addCell(new Label(j,i+1,bill.get(j)));
                }
            }
            //jxl.write.Number number = new jxl.write.Number(1,0,12345);
            //sheet.addCell(number);
            book.write();
            book.close();
            final WeakReference<Activity> mActivity=new WeakReference<Activity> (activity);
            ((BasicActivity)mActivity.get()).handle(1);
            return;

        } catch (IOException e) {
            solveFail(activity);
            e.printStackTrace();
        } catch (RowsExceededException e) {
            solveFail(activity);
            e.printStackTrace();
        } catch (WriteException e) {
            solveFail(activity);
            e.printStackTrace();
        }

    }
    private static void solveFail(Activity activity)
    {
        final WeakReference<Activity> mActivity=new WeakReference<Activity> (activity);
        ((BasicActivity)mActivity.get()).handle(2);
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            Log.e("fff","IIIII");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
