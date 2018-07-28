package com.refundpal.www.refundpal.models

import java.util.*
import kotlin.collections.HashMap

class User(val name: String, val id: String = UUID.randomUUID().toString(), val attributes: HashMap<String, String> = HashMap(), var token: String = "") {


}