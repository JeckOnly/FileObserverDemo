package com.luwei.fileobeserverdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.luwei.fileobeserverdemo.ui.theme.FileObeserverDemoTheme
import com.luwei.fileobeserverdemo.util.LogToFileUtil
import com.luwei.fileobeserverdemo.util.RecursiveFileObserver


class MainActivity : ComponentActivity() {

    private lateinit var listener: RecursiveFileObserver//保持对listener的引用
    private val deleteCount = mutableStateOf(0)
    private val speed = mutableStateOf(0f)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results: Map<String, Boolean> ->
            var notGrantedPermission = ""
            for (result in results) {
                if (result.value == false) notGrantedPermission+=result.key
            }
            if (!(notGrantedPermission == ""))
                Toast.makeText(this, notGrantedPermission + "没有被允许", Toast.LENGTH_SHORT).show()//有没被允许的就不跳转
            else {
                startWatchingRecursive(this)//所有都被允许了就跳转
                speed.value = 1.5f
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(this)
        setContent {
            FileObeserverDemoTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        LottieLoadingView(id = R.raw.rocket, modifier = Modifier.size(200.dp), speed = speed.value)
                        Button(
                            onClick = {
                                checkAndRequestPermissions(this@MainActivity)
                            },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(text = "启动文件删除检测")
                        }
                        Button(
                            onClick = {
                                stopWatchingRecursive()
                                speed.value = 0f
                            },
                            modifier = Modifier.padding(10.dp),
                        ) {
                            Text(text = "取消文件删除检测")
                        }
                        Text(text = "已往日志中增加 ${deleteCount.value} 处文件删除")
                    }
                }
            }
        }
    }

    private fun init(context: Context) {
        val path = Environment.getExternalStorageDirectory().absolutePath
        listener = RecursiveFileObserver(path)
        LogToFileUtil.init(context)
    }

    private fun startWatchingRecursive(context: Context) {
        listener.startWatching(context)
        Log.d("Jeck", "已启动")
    }

    private fun stopWatchingRecursive() {
        listener.stopWatching()
        Log.d("Jeck", "已关闭")
    }

    fun addDeleteCount() {
        deleteCount.value++
    }

    fun checkAndRequestPermissions(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context!!,//2：填入当前申请权限所在的context实例
                Manifest.permission.WRITE_EXTERNAL_STORAGE//
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context!!,//2：填入当前申请权限所在的context实例
                Manifest.permission.READ_EXTERNAL_STORAGE//
            ) == PackageManager.PERMISSION_GRANTED
        ) {
           startWatchingRecursive(context)
            speed.value = 1.5f
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(//4：这个参数是定义在activity或fragment中的字段，稍后介绍
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)//5:和1的权限一样
            )
        }
    }
}


@Composable
fun LottieLoadingView(
    id: Int,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(id))
    LottieAnimation(
        composition,
        modifier = modifier.defaultMinSize(300.dp),
        iterations = iterations,
        speed = speed
    )
}


