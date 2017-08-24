package org.kotlin.auctions.auctions.controllers

import io.jsonwebtoken.Claims
import org.kotlin.auctions.auctions.entity.User
import org.kotlin.auctions.auctions.repo.UserRepository
import org.kotlin.auctions.auctions.security.entity.NavItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import java.util.ArrayList

/**
 * Created by vladimir_antin on 2.8.17..
 */

@RestController
@RequestMapping(value = "/api")
class ApiController {

    @Autowired
    lateinit var userService: UserRepository

    private val ADMIN = "admin"
    private val OWNER = "owner"

    @GetMapping(value = "/me")
    fun me(request: HttpServletRequest): ResponseEntity<User> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        if (role != null) {
            val user:User = userService.findOne(java.lang.Long.parseLong(claims.subject))
            //DTO
            return ResponseEntity(user, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping(value = "/nav_items")
    fun navItems(request: HttpServletRequest): ResponseEntity<List<NavItem>> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        val navItems = ArrayList<NavItem>()
        navItems.add(NavItem("/", "Home", "home"))
        navItems.add(NavItem("/auctions", "Auctions", "shopping_cart"))
        if (role == ADMIN) {
            navItems.add(NavItem("/items", "Items", "list"))
            navItems.add(NavItem("/users", "Users", "group"))
        } else if (role == OWNER) {
            navItems.add(NavItem("/items", "Items", "list"))
        }
        navItems.add(NavItem("/users/" + claims.subject, "Profile", "person"))
        navItems.add(NavItem("/logout", "Logout", "input"))
        return ResponseEntity(navItems, HttpStatus.OK)
    }


}
