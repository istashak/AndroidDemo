package com.voloaccendo.androiddemo.data.repository.people

import com.voloaccendo.androiddemo.data.models.Person

interface IPeopleRepository {
    suspend fun getPeople(count: Int, page: Int = 1): List<Person>
    suspend fun getPerson(id: String): Person
}
