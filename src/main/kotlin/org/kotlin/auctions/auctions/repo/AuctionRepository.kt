package org.kotlin.auctions.auctions.repo

import org.kotlin.auctions.auctions.entity.Auction
import org.kotlin.auctions.auctions.projection.AuctionProjection
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
/**
 * Created by vladimir_antin on 2.8.17..
 */

@RepositoryRestResource(excerptProjection = AuctionProjection::class)
interface AuctionRepository : CrudRepository<Auction,Long>