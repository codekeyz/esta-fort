package com.hoversoftsoln.esta_fort.core.data

import com.google.firebase.firestore.PropertyName

data class Service(
        @get:PropertyName("title")
        @set:PropertyName("title") var title: String = "",
        @get:PropertyName("type")
        @set:PropertyName("type") var type: Int = 0,
        @get:PropertyName("desc")
        @set:PropertyName("desc") var desc: String = "",
        @get:PropertyName("image")
        @set:PropertyName("image") var image: String = "",
        @get:PropertyName("rating")
        @set:PropertyName("rating") var rating: Int = 0
)