package com.refundpal.www.refundpal.models.transaction

data class Transaction(
        val id: String,
        val this_account: ThisAccount,
        val other_account: OtherAccount,
        val details: Details
)