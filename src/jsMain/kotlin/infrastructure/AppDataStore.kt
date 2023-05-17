package infrastructure

import core.infrastructure.DataStore
import core.infrastructure.Logger
import kotlinx.browser.localStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

typealias DataStoreChangeListener = (key: String) -> Unit

class AppDataStore(name: String) : DataStore {

    private val tag = AppDataStore::class.simpleName.toString()
    private val scopeName = "$name#${name.hashCode()}"
    private var listeners = emptyList<DataStoreChangeListener>()

    private fun addListener(listener: DataStoreChangeListener) {
        kotlinx.atomicfu.locks.synchronized(this) {
            listeners = listeners + listener
        }
    }

    private fun removeListener(listener: DataStoreChangeListener) {
        kotlinx.atomicfu.locks.synchronized(this) {
            listeners = listeners - listener
        }
    }

    private fun getScopedKey(key: String) = "$scopeName:$key"

    override fun saveString(key: String, value: String) {
        kotlinx.atomicfu.locks.synchronized(this) {
            val scopedKey = getScopedKey(key)
            localStorage.setItem(scopedKey, value)

            listeners.forEach { listener ->
                listener(key)
            }
        }
    }

    override fun saveLong(key: String, value: Long) = saveString(key, value.toString())

    override fun saveBoolean(key: String, value: Boolean) = saveString(key, value.toString())

    @OptIn(InternalSerializationApi::class)
    override fun <T : Any> saveGeneric(key: String, value: T, type: KClass<T>) {
        val json = try {
            Json.encodeToString(type.serializer(), value)
        } catch (e: Exception) {
            Logger.trace(tag = tag, message = "Failed to save '$value'.")
            return
        }

        saveString(key, json)
    }

    override fun getString(key: String): String? {
        return kotlinx.atomicfu.locks.synchronized(this) {
            val scopedKey = getScopedKey(key)
            localStorage.getItem(scopedKey)
        }
    }

    override fun getLong(key: String): Long? = getString(key)?.toLongOrNull()

    override fun getBoolean(key: String): Boolean? = getString(key)?.toBooleanStrictOrNull()

    @OptIn(InternalSerializationApi::class)
    override fun <T : Any> getGeneric(key: String, type: KClass<T>): T? {
        val json = getString(key) ?: return null

        return try {
            Json.decodeFromString(type.serializer(), json)
        } catch (e: Exception) {
            Logger.trace(tag = tag, message = "Failed to get the value by key '$key'.")
            null
        }
    }

    override fun observeValueChange(key: String) = callbackFlow {
        trySend(key)

        val listener: DataStoreChangeListener = {
            if (key == key) {
                trySend(key)
            }
        }

        addListener(listener)

        awaitClose {
            removeListener(listener)
        }
    }

    override fun observeString(key: String) = observeValueChange(key).map { getString(key) }

    override fun observeLong(key: String) = observeValueChange(key).map { getLong(key) }

    override fun observeBoolean(key: String) = observeValueChange(key).map { getBoolean(key) }

    override fun <T : Any> observeGeneric(key: String, type: KClass<T>): Flow<T?> {
        return observeValueChange(key).map { getGeneric(key, type) }
    }

    override fun clear(key: String) {
        localStorage.removeItem(getScopedKey(key))

        listeners.forEach { listener ->
            listener(key)
        }
    }
}

inline fun <reified T : Any> DataStore.saveGeneric(key: String, value: T) = saveGeneric(key, value, T::class)
inline fun <reified T : Any> DataStore.getGeneric(key: String) = getGeneric(key, T::class)
inline fun <reified T : Any> DataStore.observeGeneric(key: String) = observeGeneric(key, T::class)