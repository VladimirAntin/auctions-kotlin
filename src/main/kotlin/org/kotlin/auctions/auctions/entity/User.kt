package org.kotlin.auctions.auctions.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.kotlin.auctions.auctions.dto.UserDTO

import javax.persistence.*
import javax.validation.constraints.Size

import java.io.Serializable
import java.util.HashSet

import javax.persistence.GenerationType.IDENTITY

/**
 * Created by vladimir_antin on 2.8.17..
 */

@Entity
@Table(name = "users")
class User : Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    val id: Long = 0

    @Column(name = "name", unique = false, nullable = false)
    @Size(max = 30)
    var name: String? = null

    @Column(name = "email", unique = true, nullable = false)
    @Size(max = 30)
    var email: String? = null

    @Column(name = "password", unique = false, nullable = false)
    @Size(max = 10)
    @JsonIgnore
    var password: String? = null

    @Column(name = "picture", unique = false, nullable = true, columnDefinition = "TEXT")
    var picture: String? = null

    @Column(name = "address", unique = false, nullable = true, columnDefinition = "TEXT")
    var address: String? = null

    @Column(name = "phone", unique = false, nullable = true)
    @Size(max = 30)
    var phone: String? = null

    @Column(name = "role", unique = false, nullable = false)
    @Size(max = 15)
    var role: String? = null

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    val auctions:Set<Auction> = HashSet<Auction>()

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    val bids:Set<Bid> = HashSet<Bid>()

    fun fromDTO(userDTO: UserDTO): User {
        this.name = userDTO.name
        this.email = userDTO.email
        this.password = userDTO.password
        this.picture = userDTO.picture
        this.address = userDTO.address
        this.phone = userDTO.phone
        setRole(userDTO.role)
        return this
    }

    fun setRole(role: String?): User {
        if (role == "admin") {
            this.role = role
        } else if (role == "owner") {
            this.role = role
        } else {
            this.role = "bidder"
        }
        return this
    }

    constructor()
}
