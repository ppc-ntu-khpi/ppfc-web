/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain.repository

import core.domain.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun observePreferences(): Flow<Preferences>
    suspend fun getPreferences(): Preferences?
    suspend fun savePreferences(preferences: Preferences)
}