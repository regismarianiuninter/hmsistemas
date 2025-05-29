package com.example.usuario.bilhete1.Utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.usuario.bilhete1.Utils.Util;

public class SharedPreferencesManager {

    public static String getBluetoothName(Context context) {
        SharedPreferences test = getSharedPreferences(context);
        return test.getString("blueName", null);
    }

    public static void saveBluetoothName(Context context, String name) {
        SharedPreferences test = getSharedPreferences(context);
        test.edit().putString("blueName", name).commit();
    }

    public static int getLanguageId(Context context) {
        String able= context.getResources().getConfiguration().locale.getCountry();
        if(able.equals("CN")){
            return getSharedPreferences(context).getInt("languageId", Util.Language.zh);
        }
        if(able.equals("TW")||able.equals("HK")||able.equals("MO")){
            return getSharedPreferences(context).getInt("languageId", Util.Language.cht);
        }
        return getSharedPreferences(context).getInt("languageId", Util.Language.en);
    }

    public static void saveLanguageId(Context context, int id) {
        getSharedPreferences(context).edit().putInt("languageId", id).commit();
    }

    /**
     * 保持本次连接的蓝牙地址
     *
     * @param context
     */
    public static void saveBluetoothAddress(Context context, String address) {
        SharedPreferences blue = getSharedPreferences(context);
        blue.edit().putString("blueAddress", address).commit();
    }

    /**
     * 获取上次连接的蓝牙地址
     *
     * @param context
     * @return
     */
    public static String getBluetoothAddress(Context context) {
        SharedPreferences blue = getSharedPreferences(context);
        return blue.getString("blueAddress", null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("TEST", Context.MODE_PRIVATE);
    }
}
