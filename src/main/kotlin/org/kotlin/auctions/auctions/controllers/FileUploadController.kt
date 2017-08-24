package org.kotlin.auctions.auctions.controllers

import org.kotlin.auctions.auctions.storage.StorageFileNotFoundException
import org.kotlin.auctions.auctions.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class FileUploadController @Autowired
constructor(private val storageService: StorageService) {

    @GetMapping("/files/{folder}/{filename:.+}")
    @ResponseBody
    fun serveFile(@PathVariable("folder") folder: String, @PathVariable filename: String): ResponseEntity<Resource> {

        val file = storageService.loadAsResource(folder + "/" + filename)
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"")
                .body(file)
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

}
