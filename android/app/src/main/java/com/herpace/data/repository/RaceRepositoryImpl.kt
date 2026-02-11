package com.herpace.data.repository

import com.herpace.data.local.dao.RaceDao
import com.herpace.data.local.entity.RaceEntity
import com.herpace.data.remote.ApiResult
import com.herpace.data.remote.HerPaceApiService
import com.herpace.data.remote.dto.CreateRaceRequest
import com.herpace.data.remote.dto.RaceResponse
import com.herpace.data.remote.safeApiCall
import com.herpace.domain.model.Race
import com.herpace.domain.model.RaceDistance
import com.herpace.domain.repository.RaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RaceRepositoryImpl @Inject constructor(
    private val apiService: HerPaceApiService,
    private val raceDao: RaceDao,
    private val authTokenProvider: AuthTokenProvider
) : RaceRepository {

    override suspend fun createRace(
        name: String,
        date: String,
        distance: String,
        goalTimeMinutes: Int?
    ): ApiResult<Race> {
        val raceDistance = RaceDistance.fromApiValue(distance)
        val goalTimeSpan = goalTimeMinutes?.let { minutesToTimeSpan(it) }

        val result = safeApiCall {
            apiService.createRace(
                CreateRaceRequest(
                    raceName = name,
                    raceDate = date,
                    distance = raceDistance.distanceKm,
                    distanceType = raceDistance.ordinal,
                    goalTime = goalTimeSpan
                )
            )
        }
        return when (result) {
            is ApiResult.Success -> {
                val race = mapResponseToDomain(result.data)
                raceDao.insert(RaceEntity.fromDomain(race))
                ApiResult.Success(race)
            }
            is ApiResult.Error -> result
            is ApiResult.NetworkError -> result
        }
    }

    override suspend fun getRaces(): ApiResult<List<Race>> {
        val result = safeApiCall { apiService.getRaces() }
        return when (result) {
            is ApiResult.Success -> {
                val races = result.data.map { mapResponseToDomain(it) }
                val userId = authTokenProvider.getUserId()
                if (userId != null) {
                    raceDao.deleteAllByUserId(userId)
                    raceDao.insertAll(races.map { RaceEntity.fromDomain(it) })
                }
                ApiResult.Success(races)
            }
            is ApiResult.Error -> fallbackToCached()
            is ApiResult.NetworkError -> fallbackToCached()
        }
    }

    override fun observeRaces(): Flow<List<Race>> {
        val userId = authTokenProvider.getUserId() ?: return kotlinx.coroutines.flow.flowOf(emptyList())
        return raceDao.observeAllByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getRaceById(raceId: String): ApiResult<Race?> {
        val result = safeApiCall { apiService.getRace(raceId) }
        return when (result) {
            is ApiResult.Success -> {
                val race = mapResponseToDomain(result.data)
                raceDao.insert(RaceEntity.fromDomain(race))
                ApiResult.Success(race)
            }
            is ApiResult.Error -> {
                val cached = raceDao.getById(raceId)
                ApiResult.Success(cached?.toDomain())
            }
            is ApiResult.NetworkError -> {
                val cached = raceDao.getById(raceId)
                ApiResult.Success(cached?.toDomain())
            }
        }
    }

    override suspend fun updateRace(
        raceId: String,
        name: String,
        date: String,
        distance: String,
        goalTimeMinutes: Int?
    ): ApiResult<Race> {
        // Backend doesn't support general race editing yet - update locally only
        val existing = raceDao.getById(raceId)
            ?: return ApiResult.Error(-1, "Race not found")
        val updated = existing.copy(
            name = name,
            date = LocalDate.parse(date),
            distance = RaceDistance.fromApiValue(distance),
            goalTimeMinutes = goalTimeMinutes,
            updatedAt = Instant.now()
        )
        raceDao.insert(RaceEntity.fromDomain(updated.toDomain()))
        return ApiResult.Success(updated.toDomain())
    }

    override suspend fun deleteRace(raceId: String): ApiResult<Unit> {
        // Backend doesn't support race deletion yet - delete locally only
        raceDao.deleteById(raceId)
        return ApiResult.Success(Unit)
    }

    private suspend fun fallbackToCached(): ApiResult<List<Race>> {
        val userId = authTokenProvider.getUserId()
        return if (userId != null) {
            val cached = raceDao.getAllByUserId(userId)
            ApiResult.Success(cached.map { it.toDomain() })
        } else {
            ApiResult.Success(emptyList())
        }
    }

    private fun mapResponseToDomain(response: RaceResponse): Race {
        val goalMinutes = response.goalTime?.let { timeSpanToMinutes(it) }

        return Race(
            id = response.id,
            userId = authTokenProvider.getUserId() ?: response.runnerId ?: "",
            name = response.raceName,
            date = try {
                LocalDate.parse(response.raceDate.substringBefore("T"))
            } catch (_: Exception) {
                LocalDate.now()
            },
            distance = RaceDistance.entries.getOrElse(response.distanceType) { RaceDistance.FIVE_K },
            goalTimeMinutes = goalMinutes,
            createdAt = try {
                Instant.parse(response.createdAt ?: Instant.now().toString())
            } catch (_: Exception) {
                Instant.now()
            },
            updatedAt = Instant.now()
        )
    }

    private fun minutesToTimeSpan(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "%02d:%02d:00".format(hours, minutes)
    }

    private fun timeSpanToMinutes(timeSpan: String): Int {
        return try {
            val parts = timeSpan.split(":")
            val hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0
            hours * 60 + minutes
        } catch (_: Exception) {
            0
        }
    }
}
