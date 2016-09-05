package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zs.application.MyAplication;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class MyBalanceActivity extends AppCompatActivity {

    private static final String TAG = "MyBalanceActivity";
    private TextView tv_mybalanceactivity_choiceDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);
        getSupportActionBar().hide();

        tv_mybalanceactivity_choiceDate = (TextView) findViewById(R.id.tv_mybalanceactivity_choiceDate);
        //从sp文件中取出已经选择是日期数据，用于回显
        String choiceDate = MyAplication.getStringFromSp("choiceDate");
        if(!choiceDate.isEmpty()){
            tv_mybalanceactivity_choiceDate.setText(choiceDate);
        }
    }

    /**
     * TextView点击事件，弹出日期选择器，获取日期
     * @param view
     */
    public void settingtime(View view){

        startActivityForResult(new Intent(MyBalanceActivity.this,ChoiceDateActivity.class),100);
    }

    /**
     * 获取从下个页面传回来的日期信息，并修改TextView
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String choiceDate = data.getStringExtra("choiceDate");
        Log.i(TAG,"requestCode="+requestCode+" resultCode="+resultCode+" choiceDate="+choiceDate);

        if(requestCode==100&&resultCode==200 && !choiceDate.isEmpty()){
            tv_mybalanceactivity_choiceDate.setText(choiceDate);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void back(View view){
        finish();
    }
}
