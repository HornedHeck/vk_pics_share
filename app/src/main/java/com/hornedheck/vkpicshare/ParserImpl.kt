package com.hornedheck.vkpicshare


class ParserImpl : Parser {

    private val postSectionRegex =
        Regex("(?<= <div class=\"pi_medias thumbs_list).+")

    private val linksRegex = Regex("(?<=a {2}href=\").+?(?=\")")

    private val imageLinkRegex =
        Regex("(?<=<div class=\"PhotoviewPage__thumb\"><a class=\"PhotoviewPage__photo\"><img src=\").+?(?=\" style)")

    override fun getPostLinks(content: String) =
        postSectionRegex.find(content)?.value
            ?.let { linksRegex.findAll(it) }
            ?.map { "https://m.vk.com" + it.value }
            ?.toList() ?: emptyList()

    override fun getImageLink(imagePost: String): String? {
        return imageLinkRegex.find(imagePost)?.value
    }
}