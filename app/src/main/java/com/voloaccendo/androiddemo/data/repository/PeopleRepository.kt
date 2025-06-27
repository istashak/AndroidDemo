package com.voloaccendo.androiddemo.data.repository

import com.voloaccendo.androiddemo.data.model.Person
import com.voloaccendo.androiddemo.data.network.PeopleApiService

class PeopleRepository(private val peopleApiServce: PeopleApiService) : IPeopleRepository {
    private val personMap = mutableMapOf<String, Person>()
    override suspend fun getPeople(): List<Person> {
        val response = peopleApiServce.getPeople(10)
        val people = response.body()?.results

        people?.forEach { person ->
            personMap[person.login.uuid] = person
        }

        return people ?: emptyList()
    }

    override suspend fun getPerson(id: String): Person {
        return personMap[id] ?: throw Exception("Person not found")
    }
}
