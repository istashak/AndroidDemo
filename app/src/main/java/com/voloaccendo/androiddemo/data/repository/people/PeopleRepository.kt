package com.voloaccendo.androiddemo.data.repository.people

import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.data.network.PeopleApiService

class PeopleRepository(private val peopleApiServce: PeopleApiService) : IPeopleRepository {
    private val personMap = mutableMapOf<String, Person>()
    override suspend fun getPeople(count: Int, page: Int): List<Person> {
        val response = peopleApiServce.getPeople(count = count, page = page)
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
