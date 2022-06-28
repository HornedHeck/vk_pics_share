package com.hornedheck.vkpicshare

interface Parser {

    /**
     * This function extract links to separate pics from post
     * [content] link to VK post with 1+ images
     * @return list of links to images
     */
    fun getPostLinks(content : String) : List<String>

    /**
     * This function extract downloadable image link from post
     * [imagePost] content of link from [getPostLinks]
     * @return link to download image
     */
    fun getImageLink(imagePost : String) : String?

}