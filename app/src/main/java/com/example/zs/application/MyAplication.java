package com.example.zs.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.zs.bean.WishInfo;
import com.example.zs.pager.BasePager;

/**
 * Created by wuqi on 2016/9/4 0004.
 * 用于创建应用时的初始化一些供全局使用的参数
 *
 */
public class MyAplication extends Application{

    public static SharedPreferences sp;
    public static WishInfo wishInfo;
    public static SharedPreferences UserInfosp;
    public static SharedPreferences CurUsersp;
    private BasePager accountPager;


    private BasePager wishPager;
    private BasePager ownerPager;
    private BasePager reportFormPager;



    private static float mCurrentMonthCost;
    private static float mCurrentMonthIcome;


    private static float mCurrentBudget;
    /**
     * 应用创建时调用oncreate（）
     */
    @Override
    public void onCreate() {
        sp = getSharedPreferences("MyAccount", MODE_PRIVATE);
        UserInfosp = getSharedPreferences("config",MODE_PRIVATE);
        CurUsersp = getSharedPreferences("currentUser",MODE_PRIVATE);
        super.onCreate();
    }

    /**
     * 保存字符串到SharedPreferences
     * @param key
     * @param value
     */
    public static void saveStringToSp(String key,String value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    /**
     * 从SharedPreferences获取字符串
     * @param name
     * @return
     */
    public static String getStringFromSp(String name){
        return   sp.getString(name,"");
    }

    /**
     * 保存Boolean值到SharedPreferences
     * @param key
     * @param value
     */
    public static void saveBooleanToSp(String key,Boolean value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    /**
     * 从SharedPreferences获取Boolean值
     * @param name
     * @return
     */
    public static Boolean getBooleanFromSp(String name){
        return   sp.getBoolean(name,false);
    }

    public static void saveIntToSp(String key,int value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key,value);
        edit.commit();
    }
    public static int getIntFromSp(String name){
        return   sp.getInt(name,0);
    }


    public static void setWishInfo(WishInfo info){
        wishInfo = info;
    }

    public static WishInfo getWishInfo(){
        return wishInfo;
    }

    /**
     * 保存用户名和与用户相关的信息到SharedPreferences,用户名为键，密码为值
     * @param username  用户名
     * @param key  密码、或用户注册时间
     */
    public static void saveUserInfoToSp(String username,String key){
        SharedPreferences.Editor edit = UserInfosp.edit();
        edit.putString(username,key);
        edit.commit();
    }

    /**
     * 根据用户名从SharedPreferences获取密码的字符串
     * @param username
     * @return
     */
    public static String getUserInfoFromSp(String username){
        return   UserInfosp.getString(username,"");
    }

    /**
     * 保存当前用户名到sp
     * @param key  用户名
     * @param value  密码
     */
    public static void saveCurUsernaemToSp(String key,String value){
        SharedPreferences.Editor edit = CurUsersp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    /**
     * 获取当前用户名
     * @param key
     * @return
     */
    public static String getCurUsernameFromSp(String key){
        return   CurUsersp.getString(key,"");
    }

    public static String getCurUserRegisterDate(){
        String username = getCurUsernameFromSp("username");
        String curUserRegisterDate = getUserInfoFromSp(username + "registerDate");
        return curUserRegisterDate;
    }

    /**
     * 清除当前CurUsersp对象的Sharepreferences文件数据
     * @return
     */
    public static boolean clearData(){
        SharedPreferences.Editor edit = CurUsersp.edit();
        edit.clear();
        edit.commit();
        return true;
    }

    public BasePager getOwnerPager() {
        return ownerPager;
    }

    public void setOwnerPager(BasePager ownerPager) {
        this.ownerPager = ownerPager;
    }


    public BasePager getAccountPager() {
        return accountPager;
    }

    public void setAccountPager(BasePager accountPager) {
        this.accountPager = accountPager;
    }

    public BasePager getWishPager() {
        return wishPager;
    }

    public void setWishPager(BasePager wishPager) {
        this.wishPager = wishPager;
    }

    public BasePager getReportFormPager() {
        return reportFormPager;
    }

    public void setReportFormPager(BasePager reportFormPager) {
        this.reportFormPager = reportFormPager;
    }
    public static float getmCurrentMonthCost() {
        return mCurrentMonthCost;
    }

    public static void setmCurrentMonthCost(float CurrentMonthCost) {
        mCurrentMonthCost = CurrentMonthCost;
    }

    public static  float getmCurrentMonthIcome() {
        return mCurrentMonthIcome;
    }

    public static void setmCurrentMonthIcome(float currentMonthIcome) {
        mCurrentMonthIcome = currentMonthIcome;
    }

    public static float getmCurrentBudget() {
        return mCurrentBudget;
    }

    public static void setmCurrentBudget(float mCurrentBudget) {
        MyAplication.mCurrentBudget = mCurrentBudget;
    }
}
