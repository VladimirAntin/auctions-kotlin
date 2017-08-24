package org.kotlin.auctions.auctions.controllers.entity

import io.jsonwebtoken.Claims
import org.kotlin.auctions.auctions.config.Sf57Utils
import org.kotlin.auctions.auctions.entity.Item
import org.kotlin.auctions.auctions.repo.ItemRepository
import org.kotlin.auctions.auctions.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

/**
 * Created by vladimir_antin on 2.8.17..
 */

@RestController
@RequestMapping(value = "items")
class ItemController {

    private val ADMIN = "admin"
    private val OWNER = "owner"
    @Autowired
    lateinit var itemService: ItemRepository

    @Autowired
    lateinit var storageService: StorageService


    @DeleteMapping(value = "/{id}")
    fun removeItemById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val item:Item = itemService.findOne(id) ?: return ResponseEntity<Item>(HttpStatus.NOT_FOUND)
        if (role == ADMIN || role == OWNER && !item.sold) {
            itemService.delete(id)
            return ResponseEntity<Item>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Item>(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping
    fun postItem(@RequestBody itemDTO: Item): ResponseEntity<Item> {
        var item:Item? = null
        if (!Sf57Utils.validate(itemDTO.name, 1, 30)) {
            return ResponseEntity(HttpStatus.CONFLICT)
        }
        try {
            itemDTO.sold = false
            item = itemService.save(itemDTO)
            return ResponseEntity(item, HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.CONFLICT) //409
        }

    }

    @PutMapping(value = "/{id}")
    fun updateItemById(@PathVariable("id") id: Long, @RequestBody itemDTO: Item, request: HttpServletRequest): ResponseEntity<Item> {
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val item = itemService.findOne(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if (Sf57Utils.validate(itemDTO.name, 1, 30)) {
            item.name = itemDTO.name
        }
        item.description = itemDTO.description
        item.sold = itemDTO.sold
        if (role == ADMIN || role == OWNER && !item.sold) {
            itemService.save(item)
            return ResponseEntity(item, HttpStatus.OK)
        } else if (java.lang.Long.parseLong(claims.subject) == id) {
            itemService.save(item)
            return ResponseEntity(item, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping(value = "/{id}/upload")
    fun ItemFileUpload(@PathVariable("id") id: Long, @RequestParam("file") file: MultipartFile, request: HttpServletRequest): ResponseEntity<*> {
        val filename = file.originalFilename
        if (Sf57Utils.photo_formats(filename)) {
            return ResponseEntity<Item>(HttpStatus.CONFLICT)
        }
        val claims = request.getAttribute("claims") as Claims
        val role = claims["role"] as String
        val item = itemService.findOne(id) ?: return ResponseEntity<Item>(HttpStatus.NOT_FOUND)
        val urlPhoto = "items/" + id.toString() + ".png"
        if (role == ADMIN || role == OWNER && !item.sold) {
            storageService.store(file, urlPhoto)
            item.picture = "files/" + urlPhoto
            itemService.save(item)
            return ResponseEntity<Item>(HttpStatus.OK)
        }
        return ResponseEntity<Item>(HttpStatus.UNAUTHORIZED)
    }
}
