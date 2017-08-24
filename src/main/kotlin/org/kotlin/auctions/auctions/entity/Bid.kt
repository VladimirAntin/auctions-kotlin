package org.kotlin.auctions.auctions.entity

import com.fasterxml.jackson.annotation.JsonBackReference

import javax.persistence.*
import java.io.Serializable
import java.util.Date

import javax.persistence.GenerationType.IDENTITY

/**
 * Created by vladimir_antin on 2.8.17..
 */

@Entity
@Table(name = "bids")
class Bid : Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "bid_id", unique = true, nullable = false)
    val id: Long = 0

    @Column(name = "price", unique = false, nullable = false)
    val price: Float = 0.toFloat()

    @Column(name = "dateTime", unique = false, nullable = false)
    val dateTime: Date? = null

    @ManyToOne
    @JoinColumn(name = "auction_id", referencedColumnName = "auction_id", nullable = false)
    @JsonBackReference
    val auction: Auction? = null

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonBackReference
    val user: User? = null

    constructor()
}
