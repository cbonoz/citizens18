package com.refundpal.www.refundpal.models.transaction

data class Details(
        val type: String,
        val description: String,
        val posted: String,
        val completed: String,
        val new_balance: NewBalance,
        val value: Value
)