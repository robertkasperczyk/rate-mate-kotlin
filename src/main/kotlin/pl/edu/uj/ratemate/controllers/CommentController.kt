package pl.edu.uj.ratemate.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.uj.ratemate.dto.CommentDTO
import pl.edu.uj.ratemate.row.CommentRow
import pl.edu.uj.ratemate.services.implementation.CommentServiceImpl

@RestController
class CommentController(private val service: CommentServiceImpl) {

    @GetMapping("/product/{id}/comments")
    fun getComments(@PathVariable("id") productId: Int): ResponseEntity<List<CommentRow>> {
        return ResponseEntity.ok(service.getComments(productId))
    }

    @PostMapping("/product/{productId}/comment/add")
    @ResponseStatus(value = HttpStatus.OK)
    fun addComment(
            @PathVariable("productId") productId: Int,
            @RequestBody comment: CommentDTO) {
        service.addComment(productId, comment)
    }

    @PostMapping("/product/{productId}/comment/{commentId}/edit")
    @ResponseStatus(value = HttpStatus.OK)
    fun editComment(@PathVariable("productId") productId: Int,
                    @PathVariable("commentId") commentId: Int,
                    @RequestBody comment: CommentDTO) {
        service.editComment(productId, commentId, comment)
    }

    @PostMapping("/comment/{id}/delete")
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteComment(@PathVariable("id") id: Int) {
        service.deleteComment(id)
    }

}