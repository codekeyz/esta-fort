package com.hoversoftsoln.esta_fort.core.data

import com.google.firebase.firestore.PropertyName

data class Request(
        @get:PropertyName("title")
        @set:PropertyName("title") var title: String = ""
)