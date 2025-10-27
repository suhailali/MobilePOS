package com.skegworks.mobilepos.utils

import java.util.UUID

interface UUIDGenerator {
    fun generateUUID() : String
}

class UUIDGeneratorImpl: UUIDGenerator {
    override fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}