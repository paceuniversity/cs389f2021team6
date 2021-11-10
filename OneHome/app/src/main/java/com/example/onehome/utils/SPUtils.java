package com.example.onehome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SPUtils {

    private static SPUtils instance;
    private Context mCtx;

    public String mFileName = "share_data";

    private SPUtils(Context context, String fileName) {
        mCtx = context;
        this.mFileName = fileName;
    }

    public static SPUtils init(Context context, String filename) {
        if (instance == null) {
            synchronized (SPUtils.class) {
                if (instance == null) {
                    instance = new SPUtils(context, filename);
                }
            }
        }
        return instance;
    }

    public static SPUtils getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "you should can getInstance(Context context, String filename");
        }
        return instance;
    }

    public void put(String key, Object object) {
        SharedPreferences sp = mCtx.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        }else if(object instanceof Long) {
            editor.putLong(key, (Long) object);
        }else{
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public Object get(String key, Object defaultObject) {
        SharedPreferences sp = mCtx.getSharedPreferences(mFileName, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        }else if(defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }



    public void remove(String key){
        SharedPreferences sp = mCtx.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }
    private static class SharedPreferencesCompat{
        private static final Method sApplyMethod = findApplyMethod();


        private static Method findApplyMethod(){
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void apply(SharedPreferences.Editor editor){
            try{
                if(sApplyMethod != null){
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e){
            }catch (IllegalAccessException e){
            }catch (InvocationTargetException e){
            }
            editor.commit();
        }
    }
}
