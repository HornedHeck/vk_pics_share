package com.hornedheck.vkpicshare.linksdownload

import android.net.Uri
import com.hornedheck.vkpicshare.Parser
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

abstract class LinkDownloadManager(
    private val parser: Parser,
    protected val finishedCallback: (List<Uri>) -> Unit,
    protected val linksList: List<String>
) {

    private fun loadLink(link: String): String? {
        try {
            BufferedReader(InputStreamReader(URL(link).openStream())).use {
                return it.readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    protected fun createImageLink(link: String) = loadLink(link)?.let(parser::getImageLink)


}