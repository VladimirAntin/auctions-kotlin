package org.kotlin.auctions.auctions.config

import org.kotlin.auctions.auctions.entity.Auction
import org.kotlin.auctions.auctions.entity.Bid
import org.kotlin.auctions.auctions.entity.Item
import org.kotlin.auctions.auctions.entity.User
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter

@Configuration
class RepositoryConfig : RepositoryRestConfigurerAdapter() {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?) {
        config!!.exposeIdsFor(User::class.java)
        config.exposeIdsFor(Item::class.java)
        config.exposeIdsFor(Bid::class.java)
        config.exposeIdsFor(Auction::class.java)


    }
}