package stream.com.streamapp.home;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import stream.com.streamapp.R;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;

public class BillDetail extends AppCompatActivity {
    private int BillId;
    public final static int RESULT_CODE=1;
    private boolean isEdit = false;
    private boolean changed = false;
    private Context mContext;
    private ImageView backBTN=null;
    private TextView category = null;
    private TextView InOrOut = null;
    private EditText amount = null;
    private EditText note = null;
    private EditText time = null;
    private EditText place = null;
    private TextView editBTN,deleteBTN;
    private Drawable icon;
    private Drawable arrow;
    private double mAmount;
    private String mInOrOut,mNote,mTime,mPlace,mCategory;
    private List<Bills> mBill;
    KeyListener amountListener,noteListener,timeListener,placeListener;
    PopupMenu popupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);
        Bundle extras = getIntent().getExtras();
        BillId=extras.getInt("BillId");
        Log.e("BillId:",""+BillId);
        initView();
        setListener();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("result", changed);
            setResult(RESULT_CODE, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initView(){
        editBTN=(TextView)findViewById(R.id.editBTN);
        deleteBTN=(TextView)findViewById(R.id.deleteBTN);
        backBTN = (ImageView)findViewById(R.id.back_button);
        category = (TextView)findViewById(R.id.category);
        mContext=getApplicationContext();
        InOrOut = (TextView) findViewById(R.id.InOrOut);
        amount = (EditText)findViewById(R.id.amount);
        amountListener=amount.getKeyListener();
        amount.setKeyListener(null);
        amount.setEnabled(false);
        note = (EditText)findViewById(R.id.note);
        noteListener=note.getKeyListener();
        note.setKeyListener(null);
        note.setEnabled(false);
        time = (EditText)findViewById(R.id.time);
        timeListener=time.getKeyListener();
        time.setKeyListener(null);
        time.setEnabled(false);
        place = (EditText)findViewById(R.id.place);
        placeListener=place.getKeyListener();
        place.setKeyListener(null);
        place.setEnabled(false);
        arrow= ContextCompat.getDrawable(mContext,R.drawable.arrow_down);

        popupMenu = new PopupMenu(this,InOrOut);
        popupMenu.getMenuInflater().inflate(R.menu.inoroutmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.in){
                    InOrOut.setText(R.string.income);
                }
                else if(item.getItemId()==R.id.out){
                    InOrOut.setText(R.string.expense);
                }

                return true;
            }
        });

        mBill = DataSupport.where("id = ? and state <> 3 ", String.valueOf(BillId)).find(Bills.class);

        mAmount = mBill.get(0).getAmount();
        mInOrOut = (mBill.get(0).getInOrOut().equals("in"))?"收入":"支出" ;
        mNote = mBill.get(0).getNote();
        mPlace= mBill.get(0).getPlace();
        mTime = mBill.get(0).getDate();
        mCategory = mBill.get(0).getType();

        //绑定数据不用管，都做好了
        InOrOut.setText(mInOrOut);
        amount.setText(""+mAmount);
        note.setText(mNote);
        place.setText(mPlace);
        time.setText(mTime);
        switch(mCategory)
        {
            case "meal":
                category.setText(R.string.meal);
                icon= ContextCompat.getDrawable(mContext,R.drawable.meal);
                break;
            case "transportation":
                category.setText(R.string.transportation);
                icon= ContextCompat.getDrawable(mContext,R.drawable.transportation);
                break;
            case "shopping":
                category.setText(R.string.shopping);
                icon= ContextCompat.getDrawable(mContext,R.drawable.shopping);
                break;
            case "daily":
                category.setText(R.string.daily);
                icon= ContextCompat.getDrawable(mContext,R.drawable.daily);
                break;
            case "clothes":
                category.setText(R.string.clothes);
                icon= ContextCompat.getDrawable(mContext,R.drawable.clothes);
                break;
            case "vegetables":
                category.setText(R.string.vegetable);
                icon= ContextCompat.getDrawable(mContext,R.drawable.vegetable);
                break;
            case "fruit":
                category.setText(R.string.fruit);
                icon= ContextCompat.getDrawable(mContext,R.drawable.fruit);
                break;
            case "snack":
                category.setText(R.string.snack);
                icon= ContextCompat.getDrawable(mContext,R.drawable.snack);
                break;
            case "book":
                category.setText(R.string.book);
                icon= ContextCompat.getDrawable(mContext,R.drawable.book);
                break;
            case "study":
                category.setText(R.string.study);
                icon= ContextCompat.getDrawable(mContext,R.drawable.study);
                break;
            case "house":
                category.setText(R.string.house);
                icon= ContextCompat.getDrawable(mContext,R.drawable.house);
                break;
            case "investment":
                category.setText(R.string.investment);
                icon= ContextCompat.getDrawable(mContext,R.drawable.investment);
                break;
            case "social":
                category.setText(R.string.social);
                icon= ContextCompat.getDrawable(mContext,R.drawable.social);
                break;
            case "amusement":
                category.setText(R.string.amusement);
                icon= ContextCompat.getDrawable(mContext,R.drawable.amusement);
                break;
            case "makeup":
                category.setText(R.string.makeup);
                icon= ContextCompat.getDrawable(mContext,R.drawable.makeup);
                break;
            case "call":
                category.setText(R.string.call);
                icon= ContextCompat.getDrawable(mContext,R.drawable.call);
                break;
            case "sport":
                category.setText(R.string.sport);
                icon= ContextCompat.getDrawable(mContext,R.drawable.sport);
                break;
            case "travel":
                category.setText(R.string.travel);
                icon= ContextCompat.getDrawable(mContext,R.drawable.travel);
                break;
            case "medicine":
                category.setText(R.string.medicine);
                icon= ContextCompat.getDrawable(mContext,R.drawable.medicine);
                break;
            case "office":
                category.setText(R.string.office);
                icon= ContextCompat.getDrawable(mContext,R.drawable.office);
                break;
            case "digit":
                category.setText(R.string.digit);
                icon= ContextCompat.getDrawable(mContext,R.drawable.digit);
                break;
            case "gift":
                category.setText(R.string.gift);
                icon= ContextCompat.getDrawable(mContext,R.drawable.gift);
                break;
            case "repair":
                category.setText(R.string.repair);
                icon= ContextCompat.getDrawable(mContext,R.drawable.repair);
                break;
            case "wine":
                category.setText(R.string.wine);
                icon= ContextCompat.getDrawable(mContext,R.drawable.wine);
                break;
            case "redpacket":
                category.setText(R.string.redpacket);
                icon= ContextCompat.getDrawable(mContext,R.drawable.redpacket);
                break;
            case "other":
                category.setText(R.string.other);
                icon= ContextCompat.getDrawable(mContext,R.drawable.other);
                break;
            case "parttime":
                category.setText(R.string.parttime);
                icon= ContextCompat.getDrawable(mContext,R.drawable.parttime);
                break;
            case "salary":
                category.setText(R.string.salary);
                icon= ContextCompat.getDrawable(mContext,R.drawable.salary);
                break;
        }
        icon.setBounds(0,0,icon.getMinimumWidth(),icon.getMinimumHeight());
        category.setCompoundDrawables(null,icon,null,null);

    }
    private void setListener(){
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", changed);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });
        InOrOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,"hi",Toast.LENGTH_SHORT).show();
                if(isEdit)
                    popupMenu.show();
            }
        });
        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){//编辑态, 点击保存
                    isEdit=false;
                    editBTN.setText(R.string.edit);
                    time.setKeyListener(null);
                    time.setEnabled(false);
                    amount.setKeyListener(null);
                    amount.setEnabled(false);
                    place.setKeyListener(null);
                    place.setEnabled(false);
                    note.setKeyListener(null);
                    note.setEnabled(false);
                    InOrOut.setCompoundDrawables(null, null, null, null);
                    save();
                }
                else {//
                    isEdit = true;
                    editBTN.setText(R.string.save);
                    time.setKeyListener(timeListener);
                    time.setEnabled(true);
                    amount.setKeyListener(amountListener);
                    amount.setEnabled(true);
                    place.setKeyListener(placeListener);
                    place.setEnabled(true);
                    note.setKeyListener(noteListener);
                    note.setEnabled(true);
                    arrow.setBounds(0, 0, arrow.getMinimumWidth(), arrow.getMinimumHeight());
                    InOrOut.setCompoundDrawables(null, null, arrow, null);
                }
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed=true;
                //DataSupport.deleteAll(Bills.class,"id = ?", String.valueOf(BillId));
                ContentValues values = new ContentValues();
                values.put("state", 3);
                DataSupport.update(Bills.class, values, BillId);
                Toast.makeText(BillDetail.this,"已删除",Toast.LENGTH_SHORT).show();
                try {
                    UpdateData.UploadBill();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("result", changed);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });
    }
    private void save(){
        String newInOrOut, newTime, newPlace, newNote;
        double newAmount;
        newInOrOut = InOrOut.getText().toString();
        newTime = time.getText().toString();
        newNote = note.getText().toString();
        newPlace = place.getText().toString();
        newAmount = Double.valueOf(amount.getText().toString());
        Log.e("inorout",newInOrOut);
        Bills newBill = new Bills();
        newBill.setInOrOut(newInOrOut.equals("收入")?"in":"out");
        newBill.setDate(newTime);
        newBill.setNote(newNote);
        newBill.setPlace(newPlace);
        newBill.setAmount(newAmount);
        newBill.setState(2);
        //TODO:去除注释，加上信息
        //newBill.setMethods();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        newBill.setTimeStamp(time);
        newBill.update(BillId);
        try {
            UpdateData.UploadBill();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //DataSupport.update(Bills.class, values, BillId);
        changed=true;
        //TODO: 在数据库中修改

        Toast.makeText(this,"已保存",Toast.LENGTH_SHORT).show();
    }
}
