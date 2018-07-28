package com.refundpal.www.refundpal.models.transaction

data class OtherAccount(
        val id: String,
        val holder: Holder,
        val bank_routing: BankRouting,
        val account_routings: List<AccountRouting>
)