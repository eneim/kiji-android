package dev.kiji.core.data.hackernews.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class HackerNewsItem(
    val id: Long,
    val deleted: Boolean?,
    val type: Type?,
    @Json(name = "by") val author: String?,
    @Json(name = "time") val createdTimestamp: Long?,
    val text: String?,
    val dead: Boolean?,
    val parent: Long?,
    val poll: Long?,
    val kids: List<Long>?,
    val url: String?,
    val score: Int?,
    val title: String?,
    val parts: List<Long>?,
    val descendants: Int?,
) {

    val isDeleted: Boolean = deleted == true

    val isDead: Boolean = dead == true

    val createdTimestampMillis: Long? = createdTimestamp?.let(TimeUnit.SECONDS::toMillis)

    @JsonClass(generateAdapter = false)
    enum class Type(val value: String) {
        JOB("job"),
        STORY("story"),
        COMMENT("comment"),
        POLL("poll"),
        POLLOPT("pollopt"),
    }
}
