package org.kotlin.auctions.auctions.repo

import org.kotlin.auctions.auctions.entity.Bid
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
/**
 * Created by vladimir_antin on 2.8.17..
 */

@RepositoryRestResource
interface BidRepository :CrudRepository<Bid,Long>