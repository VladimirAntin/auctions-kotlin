package org.kotlin.auctions.auctions.controllers.entity

import io.jsonwebtoken.Claims
import org.kotlin.auctions.auctions.config.Sf57Utils
import org.kotlin.auctions.auctions.entity.Auction
import org.kotlin.auctions.auctions.entity.User
import org.kotlin.auctions.auctions.repo.AuctionRepository
import org.kotlin.auctions.auctions.repo.ItemRepository
import org.kotlin.auctions.auctions.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import java.util.Date

/**
 * Created by vladimir_antin on 2.8.17..
 */
@RestController
@RequestMapping(value = "auctions")
class AuctionController {
    private val ADMIN = "admin"
    private val OWNER = "owner"

    @Autowired
    lateinit var auctionService: AuctionRepository

    @Autowired
    lateinit var itemService: ItemRepository

    @Autowired
    lateinit var userService: UserRepository

    @DeleteMapping(value = "/{id}")
    fun removeAuctionById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val auction = auctionService.findOne(id) ?: return ResponseEntity<Auction>(HttpStatus.NOT_FOUND)
        if (role == ADMIN || role == OWNER && auction.startDate!!.before(Date())) {
            auctionService.delete(id)
            return ResponseEntity<Auction>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Auction>(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping
    fun postAuction(@RequestBody auctionDTO: Auction, request: HttpServletRequest): ResponseEntity<Auction> {
        val claims = request.getAttribute("claims") as Claims
        val user:User = userService.findOne(Sf57Utils.long_parser(claims.subject))
        if (user != null) {
            val item = itemService.findOne(auctionDTO.item!!.id)
            if (auctionDTO.startPrice <= 0) {
                return ResponseEntity(HttpStatus.CONFLICT)
            }
            try {
                auctionDTO.user = user
                auctionDTO.item = item
                val todayAt00 = Date()
                todayAt00.hours = 0
                if (auctionDTO.startDate!!.before(todayAt00)) {
                    return ResponseEntity(HttpStatus.CONFLICT)
                }
                if (auctionDTO.endDate != null && auctionDTO.endDate!!.before(auctionDTO.startDate!!)) {
                    return ResponseEntity(HttpStatus.CONFLICT)
                }
                auctionService.save(auctionDTO)
                return ResponseEntity(auctionDTO, HttpStatus.CREATED)
            } catch (e: Exception) {
                return ResponseEntity(HttpStatus.CONFLICT) //409
            }

        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @PutMapping(value = "/{id}")
    fun updateAuctionById(@PathVariable("id") id: Long, @RequestBody auctionDTO: Auction?, request: HttpServletRequest): ResponseEntity<Auction> {
        val claims = request.getAttribute("claims") as Claims
        val user = userService.findOne(Sf57Utils.long_parser(claims.subject))
        if (auctionDTO == null) {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }
        val auction = auctionService.findOne(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if (auctionDTO.startPrice >= 0) {
            auction.startPrice = auctionDTO.startPrice
        }
        try {
            if (auction.user!!.id !== user.id || user.role !== ADMIN) {
                return ResponseEntity(HttpStatus.UNAUTHORIZED)
            }
            var endDate: Date? = null
            if (auctionDTO.endDate != null) {
                endDate = auctionDTO.endDate
            }
            val todayAt00 = Date()
            todayAt00.hours = 0
            if (!auctionDTO.startDate!!.before(todayAt00)) {
                auction.startDate = auctionDTO.startDate
            }
            if (endDate != null && endDate.after(auction.startDate!!) && endDate.after(todayAt00)) {
                auction.endDate = endDate
            } else if (endDate == null) {
                auction.endDate = null
            } else {
                throw Exception("Not greater than today")
            }
            auctionService.save(auction)
            return ResponseEntity(auction, HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.CONFLICT)
        }

    }
}
