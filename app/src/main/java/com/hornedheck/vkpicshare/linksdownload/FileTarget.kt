package com.hornedheck.vkpicshare.linksdownload

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.FileProvider
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

class FileTarget(
    private val context: Context,
    private val proceedUri: (Uri) -> Unit
) : CustomTarget<File>() {

    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
        val newFile = File(resource.absolutePath + ".jpg")
        resource.renameTo(newFile)
        proceedUri(FileProvider.getUriForFile(context, "com.hornedheck.vkpicshare.files", newFile))
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        // no placeholder
    }
}