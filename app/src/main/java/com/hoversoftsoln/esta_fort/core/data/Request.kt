package com.hoversoftsoln.esta_fort.core.data

import com.google.firebase.firestore.PropertyName

data class Request(
        @get:PropertyName("id")
        @set:PropertyName("id") var id: String = "",
        @get:PropertyName("userID")
        @set:PropertyName("userID") var userID: String = "",
        @set:PropertyName("userName")
        @get:PropertyName("userName") var userName: String = "",
        @set:PropertyName("driverTelephone")
        @get:PropertyName("driverTelephone") var driverTelephone: String = "",
        @get:PropertyName("is_completed")
        @set:PropertyName("is_completed") var iscompleted: Boolean = false,
        @get:PropertyName("is_cancelled")
        @set:PropertyName("is_cancelled") var iscancelled: Boolean = false,
        @get:PropertyName("driverID")
        @set:PropertyName("driverID") var driverID: String = "",
        @set:PropertyName("driverName")
        @get:PropertyName("driverName") var driverName: String = "",
        @get:PropertyName("date_created")
        @set:PropertyName("date_created") var dateCreated: Long = 0,
        @get:PropertyName("date_confirmed")
        @set:PropertyName("date_confirmed") var dateConfirmed: Long = 0,
        @get:PropertyName("status")
        @set:PropertyName("status") var status: Int = 0

)