package dev.kiji.core.data.hackernews.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HackerNewsUser(
    val id: String,
    @Json(name = "created") val createdTimestamp: Long,
    val karma: Int,
    val about: String?,
    val submitted: List<Long>?,
)
