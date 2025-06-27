package com.voloaccendo.androiddemo.data.model

data class Person(
    val gender: String,
    val name: Name,
    val dob: Dob,
    val email: String,
    val location: Location,
    val phone: String,
    val cell: String,
    val picture: Picture,
    val login: Login
)
