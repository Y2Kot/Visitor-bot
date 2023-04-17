package ru.kudryavtsev.domain.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import ru.kudryavtsev.domain.model.BaseUserState
import java.time.Duration

class UserStateService (
    initialCapacity: Int = USERS_INITIAL,
    limit: Long = USERS_LIMIT,
    keepInCacheDuration: Duration = Duration.ofDays(USERS_KEEP_IN_CACHE_DAYS)
) {
    private val usersCache: Cache<Long, BaseUserState> = Caffeine.newBuilder()
        .initialCapacity(initialCapacity)
        .maximumSize(limit)
        .expireAfterWrite(keepInCacheDuration)
        .build()

    fun setState(key: Long, value: BaseUserState) {
        usersCache.put(key, value)
    }

    fun getState(key: Long): BaseUserState? = usersCache.getIfPresent(key)
    companion object {
        private const val USERS_INITIAL = 50
        private const val USERS_LIMIT = 2_000L
        private const val USERS_KEEP_IN_CACHE_DAYS = 1L
    }
}
