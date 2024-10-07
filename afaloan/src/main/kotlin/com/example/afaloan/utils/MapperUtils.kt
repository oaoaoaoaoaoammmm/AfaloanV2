package com.example.afaloan.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper: ObjectMapper = jacksonObjectMapper()
    .findAndRegisterModules()

inline fun <reified T> String.toObject(): T = mapper.readValue(this, object : TypeReference<T>() {})

fun Any.toJson(): String = mapper.writeValueAsString(this)