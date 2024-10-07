package com.example.afaloan.configurations.generators

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RandomUUIDGenerator: IdGenerator {
    override fun generateId(): String = UUID.randomUUID().toString()
}