package com.example.zs.myaccount;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.application.MyAplication;
import com.example.zs.pager.AccountPager;
import com.example.zs.utils.KeyboardUtil;
import com.example.zs.view.DynamicWave;
import com.example.zs.view.WaterWaveView;
import com.example.zs.view.WaveHelper;
import com.example.zs.view.WaveView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  Shine-Zhang
 */
public class ShowBudgetStateAcivity extends Activity implements View.OnClickListener{

    private ImageButton ibShowBudgetStaBack;
    private ImageButton ibShowBudgetStaSetting;
    private ViewPager mVpShowBudgetSta;
    private List<View> pagers;
    private float currentRatio;
    private EditText et_show_budeget_sta_setbudget;
    private EditText et_show_budeget_sta_set_deadline;
    private RecyclerView.ViewHolder mViewHolder;
    HashMap<RecyclerView.ViewHolder,Integer> mMapping;
    private String last;
    private String[] daysOfMonth;
    private RecyclerView rv_show_budget_sta_recyclerview;
    private boolean isfold=true;
    private TextView tvShowBudgetStateActivityNumTip;
    private int adapterPosition;
    private int monthdays;
    private Calendar calendar;
    private String[] normalDays;
    private String[] abNormalDays;
    private String[] normal2Days;
    private String[] special2Days;
    private Calendar now;
    private float income;
    private float balance;
    public KeyboardUtil keyboardUtil;
    private WaveView myWave;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;
    private WaveHelper mWaveHelper;
    private OnBudgetChangedListener mOnBudgetChangedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_budget_state_acivity);

        Intent intent = getIntent();
        //如果没有拿到从
        currentRatio = intent.getFloatExtra("currentHight",-1);
        income = intent.getFloatExtra("totalIncome",-1);
        balance = intent.getFloatExtra("balance",0);
        // Log.i("haha","!!!!!!!!!!!!!!!!! :"+currentRatio);

        mVpShowBudgetSta = (ViewPager) findViewById(R.id.vp_show_budget_sta);

        pagers = new ArrayList<>();
        pagers.add(View.inflate(this, R.layout.vp_show_budget_sta,null));
        pagers.add(View.inflate(this, R.layout.vp_show_budget_state_setting,null));

        mVpShowBudgetSta.setAdapter(new ContentAdapter());
        mVpShowBudgetSta.setCurrentItem(0);

        mMapping = new HashMap<>();


        //数据
        //获取当前的日期
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
        //获取当前月最后一天,即计算当前月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        last = dateFormater.format(calendar.getTime());
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        //本月天数
        String substring = last.substring(8,10);
        monthdays = Integer.parseInt(substring);

        normalDays = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28","29","30"};

        abNormalDays = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

        normal2Days = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28"};

        special2Days = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28","29"};

        daysOfMonth = bindDaysOfMonths(year, month);


        mVpShowBudgetSta.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String budget="";
                if(position==0){

                   if((!TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget")))) {
                       //Log.i("haha","***************************"+MyAplication.getStringFromSp("myBudget"));
                       budget =MyAplication.getStringFromSp("myBudget");
                       TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_num_tip);
                       text.setText(budget);
                   }else{

                       TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_num_tip);
                       text.setText("3000");
                   }

                       int year = now.get(Calendar.YEAR);
                       int month = now.get(Calendar.MONTH);
                       int day = now.get(Calendar.DAY_OF_MONTH);
                        String from = year+"-"+month+"-"+day;
                        if(month==12){
                            month =1;
                            year++;
                        }else{
                            month++;
                        }
                        String to = year+"-"+month+"-"+1;
                        String span = getSpan(from,to);
                         TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                        text.setText(span);
             /*       if(!TextUtils.isEmpty(MyAplication.getStringFromSp("budget_deadline"))) {
                        String budget_deadline =MyAplication.getStringFromSp("budget_deadline");


                        String src = "2016-09-10";
                        String des = "2016-09-"+budget_deadline;
                        int span = Integer.parseInt(getSpan(des,src));
                        if(span>=0){

                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }else{

                            des = "2016-10-"+budget_deadline;
                            span =  Integer.parseInt(getSpan(des,src));
                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }

                    }else{

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                        text.setText("未知");

                    }*/
                    TextView showRemain = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity);
                    float nowRemain = Float.parseFloat(budget) - MyAplication.getmCurrentMonthCost();
                    if(nowRemain>0){
                        Log.i("haha","()()()()"+nowRemain);
/*                        myWave.setWaterLevelRatio((float)1.0*nowRemain/Float.parseFloat(budget));
                        mWaveHelper.cancel();
                        myWave.setWaveColor(
                                WaveView.DEFAULT_BEHIND_WAVE_COLOR,
                                WaveView.DEFAULT_FRONT_WAVE_COLOR);
                        mBorderColor = Color.parseColor("#44FFFFFF");
                        myWave.setBorder(mBorderWidth, mBorderColor);
                        mWaveHelper = new WaveHelper(myWave,(float)1.0*nowRemain/Float.parseFloat(budget));
                        mWaveHelper.start();*/
                       // mWaveHelper.restart();
            /*            mWaveHelper = new WaveHelper(myWave,(float)1.0*nowRemain/Float.parseFloat(budget));
                        mWaveHelper.cancel();
                        mWaveHelper.start();*/
                        mWaveHelper.setmWaveHight((float)1.0*nowRemain/Float.parseFloat(budget));
                        //mWaveHelper.restart();
                        showRemain.setText(String.format("%.2f",nowRemain));
                        showRemain.setTextColor(Color.rgb(0,0,0));
                    }else{
/*                       myWave.setWaterLevelRatio(0);
                        mWaveHelper.cancel();
                        myWave.setWaveColor(
                                WaveView.DEFAULT_BEHIND_WAVE_COLOR,
                                WaveView.DEFAULT_FRONT_WAVE_COLOR);
                        mBorderColor = Color.parseColor("#44FFFFFF");
                        myWave.setBorder(mBorderWidth, mBorderColor);
                        mWaveHelper = new WaveHelper(myWave,0);*/
                 /*       mWaveHelper.setmWaveHight(0);
                        mWaveHelper.restart();*/
                     //mWaveHelper.restart();
                        showRemain.setText("赤字啦！");
                        showRemain.setTextColor(Color.RED);
                    }

                }

                else if(position==1){

                    String myBudget="";
                    if((!TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget")))) {
                        myBudget =MyAplication.getStringFromSp("myBudget");
                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_setbudget);
                        text.setText(myBudget);
                    }else {

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_setbudget);
                        text.setText("3000");
                    }


                   /* if(!TextUtils.isEmpty(MyAplication.getStringFromSp("budget_deadline"))) {
                        String budget_deadline =MyAplication.getStringFromSp("budget_deadline");


                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_set_deadline);
                        text.setText(budget_deadline+"");
                    }else {

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_set_deadline);
                        text.setText("1");

                    }*/

                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private String[] bindDaysOfMonths(int year,int month) {
        String [] result = null;
        switch (month){
            case 1:
                result = abNormalDays;
                break;
            case 3:
                result = abNormalDays;
                break;
            case 5:
                result = abNormalDays;
                break;
            case 7:
                result = abNormalDays;
                break;
            case 8:
                result = abNormalDays;
                break;
            case 10:
                result = abNormalDays;
                break;
            case 12:
                result = abNormalDays;
                break;
            case 4:
                result =  normalDays;
                break;
            case 6:
                result =  normalDays;
                break;
            case 9:
                result =  normalDays;
                break;
            case 11:
                result =  normalDays;
                break;
            case 2:
                if(!isLeapYear( year)){

                    result =  normal2Days;

                }else{

                    result =  special2Days;
                }
        }

        return result;

    }

    public boolean  isLeapYear(int year){
        boolean ret = false;

        if((year%4==0&&year%100!=0)||(year%400==0)){
                ret = true;
        }


        return ret;
    }
    @Override
    public void onClick(View view) {
        String budget;
        String rule;
        String rule1;
        Pattern p1;
        Matcher m1;
        switch (view.getId()){

            case R.id.ib_show_budeget_sta_setting_back:

/*
                    MyAplication.saveStringToSp("budget_deadline",et_show_budeget_sta_set_deadline.getText().toString());

                budget =  et_show_budeget_sta_setbudget.getText().toString(); ;
                if(TextUtils.isEmpty(budget)){
                    Toast.makeText(ShowBudgetStateAcivity.this,"预算金额不能为空!",Toast.LENGTH_SHORT).show();
                }else {
                    rule = "^\\d{1,7}|^\\d{1,7}\\.\\d{1,2}";
                    Pattern p = Pattern.compile(rule);
                    Matcher m = p.matcher(budget);
                    if (m.matches()) {
                        MyAplication.saveStringToSp("myBudget", budget);

                    } else {

                        Toast.makeText(ShowBudgetStateAcivity.this, "请设置正确的预算!", Toast.LENGTH_SHORT).show();
                    }
                }
*/

                    mVpShowBudgetSta.setCurrentItem(0);
                    break;


                    case R.id.bt_show_budget_sta_finish:

                       // MyAplication.saveStringToSp("budget_deadline",et_show_budeget_sta_set_deadline.getText().toString());

                        budget = et_show_budeget_sta_setbudget.getText().toString();
                        Log.i("xuxu",budget);
                        if (TextUtils.isEmpty(budget)) {
                            Toast.makeText(ShowBudgetStateAcivity.this, "预算金额不能为空!", Toast.LENGTH_SHORT).show();
                        } else {
                            rule = "^\\d{1,7}|^\\d{1,7}\\.\\d{1,2}";

                            Pattern p = Pattern.compile(rule);
                            Matcher m = p.matcher(budget);
                            if (m.matches()) {
                                float tmp =Float.parseFloat(budget);
                                budget = String.format("%.2f",tmp);
                                MyAplication.saveStringToSp("myBudget", budget);
                                MyAplication.setmCurrentBudget(tmp);
                                //mVpShowBudgetSta.setCurrentItem(0);
                                MyAplication application = (MyAplication) getApplication();
                                AccountPager pager = (AccountPager) application.getAccountPager();
                                Log.i("ooooo",pager.toString());
                                if(pager!=null) {
                                    pager.myBudget =budget; ;
                                    pager.tvAccountPagerBudget.setText(String.format("%.2f",tmp - MyAplication.getmCurrentMonthCost()));
                                }
                                finish();
                            } else {

                                Toast.makeText(ShowBudgetStateAcivity.this, "请设置正确的预算!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        break;



           case  R.id.rl_show_budget_sta_set_budget:
               //弹出键盘
               if(!TextUtils.isEmpty(et_show_budeget_sta_setbudget.getText().toString())){
                   et_show_budeget_sta_setbudget.setText("");
               }
               int inputback = et_show_budeget_sta_setbudget.getInputType();
               et_show_budeget_sta_setbudget.setInputType(InputType.TYPE_NULL);
               keyboardUtil = new KeyboardUtil(this, this, et_show_budeget_sta_setbudget,true);
               keyboardUtil.setNumberFormat(7);
              // RelativeLayout parentView = (RelativeLayout) findViewById(R.id.bt_show_budget_sta_parent);
               keyboardUtil.showKeyboard(pagers.get(1));
               et_show_budeget_sta_setbudget.setInputType(inputback);

            break;



           case  R.id.rl_show_budeget_sta_set_deadline:
               //弹出日期选择的indicator
               if(isfold) {
                   rv_show_budget_sta_recyclerview.setVisibility(View.VISIBLE);
                   isfold = false;
               }else{
                   isfold = true;
                   rv_show_budget_sta_recyclerview.setVisibility(View.GONE);
               }
            break;

        }

    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagers.get(position));
            switch (position){
                case 0:

                    ibShowBudgetStaBack = (ImageButton) pagers.get(0).findViewById(R.id.ib_show_budeget_sta_back);
                    ibShowBudgetStaSetting = (ImageButton)pagers.get(0). findViewById(R.id.ib_show_budeget_sta_setting);
                    tvShowBudgetStateActivityNumTip = (TextView) pagers.get(0).findViewById(R.id.tv_show_budget_state_activity_num_tip);
                   // myWave = (DynamicWave) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.dynamicWave_show_budget_activity_mywave);
                   // myWave.setmCurentRatio(currentRatio);
                    myWave = (WaveView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.dynamicWave_show_budget_activity_mywave);
                    myWave.setShapeType(WaveView.ShapeType.CIRCLE);
//                    myWave.setmCurentRatio(currentRatio);



                    myWave.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            myWave.setWaveColor(
                                    WaveView.DEFAULT_BEHIND_WAVE_COLOR,
                                    WaveView.DEFAULT_FRONT_WAVE_COLOR);
                            mBorderColor = Color.parseColor("#44FFFFFF");
                            myWave.setBorder(mBorderWidth, mBorderColor);
                            if(balance>0){
                                mWaveHelper = new WaveHelper(myWave,currentRatio);
                            }else{
                                mWaveHelper = new WaveHelper(myWave,0);
                            }
                            mWaveHelper.start();
                        }
                    });
                    if(TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget"))){
                        //没数据就给默认值
                        tvShowBudgetStateActivityNumTip.setText("3000");

                    }else{

                        tvShowBudgetStateActivityNumTip.setText(MyAplication.getStringFromSp("myBudget"));
                    }
                    now = Calendar.getInstance();
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH);
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    String from = year+"-"+month+"-"+day;
                    if(month==12){
                        month =1;
                        year++;
                    }else{
                        month++;
                    }
                    String to = year+"-"+month+"-"+1;
                    String span = getSpan(from,to);
                    TextView text1 = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                    text1.setText(span);
                    TextView text2 = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity);
                    if(balance<0){
                        text2.setText("赤字啦！");
                        text2.setTextColor(Color.RED);
                    }else{

                        text2.setText(String.format("%.2f",balance));
                        text2.setTextColor(Color.rgb(0,0,0));
                    }

                    TextView text3 = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_income);
                    text3.setText(String.format("%.2f",income));
                  //  Log.i("hhhhhhh","************************"+span);
                  /*  if(!TextUtils.isEmpty(MyAplication.getStringFromSp("budget_deadline"))) {
                        String budget_deadline =MyAplication.getStringFromSp("budget_deadline");


                        String src = "2016-09-10";
                        String des = "2016-09-"+budget_deadline;
                        int span = Integer.parseInt(getSpan(des,src));
                        if(span>=0){

                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }else{

                             des = "2016-10-"+budget_deadline;
                            span =  Integer.parseInt(getSpan(des,src));
                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }

                    }else{

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                        text.setText("未知");

                    }*/


                    ibShowBudgetStaBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });

                    ibShowBudgetStaSetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //实现页面切换
                            mVpShowBudgetSta.setCurrentItem(1);
                        }
                    });

                    break;

                case 1:

                     ImageButton ib_show_budeget_sta_setting_back = (ImageButton) pagers.get(1).findViewById(R.id.ib_show_budeget_sta_setting_back);
                     Button bt_show_budget_sta_finish = (Button) pagers.get(1).findViewById(R.id.bt_show_budget_sta_finish);
                     RelativeLayout rl_show_budget_sta_set_budget = (RelativeLayout) pagers.get(1).findViewById(R.id.rl_show_budget_sta_set_budget);
                     RelativeLayout rl_show_budeget_sta_set_deadline = (RelativeLayout) pagers.get(1).findViewById(R.id.rl_show_budeget_sta_set_deadline);
                    et_show_budeget_sta_setbudget = (EditText) pagers.get(1).findViewById(R.id.et_show_budeget_sta_setbudget);
                    et_show_budeget_sta_set_deadline = (EditText) pagers.get(1).findViewById(R.id.et_show_budeget_sta_set_deadline);
                    rv_show_budget_sta_recyclerview = (RecyclerView) pagers.get(1).findViewById(R.id.rv_show_budget_sta_recyclerview);

                    ib_show_budeget_sta_setting_back.setOnClickListener(ShowBudgetStateAcivity.this);
                    bt_show_budget_sta_finish.setOnClickListener(ShowBudgetStateAcivity.this);
                    rl_show_budget_sta_set_budget.setOnClickListener(ShowBudgetStateAcivity.this);
                    //rl_show_budeget_sta_set_deadline.setOnClickListener(ShowBudgetStateAcivity.this);
                    et_show_budeget_sta_setbudget.setOnClickListener(ShowBudgetStateAcivity.this);
                    et_show_budeget_sta_set_deadline.setOnClickListener(ShowBudgetStateAcivity.this);
