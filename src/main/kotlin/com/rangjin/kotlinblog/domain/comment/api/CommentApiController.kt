package com.rangjin.kotlinblog.domain.comment.api

import com.rangjin.kotlinblog.domain.comment.application.CommentService
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/comment")
class CommentApiController (
    private val commentService: CommentService,
) {

    @PostMapping("/create/{id}")
    fun create(
        @PathVariable id: Long,
        @RequestBody @Valid request: CommentCreateOrUpdateRequestDto,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(commentService.create(request, id))
    }

    @PutMapping("/update/{articleId}/{commentId}")
    fun update(
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @RequestBody @Valid request: CommentCreateOrUpdateRequestDto,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(commentService.update(request, articleId, commentId))
    }

    @DeleteMapping("/delete/{articleId}/{commentId}")
    fun delete(
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @RequestBody @Valid request: CommentDeleteRequestDto,
    ): ResponseEntity<Any> {
        commentService.delete(request, articleId, commentId)
        return ResponseEntity.ok().build()
    }

}