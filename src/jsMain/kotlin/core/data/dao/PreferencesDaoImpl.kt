package core.data.dao

import core.domain.model.Preferences
import core.infrastructure.DataStore
import infrastructure.getGeneric
import infrastructure.observeGeneric
import infrastructure.saveGeneric
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDaoImpl(
    private val dataStore: DataStore
) : PreferencesDao {

    override fun observePreferences(): Flow<Preferences> = dataStore.observeGeneric<Preferences>(
        key = DS_PREFERENCES_KEY
    ).map {
        it ?: Preferences.Empty
    }

    override fun getPreferences(): Preferences? {
        return dataStore.getGeneric(
            key = DS_PREFERENCES_KEY
        )
    }

    override suspend fun savePreferences(preferences: Preferences) {
        dataStore.saveGeneric(
            key = DS_PREFERENCES_KEY,
            value = preferences
        )
    }

    companion object {
        private const val DS_PREFERENCES_KEY = "PREFERENCES"
    }
}