/*                     String deadLine = MyAplication.getStringFromSp("budget_deadline");
                    if(TextUtils.isEmpty(deadLine)){
                        et_show_budeget_sta_set_deadline.setText("1");
                    }else{
                        et_show_budeget_sta_set_deadline.setText(deadLine+"");
                    }*/
//                    et_show_budeget_sta_setbudget.
                    //创建默认的线性LayoutManager
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ShowBudgetStateAcivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rv_show_budget_sta_recyclerview.setLayoutManager(layoutManager);
                    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                    rv_show_budget_sta_recyclerview.setHasFixedSize(true);
                    //初始化自定义的适配器
                    final MyAdapter myAdapter = new MyAdapter(daysOfMonth);
                    //为rcv_wishpager_wishes设置适配器
                    rv_show_budget_sta_recyclerview.setAdapter(myAdapter);
                    //给拿到RecyclerView添加条目点击事件
                    rv_show_budget_sta_recyclerview.addOnItemTouchListener(new OnItemTouchListener(rv_show_budget_sta_recyclerview) {
                        @Override
                        public void onItemClick(RecyclerView.ViewHolder vh) {
                            //点击时做的事
                            adapterPosition = vh.getAdapterPosition();
                            et_show_budeget_sta_set_deadline.setText(daysOfMonth[adapterPosition]);
                            View tv_itemsetbudgetdays_day = vh.itemView.findViewById(R.id.tv_itemsetbudgetdays_day);
                            tv_itemsetbudgetdays_day.setEnabled(false);
                           // Toast.makeText(ShowBudgetStateAcivity.this, "click"+daysOfMonth[adapterPosition], Toast.LENGTH_SHORT).show();
                            if(mViewHolder!=null){
                              //  Log.i("haha","mViewHoder="+mViewHolder);
                                TextView tmpTextView = (TextView) mViewHolder.itemView.findViewById(R.id.tv_itemsetbudgetdays_day);
                                tmpTextView.setEnabled(true);
                                myAdapter.notifyItemChanged(mMapping.get(mViewHolder));
                             //   Log.i("haha","mPosition: "+mViewHolder.getAdapterPosition()+"---"+"position: "+ adapterPosition);
                            }

                            mViewHolder = vh;
                            mMapping.put(vh, adapterPosition);
                        }
                    });

                    break;


            }
            return pagers.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


    /**
     * 该类为RecyclerView的Adapter
     * RecyclerView与ListView类似，都需要Adapter
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        /**
         * 这里创建一个数组，准备接收传过来的数据
         */
        public String[] datas = null;

        //构造方法

        /**
         * 这里调用在创建MyAdapter实例的时候，可以将数据传过来
         *
         * @param datas
         */
        public MyAdapter(String[] datas) {
            this.datas = datas;
            Log.i("TAG","datas="+datas.length);
        }

        //创建新View，被LayoutManager所调用
        /**
         * 这里加载加载Item，并且创建ViewHolder对象，把加载的Item（View）传给viewholder
         *
         * @param viewGroup
         * @param viewType
         * @return
         */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_setbudgetdays, viewGroup, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;
        }

        //将数据与界面进行绑定的操作

        /**
         * 这里给item中的子View绑定数据
         *
         * @param viewHolder
         * @param position
         */
        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int position) {
            // 给ViewHolder设置元素
            String s = datas[position];
            viewHolder.tv_itemsetbudgetdays_day.setText(s);

        }

        //获取数据的数量

        /**
         * 这里返回item数量
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return datas.length;
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        /**
         * ViewHolder类，注意要继承RecyclerView.ViewHolder
         */
        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_itemsetbudgetdays_day;

            public MyViewHolder(View view) {
                super(view);
                tv_itemsetbudgetdays_day = (TextView) view.findViewById(R.id.tv_itemsetbudgetdays_day);
            }
        }

    }

/*    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }*/


    public static  String  getSpan(String from, String to) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date fromDate = myFormatter.parse(from);
            java.util.Date toDate = myFormatter.parse(to);
            day = (toDate.getTime() - fromDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    @Override
    protected void onDestroy() {
        if(keyboardUtil!=null) {
            if (!keyboardUtil.isNormal) {
                keyboardUtil.hideKeyboardAsNormal();
                }
            }else{
                if(keyboardUtil!=null) {
                    keyboardUtil.hideKeyboardAsNormal();
                }
            }
        super.onDestroy();
        }

    public interface OnBudgetChangedListener{

        void onBudgetChanged(AccountPager page);
    }

    public void setOnBudgetChangedListener(OnBudgetChangedListener onBudgetChangedListener){
        this.mOnBudgetChangedListener = onBudgetChangedListener;
    }

    }


/**
 * RecyclerView的触摸监听
 * 重写了RecyclerView.OnItemTouchListener的四个方法
 * 加了一个抽象方法，可以自行处理当前点击该Item
 */
abstract class ShowBudgetStateOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    public ShowBudgetStateOnItemTouchListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                new MyGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);

    /**
     * 手势判断
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                onItemClick(VH);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                onItemClick(VH);
            }
        }
    }



}




