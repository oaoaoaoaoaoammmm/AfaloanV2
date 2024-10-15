package org.example.afauser.models

import org.example.afauser.models.enumerations.Role
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*


@Table(name = "users")
data class User(

    @Id
    val id: UUID? = null,

    @Column("username")
    val username: String,

    @Column("password")
    val password: String,

    @Column("confirmed")
    val confirmed: Boolean = false,

    @Column("confirmed_username")
    val confirmedUsername: Boolean = false,

    @Column("blocked")
    val blocked: Boolean = false,

    @Column("role")
    val role: Role = Role.CUSTOMER
)
