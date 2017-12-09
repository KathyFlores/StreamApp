package stream.com.streamapp.addbill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;

import stream.com.streamapp.R;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;

/**
 * Created by KathyF on 2017/11/26.
 */

public class IncomeFragment extends Fragment implements View.OnClickListener{
    private TextView salary,redpacket,parttime,other;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inconme_type_layout, null);
        initView();
        setListener();
        return view;
    }
    private void initView()
    {
        salary=view.findViewById(R.id.salary);
        redpacket=view.findViewById(R.id.redpacket);
        parttime=view.findViewById(R.id.parttime);
        other=view.findViewById(R.id.other);
    }
    private void setListener()
    {
        salary.setOnClickListener(this);
        redpacket.setOnClickListener(this);
        parttime.setOnClickListener(this);
        other.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        String type = new String();
        //TODO：绑定种类
        switch(v.getId())
        {
            case R.id.salary:
                type = "salary";
                break;
            case R.id.redpacket:
                type = "redpacket";
                break;
            case R.id.parttime:
                type = "parttime";
                break;
            case R.id.other:
                type = "other";
                break;
        }
        final String typeFinal = new String(type) ;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.bill_info,null);
        final EditText amountET = dialogView.findViewById(R.id.amount);
        final EditText noteET = dialogView.findViewById(R.id.note);
        builder.setView(dialogView);
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bills bill = new Bills();
                String amount=amountET.getText().toString().trim();
                String note = noteET.getText().toString().trim();
                bill.setType(typeFinal);
                bill.setAmount(Double.valueOf(amount));
                bill.setNote(note);
                bill.setPlace("somewhere");
                bill.setInOrOut("in");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                bill.setDate(sdf.format(new Date()));
                bill.setUser_id(login.getUser_id());
                bill.save();
                Toast.makeText(getActivity(),"账单添加成功",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }
}
