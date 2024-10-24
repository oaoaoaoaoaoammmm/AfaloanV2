package org.example.afauser.utils

import org.example.afauser.models.User
import org.example.afauser.models.enumerations.Role
import java.util.*


const val USER_PASSWORD = "12345"

const val ENCODED_USER_PASSWORD = "\$2a\$10\$.IEUyyxTZjIGYnDHOcFW3e8AD5QFAKWj7nu7NM1NfBs.wE6AtC83a"

var USER = User(
    id = UUID.fromString("37790e1b-2d12-4e3e-b222-522e81e90205"),
    username = "john.doe@mail.ru",
    password = ENCODED_USER_PASSWORD,
    confirmed = true,
    confirmedUsername = true,
    blocked = false,
    role = Role.SUPERVISOR
)

val UNAUTHORIZED_USER = User(
    id = UUID.randomUUID(),
    username = "johan.do@mail.ru",
    password = "12345678",
    confirmed = false,
    confirmedUsername = false,
    blocked = false,
    role = Role.CUSTOMER
)