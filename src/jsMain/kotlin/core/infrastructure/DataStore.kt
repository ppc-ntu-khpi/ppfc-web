/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.infrastructure

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface DataStore {
    fun saveString(key: String, value: String)

    fun saveLong(key: String, value: Long)

    fun saveBoolean(key: String, value: Boolean)

    fun <T : Any> saveGeneric(key: String, value: T, type: KClass<T>)

    fun getString(key: String): String?

    fun getLong(key: String): Long?

    fun getBoolean(key: String): Boolean?

    fun <T : Any> getGeneric(key: String, type: KClass<T>): T?

    fun observeValueChange(key: String): Flow<String?>

    fun observeString(key: String): Flow<String?>

    fun observeLong(key: String): Flow<Long?>

    fun observeBoolean(key: String): Flow<Boolean?>

    fun <T : Any> observeGeneric(key: String, type: KClass<T>): Flow<T?>

    fun clear(key: String)
}