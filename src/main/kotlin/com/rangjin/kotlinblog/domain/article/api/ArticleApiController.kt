package com.rangjin.kotlinblog.domain.article.api

import com.rangjin.kotlinblog.domain.article.application.ArticleService
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/article")
class ArticleApiController (
    private val articleService: ArticleService,
) {

    @PostMapping("/create")
    fun create(
        @RequestBody @Valid request: ArticleCreateOrUpdateRequestDto,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(articleService.create(request))
    }

    @PutMapping("/update/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: ArticleCreateOrUpdateRequestDto,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(articleService.update(request, id))
    }

    @DeleteMapping("/delete/{articleId}")
    fun delete(
        @PathVariable articleId: Long,
        @RequestBody @Valid request: ArticleDeleteRequestDto,
    ): ResponseEntity<Any> {
        articleService.delete(request, articleId)
        return ResponseEntity.ok().build()
    }

}