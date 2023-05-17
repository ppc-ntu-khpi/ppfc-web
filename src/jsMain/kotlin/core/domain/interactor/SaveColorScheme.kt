package core.domain.interactor

import core.domain.Interactor
import core.domain.model.ColorSchemeMode
import core.domain.model.Preferences
import core.domain.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveColorScheme(
    private val preferencesRepository: PreferencesRepository
) : Interactor<SaveColorScheme.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val newPreferences = preferencesRepository.getPreferences() ?: Preferences.Empty
        preferencesRepository.savePreferences(
            preferences = newPreferences.copy(colorSchemeMode = params.colorSchemeMode)
        )
    }

    data class Params(val colorSchemeMode: ColorSchemeMode)
}