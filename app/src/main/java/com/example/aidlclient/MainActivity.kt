package com.example.aidlclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.aidlserver.IMyAidlInterface

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.name.toString()

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

    private fun bindService() {
        val mIntent = Intent()
        mIntent.action = "com.example.aidlserver.RemoteService"
        mIntent.component = ComponentName("com.example.aidlserver", "com.example.aidlserver.RemoteService")
        val bindFlag = bindService(mIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "#bindService bingFlag = $bindFlag")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 绑定远程服务
        findViewById<Button>(R.id.btn_send).setOnClickListener {
            bindService()
            // 在ipcAidl不为null时调用执行。
            DelayTaskDispatcher().addTask(object : Task{
                override fun run() {
                    // 展示服务端数据
                    findViewById<TextView>(R.id.tv_Show).text = "从服务端获取到数据 = ${ipcAidl?.getValue(100)}"
                }
            }).start()
        }
    }
}