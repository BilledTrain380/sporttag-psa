package ch.schulealtendorf.psa.service.standard.web

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.File
import java.io.FileInputStream

fun buildFileResponse(file: File): ResponseEntity<InputStreamResource> {
    val headers = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_OCTET_STREAM
        contentLength = file.length()
        setContentDispositionFormData("attachment", file.name)
    }

    val inputStreamResource = InputStreamResource(FileInputStream(file))

    return ResponseEntity(inputStreamResource, headers, HttpStatus.OK)
}
