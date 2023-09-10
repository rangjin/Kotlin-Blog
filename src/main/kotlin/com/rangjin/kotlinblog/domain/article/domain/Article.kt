package com.rangjin.kotlinblog.domain.article.domain

import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.ursuuassignment.global.common.BaseTimeEntity
import javax.persistence.*

@Entity
class Article (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var content: String,

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