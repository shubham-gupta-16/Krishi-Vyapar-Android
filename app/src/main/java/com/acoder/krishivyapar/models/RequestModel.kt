package com.acoder.krishivyapar.models

data class RequestModel(
    val postId: Int, val title: String, val description: String, val price: Float,
    val category: String, val subCategory: String,
    val viewsCount: Int, val favCount: Int, val createdAt: String, val status: Int,
    val extra: String, val user: UserModel, val location: LocationModel,
)
