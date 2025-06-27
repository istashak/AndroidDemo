package com.voloaccendo.androiddemo.data.repository

import com.voloaccendo.androiddemo.data.models.Person

interface IPeopleRepository {
    suspend fun getPeople(): List<Person>
    suspend fun getPerson(id: String): Person
}
