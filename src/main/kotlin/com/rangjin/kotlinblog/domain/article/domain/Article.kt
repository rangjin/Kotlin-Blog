package com.rangjin.kotlinblog.domain.article.domain

import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.common.BaseTimeEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Article (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "내용은 빈 칸일 수 없습니다")
    var content: String,

    @field:NotBlank(message = "제목은 빈 칸일 수 없습니다")
    var title: String,

    @ManyToOne
    val user: User,

    @OneToMany(mappedBy = "article", cascade = [CascadeType.REMOVE])
    val commentList: List<Comment>,

): BaseTimeEntity() {

    constructor(content: String, title: String, user: User): this(null, content, title, user, emptyList())

    fun update(content: String, title: String) {
        this.content = content
        this.title = title
    }

}