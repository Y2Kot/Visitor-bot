package ru.kudryavtsev.domain.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import ru.kudryavtsev.domain.model.StudentState
import java.time.Duration

class StudentStateService (
    initialCapacity: Int = STUDENTS_INITIAL,
    limit: Long = STUDENTS_LIMIT,
    keepInCacheDuration: Duration = Duration.ofDays(STUDENT_KEEP_IN_CACHE)
) {
    private val usersCache: Cache<Long, StudentState> = Caffeine.newBuilder()
        .initialCapacity(initialCapacity)
        .maximumSize(limit)
        .expireAfterWrite(keepInCacheDuration)
        .build()

    fun setState(key: Long, value: StudentState) {
        usersCache.put(key, value)
    }

    fun getState(key: Long): StudentState? = usersCache.getIfPresent(key)
    companion object {
        private const val STUDENTS_INITIAL = 50
        private const val STUDENTS_LIMIT = 200L
        private const val STUDENT_KEEP_IN_CACHE = 1L
    }
}
