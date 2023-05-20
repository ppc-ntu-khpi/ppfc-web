/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.data.repository

import core.data.dao.PreferencesDao
import core.domain.model.Preferences
import core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val preferencesDao: PreferencesDao
) : PreferencesRepository {

    override fun observePreferences(): Flow<Preferences> = preferencesDao.observePreferences()

    override suspend fun getPreferences(): Preferences? {
        return preferencesDao.getPreferences()
    }

    override suspend fun savePreferences(preferences: Preferences) {
        preferencesDao.savePreferences(preferences = preferences)
    }
}