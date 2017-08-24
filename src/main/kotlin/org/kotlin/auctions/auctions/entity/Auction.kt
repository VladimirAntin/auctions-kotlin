package org.kotlin.auctions.auctions.entity

import com.fasterxml.jackson.annotation.*
import org.springframework.boot.jackson.JsonComponent

import javax.persistence.*
import java.io.Serializable
import java.util.*

import javax.persistence.GenerationType.IDENTITY

/**
 * Created by vladimir_antin on 2.8.17..
 */


@Entity
@Table(name = "auctions")
class Auction : Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "auction_id", unique = true, nullable = false)
    var id: Long = 0

    @Column(name = "startPrice", nullable = false)
    var startPrice: Float = 0.toFloat()

    @Column(name = "startDate", nullable = false)
    var startDate: Date? = null

    @Column(name = "endDate")
    var endDate: Date? = null

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", nullable = false)
    @JsonBackReference
    var item: Item? = null

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonBackReference
    var user: User? = null

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, mappedBy = "auction")
    @JsonManagedReference
    var bids:Set<Bid> = HashSet<Bid>()

    constructor()
}

