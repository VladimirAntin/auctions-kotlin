package org.kotlin.auctions.auctions.security

import java.io.IOException
import java.util.HashMap

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.web.filter.GenericFilterBean

import io.jsonwebtoken.Jwts
import org.kotlin.auctions.auctions.config.Sf57Utils


/**
 * @GitHub https://github.com/VladimirAntin/jwt-angular-spring/blob/master/src/main/java/com/nibado/example/jwtangspr/JwtFilter.java
 */

class JwtFilter : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest,
                          res: ServletResponse,
                          chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        val authHeader = request.getHeader("Authorization")
        if (request.method != "OPTIONS") {
            if (authHeader == null || !authHeader.startsWith("jwt ")) {
                response.sendError(403, "Missing or invalid Authorization header.")
                return
            }
            val token = authHeader.substring(4) // The part after "jwt "

            try {
                val claims = Jwts.parser().setSigningKey("secretkey")
                        .parseClaimsJws(token).body
                request.setAttribute("claims", claims)
                val link = request.requestURI
                val method = request.method
                val bidderNotAllowedLinks = object : HashMap<String, String>() {
                    init {
                        put("/api/auctions/*", "DELETE")
                        put("/api/items/*", "DELETE")
                        put("/api/users/*", "DELETE")
                        put("/api/users", "GET")
                        put("/api/users", "POST")
                        put("/api/account", "POST")
                        put("/api/items", "POST")
                        put("/api/items/*", "PUT")

                    }
                }
                val ownerNotAllowedLinks = object : HashMap<String, String>() {
                    init {
                        put("/api/users", "GET")
                        put("/api/users/*", "DELETE")
                        put("/api/users", "POST")
                        put("/api/account", "POST")
                    }
                }

                if (link == "/api/users/" + claims.subject && method=="DELETE") {
                    throw Exception()
                }
                if (claims["role"] == "bidder" && !Sf57Utils.checkRole(link, method, bidderNotAllowedLinks)) {
                    throw Exception()
                }
                if (claims["role"] == "owner" && !Sf57Utils.checkRole(link, method, ownerNotAllowedLinks)) {
                    throw Exception()
                }
            } catch (e: Exception) {
                response.sendError(401, "Invalid token.")
                return
                //throw new ServletException("Invalid token."); //500
            }

        }


        chain.doFilter(req, res)
    }


}
