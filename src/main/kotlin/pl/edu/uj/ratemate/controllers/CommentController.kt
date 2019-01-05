package pl.edu.uj.ratemate.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.uj.ratemate.entities.Comment
import pl.edu.uj.ratemate.services.CommentService

@RestController
class CommentController(private val service: CommentService) {

    @GetMapping("/product/{id}/comments")
    fun getComments(@PathVariable("id") productId: Int): ResponseEntity<List<Comment>> {
        return ResponseEntity.ok(service.getComments(productId))
    }

    @PostMapping("/comment/add")
    fun addComment(@RequestBody comment: Comment): ResponseEntity.BodyBuilder {
        service.addComment(comment)
        return ResponseEntity.ok()
    }

}