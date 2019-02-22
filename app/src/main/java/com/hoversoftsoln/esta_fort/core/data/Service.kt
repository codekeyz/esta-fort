package com.hoversoftsoln.esta_fort.core.data

import com.google.firebase.firestore.PropertyName

data class Service(
        @get:PropertyName("title")
        @set:PropertyName("title") var title: String = "",
        @get:PropertyName("desc")
        @set:PropertyName("desc") var desc: String = "",
        @get:PropertyName("image")
        @set:PropertyName("image") var image: String = "",
        @set:PropertyName("issue_date")
        @get:PropertyName("issue_date") var issue_date: Long = 0
)