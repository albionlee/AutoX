package com.stardust.autojs.util;

import android.os.Looper;

import com.stardust.autojs.ocr.IOcrInstance;
import com.stardust.autojs.ocr.PaddleOcrInstance;

/**
 * Created by linke on 2021/12/10
 */
public class OcrHelper {

    private final IOcrInstance mOcrInstance;

    private boolean isInitialized;

    private OcrHelper() {
        mOcrInstance = new PaddleOcrInstance();
    }

    private static class Holder {
        private static final OcrHelper INSTANCE = new OcrHelper();
    }

    public static OcrHelper getInstance() {
        return Holder.INSTANCE;
    }

    private synchronized void init() {
        mOcrInstance.init();
    }

    public synchronized void initIfNeeded(InitializeCallback callback) {
        if (isInitialized) {
            if (callback != null) {
                callback.onInitFinish();
                return;
            }
        }
        isInitialized = true;
        if (Looper.getMainLooper() == Looper.myLooper()) {
            new Thread(() -> {
                init();
                if (callback != null) {
                    callback.onInitFinish();
                }
            }).start();
        } else {
            init();
            if (callback != null) {
                callback.onInitFinish();
            }
        }
    }

    public IOcrInstance getOcrInstance() {
        return mOcrInstance;
    }

    public synchronized void end() {
        mOcrInstance.end();
    }


    public interface InitializeCallback {
        void onInitFinish();
    }
}