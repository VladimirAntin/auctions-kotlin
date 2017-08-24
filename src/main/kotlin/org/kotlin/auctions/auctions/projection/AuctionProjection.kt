package org.kotlin.auctions.auctions.projection


import org.kotlin.auctions.auctions.entity.Auction
import org.kotlin.auctions.auctions.entity.Item
import org.kotlin.auctions.auctions.entity.User
import org.springframework.data.rest.core.config.Projection
import java.util.*

/**
 * Created by vladimir_antin on 2.8.17..
 */


@Projection(name = "auctions", types = arrayOf(Auction::class))
interface AuctionProjection {
    val id:Long
    val startDate:Date
    val endDate:Date
    val startPrice:Float
    val item: Item
    val user: User
}
