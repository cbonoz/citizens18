package com.refundpal.www.refundpal.util

import java.util.*

class RefundService(private val prefManager: PrefManager) {

    val random = Random()

    fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

}