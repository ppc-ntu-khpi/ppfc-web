package core.data.dao

import core.domain.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesDao {
    fun observePreferences(): Flow<Preferences>
    fun getPreferences(): Preferences?
    suspend fun savePreferences(preferences: Preferences)
}