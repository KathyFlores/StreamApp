package stream.com.streamapp.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import stream.com.streamapp.R;

public class BillDetail extends AppCompatActivity {
    private int BillId;
    private Context mContext;
    private ImageView backBTN=null;
    private TextView category = null;
    private TextView InOrOut = null;
    private TextView amount = null;
    private TextView note = null;
    private TextView time = null;
    private TextView place = null;
    private Drawable icon;
    private double mAmount;
    private String mInOrOut,mNote,mTime,mPlace,mCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);
        Bundle extras = getIntent().getExtras();
        BillId=extras.getInt("BillId");
        Log.e("BillId:",""+BillId);
        initView();
    }
    private void initView(){
        backBTN = (ImageView)findViewById(R.id.back_button);
        category = (TextView)findViewById(R.id.category);
        mContext=getApplicationContext();
        InOrOut = (TextView)findViewById(R.id.InOrOut);
        amount = (TextView)findViewById(R.id.amount);
        note = (TextView)findViewById(R.id.note);
        time = (TextView)findViewById(R.id.time);
        place = (TextView)findViewById(R.id.place);
        //TODO:根据billid维护以下数据
        mAmount = 0;
        mInOrOut = "out";
        mNote = "note";
        mPlace= "place";
        mTime = "2017/12/25";
        mCategory = "meal";

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
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
