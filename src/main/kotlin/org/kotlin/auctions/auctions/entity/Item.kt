package org.kotlin.auctions.auctions.entity

import com.fasterxml.jackson.annotation.JsonManagedReference

import javax.persistence.*
import javax.validation.constraints.Size

import java.io.Serializable
import java.util.HashSet

import javax.persistence.GenerationType.IDENTITY

/**
 * Created by vladimir_antin on 2.8.17..
 */

@Entity
@Table(name = "items")
class Item : Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false)
    var id: Long = 0

    @Column(name = "name", unique = false, nullable = false)
    @Size(max = 30)
    var name: String? = null

    @Column(name = "description", unique = false, nullable = false, columnDefinition = "TEXT")
    var description: String? = null

    @Column(name = "picture", unique = false, nullable = true, columnDefinition = "TEXT")
    var picture: String? = null

    @Column(name = "sold", unique = false, nullable = false)
    var sold: Boolean = false

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, mappedBy = "item")
    @JsonManagedReference
    var auctions:Set<Auction> = HashSet<Auction>()

    constructor()


}

