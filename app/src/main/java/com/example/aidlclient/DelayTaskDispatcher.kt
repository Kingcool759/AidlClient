package com.example.aidlclient

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import java.util.*

/**
 * @Author : zhaojianwei02
 * @Date : 2022/8/31 2:36 下午
 * @Description : 利用IdleHandler实现延迟任务分发，用于保证mDownloadBinder?.start()时binder不为null
 */
class DelayTaskDispatcher {
    private val delayTasks: Queue<Task> = LinkedList()

    private val idleHandler = IdleHandler {
        if (delayTasks.size > 0) {
            val task = delayTasks.poll()
            task?.run()
        }
        !delayTasks.isEmpty() //delayTasks非空时返回ture表示下次继续执行，为空时返回false系统会移除该IdleHandler不再执行
    }

    fun addTask(task: Task): DelayTaskDispatcher {
        delayTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(idleHandler)
    }
}