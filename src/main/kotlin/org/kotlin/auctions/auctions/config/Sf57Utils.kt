package org.kotlin.auctions.auctions.config


import org.kotlin.auctions.auctions.dto.UserDTO
import org.kotlin.auctions.auctions.entity.User

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by vladimir_antin on 2.8.17..
 */
object Sf57Utils {

    var sdf = SimpleDateFormat("dd/MM/yyyy")
    var jsFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    fun contains(first: String, second: String): Boolean {
        return first.toLowerCase().contains(second)
    }

    fun long_parser(num: String?): Long {
        try {
            return java.lang.Long.parseLong(num)
        } catch (e: Exception) {
            return 0
        }

    }

    fun photo_formats(filename: String): Boolean {
        if (!contains(filename, ".png") &&
                !contains(filename, ".jpg") &&
                !contains(filename, ".jpeg") &&
                !contains(filename, ".gif")) {
            return true
        }
        return false
    }

    fun validate(name: String?, minLength: Long, maxLength: Long): Boolean {
        if (name != null && name.length <= maxLength && name.length >= minLength) {
            return true
        }
        return false
    }

    fun usersToDTO(users: Collection<User>): List<UserDTO> {
        val usersDTO = ArrayList<UserDTO>()
        for (user in users) {
            usersDTO.add(UserDTO(user))
        }
        return usersDTO
    }


    /**

     * @param link - link from request
     * *
     * @param method - method from request
     * *
     * @param notAllowedLinks - not allowed links (link,method)
     * *
     * @return success
     */
    fun checkRole(link: String, method: String, notAllowedLinks: HashMap<String, String>): Boolean {
        for (key in notAllowedLinks.keys) {
            if (notAllowedLinks[key].equals(method, ignoreCase = true)) {
                if (key.equals(link, ignoreCase = true)) {
                    return false
                } else if (key.endsWith("/*")) {
                    val rootLink = key.substring(link.length - 2)
                    if (rootLink.equals(link.substring(link.length - 2), ignoreCase = true)) {
                        return false
                    }
                } else if (key.contains("/*/")) {
                    val linkSplit = key.split("/*/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (link.startsWith(linkSplit[0]) && link.endsWith(linkSplit[1])) {
                        return false
                    }
                } else if (key.contains("/**")) {
                    val linkSplit = key.split("/*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (link.startsWith(linkSplit[0])) {
                        return false
                    }
                }
            }
        }
        return true
    }
}
