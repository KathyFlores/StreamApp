package stream.com.streamapp.addbill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

import stream.com.streamapp.R;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;

/**
 * Created by KathyF on 2017/11/26.
 */

public class PayFragment extends Fragment implements View.OnClickListener{
    private TextView
            meal,transportation,shopping,daily,clothes,
            vegetable,fruit,snack,book,study,house,investment,
            social,amusement,makeup,call,sport,redpacket,travel,
            medicine,office,digit,gift,wine,repair,other;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pay_type_layout, null);
        initView();
        setListener();
        return view;
    }
    private void initView()
    {
        meal=view.findViewById(R.id.meal);
        transportation=view.findViewById(R.id.transportation);
        shopping=view.findViewById(R.id.shopping);
        daily=view.findViewById(R.id.daily);
        vegetable=view.findViewById(R.id.vegetables);
        call=view.findViewById(R.id.call);
        clothes=view.findViewById(R.id.clothes);
        fruit=view.findViewById(R.id.fruit);
        snack=view.findViewById(R.id.snack);
        book=view.findViewById(R.id.book);
        study=view.findViewById(R.id.study);
        office=view.findViewById(R.id.office);
        makeup=view.findViewById(R.id.makeup);
        house=view.findViewById(R.id.house);
        investment=view.findViewById(R.id.investment);
        redpacket=view.findViewById(R.id.redpacket);
        sport=view.findViewById(R.id.sport);
        travel=view.findViewById(R.id.travel);
        medicine=view.findViewById(R.id.medicine);
        digit=view.findViewById(R.id.digit);
        gift=view.findViewById(R.id.gift);
        wine=view.findViewById(R.id.wine);
        repair=view.findViewById(R.id.repair);
        amusement=view.findViewById(R.id.amusement);
        social=view.findViewById(R.id.social);
        other=view.findViewById(R.id.other);
    }
    private void setListener()
    {
        meal.setOnClickListener(this);
        transportation.setOnClickListener(this);
        shopping.setOnClickListener(this);
        daily.setOnClickListener(this);
        vegetable.setOnClickListener(this);
        call.setOnClickListener(this);
        clothes.setOnClickListener(this);
        fruit.setOnClickListener(this);
        snack.setOnClickListener(this);
        book.setOnClickListener(this);
        study.setOnClickListener(this);
        office.setOnClickListener(this);
        makeup.setOnClickListener(this);
        house.setOnClickListener(this);
        investment.setOnClickListener(this);
        redpacket.setOnClickListener(this);
        sport.setOnClickListener(this);
        travel.setOnClickListener(this);
        medicine.setOnClickListener(this);
        digit.setOnClickListener(this);
        gift.setOnClickListener(this);
        wine.setOnClickListener(this);
        repair.setOnClickListener(this);
        amusement.setOnClickListener(this);
        social.setOnClickListener(this);
        other.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        //TODO：绑定种类
        String type = new String();
        switch(v.getId())
        {
            case R.id.meal:
                type = "meal";
                break;
            case R.id.transportation:
                type = "transportation";
                break;
            case R.id.shopping:
                type = "shopping";
                break;
            case R.id.daily:
                type = "daily";
                break;
            case R.id.clothes:
                type = "clothes";
                break;
            case R.id.vegetables:
                type = "vegetables";
                break;
            case R.id.fruit:
                type = "fruit";
                break;
            case R.id.snack:
                type = "snack";
                break;
            case R.id.book:
                type = "book";
                break;
            case R.id.study:
                type = "study";
                break;
            case R.id.house:
                type = "house";
                break;
            case R.id.investment:
                type = "investment";
                break;
            case R.id.social:
                type = "social";
                break;
            case R.id.amusement:
                type = "amusement";
                break;
            case R.id.makeup:
                type = "makeup";
                break;
            case R.id.call:
                type = "call";
                break;
            case R.id.sport:
                type = "sport";
                break;
            case R.id.travel:
                type = "travel";
                break;
            case R.id.medicine:
                type = "medicine";
                break;
            case R.id.office:
                type = "office";
                break;
            case R.id.digit:
                type = "digit";
                break;
            case R.id.gift:
                type = "gift";
                break;
            case R.id.repair:
                type = "repair";
                break;
            case R.id.wine:
                type = "wine";
                break;
            case R.id.redpacket:
                type = "redpacket";
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                bill.setDate(sdf.format(new Date()));
                bill.setInOrOut("out");
                bill.setUser_id(login.getUser_id());
                bill.save();
                Toast.makeText(getActivity(),"账单添加成功",Toast.LENGTH_SHORT).show();
                //Log.d("insetttttttttt", DataSupport.findAll(Bills.class).toString());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }
}
