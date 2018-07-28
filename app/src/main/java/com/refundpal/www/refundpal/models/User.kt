package com.refundpal.www.refundpal.models

import java.util.*

class User(val name: String, val id: String = UUID.randomUUID().toString()) {
}