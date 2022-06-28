package com.hornedheck.vkpicshare

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL


const val DOWNLOAD_OK = 1
const val DOWNLOAD_FAIL = 0


class PostLoadingHandler(private val link: String, callback: Handler.Callback) :
    HandlerThread(link) {

    private val handler: Handler

    init {
        start()
        Log.d("VKPICS", "Started: $link")
        handler = Handler(looper, callback)
        handler.post(this::loadLink)
    }

    private fun loadLink() {
        try {
            BufferedReader(InputStreamReader(URL(link).openStream())).use {
                Log.d("VKPICS", "Loading: $link")
                handler.sendMessage(Message.obtain(handler, DOWNLOAD_OK, it.readText()))
                Log.d("VKPICS", "Loaded: $link")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            handler.sendMessage(Message.obtain(handler, DOWNLOAD_FAIL))
        }
    }


}