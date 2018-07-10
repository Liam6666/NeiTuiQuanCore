package com.neituiquan.utils;


import android.os.Handler;

import java.util.HashMap;


/** 特别NB的循环任务工具 */
public class TaskLooper {



    public interface TaskCompleteCallback{
        void onComplete();
    }
    public interface Task{
        void run(TaskCompleteCallback callback);
    }

    private static final HashMap<Task, Handler> tasks = new HashMap<>();

    private static boolean existsTask(Task task){
        return tasks.containsKey(task);
    }

    public static void bindTask(final Task task, final long taskDuration){
        if(android.os.Looper.myLooper() != android.os.Looper.getMainLooper())
            throw new IllegalStateException("fuck you");
        if(tasks.containsKey(task))
            throw new IllegalStateException("fuck you");
        final Handler handler = new Handler();
        tasks.put(task, handler);
        TaskCompleteCallback callback = new TaskCompleteCallback() {
            public void onComplete() {
                if(!existsTask(task))
                    return ;
                final TaskCompleteCallback self = this;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(!existsTask(task))
                            return ;
                        task.run(self);
                    }
                }, taskDuration);
            }
        };
        task.run(callback);
    }

    public static void unBind(Task task){
        Handler handler = tasks.remove(task);
        if(handler != null)
            handler.removeCallbacksAndMessages(null);
    }


}
