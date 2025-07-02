package com.voloaccendo.androiddemo.data.repository.people

import com.voloaccendo.androiddemo.data.models.ApiInfo
import com.voloaccendo.androiddemo.data.models.ApiResponse
import com.voloaccendo.androiddemo.data.models.Dob
import com.voloaccendo.androiddemo.data.models.Location
import com.voloaccendo.androiddemo.data.models.Login
import com.voloaccendo.androiddemo.data.models.Name
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.data.models.Picture
import com.voloaccendo.androiddemo.data.models.Street
import com.voloaccendo.androiddemo.data.network.PeopleApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever
import retrofit2.Response

class PeopleRepositoryTest {

    // Using MockitoRule for cleaner mock initialization
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockPeopleApiService: PeopleApiService // Renamed for clarity

    private lateinit var peopleRepository: PeopleRepository

    @Before
    fun setUp() {
        // mockPeopleApiService is initialized by the MockitoRule
        peopleRepository = PeopleRepository(mockPeopleApiService)
    }

    @Test
    fun `fetchPeople success returns mapped domain models`() = runTest {
        // Arrange
        val countToFetch = 10
        val fakeApiPeopleList = createMockPersonList(countToFetch)
        val fakeApiResponse = ApiResponse(fakeApiPeopleList, ApiInfo(seed = "seed", results = countToFetch, page = 1, version = "1.0"))
        val fakeResponse = Response.success(fakeApiResponse)

        // This is where you mock the call:
        // When peopleApiService.getPeople(with argument 10) is called,
        // then return our predefined successfulRetrofitResponse
        whenever(mockPeopleApiService.getPeople(countToFetch)).thenReturn(fakeResponse)

        // Act
        val result = peopleRepository.getPeople()

        // Assert
        assertTrue(result.size == countToFetch)
    }

    fun createMockPersonList(count: Int): List<Person> {
        val mockList = mutableListOf<Person>()
        for (i in 1..count) {
            mockList.add(
                Person(
                    gender = if (i % 2 == 0) "female" else "male",
                    name = Name(title = "Mr", first = "John $i", last = "Doe $i"),
                    dob = Dob(
                        date = "1990-01-${String.format("%02d", i)}T00:00:00.000Z",
                        age = 30 + i
                    ),
                    email = "john.doe$i@example.com",
                    location = Location(
                        street = Street(number = "123$i", name = "Main St"),
                        city = "Anytown $i",
                        state = "Anystate",
                        postcode = (10000 + i).toString()
                    ),
                    phone = "555-010$i",
                    cell = "555-020$i",
                    picture = Picture(
                        large = "https://example.com/large$i.jpg",
                        medium = "https://example.com/medium$i.jpg",
                        thumbnail = "https://example.com/thumbnail$i.jpg"
                    ),
                    login = Login(
                        uuid = "uuid-$i",
                        username = "johndoe$i",
                        password = "password$i",
                        salt = "salt$i",
                        md5 = "md5$i",
                        sha1 = "sha1$i",
                        sha256 = "sha256$i")
                )
            )
        }
        return mockList
    }
}
