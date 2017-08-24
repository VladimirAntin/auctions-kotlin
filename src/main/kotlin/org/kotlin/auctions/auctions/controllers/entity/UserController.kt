package org.kotlin.auctions.auctions.controllers.entity

import org.kotlin.auctions.auctions.config.Sf57Utils
import org.kotlin.auctions.auctions.dto.UserDTO
import org.kotlin.auctions.auctions.entity.User
import org.kotlin.auctions.auctions.repo.UserRepository
import org.kotlin.auctions.auctions.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import io.jsonwebtoken.Claims
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.web.multipart.MultipartFile


import javax.servlet.http.HttpServletRequest

/**
 * Created by vladimir_antin on 2.8.17..
 */

@RepositoryRestController
@RequestMapping(value = "users")
class UserController {
    @Autowired
    lateinit var userService: UserRepository

    @Autowired
    lateinit var storageService: StorageService

    private val ADMIN = "admin"


    @PostMapping(value = "")
    fun post_user(@RequestBody userDTO: UserDTO): ResponseEntity<UserDTO> {
        var user: User? = null
        if (!Sf57Utils.validate(userDTO.email, 1, 30) ||
                !Sf57Utils.validate(userDTO.name, 1, 30) ||
                !Sf57Utils.validate(userDTO.password, 1, 10) ||
                !Sf57Utils.validate(userDTO.phone, 0, 30)) {
            return ResponseEntity(HttpStatus.CONFLICT) //409
        }
        try {
            userDTO.picture = "/images/profile.png"
            user = userService.save(User().fromDTO(userDTO))
            return ResponseEntity(UserDTO(user!!), HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.CONFLICT) //409
        }

    }

    @DeleteMapping(value = "/{id}")
    fun deleteUserById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val user:User = userService.findOne(id) ?: return ResponseEntity<User>(HttpStatus.NOT_FOUND)
        if (user.id == java.lang.Long.parseLong(claims.subject)) {
            return ResponseEntity<User>(HttpStatus.UNAUTHORIZED)
        }
        userService.delete(id)
        return ResponseEntity<User>(HttpStatus.NO_CONTENT)
    }

    @PutMapping(value = "/{id}")
    fun updateUserById(@PathVariable("id") id: Long, @RequestBody userDTO: UserDTO, request: HttpServletRequest): ResponseEntity<UserDTO> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val user:User = userService.findOne(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if (Sf57Utils.validate(userDTO.name, 1, 30)) {
            user.name = userDTO.name
        }
        if (Sf57Utils.validate(userDTO.phone, 0, 30)) {
            val phoneCheck:Long = Sf57Utils.long_parser(userDTO.phone)
            if (phoneCheck <= 0) {
                user.phone = userDTO.phone
            } else if (userDTO.phone == "") {
                user.phone = ""
            }
        }
        user.address = userDTO.address
        if (role == ADMIN) {
            user.setRole(userDTO.role!!)
            userService.save(user)
            return ResponseEntity(UserDTO(user), HttpStatus.OK)
        } else if (java.lang.Long.parseLong(claims.subject) == id) {
            userService.save(user)
            return ResponseEntity(UserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @PatchMapping(value = "/{id}/password")
    fun updatePassword(@PathVariable("id") id: Long, @RequestBody userDTO: UserDTO, request: HttpServletRequest): ResponseEntity<UserDTO> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val user = userService.findOne(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if (role == ADMIN) {
            if (Sf57Utils.validate(userDTO.password, 1, 10)) {
                user.password = userDTO.password
                userService.save(user)
                return ResponseEntity(UserDTO(user), HttpStatus.OK)
            }
        } else if (java.lang.Long.parseLong(claims.subject) == id) {
            if (user.password == userDTO.oldPassword && Sf57Utils.validate(userDTO.password, 1, 10)) {
                user.password = userDTO.password
                userService.save(user)
                return ResponseEntity(UserDTO(user), HttpStatus.OK)
            }
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping(value = "/{id}/upload")
    fun userFileUpload(@PathVariable("id") id: Long, @RequestParam("file") file: MultipartFile, request: HttpServletRequest): ResponseEntity<User> {
        val filename = file.originalFilename
        if (Sf57Utils.photo_formats(filename)) {
            return ResponseEntity<User>(HttpStatus.CONFLICT)
        }
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val user:User = userService.findOne(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val urlPhoto = "users/" + id.toString() + ".png"
        if (role == ADMIN) {
            storageService.store(file, urlPhoto)
            user.picture = "files/" + urlPhoto
            userService.save(user)
            return ResponseEntity<User>(HttpStatus.OK)
        } else if (java.lang.Long.parseLong(claims.subject) == id) {
            storageService.store(file, urlPhoto)
            user.picture = "files/" + urlPhoto
            userService.save(user)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }


}
