package com.refundpal.www.refundpal.models.transaction

data class ThisAccount(
        val id: String,
        val bank_routing: BankRoutingX,
        val account_routings: List<AccountRoutingX>,
        val holders: List<Holder>
)