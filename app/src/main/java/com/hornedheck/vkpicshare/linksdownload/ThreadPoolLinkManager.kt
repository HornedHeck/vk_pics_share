package com.hornedheck.vkpicshare.linksdownload

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.bumptech.glide.Glide
import com.hornedheck.vkpicshare.ParserImpl
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.min


private const val DOWNLOAD_OK = 1
private const val DOWNLOAD_FAIL = 0
private const val TAG = "VKPIC_TPLM"

internal class ThreadPoolLinkManager(
    linksList: List<String>,
    finishedCallback: (List<Uri>) -> Unit,
    context: Context
) :
    LinkDownloadManager(ParserImpl(), finishedCallback, linksList) {


    private val poolExecutor: ThreadPoolExecutor

    private val imageUris = mutableListOf<Uri>()
    private val collectorHandler = Handler(Looper.getMainLooper()) {
        if (it.what == DOWNLOAD_OK) {
            Glide.with(context)
                .downloadOnly()
                .load(it.obj as String)
                .into(FileTarget(context) { fileUri ->
                    imageUris.add(fileUri)
                    if (imageUris.size == linksList.size) {
                        finishedCallback(imageUris)
                    }
                })

        } else {
            Log.d(TAG, "Loading Error")
        }
        true
    }

    init {
        val tasks = List(linksList.size) {
            Runnable {
                createImageLink(linksList[it])?.let {
                    collectorHandler.sendMessage(
                        Message.obtain(
                            collectorHandler,
                            DOWNLOAD_OK,
                            it
                        )
                    )
                } ?: run {
                    collectorHandler.sendMessage(
                        Message.obtain(
                            collectorHandler,
                            DOWNLOAD_FAIL
                        )
                    )
                }
            }
        }

        poolExecutor = ThreadPoolExecutor(
            min(Runtime.getRuntime().availableProcessors(), linksList.size),
            linksList.size,
            30L,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(linksList.size, false, tasks)
        )
        poolExecutor.prestartCoreThread()
    }


}