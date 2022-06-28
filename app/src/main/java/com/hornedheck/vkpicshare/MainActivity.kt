package com.hornedheck.vkpicshare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.hornedheck.vkpicshare.linksdownload.ThreadPoolLinkManager


private val TAG = "VKPICS"

class MainActivity : Activity() {

    private val parser: Parser by lazy { ParserImpl() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PostLoadingHandler(intent.getStringExtra(Intent.EXTRA_TEXT) ?: "", postLoadingHandler)
    }

    private val postLoadingHandler = Handler.Callback {
        if (it.what == DOWNLOAD_OK) {
            val links = parser.getPostLinks(it.obj as String)
            Log.d(TAG, links.toString())
            initLinkDownloadManager(links)
        } else {
            Log.d(TAG, getString(R.string.error_toast))
        }
        true
    }

    private fun initLinkDownloadManager(links: List<String>) {
        ThreadPoolLinkManager(links, this::imageLinksLoadedCallback, baseContext)
    }

    private fun imageLinksLoadedCallback(imageUris: List<Uri>) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(imageUris))
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun onResume() {
        super.onResume()
        finish()
    }
}