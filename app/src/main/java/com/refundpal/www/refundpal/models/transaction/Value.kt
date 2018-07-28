package com.refundpal.www.refundpal.models.transaction

data class Value(
        val currency: String,
        val amount: String
) {
    override fun toString(): String {
        return "$$amount"
    }
}