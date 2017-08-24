package org.kotlin.auctions.auctions.dto

import org.kotlin.auctions.auctions.entity.User
import java.io.Serializable
/**
 * Created by vladimir_antin on 2.8.17..
 */

class UserDTO : Serializable {
    var id: Long = 0
    var name: String? = null
    var email: String? = null
    var oldPassword: String? = null
    var password: String? = null
    var picture: String? = null
    var address: String? = null
    var phone: String? = null
    var role: String? = null

    constructor() {}

    constructor(user: User) {
        this.id = user.id!!
        this.name = user.name
        this.email = user.email
        this.picture = user.picture
        this.address = user.address
        this.phone = user.phone
        this.role = user.role
    }

}
