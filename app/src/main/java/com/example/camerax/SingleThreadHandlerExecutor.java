package com.example.camerax;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

final class SingleThreadHandlerExecutor implements Executor {

    private final String mThreadName;
    private final HandlerThread mHandlerThread;
    private final Handler mHandler;

    SingleThreadHandlerExecutor(@NonNull String threadName, int priority) {
        this.mThreadName = threadName;
        mHandlerThread = new HandlerThread(threadName, priority);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @NonNull
    Handler getHandler() {
        return mHandler;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        if (!mHandler.post(command)) {
            throw new RejectedExecutionException(mThreadName + " is shutting down.");
        }
    }

    boolean shutdown() {
        return mHandlerThread.quitSafely();
    }
}