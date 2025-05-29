package com.example.usuario.bilhete1;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.example.usuario.bilhete1.Utils.PrintfManager;
import com.example.usuario.bilhete1.Utils.SharedPreferencesManager;
import com.example.usuario.bilhete1.Utils.StaticVar;
import com.example.usuario.bilhete1.Utils.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private ExecutorService cachedThreadPool = null;
    private Handler handler = new Handler();
    private static MyApplication instance = null;

    public static MyApplication getInstance() {
        if (instance == null) {
            Log.e(TAG, "Instância de MyApplication é null. Certifique-se de que MyApplication está registrado no AndroidManifest.xml");
            throw new IllegalStateException("MyApplication não inicializado");
        }
        return instance;
    }

    public ExecutorService getCachedThreadPool() {
        if (cachedThreadPool == null) {
            Log.w(TAG, "cachedThreadPool é null, inicializando novamente");
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        return cachedThreadPool;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication onCreate chamado");
        instance = this;
        cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                    if (bitmap == null) {
                        Log.e(TAG, "Falha ao carregar R.mipmap.logo");
                        return;
                    }
                    int left = PrintfManager.getCenterLeft(72, bitmap);
                    byte[] bytes = PrintfManager.bitmap2PrinterBytes(bitmap, left);
                    StaticVar.bitmap_80 = bytes;
                    left = PrintfManager.getCenterLeft(48, bitmap);
                    bytes = PrintfManager.bitmap2PrinterBytes(bitmap, left);
                    StaticVar.bitmap_58 = bytes;
                    Log.d(TAG, "Bitmaps carregados com sucesso: bitmap_80=" + (StaticVar.bitmap_80 != null) + ", bitmap_58=" + (StaticVar.bitmap_58 != null));
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao processar bitmaps: " + e.getMessage());
                }
            }
        });
        Util.changLanguage(instance, SharedPreferencesManager.getLanguageId(instance));
    }
}