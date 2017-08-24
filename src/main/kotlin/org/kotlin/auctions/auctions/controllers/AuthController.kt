package org.kotlin.auctions.auctions.controllers

import java.util.Calendar
import java.util.Date

import javax.servlet.ServletException

import org.kotlin.auctions.auctions.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.kotlin.auctions.auctions.entity.User

/**
 * @GitHub https://github.com/VladimirAntin/jwt-angular-spring/blob/master/src/main/java/com/nibado/example/jwtangspr/UserController.java
 */
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var userService: UserRepository

    @Value("\${time-token-invalid-hours}")
    private val tokenTimeOut: Int = 0

    @PostMapping(value = "login")
    @Throws(ServletException::class)
    fun login(@RequestBody login: UserLogin): LoginResponse {
        val user:User = userService.findByEmailAndPassword(login.username!!, login.password!!) ?: throw ServletException("Invalid login")
        val jwt = Jwts.builder().setSubject(user.id.toString())
                .claim("role", user.role).setIssuedAt(Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
        if (tokenTimeOut != 0) {
            jwt.setExpiration(expirationDate)
        }
        return LoginResponse(jwt.compact())
    }

    class UserLogin {
        var username: String? = null
        var password: String? = null
    }

    class LoginResponse(var token: String)

    val expirationDate: Date
        get() {
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.add(Calendar.HOUR_OF_DAY, tokenTimeOut)
            return cal.time
        }
}
