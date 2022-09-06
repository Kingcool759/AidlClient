package com.example.appmanager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.aidlserver.IMyAidlInterface

/**
 * @Author : zhaojianwei02
 * @Date : 2022/9/1 11:10 上午
 * @Description : 管理app
 */
class AppManager {

    companion object{
        private val TAG = AppManager::class.java.name.toString()
    }

    private lateinit var ipcAidl: IMyAidlInterface

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            ipcAidl = IMyAidlInterface.Stub.asInterface(iBinder)
            Log.d(TAG, "#onServiceConnected")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(TAG, "#onServiceDisconnected")
        }
    }

    fun bindRemoteService(ctx: Context) {
        val mIntent = Intent()
        mIntent.action = "com.example.aidlserver.RemoteService"
        mIntent.component = ComponentName("com.example.aidlserver", "com.example.aidlserver.RemoteService")
        val bindFlag = ctx.bindService(mIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "#bindService bingFlag = $bindFlag")
    }
}