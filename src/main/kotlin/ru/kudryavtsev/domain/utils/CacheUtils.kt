@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.kudryavtsev.domain.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.LoadingCache

object CacheUtils {
    @JvmStatic
    inline operator fun <K, V> LoadingCache<K, V>.set(key: K, value: V) {
        put(key, value)
    }

    @JvmStatic
    inline operator fun <K, V> Cache<K, V>.set(key: K, value: V) {
        put(key, value)
    }

    @JvmStatic
    inline fun <K, V>LoadingCache<K, V>.getOrPut(key: K, defaultValue: () -> V): V {
        val value = this[key]
        return if (value != null) {
            value
        } else {
            val answer = defaultValue()
            this[key] = answer
            answer
        }
    }

    @JvmStatic
    inline fun <K, V> Cache<K, V>.getOrPut(key: K, defaultValue: () -> V): V {
        val value = this.getIfPresent(key)
        return if (value != null) {
            value
        } else {
            val answer = defaultValue()
            this[key] = answer
            answer
        }
    }
}