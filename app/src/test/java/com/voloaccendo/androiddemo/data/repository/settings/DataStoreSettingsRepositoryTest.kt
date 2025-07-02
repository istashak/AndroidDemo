package com.voloaccendo.androiddemo.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.voloaccendo.androiddemo.data.models.ThemeLighting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
// @RunWith(MockitoJUnitRunner::class) // Or use the rule
class DataStoreSettingsRepositoryTest {

    @get:Rule // Example using MockitoRule
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.WARN)

    private lateinit var repository: DataStoreSettingsRepository
    private lateinit var testDataStore: DataStore<Preferences>
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())
    private lateinit var tempFile: File

    @Before
    fun setUp() {
        tempFile = File.createTempFile("test_settings_pref", ".preferences_pb")
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tempFile }
        )

        // Directly pass the testDataStore
        repository = DataStoreSettingsRepository(testDataStore)
    }

    @After
    fun tearDown() {
        if (::tempFile.isInitialized && tempFile.exists()) {
            tempFile.delete()
        }
        // testScope.cancel() // Good practice
    }

    // ... your tests remain largely the same
    @Test
    fun `initial settings should have SYSTEM themeLighting`() = testScope.runTest {
        val settings = repository.settings.first()
        assert(settings.themeLighting == ThemeLighting.SYSTEM) {
            "Expected SYSTEM theme, but got ${settings.themeLighting}"
        }
    }

    @Test
    fun `setThemeLighting updates DataStore and settings flow`() = testScope.runTest {
        repository.setThemeLightingSettings(ThemeLighting.DARK) // Assuming you have direct setThemeLightingSettings
        // or use setSettings as before

        val updatedSettings = repository.settings.first()
        assert(updatedSettings.themeLighting == ThemeLighting.DARK) {
            "Expected DARK theme, but got ${updatedSettings.themeLighting}"
        }
    }
}
